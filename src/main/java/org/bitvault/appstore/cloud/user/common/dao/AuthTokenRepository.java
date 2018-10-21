package org.bitvault.appstore.cloud.user.common.dao;

import org.bitvault.appstore.cloud.model.AuthToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthTokenRepository extends JpaRepository<AuthToken, String>{
	AuthToken getAuthTokenByEmail(String email);
}
