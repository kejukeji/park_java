package com.ssbusy.admin.controller.order;

import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.broadleafcommerce.inventory.domain.FulfillmentLocation;
import org.broadleafcommerce.openadmin.server.security.domain.AdminUser;
import org.broadleafcommerce.openadmin.server.security.remote.SecurityVerifier;
import org.broadleafcommerce.openadmin.server.security.service.AdminSecurityService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ssbusy.admin.user.domain.MyAdminUser;
import com.ssbusy.core.myorder.service.MyOrderService;

@Controller
@RequestMapping("/order-warn")
public class OrderWarnController {

	@Resource(name = "ssbMyOrderService")
	protected MyOrderService myOrderService;
	@Resource(name = "blAdminSecurityService")
	protected AdminSecurityService securityService;
	@Resource(name = "blAdminSecurityRemoteService")
	protected SecurityVerifier securityVerifier;

	@ResponseBody
	@RequestMapping
	protected Long orderWarn(HttpServletRequest request,
			@RequestParam("lastTime") long lastTime) {
		AdminUser adminUser = securityVerifier.getPersistentAdminUser();
		FulfillmentLocation fulfillmentLocation = ((MyAdminUser) adminUser)
				.getFulfillmentLocation();
		Long count = myOrderService.countOrderByTime(new Date(lastTime),
				fulfillmentLocation);
		// List<Long> orderids=myOrderService.countOrderByTime(new
		// Date(lastTime),fulfillmentLocation);
		return count;
	}
}
