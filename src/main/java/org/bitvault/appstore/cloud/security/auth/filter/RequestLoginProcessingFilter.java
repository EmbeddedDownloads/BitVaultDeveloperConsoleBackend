package org.bitvault.appstore.cloud.security.auth.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.bitvault.appstore.cloud.constant.Constants;
import org.bitvault.appstore.cloud.exception.AuthMethodNotSupportedException;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.bitvault.appstore.cloud.exception.BitVaultResponse;
import org.bitvault.appstore.cloud.user.common.GeneralResponseModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

public class RequestLoginProcessingFilter extends AbstractAuthenticationProcessingFilter {
    private static Logger logger = LoggerFactory.getLogger(RequestLoginProcessingFilter.class);

    private final AuthenticationSuccessHandler successHandler;
    private final AuthenticationFailureHandler failureHandler;

    private final ObjectMapper objectMapper;
    
    public RequestLoginProcessingFilter(String defaultProcessUrl, AuthenticationSuccessHandler successHandler, 
            AuthenticationFailureHandler failureHandler, ObjectMapper mapper) {
        super(defaultProcessUrl);
        this.successHandler = successHandler;
        this.failureHandler = failureHandler;
        this.objectMapper = mapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    	try {
			// || !WebUtil.isAjax(request)
			logger.info("User has made by: "+ request.getRemoteAddr() + ":" + request.getRemotePort() + " for api: "+ request.getRequestURL());
			if (!HttpMethod.POST.name().equals(request.getMethod())) {
			    if(logger.isDebugEnabled()) {
			        logger.debug("Authentication method not supported. Request method: " + request.getMethod());
			    }
			    throw new AuthMethodNotSupportedException("Authentication method "+ request.getMethod() +" not supported");
			}

			LoginRequest loginRequest = objectMapper.readValue(request.getReader(), LoginRequest.class);
			
			if (StringUtils.isBlank(loginRequest.getUsername()) || StringUtils.isBlank(loginRequest.getPassword())) {
			    throw new AuthenticationServiceException("Username or Password not provided");
			}

			UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
			token.setDetails(loginRequest.getLoginType());
			request.setAttribute("ownBy", loginRequest.getLoginType());
			return this.getAuthenticationManager().authenticate(token);
		} catch (AuthenticationException | BitVaultException e) {
			ObjectMapper mapper = new ObjectMapper();
			mapper.writeValue(response.getWriter(), GeneralResponseModel.of(Constants.FAILED, new BitVaultResponse(e.getMessage())));
            return null;
		}
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {
        successHandler.onAuthenticationSuccess(request, response, authResult);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException failed) throws IOException, ServletException {
        SecurityContextHolder.clearContext();
        failureHandler.onAuthenticationFailure(request, response, failed);
    }
}
