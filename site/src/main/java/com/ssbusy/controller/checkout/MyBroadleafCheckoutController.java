package com.ssbusy.controller.checkout;

import java.beans.PropertyEditorSupport;
import java.math.BigDecimal;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.broadleafcommerce.common.exception.ServiceException;
import org.broadleafcommerce.common.money.Money;
import org.broadleafcommerce.common.vendor.service.exception.FulfillmentPriceException;
import org.broadleafcommerce.common.web.BroadleafRequestContext;
import org.broadleafcommerce.core.checkout.service.exception.CheckoutException;
import org.broadleafcommerce.core.checkout.service.workflow.CheckoutResponse;
import org.broadleafcommerce.core.offer.service.OfferService;
import org.broadleafcommerce.core.order.domain.FulfillmentGroup;
import org.broadleafcommerce.core.order.domain.FulfillmentOption;
import org.broadleafcommerce.core.order.domain.NullOrderImpl;
import org.broadleafcommerce.core.order.domain.Order;
import org.broadleafcommerce.core.payment.domain.PaymentInfo;
import org.broadleafcommerce.core.payment.domain.Referenced;
import org.broadleafcommerce.core.payment.service.PaymentInfoFactory;
import org.broadleafcommerce.core.payment.service.SecurePaymentInfoService;
import org.broadleafcommerce.core.payment.service.type.PaymentInfoType;
import org.broadleafcommerce.core.pricing.service.exception.PricingException;
import org.broadleafcommerce.core.pricing.service.fulfillment.provider.FulfillmentEstimationResponse;
import org.broadleafcommerce.core.web.checkout.model.MultiShipInstructionForm;
import org.broadleafcommerce.core.web.checkout.model.OrderInfoForm;
import org.broadleafcommerce.core.web.checkout.model.OrderMultishipOptionForm;
import org.broadleafcommerce.core.web.checkout.model.ShippingInfoForm;
import org.broadleafcommerce.core.web.checkout.validator.BillingInfoFormValidator;
import org.broadleafcommerce.core.web.controller.checkout.AbstractCheckoutController;
import org.broadleafcommerce.core.web.order.CartState;
import org.broadleafcommerce.inventory.service.InventoryService;
import org.broadleafcommerce.profile.core.domain.Address;
import org.broadleafcommerce.profile.core.domain.Country;
import org.broadleafcommerce.profile.core.domain.Customer;
import org.broadleafcommerce.profile.core.domain.CustomerAddress;
import org.broadleafcommerce.profile.core.domain.State;
import org.broadleafcommerce.profile.web.core.CustomerState;
import org.joda.time.DateTime;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ssbusy.core.account.domain.MyCustomer;
import com.ssbusy.core.account.service.MyCustomerService;
import com.ssbusy.core.domain.AreaAddress;
import com.ssbusy.core.domain.MyAddress;
import com.ssbusy.core.domain.MyAddressImpl;
import com.ssbusy.core.inneraddress.service.AreaService;
import com.ssbusy.core.inneraddress.service.DormitoryService;
import com.ssbusy.core.region.domain.Region;
import com.ssbusy.payment.service.type.AlipayPaymentInfo;
import com.ssbusy.payment.service.type.BpPaymentInfo;
import com.ssbusy.payment.service.type.CodPaymentInfo;
import com.ssbusy.payment.service.type.IntegrlPaymentInfo;
import com.ssbusy.payment.service.type.MyPaymentInfoType;
import com.ssbusy.site.myshippingform.MyBillingInfoForm;

/*
 * MyBroadleafCheckoutControlle继承BroadleafCheckoutController 添加了货到付款的信息
 * 
 * 
 * 
 * 
 * */

public class MyBroadleafCheckoutController extends AbstractCheckoutController {

	private static final Log LOG = LogFactory
			.getLog(MyBroadleafCheckoutController.class);

	protected static String cartPageRedirect = "redirect:/cart";
	protected static String checkoutView = "checkout/checkout";
	protected static String checkoutAddress = "checkout/checkout-step-2";
	protected static String checkoutPageRedirect = "redirect:/checkout";
	protected static String multishipView = "checkout/multiship";
	protected static String multishipAddAddressView = "checkout/multishipAddAddressForm";
	protected static String multishipAddAddressSuccessView = "redirect:/checkout/multiship";
	protected static String multishipSuccessView = "redirect:/checkout";
	protected static String baseConfirmationView = "ajaxredirect:/confirmation";

