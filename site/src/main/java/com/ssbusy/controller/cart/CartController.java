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

package com.ssbusy.controller.cart;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.broadleafcommerce.common.currency.domain.BroadleafCurrency;
import org.broadleafcommerce.core.catalog.domain.Sku;
import org.broadleafcommerce.core.offer.domain.OfferCode;
import org.broadleafcommerce.core.offer.service.exception.OfferMaxUseExceededException;
import org.broadleafcommerce.core.order.domain.NullOrderImpl;
import org.broadleafcommerce.core.order.domain.Order;
import org.broadleafcommerce.core.order.service.exception.AddToCartException;
import org.broadleafcommerce.core.order.service.exception.ProductOptionValidationException;
import org.broadleafcommerce.core.order.service.exception.RemoveFromCartException;
import org.broadleafcommerce.core.order.service.exception.RequiredAttributeNotProvidedException;
import org.broadleafcommerce.core.order.service.exception.UpdateCartException;
import org.broadleafcommerce.core.pricing.service.exception.PricingException;
import org.broadleafcommerce.core.web.controller.cart.BroadleafCartController;
import org.broadleafcommerce.core.web.order.CartState;
import org.broadleafcommerce.core.web.order.model.AddToCartItemEx;
import org.broadleafcommerce.inventory.exception.InventoryUnavailableException;
import org.broadleafcommerce.profile.web.core.CustomerState;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ssbusy.core.product.service.ProductService;

@Controller("ssbCartController")
public class CartController extends BroadleafCartController {
	@Resource(name = "ssbProductService")
	protected ProductService productService;

	@Override
	@RequestMapping("/cart")
	public String cart(HttpServletRequest request,
			HttpServletResponse response, Model model) throws PricingException {
		String v = super.cart(request, response, model);
		// if (CartState.getCart() != null) {
		// Money total = CartState.getCart().getTotal();
		// if (total != null) {
		// List<Sku> skus = productService.findProductsByPrice(total
		// .getAmount());
		// model.addAttribute("skus", skus);
		// }
		// }

		// TODO 临时
		// Order cart = CartState.getCart();
		// if(cart != null) {
		// boolean fixed = false;
		// List<OrderItem> orderItems = cart.getOrderItems();
		// if(orderItems != null) {
		// for (OrderItem oi : orderItems) {
		// if(oi instanceof DiscreteOrderItem) {
		// DiscreteOrderItem doi = (DiscreteOrderItem) oi;
		// if(doi.getProduct() == null && doi.getSku() != null) {
		// doi.setProduct(doi.getSku().getProduct());
		// fixed = true;
		// }
		// }
		// }
		// }
		// if(fixed)
		// orderService.save(cart, Boolean.FALSE);
		// }

		if (super.isAjaxRequest(request))
			return v;
		else
			return "redirect:/checkout";
	}

	/*
	 * The Heat Clnic does not show the cart when a product is added. Instead,
	 * when the product is added via an AJAX POST that requests JSON, we only
	 * need to return a few attributes to update the state of the page. The most
	 * efficient way to do this is to call the regular add controller method,
	 * but instead return a map that contains the necessary attributes. By using
	 * the @ResposeBody tag, Spring will automatically use Jackson to convert
	 * the returned object into JSON for easy processing via JavaScript.
	 */
	@RequestMapping(value = "/cart/add", produces = "application/json")
	public @ResponseBody
	Map<String, Object> addJson(HttpServletRequest request,
			HttpServletResponse response, Model model,
			@ModelAttribute("addToCartItem") AddToCartItemEx addToCartItem)
			throws IOException, PricingException, AddToCartException {
		addToCartItem.validateLocationId();
		Map<String, Object> responseMap = new HashMap<String, Object>();
		try {
			add0(request, response, model, addToCartItem);
			// responseMap.put("productName",
			// catalogService.findProductById(addToCartItem.getProductId()).getName());
			responseMap.put("quantityAdded", addToCartItem.getQuantity());
			responseMap.put("cartItemCount",
					String.valueOf(CartState.getCart().getItemCount()));
			responseMap.put("cartTotal",
					String.valueOf(CartState.getCart().getTotal()));
			responseMap.put("cartSubTotal",
					String.valueOf(CartState.getCart().getSubTotal()));
			if (addToCartItem.getItemAttributes() == null
					|| addToCartItem.getItemAttributes().size() == 0) {
				// We don't want to return a productId to hide actions for when
				// it is a product that has multiple
				// product options. The user may want the product in another
				// version of the options as well.
				responseMap.put("productId", addToCartItem.getProductId());
			}
		} catch (AddToCartException e) {
			if (e.getCause() instanceof RequiredAttributeNotProvidedException) {
				responseMap.put("error", "allOptionsRequired");
				responseMap.put("errorMessage", "抱歉，加入购物车失败");
			} else if (e.getCause() instanceof ProductOptionValidationException) {
				ProductOptionValidationException exception = (ProductOptionValidationException) e
						.getCause();
				responseMap.put("error", "productOptionValidationError");
				responseMap.put("errorCode", exception.getErrorCode());
				responseMap.put("errorMessage", exception.getMessage());
				// blMessages.getMessage(exception.get, lfocale))
			} else if (e.getCause() instanceof InventoryUnavailableException
					|| e.getCause().getCause() instanceof InventoryUnavailableException) {
				responseMap.put("error", "inventoryUnavailable");
				responseMap.put("errorMessage", "这个太受欢迎，已经抢光了，抱歉下次再来吧！");
			} else if (e.getCause() instanceof IllegalArgumentException
					&& e.getCause().getMessage() != null
					&& e.getCause().getMessage().indexOf("currencies") > 0) {
				responseMap.put("error", "currencyError");
				responseMap.put("errorMessage", "抱歉，积分商品不能与普通商品同买，");
			} else {
				responseMap.put("error", "addToCartException");
				responseMap.put("errorMessage", e.getMessage());
			}
		} catch (Exception e1) {
			if (e1 instanceof InventoryUnavailableException) {
				responseMap.put("error", "inventoryUnavailable");
				responseMap.put("errorMessage", "这个太受欢迎，已经抢光了，抱歉下次再来吧！");
			} else
				throw new RuntimeException("error add", e1);
		}

		return responseMap;
	}

