package org.bitvault.appstore.cloud.user.common.dao;

import org.bitvault.appstore.cloud.model.ForgetPassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ForgetPasswordRepository extends JpaRepository<ForgetPassword, Integer>{
	ForgetPassword findByUserId(String email);
	ForgetPassword findByHashcode(String id);
}
