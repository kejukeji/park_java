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

package com.ssbusy.controller.rechargeablecard;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.broadleafcommerce.common.web.controller.BroadleafAbstractController;
import org.broadleafcommerce.profile.web.core.CustomerState;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ssbusy.core.account.service.MyCustomerService;
import com.ssbusy.core.rechargeablecard.domain.TransactionDetails;
import com.ssbusy.core.rechargeablecard.service.RechargeableCardService;
import com.ssbusy.core.rechargeablecard.service.TransactionDetailsService;

@Controller
@RequestMapping("/account/rechargeableCard")
public class RechargeableCardController extends BroadleafAbstractController {

	@Resource(name = "ssbRechargeableCardService")
	public RechargeableCardService rechargeableCardService;

	@Resource(name = "blCustomerService")
	protected MyCustomerService myCustomerService;

	@Resource(name = "ssbTransactionDetailsService")
	protected TransactionDetailsService transactionDetailsService;

	@RequestMapping
	public String viewUpdateAccount(HttpServletRequest request, Model model) {
		Long customerid = CustomerState.getCustomer().getId();
		List<TransactionDetails> transactionDetails = transactionDetailsService
				.getTransactionDetails(customerid);
		model.addAttribute("transactionDetails",transactionDetails);
//		List<RechargeableCard> cards = rechargeableCardService
//				.getRechargeableCard(customerid);
//		model.addAttribute("cards", cards);
		model.addAttribute("balance",
				myCustomerService.getCustomerBalance(customerid).setScale(2));
		return "account/transactionDetails";
	}

}