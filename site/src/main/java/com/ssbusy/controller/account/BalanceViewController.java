package com.ssbusy.controller.account;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.broadleafcommerce.profile.web.core.CustomerState;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ssbusy.core.account.service.MyCustomerService;

@Controller
@RequestMapping("account/balance")

public class BalanceViewController {
	@Resource(name = "blCustomerService")
	protected MyCustomerService myCustomerService;
	@RequestMapping
	public String viewBalance(HttpServletRequest request, Model model){
		Long customerid =  CustomerState.getCustomer().getId(); 
		model.addAttribute("balance", myCustomerService.getCustomerBalance(customerid));
		return "account/balance";
	}
}