	/*
	 * The Heat Clinic does not support adding products with required product
	 * options from a category browse page when JavaScript is disabled. When
	 * this occurs, we will redirect the user to the full product details page
	 * for the given product so that the required options may be chosen.
	 */
	@RequestMapping(value = "/cart/add", produces = { "text/html", "*/*" })
	public String add(HttpServletRequest request, HttpServletResponse response,
			Model model, RedirectAttributes redirectAttributes,
			@ModelAttribute("addToCartItem") AddToCartItemEx addToCartItem)
			throws IOException, PricingException, AddToCartException {
		Boolean w_flag = (Boolean)request.getSession().getAttribute("w_flag");
		addToCartItem.validateLocationId();
		try {
			String return_url =  add0(request, response, model, addToCartItem);
			if(w_flag!=null&&w_flag){
				return "redirect:/weixin/cart";
			}else{
				return return_url;
			}
		} catch (AddToCartException e) {
			// FIXME 异常信息反馈到页面
			Sku sku = catalogService.findSkuById(addToCartItem.getSkuId());
			if (sku == null)
				return "redirect:/";
			String errorMsg;
			if (e.getCause() instanceof RequiredAttributeNotProvidedException) {
				errorMsg = "请选择商品规格，";
			} else if (e.getCause() instanceof ProductOptionValidationException) {
				errorMsg = "请选择商品规格，";
				// blMessages.getMessage(exception.get, lfocale))
			} else if (e.getCause() instanceof InventoryUnavailableException
					|| e.getCause().getCause() instanceof InventoryUnavailableException) {
				errorMsg = "这个太好了，被抢光了，下次来早点吧。";
			} else if (e.getCause() instanceof IllegalArgumentException
					&& e.getCause().getMessage() != null) {
				if (e.getCause().getMessage().indexOf("currencies") > 0)
					errorMsg = "抱歉，积分商品不能与普通商品同买，";
				else
					errorMsg = e.getCause().getMessage();
			} else {
				errorMsg = "抱歉，加入购物车出错了！";
			}
			return "redirect:" + sku.getProduct().getUrl() + "?errorMessage="
					+ URLEncoder.encode(errorMsg, "UTF-8");
		} catch (Exception e1) {
			if (e1 instanceof InventoryUnavailableException) {
				Sku sku = catalogService.findSkuById(addToCartItem.getSkuId());
				if (sku == null)
					return "redirect:/";
				return "redirect:" + sku.getProduct().getUrl()
						+ "?errorMessage="
						+ URLEncoder.encode("这个太好了，被抢光了，下次来早点吧。", "UTF-8");
			} else
				throw new RuntimeException("error add", e1);
		}
	}

