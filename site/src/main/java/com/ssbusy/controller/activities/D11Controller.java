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

package com.ssbusy.controller.activities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.broadleafcommerce.common.web.controller.BroadleafAbstractController;
import org.broadleafcommerce.core.order.service.exception.AddToCartException;
import org.broadleafcommerce.core.web.order.model.AddToCartItemEx;
import org.broadleafcommerce.inventory.exception.InventoryUnavailableException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ssbusy.controller.cart.CartController;

/**
 * @author Ju
 */
@Controller
@RequestMapping("/activity/d11")
public class D11Controller extends BroadleafAbstractController {
	@Resource(name = "ssbCartController")
	private CartController ssbCartController;
	private long times[][] = { { 1383966671000l, 1383981071000l },
			{ 1383981071000l, 1383995471000l },
			{ 1383995471000l, 1384009871000l },
			{ 1384053071000l, 1384067471000l },
			{ 1384067471000l, 1384081871000l },
			{ 1384081871000l, 1384096271000l },
			{ 1384139471000l, 1384153871000l },
			{ 1384153871000l, 1384168271000l },
			{ 1384168271000l, 1384182671000l } };
	private Map<String, List<Long>> productTime = new HashMap<String, List<Long>>(
			9);

	private void SetProductTime() {
		if (productTime.isEmpty()) {
			List<Long> Nine4Eleven = new ArrayList<Long>(6);
			List<Long> Nine4Fifteen = new ArrayList<Long>(6);
			List<Long> Nine4Nineteen = new ArrayList<Long>(6);
			List<Long> Ten4Eleven = new ArrayList<Long>(6);
			List<Long> Ten4Fifteen = new ArrayList<Long>(6);
			List<Long> Ten4Nineteen = new ArrayList<Long>(6);
			List<Long> Eleven4Eleven = new ArrayList<Long>(6);
			List<Long> Eleven4Fifteen = new ArrayList<Long>(6);
			List<Long> Eleven4Nineteen = new ArrayList<Long>(7);
			productTime.put("0", Nine4Eleven);
			productTime.put("1", Nine4Fifteen);
			productTime.put("2", Nine4Nineteen);
			productTime.put("3", Ten4Eleven);
			productTime.put("4", Ten4Fifteen);
			productTime.put("5", Ten4Nineteen);
			productTime.put("6", Eleven4Eleven);
			productTime.put("7", Eleven4Fifteen);
			productTime.put("8", Eleven4Nineteen);
			Nine4Eleven.add(652L);
			Ten4Eleven.add(652L);
			Eleven4Eleven.add(652L);
			Nine4Eleven.add(653L);
			Ten4Eleven.add(653L);
			Eleven4Eleven.add(653L);
			Nine4Eleven.add(654L);
			Ten4Eleven.add(654L);
			Eleven4Eleven.add(654L);
			Nine4Eleven.add(-12152L);
			Ten4Eleven.add(655L);
			Eleven4Eleven.add(658L);
			Nine4Eleven.add(-12160L);
			Ten4Eleven.add(656L);
			Eleven4Eleven.add(659L);
			Nine4Eleven.add(-109768L);
			Ten4Eleven.add(657L);
			Eleven4Eleven.add(660L);

			Nine4Fifteen.add(661L);
			Ten4Fifteen.add(661L);
			Eleven4Fifteen.add(661L);
			Nine4Fifteen.add(-12150L);
			Ten4Fifteen.add(-12150L);
			Eleven4Fifteen.add(-12150L);
			Nine4Fifteen.add(-109780L);
			Ten4Fifteen.add(-109780L);
			Eleven4Fifteen.add(-109780L);
			Nine4Fifteen.add(-109832L);
			Ten4Fifteen.add(-1113L);
			Eleven4Fifteen.add(663L);
			Nine4Fifteen.add(-11045L);
			Ten4Fifteen.add(662L);
			Eleven4Fifteen.add(664L);
			Nine4Fifteen.add(-10027L);
			Ten4Fifteen.add(-12159L);
			Eleven4Fifteen.add(665L);

			Nine4Nineteen.add(666L);
			Ten4Nineteen.add(666L);
			Eleven4Nineteen.add(701L);
			Nine4Nineteen.add(653L);
			Ten4Nineteen.add(653L);
			Eleven4Nineteen.add(703L);
			Nine4Nineteen.add(667L);
			Ten4Nineteen.add(667L);
			Eleven4Nineteen.add(702L);
			Nine4Nineteen.add(-12139L);
			Ten4Nineteen.add(-12146L);
			Eleven4Nineteen.add(-12111L);
			Nine4Nineteen.add(-1365L);
			Ten4Nineteen.add(668L);
			Eleven4Nineteen.add(-109862L);
			Nine4Nineteen.add(-12162L);
			Ten4Nineteen.add(-109775L);
			Eleven4Nineteen.add(-1091L);
			Eleven4Nineteen.add(-1263L);
		}
	}

	@RequestMapping("/qianggou")
	public void listCategories(Model model) {
	}

	public boolean checkTime(Long productid) {
		SetProductTime();
		long now = Calendar.getInstance().getTime().getTime();
		int i = 0;
		for (i = 0; i < times.length; i++) {
			if (times[i][0] <= now && now < times[i][1]) {
				break;
			}
		}
		if (i >= 9) {
			return false;
		}
		List<Long> allProductIds = productTime.get(i + "");
		if (allProductIds.contains(productid)) {
			return true;
		} else
			return false;
	}

	@RequestMapping("/qianggou/checkout")
	@ResponseBody
	public Map<String, Object> checkoutQiang(Model model,
			HttpServletRequest request, HttpServletResponse response,
			@RequestParam("product_id") Long productId) {
		Map<String, Object> ret = new HashMap<String, Object>(2);
		if (!checkTime(productId)) {
			ret.put("errorTime", "别急嘛，心急吃不了热豆腐，等会再来吧。。");
			return ret;
		} else {
			AddToCartItemEx addToCartItem = new AddToCartItemEx();
			addToCartItem.setQuantity(1);
			addToCartItem.setSkuId(productId);
			// FIXME
			addToCartItem.validateLocationId();
			try {
				ssbCartController.add0(request, response, model, addToCartItem);
				ret.put("redirect", "redirect");
				return ret;
			} catch (AddToCartException e) {
				String errorMsg = null;
				if (e.getCause() instanceof InventoryUnavailableException
						|| e.getCause().getCause() instanceof InventoryUnavailableException) {
					ret.put("errorInventory", "不好意思哦，卖完了，你来晚了，下次早点吧。");
					return ret;
				} else if (e.getCause() instanceof IllegalArgumentException
						&& e.getCause().getMessage() != null) {
					if (e.getCause().getMessage().indexOf("currencies") > 0)
						errorMsg = "抱歉，积分商品不能与普通商品同买，";
					else
						errorMsg = "出错了。";
				} else {
					errorMsg = "不好意思，操作失败了，再来啊。";
				}
				ret.put("errorOther", errorMsg);
				return ret;
			} catch (Exception e1) {
				if (e1 instanceof InventoryUnavailableException) {
					ret.put("errorInventoryUn", "不好意思哦，卖完了，你来晚了，下次早点吧。");
					return ret;
				} else {
					throw new RuntimeException("不好意思，操作失败了，再来啊。", e1);
				}
			}
		}
	}
}
