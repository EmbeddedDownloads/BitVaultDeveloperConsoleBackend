package org.bitvault.appstore.cloud.user.admin.dao;

import org.bitvault.appstore.cloud.model.AdminUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("adminUserRepository")
public interface AdminUserRepository extends JpaRepository<AdminUser, String>{
	AdminUser findByEmail(String email);
	AdminUser findByUserId(String userId);
}
