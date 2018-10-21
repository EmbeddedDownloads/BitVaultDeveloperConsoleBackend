package org.bitvault.appstore.commons.application.service;

import java.util.List;
import java.util.Map;

import org.bitvault.appstore.cloud.dto.AppApplication;
import org.bitvault.appstore.cloud.dto.AppRateReviewDto;
import org.bitvault.appstore.cloud.dto.AverageRatingChartDto;
import org.bitvault.appstore.cloud.dto.MobileUser;

public interface AppRateReviewService {

	AppRateReviewDto saveRateReview(AppRateReviewDto appRateReviewDto, AppApplication appApplicatioDto,
			MobileUser mobileUser);

	AppRateReviewDto findAppRateReviewByAppRateReviewId(Integer appRateReviewId);
	AppRateReviewDto findAppRateReviewByMobileUserId(Integer mobileUserId,Integer applicationId);

	float getAverageRating(Integer applicationId);

	Map<String, Long> findStarRatingByApplicationId(Integer applicationId);

	Map<String, Object> findAppRateReviewByAppId(Integer applicationId, Integer page, Integer size, String direction,
			String property);
	int findTotalRatingByAppId(Integer applicationId);
	int findReviewWithRatingByAppId(Integer applicationId);
	//float findAvgRatingByAppId(Integer applicationId);
	List<AverageRatingChartDto> getChartStatsbyAppIdAndYear(Integer appId, int year);
	List<AverageRatingChartDto> getChartStatsbyAppIdAndDates(Integer appId, String startDate, String endDate);
    void deleteReview(Integer appRateReviewId);
}
