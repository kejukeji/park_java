package org.springframework.security.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ForwardRedirectStrategy extends DefaultRedirectStrategy {

	@Override
	public void sendRedirect(HttpServletRequest request,
			HttpServletResponse response, String url) throws IOException {
		try {
			request.getRequestDispatcher(url).forward(request, response);
		} catch (ServletException e) {
			logger.error("Error do forward, so try redirect. url=" + url, e);
			super.sendRedirect(request, response, url);
		}
	}

}
