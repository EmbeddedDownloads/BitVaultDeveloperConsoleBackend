package org.bitvault.appstore.cloud.dto;

import java.util.Date;

public class AppRewRepDetailDto {
	private Integer appRating;

	private String appReview;

	private Integer mobileUserId;

	private Integer applicationId;
	private Integer appRateReviewId;

	private Date createdAt;

	private Date updatedAt;

	private String replyResponse;
	private String replyFrom;
	private Date replyUpdatedAt;

	public Integer getAppRating() {
		return appRating;
	}

	public Integer getAppRateReviewId() {
		return appRateReviewId;
	}

	public void setAppRateReviewId(Integer appRateReviewId) {
		this.appRateReviewId = appRateReviewId;
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

	public String getReplyResponse() {
		return replyResponse;
	}

	public void setReplyResponse(String replyResponse) {
		this.replyResponse = replyResponse;
	}

	public String getReplyFrom() {
		return replyFrom;
	}

	public void setReplyFrom(String replyFrom) {
		this.replyFrom = replyFrom;
	}

	

	public Date getReplyUpdatedAt() {
		return replyUpdatedAt;
	}

	public void setReplyUpdatedAt(Date replyUpdatedAt) {
		this.replyUpdatedAt = replyUpdatedAt;
	}

	public void setAppRating(Integer appRating) {
		this.appRating = appRating;
	}

	public String getAppReview() {
		return appReview;
	}

	public void setAppReview(String appReview) {
		this.appReview = appReview;
	}

	public Integer getMobileUserId() {
		return mobileUserId;
	}

	public void setMobileUserId(Integer mobileUserId) {
		this.mobileUserId = mobileUserId;
	}

	public Integer getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(Integer applicationId) {
		this.applicationId = applicationId;
	}

	

}
