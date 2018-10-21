package org.bitvault.appstore.cloud.dto;

import java.util.Date;

import org.bitvault.appstore.cloud.constant.ErrorCode;
import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.bitvault.appstore.cloud.model.AppApplication;
import org.springframework.beans.BeanUtils;

public class AppApplicationDTO {
	private Integer appApplicationId;

	private String appIconUrl;
	
	private String appName;


	private float appPrice;

	private float averageRating;

	private Date createdAt;

	private String createdBy;

	private String email;

	private String latestVersionName;

	private float latestVersionNo;

	private String packageName;

	private String privacyPolicyUrl;


	private String status;

	private int subscription;

	private Date updatedAt;

	private String updatedBy;

	private String userId;

	private String website;

	private AppCategoryDTO appCategory;

	public Integer getAppApplicationId() {
		return appApplicationId;
	}

	public void setAppApplicationId(Integer appApplicationId) {
		this.appApplicationId = appApplicationId;
	}

	public String getAppIconUrl() {
		return appIconUrl;
	}

	public void setAppIconUrl(String appIconUrl) {
		this.appIconUrl = appIconUrl;
	}

	public float getAppPrice() {
		return appPrice;
	}

	public void setAppPrice(float appPrice) {
		this.appPrice = appPrice;
	}

	public float getAverageRating() {
		return averageRating;
	}

	public void setAverageRating(float averageRating) {
		this.averageRating = averageRating;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLatestVersionName() {
		return latestVersionName;
	}

	public void setLatestVersionName(String latestVersionName) {
		this.latestVersionName = latestVersionName;
	}

	public float getLatestVersionNo() {
		return latestVersionNo;
	}

	public void setLatestVersionNo(float latestVersionNo) {
		this.latestVersionNo = latestVersionNo;
	}

	public String getPackageName() {
		return packageName;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getPrivacyPolicyUrl() {
		return privacyPolicyUrl;
	}

	public void setPrivacyPolicyUrl(String privacyPolicyUrl) {
		this.privacyPolicyUrl = privacyPolicyUrl;
	}


	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getSubscription() {
		return subscription;
	}

	public void setSubscription(int subscription) {
		this.subscription = subscription;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public AppCategoryDTO getAppCategory() {
		return appCategory;
	}

	public void setAppCategory(AppCategoryDTO appCategory) {
		this.appCategory = appCategory;
	}

	public AppApplication populateAppApplication(AppApplicationDTO appApplicationDTO) {
		AppApplication app = new AppApplication();
		try {
			BeanUtils.copyProperties(appApplicationDTO, app);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED, ErrorCode.SOME_ERROR_OCCURED_CODE);
		}
		return app;
	}

}
