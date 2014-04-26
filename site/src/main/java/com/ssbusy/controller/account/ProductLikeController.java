package com.ssbusy.controller.account;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.broadleafcommerce.common.money.Money;
import org.broadleafcommerce.core.catalog.domain.Product;
import org.broadleafcommerce.core.catalog.domain.Sku;
import org.broadleafcommerce.core.catalog.service.CatalogService;
import org.broadleafcommerce.profile.web.core.CustomerState;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ssbusy.core.like.service.LikeService;
import com.ssbusy.core.product.domain.MyProduct;

@Controller
public class ProductLikeController {

	@Resource(name = "ssbLikeService")
	protected LikeService likeService;
	@Resource(name = "blCatalogService")
	private CatalogService catalogService;
	protected static String PRODUCT_LIKE = "productLike";

	@RequestMapping("/customer/like")
	@ResponseBody
	public String toggleLike(@RequestParam("id") Long productId) {
		Boolean flag = Boolean.FALSE;
		Product product = catalogService.findProductById(productId);
		if (product == null) {
			return flag.toString();
		}
		if (CustomerState.getCustomer().isAnonymous()) {
			if (CustomerState.getCustomer().getUsername() != null
					&& !("".equals(CustomerState.getCustomer().getUsername()))) {
				flag = likeService.toggleLike(product.getId(), CustomerState
						.getCustomer().getId());
			} else {
				flag = true;
			}
		} else {
			flag = likeService.toggleLike(product.getId(), CustomerState
					.getCustomer().getId());
		}
		return flag == null ? "" : flag.toString();
	}

	@RequestMapping("/app/toggle-like")
	@ResponseBody
	public List<String> toggleLikeApp(@RequestParam("id") Long productId) {
		return Arrays.asList(toggleLike(productId));
	}

	@RequestMapping("/customer/like/show")
	public String showLike(Model model) {
		List<Product> products = null;
		if (CustomerState.getCustomer().getUsername() != null
				&& !("".equals(CustomerState.getCustomer().getUsername()))) {
			products = likeService
					.showLike(CustomerState.getCustomer().getId());
		}
		model.addAttribute("products", products);
		return "account/like";
	}

	@RequestMapping("/customer/cancel-like")
	@ResponseBody
	public String customerCancelLike(@RequestParam("pid") Long productId){
		Long customerId =CustomerState.getCustomer().getId();
		likeService.cancelLike(customerId, productId);
		return Boolean.TRUE.toString();
	}
	
	@RequestMapping("/app/cancel-like")
	@ResponseBody
	public Map<String, Object> cancelLike(@RequestParam("pid") Long productId) {
		likeService.cancelLike(CustomerState.getCustomer().getId(), productId);
		return Collections.emptyMap();
	}

	@RequestMapping("/app/likes")
	@ResponseBody
	public List<Map<String, Object>> showLikeApp() {
		List<Product> products = likeService.showLike(CustomerState
				.getCustomer().getId());
		return wrapProducts(products);
	}

	private List<Map<String, Object>> wrapProducts(List<Product> products) {
		if (products == null || products.isEmpty())
			return Collections.emptyList();

		List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>(
				products.size());
		for (Product p : products) {
			Sku defSku = p.getDefaultSku();
			if (defSku == null || !defSku.isActive())
				continue;
			Map<String, Object> m = new HashMap<String, Object>();
			ret.add(m);
			m.put("id", p.getId());
			m.put("url", p.getUrl());
			m.put("price",
					defSku.getSalePrice() == null ? defSku.getRetailPrice()
							: defSku.getSalePrice());
			m.put("media", p.getMedia().get("primary"));
			if (p instanceof MyProduct) {
				m.put("likes", ((MyProduct) p).getTotalLike());
			}

			// FIXME like时，还是别取skus了
			List<Map<String, Object>> more = new ArrayList<Map<String, Object>>();
			m.put("skus", more);
			Map<String, Object> wrapSku = wrapSku(defSku);
			wrapSku.put("name", "单价");
			more.add(wrapSku);
			List<Sku> skus = p.getSkus();
			if (skus != null && skus.size() > 0) {
				for (Sku sku : skus) {
					more.add(wrapSku(sku));
				}
			}
		}
		return ret;
	}

	private Map<String, Object> wrapSku(Sku sku) {
		Map<String, Object> s = new HashMap<String, Object>(3);
		s.put("id", sku.getId());
		s.put("name", sku.getName());
		Money price = sku.getSalePrice() == null ? sku.getRetailPrice() : sku
				.getSalePrice();
		s.put("price", price);
		return s;
	}
}
