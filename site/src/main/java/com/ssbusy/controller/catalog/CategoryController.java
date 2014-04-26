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

package com.ssbusy.controller.catalog;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.broadleafcommerce.core.catalog.domain.Category;
import org.broadleafcommerce.core.catalog.domain.Product;
import org.broadleafcommerce.core.catalog.service.CatalogService;
import org.broadleafcommerce.core.rating.service.RatingService;
import org.broadleafcommerce.core.rating.service.type.RatingType;
import org.broadleafcommerce.core.search.domain.ProductSearchCriteria;
import org.broadleafcommerce.core.search.domain.ProductSearchResult;
import org.broadleafcommerce.core.search.domain.SearchFacetDTO;
import org.broadleafcommerce.core.search.domain.SearchFacetResultDTO;
import org.broadleafcommerce.core.search.service.solr.InventorySolrSearchServiceExtensionHandler;
import org.broadleafcommerce.core.web.catalog.CategoryHandlerMapping;
import org.broadleafcommerce.core.web.controller.catalog.BroadleafCategoryController;
import org.broadleafcommerce.core.web.util.ProcessorUtils;
import org.broadleafcommerce.inventory.domain.FulfillmentLocation;
import org.broadleafcommerce.inventory.domain.Inventory;
import org.broadleafcommerce.inventory.service.InventoryService;
import org.broadleafcommerce.profile.web.core.CustomerState;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ssbusy.core.account.domain.MyCustomer;
import com.ssbusy.core.gift.service.GiftService;
import com.ssbusy.core.offer.domain.MyOfferCode;

/**
 * This class works in combination with the CategoryHandlerMapping which finds a
 * category based upon the passed in URL.
 */
@Controller("blCategoryController")
public class CategoryController extends BroadleafCategoryController {
	private static final Log LOG = LogFactory.getLog(CategoryController.class);

	@Value("${offercode.singleuser.maxcount}")
	private int maxoffercodeCount;

	@Resource(name = "blGiftService")
	protected GiftService giftService;

	@Resource(name = "blCatalogService")
	protected CatalogService catalogService;

	@Resource(name = "blInventoryService")
	protected InventoryService inventoryService;

	@Resource(name = "blRatingService")
	protected RatingService ratingService;

	protected static String RETURN_PRODUCT_LIST_ITEM = "/catalog/partials/floorProductListItem";
	public static String RETURN_PRODUCT_WATERFALL_ITEM = "/catalog/partials/productWaterfallItem";
	public static String hotProduct = "/catalog/partials/hotProductItem";

	@Override
	@SuppressWarnings("unchecked")
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView model = new ModelAndView();
		MyCustomer customer = (MyCustomer) CustomerState.getCustomer();

		HttpSession session = request.getSession();
		MyOfferCode myOfferCode = (MyOfferCode) session
				.getAttribute("bonusOfferCode");
		Boolean w_flag = Boolean.FALSE;
		// 查看cookies里领过的次数
		String dateTime = new SimpleDateFormat("yyyy-MM-dd").format(Calendar
				.getInstance().getTime());
		int count = 0;// 单用户当日已发放计数
		Cookie cookies[] = request.getCookies();
		Boolean uiv2 = null;
		if (cookies != null) {
			for (Cookie c : cookies) {
				if (dateTime.equals(c.getName())) {
					count = Integer.valueOf(c.getValue());
					break;
					// } else if ("uiv2".equals(c.getName())) {
					// uiv2 = Boolean.valueOf(c.getValue()); // 2 cookie
				}
			}
		}
		if (cookies != null) {
			for (Cookie c : cookies) {
				if ("SPRING_SECURITY_REMEMBER_ME_COOKIE".equals(c.getName())) {
					model.addObject("rember", c.getValue());
					break;
				}
			}
		}
		// String uiParam = request.getParameter("uiv2");
		// if (StringUtils.isNotEmpty(uiParam)) { // 1 param
		// uiv2 = Boolean.valueOf(uiParam);
		// Cookie c = new Cookie("uiv2", uiv2.toString());
		// c.setPath("/");
		// c.setMaxAge(60 * 60 * 24 * 360);
		// response.addCookie(c);
		// } else if (uiv2 == null) {
		uiv2 = Boolean.TRUE; // 3 default. 二阶段全部開啟
		// }
		session.setAttribute("uiv2", uiv2);
		// LOG.warn("uiv2=" + uiv2);

		if (myOfferCode != null) {
			if (customer.isRegistered())
				giftService.updateOwnerCustomer(customer, myOfferCode);
			else
				myOfferCode = null;
		} else if (count < maxoffercodeCount) {
			myOfferCode = giftService.getgift(customer);
			if (myOfferCode != null) {
				if (customer.isAnonymous()) {
					session.setAttribute("bonusOfferCode", myOfferCode);
					model.addObject("bonusOfferCode", myOfferCode);
					myOfferCode = null;
				}
			}
		}
		if (myOfferCode != null) {
			session.removeAttribute("bonusOfferCode");
			model.addObject("bonusOfferCode", myOfferCode);
			Cookie c = new Cookie(dateTime, String.valueOf(count + 1));
			c.setPath("/");
			c.setMaxAge(60 * 60 * 24);
			response.addCookie(c);
			LOG.info("offerCode sent, id=" + myOfferCode.getId() + ", ip="
					+ request.getRemoteAddr());
		}

