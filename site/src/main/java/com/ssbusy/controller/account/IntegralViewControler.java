package com.ssbusy.controller.account;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.broadleafcommerce.profile.core.domain.Customer;
import org.broadleafcommerce.profile.web.core.CustomerState;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ssbusy.core.account.domain.MyCustomer;
import com.ssbusy.core.integral.domain.Integral;
import com.ssbusy.core.integral.service.IntegralService;

@Controller
public class IntegralViewControler {

	@Resource(name = "ssbIntegralService")
	protected IntegralService integralService;

	@RequestMapping("/account/viewIntegral")
	public String viewIntegral(HttpServletRequest request, Model model) {
		Customer myCustomer = CustomerState.getCustomer();
		model.addAttribute("customerIntegral",
				integralService.viewCustomerIntegral(myCustomer.getId()));
		model.addAttribute("integralHistoryList",
				integralService.getIntegralHistoryList(myCustomer.getId()));
		return "account/integral";
	}

	// app /////////////////////////////////
	@RequestMapping("/app/account/score-details")
	@ResponseBody
	public Map<String, Object> viewIntegralApp(HttpServletRequest request,
			Model model) {
		Customer myCustomer = CustomerState.getCustomer();
		Map<String, Object> ret = new HashMap<String, Object>(3);
		ret.put("totalScore",
				integralService.viewCustomerIntegral(myCustomer.getId()));
		ret.put("details", wrapScores(integralService
				.getIntegralHistoryList(myCustomer.getId())));

		MyCustomer c = (MyCustomer) CustomerState.getCustomer();
		ret.put("totalBalance", c.getBalance());
		return ret;
	}

	private List<Map<String, Object>> wrapScores(List<Integral> list) {
		if (list == null || list.isEmpty())
			return Collections.emptyList();

		List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>(
				list.size());
		for (Integral i : list) {
			if(i.getChangeQuantity()!=0){
				Map<String, Object> m = new HashMap<String, Object>(3);
				m.put("date", format(i.getChangeDate()));
				m.put("quantity", i.getChangeQuantity());
				m.put("type", i.getChangeType());
				ret.add(m);
			}
		}
		return ret;
	}

	private String format(Date d) {
		if (d == null)
			return "";
		return new SimpleDateFormat("MM-dd").format(d);
	}

}
