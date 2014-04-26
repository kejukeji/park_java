package com.ssbusy.controller.account;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.broadleafcommerce.profile.web.core.CustomerState;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ssbusy.checkout.validator.SsbRechargeFormValidator;
import com.ssbusy.controller.rechargeablecard.RechargeForm;
import com.ssbusy.core.account.service.BalanceChangeType;
import com.ssbusy.core.account.service.MyCustomerService;
import com.ssbusy.core.rechargeablecard.service.RechargeableCardService;

@Controller
public class RechargeController {

	@Resource(name = "ssbRechargeableCardService")
	protected RechargeableCardService rechargeableCardService;
	@Resource(name = "blCustomerService")
	protected MyCustomerService myCustomerService;
	@Resource(name = "ssbRechargeFormValidator")
	protected SsbRechargeFormValidator ssbRechargeFormValidator;

	protected static String rechargePage = "account/recharge";
	@RequestMapping("/account/recharge")
	public String rechargeView(HttpServletRequest request, Model model,
			@ModelAttribute("rechargeForm") RechargeForm form) {
		return rechargePage;
	}

	@RequestMapping("/account/rechargeSubmit")
	public String recharge(HttpServletRequest request, Model model,
			@ModelAttribute("rechargeForm") RechargeForm form,
			BindingResult result) {
		String card_id = form.getCard_id();
		String card_password = form.getCard_password();
		Long customerid = CustomerState.getCustomer().getId();
		ssbRechargeFormValidator.validate(form, result);
		if (result.hasErrors()) {
			return rechargePage;
		}

		String rechargeMessage = "充值失败 ";
		float value = rechargeableCardService.getRechargeableCardValue(card_id,
				card_password);
		if (value > 0) {
			/*判断是否改用户是否已经使用onceforcustomer充值卡*/
			if(rechargeableCardService.isSecondUseOnceCard(customerid,card_id)){
				 rechargeMessage = "此类型充值卡只能充值一次！";			 
			}else{
				if (rechargeableCardService.tagRechargeableCardToCustomer(
						customerid, card_id)) {
					/*
					 * TODO判断充值卡类型
					 * */
					if (myCustomerService.rechargeToAccountBalance(customerid,
							BigDecimal.valueOf(value),BalanceChangeType.RECHARGE)) {
						rechargeMessage = "充值成功，面值为" + value;
					}
				}
			}
		} else if (value < 0) {
			rechargeMessage = "抱歉，该卡已过期...";
		}
		model.addAttribute("rechargeMessage", rechargeMessage);
		return rechargePage;
	}
	
	
	
	@RequestMapping("/app/account/rechargeSubmit")
	@ResponseBody
	public Map<String,Object> appRecharge(HttpServletRequest request, Model model,
			@ModelAttribute("re_number") String card_id,@ModelAttribute("re_pwd") String card_password) {
		Long customerid = CustomerState.getCustomer().getId();
		String rechargeMessage = "哎呀，充值失败了~~~";
		Map<String, Object> ret = new HashMap<String, Object>(2);
		if("".equals(card_id)|| card_id==null ||"".equals(card_password)||card_password==null){
			ret.put("error", "error_recaharge");
			ret.put("errorMessage", "卡号为空了~~");
			return ret;
		}
		float value = rechargeableCardService.getRechargeableCardValue(card_id,
				card_password);
		if (value > 0) {
			/*判断是否改用户是否已经使用onceforcustomer充值卡*/
			if(rechargeableCardService.isSecondUseOnceCard(customerid,card_id)){
				 rechargeMessage = "此类型充值卡只能充值一次~~";			 
			}else{
				if (rechargeableCardService.tagRechargeableCardToCustomer(
						customerid, card_id)) {
					/*
					 * TODO判断充值卡类型
					 * */
					if (myCustomerService.rechargeToAccountBalance(customerid,
							BigDecimal.valueOf(value),BalanceChangeType.RECHARGE)) {
						ret.put("sucess", "sucess_recharge");
						ret.put("sucessMessage", "真兴奋，充值成功了，充了" + value+"元呢~~~");
						return ret;
					}
				}
			}
		} else if (value < 0) {
			rechargeMessage = "抱歉，该卡已过期...";
		}
		ret.put("error", "error_recharge");
		ret.put("errorMessage", rechargeMessage);
		return ret;
	}
}
