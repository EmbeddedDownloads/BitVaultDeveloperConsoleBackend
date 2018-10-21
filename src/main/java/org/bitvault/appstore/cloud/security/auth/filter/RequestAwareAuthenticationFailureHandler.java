package org.bitvault.appstore.cloud.security.auth.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bitvault.appstore.cloud.constant.Constants;
import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.exception.AuthMethodNotSupportedException;
import org.bitvault.appstore.cloud.exception.InvalidTokenException;
import org.bitvault.appstore.cloud.exception.JwtExpiredTokenException;
import org.bitvault.appstore.cloud.user.common.GeneralResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;


@Component
public class RequestAwareAuthenticationFailureHandler implements AuthenticationFailureHandler {
    private final ObjectMapper mapper;
    
    @Autowired
    public RequestAwareAuthenticationFailureHandler(ObjectMapper mapper) {
        this.mapper = mapper;
    }	
    
	/* 
	 * This method will invoke if authentication failed
	 * @see org.springframework.security.web.authentication.AuthenticationFailureHandler#onAuthenticationFailure(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.security.core.AuthenticationException)
	 */
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException e) throws IOException, ServletException {
		
		response.setStatus(HttpServletResponse.SC_OK);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		Map<String, String> errorMap = new HashMap<String, String>();
		
		if (e instanceof BadCredentialsException)
			errorMap.put(Constants.MESSAGE, ErrorMessageConstant.BAD_CREDENTIALS);
		else if (e instanceof InvalidTokenException)
			errorMap.put(Constants.MESSAGE, ErrorMessageConstant.REFRESH_TOKEN_MISUSE);
		else if (e instanceof JwtExpiredTokenException)
			errorMap.put(Constants.MESSAGE, ErrorMessageConstant.TOKEN_EXPIRED);
		else if (e instanceof AuthMethodNotSupportedException)
			errorMap.put(Constants.MESSAGE, e.getMessage());
		else if (e instanceof AuthenticationServiceException)
			errorMap.put(Constants.MESSAGE, e.getMessage());
		
		mapper.writeValue(response.getWriter(), GeneralResponseModel.of(Constants.FAILED, errorMap));
	}
}
