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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.broadleafcommerce.core.catalog.service.CatalogService;
import org.broadleafcommerce.core.rating.domain.ReviewDetail;
import org.broadleafcommerce.core.rating.service.type.RatingSortType;
import org.broadleafcommerce.core.rating.service.type.RatingType;
import org.broadleafcommerce.core.web.controller.catalog.BroadleafRatingsController;
import org.broadleafcommerce.core.web.controller.catalog.ReviewForm;
import org.broadleafcommerce.profile.web.core.CustomerState;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RatingsController extends BroadleafRatingsController {

	@Resource(name = "blCatalogService")
	private CatalogService catalogService;

	@RequestMapping(value = "/reviews/product/{itemId}", method = RequestMethod.GET)
	public String viewReviewForm(HttpServletRequest request, Model model,
			@PathVariable("itemId") String itemId,
			@ModelAttribute("reviewForm") ReviewForm form) {
		return super.viewReviewForm(request, model, form, itemId);
	}
	
	@ResponseBody
	@RequestMapping(value = "/reviews/product/{itemId}", method = RequestMethod.POST)
	public Map<String, Object> reviewItem(HttpServletRequest request, Model model,
			@PathVariable("itemId") String itemId,
			@ModelAttribute("reviewForm") ReviewForm form) {
		Map<String,Object> ret = new HashMap<String, Object>(1); 
		if("".equals(form.getReviewText())||form.getReviewText()==null){
			ret.put("message", "failed");
		}else{
			ratingService.reviewItem(itemId, RatingType.PRODUCT,
					CustomerState.getCustomer(), form.getRating(),
					form.getReviewText());
			ret.put("message", "success");
		}
		return ret;
	}

	// app //////////////////////////

	@RequestMapping("/app/reviews/product/add")
	@ResponseBody
	public List<String> addProductReview(@RequestParam("itemId") String itemId,
			@RequestParam("reviewText") String reviewText) {
		ratingService.reviewItem(itemId, RatingType.PRODUCT,
				CustomerState.getCustomer(), 5d, reviewText);
		return Arrays.asList("ok");
	}

	@RequestMapping("/app/reviews/product/{itemId}/list")
	@ResponseBody
	public List<Map<String, Object>> showProductReviews(
			@PathVariable("itemId") String itemId) {
		List<ReviewDetail> reviews = new ArrayList<ReviewDetail>(15);
		ReviewDetail myReview = ratingService.readReviewByCustomerAndItem(
				CustomerState.getCustomer(), itemId);
		if (myReview != null)
			reviews.add(myReview);
		reviews.addAll(ratingService.readReviews(itemId, RatingType.PRODUCT, 0,
				14, RatingSortType.MOST_RECENT));
		return wrapReviews(reviews);
	}

	private List<Map<String, Object>> wrapReviews(List<ReviewDetail> readReviews) {
		if (readReviews == null || readReviews.isEmpty())
			return Collections.emptyList();

		List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>(
				readReviews.size());
		boolean hasMine = false;
		for (ReviewDetail r : readReviews) {
			boolean mine = CustomerState.getCustomer().getId()
					.equals(r.getCustomer().getId());
			if (hasMine && mine)
				continue;
			hasMine |= mine;
			Map<String, Object> m = new HashMap<String, Object>();
			ret.add(m);
			m.put("reviewText", r.getReviewText());
			m.put("reviewDate", r.getReviewSubmittedDate());
			m.put("reviewerId", r.getCustomer().getId());
			m.put("mine", mine);
			m.put("reviewerName", mine ? "我的评价" : r.getCustomer()
					.getFirstName());
		}
		return ret;
	}

}
