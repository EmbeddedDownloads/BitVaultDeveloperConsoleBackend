package org.bitvault.appstore.mobile.controller;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.bitvault.appstore.cloud.aws.service.AwsS3Service;
import org.bitvault.appstore.cloud.constant.APIConstants;
import org.bitvault.appstore.cloud.constant.Constants;
import org.bitvault.appstore.cloud.constant.DbConstant;
import org.bitvault.appstore.cloud.constant.ErrorCode;
import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.dto.AppRateReviewDto;
import org.bitvault.appstore.cloud.dto.AppStatisticsDto;
import org.bitvault.appstore.cloud.dto.Application;
import org.bitvault.appstore.cloud.dto.MobileUser;
import org.bitvault.appstore.cloud.dto.MobileUserAppDto;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.bitvault.appstore.cloud.exception.BitVaultResponse;
import org.bitvault.appstore.cloud.model.Account;
import org.bitvault.appstore.cloud.model.AppDownloadUrl;
import org.bitvault.appstore.cloud.security.SecurityConstants;
import org.bitvault.appstore.cloud.user.common.GeneralResponseModel;
import org.bitvault.appstore.cloud.user.dev.service.AccountService;
import org.bitvault.appstore.cloud.user.dev.service.ReviewReplyService;
import org.bitvault.appstore.cloud.utils.Response;
import org.bitvault.appstore.cloud.utils.Utility;
import org.bitvault.appstore.commons.application.service.AppApplicationService;
import org.bitvault.appstore.commons.application.service.AppDownloadService;
import org.bitvault.appstore.commons.application.service.AppRateReviewService;
import org.bitvault.appstore.commons.application.service.AppStatisticsService;
import org.bitvault.appstore.commons.application.service.ApplicationService;
import org.bitvault.appstore.mobile.service.MobileUserAppService;
import org.bitvault.appstore.mobile.service.MobileUserAppStatusService;
import org.bitvault.appstore.mobile.service.MobileUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import antlr.Utils;

@RestController
@RequestMapping(value = APIConstants.MOBILE_API_BASE)
public class MobileController {
	private static final Logger logger = LoggerFactory.getLogger(MobileController.class);
	@Autowired
	AccountService accountService;

	@Autowired
	ApplicationService applicationService;

	@Autowired
	AppApplicationService appApplicationService;

	@Autowired
	MobileUserService mobileUserService;

	@Autowired
	AppDownloadService appDownloadService;

	@Autowired
	AppRateReviewService appRateReveiwService;

	@Autowired
	AppStatisticsService appStatisticsService;

	@Autowired
	AwsS3Service awsService;

	@Autowired
	MobileUserAppService mobileUserAppService;
	@Autowired
	ReviewReplyService reviewReplyService;
	@Autowired
	MobileUserAppStatusService mobileUserAppStatusService;

	public static final String filePath = "raw/";

	@RequestMapping(value = "/getAppHomeData", method = RequestMethod.GET)
	public String getAppHomeData() {
		String jsonString = readfile("home.txt", filePath);
		return jsonString;
	}

	@RequestMapping(value = "/getAppDetail", method = RequestMethod.POST)
	public String getAppDetail(@RequestParam String package_name) {
		String jsonString = readfile("appdetail.txt", filePath);
		return jsonString;
	}

	@RequestMapping(value = "/getPermission", method = RequestMethod.GET)
	public String getPermission() {
		String jsonString = readfile("permission.txt", filePath);
		return jsonString;
	}

	@RequestMapping(value = "/getAppFullDesc", method = RequestMethod.POST)
	public String getAppFullDesc(@RequestParam String package_name) {
		String jsonString = readfile("appfulldescription.txt", filePath);
		return jsonString;
	}

	@RequestMapping(value = "/getAppsByCategoryId", method = RequestMethod.POST)
	public String getApkfile(@RequestParam String category_id) {
		String jsonString = readfile("category.txt", filePath);
		return jsonString;
	}

	@RequestMapping(value = "/searchApp", method = RequestMethod.POST)
	public String searchApp(@RequestParam String search_query) {
		String jsonString = readfile("search.txt", filePath);
		return jsonString;
	}

	@RequestMapping(value = "/getAppAllReviews", method = RequestMethod.POST)
	public String getAppAllReviews(@RequestParam String package_name) {
		String jsonString = readfile("allreviews.txt", filePath);
		return jsonString;
	}

