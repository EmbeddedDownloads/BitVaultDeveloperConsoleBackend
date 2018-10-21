package org.bitvault.appstore.commons.application.dao;

import java.util.Date;
import java.util.List;

import org.bitvault.appstore.cloud.dto.AverageRatingChartDto;
import org.bitvault.appstore.cloud.model.AppRateReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository("AppRateReview")
public interface AppRateReviewRepository extends JpaRepository<AppRateReview, Integer> {

	@Query("select AVG(appRateReview.appRating) from AppRateReview appRateReview where appRateReview.appApplication.appApplicationId = ?1")
	Float findAverageByRating(Integer applicationId);

	@Query("select appRateReview from AppRateReview appRateReview where appRateReview.appRateReviewId = ?1")
	AppRateReview findAppRateReviewByAppRateReviewId(Integer appRateReviewId);

	@Query("select appRateReview from AppRateReview appRateReview where appRateReview.mobileUser.mobileUserId = ?1 and appRateReview.appApplication.appApplicationId = ?2")
	AppRateReview findAppRateReviewByMobileUserId(Integer mobileUserId, Integer applicationId);

	// Select COUNT(app_rating) as count , a.app_rating from app_rate_review a
	// where appplication_id = 36 GROUP BY app_rating ;

	@Query("select COUNT(appRateReview.appRating) as count, appRateReview.appRating as rating from AppRateReview appRateReview where appRateReview.appApplication.appApplicationId = ?1 GROUP BY appRateReview.appRating")
	List<Object[]> findStarRatingByApplicationId(Integer applicationId);

	@Query("select appRateReview from AppRateReview appRateReview where appRateReview.appApplication.appApplicationId = ?1")
	Page<AppRateReview> findAppRateReviewByAppId(Integer applicationId, Pageable pageable);

	@Query("select count(*)From AppRateReview appRateReview where appRateReview.appRating IS NOT NULL  AND appRateReview.appApplication.appApplicationId=?1")
	int findCountByApplicationId(Integer applicationId);

	@Query("select count(*) From AppRateReview app where app.appApplication.appApplicationId=?1  AND app.appReview  IS NOT NULL AND app.appReview <> '' AND app.appRating IS NOT NULL AND app.appRating <> '' ")
	int findReviewRatingCountByApplicationId(Integer applicationId);

	@Query("select new org.bitvault.appstore.cloud.dto.AverageRatingChartDto(AVG(appRateView.appRating), monthname(appRateView.createdAt), date(appRateView.createdAt)) "
			+ " from AppRateReview appRateView where appRateView.appApplication.appApplicationId = ?1"
			+ " and year(appRateView.createdAt) = ?2 group by month(appRateView.createdAt)")
	List<AverageRatingChartDto> getChartStatsbyAppIdAndYear(Integer appId, int year);

	@Query("select new org.bitvault.appstore.cloud.dto.AverageRatingChartDto(AVG(appRateView.appRating),appRateView.createdBy ,date(appRateView.createdAt))"
			+ " from AppRateReview appRateView  where appRateView.appApplication.appApplicationId = ?1"
			+ " and date(appRateView.createdAt) between ?2 and ?3  group by date(appRateView.createdAt)")
	List<AverageRatingChartDto> getChartStatsbyAppIdAndDates(Integer appId, Date startDate, Date endDate);

	@Modifying
	@Query("delete  From AppRateReview app where app.appApplication.appApplicationId=?1")
	void deleteRateReviewByApplicationId(Integer applicationId);
}
