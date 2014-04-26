/**
 * sudaw copy right 1.0 
 */
package org.broadleafcommerce.core.checkout.service.workflow;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.broadleafcommerce.core.offer.domain.Adjustment;
import org.broadleafcommerce.core.offer.domain.FulfillmentGroupAdjustment;
import org.broadleafcommerce.core.offer.domain.Offer;
import org.broadleafcommerce.core.offer.domain.OfferCode;
import org.broadleafcommerce.core.offer.domain.OrderAdjustment;
import org.broadleafcommerce.core.offer.domain.OrderItemAdjustment;
import org.broadleafcommerce.core.offer.domain.OrderItemPriceDetailAdjustment;
import org.broadleafcommerce.core.order.domain.FulfillmentGroup;
import org.broadleafcommerce.core.order.domain.Order;
import org.broadleafcommerce.core.order.domain.OrderItem;
import org.broadleafcommerce.core.order.domain.OrderItemPriceDetail;

import com.ssbusy.core.offer.service.MyOfferService;

/**
 * 
 * 
 * MyCompleteOrderActivity.java
 * 
 * @author Ju
 */
public class MyCompleteOrderActivity extends CompleteOrderActivity {

	@Resource(name = "blOfferService")
	private MyOfferService offerService;

	@Override
	public CheckoutContext execute(CheckoutContext context) throws Exception {

		CheckoutSeed seed = context.getSeedData();

		// 统计OfferCode用量 FIXME 退回用量，还没做！
		Order order = seed.getOrder();
		List<OfferCode> offerCodes = order.getAddedOfferCodes();

		if (offerCodes != null && offerCodes.size() > 0) {
			List<Adjustment> adjs = getAllAdjustments(order);
			Set<Offer> offers = new HashSet<Offer>(adjs.size());
			if (adjs != null && !adjs.isEmpty())
				for (Adjustment adj : adjs) {
					offers.add(adj.getOffer());
				}
			if (offers.size() > 0)
				for (OfferCode offerCode : offerCodes) {
					if (offerCode.getMaxUses() > 0 // 需要用量限制
							&& offers.contains(offerCode.getOffer())) {
						offerService.incrementOfferCodeUsage(offerCode);
					}
				}
		}

		super.execute(context);

		return context;
	}

	/**
	 * @param order
	 * @return
	 */
	private List<Adjustment> getAllAdjustments(Order order) {
		List<Adjustment> adjs = new ArrayList<Adjustment>();
		List<OrderAdjustment> orderAdjs = order.getOrderAdjustments();
		if (orderAdjs != null)
			adjs.addAll(orderAdjs);

		List<OrderItem> orderItems = order.getOrderItems();
		if (orderItems != null) {
			for (OrderItem orderItem : orderItems) {
				List<OrderItemAdjustment> itemAdjs = orderItem
						.getOrderItemAdjustments();
				if (itemAdjs != null)
					adjs.addAll(itemAdjs);
				List<OrderItemPriceDetail> priceDetails = orderItem
						.getOrderItemPriceDetails();
				if (priceDetails != null)
					for (OrderItemPriceDetail detail : priceDetails) {
						List<OrderItemPriceDetailAdjustment> pdAdjs = detail
								.getOrderItemPriceDetailAdjustments();
						if (pdAdjs != null)
							adjs.addAll(pdAdjs);
					}
			}
		}
		List<FulfillmentGroup> fulfillmentGroups = order.getFulfillmentGroups();
		if (fulfillmentGroups != null) {
			for (FulfillmentGroup fg : fulfillmentGroups) {
				List<FulfillmentGroupAdjustment> fgAdjs = fg
						.getFulfillmentGroupAdjustments();
				if (fgAdjs != null)
					adjs.addAll(fgAdjs);
			}
		}
		return adjs;
	}

}
