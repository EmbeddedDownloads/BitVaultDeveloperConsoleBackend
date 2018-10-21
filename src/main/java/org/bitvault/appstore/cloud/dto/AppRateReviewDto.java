package org.bitvault.appstore.cloud.dto;

import java.util.Date;

import org.bitvault.appstore.cloud.model.AppRateReview;
import org.springframework.beans.BeanUtils;

public class AppRateReviewDto {

	private Integer appRating;

	private String appReview;

	private Integer mobileUserId;

	private Integer applicationId;

	private String replyResponse;
    
   
	public String getReplyResponse() {
		return replyResponse;
	}

	public void setReplyResponse(String replyResponse) {
		this.replyResponse = replyResponse;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	private Integer appRateReviewId;

	private Date createdAt;

	private Date updatedAt;

	public Integer getAppRating() {
		return appRating;
	}

	public void setAppRating(Integer appRating) {
		this.appRating = appRating;
	}

	public Integer getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(Integer applicationId) {
		this.applicationId = applicationId;
	}

	public String getAppReview() {
		return appReview;
	}

	public void setAppReview(String appReview) {
		this.appReview = appReview;
	}

	public Integer getAppRateReviewId() {
		return appRateReviewId;
	}

	public void setAppRateReviewId(Integer appRateReviewId) {
		this.appRateReviewId = appRateReviewId;
	}

	public AppRateReview populateAppRateReview(AppRateReviewDto appRateReviewDto,
			org.bitvault.appstore.cloud.model.AppApplication appApplicationDto) {
		AppRateReview appRateReview = null;
		try {
			appRateReview = new AppRateReview();
			// Application application = new Application();

			/*
			 * if(appRateReviewDto.appRateReviewId == null) {
			 * appRateReviewDto.appRateReviewId = 0 ; }
			 */

			BeanUtils.copyProperties(appRateReviewDto, appRateReview);
			appRateReview.setAppApplication(appApplicationDto);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return appRateReview;

	}

	public AppRateReview populateAppRateReview(AppRateReviewDto appRateReviewDto) {
		AppRateReview appRateReview = null;
		try {
			appRateReview = new AppRateReview();
			BeanUtils.copyProperties(appRateReviewDto, appRateReview);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return appRateReview;

	}

	public Integer getMobileUserId() {
		return mobileUserId;
	}

	public void setMobileUserId(Integer mobileUserId) {
		this.mobileUserId = mobileUserId;
	}

}
