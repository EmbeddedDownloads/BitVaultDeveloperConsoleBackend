package org.bitvault.appstore.cloud.user.common.service;

import org.bitvault.appstore.cloud.model.AuthToken;

public interface AuthTokenService {
	public String getAuthToken(String authToken);

	public boolean isTokenExists(String authToken);

	public void deleteToken(String authToken);

	public void addAuthToken(String token, String email);
	
	public AuthToken getAuthTokenByEmail(String email);
}
