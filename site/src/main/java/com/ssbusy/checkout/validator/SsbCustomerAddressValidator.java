package com.ssbusy.checkout.validator;

import org.broadleafcommerce.core.web.controller.account.validator.CustomerAddressValidator;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

public class SsbCustomerAddressValidator extends CustomerAddressValidator {
	 @SuppressWarnings("rawtypes")
	    public boolean supports(Class clazz) {
	        return clazz.equals(SsbCustomerAddressValidator.class);
	    }

	    public void validate(Object obj, Errors errors) {

	    	ValidationUtils.rejectIfEmptyOrWhitespace(errors, "myAddress.firstName", "firstName.required");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "myAddress.addressLine3", "addressLine3.required");
	        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "myAddress.roomNo", "roomNo.required");
	        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "myAddress.primaryPhone", "primaryPhone.required");
	    }
}
