package org.broadleafcommerce.cms.web.file;

import java.io.FileNotFoundException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.broadleafcommerce.cms.common.AssetNotFoundException;
import org.broadleafcommerce.common.sandbox.domain.SandBox;
import org.springframework.web.servlet.ModelAndView;

public class StaticAssetViewExController extends StaticAssetViewController {
	private static final Log LOG = LogFactory
			.getLog(StaticAssetViewExController.class);
	private static final String SANDBOX_ADMIN_ID_VAR = "blAdminCurrentSandboxId";
	private static final String SANDBOX_ID_VAR = "blSandboxId";

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String fullUrl = removeAssetPrefix(request.getRequestURI());

		try {
			Long sandBoxId = (Long) request.getSession().getAttribute(
					SANDBOX_ID_VAR);
			if (sandBoxId == null) {
				sandBoxId = (Long) request.getSession().getAttribute(
						SANDBOX_ADMIN_ID_VAR);
			}
			SandBox sandBox = null;
			if (sandBoxId != null) {
				sandBox = sandBoxDao.retrieve(sandBoxId);
			}

			try {
				Map<String, String> model = staticAssetStorageService
						.getCacheFileModel(fullUrl, sandBox,
								convertParameterMap(request.getParameterMap()));
				return new ModelAndView(getViewResolverName(), model);
			} catch (FileNotFoundException e) {
				LOG.error("Unable to retrieve static asset: " + e.getMessage());
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				return null;
			} catch (AssetNotFoundException e) {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				return null;
			}
		} catch (Exception e) {
			LOG.error("Unable to retrieve static asset", e);
			throw new RuntimeException(e);
		}
	}

	private String removeAssetPrefix(String requestURI) {
		String fileName = requestURI;
		if (getAssetServerUrlPrefix() != null) {
			int pos = fileName.indexOf(getAssetServerUrlPrefix());
			fileName = fileName.substring(pos
					+ getAssetServerUrlPrefix().length());

			if (!fileName.startsWith("/")) {
				fileName = "/" + fileName;
			}
		}

		return fileName;

	}

}
