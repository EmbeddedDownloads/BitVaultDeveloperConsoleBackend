package org.bitvault.appstore.cloud.user.common.dao;

import org.bitvault.appstore.cloud.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, String> {

}
