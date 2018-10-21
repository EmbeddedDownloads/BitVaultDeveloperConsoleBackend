package org.bitvault.appstore.cloud.security.auth.endpoint;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bitvault.appstore.cloud.config.WebSecurityConfig;
import org.bitvault.appstore.cloud.constant.APIConstants;
import org.bitvault.appstore.cloud.constant.Constants;
import org.bitvault.appstore.cloud.constant.DbConstant;
import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.exception.InvalidJwtToken;
import org.bitvault.appstore.cloud.model.AdminUser;
import org.bitvault.appstore.cloud.model.DevUser;
import org.bitvault.appstore.cloud.model.Role;
import org.bitvault.appstore.cloud.security.auth.jwt.extractor.TokenExtractor;
import org.bitvault.appstore.cloud.security.auth.jwt.model.UserContext;
import org.bitvault.appstore.cloud.security.auth.jwt.model.token.JwtTokenFactory;
import org.bitvault.appstore.cloud.security.auth.jwt.model.token.RawAccessJwtToken;
import org.bitvault.appstore.cloud.security.auth.jwt.model.token.RefreshToken;
import org.bitvault.appstore.cloud.security.auth.jwt.setting.JwtSettings;
import org.bitvault.appstore.cloud.security.auth.jwt.verifier.TokenVerifier;
import org.bitvault.appstore.cloud.user.admin.service.AdminUserService;
import org.bitvault.appstore.cloud.user.common.GeneralResponseModel;
import org.bitvault.appstore.cloud.user.dev.service.DevUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.Claims;

/**
 * RefreshTokenEndpoint
 */
@RestController
@RequestMapping(APIConstants.AUTH_BASE)
public class RefreshTokenEndpoint {
    @Autowired private JwtTokenFactory tokenFactory;
    @Autowired private JwtSettings jwtSettings;
    @Autowired private DevUserService userService;
    @Autowired private AdminUserService adminUserService;
    @Autowired private TokenVerifier tokenVerifier;
    @Autowired @Qualifier("jwtHeaderTokenExtractor") private TokenExtractor tokenExtractor;
    
    @RequestMapping(value = APIConstants.GET_ACCESS_TOKEN, method=RequestMethod.GET, produces={ MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Map<String, Object> accessTokenMap = null;
    	try {
    		accessTokenMap = new HashMap<>();
			String tokenPayload = tokenExtractor.extract(request.getHeader(WebSecurityConfig.JWT_TOKEN_HEADER_PARAM));
			
			RawAccessJwtToken rawToken = new RawAccessJwtToken(tokenPayload);
			RefreshToken refreshToken = RefreshToken.create(rawToken, jwtSettings.getTokenSigningKey()).orElseThrow(() -> new InvalidJwtToken());

			String jti = refreshToken.getJti();
			if (!tokenVerifier.verify(jti)) {
			    throw new InvalidJwtToken();
			}			
			String subject = refreshToken.getSubject();
			Claims claims = refreshToken.getClaims().getBody();
			AdminUser adminUser = null;
			DevUser user = null;
			Role role = null;
			try{
				
					user = userService.findByEmailId(subject);
					if (user != null) {
						role = user.getRole();
						if(!user.getStatus().equals(DbConstant.ACTIVE)){
							accessTokenMap.put(Constants.MESSAGE, ErrorMessageConstant.ACCOUNT_EXPIRED_REJECTED);
							return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED, accessTokenMap));
						}
					}
				
					else{
			    	adminUser = adminUserService.findByEmail(subject);
			    	role = adminUser.getRole();
				}
			}
			catch(Exception e){
				e.printStackTrace();
				accessTokenMap.put(Constants.MESSAGE, ErrorMessageConstant.USER_NOT_FOUND);
				return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED, accessTokenMap));
			}
//        userService.findByUserId(userService.findByEmail(subject).getUserId());//.orElseThrow(() -> new UsernameNotFoundException("User not found: " + subject));
			// Role role = user.getRole();
			if (role == null)
				throw new InsufficientAuthenticationException("User has no roles assigned");
			List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
			authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
			// user.getRoles().stream()
			// .map(authority -> new
			// SimpleGrantedAuthority(authority.getRole().authority()))
			// .collect(Collectors.toList());

			UserContext userContext = null;
			
			if (user != null){
				userContext = UserContext.create(user.getEmail(), authorities, user.getAvatarURL(), user.getUsername());
				userContext.setUserId((String)claims.get(Constants.USERID));
				userContext.setPaymentStatus((String)claims.get("paymentStatus"));
			}
			else{
				userContext = UserContext.create(adminUser.getEmail(), authorities, adminUser.getAvatarURL(), adminUser.getFirstName());
				userContext.setUserId((String)claims.get(Constants.USERID));
				userContext.setPaymentStatus((String)claims.get("paymentStatus"));
			}
			accessTokenMap.put(Constants.ACCESS_TOKEN, tokenFactory.createAccessJwtToken(userContext).getToken());
			return ResponseEntity.ok(GeneralResponseModel.of(Constants.SUCCESS, accessTokenMap));
		} catch (InvalidJwtToken e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			accessTokenMap.put(Constants.MESSAGE, ErrorMessageConstant.INVALID_TOKEN);
			return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED, accessTokenMap));
		}
	}
}
