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

package com.ssbusy.controller.account;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.broadleafcommerce.core.catalog.domain.Product;
import org.broadleafcommerce.core.catalog.domain.Sku;
import org.broadleafcommerce.core.order.domain.Order;
import org.broadleafcommerce.core.order.domain.OrderItem;
import org.broadleafcommerce.core.order.domain.SkuAccessor;
import org.broadleafcommerce.core.order.service.type.OrderStatus;
import org.broadleafcommerce.core.pricing.service.exception.PricingException;
import org.broadleafcommerce.core.web.controller.account.BroadleafOrderHistoryController;
import org.broadleafcommerce.inventory.exception.ConcurrentInventoryModificationException;
import org.broadleafcommerce.profile.core.service.CountryService;
import org.broadleafcommerce.profile.core.service.StateService;
import org.broadleafcommerce.profile.web.core.CustomerState;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ssbusy.core.account.domain.MyCustomer;
import com.ssbusy.core.myorder.domain.MyOrderStatus;
import com.ssbusy.core.myorder.service.MyOrderService;

@Controller
public class OrderHistoryController extends BroadleafOrderHistoryController {
	private static final Log LOG = LogFactory.getLog(OrderHistoryController.class);

	@Resource(name = "blStateService")
	StateService stateService;

	@Resource(name = "blCountryService")
	CountryService countryService;

	@Resource(name = "ssbMyOrderService")
	protected MyOrderService myOrderService;

	/**
	 * @param status
	 *            参考MyOrderStatus，额外一个：
	 *            'PROCESSING'表示'SUBMITTED'后'COMPLETED'前所有的状态
	 * @return
	 */
	private List<Order> listOrdersByStatus(String status) {
		OrderStatus stats = MyOrderStatus.getInstance(status);
		if (stats != null)
			return orderService.findOrdersForCustomer(
					CustomerState.getCustomer(), stats);

		// TODO processing
		List<Order> ret = new ArrayList<Order>();
		ret.addAll(orderService.findOrdersForCustomer(
				CustomerState.getCustomer(), MyOrderStatus.DISTRIBUTING));
		ret.addAll(orderService.findOrdersForCustomer(
				CustomerState.getCustomer(), MyOrderStatus.DISTRIBUTED));
		ret.addAll(orderService.findOrdersForCustomer(
				CustomerState.getCustomer(), MyOrderStatus.DELIVERY));

		if (!"PROCESSING".equals(status)) {
			ret.addAll(orderService.findOrdersForCustomer(
					CustomerState.getCustomer(), MyOrderStatus.SUBMITTED));
			ret.addAll(orderService.findOrdersForCustomer(
					CustomerState.getCustomer(), MyOrderStatus.COMPLETED));
			ret.addAll(orderService.findOrdersForCustomer(
					CustomerState.getCustomer(), MyOrderStatus.CANCELLED));
		}
		return ret;
	}

	@RequestMapping(value = "/customer/orders", method = RequestMethod.GET)
	public String viewOrderHistory(HttpServletRequest request, Model model) {
		// model.addAttribute("orders", listOrdersByStatus(null));
		if (CustomerState.getCustomer().getUsername() != null
				&& !("".equals(CustomerState.getCustomer().getUsername()))) {
			model.addAttribute("orders", myOrderService
					.getAllOrdersAfterSubmitted(CustomerState.getCustomer()
							.getId()));
		}

		return getOrderHistoryView();
	}

	@RequestMapping(value = "/customer/orders/{orderNumber}", method = RequestMethod.GET)
	public String viewOrderDetails(HttpServletRequest request, Model model,
			@PathVariable("orderNumber") String orderNumber) {
		Order order = orderService.findOrderByOrderNumber(orderNumber);
		model.addAttribute("order", order);
		return orderDetailsRedirectView;
	}

