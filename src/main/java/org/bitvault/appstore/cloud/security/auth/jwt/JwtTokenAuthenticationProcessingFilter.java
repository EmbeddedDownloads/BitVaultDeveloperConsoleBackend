package org.bitvault.appstore.cloud.security.auth.jwt;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.bitvault.appstore.cloud.config.WebSecurityConfig;
import org.bitvault.appstore.cloud.constant.APIConstants;
import org.bitvault.appstore.cloud.constant.Constants;
import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.bitvault.appstore.cloud.exception.InvalidTokenException;
import org.bitvault.appstore.cloud.security.auth.JwtAuthenticationToken;
import org.bitvault.appstore.cloud.security.auth.jwt.extractor.TokenExtractor;
import org.bitvault.appstore.cloud.security.auth.jwt.model.UserContext;
import org.bitvault.appstore.cloud.security.auth.jwt.model.token.RawAccessJwtToken;
import org.bitvault.appstore.cloud.user.common.GeneralResponseModel;
import org.bitvault.appstore.cloud.user.common.service.DevPaymentService;
import org.bitvault.appstore.cloud.utils.PaymentUtility;
import org.bitvault.appstore.cloud.utils.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JwtTokenAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {
	private final AuthenticationFailureHandler failureHandler;
	private final TokenExtractor tokenExtractor;
	private final DevPaymentService devPaymentService;

	@Autowired
	public JwtTokenAuthenticationProcessingFilter(AuthenticationFailureHandler failureHandler,
			TokenExtractor tokenExtractor, RequestMatcher matcher, DevPaymentService service) {
		super(matcher);
		this.failureHandler = failureHandler;
		this.tokenExtractor = tokenExtractor;
		devPaymentService = service;
	}

	/*
	 * Basic purpose to override doFilter is to control the response code i.e.
	 * 200 when JWT token expired or invalid
	 * 
	 * @see org.springframework.security.web.authentication.
	 * AbstractAuthenticationProcessingFilter#doFilter(javax.servlet.
	 * ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletResponse response = (HttpServletResponse) res;
		HttpServletRequest request = (HttpServletRequest) req;
		try {
			response.setStatus(HttpServletResponse.SC_OK);
			super.doFilter(req, res, chain);
		} catch (Exception e) {
			logger.error(e.getMessage());
//			if(request.getAttribute("responseStatus") != null)
//				response.setStatus(HttpServletResponse.SC_OK);
//			else
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			ObjectMapper mapper = new ObjectMapper();
			Map<String, String> errorMap = new HashMap<>(); // uncomment this
															// and below
															// statement, if the
															// response JSON
															// message need to
															// be populated
															// under message
															// JSON object
			errorMap.put(Constants.MESSAGE, e.getMessage());
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			if(request.getAttribute(Constants.PAY_CHECK) != null)	
				mapper.writeValue(response.getWriter(), GeneralResponseModel.of(Constants.SUCCESS, errorMap));
			mapper.writeValue(response.getWriter(), GeneralResponseModel.of(Constants.FAILED, errorMap));
		}
	}

	private static final Logger logger = LoggerFactory.getLogger(JwtTokenAuthenticationProcessingFilter.class);

	private JwtAuthenticationToken jwtAuthenticationToken;

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		Authentication auth = null;

		try {
			String tokenPayload = request.getHeader(WebSecurityConfig.JWT_TOKEN_HEADER_PARAM);
			RawAccessJwtToken token = new RawAccessJwtToken(tokenExtractor.extract(tokenPayload));
			request.setAttribute("service", devPaymentService);
			jwtAuthenticationToken = new JwtAuthenticationToken(token);
			auth = getAuthenticationManager().authenticate(jwtAuthenticationToken);
			jwtAuthenticationToken = (JwtAuthenticationToken) auth;
			String roles = auth.getAuthorities().toString();
			logger.info(
					"checking the authorities; in case of refresh token restrict its use to get access token only...");
			if (roles.contains("ROLE_REFRESH_TOKEN")
					&& !request.getRequestURI().toString().contains(APIConstants.GET_ACCESS_TOKEN)) {
				throw new InvalidTokenException(ErrorMessageConstant.REFRESH_TOKEN_MISUSE);
			}

			if (auth != null) {
				request.setAttribute("email", ((UserContext) auth.getPrincipal()).getUsername());
				request.setAttribute("userId", jwtAuthenticationToken.getUserId());
				String role = roles;
				role = role.substring(role.indexOf("_") + 1, role.lastIndexOf("]"));
				request.setAttribute("role", role);
			}

			// This need to be implemented for payment check
			String paymentStatus = jwtAuthenticationToken.getPaymentStatus();
			if (!Constants.SUCCESS.equalsIgnoreCase(paymentStatus)) {
				request.setAttribute("responseStatus", "OK");
				if (request.getRequestURI().contains(APIConstants.UPDATE_DEV_PAYMENT)) {
					String requestPayload = IOUtils.toString(request.getInputStream(), "UTF-8");
					if (Utility.isStringEmpty(requestPayload)) {
						throw new BitVaultException(ErrorMessageConstant.REQUIRED_FIELD_MISSING_1);
					}
					ObjectMapper mapper = new ObjectMapper();
					Map<String, Object> inputMap = mapper.readValue(requestPayload,
							new TypeReference<Map<String, Object>>() {
							});
					PaymentUtility.processPayment(devPaymentService, request, inputMap);
				} else
					throw new BitVaultException(ErrorMessageConstant.PAYMENT_NOT_DONE);
			}

			return auth;
		} catch (Exception e) {
			logger.error(e.getMessage());
			response.setStatus(HttpServletResponse.SC_OK);
			throw new BitVaultException(e.getMessage());
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		SecurityContext context = SecurityContextHolder.createEmptyContext();
		context.setAuthentication(authResult);
		SecurityContextHolder.setContext(context);
		chain.doFilter(request, response);
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		SecurityContextHolder.clearContext();
		failureHandler.onAuthenticationFailure(request, response, failed);
	}
}
