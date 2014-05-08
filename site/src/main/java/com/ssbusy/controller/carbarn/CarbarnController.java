package com.ssbusy.controller.carbarn;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ssbusy.core.carbarn.domain.Carbarn;
import com.ssbusy.core.carbarn.service.CarbarnService;

/**
 * 
 * @author song
 * 
 */
@Controller
public class CarbarnController {

	private int pageSize = 10;

	@Resource(name = "carbarnService")
	protected CarbarnService carbarnService;
	protected String ak = "F86bd27c9f27ad2db50f6802ef6809d1";
	protected String tableId = "60709";
	protected String localUrl = "http://api.map.baidu.com/geosearch/v3/local";
	protected String nearby = "http://api.map.baidu.com/geosearch/v3/nearby";
	private static final Log LOG = LogFactory.getLog(CarbarnController.class);
	@RequestMapping(value = "/carbarn/carbarn-name", produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public String readCarbarnsByCarbarnName(
			HttpServletRequest request,
			HttpServletResponse response,
			Model model,
			@RequestParam("carbarn_name") String carbarnName,
			@RequestParam(value = "page_show", required = false) Integer pageShow) {
		List<Carbarn> carbarns = carbarnService
				.readCarbarnByCarbarnName(carbarnName);
		List<Carbarn> returnCarbarns = null;
		returnCarbarns = showPage(pageShow, carbarns);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setExcludes(new String[] { "carbarn" });
		JSONArray jsonArray = JSONArray.fromObject(returnCarbarns, jsonConfig);
		/*Map<String, Object> pageCount = new HashMap<String, Object>();
		pageCount.put("pageCount", getPageCount(carbarns.size()));
		jsonArray.add(pageCount);*/
		return jsonArray.toString();
	}

	private List<Carbarn> showPage(Integer pageShow, List<Carbarn> carbarns) {
		List<Carbarn> returnCarbarns;
		int pageCount = getPageCount(carbarns.size());
		if (pageShow == null || pageShow > pageCount) {
			pageShow = 1;
		}
		if (pageShow > 0 && pageShow < pageCount) {
			returnCarbarns = carbarns.subList((pageShow - 1) * pageSize,
					pageShow * pageSize);
		} else if (pageShow == pageCount) {
			returnCarbarns = carbarns.subList((pageShow - 1) * pageSize,
					carbarns.size());
		} else {
			returnCarbarns = carbarns;
		}
		return returnCarbarns;
	}

	@RequestMapping(value = "/carbarn/carbarn-address", produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public String readCarbarnsByCarbarnAddress(
			HttpServletRequest request,
			HttpServletResponse response,
			Model model,
			@RequestParam("carbarn_address") String carbarnAddress,
			@RequestParam(value = "page_show", required = false) Integer pageShow) {
		List<Carbarn> carbarns = carbarnService
				.readCarbarnByCarbarnAddress(carbarnAddress);
		List<Carbarn> returnCarbarns = null;
		returnCarbarns = showPage(pageShow, carbarns);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setExcludes(new String[] { "carbarn" });
		JSONArray jsonArray = JSONArray.fromObject(returnCarbarns, jsonConfig);
		/*Map<String, Object> pageCount = new HashMap<String, Object>();
		pageCount.put("pageCount", getPageCount(carbarns.size()));
		jsonArray.add(pageCount);*/
		LOG.error("Carbarn-address in");
		LOG.error(jsonArray.toString());
		return jsonArray.toString();
	}

	@RequestMapping(value = "/carbarn/latitude-longitude", produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public String readCarbarnsByLatitudeAndLongitude(
			HttpServletRequest request,
			HttpServletResponse response,
			Model model,
			@RequestParam("latitude") Double latitude,
			@RequestParam("longitude") Double longitude,
			@RequestParam(value = "page_show", required = false) Integer pageShow,
			@RequestParam(value = "sortBy", required = false) String sortBy) {
		List<Carbarn> carbarns = carbarnService
				.readCarbarnByLatitudeAndLongitude(latitude, longitude,sortBy);
		List<Carbarn> returnCarbarns = null;
		returnCarbarns = showPage(pageShow, carbarns);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setExcludes(new String[] { "carbarn" });
		JSONArray jsonArray = JSONArray.fromObject(returnCarbarns, jsonConfig);
		/*Map<String, Object> pageCount = new HashMap<String, Object>();
		pageCount.put("pageCount", getPageCount(carbarns.size()));
		jsonArray.add(pageCount);*/
		return jsonArray.toString();
	}

	@RequestMapping(value = "/carbarn/update", produces = { "application/json;charset=UTF-8" })//,method = RequestMethod.POST
	@ResponseBody
	public String updateCarbarnById(
			HttpServletRequest request,
			HttpServletResponse response,
			Model model,
			@RequestParam("id") Long id,
			@RequestParam("quantity") Integer quantity) {
		Carbarn carbarn = carbarnService.readCarbarnById(id);
		Map<String,Object> returnMap = new HashMap<String, Object>(2);
		if(carbarn==null||quantity==null){
			returnMap.put("status", 400);
			returnMap.put("message", "没有找到对应的出库数据");
		}else{
			if(quantity<0||quantity>carbarn.getCarbarnTotal()){
				returnMap.put("status", 401);
				returnMap.put("message", "剩余车位数量应该大于0小于车库车位总数");
			}else{
				carbarn.setCarbarnLast(quantity);
				carbarn = carbarnService.updateCarbarn(carbarn);
				if(carbarn.getCarbarnLast()!=null&&carbarn.getCarbarnLast()==quantity){
					returnMap.put("status", 0);
					returnMap.put("message", "更新成功");
				}else{
					returnMap.put("status", 402);
					returnMap.put("message", "更新失败");
				}
			}
		}
		JSONObject jsonObject = JSONObject.fromObject(returnMap); 
		return jsonObject.toString();
	}
	protected int getPageCount(int size) {
		return (size / (pageSize + 1)) + 1;
	}
	/*
	 * @RequestMapping("/carbarn/search/nearbyby") public String
	 * searchByPosition(String name) { Map<String, String> sPara = new
	 * HashMap<String, String>(); sPara.put("region", "黄埔"); sPara.put("ak",
	 * "F86bd27c9f27ad2db50f6802ef6809d1"); sPara.put("geotable_id", tableId);
	 * SsbusyHttpRequest httpRequest = new SsbusyHttpRequest();
	 * SsbusyHttpResponse response = null;
	 * httpRequest.setContentEncoding("utf-8"); try { response =
	 * httpRequest.sendGet(localUrl, sPara); } catch (IOException e) {
	 * e.printStackTrace(); } JSONObject jsonObject =
	 * JSONObject.fromObject(response.getContent()); JSONArray jsonArray =
	 * jsonObject.getJSONArray("contents"); List<CarbarnForm> carbarnForms = new
	 * ArrayList<CarbarnForm>(); for (int i = 0; i < jsonArray.size(); i++) {
	 * JSONObject carbarnFormObject = (JSONObject) jsonArray.get(i); CarbarnForm
	 * carbarnForm = (CarbarnForm) JSONObject.toBean( carbarnFormObject,
	 * CarbarnForm.class); if (carbarnForm != null) {
	 * carbarnForms.add(carbarnForm); } } CarbarnTotalForm carbarnTotalForm =
	 * (CarbarnTotalForm) JSONObject .toBean(jsonObject,
	 * CarbarnTotalForm.class); carbarnTotalForm.setContents(carbarnForms);
	 * String res = JSONObject.fromObject(carbarnTotalForm).toString(); return
	 * res; }
	 */
}
