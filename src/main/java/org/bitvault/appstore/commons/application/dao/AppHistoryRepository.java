package org.bitvault.appstore.commons.application.dao;

import org.bitvault.appstore.cloud.model.AppHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AppHistoryRepository extends JpaRepository<AppHistory, Integer> {
	@Modifying
	@Query("delete  From AppHistory app where app.appApplication.appApplicationId=?1")
	void deletAppHistoryByApplicationId(Integer applicationId);
	
	Page<AppHistory> findAppHistoryByAppApplicationAppApplicationId(Integer applicationId, Pageable pageable);

}
