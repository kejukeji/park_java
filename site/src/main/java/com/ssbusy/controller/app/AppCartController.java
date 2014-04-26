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

package com.ssbusy.controller.app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.broadleafcommerce.common.media.domain.Media;
import org.broadleafcommerce.core.catalog.domain.Product;
import org.broadleafcommerce.core.catalog.domain.Sku;
import org.broadleafcommerce.core.offer.domain.OrderAdjustment;
import org.broadleafcommerce.core.order.domain.NullOrderImpl;
import org.broadleafcommerce.core.order.domain.Order;
import org.broadleafcommerce.core.order.domain.OrderItem;
import org.broadleafcommerce.core.order.domain.SkuAccessor;
import org.broadleafcommerce.core.order.service.OrderService;
import org.broadleafcommerce.core.order.service.exception.AddToCartException;
import org.broadleafcommerce.core.pricing.service.exception.PricingException;
import org.broadleafcommerce.core.web.order.CartState;
import org.broadleafcommerce.core.web.order.model.AddToCartItemEx;
import org.broadleafcommerce.profile.web.core.CustomerState;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ssbusy.controller.cart.CartController;
import com.ssbusy.core.offer.domain.MyOfferCode;
import com.ssbusy.core.offer.service.MyOfferService;

/**
 * mobile app request controller. all with jsonp support.
 * 
 * @author Ju
 */
@Controller
public class AppCartController {

	private static final Log LOG = LogFactory.getLog(CartController.class);
	@Resource(name = "ssbCartController")
	private CartController ssbCartController;
	@Resource(name = "blOfferService")
	protected MyOfferService offerService;

	@Resource(name = "blOrderService")
	protected OrderService orderService;

	@RequestMapping("/app/cart")
	@ResponseBody
	public Map<String, Object> readCart(@RequestParam("detail") boolean detail) {
		Order cart = CartState.getCart();
		return wrapCart(cart, detail);
	}

	@RequestMapping("/app/cart/add")
	@ResponseBody
	public Map<String, Object> add2Cart(HttpServletRequest request,
			HttpServletResponse response, Model model,
			@ModelAttribute("addToCartItem") AddToCartItemEx addToCartItem)
			throws IOException, PricingException, AddToCartException {
		addToCartItem.validateLocationId();
		// FIXME exception handle
		return ssbCartController.addJson(request, response, model,
				addToCartItem);
	}

	private Map<String, Object> wrapCart(Order cart, boolean detail) {
		if (cart == null || cart instanceof NullOrderImpl) {
			return Collections.emptyMap();
		}

		Map<String, Object> ret = new HashMap<String, Object>();

		ret.put("cartItemCount", cart.getItemCount());
		if (detail) {
			ret.put("currencyCode", cart.getCurrency() == null ? null : cart
					.getCurrency().getCurrencyCode());
			ret.put("total", cart.getTotal());
			List<OrderAdjustment> oadjusts = cart.getOrderAdjustments();
			if (oadjusts != null && oadjusts.size() > 0) {
				List<Map<String, Object>> adjusts = new ArrayList<Map<String, Object>>(
						oadjusts.size());
				ret.put("adjusts", adjusts);
				for (OrderAdjustment oa : oadjusts) {
					Map<String, Object> adjust = new HashMap<String, Object>(1);
					adjusts.add(adjust);
					adjust.put("marketingMessage", oa.getOffer()
							.getMarketingMessage());
				}
			}
			List<OrderItem> oitems = cart.getOrderItems();
			if (oitems != null && oitems.size() > 0) {
				List<Map<String, Object>> items = new ArrayList<Map<String, Object>>(
						oitems.size());
				ret.put("items", items);
				for (OrderItem oi : oitems) {
					Map<String, Object> item = new HashMap<String, Object>(7);
					items.add(item);
					item.put("id", oi.getId());
					item.put("name", oi.getName());
					item.put("price", oi.getPriceBeforeAdjustments(true));
					item.put("quantity", oi.getQuantity());
					if (oi instanceof SkuAccessor) {
						Product p = null;
						Sku sku = ((SkuAccessor) oi).getSku();
						if (sku != null)
							p = sku.getProduct();
						if (p != null) {
							item.put("pid", p.getId());
							item.put("url", p.getUrl());
							Map<String, Media> media = p.getMedia();
							if (media != null) {
								item.put("media", media.get("primary"));
							}
						}
					} else {
						throw new UnsupportedOperationException(
								"unsupported orderItem type: "
										+ oi.getClass().getName());
					}
				}
			}
			List<MyOfferCode> myOfferCodes = offerService
					.listOfferCodeByOwner(CustomerState.getCustomer().getId());
			if (myOfferCodes != null && myOfferCodes.size() > 0) {
				List<Map<String, Object>> offerCodes = new ArrayList<Map<String, Object>>(
						myOfferCodes.size());
				ret.put("offercodes", offerCodes);
				for (MyOfferCode moc : myOfferCodes) {
					Map<String, Object> offerscodes = new HashMap<String, Object>(
							2);
					offerCodes.add(offerscodes);
					offerscodes.put("offerCode", moc.getOfferCode());
					offerscodes.put("marketingMessage", moc.getOffer()
							.getMarketingMessage());
				}
			}
		}
		return ret;
	}

	@RequestMapping("/app/cart/updateQuantity")
	@ResponseBody
	public Map<String, Object> updateQuantityApp(
			@ModelAttribute("addToCartItem") AddToCartItemEx addToCartItem) {
		addToCartItem.validateLocationId();
		Map<String, Object> ret = new HashMap<String, Object>();
		Order cart = CartState.getCart();
		try {
			cart = orderService.updateItemQuantity(cart.getId(), addToCartItem,
					true);
			cart = orderService.save(cart, false);
			CartState.setCart(cart);

			ret.put("cartItemCount", cart.getItemCount());
			ret.put("subTotal", cart.getSubTotal());
		} catch (Exception e) {
			LOG.error("error updating cart quantity, cartId=" + cart.getId()
					+ ", itemId=" + addToCartItem.getOrderItemId(), e);
			ret.put("error", Boolean.TRUE);
			ret.put("errorMessage", "抱歉，更新购物车数量失败。。");
		}
		return ret;
	}
}
