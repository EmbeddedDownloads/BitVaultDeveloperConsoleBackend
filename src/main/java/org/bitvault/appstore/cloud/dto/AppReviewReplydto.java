package org.bitvault.appstore.cloud.dto;

import java.util.Date;

import org.bitvault.appstore.cloud.model.AppReviewReply;
import org.springframework.beans.BeanUtils;

public class AppReviewReplydto {
	private int appReviewReplyId;

	private Integer appRateReviewId;

	private String replyResponse;

	private String replyFrom;
	private Date createdAt;
	private String updatedBy;
	private String createdBy;

	public Date getCreatedAt() {
		return createdAt;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
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

	private Date updatedAt;

	public Integer getAppReviewReplyId() {
		return appReviewReplyId;
	}

	public void setAppReviewReplyId(Integer appReviewReplyId) {
		this.appReviewReplyId = appReviewReplyId;
	}

	public Integer getAppRateReviewId() {
		return appRateReviewId;
	}

	public void setAppRateReviewId(Integer appRateReviewId) {
		this.appRateReviewId = appRateReviewId;
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

	public AppReviewReply populateAppReviewReply(AppReviewReplydto appReviewReplydto) {
		AppReviewReply appReview = new AppReviewReply();
		try {

			BeanUtils.copyProperties(appReviewReplydto, appReview);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return appReview;
	}

}
