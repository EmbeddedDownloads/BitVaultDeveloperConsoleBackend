package org.bitvault.appstore.cloud.dto;

import java.util.Date;

import org.bitvault.appstore.cloud.constant.DbConstant;
import org.bitvault.appstore.cloud.constant.ErrorCode;
import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.bitvault.appstore.cloud.model.AppDetail;
import org.bitvault.appstore.cloud.model.Application;
import org.bitvault.appstore.cloud.model.ApplicationType;
import org.springframework.beans.BeanUtils;

public class AppApplication {
	private Integer appApplicationId;
	private String appName;

	private String appIconUrl;
	private String language;

	private String title;

	private float appPrice;

	private float averageRating;
	private long apkFilesize;

	private Integer totalInstall;

	private Integer totalUninstall;

	private Integer totalDownloads;

	private Date createdAt;

	private String company;
	private String appPermission;

	private String createdBy;
	private String updateType;
	private String reason;

	private String email;

	private String apkUrl;

	private String latestVersionName;

	private Integer latestVersionNo;

	private String packageName;

	private String privacyPolicyUrl;

	private ApplicationTypeDto applicationType;

	private String status;

	private Integer subscription;

	private Date updatedAt;

	private String updatedBy;

	private String userId;

	private String website;

	private AppCategory appCategory;

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

	public Integer getSubscription() {
		return subscription;
	}

	public void setSubscription(Integer subscription) {
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

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public org.bitvault.appstore.cloud.model.AppApplication populateAppApplicationBean(
			AppApplication appApplicationDTO) {
		org.bitvault.appstore.cloud.model.AppApplication app = new org.bitvault.appstore.cloud.model.AppApplication();
		try {
			BeanUtils.copyProperties(appApplicationDTO, app);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED, ErrorCode.SOME_ERROR_OCCURED_CODE);
		}
		return app;
	}

	public Application populatePublisheApp(AppApplication appAppDto) {
		Application app = new Application();
		try {
			BeanUtils.copyProperties(appAppDto, app);
			app.setApplicationId(appAppDto.getAppApplicationId());
			app.setAppCategory(appAppDto.getAppCategory().populateAppCategoryDTO(appAppDto.getAppCategory()));
			app.setApplicationType(
					appAppDto.getApplicationType().populateApplicationTypeDto(appAppDto.getApplicationType()));
			app.setLatestVersion(appAppDto.getLatestVersionName());
			app.setStatus(DbConstant.PUBLISHED);
		} catch (Exception e) {
			e.printStackTrace(); 
		}
		return app;
	}

	public org.bitvault.appstore.cloud.model.AppApplication populateAppApplication(AppApplication appApplicationDTO) {
		org.bitvault.appstore.cloud.model.AppApplication app = new org.bitvault.appstore.cloud.model.AppApplication();

		app.setAppApplicationId(appApplicationDTO.getAppApplicationId());
		app.setAppName(appApplicationDTO.getAppName());
		app.setLatestVersionName(appApplicationDTO.getLatestVersionName());
		app.setLatestVersionNo(appApplicationDTO.getLatestVersionNo());
		
		app.setPackageName(appApplicationDTO.getPackageName());
		app.setCompany(appApplicationDTO.getCompany());
		// ObjectMapper om = new ObjectMapper();
		app.setAppPermission(appApplicationDTO.getAppPermission());
		app.setTitle(appApplicationDTO.getTitle());
		app.setApkUrl(appApplicationDTO.getApkUrl());
		app.setAppIconUrl(appApplicationDTO.getAppIconUrl());
		app.setLanguage(appApplicationDTO.getLanguage());
		app.setStatus(appApplicationDTO.getStatus());
		app.setUserId(appApplicationDTO.getUserId());
		app.setUpdateType(appApplicationDTO.getUpdateType());
		app.setCreatedAt(appApplicationDTO.getCreatedAt());
		app.setUpdatedAt(appApplicationDTO.getUpdatedAt());
		app.setPrivacyPolicyUrl(appApplicationDTO.getPrivacyPolicyUrl());
		app.setWebsite(appApplicationDTO.getWebsite());
		app.setEmail(appApplicationDTO.getEmail());
		app.setTotalDownloads(appApplicationDTO.getTotalDownloads());
		app.setTotalInstall(appApplicationDTO.getTotalInstall());
		app.setTotalUninstall(appApplicationDTO.getTotalUninstall());
		app.setApkFilesize(appApplicationDTO.getApkFilesize());
		app.setAverageRating(appApplicationDTO.getAverageRating());
		org.bitvault.appstore.cloud.model.AppCategory appCategory = appApplicationDTO.getAppCategory()
				.populateAppCategoryDTO(appApplicationDTO.getAppCategory());
		app.setAppCategory(appCategory);
		ApplicationType appType = appApplicationDTO.getApplicationType()
				.populateApplicationTypeDto(appApplicationDTO.getApplicationType());
		app.setApplicationType(appType);
		app.setReason(appApplicationDTO.getReason());
		return app;
	}

	public AppDetail populateAppDetail(AppApplication appDto) {
		AppDetail appDetail = new AppDetail();

		appDetail.setAppVersionName(appDto.getLatestVersionName());
		appDetail.setAppVersionNo(appDto.getLatestVersionNo());
		appDetail.setApkUrl(appDto.getApkUrl());
		appDetail.setAppIconUrl(appDto.getAppIconUrl());
		appDetail.setAppApplication(appDto.populateAppApplication(appDto));
		appDetail.setStatus(appDto.getStatus());
		return appDetail;
	}

	public String getApkUrl() {
		return apkUrl;
	}

	public void setApkUrl(String apkUrl) {
		this.apkUrl = apkUrl;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
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
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (int) (prime * result + ((appApplicationId == null) ? 0 : appApplicationId.hashCode()));
		return result;
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (!(obj instanceof AppApplication))
			return false;
		AppApplication equalCheck = (AppApplication) obj;
		if ((appApplicationId == null && equalCheck.appApplicationId != null)
				|| (appApplicationId != null && equalCheck.appApplicationId == null))
			return false;
		if (appApplicationId != null && !appApplicationId.equals(equalCheck.appApplicationId))
			return false;
		return true;
	}
	public ApplicationReqActDto populateApplication(AppApplication appDto) {
		ApplicationReqActDto applicationReqActDto=new ApplicationReqActDto();
		BeanUtils.copyProperties(appDto, applicationReqActDto);
		return applicationReqActDto;
	}

}
