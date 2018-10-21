package org.bitvault.appstore.mobile.service;

import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.bitvault.appstore.cloud.constant.Constants;
import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.dto.ChartStatsDto;
import org.bitvault.appstore.cloud.dto.MobileUserAppDto;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.bitvault.appstore.cloud.model.MobileUserApp;
import org.bitvault.appstore.cloud.model.PaymentDetailsModel;
import org.bitvault.appstore.cloud.security.EncryptDecryptData;
import org.bitvault.appstore.cloud.security.SecurityConstants;
import org.bitvault.appstore.cloud.utils.Utility;
import org.bitvault.appstore.mobile.dao.MobileUserAppRepository;
import org.bitvault.appstore.mobile.dao.PaymentDetailsRepository;
import org.bitvault.appstore.mobile.dto.PaymentDetailsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MobileUserAppServiceImpl implements MobileUserAppService {

	@Autowired
	MobileUserAppRepository mobileUserAppRepository;

	@Autowired
	PaymentDetailsRepository paymentDetailsRepository;

	@Override
	public MobileUserAppDto saveMobileUserApp(MobileUserAppDto mobileUserAppDto) {
		MobileUserApp mobileUserApp = null;
		try {

			mobileUserApp = mobileUserAppDto.populateMobileUserApp(mobileUserAppDto);
			mobileUserApp = mobileUserAppRepository.saveAndFlush(mobileUserApp);
			mobileUserAppDto = mobileUserApp.populateMobileUserAppDto(mobileUserApp);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);
		}
		return mobileUserAppDto;
	}

	@Override
	public List<ChartStatsDto> getChartStatsbyAppIdAndYear(Integer appId, int year, String status) {
		List<ChartStatsDto> chartStatsDtoList = null;
		Set<String> monthListFromDto = new LinkedHashSet<String>();
		ChartStatsDto statsDto = null;
		try {
			chartStatsDtoList = mobileUserAppRepository.getChartStatsbyAppIdAndYear(appId, year, status);
			for (ChartStatsDto chartStatsDto : chartStatsDtoList) {
				monthListFromDto.add(chartStatsDto.getLabel());
			}
			Set<String> getAllMonth = Utility.getListOfAllMonth();
			getAllMonth.removeAll(monthListFromDto);
			for (String month : getAllMonth) {
				statsDto = new ChartStatsDto();
				statsDto.setCount(0);
				statsDto.setLabel(month);
				chartStatsDtoList.add(statsDto);
			}
			Collections.sort(chartStatsDtoList, new Comparator<ChartStatsDto>() {

				@Override
				public int compare(ChartStatsDto o1, ChartStatsDto o2) {
					try {
						SimpleDateFormat fmt = new SimpleDateFormat("MMM", Locale.US);
						return fmt.parse(o1.getLabel()).compareTo(fmt.parse(o2.getLabel()));
					} catch (Exception e) {
						throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);
					}

				}

			});
		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);
		}
		return chartStatsDtoList;
	}

	@Override
	public List<ChartStatsDto> getChartStatsbyAppIdAndDates(Integer appId, String startDate, String endDate,
			String status) {
		List<ChartStatsDto> chartStatsDtoList = null;
		ChartStatsDto statsDto = null;
		Set<String> dateFromStats = new LinkedHashSet<String>();
		try {
			Set<String> dateList = Utility.getListOfDatesBetweenDates(Utility.converStringToDate(startDate),
					Utility.converStringToDate(endDate));
			chartStatsDtoList = mobileUserAppRepository.getChartStatsbyAppIdAndDates(appId,
					Utility.converStringToDate(startDate), Utility.converStringToDate(endDate), status);

			for (ChartStatsDto chartStats : chartStatsDtoList) {
				chartStats.setLabel("" + chartStats.getDate());
				dateFromStats.add(chartStats.getLabel());
				chartStats.setDate(null);
			}
			// dateFromStats.addAll(dateList);
			dateList.removeAll(dateFromStats);
			for (String date : dateList) {
				statsDto = new ChartStatsDto();
				statsDto.setCount(0);
				statsDto.setLabel(date);

				chartStatsDtoList.add(statsDto);
			}
			Collections.sort(chartStatsDtoList, new Comparator<ChartStatsDto>() {

				@Override
				public int compare(ChartStatsDto o1, ChartStatsDto o2) {
					Date fromO1 = Utility.converStringToDate(o1.getLabel());
					Date fromO2 = Utility.converStringToDate(o2.getLabel());

					return fromO1.compareTo(fromO2);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);
		}
		return chartStatsDtoList;
	}

	public PaymentDetailsDto getPaymentDetails(String publicAddress) {

		PaymentDetailsModel paymentDetailPayment = paymentDetailsRepository.findOne(1);

		try {

			if (paymentDetailPayment == null) {
				throw new BitVaultException(ErrorMessageConstant.RECORD_NOT_FOUND);
			}

			if (publicAddress.equalsIgnoreCase("anuj")) {
				publicAddress = SecurityConstants.BTC_PUBLIC_KEY;
			}

			EncryptDecryptData encryptData = new EncryptDecryptData();

			if (paymentDetailPayment.getBitcoinWalletAddress() != null) {

				byte[] encryptedBitcoinWallet = encryptData
						.encryptData(paymentDetailPayment.getBitcoinWalletAddress().getBytes(), publicAddress);

				String encryptedBitcoinWalletAddress = Base64.getEncoder().encodeToString(encryptedBitcoinWallet);
				
				paymentDetailPayment.setBitcoinWalletAddress(encryptedBitcoinWalletAddress);

			}

			if (paymentDetailPayment.getEotcoinWalletAddress() != null) {

				byte[] encryptedEotcoinWallet = encryptData
						.encryptData(paymentDetailPayment.getEotcoinWalletAddress().getBytes(), publicAddress);

				String encryptedEotcoinWalletAddress = Base64.getEncoder().encodeToString(encryptedEotcoinWallet);
				
				paymentDetailPayment.setEotcoinWalletAddress(encryptedEotcoinWalletAddress);

			}

			PaymentDetailsDto paymentDetailsDto = paymentDetailPayment.populateModel(paymentDetailPayment);

			return paymentDetailsDto;
		} catch (BitVaultException e) {
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);
		}

	}

}
