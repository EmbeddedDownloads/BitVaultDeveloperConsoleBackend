package org.bitvault.appstore.cloud.user.common.service;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

@Service
@Transactional
public class AuthTokenServiceImpl {}
//implements AuthTokenService {
//
//	@Autowired
//	AuthTokenRepository authTokenRepository;
//
//	@Autowired
//	JwtTokenUtil jwtTokenUtil;
//
//	@Override
//	public String getAuthToken(String authToken) {
//		AuthToken token = authTokenRepository.findOne(authToken);
//		if (token == null) {
//
//		}
//		return token.getAccessToken();
//	}
//
//	@Override
//	public boolean isTokenExists(String authToken) {
//		try {
//			if (authToken == null)
//				return false;
//			AuthToken token = authTokenRepository.findOne(authToken);
//			if (token == null) {
//				return false;
//			}
//			Date expiration = jwtTokenUtil.getExpirationDateFromToken(authToken);
//			if (expiration == null) {
//				//authTokenRepository.delete(authToken);
//				return false;
//			}
//			return true;
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return false;
//		}
//	}
//
//	@Override
//	public void deleteToken(String authToken) {
//		if (authToken != null)
//			authTokenRepository.delete(authToken);
//	}
//
//	@Override
//	public void addAuthToken(String token, String email) {
//		try {
//			AuthToken authToken = getAuthTokenByEmail(email);
//
//			if (authToken == null) {
//				authToken = new AuthToken();
//			}
//			else{
//				authTokenRepository.delete(authToken);
//				authTokenRepository.flush();
//			}
//			authToken.setAccessToken(token);
//			authToken.setEmail(email);
//			authTokenRepository.save(authToken);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	@Override
//	public AuthToken getAuthTokenByEmail(String email) {
//		return authTokenRepository.getAuthTokenByEmail(email);
//	}
//
//}