	@RequestMapping(value = "/cart/add-cou")
	public String addCou(HttpServletRequest request,
			HttpServletResponse response, Model model,
			RedirectAttributes redirectAttributes,
			@ModelAttribute("addToCartItem") AddToCartItemEx addToCartItem)
			throws IOException, PricingException, AddToCartException {
		addToCartItem.validateLocationId();
		try {
			add0(request, response, model, addToCartItem);
			return "redirect:/checkout";
		} catch (AddToCartException e) {
			// FIXME 异常信息反馈到页面
			Sku sku = catalogService.findSkuById(addToCartItem.getSkuId());
			if (sku == null)
				return "redirect:/";
			String errorMsg;
			if (e.getCause() instanceof RequiredAttributeNotProvidedException) {
				errorMsg = "请选择商品规格，";
			} else if (e.getCause() instanceof ProductOptionValidationException) {
				errorMsg = "请选择商品规格，";
				// blMessages.getMessage(exception.get, lfocale))
			} else if (e.getCause() instanceof InventoryUnavailableException
					|| e.getCause().getCause() instanceof InventoryUnavailableException) {
				errorMsg = "这个太好了，被抢光了，下次来早点吧。";
			} else if (e.getCause() instanceof IllegalArgumentException
					&& e.getCause().getMessage() != null) {
				if (e.getCause().getMessage().indexOf("currencies") > 0)
					errorMsg = "抱歉，积分商品不能与普通商品同买，";
				else
					errorMsg = e.getCause().getMessage();
			} else {
				errorMsg = "抱歉，加入购物车出错了！";
			}
			return "redirect:" + sku.getProduct().getUrl() + "?errorMessage="
					+ URLEncoder.encode(errorMsg, "UTF-8");
		} catch (Exception e1) {
			if (e1 instanceof InventoryUnavailableException) {
				Sku sku = catalogService.findSkuById(addToCartItem.getSkuId());
				if (sku == null)
					return "redirect:/";
				return "redirect:" + sku.getProduct().getUrl()
						+ "?errorMessage="
						+ URLEncoder.encode("这个太好了，被抢光了，下次来早点吧。", "UTF-8");
			} else
				throw new RuntimeException("error add", e1);
		}
	}

	public String add0(HttpServletRequest request,
			HttpServletResponse response, Model model, AddToCartItemEx itemRequest)
			throws IOException, AddToCartException, PricingException {
		itemRequest.setOverrideRetailPrice(null);
		itemRequest.setOverrideSalePrice(null);
		// 必须干掉productId, 以skuId为准
		itemRequest.setProductId(null);

		Order cart = CartState.getCart();

		if (cart == null || cart instanceof NullOrderImpl) {
			cart = orderService.createNewCartForCustomer(CustomerState
					.getCustomer(request));
		}
		if (cart.getItemCount() == 0) {
			Long skuid = itemRequest.getSkuId();
			if (skuid == null)
				skuid = itemRequest.getProductId();

			if (skuid == null)
				return getCartView();

			Sku sku = catalogService.findSkuById(skuid);
			BroadleafCurrency currency = sku.getCurrency();
			cart.setCurrency(currency);
			if (CustomerState.getCustomer().isAnonymous()
					&& (currency == null || !"CNY".equals(currency
							.getCurrencyCode()))) {
				throw new AddToCartException("未登录会员不能仙丹换购，请先登录或注册～",
						new IllegalArgumentException("未登录会员不能仙丹换购，请先登录或注册～"));
			}
		}

		// Order cart = CartState.getCart();
		// if (cart == null || cart instanceof NullOrderImpl) {
		// cart = orderService.createNewCartForCustomer(CustomerState
		// .getCustomer(request));
		// }
		//
		// Sku sku = null;
		// if (itemRequest.getProductId() != null) {
		// Product product = catalogService.findProductById(itemRequest
		// .getProductId());
		// if (product != null) {
		// sku = product.getDefaultSku();
		// }
		// }
		// if (sku == null)
		// sku = catalogService.findSkuById(itemRequest.getSkuId());
		// BroadleafCurrency currency = sku.getCurrency();
		// if (cart.getItemCount() == 0) {
		// cart.setCurrency(currency);
		// }
		updateCartService.validateCart(cart);

		cart = orderService.addItem(cart.getId(), itemRequest, false);
		cart = orderService.save(cart, true);
		CartState.setCart(cart);
		return isAjaxRequest(request) ? getCartView() : getCartPageRedirect();
	}

	@RequestMapping("/cart/updateQuantity")
	public String updateQuantity(
			HttpServletRequest request,
			HttpServletResponse response,
			Model model,
			@RequestParam(value = "redirect", required = false) String redirect,
			@ModelAttribute("addToCartItem") AddToCartItemEx addToCartItem)
			throws IOException, PricingException, UpdateCartException,
			RemoveFromCartException {
		
		Boolean w_flag = (Boolean)request.getSession().getAttribute("w_flag");
		addToCartItem.validateLocationId();
		try {
			String update = super.updateQuantity(request, response, model,
					addToCartItem);

			// if (CartState.getCart() != null) {
			// Money total = CartState.getCart().getTotal();
			// if (total != null) { // FIXME 凑整，币种
			// List<Sku> skus = productService
			// .findProductsByPriceAndCurrency(total.getAmount(),
			// CartState.getCart().getCurrency());
			// model.addAttribute("skus", skus);
			// }
			// }
			if(w_flag!=null&&w_flag){
				return "redirect:/weixin/cart";
			}else{
				return StringUtils.isBlank(redirect) ? update : redirect;
			}
		} catch (UpdateCartException e) {
			if (e.getCause() instanceof InventoryUnavailableException) {
				if (isAjaxRequest(request)) {
					Map<String, Object> extraData = new HashMap<String, Object>(
							1);
					extraData.put("error", "InventoryUnavailable");
					model.addAttribute("blcextradata",
							new ObjectMapper().writeValueAsString(extraData));
				}
				return StringUtils.isBlank(redirect) ? "/cart/cart_error"
						: redirect
								+ "?error=%E6%8A%B1%E6%AD%89%EF%BC%8C%E5%BA%93%E5%AD%98%E4%B8%8D%E8%B6%B3%E4%BA%86";
			} else {
				throw e;
			}
		}
	}

