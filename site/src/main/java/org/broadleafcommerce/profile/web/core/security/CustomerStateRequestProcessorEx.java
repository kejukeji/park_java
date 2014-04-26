package org.broadleafcommerce.profile.web.core.security;

import org.broadleafcommerce.profile.core.domain.Customer;
import org.springframework.web.context.request.WebRequest;

/**
 * 支持直接通过authToken找到对应customer，标为anonymous; 检查customer的region，必须有值
 * 
 * @author Ju
 * 
 */
public class CustomerStateRequestProcessorEx extends
		CustomerStateRequestProcessor {

	private String customerIdAttributeName = "authToken";

	@Override
	public Customer resolveAnonymousCustomer(WebRequest request) {
		Customer c = super.resolveAnonymousCustomer(request);

		String customerKey;
		if (c == null || c.isAnonymous()) {
			// First check to see if someone already put the customerId on the
			// request
			customerKey = String.valueOf(request.getAttribute(
					customerIdAttributeName, WebRequest.SCOPE_REQUEST));

			if (customerKey == null) {
				// If it's not on the request attribute, try the parameter
				customerKey = request.getParameter(customerIdAttributeName);
			}

			if (customerKey == null) {
				// If it's not on the request parameter, look on the header
				customerKey = request.getHeader(customerIdAttributeName);
			}

			// customerKey若为真的email，则不走此逻辑，否则漏洞
			if (customerKey != null && customerKey.trim().length() > 0
					&& customerKey.indexOf('@') < 0) {

				// If we found it, look up the customer and put it on the
				// request.
				if (!customerKey.equals(c.getUsername())) {
					Customer cc = customerService
							.readCustomerByEmail(customerKey);
					if (cc != null) {
						c = cc;
						c.setAnonymous(true);
						request.setAttribute(
								getAnonymousCustomerAttributeName(), c,
								WebRequest.SCOPE_GLOBAL_SESSION);
					}
				}
			}
		}

		return c;
	}

}
