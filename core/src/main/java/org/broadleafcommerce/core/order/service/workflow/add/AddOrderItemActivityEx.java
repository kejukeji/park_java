/**
 * sudaw copy right 1.0 
 */
package org.broadleafcommerce.core.order.service.workflow.add;

import org.broadleafcommerce.core.order.domain.DiscreteOrderItem;
import org.broadleafcommerce.core.order.domain.OrderItem;
import org.broadleafcommerce.core.order.service.workflow.CartOperationContext;

/**
 * AddOrderItemActivityEx.java
 * 
 * @author jamesp
 */
public class AddOrderItemActivityEx extends AddOrderItemActivity {

	@Override
	public CartOperationContext execute(CartOperationContext context)
			throws Exception {
		context = super.execute(context);
		OrderItem item = context.getSeedData().getAddedOrderItem();
		if (item != null && item instanceof DiscreteOrderItem) {
			DiscreteOrderItem doi = (DiscreteOrderItem) item;
			if (doi.getProduct() == null) {
				doi.setProduct(doi.getSku().getProduct() == null ? doi.getSku()
						.getDefaultProduct() : doi.getSku().getProduct());
			}
		}
		return context;
	}

}
