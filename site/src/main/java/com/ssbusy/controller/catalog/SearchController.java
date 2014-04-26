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

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.broadleafcommerce.common.exception.ServiceException;
import org.broadleafcommerce.core.catalog.domain.Product;
import org.broadleafcommerce.core.search.domain.SearchFacetDTO;
import org.broadleafcommerce.core.search.domain.SearchFacetResultDTO;
import org.broadleafcommerce.core.search.service.solr.InventorySolrSearchServiceExtensionHandler;
import org.broadleafcommerce.core.web.controller.catalog.BroadleafSearchController;
import org.broadleafcommerce.inventory.domain.FulfillmentLocation;
import org.broadleafcommerce.inventory.domain.Inventory;
import org.broadleafcommerce.inventory.service.InventoryService;
import org.broadleafcommerce.profile.web.core.CustomerState;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ssbusy.core.account.domain.MyCustomer;

@Controller
@RequestMapping("/search")
public class SearchController extends BroadleafSearchController {

	@Resource(name = "blInventoryService")
	protected InventoryService inventoryService;

	@Override
	@RequestMapping
	public String search(Model model, HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "q", required = false) String q)
			throws ServletException, IOException, ServiceException {
		String v;
		List<FulfillmentLocation> locations = null;
		try {
			// 分仓筛选
			MyCustomer c = (MyCustomer) CustomerState.getCustomer();
			if (c != null && c.getRegion() != null) {
				InventorySolrSearchServiceExtensionHandler.customerLocation
						.set(locations = c.getRegion().getFulfillmentLocations());
			}
			v = super.search(model, request, response, q);
		} finally {
			InventorySolrSearchServiceExtensionHandler.customerLocation
					.remove();
		}
		@SuppressWarnings("unchecked")
		List<Product> prods = (List<Product>) model.asMap().get(
				PRODUCTS_ATTRIBUTE_NAME);
		if (prods != null && prods.size() > 0) {
			model.addAttribute(PRODUCTS_ATTRIBUTE_NAME, prods);
			// 列出每个product实际的inventories
			if (locations != null) {
				Map<Product, List<Inventory>> invs = inventoryService
						.listAllInventories(prods, locations);
				model.addAttribute("inventories", invs);
			}
		}

		// if (isAjaxRequest(request)) {
		// model.addAttribute("ajax", Boolean.TRUE);
		// return CategoryController.RETURN_PRODUCT_WATERFALL_ITEM;
		// }
		// Boolean uiv2 = (Boolean) request.getSession().getAttribute("uiv2");
		if (super.getSearchView().equals(v)) {
			v = "v2/catalog/category";
		}
		// 过滤空的facets
		@SuppressWarnings("unchecked")
		List<SearchFacetDTO> facets = (List<SearchFacetDTO>) model.asMap().get(
				"facets");
		if (facets != null) {
			_nextFact: for (Iterator<SearchFacetDTO> itr = facets.iterator(); itr
					.hasNext();) {
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
		}
		return v;
	}
}