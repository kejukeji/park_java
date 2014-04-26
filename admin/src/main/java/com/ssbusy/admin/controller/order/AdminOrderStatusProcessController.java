package com.ssbusy.admin.controller.order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.broadleafcommerce.core.order.domain.FulfillmentGroup;
import org.broadleafcommerce.core.order.domain.LocationedItem;
import org.broadleafcommerce.core.order.domain.Order;
import org.broadleafcommerce.core.order.domain.OrderItem;
import org.broadleafcommerce.core.order.domain.SkuAccessor;
import org.broadleafcommerce.core.order.service.type.OrderStatus;
import org.broadleafcommerce.openadmin.server.security.remote.SecurityVerifier;
import org.broadleafcommerce.openadmin.server.security.service.AdminSecurityService;
import org.broadleafcommerce.openadmin.web.controller.AdminAbstractController;
import org.broadleafcommerce.profile.core.domain.Customer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ssbusy.admin.user.domain.MyAdminUser;
import com.ssbusy.core.account.service.MyCustomerService;
import com.ssbusy.core.domain.MyAddress;
import com.ssbusy.core.integral.service.IntegralService;
import com.ssbusy.core.myorder.domain.MyOrder;
import com.ssbusy.core.myorder.domain.MyOrderStatus;
import com.ssbusy.core.myorder.service.MyOrderService;
import com.ssbusy.core.product.domain.MyProduct;
import com.ssbusy.core.product.myservice.MyProductService;
import com.ssbusy.core.rechargeablecard.domain.RechargeableCard;
import com.ssbusy.core.rechargeablecard.service.RechargeableCardService;

@Controller
public class AdminOrderStatusProcessController extends AdminAbstractController {

	@Resource(name = "ssbMyOrderService")
	protected MyOrderService myOrderService;

	@Resource(name = "blAdminSecurityService")
	protected AdminSecurityService securityService;

	@Resource(name = "ssbMyProductService")
	protected MyProductService myProductService;

	@Resource(name = "blCustomerService")
	protected MyCustomerService myCustomerService;

	@Resource(name = "ssbRechargeableCardService")
	protected RechargeableCardService rechargeableCardService;

	@Resource(name = "blAdminSecurityRemoteService")
	protected SecurityVerifier securityVerifier;

	@Resource(name = "ssbIntegralService")
	protected IntegralService integralService;

	private static String orderDistributeView = "order/orderDistribute";
	private static String orderDeliveryView = "order/orderDelivery";
	private static String orderCompleteView = "order/orderComplete";
	private static String orderDistributingDetail = "order/orderDistributingDetail";
	private static String orderDistribute = "redirect:/order-distribute";
	private static String orderDelivery = "redirect:/order-delivery";
	private static String orderComplete = "redirect:/order-complete";
	private static String DISTRIBUTE = "order-distribute";
	private static String DELIVERY = "order-delivery";
	private static String COMPLETE = "order-complete";

	@RequestMapping("/order-distribute")
	public String orderDistribute(HttpServletRequest request, Model model) {
		super.setModelAttributes(model, DISTRIBUTE);
		MyAdminUser adminUser = getAdmin();
		Long adminUserId = adminUser.getId();
		Long fulfillmentLocationId = adminUser.getFulfillmentLocation().getId();
		OrderStatus submittedStatus = MyOrderStatus.SUBMITTED;
		// TODO listRencentOrders
		List<MyOrder> submittedOrders = myOrderService.getOrderListByStatus(
				submittedStatus, fulfillmentLocationId);
		if (submittedOrders != null) {
			for (Iterator<MyOrder> itr = submittedOrders.iterator(); itr
					.hasNext();) {
				MyOrder myOrder = itr.next();
				if (myOrder.getDelieverDete() != null
						&& (myOrder.getDelieverDete().getTime() - new Date()
								.getTime()) > 2 * 60 * 60 * 1000) {
					itr.remove();
				}
			}
		}
		model.addAttribute("submittedOrders", submittedOrders);
		OrderStatus distributingStatus = MyOrderStatus.DISTRIBUTING;
		List<Order> distributingOrders = myOrderService
				.getOrderListByStatusAndAdminUserId(distributingStatus,
						adminUserId);
		model.addAttribute("distributingOrders", distributingOrders);

		OrderStatus distributedStatus = MyOrderStatus.DISTRIBUTED;
		List<Order> distributedOrders = myOrderService
				.getOrderListByStatusAndAdminUserId(distributedStatus,
						adminUserId);
		model.addAttribute("distributedOrders", distributedOrders);
		return orderDistributeView;
	}