	@RequestMapping("/cart/remove")
	public String remove(HttpServletRequest request,
			HttpServletResponse response, Model model,
			@ModelAttribute("addToCartItem") AddToCartItemEx addToCartItem)
			throws IOException, PricingException, RemoveFromCartException {
		addToCartItem.validateLocationId();
		String remove = super.remove(request, response, model, addToCartItem);
		// if (CartState.getCart() != null) {
		// Money total = CartState.getCart().getTotal();
		// if (total != null) {
		// List<Sku> skus = productService.findProductsByPriceAndCurrency(
		// total.getAmount(), CartState.getCart().getCurrency());
		// model.addAttribute("skus", skus);
		// }
		// }
		return remove;
	}

	@Override
	@RequestMapping("/cart/empty")
	public String empty(HttpServletRequest request,
			HttpServletResponse response, Model model) throws PricingException {
		// return super.empty(request, response, model);
		return "ajaxredirect:/";

	}

	@Override
	@RequestMapping("/cart/promo")
	public String addPromo(HttpServletRequest request,
			HttpServletResponse response, Model model,
			@RequestParam("promoCode") String customerOffer)
			throws IOException, PricingException {
		OfferCode offerCode = null;
		Boolean promoAdded = false;
		String exception = "";
		try {
			offerCode = addPromo0(request, response, model, customerOffer);
		} catch (OfferMaxUseExceededException e) {
			exception = "优惠券已过期~";
		}
		if (offerCode == null) {
			exception = "无效优惠码,请确认后再试~";
		} else {
			promoAdded = true;
		}
		if (isAjaxRequest(request)) {
			Map<String, Object> extraData = new HashMap<String, Object>();
			extraData.put("promoAdded", promoAdded);
			extraData.put("exception", exception);
			model.addAttribute("blcextradata",
					new ObjectMapper().writeValueAsString(extraData));
		} else {
			model.addAttribute("error_pro", exception);
		}
		return "redirect:/checkout";
	}

	private OfferCode addPromo0(HttpServletRequest request,
			HttpServletResponse response, Model model, String customerOffer)
			throws PricingException, JsonGenerationException,
			JsonMappingException, IOException, OfferMaxUseExceededException {
		Order cart = CartState.getCart();

		OfferCode offerCode = offerService.lookupOfferCodeByCode(customerOffer);

		if (offerCode != null) {
			orderService.addOfferCode(cart, offerCode, false);
			cart = orderService.save(cart, true);
			// offerCode.setUserCustomerId(userCustomerId);
		}
		CartState.setCart(cart);
		return offerCode;
	}

	@Override
	@RequestMapping("/cart/promo/remove")
	public String removePromo(HttpServletRequest request,
			HttpServletResponse response, Model model,
			@RequestParam("offerCodeId") Long offerCodeId) throws IOException,
			PricingException {
		return super.removePromo(request, response, model, offerCodeId);
	}

	@RequestMapping(value = "/app/cart/promo")
	@ResponseBody
	public Map<String, Object> addAppPromo(HttpServletRequest request,
			HttpServletResponse response, Model model,
			@RequestParam("promoCode") String customerOffer)
			throws IOException, PricingException {
		OfferCode offerCode = null;
		String exception = "";
		try {
			offerCode = addPromo0(request, response, model, customerOffer);
		} catch (OfferMaxUseExceededException e) {
			exception = "优惠券已过期~";
		}
		if (offerCode == null) {
			exception = "无效优惠码,请确认后再试~";
		}
		Map<String, Object> ret = new HashMap<String, Object>(2);
		if (!("".equals(exception))) {
			ret.put("error", "promo_error");
			ret.put("errorMessage", exception);
		} else {
			ret.put("marketingMessage", offerCode.getOffer()
					.getMarketingMessage());
		}
		return ret;
	}
	@RequestMapping("/weixin/cart")
	public String weixinCart(HttpServletRequest request,
			HttpServletResponse response, Model model) throws PricingException {
		super.cart(request, response, model);
		return "weixin/cart/w_cart";
	}
}
