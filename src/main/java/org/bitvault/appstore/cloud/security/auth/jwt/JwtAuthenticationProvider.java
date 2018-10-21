package org.bitvault.appstore.cloud.security.auth.jwt;

import java.util.List;
import java.util.stream.Collectors;

import org.bitvault.appstore.cloud.constant.Constants;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.bitvault.appstore.cloud.security.auth.JwtAuthenticationToken;
import org.bitvault.appstore.cloud.security.auth.jwt.model.UserContext;
import org.bitvault.appstore.cloud.security.auth.jwt.model.token.RawAccessJwtToken;
import org.bitvault.appstore.cloud.security.auth.jwt.setting.JwtSettings;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

@Component
@SuppressWarnings("unchecked")
public class JwtAuthenticationProvider implements AuthenticationProvider {
    private final JwtSettings jwtSettings;
    
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(JwtAuthenticationProvider.class); 
    
    @Autowired
    public JwtAuthenticationProvider(JwtSettings jwtSettings) {
        this.jwtSettings = jwtSettings;
    }

    @Override
    public Authentication authenticate(Authentication authentication) {
        try {
			RawAccessJwtToken rawAccessToken = (RawAccessJwtToken) authentication.getCredentials();
			Jws<Claims> jwsClaims = rawAccessToken.parseClaims(jwtSettings.getTokenSigningKey());
			Claims claims = jwsClaims.getBody();
			String subject = claims.getSubject();
			List<String> scopes = claims.get("scopes", List.class);
			List<GrantedAuthority> authorities = scopes.stream()
			        .map(authority -> new SimpleGrantedAuthority(authority))
			        .collect(Collectors.toList());
			
			UserContext context = UserContext.create(subject, authorities, null, null);
			
			return new JwtAuthenticationToken(context, context.getAuthorities(), (String)claims.get("userId"), (String) claims.get("paymentStatus"));
		} catch (AuthenticationException e) {
			logger.error(e.getMessage());
			throw new BitVaultException(e.getMessage(), Constants.FAILED);
		}
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (JwtAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
