package org.bitvault.appstore.cloud.security.service;

import org.bitvault.appstore.cloud.model.Token;

public interface TokenService {

	Token saveToken(Token token);

	void deleteToken(String tokenId);

	Token findTokenByTokenId(String tokenId);

}
