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

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.broadleafcommerce.cms.file.domain.StaticAsset;
import org.broadleafcommerce.cms.file.service.StaticAssetService;
import org.broadleafcommerce.cms.file.service.StaticAssetStorageService;
import org.broadleafcommerce.common.exception.ServiceException;
import org.broadleafcommerce.core.web.controller.account.BroadleafUpdateAccountController;
import org.broadleafcommerce.profile.core.domain.CustomerPhone;
import org.broadleafcommerce.profile.core.domain.CustomerPhoneImpl;
import org.broadleafcommerce.profile.core.domain.Phone;
import org.broadleafcommerce.profile.core.domain.PhoneImpl;
import org.broadleafcommerce.profile.core.service.CustomerPhoneService;
import org.broadleafcommerce.profile.core.service.PhoneService;
import org.broadleafcommerce.profile.web.core.CustomerState;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.ssbusy.core.account.domain.MyCustomer;
import com.ssbusy.core.account.service.MyCustomerService;
import com.ssbusy.core.region.domain.Region;

@Controller
public class UpdateAccountController extends BroadleafUpdateAccountController {
	@Resource(name = "blCustomerService")
	protected MyCustomerService myCustomerService;
	
	@Resource(name = "blPhoneService")
	protected PhoneService phoneService;
	
	@Resource(name="blCustomerPhoneService")
	protected CustomerPhoneService customerPhoneService;
	
	@Resource(name = "blStaticAssetStorageService")
    protected StaticAssetStorageService staticAssetStorageService;
    
    @Resource(name = "blStaticAssetService")
    protected StaticAssetService staticAssetService;
    
	
	@RequestMapping(value="/account", method = RequestMethod.GET)
	public String viewUpdateAccount(
			@ModelAttribute("updateAccountBasicInfoForm") UpdateAccountBasicInfoForm form) {
		Long customerId = CustomerState.getCustomer().getId();
		MyCustomer myCustomer = myCustomerService.getMyCustomerById(customerId);
		//Region region = (Region) session.getAttribute("region");
		Region region = (Region)myCustomer.getRegion();
		if (region != null) {
			myCustomerService.setRegion(region, CustomerState.getCustomer().getId());
		}
		form.setUseremail(myCustomer.getEmailAddress());
		form.setFirstName(myCustomer.getFirstName());
		form.setSex(myCustomer.getSex());
		List<CustomerPhone> customerPhones= myCustomer.getCustomerPhones();
		if(customerPhones!=null&&customerPhones.size()>0){
			form.setPhone(customerPhones.get(0).getPhone().getPhoneNumber());
		}
		if(myCustomer.getAvatarUrl()!=null){
			form.setAvatarUrl(myCustomer.getAvatarUrl());
		}else{
			form.setAvatarUrl("img/Onxiao_userimg.gif");
		}
		
		//session.setAttribute("region", myCustomer.getRegion());
		return super.getUpdateAccountView();
	}

	@RequestMapping(value = "/account", method = RequestMethod.POST)
	public String processUpdateAccount(
			Model model,
			@ModelAttribute("updateAccountBasicInfoForm") UpdateAccountBasicInfoForm form,
			@RequestParam("file") MultipartFile file) throws ServiceException, IOException {
		Long customerId = CustomerState.getCustomer().getId();
		MyCustomer myCustomer = myCustomerService.getMyCustomerById(customerId);
		if(form.getFirstName().matches("^\\S{1,16}$"))
			myCustomer.setFirstName(form.getFirstName());
		myCustomer.setSex(form.getSex());
		List<CustomerPhone> customerPhones= myCustomer.getCustomerPhones();
		if(form.getPhone().matches("^[1-9]{1}[0-9]{5,10}$"))
			if(customerPhones!=null&&customerPhones.size()>0){
				customerPhones.get(0).getPhone().setPhoneNumber(form.getPhone());
			}else{
				Phone phone = new PhoneImpl();
				phone.setPhoneNumber(form.getPhone());
				phoneService.savePhone(phone);
				CustomerPhone customerPhone = new CustomerPhoneImpl();
				customerPhone.setPhone(phone);
				customerPhone.setCustomer(myCustomer);
				customerPhoneService.saveCustomerPhone(customerPhone);
				customerPhones.add(customerPhone);
			}
		if((file.getSize()<2000000))
			updateAvatarUrl(myCustomer,file);
		myCustomerService.saveCustomer(myCustomer);
		return super.getAccountRedirectView();
	}
	
	@RequestMapping(value="/account/avatarUrl", method=RequestMethod.POST)
	public void updateAvatarUrl(MyCustomer myCustomer,MultipartFile file) throws IOException {
		if (!file.isEmpty()) {
			Map<String, String> properties = new HashMap<String, String>();
			properties.put("entityType", "account");
	        properties.put("entityId", "avatarUrl");
			properties.put("fileName", myCustomer.getId().toString());
			StaticAsset staticAsset = staticAssetService.createStaticAssetFromFile(file, properties);
			staticAssetStorageService.createStaticAssetStorageFromFile(file, staticAsset);
			myCustomer.setAvatarUrl("/cmsstatic"+staticAsset.getFullUrl());
		 }
	}


	// app ////////////////////////////////////////////

	@RequestMapping(value = "/app/account/info")
	@ResponseBody
	public Map<String, Object> accountInfoApp() {
		Long customerId = CustomerState.getCustomer().getId();
		MyCustomer c = myCustomerService.getMyCustomerById(customerId);
		if(c == null)
			return Collections.emptyMap();

		Map<String, Object> ret = new HashMap<String, Object>(2);
		ret.put("name", c.getFirstName());
		ret.put("scores", c.getIntegral());
		ret.put("avatar", c.getAvatarUrl());
		return ret;
	}
}

