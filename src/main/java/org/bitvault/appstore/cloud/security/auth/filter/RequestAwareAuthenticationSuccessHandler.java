package org.bitvault.appstore.cloud.security.auth.filter;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.bitvault.appstore.cloud.config.SAMLUserDetailsServiceImpl;
import org.bitvault.appstore.cloud.constant.Constants;
import org.bitvault.appstore.cloud.model.Token;
import org.bitvault.appstore.cloud.security.auth.jwt.model.UserContext;
import org.bitvault.appstore.cloud.security.auth.jwt.model.token.JwtToken;
import org.bitvault.appstore.cloud.security.auth.jwt.model.token.JwtTokenFactory;
import org.bitvault.appstore.cloud.security.service.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class RequestAwareAuthenticationSuccessHandler {
	private final ObjectMapper mapper;
	private final JwtTokenFactory tokenFactory;
	@Autowired
	TokenService tokenService;

	private static final Logger LOG = LoggerFactory.getLogger(SAMLUserDetailsServiceImpl.class);

	@Autowired
	public RequestAwareAuthenticationSuccessHandler(final ObjectMapper mapper, final JwtTokenFactory tokenFactory) {
		this.mapper = mapper;
		this.tokenFactory = tokenFactory;
	}

	public Token onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			UserContext userContext) throws IOException, ServletException {
		try {

			LOG.info("onAuthenticationSuccess , creating jwt token");
			// UserContext userContext = (UserContext) authentication.getPrincipal();
			JwtToken accessToken = tokenFactory.createAccessJwtToken(userContext);
			JwtToken refreshToken = tokenFactory.createRefreshToken(userContext);

			Token token = new Token();
			token.setAccessToken(accessToken.getToken());
			token.setRefreshToken(refreshToken.getToken());
			token.setAvatarURL(userContext.getAvatar());
			token.setAvatarName(userContext.getAvatarName());
			token.setLoginType(userContext.getLoginType());

			// Map<String, Object> tokenMap = new HashMap<String, Object>();
			// tokenMap.put(Constants.ACCESS_TOKEN, accessToken.getToken());
			// tokenMap.put(Constants.REFRESH_TOKEN, refreshToken.getToken());
			// tokenMap.put("avatarURL", userContext.getAvatar());
			// tokenMap.put("avatarName", userContext.getAvatarName());
			// tokenMap.put("loginType", userContext.getLoginType());
			String paymentStatus = userContext.getPaymentStatus();
			if (!request.getAttribute("ownBy").toString().equalsIgnoreCase("child") && paymentStatus != null
					&& !paymentStatus.equalsIgnoreCase(Constants.SUCCESS)) {
				token.setPaybleAmount(userContext.getPaybleAmount());
				token.setPaymentStatus(paymentStatus);
				token.setUserId(userContext.getUserId());

				// tokenMap.put("paymentStatus", paymentStatus);
				// tokenMap.put("paybleAmount", userContext.getPaybleAmount());
				// tokenMap.put("userId", userContext.getUserId());
			}
			// response.setStatus(HttpStatus.OK.value());
			// response.setContentType(MediaType.APPLICATION_JSON_VALUE);

			token = tokenService.saveToken(token);
			clearAuthenticationAttributes(request);
			return token;

		} catch (Exception e) {
			LOG.error("onAuthenticationSuccess exception:::" + e.getMessage() + "/n" + e);

			e.printStackTrace();
			return null;
		}

	}

	/**
	 * Removes temporary authentication-related data which may have been stored in
	 * the session during the authentication process..
	 * 
	 */
	protected final void clearAuthenticationAttributes(HttpServletRequest request) {
		HttpSession session = request.getSession(false);

		if (session == null) {
			return;
		}

		session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
	}
}
