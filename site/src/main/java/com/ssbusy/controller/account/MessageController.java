package com.ssbusy.controller.account;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.broadleafcommerce.profile.web.core.CustomerState;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ssbusy.core.message.service.MessageService;


@Controller
@RequestMapping("account/message")
public class MessageController {
	
	@Resource(name = "MessageService")
	protected MessageService messageService; 
	@RequestMapping
	public String viewBalance(HttpServletRequest request, Model model){
		Long customerId =  CustomerState.getCustomer().getId(); 
		model.addAttribute("messages", messageService.listMessagesBycustomer(customerId));
		return "account/messages";
	}
}