	@Resource(name = "ssbDefaultFulfillmentOption")
	protected FulfillmentOption defaultFulfillmentOption;

	@Resource(name = "blCodPaymentInfoFactory")
	protected PaymentInfoFactory codPaymentInfoFactory;

	@Resource(name = "blBpPaymentInfoFactory")
	protected PaymentInfoFactory bpPaymentInfoFactory;

	@Resource(name = "alipayPaymentInfoFactory")
	protected PaymentInfoFactory alipayPaymentInfoFactory;

	@Resource(name = "blIntegrlPaymentInfoFactory")
	protected PaymentInfoFactory integrlPaymentInfoFactory;

	@Resource(name = "blBillingInfoFormValidator")
	protected BillingInfoFormValidator billingInfoFormValidator;

	@Resource(name = "blInSecurePaymentInfoService")
	protected SecurePaymentInfoService insecurePaymentInfoService;

	@Resource(name = "alipaySecurePaymentInfoService")
	protected SecurePaymentInfoService alipaySecurePaymentInfoService;

	@Resource(name = "ssbAreaService")
	protected AreaService areaService;

	@Resource(name = "ssbDormitoryService")
	protected DormitoryService dormitoryService;

	@Resource(name = "blBpSecurePaymentInfoService")
	protected SecurePaymentInfoService bpsecurePaymentInfoService;

	@Resource(name = "blInventoryService")
	protected InventoryService inventoryService;

	@Resource(name="blOfferService")
	protected OfferService offerService;

	@Resource(name = "blCustomerService")
	protected MyCustomerService myCustomerService;
	/**
	 * Renders the default checkout page.
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return the return path
	 */
	public String checkout(HttpServletRequest request,
			HttpServletResponse response, Model model,
			RedirectAttributes redirectAttributes) {
		return getCheckoutView();
	}

	/**
	 * Converts the order to singleship by collapsing all of the fulfillment
	 * groups into the default one
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return a redirect to /checkout
	 * @throws PricingException
	 */
	public String convertToSingleship(HttpServletRequest request,
			HttpServletResponse response, Model model) throws PricingException {
		Order cart = CartState.getCart();
		fulfillmentGroupService.collapseToOneFulfillmentGroup(cart, true);
		return getCheckoutPageRedirect();
	}

	/**
	 * Attempts to attach the user's email to the order so that they may proceed
	 * anonymously. Also set fulfillOption to default.
	 * 
	 * @param request
	 * @param model
	 * @param errors
	 * @param emailAddress
	 * @return the return path
	 * @throws ServiceException
	 */
	public String saveGlobalOrderDetails(HttpServletRequest request,
			Model model, OrderInfoForm orderInfoForm, BindingResult result) {
		Order cart = CartState.getCart();
		Boolean w_flag = (Boolean)request.getSession().getAttribute("w_flag");
		if (cart == null || (cart instanceof NullOrderImpl)) {
			if(w_flag!=null&&w_flag){
				return  "weixin/cart/w_cart";
			}else{
				return checkoutView;
			}
		}

		// TODO 仅多收货地址(multiship)时有效
		// model.addAttribute("orderMultishipOptions",
		// orderMultishipOptionService
		// .getOrGenerateOrderMultishipOptions(cart));

		List<FulfillmentGroup> groups = cart.getFulfillmentGroups();
		if (groups != null && defaultFulfillmentOption != null
				&& groups.size() > 0) {
			for (FulfillmentGroup g : groups) {
				if (g.getFulfillmentOption() == null) {
					// TODO 目前我们只支持一种配送方式
					g.setFulfillmentOption(fulfillmentOptionService
							.readFulfillmentOptionById(defaultFulfillmentOption
									.getId()));
				}
			}
		}
		populateModelWithShippingReferenceData(request, model);
		try {
			cart.setEmailAddress(orderInfoForm.getEmailAddress());
			orderService.save(cart, false);
		} catch (PricingException pe) {
			LOG.error(
					"Error when saving the email address for order confirmation to the cart",
					pe);
		}
		MyCustomer myCustomer = (MyCustomer) CustomerState.getCustomer();
		BigDecimal balance = myCustomer.getBalance();
		BigDecimal total = cart.getTotal().getAmount();
		if (balance.compareTo(total) > -1) {
			model.addAttribute("balance_pay", total);
		} else {
			model.addAttribute("balance_pay", balance);
		}
		model.addAttribute("alipay", total);
		return checkoutAddress;
	}

