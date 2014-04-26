package com.ssbusy.controller.app.util;

import java.util.Map;

import org.broadleafcommerce.common.util.SpringAppContext;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * app login process should use get.
 * 
 * @see UsernamePasswordAuthenticationFilter#setPostOnly(boolean)
 */
public class HackAuthFilterPostOnly {

	public HackAuthFilterPostOnly(String filterProcessesUrl) {
		if (filterProcessesUrl == null)
			return;

		Map<String, UsernamePasswordAuthenticationFilter> filters = SpringAppContext
				.getApplicationContext().getBeansOfType(
						UsernamePasswordAuthenticationFilter.class);
		if (filters == null)
			return;

		for (UsernamePasswordAuthenticationFilter f : filters.values()) {
			if (filterProcessesUrl.equals(f.getFilterProcessesUrl())) {
				f.setPostOnly(false);
			}
		}
	}

}
