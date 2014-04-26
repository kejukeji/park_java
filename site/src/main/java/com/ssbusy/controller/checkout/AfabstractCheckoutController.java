package com.ssbusy.controller.checkout;

import javax.annotation.Resource;

import org.broadleafcommerce.core.payment.service.PaymentInfoFactory;
import org.broadleafcommerce.core.payment.service.SecurePaymentInfoService;
import org.broadleafcommerce.core.web.checkout.validator.BillingInfoFormValidator;
import org.broadleafcommerce.core.web.controller.checkout.AbstractCheckoutController;

import com.ssbusy.core.account.service.MyCustomerService;
import com.ssbusy.core.inneraddress.service.AreaService;
import com.ssbusy.core.inneraddress.service.DormitoryService;

/**
 * 
 * @author ChenLinLin AfabstractCheckoutController 
 *         AbstractCheckoutController add property PaymentInfoFactory
 *         CodPaymentInfoFactory
 */
public abstract class AfabstractCheckoutController extends
		AbstractCheckoutController {

	@Resource(name = "blCodPaymentInfoFactory")
	protected PaymentInfoFactory codPaymentInfoFactory;
	
	@Resource(name = "blBpPaymentInfoFactory")
	protected PaymentInfoFactory bpPaymentInfoFactory;

	@Resource(name = "blIntegrlPaymentInfoFactory")
	protected PaymentInfoFactory integrlPaymentInfoFactory;

	@Resource(name = "blBillingInfoFormValidator")
	protected BillingInfoFormValidator billingInfoFormValidator;

	@Resource(name = "blSecurePaymentInfoService")
	protected SecurePaymentInfoService securePaymentInfoService;

	@Resource(name = "blBpSecurePaymentInfoService")
	protected SecurePaymentInfoService bpsecurePaymentInfoService;
	
	@Resource(name="blInSecurePaymentInfoService")
	protected SecurePaymentInfoService insecurePaymentInfoService;
	
	// get the MyCustomer info
	@Resource(name = "blCustomerService")
	protected MyCustomerService myCustomerService;

	@Resource(name = "ssbAreaService")
	protected AreaService areaService;

	@Resource(name = "ssbDormitoryService")
	protected DormitoryService dormitoryService;
	
}