	private MyAdminUser getAdmin() {
		return (MyAdminUser) securityVerifier.getPersistentAdminUser();
	}

	@RequestMapping("/order-distributing-detail")
	public String orderDistributingDetail(HttpServletRequest request,
			Model model,
			@RequestParam(value = "order_id", required = false) Long[] orderIds) {
		super.setModelAttributes(model, DISTRIBUTE);
		MyAdminUser adminUser = getAdmin();
		Long adminUserId = adminUser.getId();

		if (orderIds == null || orderIds.length == 0) {
			return orderDistribute;
		}
		myOrderService.setOrderUpdateBy(orderIds, adminUserId);
		List<Order> orders = myOrderService
				.getOrderListByOrderIdsAndAdminUserId(orderIds, adminUserId);
		List<DeliveryForm> forms = getDeliveryForm(orders);
		model.addAttribute("forms", forms);
		return orderDistributingDetail;
	}

	@RequestMapping("/order-detail/{orderId}")
	public String test(HttpServletRequest request, Model model,
			@PathVariable("orderId") Long orderId) {

		Order order = myOrderService.loadOrderByOrderId(orderId);
		if (order == null) {
			return orderDistributingDetail;
		}
		List<Order> orders = new ArrayList<Order>();
		orders.add(order);
		List<DeliveryForm> forms = getDeliveryForm(orders);
		model.addAttribute("forms", forms);
		return orderDistributingDetail;
	}

	@RequestMapping("/order-distributing-do")
	public String orderDistributingDo(HttpServletRequest request, Model model,
			@RequestParam(value = "order_id", required = false) Long[] orderIds) {
		super.setModelAttributes(model, DISTRIBUTE);
		if (orderIds != null && orderIds.length != 0) {
			OrderStatus status = MyOrderStatus.DISTRIBUTING;
			myOrderService.processOrderStatus(orderIds, status);
		}
		return orderDistribute;
	}

	@RequestMapping("/order-distributed-do")
	public String orderDistributedDo(HttpServletRequest request, Model model,
			@RequestParam(value = "order_id", required = false) Long[] orderIds) {
		super.setModelAttributes(model, DISTRIBUTE);
		if (orderIds != null && orderIds.length != 0) {
			OrderStatus status = MyOrderStatus.DISTRIBUTED;
			myOrderService.processOrderStatus(orderIds, status);
		}
		return orderDistribute;
	}

	@RequestMapping("/order-delivery")
	public String orderDelivery(HttpServletRequest request, Model model) {
		super.setModelAttributes(model, DELIVERY);
		OrderStatus distributeStatus = MyOrderStatus.DISTRIBUTED;
		MyAdminUser adminUser = getAdmin();
		Long fulfillmentLocationId = ((MyAdminUser) adminUser)
				.getFulfillmentLocation().getId();
		List<MyOrder> distributeOrders = myOrderService.getOrderListByStatus(
				distributeStatus, fulfillmentLocationId);
		model.addAttribute("distributeOrders", distributeOrders);
		return orderDeliveryView;
	}

	@RequestMapping(value = "/order-delivery-do")
	public String orderDeliveryDo(HttpServletRequest request, Model model,
			@RequestParam(value = "order_id", required = false) Long[] orderIds) {
		super.setModelAttributes(model, DELIVERY);
		if (orderIds != null && orderIds.length != 0) {
			OrderStatus status = MyOrderStatus.DELIVERY;
			myOrderService.processOrderStatus(orderIds, status);
		}
		return orderDelivery;
	}

	@RequestMapping("/order-complete")
	public String orderComplete(HttpServletRequest request, Model model) {
		super.setModelAttributes(model, COMPLETE);
		OrderStatus deliveryStatus = MyOrderStatus.DELIVERY;
		MyAdminUser adminUser = getAdmin();
		Long fulfillmentLocationId = ((MyAdminUser) adminUser)
				.getFulfillmentLocation().getId();
		List<MyOrder> deliveryOrders = myOrderService.getOrderListByStatus(
				deliveryStatus, fulfillmentLocationId);
		model.addAttribute("deliveryOrders", deliveryOrders);
		return orderCompleteView;
	}

