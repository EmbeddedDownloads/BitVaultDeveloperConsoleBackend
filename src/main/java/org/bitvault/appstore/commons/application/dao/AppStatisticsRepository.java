package org.bitvault.appstore.commons.application.dao;

import org.bitvault.appstore.cloud.model.AppStatistic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AppStatisticsRepository extends JpaRepository<AppStatistic, Integer> {

	@Query("select appStats from AppStatistic appStats where appStats.application.appApplicationId = ?1")
	AppStatistic findAppStatisticsByAppId(Integer appId);

	@Query("select appStats from AppStatistic appStats where appStats.application.userId = ?1")
	Page<AppStatistic> findAllStatsByUserId(String userId, Pageable pageable);
	
	@Modifying
    @Query("delete  From AppStatistic appStats where appStats.application.appApplicationId=?1")
    void deleteAppStatByApplicationId(Integer applicationId);
}