	/*
	 * override save single ship
	 */

	public String savePaymentForm(HttpServletRequest request,
			HttpServletResponse response, Model model) throws PricingException {

		return isAjaxRequest(request) ? getCheckoutView()
				: getCheckoutPageRedirect();
	}

	/**
	 * Renders the multiship page. This page is used by the user when shipping
	 * items to different locations (or with different FulfillmentOptions) is
	 * desired.
	 * 
	 * Note that the default Broadleaf implementation will require the user to
	 * input an Address and FulfillmentOption for each quantity of each
	 * DiscreteOrderItem.
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return the return path
	 */
	public String showMultiship(HttpServletRequest request,
			HttpServletResponse response, Model model) {
		Customer customer = CustomerState.getCustomer();
		Order cart = CartState.getCart();
		model.addAttribute("orderMultishipOptions", orderMultishipOptionService
				.getOrGenerateOrderMultishipOptions(cart));
		model.addAttribute("customerAddresses", customerAddressService
				.readActiveCustomerAddressesByCustomerId(customer.getId()));
		model.addAttribute("fulfillmentOptions",
				fulfillmentOptionService.readAllFulfillmentOptions());
		return getMultishipView();
	}

	/**
	 * Processes the given options for multiship. Validates that all options are
	 * selected before performing any actions.
	 * 
	 * @see #showMultiship(HttpServletRequest, HttpServletResponse, Model)
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @param orderMultishipOptionForm
	 * @return a redirect to the checkout page
	 * @throws PricingException
	 * @throws ServiceException
	 */
	public String saveMultiship(HttpServletRequest request,
			HttpServletResponse response, Model model,
			OrderMultishipOptionForm orderMultishipOptionForm,
			BindingResult result) throws PricingException, ServiceException {
		Order cart = CartState.getCart();
		orderMultishipOptionService.saveOrderMultishipOptions(cart,
				orderMultishipOptionForm.getOptions());
		cart = fulfillmentGroupService
				.matchFulfillmentGroupsToMultishipOptions(cart, true);
		return getMultishipSuccessView();
	}

	/**
	 * Renders the add address form during the multiship process
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return the return path
	 */
	public String showMultishipAddAddress(HttpServletRequest request,
			HttpServletResponse response, Model model) {
		model.addAttribute("states", stateService.findStates());
		model.addAttribute("countries", countryService.findCountries());
		return getMultishipAddAddressView();
	}

	/**
	 * Processes the requested add address from the multiship process. This
	 * method will create a CustomerAddress based on the requested Address and
	 * associate it with the current Customer in session.
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @param addressForm
	 * @return the return path to the multiship page
	 * @throws ServiceException
	 */
	public String saveMultishipAddAddress(HttpServletRequest request,
			HttpServletResponse response, Model model,
			ShippingInfoForm addressForm, BindingResult result)
			throws ServiceException {
		multishipAddAddressFormValidator.validate(addressForm, result);
		if (result.hasErrors()) {
			return showMultishipAddAddress(request, response, model);
		}

		Address address = addressService.saveAddress(addressForm.getAddress());

		CustomerAddress customerAddress = customerAddressService.create();
		customerAddress.setAddressName(addressForm.getAddressName());
		customerAddress.setAddress(address);
		customerAddress.setCustomer(CustomerState.getCustomer());
		customerAddressService.saveCustomerAddress(customerAddress);

		// append current time to redirect to fix a problem with ajax caching in
		// IE
		return getMultishipAddAddressSuccessView() + "?_="
				+ System.currentTimeMillis();
	}

