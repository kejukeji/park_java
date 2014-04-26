package com.ssbusy.controller.activities;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.broadleafcommerce.profile.web.core.CustomerState;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ssbusy.core.account.domain.MyCustomer;
import com.ssbusy.core.christmas.domain.ChristmasCard;
import com.ssbusy.core.christmas.service.ChristmasCardService;
import com.ssbusy.core.rechargeablecard.domain.RechargeableCard;
import com.ssbusy.core.rechargeablecard.service.RechargeableCardService;

@Controller("christmasCardController")
public class ChristmasCardController {

	@Resource(name = "christmasCardService")
	protected ChristmasCardService christmasCardService;

	@Resource(name = "ssbRechargeableCardService")
	protected RechargeableCardService rechargeableCardService;

	@RequestMapping("/activity/page/xmas-tree")
	public void viewXmasTree(HttpServletRequest request, Model model) {
	}

	@RequestMapping("/activity/christmassign")
		public String viewChristmasSign(HttpServletRequest request, Model model) {
		MyCustomer myCustomer = (MyCustomer) CustomerState.getCustomer();
		List<ChristmasCard> christmasCards = christmasCardService
				.loadChristmasCard(myCustomer.getId());
		String signed = ".d_1225";
		for (int i = 0; i < christmasCards.size(); i++) {
			signed = signed + ",.d_" + christmasCards.get(i).getSignDate();
		}
		model.addAttribute("signed", signed);
		String socks = "d_1225";
		String bell = "d_1225";
		String bigBell = "d_1225";
		String bounl = "d_1225";
		String socksCss = "";
		String bellCss = "";
		String bigBellCss = "";
		String bounlCss = "";
		int a = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		int times = 0;

		int over = 0;
		if (!christmasCards.isEmpty()) {
			times = christmasCards.size() / 5;
			over = christmasCards.size() % 5;
		}
		boolean signdFlag = false;
		for (ChristmasCard cc : christmasCards) {
			if(a==cc.getSignDate()){
				signdFlag = true;
				break;
			}
		}
		if(signdFlag){
			over = over-1;
		}
		if (times == 0) {
			over = 4-over+a;
			socks = "d_" + over;
			int top = 26;
			int left = 50;
			left = over % 7 == 0 ? 7 * 41 - 74 : (over % 7) * 41 - 74;
			if (over <= 14) {
				top = 26;
			}
			if ((over > 14) && (over <= 21)) {
				top = 62;
			}
			if ((over > 21) && (over <= 28)) {
				top = 97;
			}
			if(over!=a){
				socksCss = "."
					+ socks
					+ ":hover .socks_hover{display:block;position:absolute;top:"
					+ top + "px;left:" + left + "px;}";
			}
			over = over + 5;
			left = over % 7 == 0 ? 7 * 41 - 74 : (over % 7) * 41 - 74;
			if (over <= 14) {
				top = 26;
			}
			if ((over > 14) && (over <= 21)) {
				top = 62;
			}
			if ((over > 21) && (over <= 28)) {
				top = 97;
			}
			bell = "d_" + over;
			bellCss = "." + bell
					+ ":hover .bell_hover{display:block;position:absolute;top:"
					+ top + "px;left:" + left + "px;}";
			over = over + 5;
			left = over % 7 == 0 ? 7 * 41 - 74 : (over % 7) * 41 - 74;
			if (over <= 14) {
				top = 26;
			}
			if ((over > 14) && (over <= 21)) {
				top = 62;
			}
			if ((over > 21) && (over <= 28)) {
				top = 97;
			}
			bigBell = "d_" + over;
			bigBellCss = "."
					+ bigBell
					+ ":hover .bigbell_hover{display:block;position:absolute;top:"
					+ top + "px;left:" + left + "px;}";
			over = over + 5;
			left = over % 7 == 0 ? 7 * 41 - 74 : (over % 7) * 41 - 74;
			if (over <= 14) {
				top = 26;
			}
			if ((over > 14) && (over <= 21)) {
				top = 62;
			}
			if ((over > 21) && (over <= 28)) {
				top = 97;
			}
			bounl = "d_" + over;
			bounlCss = "."
					+ bounl
					+ ":hover .bounl_hover{display:block;position:absolute;top:"
					+ top + "px;left:" + left + "px;}";
		} else if (times == 1) {
			over = 4-over+a;
			int top = 26;
			int left = 50;
			left = over % 7 == 0 ? 7 * 41 - 74 : (over % 7) * 41 - 74;
			if (over <= 14) {
				top = 26;
			}
			if ((over > 14) && (over <= 21)) {
				top = 62;
			}
			if ((over > 21) && (over <= 28)) {
				top = 97;
			}
			bell = "d_" + over;
			if(over!=a){
				bellCss = "." + bell
						+ ":hover .bell_hover{display:block;position:absolute;top:"
						+ top + "px;left:" + left + "px;}";
			}
			over = over + 5;
			left = over % 7 == 0 ? 7 * 41 - 74 : (over % 7) * 41 - 74;
			if (over <= 14) {
				top = 26;
			}
			if ((over > 14) && (over <= 21)) {
				top = 62;
			}
			if ((over > 21) && (over <= 28)) {
				top = 97;
			}
			bigBell = "d_" + over;
			bigBellCss = "."
					+ bigBell
					+ ":hover .bigbell_hover{display:block;position:absolute;top:"
					+ top + "px;left:" + left + "px;}";
			over = over + 5;
			left = over % 7 == 0 ? 7 * 41 - 74 : (over % 7) * 41 - 74;
			if (over <= 14) {
				top = 26;
			}
			if ((over > 14) && (over <= 21)) {
				top = 62;
			}
			if ((over > 21) && (over <= 28)) {
				top = 97;
			}
			bounl = "d_" + over;
			bounlCss = "."
					+ bounl
					+ ":hover .bounl_hover{display:block;position:absolute;top:"
					+ top + "px;left:" + left + "px;}";
		} else if (times == 2) {
			over = 4-over+a;
			int top = 26;
			int left = 50;
			left = over % 7 == 0 ? 7 * 41 - 74 : (over % 7) * 41 - 74;
			if (over <= 14) {
				top = 26;
			}
			if ((over > 14) && (over <= 21)) {
				top = 62;
			}
			if ((over > 21) && (over <= 28)) {
				top = 97;
			}
			bigBell = "d_" + over;
			if(over!=a){
				bigBellCss = "."
						+ bigBell
						+ ":hover .bigbell_hover{display:block;position:absolute;top:"
						+ top + "px;left:" + left + "px;}";
			}
			over = over + 5;
			left = over % 7 == 0 ? 7 * 41 - 74 : (over % 7) * 41 - 74;
			if (over <= 14) {
				top = 26;
			}
			if ((over > 14) && (over <= 21)) {
				top = 62;
			}
			if ((over > 21) && (over <= 28)) {
				top = 97;
			}
			bounl = "d_" + over;
			bounlCss = "."
					+ bounl
					+ ":hover .bounl_hover{display:block;position:absolute;top:"
					+ top + "px;left:" + left + "px;}";
		} else if (times == 3) {
			over = 4 - over + a;
			int top = 26;
			int left = 50;
			bounl = "d_" + over;
			left = over % 7 == 0 ? 7 * 41 - 74 : (over % 7) * 41 - 74;
			if (over <= 14) {
				top = 26;
			}
			if ((over > 14) && (over <= 21)) {
				top = 62;
			}
			if ((over > 21) && (over <= 28)) {
				top = 97;
			}
			bounl = "d_" + over;
			if(over!=a){
				bounlCss = "."
						+ bounl
						+ ":hover .bounl_hover{display:block;position:absolute;top:"
						+ top + "px;left:" + left + "px;}";
			}
		}
		model.addAttribute("socks", socks);
		model.addAttribute("bell", bell);
		model.addAttribute("bigbell", bigBell);
		model.addAttribute("bounl", bounl);
		model.addAttribute("socksCss", socksCss);
		model.addAttribute("bellCss", bellCss);
		model.addAttribute("bigbellCss", bigBellCss);
		model.addAttribute("bounlCss", bounlCss);
		model.addAttribute("today", ".d_" + a);
		return "/activity/christmas/christmascard";
	}

