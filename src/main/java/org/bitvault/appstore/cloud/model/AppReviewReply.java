package org.bitvault.appstore.cloud.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.bitvault.appstore.cloud.dto.AppReviewReplydto;
import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
 * The persistent class for the app_review_reply database table.
 * 
 */
@Entity

@Table(name = "app_review_reply")
@NamedQuery(name = "AppReviewReply.findAll", query = "SELECT a FROM AppReviewReply a")
public class AppReviewReply extends Auditable<String>implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "app_review_reply_id", unique = true, nullable = false)
	private int appReviewReplyId;

//	//@Temporal(TemporalType.TIMESTAMP)
//	@CreatedDate
//	@Column(name = "created_at", nullable = false)
//	private Date createdAt;
//
//	@Column(name = "created_by", nullable = false, length = 255)
//	private String createdBy;

	@Column(name = "reply_from", nullable = false)
	private String replyFrom;

	@Column(name = "reply_response", nullable = false, length = 255)
	private String replyResponse;

	
//
//	//@Temporal(TemporalType.TIMESTAMP)
//	@LastModifiedDate
//	@Column(name = "updated_at", nullable = false)
//	private Date updatedAt;
//
//	@Column(name = "updated_by", nullable = false, length = 255)
//	private String updatedBy;

	// bi-directional many-to-one association to AppRateReview
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "app_rate_review_id", nullable = false)
	@JsonIgnore
	@JsonManagedReference("AppRateReview-AppReviewReply")
	private AppRateReview appRateReview;

	public AppReviewReply() {
	}

	public int getAppReviewReplyId() {
		return this.appReviewReplyId;
	}

	public void setAppReviewReplyId(int appReviewReplyId) {
		this.appReviewReplyId = appReviewReplyId;
	}

//	public Date getCreatedAt() {
//		return this.createdAt;
//	}
//
//	public void setCreatedAt(Date createdAt) {
//		this.createdAt = createdAt;
//	}

//	public String getCreatedBy() {
//		return this.createdBy;
//	}
//
//	public void setCreatedBy(String createdBy) {
//		this.createdBy = createdBy;
//	}

	public String getReplyFrom() {
		return this.replyFrom;
	}

	public void setReplyFrom(String replyFrom) {
		this.replyFrom = replyFrom;
	}

	public String getReplyResponse() {
		return this.replyResponse;
	}

	public void setReplyResponse(String replyResponse) {
		this.replyResponse = replyResponse;
	}

	

//	public Date getUpdatedAt() {
//		return this.updatedAt;
//	}
//
//	public void setUpdatedAt(Date updatedAt) {
//		this.updatedAt = updatedAt;
//	}
//
//	public String getUpdatedBy() {
//		return this.updatedBy;
//	}
//
//	public void setUpdatedBy(String updatedBy) {
//		this.updatedBy = updatedBy;
//	}

	public AppRateReview getAppRateReview() {
		return this.appRateReview;
	}

	public void setAppRateReview(AppRateReview appRateReview) {
		this.appRateReview = appRateReview;
	}
	public AppReviewReplydto populateAppReviewReply(AppReviewReply appReviewReply) {
		AppReviewReplydto appReviewReplyDto = new AppReviewReplydto();
		try {
			
			BeanUtils.copyProperties(appReviewReply, appReviewReplyDto);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return appReviewReplyDto;
}
}