		if (request.getParameterMap().containsKey("facetField")) {
			// If we receive a facetField parameter, we need to convert the
			// field to the
			// product search criteria expected format. This is used in
			// multi-facet selection. We
			// will send a redirect to the appropriate URL to maintain canonical
			// URLs

			String fieldName = request.getParameter("facetField");
			List<String> activeFieldFilters = new ArrayList<String>();
			Map<String, String[]> parameters = new HashMap<String, String[]>(
					request.getParameterMap());
			for (Iterator<Entry<String, String[]>> iter = parameters.entrySet()
					.iterator(); iter.hasNext();) {
				Map.Entry<String, String[]> entry = iter.next();
				String key = entry.getKey();
				if (key.startsWith(fieldName + "-")) {
					activeFieldFilters.add(key.substring(key.indexOf('-') + 1));
					iter.remove();
				}
			}

			parameters.remove(ProductSearchCriteria.PAGE_NUMBER);
			parameters.put(fieldName, activeFieldFilters
					.toArray(new String[activeFieldFilters.size()]));
			parameters.remove("facetField");

			String newUrl = ProcessorUtils.getUrl(request.getRequestURL()
					.toString(), parameters);
			model.setViewName("redirect:" + newUrl);
		} else {
			// Else, if we received a GET to the category URL (either the user
			// clicked this link or we redirected
			// from the POST method, we can actually process the results

			Category category = (Category) request
					.getAttribute(CategoryHandlerMapping.CURRENT_CATEGORY_ATTRIBUTE_NAME);
			assert (category != null);

			List<SearchFacetDTO> availableFacets = searchService
					.getCategoryFacets(category);
			ProductSearchCriteria searchCriteria = facetService
					.buildSearchCriteria(request, availableFacets);

			String searchTerm = request
					.getParameter(ProductSearchCriteria.QUERY_STRING);
			ProductSearchResult result;

			List<FulfillmentLocation> locations = null;
			try {
				// 分仓筛选
				if (customer != null && customer.getRegion() != null) {
					InventorySolrSearchServiceExtensionHandler.customerLocation
							.set(locations = customer.getRegion()
									.getFulfillmentLocations());
				}
				if (StringUtils.isNotBlank(searchTerm)) {
					result = searchService.findProductsByCategoryAndQuery(
							category, searchTerm, searchCriteria);
				} else {
					result = searchService.findProductsByCategory(category,
							searchCriteria);
				}
			} finally {
				InventorySolrSearchServiceExtensionHandler.customerLocation
						.remove();
			}

			facetService.setActiveFacetResults(result.getFacets(), request);
			List<Product> products = result.getProducts();

			if (products != null && products.size() > 0) {
				List<String> prodIds = new ArrayList<String>(products.size());
				for (Product product : products) {
					prodIds.add(String.valueOf(product.getId()));
				}
				model.addObject("ratingSums", ratingService
						.readRatingSummaries(prodIds, RatingType.PRODUCT));

				// 列出每个product实际的inventories
				if (locations != null) {
					Map<Product, List<Inventory>> invs = inventoryService
							.listAllInventories(products, locations);
					model.addObject("inventories", invs);
				}
			}

			model.addObject(PRODUCTS_ATTRIBUTE_NAME, products);
			model.addObject(CATEGORY_ATTRIBUTE_NAME, category);
			// 过滤空的facets
			List<SearchFacetDTO> facets = result.getFacets();
			if (facets != null) {
				_nextFact: for (Iterator<SearchFacetDTO> itr = facets
						.iterator(); itr.hasNext();) {
					SearchFacetDTO dto = itr.next();
					if (dto != null && dto.getFacetValues() != null) {
						for (SearchFacetResultDTO searchFacetDTO : dto
								.getFacetValues()) {
							if (searchFacetDTO != null)
								if (searchFacetDTO.getQuantity() != null
										&& searchFacetDTO.getQuantity() > 0)
									continue _nextFact;
						}
					}
					itr.remove();
				}
				model.addObject(FACETS_ATTRIBUTE_NAME, result.getFacets());
			}
			model.addObject(PRODUCT_SEARCH_RESULT_ATTRIBUTE_NAME, result);

			// TODO temp
			String view = category.getDisplayTemplate();
			if (StringUtils.isEmpty(view))
				view = getDefaultCategoryView();
			if(request.getRequestURI().startsWith("/weixin/")){
				view = "weixin/catalog/w_category_item";
				w_flag = Boolean.TRUE;
			}
			if (uiv2) {
				if ("layout/home".equals(view))
					view = "v2/home";
				else{
					if(!view.startsWith("activity")&&!view.startsWith("weixin/")){
						view = "v2/" + view;
					}
						
				}
			}
			session.setAttribute("w_flag", w_flag);
			model.setViewName(view);
		}
		// if (isAjaxRequest(request)) {
		// model.setViewName(RETURN_PRODUCT_WATERFALL_ITEM);
		// model.addObject("ajax", Boolean.TRUE);
		// }
		return model;
	}

	@RequestMapping({ "/catalog/products/page-back",
			"/catalog/products/page-forward" })
	public String pageBack(@RequestParam("category-id") Long categoryId,
			@RequestParam("start") int start,
			@RequestParam("inner-category") String innerCategory, Model model) {
		model.addAttribute("catId", categoryId);
		model.addAttribute("start", BigDecimal.valueOf(start < 0 ? 0 : start));
		model.addAttribute("type", innerCategory);
		return RETURN_PRODUCT_LIST_ITEM;
	}

	@RequestMapping({ "/catalog/hot/page-left", "/catalog/hot/page-right" })
	public String pageLeft(@RequestParam("category-id") Long categoryId,
			@RequestParam("start") int start, Model model) {
		pageBack(categoryId, start, null, model);
		return hotProduct;
	}
}
