package org.bitvault.appstore.commons.application.service;

import java.util.ArrayList;
import java.util.List;

import org.bitvault.appstore.cloud.constant.DbConstant;
import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.dto.AppStatisticsDto;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.bitvault.appstore.cloud.model.AppApplication;
import org.bitvault.appstore.cloud.model.AppStatistic;
import org.bitvault.appstore.cloud.model.Application;
import org.bitvault.appstore.commons.application.dao.AppApplicationRepository;
import org.bitvault.appstore.commons.application.dao.AppStatisticsRepository;
import org.bitvault.appstore.commons.application.dao.ApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class AppStatisticsServiceImpl implements AppStatisticsService {

	@Autowired
	AppStatisticsRepository appStatsRepository;

	@Autowired
	AppApplicationRepository appApplicationRepository;

	@Autowired
	ApplicationRepository applicationRepository;

	@Override
	public AppStatisticsDto saveAppStatistics(AppStatisticsDto appStatisticsDto, String action) {
		AppStatistic appStats = null;
		try {
			appStats = appStatsRepository
					.findAppStatisticsByAppId(appStatisticsDto.getApplication().getAppApplicationId());
			if (null != appStats) {
				appStats = appStats.populateAppStatisticsByAction(appStats, action);
				appStats = appStatsRepository.saveAndFlush(appStats);
				appStatisticsDto = appStats.populateAppStatisticsDto(appStats);

			} else {
				appStatisticsDto.setAppDownloadCount(1);
				appStats = appStatisticsDto.populateAppStatistics(appStatisticsDto);
				appStats = appStatsRepository.saveAndFlush(appStats);
				appStatisticsDto = appStats.populateAppStatisticsDto(appStats);
			}
			updateAppOnAction(appStats);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);
		}
		return appStatisticsDto;
	}

	@Override
	public AppStatisticsDto findAppStatisticsByAppId(Integer appId) {
		AppStatistic appStats = null;
		AppStatisticsDto appStatsDto = null;
		try {
			appStats = appStatsRepository.findAppStatisticsByAppId(appId);
			if (null != appStats) {
				appStatsDto = appStats.populateAppStatisticsDto(appStats);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);
		}
		return appStatsDto;
	}

	@Override
	public List<AppStatisticsDto> findAllStatsByUserId(String userId, int page, int size, String direction,
			String property) {
		List<AppStatisticsDto> appStatsDtoList = null;
		AppStatisticsDto appStatsDto = null;
		Page<AppStatistic> appStatsList = null;
		try {
			if (direction.equals(DbConstant.ASC)) {
				appStatsList = appStatsRepository.findAllStatsByUserId(userId,
						new PageRequest(page, size, Sort.Direction.ASC, property));
			} else {
				appStatsList = appStatsRepository.findAllStatsByUserId(userId,
						new PageRequest(page, size, Sort.Direction.DESC, property));
			}
			if (null != appStatsList && appStatsList.getSize() > 0) {
				appStatsDtoList = new ArrayList<AppStatisticsDto>();
			}
			for (AppStatistic appStatistic : appStatsList) {
				appStatsDto = appStatistic.populateAppStatisticsDto(appStatistic);
				appStatsDtoList.add(appStatsDto);
			}
		} catch (Exception e) {

			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);

		}
		return appStatsDtoList;
	}

	private void updateAppOnAction(AppStatistic appStats) {
		try {
			Application app = applicationRepository.findOne(appStats.getApplication().getAppApplicationId());
			if (null != app) {
				app.setTotalDownloads(appStats.getAppDownloadCount());
				app.setTotalInstall(appStats.getAppInstallCount());
				app.setTotalUninstall(appStats.getAppUninstallCount());
				applicationRepository.saveAndFlush(app);

				AppApplication appApp = appApplicationRepository
						.findOne(appStats.getApplication().getAppApplicationId());

				appApp.setTotalDownloads(appStats.getAppDownloadCount());
				appApp.setTotalInstall(appStats.getAppInstallCount());
				appApp.setTotalUninstall(appStats.getAppUninstallCount());
				appApplicationRepository.saveAndFlush(appApp);

			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);
		}

	}

}
