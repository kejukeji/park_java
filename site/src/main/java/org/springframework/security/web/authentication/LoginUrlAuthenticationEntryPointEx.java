package org.springframework.security.web.authentication;

@SuppressWarnings("deprecation")
public class LoginUrlAuthenticationEntryPointEx extends
		LoginUrlAuthenticationEntryPoint {

	@Override
	protected boolean isUseForward() {
		return true; // app use forward
	}

}
