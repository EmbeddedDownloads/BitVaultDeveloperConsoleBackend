package org.bitvault.appstore.commons.application.service;

import java.util.List;

import org.bitvault.appstore.cloud.dto.AppStatisticsDto;

public interface AppStatisticsService {

	AppStatisticsDto saveAppStatistics(AppStatisticsDto appStatisticsDto, String action);

	AppStatisticsDto findAppStatisticsByAppId(Integer appId);

	List<AppStatisticsDto> findAllStatsByUserId(String userId, int page, int size, String direction, String property);

}