	public String saveMultiShipInstruction(HttpServletRequest request,
			HttpServletResponse response, Model model,
			MultiShipInstructionForm instructionForm) throws ServiceException,
			PricingException {
		Order cart = CartState.getCart();
		FulfillmentGroup fulfillmentGroup = null;

		for (FulfillmentGroup tempFulfillmentGroup : cart
				.getFulfillmentGroups()) {
			if (tempFulfillmentGroup.getId().equals(
					instructionForm.getFulfillmentGroupId())) {
				fulfillmentGroup = tempFulfillmentGroup;
			}
		}
		fulfillmentGroup.setPersonalMessage(instructionForm
				.getPersonalMessage());
		fulfillmentGroup.setDeliveryInstruction(instructionForm
				.getDeliveryMessage());
		fulfillmentGroupService.save(fulfillmentGroup);

		// append current time to redirect to fix a problem with ajax caching in
		// IE
		return getCheckoutPageRedirect() + "?_=" + System.currentTimeMillis();
	}

	/**
	 * Processes the request to complete checkout
	 * 
	 * If the paymentMethod is undefined or "creditCard" delegates to the
	 * "completeSecureCreditCardCheckout" method.
	 * 
	 * Otherwise, returns an operation not supported.
	 * 
	 * This method assumes that a credit card payment info will be either sent
	 * to a third party gateway or saved in a secure schema. If the transaction
	 * is successful, the order will be assigned an order number, its status
	 * change to SUBMITTED, and given a submit date. The method then returns the
	 * default confirmation path "/confirmation/{orderNumber}"
	 * 
	 * If the transaction is unsuccessful, (e.g. the gateway declines payment)
	 * processFailedOrderCheckout() is called and reverses the state of the
	 * order.
	 * 
	 * Note: this method removes any existing payment infos of type CREDIT_CARD
	 * and re-creates it with the information from the BillingInfoForm
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @param billingForm
	 * @return the return path
	 * @throws ServiceException
	 */
	public String completeCheckout(HttpServletRequest request,
			HttpServletResponse response, Model model,
			MyBillingInfoForm billingForm, BindingResult result)
			throws CheckoutException, PricingException, ServiceException {
		LOG.info(billingForm.getPaymentMethod());
		/*
		 * if (billingForm.getPaymentMethod() == null ||
		 * "cod".equals(billingForm.getPaymentMethod())) { return
		 * completeCodCheckout(request, response, model, billingForm, result); }
		 * else if ("balance_pay".equals(billingForm.getPaymentMethod())) {
		 * return completeBpCheckout(request, response, model, billingForm,
		 * result); } else { throw new IllegalArgumentException(
		 * "Complete checkout called with payment Method " +
		 * billingForm.getPaymentMethod() + " which has not been implemented.");
		 * }
		 */
		return null;
	}

	/**
	 * Processes the request to complete checkout using a Credit Card
	 * 
	 * This method assumes that a credit card payment info will be either sent
	 * to a third party gateway or saved in a secure schema. If the transaction
	 * is successful, the order will be assigned an order number, its status
	 * change to SUBMITTED, and given a submit date. The method then returns the
	 * default confirmation path "/confirmation/{orderNumber}"
	 * 
	 * If the transaction is unsuccessful, (e.g. the gateway declines payment)
	 * processFailedOrderCheckout() is called and reverses the state of the
	 * order.
	 * 
	 * Note: this method removes any existing payment infos of type CREDIT_CARD
	 * and re-creates it with the information from the BillingInfoForm
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @param billingForm
	 * @return the return path
	 * @throws ServiceException
	 */

