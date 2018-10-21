package org.bitvault.appstore.cloud.user.dev.dao;

import java.util.List;

import org.bitvault.appstore.cloud.model.DevUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * This interface provide the methods used for users purpose i.e. sign-up, login
 * etc.
 */
@Repository("devUserRepository")

public interface DevUserRepository extends JpaRepository<DevUser, String> {
	DevUser findByEmail(String email);

	DevUser findByUserId(String userId);
	
	@Query("from DevUser d where d.status in (:status)")
	Page<DevUser> findAll(Pageable page, @Param("status") List<String> status);
	
	Page<DevUser> findByCreatedByAndUserIdNotIn(String createdBy, String userId, Pageable page);
	
	@Query("select count(d.userId) from DevUser d where d.status in (:status)")
	int getUserCount(@Param("status") List<String> userStatus);
   
	@Modifying
    @Query("delete from DevUser devUser where devUser.createdBy=?1")
	void deleteSubDeveloperByUserId(String userId);
	
	@Query("Select devUser From DevUser devUser where (devUser.username like %?1% or devUser.email like %?1%) and devUser.parentId=?2")
	Page<DevUser> findByUserNameOrEmailId(String username, String userId,Pageable page);
}
