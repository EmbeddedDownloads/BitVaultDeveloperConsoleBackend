package org.bitvault.appstore.mobile.service;

import java.util.Date;
import java.util.List;

import org.bitvault.appstore.cloud.dto.ChartStatsDto;
import org.bitvault.appstore.cloud.dto.MobileUserAppDto;
import org.bitvault.appstore.mobile.dto.PaymentDetailsDto;

public interface MobileUserAppService {

	MobileUserAppDto saveMobileUserApp(MobileUserAppDto mobileUserAppDto);
	List<ChartStatsDto> getChartStatsbyAppIdAndYear(Integer appId, int year, String status);
	List<ChartStatsDto> getChartStatsbyAppIdAndDates(Integer appId, String startDate, String endDate, String status);

	PaymentDetailsDto getPaymentDetails(String publicAddress);
}
