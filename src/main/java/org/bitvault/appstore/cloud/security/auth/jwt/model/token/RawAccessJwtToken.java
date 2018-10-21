package org.bitvault.appstore.cloud.security.auth.jwt.model.token;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.bitvault.appstore.cloud.constant.APIConstants;
import org.bitvault.appstore.cloud.constant.Constants;
import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.bitvault.appstore.cloud.exception.JwtExpiredTokenException;
import org.bitvault.appstore.cloud.user.common.service.DevPaymentService;
import org.bitvault.appstore.cloud.utils.PaymentUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

public class RawAccessJwtToken implements JwtToken {
	private static Logger logger = LoggerFactory.getLogger(RawAccessJwtToken.class);

	private String token;

	public RawAccessJwtToken(String token) {
		this.token = token;
	}

	/**
	 * Parses and validates JWT Token signature.
	 * 
	 * @throws BadCredentialsException
	 * @throws JwtExpiredTokenException
	 * 
	 */
	public Jws<Claims> parseClaims(String signingKey) {
		try {
			Jws<Claims> claims = Jwts.parser().setSigningKey(signingKey).parseClaimsJws(this.token);
			return claims;
		} catch (UnsupportedJwtException | MalformedJwtException | IllegalArgumentException | SignatureException ex) {
			logger.error("Invalid JWT Token", ex);
			throw new BadCredentialsException(ErrorMessageConstant.INVALID_TOKEN, ex);
		} catch (ExpiredJwtException expiredEx) {
			ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
					.currentRequestAttributes();
			HttpServletRequest request = attributes.getRequest();
			if (request.getRequestURI().contains(APIConstants.UPDATE_DEV_PAYMENT)) {
				request.setAttribute("responseStatus", "OK");
				DevPaymentService devPaymentService = (DevPaymentService) request.getAttribute("service");
				String com = null;
				Map<String, Object> inputMap = null;
				try {
					com = IOUtils.toString(request.getInputStream(), "UTF-8");
					ObjectMapper mapper = new ObjectMapper();
					inputMap = mapper.readValue(com, new TypeReference<Map<String, Object>>() {
					});
				} catch (IOException exception) {
					throw new BitVaultException(ErrorMessageConstant.REQUIRED_FIELD_MISSING_1);
				}
				PaymentUtility.processPayment(devPaymentService, request, inputMap);
				String payStatus = (String) request.getAttribute(Constants.PAY_CHECK);
				if (payStatus.equals("done"))
					throw new JwtExpiredTokenException(this, Constants.PAYMENT_DONE, expiredEx);
				else if (payStatus != null)
					throw new JwtExpiredTokenException(this, Constants.PAYMENT_ALREADY_DONE, expiredEx);
			}
			logger.info("JWT Token is expired", expiredEx);
			throw new JwtExpiredTokenException(this, "JWT Token expired", expiredEx);
		}
	}

	@Override
	public String getToken() {
		return token;
	}
}