	@RequestMapping(value = "/order-complete-do")
	public String orderCompleteDo(HttpServletRequest request, Model model,
			@RequestParam(value = "order_id", required = false) Long[] orderIds) {
		super.setModelAttributes(model, COMPLETE);
		if (orderIds != null && orderIds.length != 0) {
			myOrderService
					.processOrderStatus(orderIds, MyOrderStatus.COMPLETED);
			List<Order> orders = myOrderService.getOrderbyIds(orderIds);
			// FIXME 下面这部分应该挪入 service层

			// 登录账户返现
			// Customer customer = order.getCustomer();
			// if (customer.isRegistered()
			// // 只充值人民币
			// && "CNY".equals(order.getCurrency().getCurrencyCode())) {
			// List<OrderAdjustment> orderAdjustments = order
			// .getOrderAdjustments();
			// if (orderAdjustments != null && orderAdjustments.size() > 0)
			// for (OrderAdjustment oa : orderAdjustments) {
			// if (!(oa instanceof MyOrderAdjustment))
			// continue;
			// Offer offer = oa.getOffer();
			// if (!(offer instanceof MyOffer))
			// continue;
			// if (Boolean.TRUE.equals(((MyOffer) offer)
			// .isAddToBalance())) {
			// myCustomerService.rechargeToAccountBalance(
			// customer.getId(),
			// ((MyOrderAdjustment) oa)
			// .getActualValue().getAmount(),
			// BalanceChangeType.CASHBACK);
			// }
			// }
			// // TODO item级别优惠返现等
			// }
			for (Order order : orders) {
				// 累加已卖计数
				for (OrderItem oi : order.getOrderItems()) {
					if (oi instanceof SkuAccessor) {
						Long productId = ((SkuAccessor) oi).getSku()
								.getProduct().getId();
						Long quantity = new Long((long) oi.getQuantity());
						myProductService.setTotalSale(quantity, productId);
					}
				}
			}
			/*
			 * 增加账户积分
			 */
			for (Order order : orders) {
				for (OrderItem oi : order.getOrderItems()) {
					if (oi instanceof SkuAccessor) {
						int value = ((MyProduct) ((SkuAccessor) oi).getSku()
								.getProduct()).getJifen().intValue();
						integralService.gainIntegralForPayment(order
								.getCustomer().getId(), value);
					}
				}
			}
		}
		return orderComplete;
	}

