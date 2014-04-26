package com.ssbusy.controller.cart;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.broadleafcommerce.common.web.controller.BroadleafControllerUtility;
import org.broadleafcommerce.profile.web.core.CustomerState;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ssbusy.core.account.domain.MyCustomer;
import com.ssbusy.core.account.service.MyCustomerService;
import com.ssbusy.core.region.domain.Region;
import com.ssbusy.core.region.service.RegionService;

@Controller
public class RegionController {
	protected static String regionView = "/layout/region";
	protected static String firstView = "layout/home";
	@Resource(name = "ssbRegionService")
	protected RegionService regionService;
	@Resource(name = "blCustomerService")
	protected MyCustomerService myCustomerService;

	@RequestMapping("/region")
	public String getRegion(
			@RequestParam(value = "redirect", required = false) String redirect,
			HttpServletRequest request, HttpServletResponse response,
			Model model) {
		if (BroadleafControllerUtility.isAjaxRequest(request)) {
			return regionView;
		} else {
			Cookie cookies[] = request.getCookies();
			Cookie c = null;
			if (cookies != null) {
				for (int i = 0; i < cookies.length; i++) {
					c = cookies[i];
					if (c.getName().equals("regionid")) {
						long id = Long.valueOf(c.getValue());
						Region region = regionService.getRegion(id);
						MyCustomer myCustomer = (MyCustomer) CustomerState
								.getCustomer();
						myCustomer.setRegion(region);
						if (!StringUtils.isBlank(CustomerState.getCustomer()
								.getUsername()))
							myCustomerService.setRegion(region,
									myCustomer.getId());
						if (StringUtils.isEmpty(redirect)
								|| redirect.startsWith("/region"))
							redirect = "/";
						return redirect(redirect);
					}
				}
			}
		}
		model.addAttribute("regions", regionService.listRegions());
		model.addAttribute("isAjax",
				BroadleafControllerUtility.isAjaxRequest(request));
		return regionView;
	}

	/**
	 * @author song
	 * @param redirect
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/weixin/region")
	public String getRegionForWeixin(
			@RequestParam(value = "redirect", required = false) String redirect,
			HttpServletRequest request, HttpServletResponse response,
			Model model) {
		Cookie cookies[] = request.getCookies();
		Cookie c = null;
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				c = cookies[i];
				if (c.getName().equals("regionid")) {
					long id = Long.valueOf(c.getValue());
					Region region = regionService.getRegion(id);
					MyCustomer myCustomer = (MyCustomer) CustomerState
							.getCustomer();
					myCustomer.setRegion(region);
					if (!StringUtils.isBlank(CustomerState.getCustomer()
							.getUsername()))
						myCustomerService.setRegion(region, myCustomer.getId());
					if (StringUtils.isEmpty(redirect)
							|| redirect.startsWith("/weixin/region"))
						redirect = "/weixin/home";
					return "redirect:" + redirect;
				}
			}
		}
		model.addAttribute("regions", regionService.listRegions());
		return "weixin/w_region";
	}

	/**
	 * @author song
	 * @param id
	 * @param redirect
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/weixin/region/select")
	public String regionSelectWeixin(
			@RequestParam("id") Long id,
			@RequestParam(value = "redirect", required = false) String redirect,
			HttpServletRequest request, HttpServletResponse response) {
		Region region = regionService.getRegion(id);
		Cookie cregion = new Cookie("regionid", id.toString());
		cregion.setMaxAge(60 * 60 * 24 * 365);
		response.addCookie(cregion);
		MyCustomer myCustomer = (MyCustomer) CustomerState.getCustomer();
		myCustomer.setRegion(region);
		if (!StringUtils.isBlank(CustomerState.getCustomer().getUsername()))
			myCustomerService.setRegion(region, myCustomer.getId());
		return "redirect:" + redirect;
	}

	@RequestMapping("/region/select")
	public String select(
			@RequestParam("id") Long id,
			@RequestParam(value = "redirect", required = false) String redirect,
			HttpServletRequest request, HttpServletResponse response) {
		Region region = regionService.getRegion(id);
		Cookie cregion = new Cookie("regionid", id.toString());
		cregion.setMaxAge(60 * 60 * 24 * 365);
		response.addCookie(cregion);
		// myCustomerService.setRegion(region,CustomerState.getCustomer().getId());
		MyCustomer myCustomer = (MyCustomer) CustomerState.getCustomer();
		myCustomer.setRegion(region);
		if (!StringUtils.isBlank(CustomerState.getCustomer().getUsername()))
			myCustomerService.setRegion(region, myCustomer.getId());
		return redirect(redirect);
	}

	private String redirect(String redirect) {
		if (StringUtils.isEmpty(redirect) || redirect.startsWith("/region"))
			redirect = "/";
		return "redirect:" + redirect;
	}

	@RequestMapping("/app/region")
	@ResponseBody
	public List<Map<String, Object>> getRegionsApp() {
		List<Region> regions = regionService.listRegions();
		if (regions == null || regions.isEmpty())
			return Collections.emptyList();
		List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>(
				regions.size());
		for (Region region : regions) {
			ret.add(wrapRegion(region));
		}
		return ret;
	}

	@RequestMapping("/app/region/select")
	@ResponseBody
	public List<?> selectRegionApp(@RequestParam("id") Long id,
			HttpServletRequest request, HttpServletResponse response) {
		this.select(id, "/", request, response);
		return Collections.emptyList();
	}

	@RequestMapping("/app/region/redirect")
	@ResponseBody
	public Map<String, Object> getRegionsAppRedirect() {
		Map<String, Object> ret = new HashMap<String, Object>(1);
		ret.put("error", "region_select");
		return ret;
	}

	private Map<String, Object> wrapRegion(Region region) {
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("id", region.getId());
		m.put("name", region.getRegionName());
		return m;
	}
}
