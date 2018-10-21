package org.bitvault.appstore.cloud.security.service;

import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.bitvault.appstore.cloud.model.Token;
import org.bitvault.appstore.cloud.user.common.dao.TokenRepository;
import org.bitvault.appstore.cloud.utils.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements TokenService {

	public static final Logger logger = LoggerFactory.getLogger(TokenServiceImpl.class);

	@Autowired
	TokenRepository tokenRepository;

	@Override
	public Token saveToken(Token token) {
		try {
			logger.info("saveToken :::: saving token ");
			token.setTokenId(Utility.getUuid());
			token = tokenRepository.saveAndFlush(token);
			logger.info("saveToken :::: " + token);

			return token;
		} catch (Exception e) {
			logger.error("saveToken :::: exception in saving token " + e.getMessage());

			throw new BitVaultException(ErrorMessageConstant.UNABLE_TO_SAVE);
		}
	}

	@Override
	public void deleteToken(String tokenId) {
		try {
			logger.info("deleteToken :::: " + tokenId);

			tokenRepository.delete(tokenId);
		} catch (Exception e) {
			logger.error("deleteToken :::: exception::: " + e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public Token findTokenByTokenId(String tokenId) {
		try {
			logger.error("findTokenByTokenId ::: " + tokenId);

			return tokenRepository.findOne(tokenId);
		} catch (Exception e) {
			logger.error("findTokenByTokenId :::: exception::: " + e.getMessage());

			return null;
		}
	}

}
