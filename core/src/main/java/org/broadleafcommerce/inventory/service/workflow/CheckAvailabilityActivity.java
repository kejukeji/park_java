/*
 * Copyright 2008-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.broadleafcommerce.inventory.service.workflow;

import java.util.List;

import javax.annotation.Resource;

import org.broadleafcommerce.core.catalog.domain.Sku;
import org.broadleafcommerce.core.catalog.service.CatalogService;
import org.broadleafcommerce.core.order.domain.FulfillmentGroup;
import org.broadleafcommerce.core.order.domain.LocationedItem;
import org.broadleafcommerce.core.order.domain.OrderItem;
import org.broadleafcommerce.core.order.domain.SkuAccessor;
import org.broadleafcommerce.core.order.service.OrderItemService;
import org.broadleafcommerce.core.order.service.workflow.CartOperationContext;
import org.broadleafcommerce.core.order.service.workflow.CartOperationRequest;
import org.broadleafcommerce.core.workflow.BaseActivity;
import org.broadleafcommerce.inventory.domain.FulfillmentLocation;
import org.broadleafcommerce.inventory.exception.InventoryUnavailableException;
import org.broadleafcommerce.inventory.service.FulfillmentLocationService;
import org.broadleafcommerce.inventory.service.InventoryService;

import com.ssbusy.core.account.domain.MyCustomer;
import com.ssbusy.core.domain.MyAddress;
import com.ssbusy.core.region.domain.Region;

public class CheckAvailabilityActivity extends
		BaseActivity<CartOperationContext> {

	@Resource(name = "blCatalogService")
	protected CatalogService catalogService;

	@Resource(name = "blOrderItemService")
	protected OrderItemService orderItemService;

	@Resource(name = "blInventoryService")
	protected InventoryService inventoryService;

	@Resource(name = "blFulfillmentLocationService")
	protected FulfillmentLocationService fulfillmentLocationService;

	public CartOperationContext execute(CartOperationContext context)
			throws Exception {

		FulfillmentLocation itemLocation = null;

		CartOperationRequest request = context.getSeedData();
		Long skuId = request.getItemRequest().getSkuId();
		Sku sku = null;
		if (skuId != null) {
			sku = catalogService.findSkuById(skuId);
		} else {
			OrderItem orderItem = orderItemService.readOrderItemById(request
					.getItemRequest().getOrderItemId());
			if (orderItem instanceof SkuAccessor) {
				sku = ((SkuAccessor) orderItem).getSku();
				request.getItemRequest().setSkuId(sku.getId());
				skuId = sku.getId();
			}
			if (orderItem instanceof LocationedItem) {
				itemLocation = ((LocationedItem) orderItem).getLocation();
			}
		}
		if (itemLocation == null) {
			String locId = request.getItemRequest().getItemAttributes()
					.get(LocationedItem.LOCATIONED_ITEM_LOCATION_ID);
			if (locId != null) {
				itemLocation = fulfillmentLocationService.readById(Long
						.valueOf(locId));
			}
		}
		if (itemLocation == null) {
			String errorMessage = "抱歉，未指定商品 [" + sku.getName() + "] 的配送地.";
			throw new InventoryUnavailableException(errorMessage);
		}

		// 订单收货校区
		Region orderRegion = null;
		List<FulfillmentGroup> fulfillmentGroups = request.getOrder()
				.getFulfillmentGroups();
		if (fulfillmentGroups != null && !fulfillmentGroups.isEmpty()) {
			MyAddress address = (MyAddress) fulfillmentGroups.get(0)
					.getAddress();
			if (address != null)
				orderRegion = address.getDormitory().getAreaAddress()
						.getRegion();
		}
		if (orderRegion == null) {
			orderRegion = ((MyCustomer) request.getOrder().getCustomer())
					.getRegion();
		}
		if (!orderRegion.getFulfillmentLocations().contains(itemLocation)) {
			String errorMessage = "抱歉，商品 [" + sku.getName() + "] 的配送地 ["
					+ itemLocation.getName() + "] <br/>与收货校区 ["
					+ orderRegion.getRegionName() + "] 不一致，无法送达.";
		}

		// Available inventory will not be decremented for this sku until
		// checkout. This activity is assumed to be
		// part of the add to cart / update cart workflow
		boolean quantityAvailable = false;

		quantityAvailable = inventoryService.isQuantityAvailable(sku, request
				.getItemRequest().getQuantity(), itemLocation);
		if (!quantityAvailable) {
			String errorMessage = "抱歉，商品 [" + sku.getName() + "] 库存不足 "
					+ request.getItemRequest().getQuantity() + " 个了.";
			throw new InventoryUnavailableException(errorMessage);
		}

		return context;
	}

}
