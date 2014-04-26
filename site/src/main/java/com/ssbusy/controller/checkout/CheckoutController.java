/*
 * Copyright 2008-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *	   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ssbusy.controller.checkout;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.LRUMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.broadleafcommerce.common.exception.ServiceException;
import org.broadleafcommerce.common.money.Money;
import org.broadleafcommerce.common.persistence.Status;
import org.broadleafcommerce.common.rule.MvelHelper;
import org.broadleafcommerce.common.time.SystemTime;
import org.broadleafcommerce.core.checkout.service.exception.CheckoutException;
import org.broadleafcommerce.core.checkout.service.workflow.CheckoutResponse;
import org.broadleafcommerce.core.offer.domain.Offer;
import org.broadleafcommerce.core.offer.service.type.OfferRuleType;
import org.broadleafcommerce.core.offer.service.type.OfferType;
import org.broadleafcommerce.core.order.domain.FulfillmentGroup;
import org.broadleafcommerce.core.order.domain.NullOrderImpl;
import org.broadleafcommerce.core.order.domain.Order;
import org.broadleafcommerce.core.order.domain.OrderItem;
import org.broadleafcommerce.core.order.domain.SkuAccessor;
import org.broadleafcommerce.core.order.service.type.FulfillmentType;
import org.broadleafcommerce.core.order.service.type.OrderStatus;
import org.broadleafcommerce.core.payment.domain.PaymentInfo;
import org.broadleafcommerce.core.payment.domain.Referenced;
import org.broadleafcommerce.core.payment.service.exception.InsufficientFundsException;
import org.broadleafcommerce.core.payment.service.type.PaymentInfoType;
import org.broadleafcommerce.core.pricing.service.exception.PricingException;
import org.broadleafcommerce.core.web.checkout.model.OrderInfoForm;
import org.broadleafcommerce.core.web.order.CartState;
import org.broadleafcommerce.inventory.domain.FulfillmentLocation;
import org.broadleafcommerce.inventory.domain.Inventory;
import org.broadleafcommerce.inventory.exception.InventoryUnavailableException;
import org.broadleafcommerce.profile.core.domain.Address;
import org.broadleafcommerce.profile.core.domain.Customer;
import org.broadleafcommerce.profile.core.domain.CustomerAddress;
import org.broadleafcommerce.profile.web.core.CustomerState;
import org.mvel2.MVEL;
import org.mvel2.ParserContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ssbusy.core.account.domain.MyCustomer;
import com.ssbusy.core.account.service.BalanceChangeType;
import com.ssbusy.core.domain.AreaAddress;
import com.ssbusy.core.domain.Dormitory;
import com.ssbusy.core.domain.MyAddress;
import com.ssbusy.core.inneraddress.service.DormitoryService;
import com.ssbusy.core.myorder.domain.MyOrder;
import com.ssbusy.core.myorder.service.MyOrderService;
import com.ssbusy.core.offer.domain.MyOfferCode;
import com.ssbusy.core.offer.service.MyOfferService;
import com.ssbusy.core.region.domain.Region;
import com.ssbusy.core.region.service.RegionService;
import com.ssbusy.payment.service.type.AlipayPaymentInfo;
import com.ssbusy.payment.service.type.MyPaymentInfoType;
import com.ssbusy.site.myshippingform.MyBillingInfoForm;
import com.ssbusy.site.myshippingform.MyShippingInfoForm;

@Controller("checkoutController")
public class CheckoutController extends MyBroadleafCheckoutController {
	private static final Log LOG = LogFactory.getLog(CheckoutController.class);
	private static final Map EXPRESSION_CACHE = new LRUMap(1000);
	/*
	 * The Checkout page for Heat Clinic will have the shipping information
	 * pre-populated with an address if the fulfillment group has an address and
	 * fulfillment option associated with it. It also assumes that there is only
	 * one payment info of type credit card on the order. If so, then the
	 * billing address will be pre-populated.
	 */
	@Resource(name = "ssbDormitoryService")
	protected DormitoryService dormitoryService;

	@Resource(name = "ssbRegionService")
	protected RegionService regionService;

	@Resource(name = "blOfferService")
	protected MyOfferService offerService;

	@Resource(name = "ssbMyOrderService")
	protected MyOrderService myorderService;

	@Value("${couzheng_show_quantity}")
	private int couzheng_show_quantity;

	private static final String REDIRECT_REGION_SELECT = "redirect:/region";
	private static final String REDIRECT_DENY = "redirect:/login";

	/**
	 * TODO 这一步其实是show cart，不是checkout
	 */
	@RequestMapping("/checkout")
	public String checkout(Model model) {
		Region region = null;
		MyCustomer myCustomer = (MyCustomer) CustomerState.getCustomer();
		region = myCustomer.getRegion();
		if (CartState.getCart() != null) {
			Money total = CartState.getCart().getTotal();
			if (total != null
					&& Math.floor(total.doubleValue()) != total.doubleValue()) {
				List<Inventory> invs = inventoryService
						.findProductsByPriceAndCurrency(total.getAmount(),
								CartState.getCart().getCurrency(),
								region.getFulfillmentLocations(), 0, 12);
				if (couzheng_show_quantity < invs.size()) {
					invs = invs.subList(0, couzheng_show_quantity);
				}
				model.addAttribute("inventories", invs);
			}
		}
		// 取出所有的优惠信息，并把过期的移除
		List<Offer> offers = offerService.findAllOffers();
		for (Iterator<Offer> iterator = offers.iterator(); iterator.hasNext();) {
			Offer offer = null;
			offer = iterator.next();

			if (!((Status) offer).isActive()) {
				iterator.remove();
			}
		}
		if (offers.iterator() != null) {
			Iterator<Offer> iterator = offers.iterator();
			while (iterator.hasNext()) {
				// To do
				Offer offer = iterator.next();
				if (offer.getOfferMatchRules().get(
						OfferRuleType.CUSTOMER.getType()) != null) {

					String maprule = offer.getOfferMatchRules()
							.get(OfferRuleType.CUSTOMER.getType())
							.getMatchRule();
					HashMap<String, Object> vars = new HashMap<String, Object>();
					vars.put("customer", myCustomer);
					Boolean expressionOutcome = executeExpression(maprule, vars);
					if (expressionOutcome == null || !expressionOutcome) {
						iterator.remove();
					}
				}
			}

		}

		model.addAttribute("offers", offers);
		List<MyOfferCode> myOfferCodes = offerService
				.listOfferCodeByOwner(CustomerState.getCustomer().getId());
		model.addAttribute("offercodes", myOfferCodes);
		return getCheckoutView();
	}

	@RequestMapping(value = "/checkout/checkout-step-2")
	public String saveGlobalOrderDetails(
			HttpServletRequest request,
			Model model,
			@ModelAttribute("orderInfoForm") OrderInfoForm orderInfoForm,
			@ModelAttribute("shippingInfoForm") MyShippingInfoForm shippingForm,
			@ModelAttribute("billingInfoForm") MyBillingInfoForm billingForm,
			BindingResult result) {
		MyCustomer myCustomer = (MyCustomer) CustomerState.getCustomer();
		Region region = myCustomer.getRegion();
		if (region == null) {
			return REDIRECT_REGION_SELECT;
		}

		SimpleDateFormat dateformat1 = new SimpleDateFormat("HH:mm");
		String date = dateformat1.format(new Date());
		String[] times = region.getShipping_time().split(";");
		boolean isOutDeliveryDateRange = true;
		for (String time : times) {
			String[] shipping_time = time.split("-");
			if ((date.compareTo(shipping_time[0]) > 0)
					&& (date.compareTo(shipping_time[1]) < 0))
				isOutDeliveryDateRange = false;
		}
		model.addAttribute("isOutDeliveryDateRange", isOutDeliveryDateRange);

		prepopulateCheckoutForms(CartState.getCart(), orderInfoForm,
				shippingForm, billingForm);
		return super.saveGlobalOrderDetails(request, model, orderInfoForm,
				result);
	}

	@RequestMapping(value = "/checkout/singleship", method = RequestMethod.GET)
	public String convertToSingleship(HttpServletRequest request,
			HttpServletResponse response, Model model) throws PricingException {
		return super.convertToSingleship(request, response, model);
	}

	@RequestMapping(value = "/checkout/singleship", method = RequestMethod.POST)
	public String saveSingleShip(
			HttpServletRequest request,
			HttpServletResponse response,
			Model model,
			@ModelAttribute("orderInfoForm") OrderInfoForm orderInfoForm,
			@ModelAttribute("billingInfoForm") MyBillingInfoForm billingForm,
			@ModelAttribute("shippingInfoForm") MyShippingInfoForm shippingForm,
			BindingResult result) throws Exception {

		/*
		 * 验证是否remember登录并且采用余额和积分交易
		 */
		SecurityContext context = SecurityContextHolder.getContext();
		Authentication auth = context.getAuthentication();
		Boolean b = (auth instanceof RememberMeAuthenticationToken)
				&& ("integral_pay".equals(shippingForm.getPaymentMethod()) || "balance_pay"
						.equals(shippingForm.getPaymentMethod()));
		if (b) {
			return REDIRECT_DENY;
		}
		return saveSingleShip0(request, response, model, orderInfoForm,
				billingForm, shippingForm, result, false);

	}

	Date myDate1;
	Date myDate2;
	Date myDate3;
	Date myDate4;

	public CheckoutController() {
		DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			myDate1 = dateFormat1.parse("2013-11-11 11:11:00");
			myDate2 = dateFormat1.parse("2013-11-11 15:11:00");
			myDate3 = dateFormat1.parse("2013-11-11 19:11:00");
			myDate4 = dateFormat1.parse("2013-11-11 23:11:00");
		} catch (ParseException e) {
			// impossible
		}
	}

	private String saveSingleShip0(HttpServletRequest request,
			HttpServletResponse response, Model model,
			OrderInfoForm orderInfoForm, MyBillingInfoForm billingForm,
			MyShippingInfoForm shippingForm, BindingResult result, boolean ajax)
			throws PricingException, ServiceException, CheckoutException,
			ParseException {
		MyCustomer myCustomer = (MyCustomer) CustomerState.getCustomer();
		Region region = myCustomer.getRegion();
		Boolean w_flag = (Boolean)request.getSession().getAttribute("w_flag");
		if (region == null) {
			if(w_flag!=null&&w_flag){
				return  "redirect:/weixin/region";
			}else
			return REDIRECT_REGION_SELECT;
		}

		MyAddress myAddress = processShippingForm(shippingForm, result);
		if (result.hasErrors()) {
			putFulfillmentOptionsAndEstimationOnModel(model);
			populateModelWithShippingReferenceData(request, model);
			model.addAttribute("states", stateService.findStates());
			model.addAttribute("countries", countryService.findCountries());
			model.addAttribute("expirationMonths", populateExpirationMonths());
			model.addAttribute("expirationYears", populateExpirationYears());
			model.addAttribute("validShipping", false);
			if(w_flag!=null&&w_flag){
				return  "weixin/cart/w_checkout";
			}else
			return checkoutAddress;
		}

		billingForm.setPaymentMethod(shippingForm.getPaymentMethod());
		billingForm.setBp_pay(shippingForm.getBp_pay());
		billingForm.setAlipay(shippingForm.getAlipay());
		if (shippingForm.getBp_pay() == null
				|| shippingForm.getBp_pay().doubleValue() < 0)
			billingForm.setBp_pay(BigDecimal.ZERO);
		if (shippingForm.getAlipay() == null
				|| shippingForm.getAlipay().doubleValue() < 0)
			billingForm.setAlipay(BigDecimal.ZERO);
		billingForm.setMyAddress(myAddress);
		prepopulateCheckoutForms(CartState.getCart(), orderInfoForm, null,
				billingForm);

		String assign = request.getParameter("assign");
		MyOrder cart = (MyOrder) CartState.getCart();

		Date dStart = null, dEnd = null;
		Date nowDay = Calendar.getInstance().getTime();
		if (nowDay.after(myDate1) && nowDay.before(myDate2)) {
			dStart = myDate1;
			dEnd = myDate2;
		} else if (nowDay.after(myDate2) && nowDay.before(myDate3)) {
			dStart = myDate2;
			dEnd = myDate3;
		} else if (nowDay.after(myDate3) && nowDay.before(myDate4)) {
			dStart = myDate3;
			dEnd = myDate4;
		}
		if (dStart != null) {
			List<Order> orders = myorderService.getAllSubmittedInTime(
					myCustomer.getId(), dStart, dEnd);
			boolean contain = ifCanAdd(orders);
			if (contain) {
				if(w_flag!=null&&w_flag){
					return  "redirect:/weixin/checkout";
				}else
				return "redirect:/checkout/checkout-step-2?error=%E4%B8%8D%E8%A6%81%E5%A4%AA%E8%B4%AA%E5%BF%83%EF%BC%8C%E5%8F%AA%E8%83%BD%E6%8A%A21%E6%AC%A1%E5%93%A6";
			}
		}

		String a1;
		Date a2;
		if ("2".equals(assign)) {
			String date = request.getParameter("date");
			String detaildate = request.getParameter("detaildate");
			int deta = 0;
			try {
				deta = Integer.parseInt(detaildate);
			} catch (Exception e) {
				// ignore
			}

			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.HOUR_OF_DAY, deta);
			if ("1".equals(date)) {
				calendar.set(Calendar.DAY_OF_MONTH,
						calendar.get(Calendar.DAY_OF_MONTH) + 1);
			} else if ("2".equals(date)) {
				calendar.set(Calendar.DAY_OF_MONTH,
						calendar.get(Calendar.DAY_OF_MONTH) + 2);
			}
			SimpleDateFormat dateformat1 = new SimpleDateFormat("HH:mm");
			a2 = calendar.getTime();
			a1 = dateformat1.format(a2);
		} else {
			SimpleDateFormat dateformat1 = new SimpleDateFormat("HH:mm");
			a2 = new Date();
			a1 = dateformat1.format(a2);
		}
		cart.setDelieverDate(a2);

		FulfillmentGroup fulfillmentGroup = cart.getFulfillmentGroups().get(0);

		fulfillmentGroup.setAddress(shippingForm.getMyAddress());
		fulfillmentGroup.setPersonalMessage(shippingForm.getPersonalMessage());
		fulfillmentGroup.setDeliveryInstruction(shippingForm
				.getDeliveryMessage());

		cart = (MyOrder) orderService.save(cart, true);
		CartState.setCart(cart);
		CustomerAddress defaultAddress = customerAddressService
				.findDefaultCustomerAddress(CustomerState.getCustomer().getId());
		if (defaultAddress == null) {
			MyAddress address = (MyAddress) addressService
					.saveAddress(shippingForm.getMyAddress());
			CustomerAddress customerAddress = customerAddressService.create();
			customerAddress.setAddress(address);
			customerAddress.setCustomer(CustomerState.getCustomer());
			customerAddress = customerAddressService
					.saveCustomerAddress(customerAddress);
			customerAddressService.makeCustomerAddressDefault(customerAddress
					.getId(), customerAddress.getCustomer().getId());
		}

		// 是否不验证送货时间
		if (false) {// if("no".equals(request.getParameter("forcedSubmit"))){
			String[] times = region.getShipping_time().split(";");
			boolean isOutDeliveryDateRange = true;
			for (String time : times) {
				String[] shipping_time = time.split("-");
				if ((a1.compareTo(shipping_time[0]) > 0)
						&& (a1.compareTo(shipping_time[1]) < 0))
					isOutDeliveryDateRange = false;
			}
			if (isOutDeliveryDateRange) {
				return "redirect:/checkout/checkout-step-2?flag=1";
			}
		}

		try {
			if ("alipay_pay".equals(billingForm.getPaymentMethod())) {
				String description = "";
				for (OrderItem ot : cart.getOrderItems()) {
					description = description + ot.getName() + "；";
					if (description.length() > 50) {
						description = description + "....";
						break;
					}
				}
				// TODO 直接取第一个location了
				FulfillmentLocation loc = null;
				List<FulfillmentGroup> fgroups = cart.getFulfillmentGroups();
				if (fgroups == null || fgroups.size() == 0) {
					Customer customer = CustomerState.getCustomer();
					if (customer instanceof MyCustomer)
						loc = ((MyCustomer) customer).getRegion()
								.getFulfillmentLocations().get(0);
				} else {
					Address addr = fgroups.get(0).getAddress();
					if (addr instanceof MyAddress) {
						loc = ((MyAddress) addr).getDormitory()
								.getAreaAddress().getRegion()
								.getFulfillmentLocations().get(0);
					}
				}
				// TODO path中的一切变量方法alipay相关的控制器里
				String path = "/alipay/direct_pay?tradeNo="
						+ (new SimpleDateFormat("yyyyMMddHHmm")
								.format(SystemTime.asDate()) + cart.getId())
						+ "&subject=在校网 " + (loc == null ? "" : loc.getId())
						+ (new Date().getTime() % 1000000) + cart.getId()
						+ " 号订单&description=" + description
						+ "&tradeUrl=http://www.onxiao.com/customer/orders";
				if (billingForm.getAlipay().compareTo(
						cart.getTotal().getAmount()) >= 0) {
					// 全部支付宝
					path = path + "&totalFee=" + cart.getTotal();
					return "forward:" + path;

				} else {
					if (billingForm.getAlipay().doubleValue() == 0) {
						return completeCodCheckout(request, response, model,
								billingForm, result);
					} else {
						// 部分支付宝
						path = path + "&totalFee=" + billingForm.getAlipay();
						return "forward:" + path;
					}
				}
			} else if ("balance_pay".equals(billingForm.getPaymentMethod())) {
				if (billingForm.getBp_pay().compareTo(
						cart.getTotal().getAmount()) >= 0) {
					return completeBpCheckout(request, response, model,
							billingForm, result);
				} else {
					if (billingForm.getAlipay().doubleValue() <= 0) {
						return completeCodCheckout(request, response, model,
								billingForm, result);
					} else {
						return complexCheckout(request, response, model,
								billingForm, result,
								MyPaymentInfoType.Payment_Bp, cart.getId());
					}
				}

			} else if ("integral_pay".equals(billingForm.getPaymentMethod())) {
				return completeIntegrlCheckout(request, response, model,
						billingForm, result);
			} else {
				return completeCodCheckout(request, response, model,
						billingForm, result);
			}
		} catch (CheckoutException e) {
			if (ajax)
				throw e;
			if (e.getCause() instanceof InventoryUnavailableException) {
				try {
					if(w_flag!=null&&w_flag){
						return  "redirect:/weixin/checkout";
					}else
					return "redirect:/checkout/checkout-step-2?inventoryError=1&errorMessage="
							+ URLEncoder.encode(
									((InventoryUnavailableException) e
											.getCause()).getMessage(), "utf-8");
				} catch (UnsupportedEncodingException e1) {
					if(w_flag!=null&&w_flag){
						return  "redirect:/weixin/checkout";
					}else
					return "redirect:/checkout/checkout-step-2?inventoryError=1&errorMessage="
							+ ((InventoryUnavailableException) e.getCause())
									.getMessage();
				}
			} else if (e.getCause() instanceof InsufficientFundsException) {
				if(w_flag!=null&&w_flag){
					return  "redirect:/weixin/checkout";
				}else
				return "redirect:/checkout/checkout-step-2?error=%E7%A7%AF%E5%88%86%E6%88%96%E8%80%85%E4%BD%99%E9%A2%9D%E4%B8%8D%E8%B6%B3";
			} else {
				throw e;
			}
		}
	}

	@RequestMapping(value = "/checkout/alipaySucessOrFailed")
	public String alipayCheckout(HttpServletRequest request,
			HttpServletResponse response, Model model,
			MyBillingInfoForm billingForm, BindingResult result,
			String orderNum, String returnInfo, String total_fee) {
		LOG.info("this is our alipayCheckout function:");
		/*
		 * String orderNum = (String) request.getAttribute("order_id"); String
		 * returnInfo = (String) request.getAttribute("return_info"); String
		 * total_fee = (String) request.getAttribute("total_fee");
		 */
		BigDecimal alipay = null;
		if ("".equals(total_fee) || total_fee == null) {
			LOG.error("total_fee is null");
		} else {
			alipay = new BigDecimal(total_fee);
		}

		if (orderNum == null || returnInfo == null || alipay == null
				|| orderNum.length() < 13 || !"success".equals(returnInfo)) {
			LOG.error("orderNum or retutnInfo or alipay is error");
			return getCartPageRedirect();
		}

		Long orderId;
		try {
			orderId = Long.parseLong(orderNum.substring(12));
		} catch (NumberFormatException e) {
			LOG.error("substring orderNum throws an exception" + orderNum, e);
			return getCartPageRedirect();
		}
		Order cart = orderService.findOrderById(orderId);
		if (cart != null && OrderStatus.IN_PROCESS.equals(cart.getStatus())) {
			if (alipay.compareTo(cart.getTotal().getAmount()) < 0) {
				billingForm.setAlipay(alipay);
				LOG.info("a part of the order is used alipay");
				String ret = "";
				try {
					ret = complexCheckout(request, response, model,
							billingForm, result,
							MyPaymentInfoType.Payment_Alipay, orderId);
				} catch (CheckoutException e) {
					if (e.getCause() instanceof InventoryUnavailableException) {
						LOG.info("InventoryUnavailableException so we pay balance to the customer");
						return aplipayFailedRollBack2Banlance(alipay, cart);
					} else {
						LOG.info("not know exception", e);
					}
				}
				if (OrderStatus.IN_PROCESS.equals(cart.getStatus())) {
					return aplipayFailedRollBack2Banlance(alipay, cart);
				} else {
					LOG.info("alipay pay the part of the order is success");
					return ret;
				}
			}
			copyShippingAddressToBillingAddress(cart, billingForm);
			billingInfoFormValidator.validate(billingForm, result);
			if (result.hasErrors()) {
				LOG.error("result.hasErrors() orderid=" + orderId);
				populateModelWithShippingReferenceData(request, model);
				return getCheckoutView();
			}
			// 先把原有支付信息清理掉
			cart.getPaymentInfos().clear();
			PaymentInfo alipayInfo = alipayPaymentInfoFactory
					.constructPaymentInfo(cart);
			alipayInfo.setAddress(billingForm.getMyAddress());
			cart.getPaymentInfos().add(alipayInfo);
			AlipayPaymentInfo alipayReference = (AlipayPaymentInfo) alipaySecurePaymentInfoService
					.create(MyPaymentInfoType.Payment_Alipay);
			alipayReference.setMessage("success");
			alipayReference.setReferenceNumber("try");
			Map<PaymentInfo, Referenced> payments = new HashMap<PaymentInfo, Referenced>(
					1);
			payments.put(alipayInfo, alipayReference);
			CheckoutResponse checkoutResponse = null;
			try {
				checkoutResponse = checkoutService.performCheckout(cart,
						payments);
			} catch (CheckoutException e) {
				if (e.getCause() instanceof InventoryUnavailableException) {
					LOG.info("InventoryUnavailableException in all pay by alipay");
					return aplipayFailedRollBack2Banlance(alipay, cart);
				} else {
					LOG.info("not know exception in all pay by alipay", e);
				}
			}
			if (!checkoutResponse.getPaymentResponse().getResponseItems()
					.get(alipayInfo).getTransactionSuccess()) {

				LOG.error("alipay is finished but the order is not success because some problems");
				// 支付宝支付成功，但是我们这边有问题，查看客户是否注册，注册则返回到余额，没有注册则提醒联系客服。
				return aplipayFailedRollBack2Banlance(alipay, cart);
			}
			LOG.info("alipay success, the order is success order id=" + orderId);
			return getConfirmationView(cart.getOrderNumber());
		} else {

			LOG.error("alipay failed, the order is not normal order invalid: id="
					+ orderId);
			if (cart != null) {
				LOG.error("alipay failed OrderStatus is " + cart.getStatus());
			}
		}
		return getCartPageRedirect();

	}

	private String aplipayFailedRollBack2Banlance(BigDecimal alipay, Order cart) {
		MyCustomer myCustomer = (MyCustomer) cart.getCustomer();
		if (myCustomer.isRegistered()) {
			LOG.info("return the alipay money to the customer Balance; The customer_id is "
					+ myCustomer.getId()
					+ ";return "
					+ alipay
					+ " balance to the Customer");
			myCustomerService.rechargeToAccountBalance(myCustomer.getId(),
					alipay, BalanceChangeType.BPBACK);
			return "redirect:/checkout/checkout-step-2?alipayerrorCode=2";
		} else {
			LOG.info("Customer is not registered so the customer will call you latter,orderId is "
					+ cart.getId());
			for (PaymentInfo pi : cart.getPaymentInfos()) {
				pi.setAmount(new Money(BigDecimal.ZERO, cart.getCurrency()));
			}
			return "redirect:/checkout/checkout-step-2?alipayerrorCode=1";
		}
	}

	@SuppressWarnings("deprecation")
	private MyAddress processShippingForm(MyShippingInfoForm shippingForm,
			BindingResult result) {
		shippingInfoFormValidator.validate(shippingForm, result);
		if (result.hasErrors())
			return null;
		// TODO ID为空的时候处理
		// 这里先用address的AddressLine3保存dormitoryid然后根据id找dormitory对象，最后要改掉的。
		MyAddress myAddress = shippingForm.getMyAddress();
		Long dormitoryId;
		try {
			dormitoryId = Long.parseLong(myAddress.getAddressLine3());
		} catch (NumberFormatException e) {
			ValidationUtils.rejectIfEmpty(result, "myAddress.addressLine3",
					"addressLine3.required");
			return null;
		}
		if (myAddress.getDormitory() == null
				|| !dormitoryId.equals(myAddress.getDormitory()
						.getDormitoryId())) {
			Dormitory dormitory = dormitoryService
					.loadDormitotyById(dormitoryId);
			if (dormitory == null) {
				ValidationUtils.rejectIfEmpty(result, "myAddress.addressLine3",
						"addressLine3.required");
				return null;
			}
			// dormitory.id为-1的时候表示用户选择的是其他。
			myAddress.setDormitory(dormitory);
		}

		myAddress.setAddressLine1("addressLine1");
		myAddress.setCity("city");
		myAddress.setPostalCode("310018");
		if (myAddress.getLastName() != null
				&& myAddress.getLastName().length() > 30) {
			myAddress.setLastName(myAddress.getLastName().substring(0, 30));
		}
		myAddress.setState(shippingForm.getAddress().getState());
		if (myAddress.getCountry() == null)
			myAddress.setCountry(shippingForm.getAddress().getCountry());
		return myAddress;
	}

	// @RequestMapping(value = "/checkout/multiship", method =
	// RequestMethod.GET)
	// public String showMultiship(
	// HttpServletRequest request,
	// HttpServletResponse response,
	// Model model,
	// @ModelAttribute("orderMultishipOptionForm") OrderMultishipOptionForm
	// orderMultishipOptionForm,
	// BindingResult result) throws PricingException {
	// return super.showMultiship(request, response, model);
	// }

	// @RequestMapping(value = "/checkout/multiship", method =
	// RequestMethod.POST)
	// public String saveMultiship(
	// HttpServletRequest request,
	// HttpServletResponse response,
	// Model model,
	// @ModelAttribute("orderMultishipOptionForm") OrderMultishipOptionForm
	// orderMultishipOptionForm,
	// BindingResult result) throws PricingException, ServiceException {
	// return super.saveMultiship(request, response, model,
	// orderMultishipOptionForm, result);
	// }

	// @RequestMapping(value = "/checkout/add-address", method =
	// RequestMethod.GET)
	// public String showMultishipAddAddress(HttpServletRequest request,
	// HttpServletResponse response, Model model,
	// @ModelAttribute("addressForm") ShippingInfoForm addressForm,
	// BindingResult result) {
	// return super.showMultishipAddAddress(request, response, model);
	// }

	// @RequestMapping(value = "/checkout/add-address", method =
	// RequestMethod.POST)
	// public String saveMultishipAddAddress(HttpServletRequest request,
	// HttpServletResponse response, Model model,
	// @ModelAttribute("addressForm") ShippingInfoForm addressForm,
	// BindingResult result) throws ServiceException {
	// return super.saveMultishipAddAddress(request, response, model,
	// addressForm, result);
	// }

	@RequestMapping(value = "/checkout/complete", method = RequestMethod.POST)
	public String completeCheckout(
			HttpServletRequest request,
			HttpServletResponse response,
			Model model,
			@ModelAttribute("orderInfoForm") OrderInfoForm orderInfoForm,
			@ModelAttribute("shippingInfoForm") MyShippingInfoForm shippingForm,
			@ModelAttribute("billingInfoForm") MyBillingInfoForm billingForm,
			BindingResult result) throws Exception {
		/*
		 * prepopulateCheckoutForms(CartState.getCart(), null, shippingForm,
		 * billingForm); try { if (billingForm.getPaymentMethod().equals("cod"))
		 * return super.completeCodCheckout(request, response, model,
		 * billingForm, result); else if
		 * (billingForm.getPaymentMethod().equals("balance_pay")) { return
		 * super.completeBpCheckout(request, response, model, billingForm,
		 * result); } else { return super.completeIntegrlCheckout(request,
		 * response, model, billingForm, result); } } catch (CheckoutException
		 * e) { if (e.getCause() instanceof InventoryUnavailableException) {
		 * return "/checkout/checkout_error"; } else { throw e; } } catch
		 * (Exception e) { if (e.getCause() instanceof
		 * InsufficientFundsException) { return "/checkout/checkout_error"; }
		 * else { throw e; } }
		 */
		return null;
	}

	protected void prepopulateOrderInfoForm(Order cart,
			OrderInfoForm orderInfoForm) {
		if (orderInfoForm == null)
			return;
		if (cart.getEmailAddress() == null
				|| cart.getEmailAddress().length() == 0)
			orderInfoForm.setEmailAddress("admin@onxiao.com");
		else
			orderInfoForm.setEmailAddress(cart.getEmailAddress());
	}

	protected void prepopulateCheckoutForms(Order cart,
			OrderInfoForm orderInfoForm, MyShippingInfoForm shippingForm,
			MyBillingInfoForm billingForm) {
		if (orderInfoForm != null)
			prepopulateOrderInfoForm(cart, orderInfoForm);
		if (shippingForm != null) {
			MyAddress myAddress = null;
			List<FulfillmentGroup> groups = cart.getFulfillmentGroups();
			if (CollectionUtils.isNotEmpty(groups)) {
				FulfillmentGroup fulfillmentGroup = groups.get(0);
				// if the cart has already has fulfillment information
				myAddress = (MyAddress) fulfillmentGroup.getAddress();
				if (fulfillmentGroup.getFulfillmentOption() != null) {
					shippingForm.setFulfillmentOption(fulfillmentGroup
							.getFulfillmentOption());
					shippingForm.setFulfillmentOptionId(fulfillmentGroup
							.getFulfillmentOption().getId());
				}
			}
			if (myAddress == null) {
				Region region = ((MyCustomer) CustomerState.getCustomer())
						.getRegion();
				myAddress = getAddressByRegion(region);
			}
			if (myAddress != null) {
				// shippingForm.getAddress().setPrimaryPhone(
				// myAddress.getPrimaryPhone());
				shippingForm.setMyAddress(myAddress);
			}
		}

		if (billingForm != null) {
			billingForm.setUseShippingAddress(true);
		}

		// if (cart.getPaymentInfos() != null) {
		// for (PaymentInfo paymentInfo : cart.getPaymentInfos()) {
		// if (PaymentInfoType.CREDIT_CARD.equals(paymentInfo.getType())) {
		// billingForm.setAddress(paymentInfo.getAddress());
		// }
		// }
		// }
	}

	@InitBinder
	protected void initBinder(HttpServletRequest request,
			ServletRequestDataBinder binder) throws Exception {
		super.initBinder(request, binder);
	}

	@ResponseBody
	@RequestMapping({ "/checkout/getdormitory", "/app/list-dorms" })
	public List<Map<String, Object>> getDormitoryByAreaId(
			@RequestParam("area_id") Long areaId) {
		AreaAddress areaAddress = areaService.getAreaById(areaId);
		if (areaAddress == null)
			return Collections.emptyList();
		return wrapDorms(dormitoryService
				.listDormitoriesByAreaAddress(areaAddress));
	}

	private List<Map<String, Object>> wrapDorms(List<Dormitory> dorms) {
		if (dorms == null || dorms.isEmpty())
			return Collections.emptyList();

		List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>(
				dorms.size());
		for (Dormitory d : dorms) {
			Map<String, Object> m = new HashMap<String, Object>(3);
			ret.add(m);
			m.put("dormitoryId", d.getDormitoryId());
			m.put("dormitoryName", d.getDormitoryName());
			m.put("displayName", getAbrevStr(d.toString(), 15));
		}
		return ret;
	}

	// app ////////////////////////////////////////////////////

	@ResponseBody
	@RequestMapping("app/checkout")
	public Map<String, Object> checkoutApp(
			HttpServletRequest request,
			Model model,
			@ModelAttribute("orderInfoForm") OrderInfoForm orderInfoForm,
			@ModelAttribute("shippingInfoForm") MyShippingInfoForm shippingForm,
			@ModelAttribute("billingInfoForm") MyBillingInfoForm billingForm,
			BindingResult result) {
		Map<String, Object> ret = new HashMap<String, Object>();

		Order cart = CartState.getCart();
		if (cart == null || cart instanceof NullOrderImpl) {
			ret.put("error", "null_order");
			ret.put("errorMessage", "您的购物车是空的哦～");
		} else {
			String view = this.saveGlobalOrderDetails(request, model,
					orderInfoForm, shippingForm, billingForm, result);

			if (REDIRECT_REGION_SELECT.equals(view)) {
				ret.put("error", "region_select");
			} else {
				boolean addrSet = shippingForm.getMyAddress().getDormitory() != null;
				String addressName = addrSet ? shippingForm.getAddressName()
						: "";
				if (addressName == null || addressName.length() == 0) {
					addressName = getAddressName(shippingForm.getMyAddress(),
							true);
				}
				ret.put("addressName", addressName);
				ret.put("currencyCode", cart.getCurrency() == null ? null
						: cart.getCurrency().getCurrencyCode());
				ret.put("subtotal", cart.getSubTotal());
				ret.put("totalShipping", cart.getTotalShipping());
				ret.put("orderAdjustmentsValue",
						cart.getOrderAdjustmentsValue());
				ret.put("total", cart.getTotal());
			}
		}
		return ret;
	}

	@SuppressWarnings("deprecation")
	@RequestMapping(value = "/app/checkout/singleship")
	@ResponseBody
	public Map<String, Object> singleShip(Model model,
			HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute("shippingInfoForm") MyShippingInfoForm shippingForm) {
		Map<String, Object> ret = new HashMap<String, Object>(7);

		Region region = ((MyCustomer) CustomerState.getCustomer()).getRegion();
		ret.put("regionName", region == null ? "" : region.getRegionName());
		ret.put("areas", wrapAreas(areaService.listAreasByRegion(region)));

		prepopulateCheckoutForms(CartState.getCart(), null, shippingForm, null);
		MyAddress address = shippingForm.getMyAddress();
		if (address.getDormitory() == null)
			return ret;

		ret.put("addressName", getAddressName(address, false));
		ret.put("realName", address.getFirstName());
		ret.put("mobile", address.getPrimaryPhone());
		ret.put("dormitoryId", address.getDormitory() == null ? "" : address
				.getDormitory().getDormitoryId());
		ret.put("roomNo", address.getRoomNo());
		return ret;
	}

	private List<Map<String, Object>> wrapAreas(List<AreaAddress> areas) {
		if (areas == null || areas.isEmpty())
			return Collections.emptyList();
		List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>(
				areas.size());
		for (AreaAddress a : areas) {
			Map<String, Object> m = new HashMap<String, Object>();
			ret.add(m);
			m.put("id", a.getAreaId());
			m.put("name", a.getAreaName());
		}
		return ret;
	}

	@RequestMapping(value = "/app/checkout/singleship/save")
	@ResponseBody
	public Map<String, Object> setShipAddress(
			@ModelAttribute("shippingInfoForm") MyShippingInfoForm shippingForm,
			BindingResult result) {
		Map<String, Object> ret = new HashMap<String, Object>(1);

		Order cart = CartState.getCart();
		if (cart == null || cart instanceof NullOrderImpl) {
			ret.put("error", "null_order");
			ret.put("errorMessage", "您的购物车是空的哦～");
			return ret;
		}
		processShippingForm(shippingForm, result);
		if (result.hasErrors()) {
			ret.put("error", "error_address");
			ret.put("errorMessage", "地址输入不正确");
			return ret;
		}

		MyAddress myAddress = shippingForm.getMyAddress();
		List<FulfillmentGroup> fgs = cart.getFulfillmentGroups();
		if (fgs == null || fgs.isEmpty()) {
			ret.put("error", "error_no_fg");
			return ret;
		}

		FulfillmentGroup group = fgs.get(0);
		group.setAddress(myAddress);
		try {
			fulfillmentGroupService.save(group);
		} catch (DataAccessException e) {
			ret.put("error", "error_db_access");
			return ret;
		}
		return ret;
	}

	@RequestMapping(value = "/app/checkout/submit")
	@ResponseBody
	public Map<String, Object> checkoutSubmitApp(HttpServletRequest request,
			HttpServletResponse response, Model model,
			@RequestParam("paymentType") String paymentType) throws Exception {
		Order cart = CartState.getCart();
		OrderInfoForm orderInfoForm = new OrderInfoForm();
		MyBillingInfoForm billingForm = new MyBillingInfoForm();
		MyShippingInfoForm shippingForm = new MyShippingInfoForm();

		// address already saved
		prepopulateCheckoutForms(cart, orderInfoForm, shippingForm, billingForm);
		shippingForm.setPaymentMethod(paymentType);
		shippingForm.setBp_pay(cart.getTotal().getAmount());
		String detial = request.getParameter("detial");
		shippingForm.getMyAddress().setLastName(detial);
		/*
		 * TODO app验证用户是否为remember登录，使用余额登陆。
		 */

		Map<String, Object> ret = new HashMap<String, Object>(1);
		if (shippingForm.getMyAddress().getDormitory() == null) {
			ret.put("error", "error_address");
			ret.put("errorMessage", "地址输入不正确");
			return ret;
		}

		try {
			this.saveSingleShip0(request, response, model, orderInfoForm,
					billingForm, shippingForm, new BeanPropertyBindingResult(
							shippingForm, ""), true);
			ret.put("orderNum", cart.getOrderNumber());
			ret.put("itemCount", cart.getItemCount());
			ret.put("totalShipping", cart.getTotalFulfillmentCharges());
			ret.put("total", cart.getTotal());
			// TODO 改掉
			List<PaymentInfo> paymentInfos = cart.getPaymentInfos();
			if (paymentInfos != null && paymentInfos.size() > 0) {
				PaymentInfoType payType = paymentInfos.get(0).getType();
				ret.put("payType", payType.getFriendlyType());
			}
		} catch (CheckoutException e) {
			LOG.error("", e);
			ret.put("error", "error_checkout");
			if (e.getCause() instanceof InventoryUnavailableException) {
				ret.put("errorMessage", "抱歉，东西被抢光了，下次出手要快哦");
			} else if (e.getCause() instanceof InsufficientFundsException) {
				ret.put("errorMessage", "抱歉，你余额不足哈");
			} else
				ret.put("errorMessage", "抱歉，提交订单失败");
		} catch (PricingException e) {
			LOG.error("", e);
			ret.put("error", "error_price");
			ret.put("errorMessage", "抱歉，提交订单失败");
		} catch (ServiceException e) {
			LOG.error("", e);
			ret.put("error", "error_service");
			ret.put("errorMessage", "抱歉，提交订单失败");
		}
		return ret;
	}

	private String getAddressName(MyAddress address, boolean withRoomNo) {
		if (address.getDormitory() == null)
			return "";
		String s = withRoomNo ? address.toString() : address.getDormitory()
				.toString();
		return getAbrevStr(s, 15);
	}

	private String getAbrevStr(String s, int len) {
		if (s != null && s.length() > len) {
			s = s.substring(0, 5) + " ... " + s.substring(s.length() - 7);
		}
		return s;
	}

	public Boolean executeExpression(String expression, Map<String, Object> vars) {
		try {
			Serializable exp;
			synchronized (EXPRESSION_CACHE) {
				exp = (Serializable) EXPRESSION_CACHE.get(expression);
				if (exp == null) {
					ParserContext context = new ParserContext();
					context.addImport("OfferType", OfferType.class);
					context.addImport("FulfillmentType", FulfillmentType.class);
					context.addImport("MVEL", MVEL.class);
					context.addImport("MvelHelper", MvelHelper.class);
					// StringBuffer completeExpression = new
					// StringBuffer(functions.toString());
					// completeExpression.append(" ").append(expression);
					exp = MVEL.compileExpression(expression, context);

					EXPRESSION_CACHE.put(expression, exp);
				}
			}

			Object test = MVEL.executeExpression(exp, vars);

			return (Boolean) test;
		} catch (Exception e) {
			// Unable to execute the MVEL expression for some reason
			// Return false, but notify about the bad expression through logs
			LOG.warn(
					"Unable to parse and/or execute an mvel expression. Reporting to the logs and returning false "
							+ "for the match expression:" + expression, e);
			return false;
		}
	}

	List<Long> ids = Arrays.asList(-1263L, 701L, 703L, 702L, -12111L, -109862L,
			-1091L, 663L, 664L, 665L, 661L, -12150L, -109780L);

	public boolean ifCanAdd(List<Order> orders) {
		int count = 0;
		boolean cotain = false;
		Iterator<Order> itro = orders.iterator();
		while (itro.hasNext()) {
			Order eg = (Order) itro.next();
			List<OrderItem> orderitems = eg.getOrderItems();
			for (OrderItem item : orderitems) {
				if (!(item instanceof SkuAccessor))
					continue;
				if (ids.contains(((SkuAccessor) item).getSku().getId())) {
					count++;
					// break;
					return true;
				}
			}
		}
		if (count >= 1)
			cotain = true;

		return cotain;
	}
	
	@RequestMapping(value = "/weixin/checkout")
	public String weixinSaveGlobalOrderDetails(
			HttpServletRequest request,
			Model model,
			@ModelAttribute("orderInfoForm") OrderInfoForm orderInfoForm,
			@ModelAttribute("shippingInfoForm") MyShippingInfoForm shippingForm,
			@ModelAttribute("billingInfoForm") MyBillingInfoForm billingForm,
			BindingResult result) {
		MyCustomer myCustomer = (MyCustomer) CustomerState.getCustomer();
		Region region = myCustomer.getRegion();
		if (region == null) {
			return "redirect:/weixin/region";
		}
		SimpleDateFormat dateformat1 = new SimpleDateFormat("HH:mm");
		String date = dateformat1.format(new Date());
		String[] times = region.getShipping_time().split(";");
		boolean isOutDeliveryDateRange = true;
		for (String time : times) {
			String[] shipping_time = time.split("-");
			if ((date.compareTo(shipping_time[0]) > 0)
					&& (date.compareTo(shipping_time[1]) < 0))
				isOutDeliveryDateRange = false;
		}
		model.addAttribute("isOutDeliveryDateRange", isOutDeliveryDateRange);

		prepopulateCheckoutForms(CartState.getCart(), orderInfoForm,
				shippingForm, billingForm);
		super.saveGlobalOrderDetails(request, model, orderInfoForm,
				result);
		return "weixin/cart/w_checkout";
	}
	
	@RequestMapping(value = "/weixin/checkout/singleship", method = RequestMethod.POST)
	public String weixinSaveSingleShip(
			HttpServletRequest request,
			HttpServletResponse response,
			Model model,
			@ModelAttribute("orderInfoForm") OrderInfoForm orderInfoForm,
			@ModelAttribute("billingInfoForm") MyBillingInfoForm billingForm,
			@ModelAttribute("shippingInfoForm") MyShippingInfoForm shippingForm,
			BindingResult result) throws Exception {

		return saveSingleShip0(request, response, model, orderInfoForm,
				billingForm, shippingForm, result, false);

	}
}