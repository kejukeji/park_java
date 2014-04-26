package com.ssbusy.register.validator;

/*
 * Copyright 2008-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import javax.annotation.Resource;

import org.apache.commons.validator.GenericValidator;
import org.broadleafcommerce.profile.core.domain.Customer;
import org.broadleafcommerce.profile.core.service.CustomerService;
import org.broadleafcommerce.profile.web.controller.validator.RegisterCustomerValidator;
import org.broadleafcommerce.profile.web.core.form.RegisterCustomerForm;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * @author bpolster
 */

public class MyRegisterCustomerValidator extends RegisterCustomerValidator
		implements Validator {

	@Resource(name = "blCustomerService")
	private CustomerService customerService;

	public void validate(Object obj, Errors errors, boolean useEmailForUsername) {
		RegisterCustomerForm form = (RegisterCustomerForm) obj;

		Customer customerFromDb = customerService.readCustomerByUsername(form
				.getCustomer().getUsername());

		if (customerFromDb != null) {
			if (useEmailForUsername) {
				errors.rejectValue("customer.emailAddress",
						"emailAddress.used", null, null);
			} else {
				errors.rejectValue("customer.username", "username.used", null,
						null);
			}
		}

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password",
				"password.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "passwordConfirm",
				"passwordConfirm.required");

		errors.pushNestedPath("customer");
		// ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName",
		// "firstName.required");
		// ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastName",
		// "lastName.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "emailAddress",
				"emailAddress.required");
		errors.popNestedPath();

		if (!errors.hasErrors()) {

			if (form.getPassword().length() < 4
					|| form.getPassword().length() > 15) {
				errors.rejectValue("password", "password.invalid", null, null);
			}

			if (!form.getPassword().equals(form.getPasswordConfirm())) {
				errors.rejectValue("password", "passwordConfirm.invalid", null,
						null);
			}

			if (!GenericValidator.isEmail(form.getCustomer().getEmailAddress())) {
				errors.rejectValue("customer.emailAddress",
						"emailAddress.invalid", null, null);
			}
		}
	}
}
