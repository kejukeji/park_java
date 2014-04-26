/**
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.broadleafcommerce.inventory.service.workflow;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.broadleafcommerce.core.catalog.domain.Sku;
import org.broadleafcommerce.core.checkout.service.workflow.CheckoutContext;
import org.broadleafcommerce.core.checkout.service.workflow.CheckoutSeed;
import org.broadleafcommerce.core.order.domain.FulfillmentGroup;
import org.broadleafcommerce.core.order.domain.LocationedItem;
import org.broadleafcommerce.core.order.domain.OrderItem;
import org.broadleafcommerce.core.order.domain.SkuAccessor;
import org.broadleafcommerce.core.workflow.BaseActivity;
import org.broadleafcommerce.core.workflow.state.ActivityStateManagerImpl;
import org.broadleafcommerce.inventory.domain.FulfillmentLocation;
import org.broadleafcommerce.inventory.exception.ConcurrentInventoryModificationException;
import org.broadleafcommerce.inventory.exception.InventoryUnavailableException;
import org.broadleafcommerce.inventory.service.FulfillmentLocationService;
import org.broadleafcommerce.inventory.service.InventoryService;

import com.ssbusy.core.account.domain.MyCustomer;
import com.ssbusy.core.domain.MyAddress;
import com.ssbusy.core.region.domain.Region;

/**
 * Default workflow activity to decrement inventory. This only attempts to
 * decrement inventory from the default fulfillment location. If you have
 * specific requirements for additional fulfillment locations, please implement
 * this in a way that works for you.
 * 
 * @author Kelly Tisdell
 */
public class DecrementInventoryActivity extends BaseActivity<CheckoutContext> {

	@Resource(name = "blInventoryService")
	protected InventoryService inventoryService;

	@Resource(name = "blFulfillmentLocationService")
	protected FulfillmentLocationService fulfillmentLocationService;

	protected Integer maxRetries = 5;

	public DecrementInventoryActivity() {
		super.setAutomaticallyRegisterRollbackHandler(false);
	}

	@Override
	public CheckoutContext execute(CheckoutContext context) throws Exception {

		CheckoutSeed seed = context.getSeedData();

		List<OrderItem> orderItems = seed.getOrder().getOrderItems();

		// 订单收货校区
		Region orderRegion = null;
		List<FulfillmentGroup> fulfillmentGroups = seed.getOrder()
				.getFulfillmentGroups();
		if (fulfillmentGroups != null && !fulfillmentGroups.isEmpty()) {
			MyAddress address = (MyAddress) fulfillmentGroups.get(0)
					.getAddress();
			if (address != null)
				orderRegion = address.getDormitory().getAreaAddress()
						.getRegion();
		}
		if (orderRegion == null) {
			orderRegion = ((MyCustomer) seed.getOrder().getCustomer())
					.getRegion();
		}

		// map to hold skus and quantity purchased, for each location
		Map<FulfillmentLocation, Map<Sku, Integer>> skuInventoryMap = new HashMap<FulfillmentLocation, Map<Sku, Integer>>();

		for (OrderItem orderItem : orderItems) {
			Sku sku = null;
			if (orderItem instanceof SkuAccessor) {
				sku = ((SkuAccessor) orderItem).getSku();
			}
			FulfillmentLocation location = null;
			if (orderItem instanceof LocationedItem) {
				location = ((LocationedItem) orderItem).getLocation();
			}
			if (location == null) {
				location = orderRegion.getFulfillmentLocations().get(0); // TODO
			} else if (!orderRegion.getFulfillmentLocations()
					.contains(location)) {
				// 配送地不对
				String errorMessage = "抱歉商品 [" + sku.getName() + "] 的配送地 ["
						+ location.getName() + "] <br/>与收货校区 ["
						+ orderRegion.getRegionName() + "] 不一致，无法送达.";
				throw new InventoryUnavailableException(errorMessage);
			}

			Map<Sku, Integer> skuMap = skuInventoryMap.get(location);
			if (skuMap == null) {
				skuInventoryMap.put(location,
						skuMap = new HashMap<Sku, Integer>());
			}
			skuMap.put(sku, orderItem.getQuantity());
		}

		// There is a retry policy set in case of concurrent update exceptions
		// where several
		// requests would try to update the inventory at the same time. The call
		// to decrement inventory,
		// by default creates a new transaction because repeatable reads would
		// occur if it were called
		// inside of the same transaction. Essentially, we want to try to
		// transactionally decrement the
		// inventory, but if it fails due to locking, then we need to leave the
		// transaction and re-read
		// the data to ensure repeatable reads don't prevent us from getting the
		// freshest data. The
		// retry count is in place to handle higher concurrency situations where
		// there may be more than one
		// failure.

		for (Map.Entry<FulfillmentLocation, Map<Sku, Integer>> entry : skuInventoryMap
				.entrySet()) {
			FulfillmentLocation location = entry.getKey();
			int retryCount = 0;
			while (retryCount < maxRetries) {
				try {
					if (location == null) {
						inventoryService.decrementInventory(entry.getValue());
						inventoryService.decrementInventoryOnHand(entry
								.getValue());
						break;
					} else {
						inventoryService.decrementInventory(entry.getValue(),
								location);
						inventoryService.decrementInventoryOnHand(
								entry.getValue(), location);
						break;
					}
				} catch (ConcurrentInventoryModificationException ex) {
					retryCount++;
					if (retryCount >= maxRetries) {
						// maximum number of retries has been reached, bubble up
						// exception
						throw ex;
					}
				}
			}
		}

		if (getRollbackHandler() != null
				&& !getAutomaticallyRegisterRollbackHandler()) {
			Map<String, Object> myState = new HashMap<String, Object>();
			if (getStateConfiguration() != null
					&& !getStateConfiguration().isEmpty()) {
				myState.putAll(getStateConfiguration());
			}

			HashMap<FulfillmentLocation, InventoryState> states = new HashMap<FulfillmentLocation, InventoryState>();
			for (Map.Entry<FulfillmentLocation, Map<Sku, Integer>> entry : skuInventoryMap
					.entrySet()) {
				FulfillmentLocation location = entry.getKey();
				InventoryState state = new InventoryState();
				state.setFulfillmentLocation(location);
				state.setSkuQuantityMap(entry.getValue());
				states.put(location, state);
			}
			myState.put(
					InventoryRollbackHandler.ROLLBACK_BLC_INVENTORY_DECREMENTED,
					states);
			myState.put(InventoryRollbackHandler.ROLLBACK_BLC_ORDER_ID, seed
					.getOrder().getId());
			ActivityStateManagerImpl.getStateManager()
					.registerState(this, context, getRollbackRegion(),
							getRollbackHandler(), myState);
		}
		return context;

	}

	public void setMaxRetries(Integer maxRetries) {
		this.maxRetries = maxRetries;
	}

}