	public String completeCodCheckout(HttpServletRequest request,
			HttpServletResponse response, Model model,
			MyBillingInfoForm billingForm, BindingResult result)
			throws CheckoutException, PricingException, ServiceException {
		Order cart = CartState.getCart();
		Boolean w_flag = (Boolean)request.getSession().getAttribute("w_flag");
		if (cart != null && !(cart instanceof NullOrderImpl)) {

			copyShippingAddressToBillingAddress(cart, billingForm);
			billingInfoFormValidator.validate(billingForm, result);
			if (result.hasErrors()) {
				populateModelWithShippingReferenceData(request, model);
				return getCheckoutView();
			}
			// 先把原有支付信息清理掉
			cart.getPaymentInfos().clear();
			// 创建codPaymentInfo
			PaymentInfo ccInfo = codPaymentInfoFactory
					.constructPaymentInfo(cart);
			ccInfo.setAddress(billingForm.getMyAddress());
			cart.getPaymentInfos().add(ccInfo);

			CodPaymentInfo ccreference = (CodPaymentInfo) securePaymentInfoService
					.create(MyPaymentInfoType.Payment_Cod);
			ccreference.setMessage("success");
			ccreference.setReferenceNumber("tese");

			Map<PaymentInfo, Referenced> payments = new HashMap<PaymentInfo, Referenced>();
			payments.put(ccInfo, ccreference);

			CheckoutResponse checkoutResponse = checkoutService
					.performCheckout(cart, payments);
			if (!checkoutResponse.getPaymentResponse().getResponseItems()
					.get(ccInfo).getTransactionSuccess()) {
				populateModelWithShippingReferenceData(request, model);
				// result.rejectValue("creditCardNumber", "payment.exception",
				// null, null);
				// return getCheckoutView();
			}
			if(w_flag!=null&&w_flag){
				return  "ajaxredirect:/weixin/confirmation/"+cart.getOrderNumber();
			}else{
				return getConfirmationView(cart.getOrderNumber());
			}
		}

		return getCartPageRedirect();
	}

	/**
	 * This method will copy the shipping address of the first fulfillment group
	 * on the order to the billing address on the BillingInfoForm that is passed
	 * in.
	 * 
	 * @param billingInfoForm
	 */
	@SuppressWarnings("deprecation")
	protected void copyShippingAddressToBillingAddress(Order order,
			MyBillingInfoForm billingInfoForm) {
		if (order.getFulfillmentGroups().get(0) != null) {
			Address shipping = order.getFulfillmentGroups().get(0).getAddress();
			if (shipping != null) {
				MyAddress billing = new MyAddressImpl();
				billing.setFirstName(shipping.getFirstName());
				billing.setLastName(shipping.getLastName());
				billing.setAddressLine1(shipping.getAddressLine1());
				billing.setAddressLine2(shipping.getAddressLine2());
				billing.setCity(shipping.getCity());
				billing.setState(shipping.getState());
				billing.setPostalCode(shipping.getPostalCode());
				billing.setCountry(shipping.getCountry());
				billing.setPrimaryPhone(shipping.getPrimaryPhone());
				billing.setEmailAddress(shipping.getEmailAddress());
				billingInfoForm.setMyAddress(billing);
			}
		}
	}

	/**
	 * A helper method used to determine the validity of the fulfillment groups
	 * 
	 * @param cart
	 * @return boolean indicating whether or not the fulfillment groups on the
	 *         cart have addresses.
	 */
	protected boolean hasValidShippingAddresses(Order cart) {
		if (cart.getFulfillmentGroups() == null) {
			return false;
		}

		for (FulfillmentGroup fulfillmentGroup : cart.getFulfillmentGroups()) {
			if (fulfillmentGroup.getAddress() == null
					|| fulfillmentGroup.getFulfillmentOption() == null) {
				return false;
			}
		}
		return true;
	}

	/**
	 * A helper method used to determine the validity of order info
	 * 
	 * @param cart
	 * @return boolean indicating whether or not the order has valid info
	 */
	protected boolean hasValidOrderInfo(Order cart) {
		return StringUtils.isNotBlank(cart.getEmailAddress());
	}

	/**
	 * A helper method to retrieve all fulfillment options for the cart
	 */
	protected void putFulfillmentOptionsAndEstimationOnModel(Model model) {
		List<FulfillmentOption> fulfillmentOptions = fulfillmentOptionService
				.readAllFulfillmentOptions();
		Order cart = CartState.getCart();

		if (!(cart instanceof NullOrderImpl)
				&& cart.getFulfillmentGroups().size() > 0
				&& hasValidShippingAddresses(cart)) {
			Set<FulfillmentOption> options = new HashSet<FulfillmentOption>();
			options.addAll(fulfillmentOptions);
			FulfillmentEstimationResponse estimateResponse = null;
			try {
				estimateResponse = fulfillmentPricingService
						.estimateCostForFulfillmentGroup(cart
								.getFulfillmentGroups().get(0), options);
			} catch (FulfillmentPriceException e) {
				LOG.error("", e);
			}
			model.addAttribute("estimateResponse", estimateResponse);
		}
		model.addAttribute("fulfillmentOptions", fulfillmentOptions);
	}

