package com.ssbusy.checkout.validator;

import org.broadleafcommerce.core.web.checkout.validator.ShippingInfoFormValidator;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import com.ssbusy.site.myshippingform.MyShippingInfoForm;
/*
 *
 *
 *
 *
 */

public class SsbShippingInfoFormValidator extends ShippingInfoFormValidator{

	public SsbShippingInfoFormValidator() {
		super();
	}

	@SuppressWarnings("rawtypes") 
	public boolean supports(Class clazz) {
		
		return clazz.equals(SsbShippingInfoFormValidator.class);
	}

	@SuppressWarnings("deprecation")
	public void validate(Object target, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "myAddress.firstName", "firstName.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "myAddress.addressLine3", "addressLine3.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "myAddress.roomNo", "roomNo.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "myAddress.primaryPhone", "primaryPhone.required");
        // ValidationUtils.rejectIfEmptyOrWhitespace(errors, "bp_pay", "bp_pay.required");
        // ValidationUtils.rejectIfEmptyOrWhitespace(errors, "alipay", "alipay.required");
        MyShippingInfoForm myShippingInfoForm = (MyShippingInfoForm) target;
        String regex = "^([1-9]{1}[0-9]{5})|(1[0-9]{10})|([1-9]{1}[0-9]{7})$";
        if(!myShippingInfoForm.getMyAddress().getPrimaryPhone().matches(regex))
        		errors.rejectValue("myAddress.primaryPhone", "primaryPhone.required");
	}

}
