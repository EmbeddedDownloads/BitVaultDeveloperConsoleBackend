package org.bitvault.appstore.cloud.user.dev.dao;

import org.bitvault.appstore.cloud.model.UserActivity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserActivityRepository extends JpaRepository<UserActivity, Integer>{

	@Query("Select userActivity From UserActivity userActivity where userActivity.userId=?1 ")
	Page<UserActivity> findUserActivityByUserId(String userId, Pageable page);
	
}