	/**
	 * A helper method used to construct a list of Credit Card Expiration Months
	 * Useful for expiration dropdown menus. Will use locale to determine
	 * language if a locale is available.
	 * 
	 * @return List containing expiration months of the form "01 - January"
	 */
	protected List<String> populateExpirationMonths() {
		DateFormatSymbols dateFormatter;
		if (BroadleafRequestContext.hasLocale()) {
			Locale locale = BroadleafRequestContext
					.getBroadleafRequestContext().getJavaLocale();
			dateFormatter = new DateFormatSymbols(locale);
		} else {
			dateFormatter = new DateFormatSymbols();
		}
		List<String> expirationMonths = new ArrayList<String>();
		NumberFormat formatter = new DecimalFormat("00");
		String[] months = dateFormatter.getMonths();
		for (int i = 1; i < months.length; i++) {
			expirationMonths.add(formatter.format(i) + " - " + months[i - 1]);
		}
		return expirationMonths;
	}

	/**
	 * A helper method used to construct a list of Credit Card Expiration Years
	 * Useful for expiration dropdown menus.
	 * 
	 * @return List of the next ten years starting with the current year.
	 */
	protected List<String> populateExpirationYears() {
		List<String> expirationYears = new ArrayList<String>();
		DateTime dateTime = new DateTime();
		for (int i = 0; i < 10; i++) {
			expirationYears.add(dateTime.plusYears(i).getYear() + "");
		}
		return expirationYears;
	}

	/**
	 * Initializes some custom binding operations for the checkout flow. More
	 * specifically, this method will attempt to bind state and country
	 * abbreviations to actual State and Country objects when the String
	 * representation of the abbreviation is submitted.
	 * 
	 * @param request
	 * @param binder
	 * @throws Exception
	 */
	protected void initBinder(HttpServletRequest request,
			ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(State.class, "address.state",
				new PropertyEditorSupport() {
					@Override
					public void setAsText(String text) {
						State state = stateService
								.findStateByAbbreviation(text);
						setValue(state);
					}
				});

		binder.registerCustomEditor(Country.class, "address.country",
				new PropertyEditorSupport() {
					@Override
					public void setAsText(String text) {
						Country country = countryService
								.findCountryByAbbreviation(text);
						setValue(country);
					}
				});
		// binder.registerCustomEditor(Phone.class, "address.phonePrimary",
		// new PropertyEditorSupport() {
		//
		// @Override
		// public void setAsText(String text) {
		// if (!StringUtils.isBlank(text)) {
		// Phone phone = new PhoneImpl();
		// phone.setPhoneNumber(text);
		// setValue(phone);
		// } else {
		// setValue(null);
		// }
		// }
		// });
	}

	protected void populateModelWithShippingReferenceData(
			HttpServletRequest request, Model model) {
		// email强制废弃了
		// String editOrderInfo = request.getParameter("edit-order-info");
		// boolean hasValidOrderInfo;
		// if (BooleanUtils.toBoolean(editOrderInfo)) {
		// hasValidOrderInfo = false;
		// } else {
		// hasValidOrderInfo = hasValidOrderInfo(CartState.getCart());
		// }
		model.addAttribute("validOrderInfo", true);

		String editShipping = request.getParameter("edit-shipping");
		boolean hasValidShipping;
		if (BooleanUtils.toBoolean(editShipping)) {
			hasValidShipping = false;
		} else {
			hasValidShipping = hasValidShippingAddresses(CartState.getCart());
		}
		model.addAttribute("validShipping", hasValidShipping);

		// TODO 送货方式暂时只有筋斗云配送 @see #defaultFulfillmentOption
		// putFulfillmentOptionsAndEstimationOnModel(model);

		model.addAttribute("states", stateService.findStates());
		model.addAttribute("countries", countryService.findCountries());
		model.addAttribute("expirationMonths", populateExpirationMonths());
		model.addAttribute("expirationYears", populateExpirationYears());

		Region region = null;
		MyCustomer myCustomer = (MyCustomer) CustomerState.getCustomer();
		region = myCustomer.getRegion();
		List<AreaAddress> areas = areaService.listAreasByRegion(region);
		model.addAttribute("areas", areas);
		MyAddress myAddress = getAddressByRegion(region);
		if (myAddress != null) {
			model.addAttribute("dormitorys", dormitoryService
					.listDormitoriesByAreaAddress(myAddress.getDormitory()
							.getAreaAddress()));
		} else {
			if (!areas.isEmpty() && areas.size() == 1) {
				model.addAttribute("dormitorys", dormitoryService
						.listDormitoriesByAreaAddress(areas.get(0)));
			} else
				model.addAttribute("dormitorys", null);
		}
	}

