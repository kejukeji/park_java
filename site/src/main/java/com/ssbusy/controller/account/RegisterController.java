/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ssbusy.controller.account;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.broadleafcommerce.common.exception.ServiceException;
import org.broadleafcommerce.core.web.controller.account.BroadleafSocialRegisterController;
import org.broadleafcommerce.profile.core.domain.Customer;
import org.broadleafcommerce.profile.web.core.form.RegisterCustomerForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * The controller responsible for registering a customer
 */
@Controller
public class RegisterController extends BroadleafSocialRegisterController {

	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String register(
			HttpServletRequest request,
			HttpServletResponse response,
			Model model,
			@ModelAttribute("registrationForm") RegisterCustomerForm registerCustomerForm) {
		return super.register(registerCustomerForm, request, response, model);
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String processRegister(
			HttpServletRequest request,
			HttpServletResponse response,
			Model model,
			@ModelAttribute("registrationForm") RegisterCustomerForm registerCustomerForm,
			BindingResult errors) throws ServiceException {
		Customer customer = registerCustomerForm.getCustomer();
		int place = customer.getEmailAddress() == null ? -1 : customer
				.getEmailAddress().indexOf("@");
		if (place >= 0) {
			customer.setFirstName(customer.getEmailAddress()
					.substring(0, place));
		}
		return super.processRegister(registerCustomerForm, errors, request,
				response, model);
	}

	@ModelAttribute("registrationForm")
	public RegisterCustomerForm initCustomerRegistrationForm() {
		return super.initCustomerRegistrationForm();
	}

	// app //////////////////////////////////////////////
	@RequestMapping(value = "/app/register")
	@ResponseBody
	public Map<String, Object> processRegisterApp(
			HttpServletRequest request,
			HttpServletResponse response,
			Model model,
			@ModelAttribute("registrationForm") RegisterCustomerForm registerCustomerForm,
			BindingResult errors) throws ServiceException {
		this.processRegister(request, response, model, registerCustomerForm,
				errors);
		Map<String, Object> ret = new HashMap<String, Object>();
		if (errors.hasErrors()) {
			ret.put("error", errors.getAllErrors().get(0).getCode());
		}

		return ret;
	}
}
