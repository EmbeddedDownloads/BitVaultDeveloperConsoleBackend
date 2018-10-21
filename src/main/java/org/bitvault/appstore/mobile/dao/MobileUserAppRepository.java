package org.bitvault.appstore.mobile.dao;

import java.util.Date;
import java.util.List;

import org.bitvault.appstore.cloud.dto.ChartStatsDto;
import org.bitvault.appstore.cloud.model.MobileUserApp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MobileUserAppRepository extends JpaRepository<MobileUserApp, Integer> {

	@Query("select new org.bitvault.appstore.cloud.dto.ChartStatsDto(count(mobileUserApp), monthname(mobileUserApp.createdAt),  date(mobileUserApp.createdAt)) "
			+ " from MobileUserApp mobileUserApp where mobileUserApp.application.appApplicationId = ?1"
			+ " and year(mobileUserApp.createdAt) = ?2 and mobileUserApp.status = ?3 group by month(mobileUserApp.createdAt)")
	List<ChartStatsDto> getChartStatsbyAppIdAndYear(Integer appId, int year, String status);

	@Query("select new org.bitvault.appstore.cloud.dto.ChartStatsDto(count(mobileUserApp), mobileUserApp.status,  date(mobileUserApp.createdAt))"
			+ " from MobileUserApp mobileUserApp where mobileUserApp.application.appApplicationId = ?1"
			+ " and date(mobileUserApp.createdAt) between ?2 and ?3 and mobileUserApp.status = ?4 group by date(mobileUserApp.createdAt)")
	List<ChartStatsDto> getChartStatsbyAppIdAndDates(Integer appId, Date startDate, Date endDate, String status);

	
	@Modifying
    @Query("delete  From MobileUserApp mobileUserApp where mobileUserApp.application.appApplicationId=?1")
    void deletemobileUserAppByApplicationId(Integer applicationId);
}
