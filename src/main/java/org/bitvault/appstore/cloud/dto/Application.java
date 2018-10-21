package org.bitvault.appstore.cloud.dto;

import java.util.Date;

import org.bitvault.appstore.cloud.constant.ErrorCode;
import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.springframework.beans.BeanUtils;

public class Application {

	private Integer applicationId;

	private String appIconUrl;

	private String appName;
	private String language;
	private String title;
	private String apkUrl;

	private String company;
	private String appPermission;

	private String status;
	private float appPrice;
	private String updateType;

	private ApplicationTypeDto applicationType;

	private float averageRating;

	private Date createdAt;

	private String createdBy;

	private String email;

	private String latestVersion;

	private Integer latestVersionNo;

	private String packageName;

	private String privacyPolicyUrl;

	private int subscription;

	private Date updatedAt;

	private String updatedBy;

	private String userId;

	private String website;

	private AppCategory appCategory;
	private long apkFilesize;

	private Integer totalInstall;

	private Integer totalUninstall;

	private Integer totalDownloads;

	public long getApkFilesize() {
		return apkFilesize;
	}

	public void setApkFilesize(long apkFilesize) {
		this.apkFilesize = apkFilesize;
	}

	public Integer getTotalInstall() {
		return totalInstall;
	}

	public void setTotalInstall(Integer totalInstall) {
		this.totalInstall = totalInstall;
	}

	public Integer getTotalUninstall() {
		return totalUninstall;
	}

	public void setTotalUninstall(Integer totalUninstall) {
		this.totalUninstall = totalUninstall;
	}

	public Integer getTotalDownloads() {
		return totalDownloads;
	}

	public void setTotalDownloads(Integer totalDownloads) {
		this.totalDownloads = totalDownloads;
	}

	public Integer getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(Integer applicationId) {
		this.applicationId = applicationId;
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

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getAppPermission() {
		return appPermission;
	}

	public void setAppPermission(String appPermission) {
		this.appPermission = appPermission;
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

	public String getLatestVersion() {
		return latestVersion;
	}

	public void setLatestVersion(String latestVersion) {
		this.latestVersion = latestVersion;
	}

	public Integer getLatestVersionNo() {
		return latestVersionNo;
	}

	public void setLatestVersionNo(Integer latestVersionNo) {
		this.latestVersionNo = latestVersionNo;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getPrivacyPolicyUrl() {
		return privacyPolicyUrl;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setPrivacyPolicyUrl(String privacyPolicyUrl) {
		this.privacyPolicyUrl = privacyPolicyUrl;
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

	public AppCategory getAppCategory() {
		return appCategory;
	}

	public void setAppCategory(AppCategory appCategory) {
		this.appCategory = appCategory;
	}

	public String getApkUrl() {
		return apkUrl;
	}

	public void setApkUrl(String apkUrl) {
		this.apkUrl = apkUrl;
	}

	public org.bitvault.appstore.cloud.model.Application populateApplication(Application appDto) {
		org.bitvault.appstore.cloud.model.Application app = new org.bitvault.appstore.cloud.model.Application();
		try {
			BeanUtils.copyProperties(appDto, app);
			app.setAppCategory(appDto.getAppCategory().populateAppCategoryDTO(appDto.getAppCategory()));
			app.setApplicationType(appDto.getApplicationType().populateApplicationTypeDto(appDto.getApplicationType()));

		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED, ErrorCode.SOME_ERROR_OCCURED_CODE);
		}
		return app;
	}

	public String getUpdateType() {
		return updateType;
	}

	public void setUpdateType(String updateType) {
		this.updateType = updateType;
	}

	public ApplicationTypeDto getApplicationType() {
		return applicationType;
	}

	public void setApplicationType(ApplicationTypeDto applicationType) {
		this.applicationType = applicationType;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public org.bitvault.appstore.cloud.dto.AppDetail populateAppDetail(Application appDto) {
		org.bitvault.appstore.cloud.dto.AppDetail appDetail = new org.bitvault.appstore.cloud.dto.AppDetail();

		appDetail.setAppVersionName(appDto.getLatestVersion());
		appDetail.setAppVersionNo(appDto.getLatestVersionNo());
		appDetail.setApkUrl(appDto.getApkUrl());
		appDetail.setAppIconUrl(appDto.getAppIconUrl());
		
		appDetail.setStatus(appDto.getStatus());
		return appDetail;
	}
}