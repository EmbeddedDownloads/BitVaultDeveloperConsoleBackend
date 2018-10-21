package org.bitvault.appstore.cloud.user.dev.dao;

import org.bitvault.appstore.cloud.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer>{
	Account findByAccEmail(String email);
	
	
}
