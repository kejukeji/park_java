package com.ssbusy.admin.controller.order;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.broadleafcommerce.core.order.domain.Order;
import org.broadleafcommerce.core.order.domain.OrderItem;
import org.broadleafcommerce.core.order.service.OrderItemService;
import org.broadleafcommerce.core.order.service.exception.RemoveFromCartException;
import org.broadleafcommerce.core.pricing.service.exception.PricingException;
import org.broadleafcommerce.inventory.exception.ConcurrentInventoryModificationException;
import org.broadleafcommerce.inventory.service.InventoryService;
import org.broadleafcommerce.openadmin.web.controller.AdminAbstractController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ssbusy.core.account.domain.MyCustomer;
import com.ssbusy.core.myorder.service.MyOrderService;

@Controller("ssbAdminOrderProcessController")
public class AdminOrderProcessController extends AdminAbstractController {

	@Resource(name = "ssbMyOrderService")
	protected MyOrderService myOrderService;

	@Resource(name = "blOrderItemService")
	protected OrderItemService orderItemService;

	@Resource(name = "blInventoryService")
	protected InventoryService inventoryService;

	protected static final String updateOrder = "order/update-order";
	private static String orderComplete = "redirect:/order-complete";
	private static String COMPLETE = "order-complete";

	@RequestMapping("/order-process/update")
	public String updateOrderView(HttpServletRequest request,
			HttpServletResponse response, Model model,
			@RequestParam("id") Long orderId) {
		super.setModelAttributes(model, COMPLETE);
		Order order = myOrderService.loadOrderByOrderId(orderId);
		if (order == null) {
			return orderComplete;
		} else {
			if (order.getOrderItems().size() <= 0) {
				myOrderService.cancelOrder(order);
				return orderComplete;
			} else
				model.addAttribute("order", order);
		}
		return updateOrder;
	}

	@RequestMapping("/order-process/removeOrderitem")
	public String removeOrderitem(HttpServletRequest request,
			HttpServletResponse response, Model model,
			@RequestParam("orderItemId") Long orderItemId,
			@RequestParam("quantity") long quantity) throws IOException,
			RemoveFromCartException, PricingException,
			ConcurrentInventoryModificationException {
		super.setModelAttributes(model, COMPLETE);
		OrderItem orderItem = orderItemService.readOrderItemById(orderItemId);
		if (orderItem == null) {
			return orderComplete;
		}
		Order order = orderItem.getOrder();
		myOrderService.cancelOrderItem(orderItem,
				(MyCustomer) order.getCustomer(), quantity);
		// order = myOrderService.save(order, true);
		model.addAttribute("order", order);
		return updateOrder;
	}

	@RequestMapping("/order-process/cancel/{orderNumber}")
	public String candelOrder(@PathVariable("orderNumber") String orderNumber)
			throws PricingException, ConcurrentInventoryModificationException {
		Order order = myOrderService.findOrderByOrderNumber(orderNumber);
		if (order != null) {
			myOrderService.cancelMyOrder(order,
					(MyCustomer) order.getCustomer());
		}
		return orderComplete;
	}

	@RequestMapping("/order-process/search")
	public String search(HttpServletRequest request,
			HttpServletResponse response, Model model,
			@RequestParam("id") Long orderItemId) {
		return null;
	}
}
