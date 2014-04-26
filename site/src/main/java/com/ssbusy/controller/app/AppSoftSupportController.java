package com.ssbusy.controller.app;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AppSoftSupportController {
	
	private String androidDownload = "redirect:/dl/onxiao.apk";
	private String iosShow = "content/aboutios";
	private String windowsShow = "content/aboutapp";
	@RequestMapping("/d/app")
	public String checkUserOs(HttpServletRequest request,Model model){
		String agent = request.getHeader("user-agent");
		if(agent.matches("^.*Mac OS X.*$"))
			return iosShow;
		if(agent.matches("^.*Android.*$"))
			return androidDownload;
		return windowsShow;
	}
	
}