	protected MyAddress getAddressByRegion(Region region) {
		MyAddress myAddress = null;
		List<CustomerAddress> customerAddresses = customerAddressService
				.readActiveCustomerAddressesByCustomerId(CustomerState
						.getCustomer().getId());
		boolean flag = false;
		for (CustomerAddress cs : customerAddresses) {
			myAddress = (MyAddress) cs.getAddress();
			if (region.equals(myAddress.getDormitory().getAreaAddress()
					.getRegion())) {
				flag = true;
				break;
			}

		}
		if (flag)
			return myAddress;
		else
			return null;
	}

	public String complexCheckout(HttpServletRequest request,
			HttpServletResponse response, Model model,
			MyBillingInfoForm billingForm, BindingResult result,
			PaymentInfoType paymentInfoType, Long orderId)
			throws CheckoutException {
		Order cart = orderService.findOrderById(orderId);
		// CartState.getCart();
		if (cart != null) {
			copyShippingAddressToBillingAddress(cart, billingForm);
			billingInfoFormValidator.validate(billingForm, result);
			if (result.hasErrors()) {
				populateModelWithShippingReferenceData(request, model);
				return getCheckoutView();
			}
			cart.getPaymentInfos().clear();
			PaymentInfo info = null;
			Referenced ref = null;
			if (MyPaymentInfoType.Payment_Bp.equals(paymentInfoType)) {
				info = bpPaymentInfoFactory.constructPaymentInfo(cart);
				info.setAmount(new Money(billingForm.getBp_pay(), info
						.getCurrency()));
				ref = (BpPaymentInfo) bpsecurePaymentInfoService
						.create(paymentInfoType);
			} else if (MyPaymentInfoType.Payment_Alipay.equals(paymentInfoType)) {
				info = alipayPaymentInfoFactory.constructPaymentInfo(cart);
				info.setAmount(new Money(billingForm.getAlipay(), info
						.getCurrency()));
				ref = (AlipayPaymentInfo) alipaySecurePaymentInfoService
						.create(paymentInfoType);
			} else {
				// TODO 目前仅支持支付宝 和 余额 与到付结合
			}
			info.setAddress(billingForm.getMyAddress());
			cart.getPaymentInfos().add(info);
			ref.setReferenceNumber("try");
			PaymentInfo codInfo = codPaymentInfoFactory
					.constructPaymentInfo(cart);
			codInfo.setAddress(billingForm.getMyAddress());
			cart.getPaymentInfos().add(codInfo);

			CodPaymentInfo codreference = (CodPaymentInfo) securePaymentInfoService
					.create(MyPaymentInfoType.Payment_Cod);
			codreference.setReferenceNumber("tese");
			Map<PaymentInfo, Referenced> payments = new HashMap<PaymentInfo, Referenced>(
					2);
			payments.put(info, ref);
			payments.put(codInfo, codreference);
			CheckoutResponse checkoutResponse = checkoutService
					.performCheckout(cart, payments);
			if (!checkoutResponse.getPaymentResponse().getResponseItems()
					.get(info).getTransactionSuccess()) {
				populateModelWithShippingReferenceData(request, model);
				result.rejectValue("Bp", "no enough balance", null, null);
				return getCheckoutView();
			}

			return getConfirmationView(cart.getOrderNumber());
		}

		return getCartPageRedirect();
	}

