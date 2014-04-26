/**
 * sudaw copy right 1.0 
 */
package org.broadleafcommerce.core.order.service;

import javax.annotation.Resource;

import org.broadleafcommerce.core.order.domain.DiscreteOrderItem;
import org.broadleafcommerce.core.order.domain.LocationedItem;
import org.broadleafcommerce.core.order.domain.OrderItem;
import org.broadleafcommerce.core.order.service.call.AbstractOrderItemRequest;
import org.broadleafcommerce.core.order.service.call.OrderItemRequestDTO;
import org.broadleafcommerce.inventory.service.FulfillmentLocationService;

/**
 * OrderItemServiceImplEx.java
 * 
 * @author jamesp
 */
public class OrderItemServiceImplEx extends OrderItemServiceImpl {
	@Resource(name = "blFulfillmentLocationService")
	protected FulfillmentLocationService fulfillmentLocationService;

	@Override
	public OrderItemRequestDTO buildOrderItemRequestDTOFromOrderItem(
			OrderItem item) {
		OrderItemRequestDTO ret = super
				.buildOrderItemRequestDTOFromOrderItem(item);
		if (item instanceof LocationedItem) {
			ret.getItemAttributes().put(
					LocationedItem.LOCATIONED_ITEM_LOCATION_ID,
					"" + ((LocationedItem) item).getLocation().getId());
		}
		return ret;
	}

	@Override
	protected void populateDiscreteOrderItem(DiscreteOrderItem item,
			AbstractOrderItemRequest itemRequest) {
		super.populateDiscreteOrderItem(item, itemRequest);
		if (item instanceof LocationedItem) {
			String locId = itemRequest.getItemAttributes().get(
					LocationedItem.LOCATIONED_ITEM_LOCATION_ID);
			if (locId != null) {
				((LocationedItem) item).setLocation(fulfillmentLocationService
						.readById(Long.valueOf(locId)));
			}
		}
	}

}
