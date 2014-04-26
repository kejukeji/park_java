package org.broadleafcommerce.common.web;


import java.util.Locale;

import org.springframework.web.servlet.View;

public class BroadleafThymeleafViewResolverEx extends
		BroadleafThymeleafViewResolver {

	private static final String SURFIX_NONAJAX = ":NONAJAX";

	@Override
	public View resolveViewName(String viewName, Locale locale)
			throws Exception {
		if (!isAjaxRequest()) {
			viewName += SURFIX_NONAJAX;
		}
		return super.resolveViewName(viewName, locale);
	}

	@Override
	protected View createView(String viewName, Locale locale) throws Exception {
		return super.createView(viewName.replace(SURFIX_NONAJAX, ""), locale);
	}

}
