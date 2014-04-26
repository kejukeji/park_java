package com.ssbusy.controller.account;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ConnectController {

	@RequestMapping("/stub/oauth/auto")
	public void autoPost(@RequestParam("p") String provider) {
	}

	/**
	 * 三方登录成功后，inAppbrowser自动关闭
	 */
	@RequestMapping("/stub/oauth/logined")
	public void logined() {
	}
}
