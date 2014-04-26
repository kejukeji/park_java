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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.broadleafcommerce.common.exception.ServiceException;
import org.broadleafcommerce.common.media.domain.Media;
import org.broadleafcommerce.common.money.Money;
import org.broadleafcommerce.common.web.controller.BroadleafAbstractController;
import org.broadleafcommerce.core.catalog.domain.Category;
import org.broadleafcommerce.core.catalog.domain.CategoryImpl;
import org.broadleafcommerce.core.catalog.domain.CategoryProductXref;
import org.broadleafcommerce.core.catalog.domain.FeaturedProduct;
import org.broadleafcommerce.core.catalog.domain.Product;
import org.broadleafcommerce.core.catalog.domain.RelatedProduct;
import org.broadleafcommerce.core.catalog.domain.Sku;
import org.broadleafcommerce.core.catalog.service.CatalogService;
import org.broadleafcommerce.core.rating.domain.RatingSummary;
import org.broadleafcommerce.core.rating.service.RatingService;
import org.broadleafcommerce.core.rating.service.type.RatingType;
import org.broadleafcommerce.core.search.domain.ProductSearchCriteria;
import org.broadleafcommerce.core.search.domain.ProductSearchResult;
import org.broadleafcommerce.core.search.domain.SearchFacetDTO;
import org.broadleafcommerce.core.search.service.SearchService;
import org.broadleafcommerce.core.web.service.SearchFacetDTOService;
import org.broadleafcommerce.inventory.service.InventoryService;
import org.broadleafcommerce.profile.web.core.CustomerState;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ssbusy.core.account.domain.MyCustomer;
import com.ssbusy.core.like.service.LikeService;
import com.ssbusy.core.product.domain.MyProduct;

/**
 * mobile app request controller. all with jsonp support.
 * 
 * @author Ju
 */
@Controller
@RequestMapping("/app")
public class AppController extends BroadleafAbstractController {

	@Resource(name = "blCatalogService")
	protected CatalogService catalogService;

	@Resource(name = "blSearchService")
	protected SearchService searchService;

	@Resource(name = "blSearchFacetDTOService")
	protected SearchFacetDTOService facetService;

	@Resource(name = "blRatingService")
	protected RatingService ratingService;

	@Resource(name = "blInventoryService")
	protected InventoryService inventoryService;

	@Resource(name = "ssbLikeService")
	protected LikeService likeService;

	@Value("${category.nav.root.id}")
	protected Long navCategoryRootId;

	@Value("${category.floor.root.id}")
	protected Long floorCategoryRootId;

	@Value("${nowVersion}")
	private String nowVersion;

	private Map<Long, Category> cache = new HashMap<Long, Category>(2);

	@RequestMapping("/list-categories")
	@ResponseBody
	public List<Map<String, Object>> listCategories() {
		List<Category> cats = catalogService
				.findActiveSubCategoriesByCategory(cachedCategoryById(navCategoryRootId));
		if (cats != null && cats.size() > 0) {
			// gets child categories in order ONLY if they are in the xref table
			// and active
			List<Map<String, Object>> results = new ArrayList<Map<String, Object>>(
					cats.size());
			for (Category cart : cats) {
				Map<String, Object> c = new HashMap<String, Object>(2);
				c.put("id", cart.getId());
				c.put("name", cart.getName());
				results.add(c);
			}
			return results;
		}
		return Collections.emptyList();
	}

	@RequestMapping("/search")
	@ResponseBody
	public List<Map<String, Object>> searchProucts(@RequestParam("q") String q,
			HttpServletRequest request) throws ServiceException {
		if (q == null || (q = q.trim()).length() == 0)
			return Collections.emptyList();

		List<SearchFacetDTO> availableFacets = searchService.getSearchFacets();
		ProductSearchCriteria searchCriteria = facetService
				.buildSearchCriteria(request, availableFacets);
		ProductSearchResult result = searchService.findProductsByQuery(q,
				searchCriteria);
		facetService.setActiveFacetResults(result.getFacets(), request);

		List<Product> products = result.getProducts();
		MyCustomer c = (MyCustomer) CustomerState.getCustomer();
		if (c != null && c.getRegion() != null)
			products = inventoryService.filterProducts(products, c.getRegion()
					.getFulfillmentLocations());

		List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>(1);
		Map<String, Object> m = new HashMap<String, Object>(2);
		ret.add(m);
		m.put("name", q);
		m.put("items", wrapProducts(products, false));
		return ret;
	}

	/**
	 * @return List<楼层<楼层属性, 值>>
	 */
	@RequestMapping("/list-products")
	@ResponseBody
	public List<Map<String, Object>> listProucts(
			@RequestParam(value = "category-id", required = false) Long categoryId,
			@RequestParam(value = "sorter", required = false) String sorter) {
		List<Category> cats = null;
		if (categoryId != null) {
			Category cat = catalogService.findCategoryById(categoryId);
			if (cat != null) {
				cats = Arrays.asList(cat);
			}
		}
		if (cats == null) // 默认列楼层
			cats = catalogService
					.findActiveSubCategoriesByCategory(cachedCategoryById(floorCategoryRootId));

		return wrapCategories(cats, sorter);
	}