	@RequestMapping(value = "/saveaccount", method = RequestMethod.POST)
	public Account getAppAllReviews(@RequestBody Account account) {
		try {
			// account = accountService.saveAccountDetail(account);
			return account;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@RequestMapping(value = APIConstants.REGISTER_MOBILE, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response> saveMobile(@RequestBody MobileUser mobileUser) {
		Response response = new Response();

		if (!Utility.isStringEmpty(mobileUser.getPublicAdd())) {
			try {
				logger.info("Save MobileUser method calling");
				MobileUser mobileUserDTO = mobileUserService.saveMobileUser(mobileUser);
				logger.info("MobileUser saved successfully");
				response.setResult(mobileUserDTO);
				// response.setType(APIConstants.PUBLSHED_APPLICATION_BY_PACKAGENAME);
				response.setStatus(Constants.SUCCESS);
				return new ResponseEntity<Response>(response, HttpStatus.OK);
			} catch (BitVaultException e) {
				logger.info("Error occured during saving MobileUser");
				response.setResult(new BitVaultResponse(e));
				// response.setType(APIConstants.TYPE_ERROR);
				response.setStatus(Constants.FAILED);
				return new ResponseEntity<Response>(response, HttpStatus.OK);

			}
		} else {
			response.setResult(new BitVaultResponse(
					new BitVaultException(ErrorMessageConstant.FIELD_IS_EMPTY, ErrorCode.FIELD_IS_EMPTY_CODE)));
			// response.setType(APIConstants.TYPE_ERROR);
			response.setStatus(Constants.FAILED);
			return new ResponseEntity<Response>(response, HttpStatus.EXPECTATION_FAILED);
		}

	}

	@RequestMapping(value = APIConstants.GET_MOBILE_USER_BY_PUBLICADD, method = RequestMethod.POST)
	public ResponseEntity<Response> getMobileUser(@RequestParam String publicAdd) {
		Response response = new Response();

		if (!Utility.isStringEmpty(publicAdd)) {
			try {
				MobileUser mobileUserDTO = mobileUser(publicAdd);
				response.setResult(mobileUserDTO);
				// response.setType(APIConstants.PUBLSHED_APPLICATION_BY_PACKAGENAME);
				response.setStatus(Constants.SUCCESS);
				return new ResponseEntity<Response>(response, HttpStatus.OK);
			} catch (BitVaultException e) {
				response.setResult(new BitVaultResponse(e));
				// response.setType(APIConstants.TYPE_ERROR);
				response.setStatus(Constants.FAILED);
				return new ResponseEntity<Response>(response, HttpStatus.EXPECTATION_FAILED);

			}
		} else {
			response.setResult(new BitVaultResponse(
					new BitVaultException(ErrorMessageConstant.FIELD_IS_EMPTY, ErrorCode.FIELD_IS_EMPTY_CODE)));
			// response.setType(APIConstants.TYPE_ERROR);
			response.setStatus(Constants.FAILED);
			return new ResponseEntity<Response>(response, HttpStatus.EXPECTATION_FAILED);
		}

	}

	@RequestMapping(value = APIConstants.FIND_ALL_MOBILE_USER, method = RequestMethod.GET)
	public ResponseEntity<Response> findAllMobileUser() {
		Response response = new Response();

		try {
			List<MobileUser> mobileUserDTOList = mobileUserService.findAllMobileUser();
			response.setResult(mobileUserDTOList);
			// response.setType(APIConstants.PUBLSHED_APPLICATION_BY_PACKAGENAME);
			response.setStatus(Constants.SUCCESS);
			return new ResponseEntity<Response>(response, HttpStatus.OK);
		} catch (BitVaultException e) {
			response.setResult(new BitVaultResponse(e));
			// response.setType(APIConstants.TYPE_ERROR);
			response.setStatus(Constants.FAILED);
			return new ResponseEntity<Response>(response, HttpStatus.EXPECTATION_FAILED);

		}

	}

	@RequestMapping(value = APIConstants.GET_APK_URL, method = RequestMethod.GET)
	public ResponseEntity<GeneralResponseModel> getAppUrl(@RequestParam String packageName,
			@RequestParam String publicAdd) {
		GeneralResponseModel response = null;

		if (!Utility.isStringEmpty(publicAdd) && !Utility.isStringEmpty(packageName)) {
			try {
				// MobileUser mobileUser =
				// mobileUserService.findMobileUserByPublicAddress(publicAdd);
				String appUrl = applicationService.findAppApkUrlByPackageName(packageName);

				// download file , then encrypt and save in local

				String tempFilepath = "";

				String mobileUserPublicKey = publicAdd;

				URL url = null;
				try {
					url = new URL(appUrl);
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				String fileName = String.format(url.getFile().substring(url.getFile().lastIndexOf("/") + 1));

				if (mobileUserPublicKey.equalsIgnoreCase("anuj")) {
					mobileUserPublicKey = SecurityConstants.BTC_PUBLIC_KEY;
				}

				tempFilepath = Constants.FILEPATH + mobileUserPublicKey + "/" + fileName;

				InputStream inputStream = awsService.getS3Object(appUrl);

				Utility.encryptSymKeyAndWrite(mobileUserPublicKey, tempFilepath, inputStream);

				System.out.println("file generated");

				AppDownloadUrl appDownloadUrl = appDownloadService.saveAppDownloadUrl(tempFilepath, publicAdd);
				response = GeneralResponseModel.of(Constants.SUCCESS,
						appDownloadUrl.populateAppDownloadURLDTO(appDownloadUrl));

				System.out.println("Success");

				return new ResponseEntity<GeneralResponseModel>(response, HttpStatus.OK);
			} catch (BitVaultException e) {

				logger.info("Error while generating download key");

				response = GeneralResponseModel.of(Constants.FAILED, new BitVaultResponse(e));

				return new ResponseEntity<GeneralResponseModel>(response, HttpStatus.OK);

			}
		} else {
			response = GeneralResponseModel.of(Constants.FAILED,
					new BitVaultResponse(ErrorMessageConstant.FIELD_IS_EMPTY));

			return new ResponseEntity<GeneralResponseModel>(response, HttpStatus.OK);
		}
	}

	@RequestMapping(value = APIConstants.DOWNLOAD_APK + "/{urlId}", method = RequestMethod.GET)
	public ResponseEntity<?> doDownload(@PathVariable String urlId, HttpServletRequest request,
			HttpServletResponse response) {
		GeneralResponseModel customResponse = null;

		if (!Utility.isStringEmpty(urlId)) {
			try {

				AppDownloadUrl appDownloadUrl = appDownloadService.findAppDownloadById(urlId);
				if (null != appDownloadUrl) {

					logger.info("Start downloading file");

					if (!mobileUserService.downloadApkFile(appDownloadUrl.getUrl(), request, response,
							appDownloadUrl.getPublicKey())) {
						throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED,
								ErrorCode.SOME_ERROR_OCCURED_CODE);
					} else {
						appDownloadService.deleteAppDownloadById(appDownloadUrl.getUrlId());
					}
				}
			} catch (BitVaultException e) {

				logger.info("Error in downloading file");

				customResponse = GeneralResponseModel.of(Constants.FAILED, new BitVaultResponse(e));

				return new ResponseEntity<GeneralResponseModel>(customResponse, HttpStatus.OK);

			}
			return new ResponseEntity<>("Ok", HttpStatus.OK);
		} else {
			customResponse = GeneralResponseModel.of(Constants.FAILED,
					new BitVaultResponse(ErrorMessageConstant.FIELD_IS_EMPTY));

			return new ResponseEntity<GeneralResponseModel>(customResponse, HttpStatus.OK);
		}
	}

	public String readfile(String fileName, String filePath) {
		String everything = null;
		FileInputStream inputStream = null;
		try {
			inputStream = new FileInputStream(filePath + fileName);

			everything = IOUtils.toString(inputStream);
			inputStream.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return everything;

	}

	@RequestMapping(value = APIConstants.SAVE_RATE_REVIEW, method = RequestMethod.POST)
	public ResponseEntity<GeneralResponseModel> saveRateReview(@RequestParam Integer applicationId,
			@RequestParam Integer appRating, @RequestParam(required = false) Integer appRateReviewId,
			@RequestParam String appReview, @RequestParam String publicAddress) {

		GeneralResponseModel customResponse = null;
		float averageRating;
		AppRateReviewDto appResponseRateReviewDto = null;

		if (!Utility.isIntegerEmpty(applicationId) && !Utility.isIntegerEmpty(appRating)
				&& !Utility.isStringEmpty(publicAddress)) {
			try {

				Application appDto = applicationService.findApplicationByAppId(applicationId);
				MobileUser mobileUser = mobileUser(publicAddress);

				if (null == appDto) {
					throw new BitVaultException(ErrorMessageConstant.REVIEW_NOT_PERMITTED);
				}
				org.bitvault.appstore.cloud.dto.AppApplication appApplicationDto = appApplicationService
						.findApplicationByAppId(applicationId);
				appResponseRateReviewDto = appRateReveiwService
						.findAppRateReviewByMobileUserId(mobileUser.getMobileUserId(), applicationId);
				if (appRating > 5) {
					appRating = 5;
				}
				if (null != appResponseRateReviewDto) {
					// AppReviewReply appReviewReply = reviewReplyService
					// .findByappRateReviewId(appResponseRateReviewDto.getAppRateReviewId());
					// if (appReviewReply != null) {
					// throw new
					// BitVaultException(ErrorMessageConstant.INVALID_REQUEST);
					// }
					//

					appResponseRateReviewDto.setAppRating(appRating);
					appResponseRateReviewDto.setAppReview(appReview);
					appResponseRateReviewDto = appRateReveiwService.saveRateReview(appResponseRateReviewDto,
							appApplicationDto, mobileUser);

				} else {
					appResponseRateReviewDto = new AppRateReviewDto();
					appResponseRateReviewDto.setApplicationId(applicationId);
					appResponseRateReviewDto.setAppRating(appRating);
					appResponseRateReviewDto.setAppReview(appReview);
					appResponseRateReviewDto = appRateReveiwService.saveRateReview(appResponseRateReviewDto,
							appApplicationDto, mobileUser);
				}

				if (appResponseRateReviewDto != null) {
					averageRating = appRateReveiwService.getAverageRating(appDto.getApplicationId());

					if (averageRating != 0.0) {
						appApplicationService.updateAverageRating(appDto.getApplicationId(), averageRating);
						// appDto.setAverageRating(averageRating);
						// org.bitvault.appstore.cloud.model.Application
						// application =
						// appDto.populateApplication(appDto);
						// AppApplication appApplication =
						// application.populateAppApplicationByApplication(application);
						// applicationService.saveApllication(application);
						//
						// appApplicationService
						// .saveAppApllication(appApplication.populateAppApplicationDTO(appApplication));
					}
				}

				customResponse = GeneralResponseModel.of(Constants.SUCCESS, appResponseRateReviewDto);
			} catch (BitVaultException e) {
				e.printStackTrace();
				customResponse = GeneralResponseModel.of(Constants.FAILED, new BitVaultResponse(e));
			}
		} else {
			customResponse = GeneralResponseModel.of(Constants.FAILED, new BitVaultResponse(
					new BitVaultException(ErrorMessageConstant.FIELD_IS_EMPTY, ErrorCode.FIELD_IS_EMPTY_CODE)));
		}
		return new ResponseEntity<GeneralResponseModel>(customResponse, HttpStatus.OK);
	}

	@RequestMapping(value = APIConstants.GET_STAR_RATING + "/{application_id}", method = RequestMethod.GET)
	public ResponseEntity<GeneralResponseModel> getStarRating(@PathVariable("application_id") Integer applicationID) {

		GeneralResponseModel customResponse = null;
		float averageRating;
		AppRateReviewDto appResponseRateReviewDto = null;

		if (!Utility.isIntegerEmpty(applicationID)) {
			try {
				Application appDto = applicationService.findApplicationByAppId(applicationID);

				if (null == appDto) {
					throw new BitVaultException(ErrorMessageConstant.RESULT_NOT_FOUND);
				}

				appRateReveiwService.findStarRatingByApplicationId(appDto.getApplicationId());

				customResponse = GeneralResponseModel.of(Constants.SUCCESS,
						appRateReveiwService.findStarRatingByApplicationId(appDto.getApplicationId()));
			} catch (BitVaultException e) {
				e.printStackTrace();
				customResponse = GeneralResponseModel.of(Constants.FAILED, new BitVaultResponse(e));
			}
		} else {
			customResponse = GeneralResponseModel.of(Constants.FAILED, new BitVaultResponse(
					new BitVaultException(ErrorMessageConstant.FIELD_IS_EMPTY, ErrorCode.FIELD_IS_EMPTY_CODE)));
		}
		return new ResponseEntity<GeneralResponseModel>(customResponse, HttpStatus.OK);
	}

	@RequestMapping(value = APIConstants.RATE_REVIEW_LIST, method = RequestMethod.GET)
	public ResponseEntity<?> rateReviewList(@RequestParam Integer applicationId,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size,
			@RequestParam(defaultValue = Constants.UPDATED_AT) String orderBy) {

		Response response = null;
		GeneralResponseModel genralResponse = null;
		Map<String, Object> allAppMap = null;

		if (page > 0) {
			page = page - 1;
		}

		try {
			if (Utility.isIntegerEmpty(applicationId)) {
				throw new BitVaultException(ErrorMessageConstant.FIELD_IS_EMPTY);
			}
			Application app = applicationService.findApplicationByAppId(applicationId);
			if (app == null) {
				throw new BitVaultException(ErrorMessageConstant.RESULT_NOT_FOUND);
			}
			response = new Response();
			allAppMap = appRateReveiwService.findAppRateReviewByAppId(applicationId, page, size, DbConstant.ASC,
					orderBy);
			logger.info("RateReview list fetched successfully");
			response.setResult(allAppMap.get("appRateReviewDtoList"));
			// response.setType(APIConstants.LIST_OF_ALL_APPLICATION);
			response.setStatus(Constants.SUCCESS);
			int totalPages = (int) allAppMap.get(Constants.TOTAL_PAGES);
			int totalRecord = ((Long) allAppMap.get(Constants.TOTAL_RECORDS)).intValue();
			int pageSize = (int) allAppMap.get(Constants.SIZE);
			response.setSize(pageSize);
			response.setTotalPages(totalPages);
			response.setTotalRecords(totalRecord);
			response.setSort(orderBy);
			return new ResponseEntity<Response>(response, HttpStatus.OK);

		} catch (BitVaultException e) {
			genralResponse = GeneralResponseModel.of(Constants.FAILED, new BitVaultResponse(e));

			return new ResponseEntity<GeneralResponseModel>(genralResponse, HttpStatus.OK);

		}

	}

	@PostMapping(value = APIConstants.UPDATE_INTSALL_UNISTALL_DOWNLOAD)
	public ResponseEntity<GeneralResponseModel> updateApkOnDeviceAction(@RequestParam String packageName,
			@RequestParam String action, @RequestParam String publicAddress) {

		GeneralResponseModel customResponse = null;

		if (!Utility.isStringEmpty(packageName) && !Utility.isStringEmpty(action)
				&& !Utility.isStringEmpty(publicAddress)) {

			try {
				MobileUser mobileUser = mobileUser(publicAddress);
				if (action.equalsIgnoreCase(DbConstant.INSTALL) || action.equalsIgnoreCase(DbConstant.UNINSTALL)
						|| action.equalsIgnoreCase(DbConstant.DOWNLOAD)) {
					logger.info("findApplicationByPackageName method calling");
					Application appDto = applicationService.findApplicationByPackageName(packageName);
					logger.info("Application appDto fetched successfully");
					logger.info("findAppApplicationByPackageName method calling");
					org.bitvault.appstore.cloud.dto.AppApplication appAppDto = appApplicationService
							.findAppApplicationByPackageName(packageName);
					logger.info("AppApplication appDto fetched successfully");
					if (null == appDto || null == appAppDto) {
						throw new BitVaultException(ErrorMessageConstant.RESULT_NOT_FOUND);
					}
					if (appDto.getTotalInstall().intValue() == 0 && action.equalsIgnoreCase(DbConstant.UNINSTALL)) {
						throw new BitVaultException(ErrorMessageConstant.INVALID_REQUEST);

					}
					logger.info("SaveMobileUserAppStatus method calling");
					mobileUserAppStatusService.saveMobileUserAppStatus(appAppDto, mobileUser, action, "na");
					logger.info("MobileUserAppStatus saved successfully");
					MobileUserAppDto mobileUserAppDto = new MobileUserAppDto();
					mobileUserAppDto.setApplication(appAppDto);
					mobileUserAppDto.setMobileUser(mobileUser);
					mobileUserAppDto.setAppPrice(appAppDto.getAppPrice());
					mobileUserAppDto.setAppTxnId("na");
					mobileUserAppDto.setStatus(action);
					logger.info("SaveAppStatistics method calling");
					mobileUserAppService.saveMobileUserApp(mobileUserAppDto);
					logger.info("MobileUserApp saved successfully");
					logger.info("SaveAppStatistics method calling");
					AppStatisticsDto appStatisticsDto = new AppStatisticsDto();
					logger.info("AppStatistics saved successfully");
					appStatisticsDto.setApplication(appAppDto);
					appStatisticsDto = appStatisticsService.saveAppStatistics(appStatisticsDto, action);
					customResponse = GeneralResponseModel.of(Constants.SUCCESS, appStatisticsDto);

				} else {
					throw new BitVaultException(ErrorMessageConstant.INVALID_REQUEST);
				}
			} catch (BitVaultException e) {
				e.printStackTrace();
				customResponse = GeneralResponseModel.of(Constants.FAILED, new BitVaultResponse(e));
			}
		} else {
			customResponse = GeneralResponseModel.of(Constants.FAILED, new BitVaultResponse(
					new BitVaultException(ErrorMessageConstant.FIELD_IS_EMPTY, ErrorCode.FIELD_IS_EMPTY_CODE)));
		}
		return new ResponseEntity<GeneralResponseModel>(customResponse, HttpStatus.OK);
	}

	private MobileUser mobileUser(String publicAddress) {

		MobileUser mobileUser = mobileUserService.findMobileUserByPublicAddress(publicAddress);
		if (null == mobileUser) {
			throw new BitVaultException(ErrorMessageConstant.INVALID_PUBLIC_ADDRESS);
		}
		return mobileUser;
	}

	@PostMapping(value = APIConstants.DELETE_REVIEW)
	public ResponseEntity<GeneralResponseModel> deleteReview(@RequestParam Integer appRateReviewId,
			@RequestParam String publicAddress) {
		GeneralResponseModel customResponse = null;
		if (!Utility.isIntegerEmpty(appRateReviewId) & !Utility.isStringEmpty(publicAddress)) {
			try {
				MobileUser mobileUser = mobileUser(publicAddress);

				org.bitvault.appstore.cloud.dto.AppRateReviewDto appRateReviewDto = appRateReveiwService
						.findAppRateReviewByAppRateReviewId(appRateReviewId);
				if (null == appRateReviewDto) {
					throw new BitVaultException(ErrorMessageConstant.RESULT_NOT_FOUND);
				}
				if (!mobileUser.getMobileUserId().equals(appRateReviewDto.getMobileUserId())) {
					throw new BitVaultException(ErrorMessageConstant.INVALID_REQUEST);

				}
				appRateReveiwService.deleteReview(appRateReviewId);
				logger.info("review deleted successfully");
				float averageRating = appRateReveiwService.getAverageRating(appRateReviewDto.getApplicationId());

				if (averageRating != 0.0) {

					appApplicationService.updateAverageRating(appRateReviewDto.getApplicationId(), averageRating);
					logger.info("after deleting a review ,average rating get updated");

				}

				customResponse = GeneralResponseModel.of(Constants.SUCCESS, new BitVaultResponse(Constants.DELETE));

			} catch (BitVaultException e) {
				logger.info("Error occured while deleting review");
				e.printStackTrace();
				customResponse = GeneralResponseModel.of(Constants.FAILED, new BitVaultResponse(e));
			}
		} else {
			customResponse = GeneralResponseModel.of(Constants.FAILED, new BitVaultResponse(
					new BitVaultException(ErrorMessageConstant.FIELD_IS_EMPTY, ErrorCode.FIELD_IS_EMPTY_CODE)));
		}
		return new ResponseEntity<GeneralResponseModel>(customResponse, HttpStatus.OK);
	}

	@PostMapping(value = APIConstants.UNINSTALL_MULTI_APK)
	public ResponseEntity<GeneralResponseModel> uinstallMultiApk(@RequestParam List<String> packageNameList,
			@RequestParam(defaultValue = "Uninstall") String action, @RequestParam String publicAddress) {

		GeneralResponseModel customResponse = null;
		List<AppStatisticsDto> appStatisticsDtoList = new ArrayList<AppStatisticsDto>();
		;

		if (!Utility.isCollectionEmpty(packageNameList) && !Utility.isStringEmpty(action)
				&& !Utility.isStringEmpty(publicAddress)) {

			try {
				MobileUser mobileUser = mobileUser(publicAddress);
				if (action.equalsIgnoreCase(DbConstant.UNINSTALL) || action.equalsIgnoreCase(DbConstant.DOWNLOAD)) {
					for (String packageName : packageNameList) {
						logger.info("findApplicationByPackageName method calling");
						Application appDto = applicationService.findApplicationByPackageName(packageName);
						logger.info("Application appDto fetched successfully");
						logger.info("findAppApplicationByPackageName method calling");
						org.bitvault.appstore.cloud.dto.AppApplication appAppDto = appApplicationService
								.findAppApplicationByPackageName(packageName);
						logger.info("AppApplication appDto fetched successfully");
						if (null == appDto || null == appAppDto) {
							throw new BitVaultException(ErrorMessageConstant.RESULT_NOT_FOUND);
						}
						if (appDto.getTotalInstall().intValue() == 0 && action.equalsIgnoreCase(DbConstant.UNINSTALL)) {
							throw new BitVaultException(ErrorMessageConstant.INVALID_REQUEST);

						}
						logger.info("SaveMobileUserAppStatus method calling");
						mobileUserAppStatusService.saveMobileUserAppStatus(appAppDto, mobileUser, action, "na");
						logger.info("MobileUserAppStatus saved successfully");
						MobileUserAppDto mobileUserAppDto = new MobileUserAppDto();
						mobileUserAppDto.setApplication(appAppDto);
						mobileUserAppDto.setMobileUser(mobileUser);
						mobileUserAppDto.setAppPrice(appAppDto.getAppPrice());
						mobileUserAppDto.setAppTxnId("na");
						mobileUserAppDto.setStatus(action);
						logger.info("SaveMobileUserApp method calling");
						mobileUserAppService.saveMobileUserApp(mobileUserAppDto);
						logger.info("MobileUserApp saved successfully");
						AppStatisticsDto appStatisticsDto = new AppStatisticsDto();
						appStatisticsDto.setApplication(appAppDto);
						logger.info("SaveAppStatistics method calling");
						appStatisticsDto = appStatisticsService.saveAppStatistics(appStatisticsDto, action);
						logger.info("AppStatistics saved successfully");
						appStatisticsDtoList.add(appStatisticsDto);
						customResponse = GeneralResponseModel.of(Constants.SUCCESS, appStatisticsDtoList);

					}
				} else {
					throw new BitVaultException(ErrorMessageConstant.INVALID_REQUEST);
				}
			} catch (BitVaultException e) {
				logger.info("Error occured during uninstall multiApk");
				e.printStackTrace();
				customResponse = GeneralResponseModel.of(Constants.FAILED, new BitVaultResponse(e));
			}
		} else {
			customResponse = GeneralResponseModel.of(Constants.FAILED, new BitVaultResponse(
					new BitVaultException(ErrorMessageConstant.FIELD_IS_EMPTY, ErrorCode.FIELD_IS_EMPTY_CODE)));
		}
		return new ResponseEntity<GeneralResponseModel>(customResponse, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET, value = APIConstants.GET_PAYMENT_ADDRESS)
	public ResponseEntity<GeneralResponseModel> getPaymentDetails(HttpServletRequest request,
			@PathVariable String publicAddress) {

		GeneralResponseModel response ;

		try {
			
			if(Utility.isStringEmpty(publicAddress)) {
				throw new BitVaultException(ErrorMessageConstant.FIELD_IS_EMPTY);
			}
			
			MobileUser mobileUser = mobileUserService.findMobileUserByPublicAddress(publicAddress);
			
			if(mobileUser == null) {
				throw new BitVaultException(ErrorMessageConstant.USER_NOT_FOUND);
			}
			
			response = GeneralResponseModel.of(Constants.SUCCESS ,mobileUserAppService.getPaymentDetails(publicAddress));
			
		} catch (BitVaultException e) {
			
			response = GeneralResponseModel.of(Constants.FAILED ,new BitVaultResponse(e));
			
		}

		return new ResponseEntity<GeneralResponseModel>(response, HttpStatus.OK);
	}
}
