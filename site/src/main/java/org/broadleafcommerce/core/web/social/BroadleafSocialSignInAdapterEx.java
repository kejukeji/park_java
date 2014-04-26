package org.broadleafcommerce.core.web.social;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.broadleafcommerce.common.security.MergeCartProcessor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.social.connect.Connection;
import org.springframework.web.context.request.NativeWebRequest;

public class BroadleafSocialSignInAdapterEx extends
		BroadleafSocialSignInAdapter {

	private String postSignInUrl;
	@Resource(name = "blMergeCartProcessor")
	protected MergeCartProcessor mergeCartProcessor;
	@Resource(name="blcRememberMeService")
	protected RememberMeServices rememberMeServices;

	public String signIn(String username, Connection<?> connection,
			NativeWebRequest request) {
		String s = super.signIn(username, connection, request);
		// TODO resp null?
		mergeCartProcessor.execute(
				request.getNativeRequest(HttpServletRequest.class), null,
				SecurityContextHolder.getContext().getAuthentication());

		// this is a hack
		//rememberMe
		
		HttpServletRequest httpRequest=(HttpServletRequest)request.getNativeRequest();
		HttpServletResponse httpResponse=(HttpServletResponse)request.getNativeResponse();
		rememberMeServices.loginSuccess(httpRequest,httpResponse,SecurityContextHolder.getContext().getAuthentication());
		String referer = request.getHeader("Referer");
		if (referer != null && referer.indexOf("termtype=app") > 0)
			return postSignInUrl;
		return s;
	}

	public String getPostSignInUrl() {
		return postSignInUrl;
	}

	public void setPostSignInUrl(String postSignInUrl) {
		this.postSignInUrl = postSignInUrl;
	}
}
