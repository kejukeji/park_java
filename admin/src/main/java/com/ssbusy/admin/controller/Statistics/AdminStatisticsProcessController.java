package com.ssbusy.admin.controller.Statistics;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.broadleafcommerce.openadmin.server.security.remote.SecurityVerifier;
import org.broadleafcommerce.openadmin.web.controller.AdminAbstractController;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ssbusy.admin.Statistics.service.StatisticsService;
import com.ssbusy.admin.user.domain.MyAdminUser;
import com.ssbusy.core.region.service.RegionService;

@Controller
public class AdminStatisticsProcessController extends AdminAbstractController {

	@Resource(name = "ssbStatisticsService")
	private StatisticsService statisticsService;

	@Resource(name = "blAdminSecurityRemoteService")
	protected SecurityVerifier securityVerifier;

	@Resource(name = "ssbRegionService")
	protected RegionService regionService;

	@RequestMapping("/statistics")
	public String showStatisticsInfo(HttpServletRequest request,
			HttpServletResponse response, Model model) {
		super.setModelAttributes(model, "statistics");
		model.addAttribute("regions", regionService.listRegions());
		return "statistics/statistics";
	}

	@RequestMapping("/statistics-productsales")
	public String showProductSalesInfo(HttpServletRequest request,
			@RequestParam("startdate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam("enddate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
			HttpServletResponse response, Model model) {
		super.setModelAttributes(model, "statistics");
		MyAdminUser adminUser = (MyAdminUser) securityVerifier
				.getPersistentAdminUser();
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
		if(startDate==null){
			String date = sd.format(new Date());
			try {
				startDate = sd.parse(date);
				endDate = sd.parse(sd.format(new Date(
						new Date().getTime() + 3600 * 1000 * 24)));
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
		}
		else if(endDate ==null){
			try {
				endDate = sd.parse(sd.format(new Date(
						new Date().getTime() + 3600 * 1000 * 24)));
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
		}
		
		model.addAttribute("productSalesform", statisticsService
				.getProductSalesInfoForm(startDate, endDate, adminUser
						.getFulfillmentLocation().getId()));
		return "statistics/productsales";
	}
	
	@RequestMapping("/statistics-totalsales")
	public String showTotalSalesInfo(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "regionIds", required = false) Long[] regionIds,
			@RequestParam("startdate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam("enddate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
			Model model) {
		super.setModelAttributes(model, "statistics");
		if (regionIds == null || regionIds.length == 0) {
			regionIds = new Long[1];
			regionIds[0] = 1L;
		}
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
		if(startDate==null){
			String date = sd.format(new Date());
			try {
				startDate = sd.parse(date);
				endDate = sd.parse(sd.format(new Date(
						new Date().getTime() + 3600 * 1000 * 24)));
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
		}
		else if(endDate ==null){
			try {
				endDate = sd.parse(sd.format(new Date(
						new Date().getTime() + 3600 * 1000 * 24)));
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
		}
		model.addAttribute("totalSalesForm", statisticsService
				.getTotalSalesForm(regionIds, startDate, endDate));
		return "statistics/totalsales";
	}

	@RequestMapping("/statistics-adminuserinfo")
	public String showAdminUserInfo(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "regionId", required = false) Long regionId,
			@RequestParam("startdate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam("enddate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
			Model model) {
		super.setModelAttributes(model, "statistics");
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
		if(startDate==null){
			String date = sd.format(new Date());
			try {
				startDate = sd.parse(date);
				endDate = sd.parse(sd.format(new Date(
						new Date().getTime() + 3600 * 1000 * 24)));
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
		}
		else if(endDate ==null){
			try {
				endDate = sd.parse(sd.format(new Date(
						new Date().getTime() + 3600 * 1000 * 24)));
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
		}
		model.addAttribute("userForm", statisticsService.getAdminUserInfoForm(
				startDate, endDate, regionId));
		return "statistics/userinfo";
	}

	@RequestMapping("/statistics-adminorderinfo")
	public String showAdminOrderInfo(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "regionId", required = false) Long regionId,
			@RequestParam("startdate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam("enddate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
			Model model) {
		super.setModelAttributes(model, "statistics");
	/*	if(startDate==null||endDate==null){
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
			String date = sd.format(new Date());
			try {
				startDate = sd.parse(date);
				endDate = sd.parse(sd.format(new Date(
						new Date().getTime() + 3600 * 1000 * 24)));
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
		}*/
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
		if(startDate==null){
			String date = sd.format(new Date());
			try {
				startDate = sd.parse(date);
				endDate = sd.parse(sd.format(new Date(
						new Date().getTime() + 3600 * 1000 * 24)));
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
		}
		else if(endDate ==null){
			try {
				endDate = sd.parse(sd.format(new Date(
						new Date().getTime() + 3600 * 1000 * 24)));
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
		}
		model.addAttribute("orderForm", statisticsService
				.getAdminOrderInfoForm(startDate, endDate, regionId));
		return "statistics/orderinfo";
	}
}