	/*
	 * 显示订单打印小票. 若訂單項來自多個分仓，则只显示当前admin相关的分仓项目；
	 * 金额的话，由于前台限制了多分仓单子，只能到付；所以优惠按比例分摊。
	 * FIXME 运费如何收
	 */
	@SuppressWarnings("deprecation")
	public List<DeliveryForm> getDeliveryForm(List<Order> orders) {
		List<DeliveryForm> forms = new ArrayList<DeliveryForm>();
		MyAdminUser admin = getAdmin();
		Long locId = admin.getFulfillmentLocation().getId();
		for (Order order : orders) {
			Customer customer = order.getCustomer();
			DeliveryForm form = new DeliveryForm();
			form.setOrderId(order.getId());
			form.setSubmitDate(order.getSubmitDate());

			List<OrderItem> orderitems = new ArrayList<OrderItem>(
					order.getOrderItems());
			int quantitySum = 0;
			Map<Long, Double> totalPriceSums = new HashMap<Long, Double>();
			for (Iterator<OrderItem> itr = orderitems.iterator(); itr.hasNext();) {
				OrderItem oi = itr.next();
				if (oi instanceof LocationedItem) {
					Long locIdOi = ((LocationedItem) oi).getLocation().getId();
					addOiTotalPrice(locIdOi, oi, totalPriceSums);
					if (!locIdOi.equals(locId)) {
						itr.remove();
						continue;
					}
				} else {
					addOiTotalPrice(locId, oi, totalPriceSums);
				}
				quantitySum += oi.getQuantity();
			}
			form.setQuantitySum(quantitySum);
			Collections.sort(orderitems, new Comparator<OrderItem>() {
				@Override
				public int compare(OrderItem o1, OrderItem o2) {
					return o2.getQuantity() - o1.getQuantity();
				}
			});

			double ratio = 1;
			if(totalPriceSums.size() > 1) {// 多分仓情形
				Double curSubTotal = totalPriceSums.get(locId);
				if(curSubTotal == null)
					curSubTotal = 0d;
				double subTotal = 0;
				for (Double t : totalPriceSums.values()) {
					subTotal += t;
				}
				ratio = curSubTotal / subTotal;
			}

			form.setSubTotal(order.getSubTotal().multiply(ratio));
			form.setTotalAdjustmentsValue(order.getTotalAdjustmentsValue().multiply(ratio));
			form.setTotal(order.getTotal().multiply(ratio));

			form.setItems(orderitems);
			form.setEmailAddress(order.getEmailAddress());
			form.setOrderNumber(order.getOrderNumber());
			form.setShipping(order.getTotalShipping().getAmount()
					.setScale(1, BigDecimal.ROUND_HALF_DOWN));
			form.setDeliveryDate(((MyOrder) order).getDelieverDete());
			form.setCustomerUserName(customer.isRegistered() ? customer.getFirstName() : "匿名用户");

			if(totalPriceSums.size() <= 1)
				form.setPaymentInfos(order.getPaymentInfos());
			List<FulfillmentGroup> fulfillmentGroups = order
					.getFulfillmentGroups();
			if (fulfillmentGroups.size() > 0) {
				MyAddress myAddress = (MyAddress) fulfillmentGroups.get(0)
						.getAddress();
				form.setMyAddress(myAddress);
				form.setPhone(myAddress.getPrimaryPhone());
				form.setDetail(myAddress.getLastName());
			}

			// BigDecimal cashback = new BigDecimal(0);
			// if (customer.isRegistered()
			// // 只充值人民币
			// && "CNY".equals(order.getCurrency().getCurrencyCode())) {
			// List<OrderAdjustment> orderAdjustments = order
			// .getOrderAdjustments();
			// if (orderAdjustments != null && orderAdjustments.size() > 0)
			// for (OrderAdjustment oa : orderAdjustments) {
			// if (!(oa instanceof MyOrderAdjustment))
			// continue;
			// Offer offer = oa.getOffer();
			// if (!(offer instanceof MyOffer))
			// continue;
			//
			// if (Boolean.TRUE.equals(((MyOffer) offer)
			// .isAddToBalance())) {
			// cashback = cashback.add(((MyOrderAdjustment) oa)
			// .getActualValue().getAmount());
			// }
			// }
			// // TODO item级别优惠返现等
			// }
			// form.setCashback(cashback.setScale(1,
			// BigDecimal.ROUND_HALF_DOWN));

			// 充值卡生成；
			double card_value = 0;
			if ("CNY".equals(order.getCurrency().getCurrencyCode())) {
				int a = (int) (order.getSubTotal().doubleValue() / 10);
				card_value = a;
			}
			if (card_value > 0) {
				String orderNumber = order.getOrderNumber().toString();
				String reId = orderNumber.substring(orderNumber.length() - 8,
						orderNumber.length());
				RechargeableCard rechargeableCard = rechargeableCardService
						.readRechargCardById(reId);
				if (rechargeableCard == null) {
					long exTime = new Date().getTime() + 7 * 24 * 60 * 60
							* 1000;
					rechargeableCard = rechargeableCardService
							.createRechargeableCard(reId, null,
									new Date(exTime), (float) card_value);
				}
				form.setRechargeableCard(rechargeableCard);
			}
			forms.add(form);
		}
		return forms;
	}

	/**
	 * @param locId
	 * @param oi
	 * @param totalPriceSums
	 */
	private void addOiTotalPrice(Long locId, OrderItem oi,
			Map<Long, Double> totalPriceSums) {
		Double total = totalPriceSums.get(locId);
		if(total == null)
			total = 0d;
		total += oi.getTotalPrice().getAmount().doubleValue();
		totalPriceSums.put(locId, total);
	}

}
