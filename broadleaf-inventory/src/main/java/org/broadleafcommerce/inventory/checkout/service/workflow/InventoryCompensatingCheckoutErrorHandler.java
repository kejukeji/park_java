/**
 * Copyright 2012 the original author or authors.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.broadleafcommerce.inventory.checkout.service.workflow;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.broadleafcommerce.core.catalog.domain.Sku;
import org.broadleafcommerce.core.checkout.service.workflow.CheckoutContext;
import org.broadleafcommerce.core.checkout.service.workflow.CheckoutSeed;
import org.broadleafcommerce.core.workflow.DefaultErrorHandler;
import org.broadleafcommerce.core.workflow.ErrorHandler;
import org.broadleafcommerce.core.workflow.ProcessContext;
import org.broadleafcommerce.core.workflow.WorkflowException;
import org.broadleafcommerce.inventory.domain.FulfillmentLocation;
import org.broadleafcommerce.inventory.exception.ConcurrentInventoryModificationException;
import org.broadleafcommerce.inventory.service.InventoryService;
import org.springframework.stereotype.Component;

/**
 * This error handler essentially does exactly what the
 * {@link DefaultErrorHandler} does,
 * but it also attempts to detect whether inventory was decremented during the
 * workflow. If
 * so, it attampts to compensate for that inventory.
 * 
 * @author Kelly Tisdell
 * @deprecated Use InventoryRollbackHandler
 */
@Deprecated
@Component("blInventoryCompensatingCheckoutErrorHandler")
public class InventoryCompensatingCheckoutErrorHandler implements ErrorHandler {

	private static final Log LOG = LogFactory
			.getLog(InventoryCompensatingCheckoutErrorHandler.class);

	@Resource(name = "blInventoryService")
	protected InventoryService inventoryService;

	@SuppressWarnings("unused")
	private String name;

	private final int maxRetries = 5;

	@Override
	public void setBeanName(String name) {
		this.name = name;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void handleError(ProcessContext context, Throwable th)
			throws WorkflowException {
		context.stopProcess();

		CheckoutSeed seed = ((CheckoutContext) context).getSeedData();
		// The DecrementInventoryActivity, if successful, adds the inventory
		// that was decremented to the context. If we
		// find that, then we need to attempt to compensate for that inventory.
		if (seed.getUserDefinedFields() != null
				&& seed.getUserDefinedFields().get("BLC_INVENTORY_DECREMENTED") != null) {
			Map<FulfillmentLocation, Map<Sku, Integer>> inventoryToIncrement = (Map<FulfillmentLocation, Map<Sku, Integer>>) seed
					.getUserDefinedFields().get("BLC_INVENTORY_DECREMENTED");
			if (!inventoryToIncrement.isEmpty()) {
				for (Map.Entry<FulfillmentLocation, Map<Sku, Integer>> entry : inventoryToIncrement
						.entrySet()) {
					FulfillmentLocation location = entry.getKey();
					int retryCount = 0;
					while (retryCount < maxRetries) {
						try {
							if (location == null) {
								inventoryService.incrementInventory(entry
										.getValue());
								inventoryService.incrementInventoryOnHand(entry
										.getValue());
							} else {
								inventoryService.incrementInventory(
										entry.getValue(), location);
								inventoryService.incrementInventoryOnHand(
										entry.getValue(), location);
							}
							break;
						} catch (ConcurrentInventoryModificationException ex) {
							retryCount++;
							if (retryCount == maxRetries) {
								LOG.error(
										"After an exception was encountered during checkout, where inventory was decremented. "
												+ maxRetries
												+ " attempts were made to compensate, "
												+ "but were unsuccessful for order ID: "
												+ seed.getOrder().getId()
												+ ". This should be corrected manually!",
										ex);
							}
						} catch (RuntimeException ex) {
							LOG.error(
									"An unexpected error occured in the error handler of the checkout workflow trying to compensate for inventory. This happend for order ID: "
											+ seed.getOrder().getId()
											+ ". This should be corrected manually!",
									ex);
							break;
						}
					}
				}
			}
		}

		LOG.error("An error occurred during the workflow", th);
		throw new WorkflowException(th);
	}

}
