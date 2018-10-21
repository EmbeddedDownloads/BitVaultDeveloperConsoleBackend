package org.bitvault.appstore.cloud.dto;

import java.util.Date;

public class ApplicationElasticDto {
	private Integer applicationId;
	private String appName;
	private String packageName;
	private Integer appApplicationId;
	private String status;
	private String latestVersionName;
	private Integer latestVersionNo;
	private String title;
	private String appIconUrl;
	private String company;
	private float averageRating;
	private Date createdAt;
	private Date updatedAt;
	private Integer totalInstall;
    private Integer totalUninstall;
    private String email;
    private String language;
    private float appPrice;
	public float getAppPrice() {
		return appPrice;
	}

	public void setAppPrice(float appPrice) {
		this.appPrice = appPrice;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getTitle() {
		return title;
	}

	public Integer getLatestVersionNo() {
		return latestVersionNo;
	}

	public void setLatestVersionNo(Integer latestVersionNo) {
		this.latestVersionNo = latestVersionNo;
	}

	public String getAppIconUrl() {
		return appIconUrl;
	}

	public void setAppIconUrl(String appIconUrl) {
		this.appIconUrl = appIconUrl;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
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

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
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

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLatestVersionName() {
		return latestVersionName;
	}

	public void setLatestVersionName(String latestVersionName) {
		this.latestVersionName = latestVersionName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	 public Integer getAppApplicationId() {
	 return appApplicationId;
	 }
	 public void setAppApplicationId(Integer appApplicationId) {
	 this.appApplicationId = appApplicationId;
	 }
	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public Integer getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(Integer applicationId) {
		this.applicationId = applicationId;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}
	
}
