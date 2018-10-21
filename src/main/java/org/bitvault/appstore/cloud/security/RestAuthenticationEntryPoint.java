package org.bitvault.appstore.cloud.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component("restAuthenticationEntryPoint")
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

		@Override
		public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException ex)
				throws IOException, ServletException {
			response.sendError(HttpStatus.UNAUTHORIZED.value(), "Unauthorized");
		}

//	@Override
//	public void commence(final HttpServletRequest request, final HttpServletResponse response,
//			final AuthenticationException authException) throws IOException, ServletException {
//		// Authentication failed, send error response.
//		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//		response.addHeader("WWW-Authenticate", "Basic realm=" + getRealmName() + "");
//
//		PrintWriter writer = response.getWriter();
//		writer.println("HTTP Status 401 : " + authException.getMessage());
//	}

//	@Override
//	public void afterPropertiesSet() throws Exception {
//		setRealmName("MY_TEST_REALM");
//		super.afterPropertiesSet();
//	}
}