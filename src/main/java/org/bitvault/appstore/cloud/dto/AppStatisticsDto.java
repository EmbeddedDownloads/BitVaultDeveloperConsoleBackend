package org.bitvault.appstore.cloud.dto;

import java.util.Date;

import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.bitvault.appstore.cloud.model.AppStatistic;
import org.springframework.beans.BeanUtils;

public class AppStatisticsDto {
	private Integer appStatisticsId;

	private Integer appCrashCount = 0;

	private Integer appInstallCount = 0;

	private Integer appUninstallCount = 0;

	private Integer appDownloadCount = 0;

	private AppApplication application;

	private Date createdAt;

	private Date updatedAt;

	public Integer getAppStatisticsId() {
		return appStatisticsId;
	}

	public void setAppStatisticsId(Integer appStatisticsId) {
		this.appStatisticsId = appStatisticsId;
	}

	public Integer getAppCrashCount() {
		return appCrashCount;
	}

	public void setAppCrashCount(Integer appCrashCount) {
		this.appCrashCount = appCrashCount;
	}

	public Integer getAppInstallCount() {
		return appInstallCount;
	}

	public void setAppInstallCount(Integer appInstallCount) {
		this.appInstallCount = appInstallCount;
	}

	public Integer getAppUninstallCount() {
		return appUninstallCount;
	}

	public void setAppUninstallCount(Integer appUninstallCount) {
		this.appUninstallCount = appUninstallCount;
	}

	public Integer getAppDownloadCount() {
		return appDownloadCount;
	}

	public void setAppDownloadCount(Integer appDownloadCount) {
		this.appDownloadCount = appDownloadCount;
	}

	public AppApplication getApplication() {
		return application;
	}

	public void setApplication(AppApplication application) {
		this.application = application;
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

	public AppStatistic populateAppStatistics(AppStatisticsDto appStatisticsDto) {
		AppStatistic appStats = new AppStatistic();
		try {
			BeanUtils.copyProperties(appStatisticsDto, appStats);
			appStats.setApplication(appStatisticsDto.getApplication().populateAppApplication(appStatisticsDto.getApplication()));
		} catch (Exception e) {
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);
		}
		return appStats;
	}
}
