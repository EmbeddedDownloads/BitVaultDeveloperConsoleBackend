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

import org.bitvault.appstore.cloud.constant.DbConstant;
import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.dto.AppStatisticsDto;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * The persistent class for the app_statistics database table.
 * 
 */
@Entity
@Table(name = "app_statistics")
@NamedQuery(name = "AppStatistic.findAll", query = "SELECT a FROM AppStatistic a")
public class AppStatistic extends Auditable<String> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "app_statistics_id", unique = true, nullable = false)
	private Integer appStatisticsId;

	@Column(name = "app_crash_count", nullable = false)
	private Integer appCrashCount;

	@Column(name = "app_install_count", nullable = false)
	private Integer appInstallCount;

	@Column(name = "app_uninstall_count", nullable = false)
	private Integer appUninstallCount;

	@Column(name = "app_download_count", nullable = false)
	private Integer appDownloadCount;

	public Integer getAppDownloadCount() {
		return appDownloadCount;
	}

	public void setAppDownloadCount(Integer appDownloadCount) {
		this.appDownloadCount = appDownloadCount;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "application_id", nullable = false)
	@JsonIgnore
	@JsonBackReference("AppApplication-AppStatistic")
	private AppApplication application;

	public AppApplication getApplication() {
		return application;
	}

	public void setApplication(AppApplication application) {
		this.application = application;
	}

	public AppStatistic() {
	}

	public Integer getAppStatisticsId() {
		return this.appStatisticsId;
	}

	public void setAppStatisticsId(Integer appStatisticsId) {
		this.appStatisticsId = appStatisticsId;
	}

	public Integer getAppCrashCount() {
		return this.appCrashCount;
	}

	public void setAppCrashCount(Integer appCrashCount) {
		this.appCrashCount = appCrashCount;
	}

	public Integer getAppInstallCount() {
		return this.appInstallCount;
	}

	public void setAppInstallCount(Integer appInstallCount) {
		this.appInstallCount = appInstallCount;
	}

	public Integer getAppUninstallCount() {
		return this.appUninstallCount;
	}

	public void setAppUninstallCount(Integer appUninstallCount) {
		this.appUninstallCount = appUninstallCount;
	}

	@Override
	public int hashCode() {
		final Integer prime = 31;
		Integer result = 1;
		result = (int) (prime * result + ((appStatisticsId == null) ? 0 : appStatisticsId.hashCode()));
		return result;
	}

	public AppStatistic populateAppStatisticsByAction(AppStatistic appStats, String action) {
		if (null != appStats && null != action) {

			int totalDownload = appStats.getAppDownloadCount();

			int totalInstall = appStats.getAppInstallCount();

			int totalUninstall = appStats.getAppUninstallCount();

			int totalCrashCount = appStats.getAppCrashCount();
			if (action.equalsIgnoreCase(DbConstant.INSTALL)) {
				totalInstall = totalInstall + 1;
			} else if (action.equalsIgnoreCase(DbConstant.UNINSTALL) && totalInstall > 0) {
				totalUninstall = totalUninstall + 1;
				totalInstall = totalInstall - 1;
			} else if (action.equalsIgnoreCase(DbConstant.DOWNLOAD)) {
				totalDownload = totalDownload + 1;
			} else if (action.equalsIgnoreCase(DbConstant.CRASH)) {
				totalCrashCount = totalCrashCount + 1;
			}
			appStats.setAppDownloadCount(totalDownload);
			appStats.setAppInstallCount(totalInstall);
			appStats.setAppUninstallCount(totalUninstall);
			appStats.setAppCrashCount(totalCrashCount);
		}
		return appStats;
	}

	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (!(obj instanceof AppStatistic))
			return false;
		AppStatistic equalCheck = (AppStatistic) obj;
		if ((appStatisticsId == null && equalCheck.appStatisticsId != null)
				|| (appStatisticsId != null && equalCheck.appStatisticsId == null))
			return false;
		if (appStatisticsId != null && !appStatisticsId.equals(equalCheck.appStatisticsId))
			return false;
		return true;
	}

	public AppStatisticsDto populateAppStatisticsDto(AppStatistic appStatistics) {
		AppStatisticsDto appStatsDto = new AppStatisticsDto();
		try {
			BeanUtils.copyProperties(appStatistics, appStatsDto);
			org.bitvault.appstore.cloud.dto.AppApplication appAppDto = appStatistics.getApplication()
					.populateAppApplicationDTO(appStatistics.getApplication());
			appStatsDto.setApplication(appAppDto);
		} catch (Exception e) {
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);
		}
		return appStatsDto;
	}
}