	private List<Map<String, Object>> wrapCategories(List<Category> cats,
			String sorter) {
		List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>(
				cats.size());
		for (Category cat : cats) {
			List<Product> prods;
			if ("rec".equals(sorter)) {
				List<FeaturedProduct> ps = cat.getFeaturedProducts();
				if (ps == null || ps.isEmpty())
					prods = Collections.emptyList();
				else {
					prods = new ArrayList<Product>(ps.size());
					for (FeaturedProduct fp : ps) {
						Product p = fp.getProduct();
						if (p == null)
							continue;
						prods.add(p);
					}
				}
			} else if ("hot".equals(sorter) || "new".equals(sorter)) {
				List<RelatedProduct> ps = "hot".equals(sorter) ? cat
						.getCrossSaleProducts() : cat.getUpSaleProducts();
				if (ps == null || ps.isEmpty())
					prods = Collections.emptyList();
				else {
					prods = new ArrayList<Product>(ps.size());
					for (RelatedProduct rp : ps) {
						Product p = rp.getProduct();
						if (p == null)
							continue;
						prods.add(p);
					}
				}
			} else {// 默认排序
				List<CategoryProductXref> xrefs = cat.getActiveProductXrefs();
				if (xrefs == null || xrefs.isEmpty())
					prods = Collections.emptyList();
				else {
					prods = new ArrayList<Product>(xrefs.size());
					for (CategoryProductXref xref : xrefs) {
						Product p = xref.getProduct();
						if (p == null)
							continue;
						prods.add(p);
					}
				}
			}
			MyCustomer c = (MyCustomer) CustomerState.getCustomer();
			if (c != null && c.getRegion() != null)
				prods = inventoryService.filterProducts(prods, c.getRegion()
						.getFulfillmentLocations());
			if (prods.isEmpty())
				continue;

			Map<String, Object> m = new HashMap<String, Object>(3);
			ret.add(m);
			m.put("catId", cat.getId());
			m.put("name", cat.getName());
			m.put("items", wrapProducts(prods, false));
		}
		return ret;
	}


	@RequestMapping("/d/check-update")
	@ResponseBody
	public Map<String, Object> checkForUpdate(@RequestParam("version") String version) {
		if(!"1.1".equals(version)) {
			Map<String, Object> ret = new HashMap<String, Object>(1);
			ret.put("update", "<a onclick='javascript:navigator.app.loadUrl(\"http://www.onxiao.com/d/app\", { openExternal:true});' style=\"font-size:15px;\">有新的版本可供下载，请点此升级</a>");
			return ret;
		}
		return Collections.emptyMap();
	}

	@RequestMapping("/product")
	@ResponseBody
	public Map<String, Object> showProduct(@RequestParam("url") String prodUrl) {
		Product product = catalogService.findProductByURI(prodUrl);
		if (product == null)
			return Collections.emptyMap();
		Map<String, Object> map = wrapProduct(product, true);
		return map;
	}

	private Map<String, Object> wrapProduct(Product p, boolean detail) {
		Sku defSku = p.getDefaultSku();
		if (defSku == null || !defSku.isActive())
			return null;
		Map<String, Object> m = new HashMap<String, Object>(20);
		m.put("id", p.getId());
		m.put("name", p.getName());
		m.put("saleNum", 50);
		m.put("desc", p.getDescription());
		m.put("longDesc", p.getLongDescription());
		m.put("url", p.getUrl());
		m.put("skuId", defSku.getId());
		m.put("retailPrice", defSku.getRetailPrice());
		m.put("salePrice", defSku.getSalePrice());
		m.put("retailPrice", defSku.getRetailPrice());
		Map<String, Media> medias = p.getMedia();
		if (medias != null) {
			if (detail) {
				m.put("medias", medias.values());
			} else {
				Media media = medias.get("primary");
				if (media != null) {
					m.put("media", media);
				}
			}
		}
		if (p instanceof MyProduct) {
			m.put("totalLike", ((MyProduct) p).getTotalLike());
			m.put("totalSaled", ((MyProduct) p).getTotalSaled());
		}
		// 规格
		if (detail) {
			RatingSummary rating = ratingService.readRatingSummary(p.getId()
					.toString(), RatingType.PRODUCT);
			m.put("avgRating", rating == null ? 5 : rating.getAverageRating());
			if (p instanceof MyProduct) {
				m.put("totalLike", ((MyProduct) p).getTotalLike());
				m.put("totalSaled", ((MyProduct) p).getTotalSaled());
				m.put("liked", likeService.queryLike(p, CustomerState
						.getCustomer().getId()));
				m.put("longDesc", p.getLongDescription());
			}

			List<Map<String, Object>> more = new ArrayList<Map<String, Object>>();
			m.put("skus", more);
			Map<String, Object> wrapSku = wrapSku(defSku);
			wrapSku.put("name", "单价");
			more.add(wrapSku);
			List<Sku> skus = p.getSkus();
			if (skus != null && skus.size() > 0) {
				for (Sku sku : skus) {
					if (sku.getId().equals(defSku.getId()))
						continue;

					more.add(wrapSku(sku));
				}
			}
		}
		return m;
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

	private List<Map<String, Object>> wrapProducts(List<Product> ps,
			boolean detail) {

		if (ps == null || ps.isEmpty())
			return Collections.emptyList();

		List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>(
				ps.size());
		for (Product p : ps) {
			if (p == null)
				continue;
			Map<String, Object> m = wrapProduct(p, detail);
			if (m != null)
				ret.add(m);
		}
		return ret;
	}

	private Category cachedCategoryById(Long id) {
		Category category = cache.get(id);
		if (category == null) {
			cache.put(id, category = new CategoryImpl());
			category.setId(id);
		}
		return category;
	}
}
