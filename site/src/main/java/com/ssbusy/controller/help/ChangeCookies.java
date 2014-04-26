package com.ssbusy.controller.help;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ChangeCookies {

	@ResponseBody
	@RequestMapping("/region/changecookies")
	public String changeCookies(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam("regionid") String regionid,
			@RequestParam("rememberme") String rememberme) {
		Cookie cookie = new Cookie("regionid", regionid);
		cookie.setMaxAge((int) (60 * 60 * 24 * 365));
		cookie.setPath("/");
		response.addCookie(cookie);
		Cookie cookierem = new Cookie("SPRING_SECURITY_REMEMBER_ME_COOKIE",rememberme);
		cookierem.setMaxAge((int) (60 * 60 * 24 * 14));
		cookierem.setPath("/");
		response.addCookie(cookierem);
		return null;
	}
}
