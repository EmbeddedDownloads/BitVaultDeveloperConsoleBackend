package org.bitvault.appstore.cloud.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.bitvault.appstore.cloud.dto.AppRateReviewDto;
import org.bitvault.appstore.cloud.dto.AppRewRepDetailDto;
import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
 * The persistent class for the app_rate_review database table.
 * 
 */
@Entity
@Table(name = "app_rate_review")
@NamedQuery(name = "AppRateReview.findAll", query = "SELECT a FROM AppRateReview a")
public class AppRateReview extends Auditable<String> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "app_rate_review_id", unique = true, nullable = false)
	private Integer appRateReviewId;

	@Column(name = "app_rating")
	private int appRating;

	@Column(name = "app_review", length = 255)
	private String appReview;

	// @Temporal(TemporalType.TIMESTAMP)
	// @Column(name = "created_at", nullable = false)
	// private Date createdAt;
	//
	// @Column(name = "created_by", nullable = false, length = 255)
	// private String createdBy;
	//
	// @Temporal(TemporalType.TIMESTAMP)
	// @Column(name = "updated_at", nullable = false)
	// private Date updatedAt;
	//
	// @Column(name = "updated_by", nullable = false, length = 255)
	// private String updatedBy;

	// bi-directional many-to-one association to Application
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "app_application_id", nullable = false)
	@JsonIgnore
	@JsonManagedReference("AppApplication-AppRateReview")
	private AppApplication appApplication;

	public AppApplication getAppApplication() {
		return appApplication;
	}

	public void setAppApplication(AppApplication appApplication) {
		this.appApplication = appApplication;
	}

	// bi-directional many-to-one association to AppReviewReply
	@OneToOne(mappedBy = "appRateReview")
	@JsonIgnore
	@JsonBackReference("AppReviewReply-AppRateReview")
	private AppReviewReply appReviewReplies;

	// bi-directional many-to-one association to mobileUser
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "mobile_user_id")
	@JsonIgnore
	@JsonManagedReference("MobileUser-AppRateReview")
	private MobileUser mobileUser;

	public MobileUser getMobileUser() {
		return mobileUser;
	}

	public void setMobileUser(MobileUser mobileUser) {
		this.mobileUser = mobileUser;
	}

	public AppRateReview() {
	}

	public Integer getAppRateReviewId() {
		return appRateReviewId;
	}

	public void setAppRateReviewId(Integer appRateReviewId) {
		this.appRateReviewId = appRateReviewId;
	}

	public int getAppRating() {
		return this.appRating;
	}

	public void setAppRating(int appRating) {
		this.appRating = appRating;
	}

	public String getAppReview() {
		return this.appReview;
	}

	public void setAppReview(String appReview) {
		this.appReview = appReview;
	}

	public AppReviewReply getAppReviewReplies() {
		return this.appReviewReplies;
	}

	public void setAppReviewReplies(AppReviewReply appReviewReplies) {
		this.appReviewReplies = appReviewReplies;
	}

	// public AppReviewReply addAppReviewReply(AppReviewReply appReviewReply) {
	// getAppReviewReplies().add(appReviewReply);
	// appReviewReply.setAppRateReview(this);
	//
	// return appReviewReply;
	// }
	//
	// public AppReviewReply removeAppReviewReply(AppReviewReply appReviewReply)
	// {
	// getAppReviewReplies().remove(appReviewReply);
	// appReviewReply.setAppRateReview(null);
	//
	// return appReviewReply;
	// }

	public AppRateReviewDto populateAppRateReview(AppRateReview appRateReview) {
		AppRateReviewDto appRateReviewDto = new AppRateReviewDto();
		try {
			AppReviewReply appReviewReply = new AppReviewReply();
			AppApplication application = new AppApplication();
			BeanUtils.copyProperties(appRateReview, appRateReviewDto);
			application = appRateReview.getAppApplication();
			appReviewReply = appRateReview.getAppReviewReplies();
			if (appReviewReply != null) {
				appRateReviewDto.setReplyResponse(appReviewReply.getReplyResponse());
				appRateReviewDto.setApplicationId(application.getAppApplicationId());
				appRateReviewDto.setMobileUserId(appRateReview.getMobileUser().getMobileUserId());
			} else {
				appRateReviewDto.setApplicationId(application.getAppApplicationId());
				appRateReviewDto.setMobileUserId(appRateReview.getMobileUser().getMobileUserId());

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return appRateReviewDto;

	}

	public AppRewRepDetailDto populateAppRateRev(AppRateReview appRateReview) {
		AppRewRepDetailDto appRewRepDetailDto = new AppRewRepDetailDto();
		try {
			AppReviewReply appReviewReply = new AppReviewReply();

			AppApplication application = new AppApplication();
			application = appRateReview.getAppApplication();
			appReviewReply = appRateReview.getAppReviewReplies();
			BeanUtils.copyProperties(appRateReview, appRewRepDetailDto);

			if (appReviewReply != null) {
				if (application.getUserId().equals(appReviewReply.getReplyFrom())) {
					appRewRepDetailDto.setReplyFrom(application.getCompany());

				} else {
					appRewRepDetailDto.setReplyFrom("BITVAULT");
				}
				appRewRepDetailDto.setReplyResponse(appReviewReply.getReplyResponse());
				appRewRepDetailDto.setReplyUpdatedAt(appReviewReply.getUpdatedAt());
				appRewRepDetailDto.setApplicationId(application.getAppApplicationId());
				appRewRepDetailDto.setMobileUserId(appRateReview.getMobileUser().getMobileUserId());
			} else {
				appRewRepDetailDto.setApplicationId(application.getAppApplicationId());
				appRewRepDetailDto.setMobileUserId(appRateReview.getMobileUser().getMobileUserId());

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return appRewRepDetailDto;

	}

}