	/**
	 * 
	 * @param request
	 * @param model
	 * @param orderNumber
	 * @return
	 * @throws PricingException
	 * @throws ConcurrentInventoryModificationException
	 */
	@RequestMapping("/customer/orders/cancel/{orderNumber}")
	public String candelOrder(@PathVariable("orderNumber") String orderNumber)
			throws PricingException, ConcurrentInventoryModificationException {
		Order order = orderService.findOrderByOrderNumber(orderNumber);
		if(order != null && order.getStatus().equals(OrderStatus.SUBMITTED)){
			if (order.getTotalAdjustmentsValue().doubleValue() > 0) {
				//TODO有折扣的都不能取消。回头改。
				return "redirect:/customer/orders";
			}else{
				myOrderService.cancelMyOrder(order,
						(MyCustomer) CustomerState.getCustomer());
			}
		}
		return "redirect:/customer/orders";
	}

	// app /////////////////////////////////////
	@ResponseBody
	@RequestMapping("/app/account/orders")
	public List<Map<String, Object>> listOrders(
			@RequestParam(value = "status", required = false) String status) {
		List<Order> orders = listOrdersByStatus(status);
		return wrapOrder(orders);
	}

	@ResponseBody
	@RequestMapping("/app/account/order/{orderNumber}")
	public Map<String, Object> viewOrderApp(
			@PathVariable("orderNumber") String orderNumber) {
		Order order = orderService.findOrderByOrderNumber(orderNumber);
		if (order == null) {
			Map<String, Object> ret = new HashMap<String, Object>(2);
			ret.put("error", "no_order");
			ret.put("errorMessage", "该订单不存在！");
			return ret;
		}
		return wrapOrder(order, true);
	}

	@ResponseBody
	@RequestMapping("/app/account/order/cancel/{orderNumber}")
	public Map<String, Object> cancelOrderApp(
			@PathVariable("orderNumber") String orderNumber) {
		Order order = orderService.findOrderByOrderNumber(orderNumber);
		Map<String, Object> ret = new HashMap<String, Object>(2);
		if (order == null) {
			ret.put("error", "no_order");
			ret.put("errorMessage", "该订单不存在！");
		}
		try {
			candelOrder(orderNumber);
		} catch (PricingException e) {
			LOG.error("", e);
			ret.put("error", "error_price");
			ret.put("errorMessage", "抱歉，取消订单失败");
		} catch (ConcurrentInventoryModificationException e) {
			LOG.error("", e);
			ret.put("error", "error_inventory");
			ret.put("errorMessage", "抱歉，取消订单失败");
		}
		return ret;
	}

	private List<Map<String, Object>> wrapOrder(List<Order> orders) {
		if (orders == null || orders.isEmpty())
			return Collections.emptyList();

		List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>(
				orders.size());
		for (Order o : orders) {
			ret.add(wrapOrder(o, false));
		}
		return ret;
	}

	private Map<String, Object> wrapOrder(Order o, boolean listAll) {
		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("id", o.getId());
		ret.put("timestamp", o.getSubmitDate().getTime());
		ret.put("itemCount", o.getItemCount());
		ret.put("orderNum", o.getOrderNumber());
		ret.put("amount", o.getTotal());
		ret.put("status", o.getStatus().getType());
		ret.put("statusName", toString(o.getStatus()));
		List<OrderItem> items = o.getOrderItems();
		ret.put("oiCount", items.size());
		if (!listAll && items.size() > 2)
			items = items.subList(0, 2);
		List<Map<String, Object>> its = new ArrayList<Map<String, Object>>(
				items.size());
		ret.put("items", its);
		for (OrderItem oi : items) {
			Map<String, Object> m = new HashMap<String, Object>();
			its.add(m);
			m.put("quantity", oi.getQuantity());
			m.put("price", oi.getPriceBeforeAdjustments(true));
			Product p = null;
			if (oi instanceof SkuAccessor) {
				Sku sku = ((SkuAccessor) oi).getSku();
				if (sku != null) {
					p = sku.getProduct();
					m.put("id", p.getId());
					m.put("name", p.getName());
					m.put("url", p.getUrl());
					if (listAll)
						m.put("media", p.getMedia().get("primary"));
				}
			} // TODO else
		}
		return ret;
	}

	private Object toString(OrderStatus status) {
		if (OrderStatus.SUBMITTED.equals(status)) {
			return "已提交";
		} else if (OrderStatus.CANCELLED.equals(status)) {
			return "已取消";
		} else if (OrderStatus.CANCELLED.equals(status)) {
			// FIXME
		}
		return "";
	}
}
