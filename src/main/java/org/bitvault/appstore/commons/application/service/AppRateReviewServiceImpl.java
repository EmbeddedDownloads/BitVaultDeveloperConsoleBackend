package org.bitvault.appstore.commons.application.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

import org.bitvault.appstore.cloud.constant.Constants;
import org.bitvault.appstore.cloud.constant.DbConstant;
import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.dto.AppApplication;
import org.bitvault.appstore.cloud.dto.AppRateReviewDto;
import org.bitvault.appstore.cloud.dto.AppRewRepDetailDto;
import org.bitvault.appstore.cloud.dto.AverageRatingChartDto;
import org.bitvault.appstore.cloud.dto.MobileUser;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.bitvault.appstore.cloud.model.AppRateReview;
import org.bitvault.appstore.cloud.model.AppReviewReply;
import org.bitvault.appstore.cloud.user.dev.dao.ReviewReplyRepository;
import org.bitvault.appstore.cloud.user.dev.service.ReviewReplyService;
import org.bitvault.appstore.cloud.utils.Utility;
import org.bitvault.appstore.commons.application.dao.AppRateReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service("AppRateReviewService")
@Transactional
public class AppRateReviewServiceImpl implements AppRateReviewService {

	@Autowired
	AppRateReviewRepository appRateReviewRespository;
	@Autowired
	ReviewReplyRepository reviewReplyRepository;
	
	@Autowired
	ReviewReplyService reviewReplyService;

	@Override
	public AppRateReviewDto saveRateReview(AppRateReviewDto appRateReviewDto, AppApplication appApplicatioDto,
			MobileUser mobileUser) {
		AppRateReview appRateReview = null;
		AppRateReview appRateReviewEntity = null;
		org.bitvault.appstore.cloud.model.AppApplication appApplication;
		try {

			appApplication = appApplicatioDto.populateAppApplication(appApplicatioDto);

			appRateReview = appRateReviewDto.populateAppRateReview(appRateReviewDto, appApplication);
			appRateReview.setMobileUser(mobileUser.populateMobileUser(mobileUser));
			appRateReviewEntity = appRateReviewRespository.saveAndFlush(appRateReview);

			appRateReviewDto = appRateReview.populateAppRateReview(appRateReviewEntity);

		} catch (Exception e) {

			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);
		}

