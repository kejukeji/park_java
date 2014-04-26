package org.broadleafcommerce.profile.web.core.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.broadleafcommerce.profile.web.core.CustomerState;
import org.springframework.web.context.request.ServletWebRequest;

import com.ssbusy.core.account.domain.MyCustomer;

public class CustomerStateFilterEx extends CustomerStateFilter {

	private String regionView = "/region";
	private String regionViewApp = "/app/region";

	@Override
	public void doFilter(ServletRequest baseRequest,
			ServletResponse baseResponse, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) baseRequest;
		customerStateProcessor.process(new ServletWebRequest(req,
				(HttpServletResponse) baseResponse));
		MyCustomer customer = (MyCustomer) CustomerState.getCustomer();
		if (customer.getRegion() == null && isNotSpider(req)) {
			String uri = req.getRequestURI();
			// TODO /d/ 是下载页，不需要region
			if(uri.startsWith("/weixin/")&&!uri.startsWith("/weixin/region")){
				if(!uri.startsWith("/weixin/service")){
					((HttpServletResponse) baseResponse).sendRedirect("/weixin/region?redirect="+req.getRequestURI());
					return;
				}
			}
			if (!uri.startsWith(regionView) && !uri.startsWith(regionViewApp)
					&& !uri.startsWith("/signin") && !uri.startsWith("/login")
					&& !uri.startsWith("/extern")
					&& !uri.startsWith("/register") && !uri.startsWith("/d/")
					&& !uri.startsWith("/dl/") && !uri.startsWith("/app/d/")&& !uri.startsWith("/weixin/")) {
				// redirect，必选校区
				((HttpServletResponse) baseResponse)
						.sendRedirect(isAppRequest(baseRequest) ? regionViewApp
						// TODO hardcoded surfix '/redirect'
								+ "/redirect?" + req.getQueryString()
								: regionView + "?redirect="
										+ req.getRequestURI());
				return;
			}
		}
		chain.doFilter(baseRequest, baseResponse);
	}

	private boolean isNotSpider(HttpServletRequest req) {
		String ua = req.getHeader("user-agent");
		return (ua == null || ua.indexOf("pider") < 0 || ua.indexOf("bot") < 0 || ua
				.indexOf("Bot") < 0);
	}

	private boolean isAppRequest(ServletRequest baseRequest) {
		return "jsonp".equals(baseRequest.getParameter("format"))
				&& baseRequest.getParameter("callback") != null;
	}

	public String getRegionView() {
		return regionView;
	}

	public void setRegionView(String regionView) {
		this.regionView = regionView;
	}

	public String getRegionViewApp() {
		return regionViewApp;
	}

	public void setRegionViewApp(String regionViewApp) {
		this.regionViewApp = regionViewApp;
	}

}
