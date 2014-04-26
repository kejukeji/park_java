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

import java.beans.PropertyEditorSupport;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.broadleafcommerce.common.exception.ServiceException;
import org.broadleafcommerce.core.web.controller.account.BroadleafManageCustomerAddressesController;
import org.broadleafcommerce.core.web.controller.account.validator.CustomerAddressValidator;
import org.broadleafcommerce.profile.core.domain.Address;
import org.broadleafcommerce.profile.core.domain.Country;
import org.broadleafcommerce.profile.core.domain.CustomerAddress;
import org.broadleafcommerce.profile.core.domain.State;
import org.broadleafcommerce.profile.core.service.AddressService;
import org.broadleafcommerce.profile.core.service.CountryService;
import org.broadleafcommerce.profile.core.service.CustomerAddressService;
import org.broadleafcommerce.profile.core.service.StateService;
import org.broadleafcommerce.profile.web.core.CustomerState;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ssbusy.core.account.domain.MyCustomer;
import com.ssbusy.core.domain.AreaAddress;
import com.ssbusy.core.domain.Dormitory;
import com.ssbusy.core.domain.MyAddress;
import com.ssbusy.core.inneraddress.service.AreaService;
import com.ssbusy.core.inneraddress.service.DormitoryService;
import com.ssbusy.core.region.domain.Region;
import com.ssbusy.site.myshippingform.MyCustomerAddressForm;