		return appRateReviewDto;
	}

	@Override
	public float getAverageRating(Integer applicationId) {

		Float avgRating;
		try {

			avgRating = appRateReviewRespository.findAverageByRating(applicationId);
			if (null == avgRating) {
				avgRating = 0.0f;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);
		}
		return avgRating;
	}

	@Override
	public AppRateReviewDto findAppRateReviewByAppRateReviewId(Integer appRateReviewId) {

		AppRateReviewDto appRateReviewDto = null;
		try {

			AppRateReview appRateReview = appRateReviewRespository.findAppRateReviewByAppRateReviewId(appRateReviewId);

			if (null != appRateReview) {
				appRateReviewDto = appRateReview.populateAppRateReview(appRateReview);
			}
			// findAppRateReviewByRating();
		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);
		}
		return appRateReviewDto;
	}

	@Override
	public Map<String, Long> findStarRatingByApplicationId(Integer applicationId) {

		List<Object[]> ratingData = null;
		Map<String, Long> starCountMap = new LinkedHashMap<String, Long>();
		starCountMap.put("star1", new Long(0));
		starCountMap.put("star2", new Long(0));
		starCountMap.put("star3", new Long(0));
		starCountMap.put("star4", new Long(0));
		starCountMap.put("star5", new Long(0));

		try {

			// System.out.println(appRateReviewRespository.findStarRatingByApplicationId(modelApplication).get(0));
			ratingData = appRateReviewRespository.findStarRatingByApplicationId(applicationId);
			for (Object[] objects : ratingData) {
				if (objects[1].equals(1)) {
					starCountMap.put("star1", (Long) objects[0]);
				} else if (objects[1].equals(2)) {
					starCountMap.put("star2", (Long) objects[0]);

				} else if (objects[1].equals(3)) {
					starCountMap.put("star3", (Long) objects[0]);

				} else if (objects[1].equals(4)) {
					starCountMap.put("star4", (Long) objects[0]);

				} else if (objects[1].equals(5)) {
					starCountMap.put("star5", (Long) objects[0]);

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);
		}
		return starCountMap;
	}

	@Override
	public Map<String, Object> findAppRateReviewByAppId(Integer applicationId, Integer page, Integer size,
			String direction, String property) {
		Page<AppRateReview> appRateReviewList = null;
		AppRewRepDetailDto appRewRepDetailDto = null;
		Float avgRating;
		List<AppRewRepDetailDto> appRewRepDetailDtoList = new ArrayList<AppRewRepDetailDto>();

		Map<String, Object> allAppMap = null;
		try {
			if (direction.equals(DbConstant.ASC)) {
				appRateReviewList = appRateReviewRespository.findAppRateReviewByAppId(applicationId,
						new PageRequest(page, size, Sort.Direction.ASC, property));
			} else {
				appRateReviewList = appRateReviewRespository.findAppRateReviewByAppId(applicationId,
						new PageRequest(page, size, Sort.Direction.DESC, property));

			}
			for (AppRateReview appRateReview : appRateReviewList) {
				appRewRepDetailDto = appRateReview.populateAppRateRev(appRateReview);
				appRewRepDetailDtoList.add(appRewRepDetailDto);
			}

			allAppMap = new HashMap<String, Object>();

			allAppMap.put("appRateReviewDtoList", appRewRepDetailDtoList);
			allAppMap.put(Constants.TOTAL_PAGES, appRateReviewList.getTotalPages());
			allAppMap.put(Constants.TOTAL_RECORDS, appRateReviewList.getTotalElements());
			allAppMap.put(Constants.SIZE, appRateReviewList.getNumberOfElements());
			allAppMap.put(Constants.SORT, appRateReviewList.getSort());

		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);
		}

		return allAppMap;

	}

	@Override
	public AppRateReviewDto findAppRateReviewByMobileUserId(Integer mobileUserId, Integer applicationId) {

		AppRateReviewDto appRateReviewDto = null;
		try {

			AppRateReview appRateReview = appRateReviewRespository.findAppRateReviewByMobileUserId(mobileUserId,
					applicationId);

			if (null != appRateReview) {
				appRateReviewDto = appRateReview.populateAppRateReview(appRateReview);
			}
			// findAppRateReviewByRating();
		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);
		}
		return appRateReviewDto;
	}

	@Override
	public int findTotalRatingByAppId(Integer applicationId) {

		int totalCount;
		try {

			totalCount = appRateReviewRespository.findCountByApplicationId(applicationId);

		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);
		}
		return totalCount;
	}

	@Override
	public int findReviewWithRatingByAppId(Integer applicationId) {

		int totalCount;
		try {

			totalCount = appRateReviewRespository.findReviewRatingCountByApplicationId(applicationId);

		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);
		}
		return totalCount;
	}

	// @Override
	// public float findAvgRatingByAppId(Integer applicationId) {
	//
	// Float avgRating;
	// try {
	//
	// avgRating = appRateReviewRespository.findAverageByRating(applicationId);
	// if (null == avgRating) {
	// avgRating = 0.0f;
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);
	// }
	// return avgRating;
	// }

	@Override
	public List<AverageRatingChartDto> getChartStatsbyAppIdAndYear(Integer appId, int year) {
		List<AverageRatingChartDto> chartStatsDtoList = null;
		Set<String> monthListFromDto = new LinkedHashSet<String>();
		AverageRatingChartDto statsDto = null;
		try {
			chartStatsDtoList = appRateReviewRespository.getChartStatsbyAppIdAndYear(appId, year);
			for (AverageRatingChartDto chartStatsDto : chartStatsDtoList) {
				monthListFromDto.add(chartStatsDto.getLabel());
				double roundedAverage = new BigDecimal(chartStatsDto.getAverage()).setScale(1, BigDecimal.ROUND_HALF_UP)
						.doubleValue();

				chartStatsDto.setAverage(roundedAverage);
			}
			Set<String> getAllMonth = Utility.getListOfAllMonth();
			getAllMonth.removeAll(monthListFromDto);
			for (String month : getAllMonth) {
				statsDto = new AverageRatingChartDto();
				statsDto.setAverage(0.0);
				statsDto.setLabel(month);
				chartStatsDtoList.add(statsDto);
			}
			Collections.sort(chartStatsDtoList, new Comparator<AverageRatingChartDto>() {

				@Override
				public int compare(AverageRatingChartDto o1, AverageRatingChartDto o2) {
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
	public List<AverageRatingChartDto> getChartStatsbyAppIdAndDates(Integer appId, String startDate, String endDate) {
		List<AverageRatingChartDto> chartStatsDtoList = null;
		AverageRatingChartDto statsDto = null;
		Set<String> dateFromStats = new LinkedHashSet<String>();
		try {
			Set<String> dateList = Utility.getListOfDatesBetweenDates(Utility.converStringToDate(startDate),
					Utility.converStringToDate(endDate));
			chartStatsDtoList = appRateReviewRespository.getChartStatsbyAppIdAndDates(appId,
					Utility.converStringToDate(startDate), Utility.converStringToDate(endDate));

			for (AverageRatingChartDto chartStats : chartStatsDtoList) {
				chartStats.setLabel("" + chartStats.getDate());

				double roundedAverage = new BigDecimal(chartStats.getAverage()).setScale(1, BigDecimal.ROUND_HALF_UP)
						.doubleValue();

				chartStats.setAverage(roundedAverage);

				dateFromStats.add(chartStats.getLabel());

				chartStats.setDate(null);
			}
			// dateFromStats.addAll(dateList);
			dateList.removeAll(dateFromStats);
			for (String date : dateList) {
				statsDto = new AverageRatingChartDto();
				statsDto.setAverage(0.0);
				statsDto.setLabel(date);

				chartStatsDtoList.add(statsDto);
			}
			Collections.sort(chartStatsDtoList, new Comparator<AverageRatingChartDto>() {

				@Override
				public int compare(AverageRatingChartDto o1, AverageRatingChartDto o2) {
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

	@Override
	public void deleteReview(Integer appRateReviewId) {
		try {
			

			AppReviewReply appReply = reviewReplyRepository.findByappRateReviewId(appRateReviewId);

			if (appReply != null) {
				reviewReplyService.deleteReviewReply(appRateReviewId);
			}
				appRateReviewRespository.delete(appRateReviewId);
			
		} catch (Exception e) {
			if (e instanceof BitVaultException) {
				throw new BitVaultException(e.getMessage());
			}
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);
		}
	}
}
