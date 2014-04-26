package com.ssbusy.checkout.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;


@Component("ssbRechargeFormValidator")
public class SsbRechargeFormValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return clazz.equals(SsbRechargeFormValidator.class);
	}

	@Override
	public void validate(Object target, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "card_id",
				"rechargeForm.card_id.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "card_password",
				"rechargeForm.card_password.required");
	}

}
