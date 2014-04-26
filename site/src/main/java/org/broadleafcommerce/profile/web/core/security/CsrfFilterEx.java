package org.broadleafcommerce.profile.web.core.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.broadleafcommerce.common.exception.ServiceException;
import org.broadleafcommerce.common.security.handler.CsrfFilter;
import org.springframework.security.web.util.AntPathRequestMatcher;
import org.springframework.security.web.util.RequestMatcher;

public class CsrfFilterEx extends CsrfFilter {

	@Override
	public void doFilter(ServletRequest baseRequest,
			ServletResponse baseResponse, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) baseRequest;
		HttpServletResponse response = (HttpServletResponse) baseResponse;

		boolean excludedRequestFound = false;
		if (excludedRequestPatterns != null
				&& excludedRequestPatterns.size() > 0) {
			for (String pattern : excludedRequestPatterns) {
				RequestMatcher matcher = new AntPathRequestMatcher(pattern);
				if (matcher.matches(request)) {
					excludedRequestFound = true;
					break;
				}
			}
		}

		// We only validate CSRF tokens on POST
		if (request.getMethod().equals("POST") && !excludedRequestFound && !request.getRequestURI().startsWith("/weixin/")) {
			String requestToken = request.getParameter(exploitProtectionService
					.getCsrfTokenParameter());
			try {
				exploitProtectionService.compareToken(requestToken);
			} catch (ServiceException e) {
				throw new ServletException(e);
			}
		}

		chain.doFilter(request, response);
	}
}
