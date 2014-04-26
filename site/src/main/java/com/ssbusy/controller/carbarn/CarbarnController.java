package com.ssbusy.controller.carbarn;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ssbusy.core.carbarn.domain.Carbarn;
import com.ssbusy.core.carbarn.service.CarbarnService;

/**
 * 
 * @author song
 * 
 */
@Controller
public class CarbarnController {

	@Resource(name = "carbarnService")
	protected CarbarnService carbarnService;
	protected String ak = "F86bd27c9f27ad2db50f6802ef6809d1";
	protected String tableId = "60709";
	protected String localUrl = "http://api.map.baidu.com/geosearch/v3/local";
	protected String nearby = "http://api.map.baidu.com/geosearch/v3/nearby";

	@RequestMapping("/carbarn/carbarn-name")
	public String readCarbarnsByCarbarnName(HttpServletRequest request,
			HttpServletResponse response, Model model,@RequestParam("carbarn_name") String carbarnName) {
		List<Carbarn> carbarns = carbarnService.readCarbarnByCarbarnName(carbarnName);
		JsonConfig  jsonConfig = new JsonConfig();
		jsonConfig.setExcludes(new String[]{"carbarn"});
		JSONArray jsonArray = JSONArray.fromObject(carbarns,jsonConfig);
		return jsonArray.toString();
	}
	@RequestMapping("/carbarn/carbarn-address")
	public String readCarbarnsByCarbarnAddress(HttpServletRequest request,
			HttpServletResponse response, Model model,@RequestParam("carbarn_address") String carbarnAddress) {
		List<Carbarn> carbarns = carbarnService.readCarbarnByCarbarnAddress(carbarnAddress);
		JsonConfig  jsonConfig = new JsonConfig();
		jsonConfig.setExcludes(new String[]{"carbarn"});
		JSONArray jsonArray = JSONArray.fromObject(carbarns,jsonConfig);
		return jsonArray.toString();
	}
	
	@RequestMapping("/carbarn/latitude-longitude")
	public String readCarbarnsByLatitudeAndLongitude(HttpServletRequest request,
			HttpServletResponse response, Model model,@RequestParam("latitude") Double latitude,@RequestParam("longitude") Double longitude) {
		List<Carbarn> carbarns = carbarnService
				.readCarbarnByLatitudeAndLongitude(latitude, longitude);
		JsonConfig  jsonConfig = new JsonConfig();
		jsonConfig.setExcludes(new String[]{"carbarn"});
		JSONArray jsonArray = JSONArray.fromObject(carbarns,jsonConfig);
		return jsonArray.toString();
	}
	

/*	@RequestMapping("/carbarn/search/nearbyby")
	public String searchByPosition(String name) {
		Map<String, String> sPara = new HashMap<String, String>();
		sPara.put("region", "黄埔");
		sPara.put("ak", "F86bd27c9f27ad2db50f6802ef6809d1");
		sPara.put("geotable_id", tableId);
		SsbusyHttpRequest httpRequest = new SsbusyHttpRequest();
		SsbusyHttpResponse response = null;
		httpRequest.setContentEncoding("utf-8");
		try {
			response = httpRequest.sendGet(localUrl, sPara);
		} catch (IOException e) {
			e.printStackTrace();
		}
		JSONObject jsonObject = JSONObject.fromObject(response.getContent());
		JSONArray jsonArray = jsonObject.getJSONArray("contents");
		List<CarbarnForm> carbarnForms = new ArrayList<CarbarnForm>();
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject carbarnFormObject = (JSONObject) jsonArray.get(i);
			CarbarnForm carbarnForm = (CarbarnForm) JSONObject.toBean(
					carbarnFormObject, CarbarnForm.class);
			if (carbarnForm != null) {
				carbarnForms.add(carbarnForm);
			}
		}
		CarbarnTotalForm carbarnTotalForm = (CarbarnTotalForm) JSONObject
				.toBean(jsonObject, CarbarnTotalForm.class);
		carbarnTotalForm.setContents(carbarnForms);
		String res = JSONObject.fromObject(carbarnTotalForm).toString();
		return res;
	}*/
}
