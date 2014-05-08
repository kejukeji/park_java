package com.ssbusy.controller.weixin;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 
 * @author song
 * 
 */
@Controller
public class WeixinController {

	@RequestMapping(value = "/weixin/service", method = RequestMethod.GET)
	public void checkSignature(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "signature", required = false) String signature,
			@RequestParam(value = "timestamp", required = false) String timestamp,
			@RequestParam(value = "nonce", required = false) String nonce,
			@RequestParam(value = "echostr", required = false) String echostr,
			Model model) throws IOException {
		/*PrintWriter out = response.getWriter();
		if (WeixinConfig.checkSignature(signature, timestamp, nonce)) {
			out.print(echostr);
		}
		out.close();
		out = null;*/
	}

	@RequestMapping(value = "/weixin/service")
	public void weixinCoreController(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		/*String respMessage = WeixinService.processRequest(request);
		PrintWriter out = response.getWriter();
		out.print(respMessage);
		out.close();*/
	}

	@RequestMapping(value = "/weixin/home")
	public String weixinHome(HttpServletRequest request,
			HttpServletResponse response, Model model) throws IOException {

		return "weixin/w_home";
	}

	@RequestMapping(value = "/weixin/category")
	public String weixinCat(HttpServletRequest request,
			HttpServletResponse response, Model model) throws IOException {

		return "weixin/w_category";
	}
	
	@RequestMapping(value = "/weixin/userinfo")
	public String weixinUserInfo(HttpServletRequest request,
			HttpServletResponse response, Model model) throws IOException {

		return "weixin/user/w_userinfo";
	}
}