@Controller
@RequestMapping("/account/addresses")
public class ManageCustomerAddressesController extends
		BroadleafManageCustomerAddressesController {

	@Resource(name = "ssbAreaService")
	private AreaService areaService;
	@Resource(name = "blCustomerAddressService")
	private CustomerAddressService customerAddressService;
	@Resource(name = "ssbDormitoryService")
	protected DormitoryService dormitoryService;
	@Resource(name = "blCustomerAddressValidator")
    private CustomerAddressValidator myCustomerAddressValidator;
    @Resource(name = "blAddressService")
    private AddressService addressService;
    @Resource(name = "blStateService")
    private StateService stateService;
    @Resource(name = "blCountryService")
    private CountryService countryService;

	@InitBinder
	protected void initBinder(HttpServletRequest request,
			ServletRequestDataBinder binder) throws Exception {
		//super.initBinder(request, binder);
		 binder.registerCustomEditor(State.class, "address.state", new PropertyEditorSupport() {
	            @Override
	            public void setAsText(String text) {
	                State state = stateService.findStateByAbbreviation(text);
	                setValue(state);
	            }
	        });

	        binder.registerCustomEditor(Country.class, "address.country", new PropertyEditorSupport() {
	            @Override
	            public void setAsText(String text) {
	                Country country = countryService.findCountryByAbbreviation(text);
	                setValue(country);
	            }
	        });

	}

	@ModelAttribute("states")
	protected List<State> populateStates() {
		return super.populateStates();
	}

	@ModelAttribute("countries")
	protected List<Country> populateCountries() {
		return super.populateCountries();
	}

	@ModelAttribute("customerAddresses")
	protected List<CustomerAddress> populateCustomerAddresses() {
		return super.populateCustomerAddresses();
	}

	@RequestMapping(method = RequestMethod.GET)
	public String viewCustomerAddresses(HttpServletRequest request, Model model) {
		  model.addAttribute("customerAddressForm", new MyCustomerAddressForm());
		  return getCustomerAddressesView();
	}

	@RequestMapping(method = RequestMethod.POST)
	public String addCustomerAddress(HttpServletRequest request, Model model,
			@ModelAttribute("customerAddressForm") MyCustomerAddressForm form,
			BindingResult result, RedirectAttributes redirectAttributes)
			throws ServiceException {

		form.getMyAddress().setState(form.getAddress().getState());
		form.getMyAddress().setCountry(form.getAddress().getCountry());
		form.getMyAddress().setCity("city");
		form.getMyAddress().setPostalCode("310018");
		myCustomerAddressValidator.validate(form, result);
        if (result.hasErrors()) {
            return getCustomerAddressesView();
        }
        Long dormitoryId = Long.parseLong(form.getMyAddress().getAddressLine3());
		Dormitory dormitory = dormitoryService.loadDormitotyById(dormitoryId);
		form.getMyAddress().setDormitory(dormitory);
		form.getMyAddress().setAddressLine1(dormitory.getAreaAddress().getRegion().getRegionName());
        Address address = addressService.saveAddress(form.getMyAddress());
        CustomerAddress customerAddress = customerAddressService.create();
        customerAddress.setAddress(address);
        customerAddress.setAddressName(form.getAddressName());
        customerAddress.setCustomer(CustomerState.getCustomer());
        customerAddress = customerAddressService.saveCustomerAddress(customerAddress);
        if (form.getAddress().isDefault()) {
            customerAddressService.makeCustomerAddressDefault(customerAddress.getId(), customerAddress.getCustomer().getId());
        }
        if (!isAjaxRequest(request)) {
            List<CustomerAddress> addresses = customerAddressService.readActiveCustomerAddressesByCustomerId(CustomerState.getCustomer().getId());
            model.addAttribute("addresses", addresses);
        }
        redirectAttributes.addFlashAttribute("successMessage", "添加地址成功");
        return getCustomerAddressesRedirect();
	}

	@SuppressWarnings("deprecation")
	@RequestMapping(value = "/{customerAddressId}", method = RequestMethod.GET)
	public String viewCustomerAddress(HttpServletRequest request, Model model,
			@PathVariable("customerAddressId") Long customerAddressId) {
		CustomerAddress customerAddress = customerAddressService
				.readCustomerAddressById(customerAddressId);
		if (customerAddress == null) {
			throw new IllegalArgumentException(
					"Customer Address not found with the specified customerAddressId");
		}
		MyCustomerAddressForm form = new MyCustomerAddressForm();
		
		MyAddress myAddress = (MyAddress) customerAddress.getAddress();
		model.addAttribute("dormitorys",myAddress.getDormitory());
		form.getAddress().setPrimaryPhone(myAddress.getPrimaryPhone());
		form.setMyAddress(myAddress);
		form.setAddressName(customerAddress.getAddressName());
		form.setCustomerAddressId(customerAddress.getId());
		model.addAttribute("customerAddressForm", form);
		return getCustomerAddressesView();
	}

	@RequestMapping(value = "/{customerAddressId}", method = RequestMethod.POST)
	public String updateCustomerAddress(HttpServletRequest request,
			Model model,
			@PathVariable("customerAddressId") Long customerAddressId,
			@ModelAttribute("customerAddressForm") MyCustomerAddressForm form,
			BindingResult result, RedirectAttributes redirectAttributes)
			throws ServiceException {
		
		form.getMyAddress().setState(form.getAddress().getState());
		form.getMyAddress().setCountry(form.getAddress().getCountry());
		Long dormitoryId = Long.parseLong(form.getMyAddress().getAddressLine3());
		Dormitory dormitory = dormitoryService.loadDormitotyById(dormitoryId);
		form.getMyAddress().setDormitory(dormitory);
		form.getMyAddress().setAddressLine1(dormitory.getAreaAddress().getRegion().getRegionName());
		form.getMyAddress().setCity("city");
		form.getMyAddress().setPostalCode("310018");
		myCustomerAddressValidator.validate(form, result);
        if (result.hasErrors()) {
            return getCustomerAddressesView();
        }
        CustomerAddress customerAddress = customerAddressService.readCustomerAddressById(customerAddressId);
        if (customerAddress == null) {
            throw new IllegalArgumentException("Customer Address not found with the specified customerAddressId");
        }
        customerAddress.setAddress(form.getMyAddress());
        customerAddress.setAddressName(form.getAddressName());
        customerAddress = customerAddressService.saveCustomerAddress(customerAddress);
        if (form.getAddress().isDefault()) {
            customerAddressService.makeCustomerAddressDefault(customerAddress.getId(), customerAddress.getCustomer().getId());
        }
        redirectAttributes.addFlashAttribute("successMessage", "修改地址成功");
        return getCustomerAddressesRedirect();
	}

	@RequestMapping(value = "/{customerAddressId}", method = RequestMethod.POST, params ="removeAddress=删除地址")
	public String removeCustomerAddress(HttpServletRequest request,
			Model model,
			@PathVariable("customerAddressId") Long customerAddressId,
			RedirectAttributes redirectAttributes) {
		return super.removeCustomerAddress(request, model, customerAddressId,
				redirectAttributes);
	}

	@ModelAttribute("areas")
	protected List<AreaAddress> getAreaByRegion() {
		Region re = ((MyCustomer) CustomerState.getCustomer()).getRegion();
		return areaService.listAreasByRegion(re);
	}

}