	@ResponseBody
	@RequestMapping("/activity/christmassign/signsubmit")
	public Map<String, Object> submitChristmasSign(
			@RequestParam("signDate") int signDate, Model model) {
		Map<String, Object> ret = new HashMap<String, Object>(1);
		MyCustomer myCustomer = (MyCustomer) CustomerState.getCustomer();
		if(!myCustomer.isRegistered()){
			ret.put("loginNotice", "亲，你还没有登录呢~~");
			return ret;
		}
		int a = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		List<ChristmasCard> christmasCards = christmasCardService
				.loadChristmasCard(myCustomer.getId());
		if (a == signDate) {
			boolean signdFlag = false;
			for (ChristmasCard cc : christmasCards) {
				if (a == cc.getSignDate()) {
					signdFlag = true;
					break;
				}
			}
			if (!signdFlag) {
				christmasCardService.persist(myCustomer.getId(), signDate);
				christmasCards = christmasCardService
						.loadChristmasCard(myCustomer.getId());
				if ((!christmasCards.isEmpty())
						&& christmasCards.size() % 5 == 0) {
					RechargeableCard rechargeableCard = null;
					int card_value = christmasCards.size() / 5;
					long exTime = 1388410426565l;
					rechargeableCard = rechargeableCardService
							.createRechargeableCard(null, null, new Date(exTime), card_value);
					ret.put("rechargeableCard", rechargeableCard);
				}
				ret.put("signdOk", "坚持,就有意外惊喜~");
			} else {
				ret.put("signdArready", "签过了亲，明天再来吧~");
			}
		} else {
			ret.put("signdError", "别急,还没到时间呢~");
		}
		return ret;
	}

}