	// 余额付款模块
	public String completeBpCheckout(HttpServletRequest request,
			HttpServletResponse response, Model model,
			MyBillingInfoForm billingForm, BindingResult result)
			throws CheckoutException {

		Order cart = CartState.getCart();
		if (cart != null) {
			Map<PaymentInfo, Referenced> payments = new HashMap<PaymentInfo, Referenced>();
			orderService.removePaymentsFromOrder(cart,
					MyPaymentInfoType.Payment_Bp);
			copyShippingAddressToBillingAddress(cart, billingForm);
			billingInfoFormValidator.validate(billingForm, result);
			if (result.hasErrors()) {
				populateModelWithShippingReferenceData(request, model);
				return getCheckoutView();
			}
			// 先把原有支付信息清理掉
			cart.getPaymentInfos().clear();
			PaymentInfo bpInfo = bpPaymentInfoFactory
					.constructPaymentInfo(cart);
			bpInfo.setAddress(billingForm.getMyAddress());
			cart.getPaymentInfos().add(bpInfo);

			BpPaymentInfo bpreference = (BpPaymentInfo) bpsecurePaymentInfoService
					.create(MyPaymentInfoType.Payment_Bp);
			bpreference.setMessage("success");
			bpreference.setReferenceNumber("try");

			payments.put(bpInfo, bpreference);
			CheckoutResponse checkoutResponse = checkoutService
					.performCheckout(cart, payments);
			if (!checkoutResponse.getPaymentResponse().getResponseItems()
					.get(bpInfo).getTransactionSuccess()) {
				populateModelWithShippingReferenceData(request, model);
				result.rejectValue("Bp", "no enough balance", null, null);
				return getCheckoutView();
			}

			return getConfirmationView(cart.getOrderNumber());
		}

		return getCartPageRedirect();

	}

	// 积分仙丹付款模块
	public String completeIntegrlCheckout(HttpServletRequest request,
			HttpServletResponse response, Model model,
			MyBillingInfoForm billingForm, BindingResult result)
			throws CheckoutException, PricingException, ServiceException {

		Order cart = CartState.getCart();
		if (cart != null) {
			Map<PaymentInfo, Referenced> payments = new HashMap<PaymentInfo, Referenced>();
			orderService.removePaymentsFromOrder(cart,
					MyPaymentInfoType.Payment_Integrl);
			copyShippingAddressToBillingAddress(cart, billingForm);
			billingInfoFormValidator.validate(billingForm, result);
			if (result.hasErrors()) {
				populateModelWithShippingReferenceData(request, model);
				return getCheckoutView();
			}

			// 先把原有支付信息清理掉
			cart.getPaymentInfos().clear();
			PaymentInfo ccInfo = integrlPaymentInfoFactory
					.constructPaymentInfo(cart);
			ccInfo.setAddress(billingForm.getMyAddress());
			cart.getPaymentInfos().add(ccInfo);
			IntegrlPaymentInfo inreference = (IntegrlPaymentInfo) insecurePaymentInfoService
					.create(MyPaymentInfoType.Payment_Integrl);
			inreference.setMessage("success");
			inreference.setReferenceNumber("try");
			payments.put(ccInfo, inreference);
			CheckoutResponse checkoutResponse = checkoutService
					.performCheckout(cart, payments);
			if (!checkoutResponse.getPaymentResponse().getResponseItems()
					.get(ccInfo).getTransactionSuccess()) {
				populateModelWithShippingReferenceData(request, model);
				result.rejectValue("In", "no enough Integrl", null, null);
				return getCheckoutView();
			}
			return getConfirmationView(cart.getOrderNumber());
		}

		return getCartPageRedirect();

	}

	public String getCartPageRedirect() {
		return cartPageRedirect;
	}

	public String getCheckoutView() {
		return checkoutView;
	}

	public String getCheckoutPageRedirect() {
		return checkoutPageRedirect;
	}

	public String getMultishipView() {
		return multishipView;
	}

	public String getMultishipAddAddressView() {
		return multishipAddAddressView;
	}

	public String getMultishipSuccessView() {
		return multishipSuccessView;
	}

	public String getMultishipAddAddressSuccessView() {
		return multishipAddAddressSuccessView;
	}

	public String getBaseConfirmationView() {
		return baseConfirmationView;
	}

	protected String getConfirmationView(String orderNumber) {
		return getBaseConfirmationView() + "/" + orderNumber;
	}

}
