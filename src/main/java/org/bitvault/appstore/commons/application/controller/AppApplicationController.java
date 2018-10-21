package org.bitvault.appstore.commons.application.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.bitvault.appstore.cloud.constant.APIConstants;
import org.bitvault.appstore.cloud.constant.Constants;
import org.bitvault.appstore.cloud.constant.DbConstant;
import org.bitvault.appstore.cloud.constant.ErrorCode;
import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.dto.AppApplication;
import org.bitvault.appstore.cloud.dto.AppApplicationDetailDto;
import org.bitvault.appstore.cloud.dto.AppCategory;
import org.bitvault.appstore.cloud.dto.AppDetail;
import org.bitvault.appstore.cloud.dto.AppImage;
import org.bitvault.appstore.cloud.dto.AppInfoDto;
import org.bitvault.appstore.cloud.dto.AppStatisticsDto;
import org.bitvault.appstore.cloud.dto.Application;
import org.bitvault.appstore.cloud.dto.ApplicationTypeDto;
import org.bitvault.appstore.cloud.dto.AverageRatingChartDto;
import org.bitvault.appstore.cloud.dto.ChartStatsDto;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.bitvault.appstore.cloud.exception.BitVaultResponse;
import org.bitvault.appstore.cloud.model.DevUser;
import org.bitvault.appstore.cloud.user.common.GeneralResponseModel;
import org.bitvault.appstore.cloud.user.dev.service.DevUserService;
import org.bitvault.appstore.cloud.utils.APKDetail;
import org.bitvault.appstore.cloud.utils.Response;
import org.bitvault.appstore.cloud.utils.Utility;
import org.bitvault.appstore.cloud.validator.ApkValidator;
import org.bitvault.appstore.commons.application.service.AppApplicationService;
import org.bitvault.appstore.commons.application.service.AppCategoryService;
import org.bitvault.appstore.commons.application.service.AppDetailService;
import org.bitvault.appstore.commons.application.service.AppHistoryService;
import org.bitvault.appstore.commons.application.service.AppImageService;
import org.bitvault.appstore.commons.application.service.AppRateReviewService;
import org.bitvault.appstore.commons.application.service.AppStatisticsService;
import org.bitvault.appstore.commons.application.service.AppTypeService;
import org.bitvault.appstore.commons.application.service.ApplicationService;
import org.bitvault.appstore.mobile.service.MobileUserAppService;
import org.bitvault.appstore.mobile.service.MobileUserAppStatusService;
import org.bitvault.appstore.mobile.service.MobileUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = APIConstants.DEV_API_BASE)
public class AppApplicationController {

	public static final Logger logger = LoggerFactory.getLogger(AppApplicationController.class);

	@Autowired
	AppApplicationService appAppService;

	@Autowired
	ApplicationService appService;

	@Autowired
	DevUserService devUserService;

	@Autowired
	AppCategoryService appCategoryService;

	@Autowired
	AppTypeService appTypeService;

	@Autowired
	AppDetailService appDetailService;

	@Autowired
	AppImageService appImageService;

	@Autowired
	MobileUserAppService mobileUserAppService;

	@Autowired
	AppRateReviewService appRateReviewService;

	@Autowired
	AppStatisticsService appStatisticsService;

	@Autowired
	AppHistoryService appHistoryService;

	@Autowired
	MobileUserAppStatusService mobileUserAppStatus;

	@Autowired
	MobileUserService mobileUserService;

	@RequestMapping(value = APIConstants.LIST_OF_ALL_APPLICATION, method = RequestMethod.GET)
	public ResponseEntity<?> listOfAllApplication(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size,
			@RequestParam(defaultValue = Constants.UPDATED_AT) String orderBy) {

		Response response = null;
		GeneralResponseModel genralResponse = null;
		Map<String, Object> allAppMap = null;

		if (page > 0) {
			page = page - 1;
		}

		try {
			response = new Response();
			logger.info("listOfAllAppApplication method calling");
			allAppMap = appAppService.listOfAllAppApplication(page, size, DbConstant.ASC, orderBy);
			logger.info("Map fetched successfully");
			response.setResult(allAppMap.get("appList"));
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
			logger.info("Error occured during list Fetching");
			genralResponse = GeneralResponseModel.of(Constants.FAILED, new BitVaultResponse(e));

			return new ResponseEntity<GeneralResponseModel>(genralResponse, HttpStatus.OK);

		}

	}

	@RequestMapping(value = APIConstants.APPLICATION_BY_PACKAGENAME, method = RequestMethod.GET)
	public ResponseEntity<?> findApplicationByPackageName(@RequestParam String packageName) {
		Response response = null;
		GeneralResponseModel genralResponse = null;
		if (!Utility.isStringEmpty(packageName)) {
			try

			{
				logger.info("findAppApplicationByPackageName method calling...");
				AppApplication app = appAppService.findAppApplicationByPackageName(packageName);
				logger.info("AppApplication fetched successfully...");
				genralResponse = GeneralResponseModel.of(Constants.SUCCESS, app);

				return new ResponseEntity<GeneralResponseModel>(genralResponse, HttpStatus.OK);
			} catch (BitVaultException e) {
				logger.info("Error occured during list fetching" + e.getMessage());
				genralResponse = GeneralResponseModel.of(Constants.FAILED, new BitVaultResponse(e));

				return new ResponseEntity<GeneralResponseModel>(genralResponse, HttpStatus.OK);

			}
		} else {
			genralResponse = GeneralResponseModel.of(Constants.FAILED,
					new BitVaultResponse(ErrorMessageConstant.FIELD_IS_EMPTY));
			return new ResponseEntity<GeneralResponseModel>(genralResponse, HttpStatus.OK);

		}
	}

	@RequestMapping(value = APIConstants.FIND_APPLICATION_BY_USERID, method = RequestMethod.GET)
	public ResponseEntity<?> findApplicationByUserId(HttpServletRequest request,
			@RequestParam(defaultValue = Constants.STATUS) List<String> status,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size,
			@RequestParam(defaultValue = Constants.UPDATED_AT) String orderBy,
			@RequestParam(defaultValue = DbConstant.DESC) String direction) {
		Response response = null;
		GeneralResponseModel genralResponse = null;
		Map<String, Object> allAppMap = null;
		if (page > 0) {
			page = page - 1;
		}
		try {
			String userId = getUserIdByAuthRequest(request);

			if (!Utility.isStringEmpty(userId)) {

				if (status.get(0).equals(Constants.STATUS)) {
					logger.info("findAppApplicationByUserId method calling");
					allAppMap = appAppService.findAppApplicationByUserId(userId, page, size, direction, orderBy);
					logger.info("Map fetched successfully");
				} else {
					logger.info("findAppApplicationByUserIdAndStatus method calling");
					allAppMap = appAppService.findAppApplicationByUserIdAndStatus(userId, status, page, size, direction,
							orderBy);
					logger.info("");
				}

				response = new Response();
				response.setResult(allAppMap.get("appList"));
				// response.setType(APIConstants.FIND_APPLICATION_BY_USERID);
				response.setStatus(Constants.SUCCESS);
				int totalPages = (int) allAppMap.get(Constants.TOTAL_PAGES);
				int totalRecord = ((Long) allAppMap.get(Constants.TOTAL_RECORDS)).intValue();
				int pageSize = (int) allAppMap.get(Constants.SIZE);
				response.setSize(pageSize);
				response.setTotalPages(totalPages);
				response.setTotalRecords(totalRecord);
				response.setSort(orderBy);
			}
			return new ResponseEntity<Response>(response, HttpStatus.OK);
		} catch (BitVaultException e) {
			genralResponse = GeneralResponseModel.of(Constants.FAILED, new BitVaultResponse(e));

			return new ResponseEntity<GeneralResponseModel>(genralResponse, HttpStatus.OK);

		}

	}

	@RequestMapping(value = APIConstants.FIND_APPLICATION_BY_CATEGORY, method = RequestMethod.GET)
	public ResponseEntity<?> findApplicationByCategory(@RequestParam List<Integer> categoryIdList,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size,
			@RequestParam(defaultValue = Constants.UPDATED_AT) String orderBy,
			@RequestParam(defaultValue = DbConstant.DESC) String direction) {
		Response response = null;
		GeneralResponseModel genralResponse = null;
		Map<String, Object> allAppMap = null;

		if (page > 0) {
			page = page - 1;
		}
		if (!Utility.isCollectionEmpty(categoryIdList)) {
			try

			{
				logger.info("findAppApplicationByCategory method calling...");
				allAppMap = appAppService.findAppApplicationByCategory(categoryIdList, page, size, direction, orderBy);
				logger.info("Map fetched successfully");
				response = new Response();
				response.setResult(allAppMap.get("appList"));
				// response.setType(APIConstants.FIND_APPLICATION_BY_CATEGORY);
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
				logger.info("Error occured during finding Application By Category fetching");
				genralResponse = GeneralResponseModel.of(Constants.FAILED, new BitVaultResponse(e));

				return new ResponseEntity<GeneralResponseModel>(genralResponse, HttpStatus.OK);

			}
		} else {
			genralResponse = GeneralResponseModel.of(Constants.FAILED,
					new BitVaultResponse(ErrorMessageConstant.FIELD_IS_EMPTY));

			return new ResponseEntity<GeneralResponseModel>(genralResponse, HttpStatus.OK);
		}

	}

	@RequestMapping(value = APIConstants.FIND_APPLICATION_BY_STATUS, method = RequestMethod.GET)
	public ResponseEntity<?> findApplicationByStatus(@RequestParam List<String> status,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size,
			@RequestParam(defaultValue = Constants.UPDATED_AT) String orderBy,
			@RequestParam(defaultValue = DbConstant.DESC) String direction) {
		Response response = null;
		GeneralResponseModel genralResponse = null;
		Map<String, Object> allAppMap = null;

		if (page > 0) {
			page = page - 1;
		}
		if (!Utility.isCollectionEmpty(status)) {
			try

			{   logger.info("findAppApplicationByCategory method calling...");
				allAppMap = appAppService.findAppApplicationByStatus(status, page, size, direction, orderBy);
				logger.info("Map fetched successfully");
				response = new Response();
				response.setResult(allAppMap.get("appList"));
				// response.setType(APIConstants.FIND_APPLICATION_BY_STATUS);
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
				logger.info("Error occured during finding application"+e.getMessage());
				genralResponse = GeneralResponseModel.of(Constants.FAILED, new BitVaultResponse(e));

				return new ResponseEntity<GeneralResponseModel>(genralResponse, HttpStatus.OK);

			}
		} else {
			genralResponse = GeneralResponseModel.of(Constants.FAILED,
					new BitVaultResponse(ErrorMessageConstant.FIELD_IS_EMPTY));

			return new ResponseEntity<GeneralResponseModel>(genralResponse, HttpStatus.OK);
		}

	}

	@RequestMapping(value = APIConstants.CHANGE_APP_STATUS + "/{appId}", method = RequestMethod.POST)
	public ResponseEntity<GeneralResponseModel> updateAppStatus(@RequestBody Map<String, String> requestBody,
			HttpServletRequest request, @PathVariable Integer appId) {
		GeneralResponseModel response = null;
		String requestStatus = null;
		String status = requestBody.get(Constants.STATUS);
		Map<String, String> errorMap = null;
		requestBody.put(Constants.UPLOADED_FROM, "Beta");
	
		String uploadedfrom = requestBody.get(Constants.UPLOADED_FROM);
		
		if (!Utility.isStringEmpty(status) && !Utility.isIntegerEmpty(appId)) {
			if (status.equalsIgnoreCase(DbConstant.PUBLISHED) || status.equalsIgnoreCase(DbConstant.UNPUBLISHED)) {
				try {
					DevUser devUser = getUserByAuthRequest(request);

					// status = DbConstant.REVIEW;
					AppApplication appAppDto = appAppService.findApplicationByAppId(appId);

					AppDetail appDetailDto = appDetailService.findApplicationDetailByAppIdAndVersion(
							appAppDto.getAppApplicationId(), appAppDto.getLatestVersionName(), "");
					// String userId = getUserIdByAuthRequest(request);
					CompareUser(devUser.getUserId(), appAppDto.getUserId());
					if (null == appAppDto) {
						throw new BitVaultException(ErrorMessageConstant.RESULT_NOT_FOUND);
					}

					if (null != appAppDto && !appAppDto.getStatus().equals(Constants.PENDING)) {
						if (status.equalsIgnoreCase(DbConstant.PUBLISHED)
								&& (appAppDto.getStatus().equals(Constants.DRAFT)
										|| appAppDto.getStatus().equals(Constants.BETA_DRAFT)
										|| appAppDto.getStatus().equals(Constants.ALPHA_DRAFT)
										|| appAppDto.getStatus().equals(Constants.UNPUBLISHED)
										|| appAppDto.getStatus().equals(Constants.BETA_UNPUBLISHED)
										|| appAppDto.getStatus().equals(Constants.ALPHA_UNPUBLISHED)
										)) {

							int imageCount = appImageService.getAppImageCountByType(DbConstant.TYPE_IMAGE,
									appAppDto.getAppApplicationId());
							int bannerCount = appImageService.getAppImageCountByType(DbConstant.TYPE_BANNER,
									appAppDto.getAppApplicationId());

							requestStatus = DbConstant.APP_PUBLISHED_REQUEST;
							AppInfoDto appInfoDto = new AppInfoDto();

							appInfoDto = appInfoDto.populateAppInfoDto(appDetailDto, appAppDto, appInfoDto);
							errorMap = appInfoDto.validateAppInfoDto(appInfoDto, appAppDto.getAppIconUrl(),
									appAppDto.getApkUrl(), imageCount, bannerCount);

							// if (null == appDetailDto) {
							// errorMap =
							// appInfoDto.fullShortDescriptionError();
							// } else {
							//
							// appInfoDto =
							// appInfoDto.populateAppInfoDto(appDetailDto,
							// appAppDto, appInfoDto);
							// errorMap =
							// appInfoDto.validateAppInfoDto(appInfoDto,
							// appAppDto.getAppIconUrl(),
							// appAppDto.getApkUrl(), imageCount, bannerCount);
							// }

							// check for version update and call notification server api
							// List<String> publicAddesslist =
							// checkForAppUpdateAndHitForNotification(appAppDto, appId);

							/*
							 * if (publicAddesslist != null && publicAddesslist.size() > 0) {
							 * NotificationData notificationData = new NotificationData();
							 * notificationData.setPublic_address(publicAddesslist);
							 * notificationData.setWeb_server_key(""); notificationData.setTag("");
							 * notificationData.setData(new
							 * NotificationAdditionalProrerties(appAppDto.getAppIconUrl(),
							 * appAppDto.getTitle(), appAppDto.getPackageName(), appId,
							 * appAppDto.getUpdatedAt(), appAppDto.getApkFilesize())); }
							 */

							if (null != errorMap && errorMap.size() > 0) {
								response = GeneralResponseModel.of(Constants.FAILED, errorMap);
								return new ResponseEntity<GeneralResponseModel>(response, HttpStatus.OK);

							}

						} else if (status.equalsIgnoreCase(DbConstant.UNPUBLISHED)
								&& (appAppDto.getStatus().equals(Constants.PUBLISHED)
										|| appAppDto.getStatus().equals(Constants.BETA_PUBLISHED)
								|| appAppDto.getStatus().equals(Constants.ALPHA_PUBLISHED))) {
							requestStatus = DbConstant.APP_UNPUBLISHED_REQUEST;

						} else {
							response = GeneralResponseModel.of(Constants.FAILED,
									new BitVaultResponse(ErrorMessageConstant.INVALID_REQUEST));
							return new ResponseEntity<GeneralResponseModel>(response, HttpStatus.OK);

						}
						
						
						status = uploadedfrom.equalsIgnoreCase(DbConstant.UPLOADED_FROM_PROD) ?  DbConstant.PENDING : 
							requestStatus.equalsIgnoreCase(DbConstant.APP_UNPUBLISHED_REQUEST) ? DbConstant.UNPUBLISHED : DbConstant.PUBLISHED;
						
						
						if(status.equalsIgnoreCase(DbConstant.UNPUBLISHED) && uploadedfrom.equalsIgnoreCase(DbConstant.UPLOADED_FROM_ALPHA)) {
							status = Constants.ALPHA_UNPUBLISHED ;
						}else if(status.equalsIgnoreCase(DbConstant.UNPUBLISHED) && uploadedfrom.equalsIgnoreCase(DbConstant.UPLOADED_FROM_BETA)) {
							status = Constants.BETA_UNPUBLISHED ;
						}else if(status.equalsIgnoreCase(DbConstant.PUBLISHED) && uploadedfrom.equalsIgnoreCase(DbConstant.UPLOADED_FROM_ALPHA)) {
							status = Constants.ALPHA_PUBLISHED ;
						}else if(status.equalsIgnoreCase(DbConstant.PUBLISHED) && uploadedfrom.equalsIgnoreCase(DbConstant.UPLOADED_FROM_BETA)) {
							status = Constants.BETA_PUBLISHED ;
						}
						
						
						Integer update = appAppService.updateAppStatus(status, appId);
						if (update > 0) {
							// Generate request when it is production
							if(uploadedfrom.equalsIgnoreCase(DbConstant.UPLOADED_FROM_PROD)) {
							appAppService.saveAppApllicationRequest(devUser.getUserId(), appId, requestStatus);
							}
							appDetailService.updateAppDetailStatus(status, appAppDto.getAppApplicationId(),
									appAppDto.getLatestVersionName());
							AppInfoDto appInfoDto = new AppInfoDto();
							appInfoDto = appInfoDto.populateAppInfoDtoToCompare(appDetailDto, appAppDto);
							appInfoDto.setStatus(status);
//							appInfoDto.setApplicationId(appId);
//							appInfoDto.setVersionName(appAppDto.getLatestVersionName());
//							appInfoDto.setVersionCode(appAppDto.getLatestVersionNo());
							appHistoryService.saveAppHistory(null, appInfoDto,
									appAppDto.populateAppApplication(appAppDto), "status");
							response = GeneralResponseModel.of(Constants.SUCCESS,
									new BitVaultResponse(Constants.SUCCESS_UPDATED));

						} else {
							response = GeneralResponseModel.of(Constants.FAILED,
									new BitVaultResponse(ErrorMessageConstant.UNABLE_TO_UPDATE));
						}

						return new ResponseEntity<GeneralResponseModel>(response, HttpStatus.OK);
					} else {
						throw new BitVaultException(ErrorMessageConstant.REQUEST_ACTION_PENDING);
					}
				} catch (BitVaultException e) {
					response = GeneralResponseModel.of(Constants.FAILED, new BitVaultResponse(e));

					return new ResponseEntity<GeneralResponseModel>(response, HttpStatus.OK);

				}
			} else {
				response = GeneralResponseModel.of(Constants.FAILED,
						new BitVaultResponse(ErrorMessageConstant.INVALID_APPSTATUS));
				return new ResponseEntity<GeneralResponseModel>(response, HttpStatus.OK);

			}
		} else {
			response = GeneralResponseModel.of(Constants.FAILED,
					new BitVaultResponse(ErrorMessageConstant.FIELD_IS_EMPTY));
			return new ResponseEntity<GeneralResponseModel>(response, HttpStatus.OK);

		}

	}

	@RequestMapping(value = APIConstants.SEARCH_APPLICATION, method = RequestMethod.GET)
	public ResponseEntity<?> searchApplication(@RequestParam String appName, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size,
			@RequestParam(defaultValue = DbConstant.APP_NAME) String orderBy) {
		Response response = new Response();
		GeneralResponseModel genralResponse = null;
		Map<String, Object> allAppMap = null;
		if (page > 0) {
			page = page - 1;
		}
		if (!Utility.isStringEmpty(appName)) {
			if (appName.length() > 2) {
				try

				{
					allAppMap = appAppService.findAppApplicationByAppName(appName, page, size,
							DbConstant.DESC.toString(), orderBy);
					response.setResult(allAppMap.get("appList"));
					// response.setType(APIConstants.SEARCH_APPLICATION);
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
		} else {
			genralResponse = GeneralResponseModel.of(Constants.FAILED,
					new BitVaultResponse(new BitVaultException(ErrorMessageConstant.FIELD_IS_EMPTY)));
			return new ResponseEntity<GeneralResponseModel>(genralResponse, HttpStatus.OK);

		}
		return new ResponseEntity<Response>(response, HttpStatus.OK);

	}

	// TODO: below code need to be optimised

	// @Secured({"ROLE_DEVELOPER","DEVELOPER"})
	// @PreAuthorize("hasPermission(#request, 'create')")
	@PostMapping(value = APIConstants.UPLOAD_APK)
	public ResponseEntity<GeneralResponseModel> uploadApkFile(@RequestParam MultipartFile apkFile,
			HttpServletRequest request, @RequestParam(defaultValue = "English") String language,
			@RequestParam(defaultValue = "1") Integer appTypeId, @RequestParam(required = false) Integer applicationId,
			@RequestParam(defaultValue = "1") Integer appCategoryId, @RequestParam String title,
			@RequestParam(defaultValue = DbConstant.UPLOADED_FROM_PROD) String uploadedFrom) {
		GeneralResponseModel response = null;
		Map<String, Object> apkDetailMap = null;
		Map<String, Object> validatorMap = null;
		Map<String, Object> responseMap = null;
		AppCategory appCategory = null;
		AppApplication appDto = null;
		APKDetail apkDetail = null;
		ApplicationTypeDto appTypeDto = null;
		String fileName = null, userId = "";
		Map<String, String> errorMap = null;
		Map<String, Object> errorMapResponse = null;
		AppDetail appDetailDto = null;
		try {
			
//			uploadedFrom = DbConstant.UPLOADED_FROM_BETA ;
			
			appCategory = appCategoryService.findCategoryById(appCategoryId);
			appTypeDto = appTypeService.findAppTypeById(appTypeId);

			if (null != apkFile && !apkFile.isEmpty()) {
				fileName = apkFile.getOriginalFilename();
				fileName = fileName.replace(" ", "_");
			}
			errorMap = ApkValidator.validateApkUploadForm(fileName, title, appCategory, appTypeDto, errorMap);

			if (null == errorMap) {
				long apkFileSize = apkFile.getSize();
				userId = getUserIdByAuthRequest(request);

				DevUser devUser = devUserService.findByUserId(userId);

				apkDetailMap = appAppService.uploadApk(title, language, apkFile, fileName, userId,
						devUser.getPrivateKey(),uploadedFrom);

				apkDetail = (APKDetail) apkDetailMap.get("detail");

				validatorMap = ApkValidator.validateApk(apkDetail);

				if (validatorMap.size() > 0) {
					errorMapResponse = new HashMap<String, Object>();
					// TODO: delete temp apk file
					errorMapResponse.put("bvk", validatorMap);
					response = GeneralResponseModel.of(Constants.FAILED, errorMapResponse);

				} else {
					appDto = appAppService.findAppApplicationByPackageName(apkDetail.getPackageName());

					if (null != appDto) {
						CompareUser(userId, appDto.getUserId());

						if (Utility.isIntegerEmpty(applicationId)) {
							throw new BitVaultException(ErrorMessageConstant.INVALID_APP_UPDATE);
						}
						if (appDto.getAppApplicationId().intValue() != applicationId.intValue()) {
							throw new BitVaultException(ErrorMessageConstant.INVALID_APP_UPDATE_VERSION);
						}
						responseMap = ApkValidator.compareApks(apkDetail, appDto, validatorMap);
						if (!appDto.getStatus().equals(DbConstant.PENDING)) {

							if (appDto.getStatus().equals(Constants.DRAFT)) {
								if (!appDto.getLatestVersionName().equals(apkDetail.getVersionName())
										|| appDto.getLatestVersionNo()
												.intValue() != (Integer.parseInt(apkDetail.getVersionNo()))) {

									throw new BitVaultException(ErrorMessageConstant.APK_DRAFT_ERROR);

								}
							}
							if (responseMap.size() > 0 && !appDto.getStatus().equals(Constants.DRAFT)) {
								errorMapResponse = new HashMap<String, Object>();

								errorMapResponse.put("bvk", responseMap);

								response = GeneralResponseModel.of(Constants.FAILED, errorMapResponse);
							} else {
								AppDetail appDetailOld = appDetailService.findApplicationDetailByAppIdAndVersion(
										appDto.getAppApplicationId(), appDto.getLatestVersionName(), null);

								if (appDto.getAppCategory().getAppCategoryId().intValue() != appCategory
										.getAppCategoryId().intValue()) {
									appCategoryService.updateAppCategoryCountByCatId(
											appDto.getAppCategory().getAppCategoryId(), -1);
									appCategoryService.updateAppCategoryCountByCatId(appCategory.getAppCategoryId(), 1);
								}

								appDto = APKDetail.populateAppApplicationByAPKDetail(apkDetail, appDto);

								appDto.setStatus(Utility.getUploadingStatus(uploadedFrom));
								appDto.setApkFilesize(apkFileSize);
								appDto.setAppCategory(appCategory);
								appDto.setUserId(userId);

								appDto.setUpdateType(DbConstant.UPDATE_TYPE_VERSION);

								appDto.setLanguage(language);
								appDto.setTitle(title);
								appDto.setApplicationType(appTypeDto);
								// appDto.setPublicKey((String)apkDetailMap.get("publicKey"));

								String apkUrl = appAppService.uploadApkToS3(appDto);

								Path path = Paths.get(Constants.FILEPATH + userId);
								if (Files.exists(path)) {
									appAppService.deleteTempApkFolder(Constants.FILEPATH + userId);
								}

								// appAppService.deleteTempApkFolder(
								// apkDetail.getApkUrl().substring(0, apkDetail.getApkUrl().lastIndexOf("/")));
								appDto.setApkUrl(apkUrl);
								appDto = appAppService.saveAppApllication(appDto);

								org.bitvault.appstore.cloud.model.AppDetail appDetail = null;
								appDetailDto = appDetailService.findApplicationDetailByAppIdAndVersion(
										appDto.getAppApplicationId(), appDto.getLatestVersionName(), "");
								appDetail = appDto.populateAppDetail(appDto);

								if (null != appDetailDto) {

									// appDetail.setAppApplication(appDto.populateAppApplication(appDto));
									appDetailDto.setStatus(Utility.getUploadingStatus(uploadedFrom));

									appDetail.setAppDetailId(appDetailDto.getAppDetailId());
									appDetail.setFullDescription(appDetailDto.getFullDescription());
									appDetail.setShortDescription(appDetailDto.getShortDescription());
								} else {
									appDetail.setFullDescription(appDetailOld.getFullDescription());
									appDetail.setShortDescription(appDetailOld.getShortDescription());
								}
								appDetailDto = appDetailService.saveAppDetail(appDetail);
								AppInfoDto appInfoDto = new AppInfoDto();
								appInfoDto = appInfoDto.populateAppInfoDtoToCompare(appDetailDto, appDto);
								appHistoryService.saveAppHistory(null, appInfoDto,
										appDto.populateAppApplication(appDto), "updateType");
								response = GeneralResponseModel.of(Constants.SUCCESS, appDto);
								// if (null != appDto) {
								// appService.saveAppApllicationRequest(devUser.getUserId());
								// }
								response = GeneralResponseModel.of(Constants.SUCCESS, appDto);

							}
						} else {
							throw new BitVaultException(ErrorMessageConstant.REQUEST_ACTION_PENDING);
						}
					} else {

						if (!Utility.isIntegerEmpty(applicationId)) {
							throw new BitVaultException(ErrorMessageConstant.PACKAGENAME_MISSMATCH);
						}

						// save new apk detail to appApplication
						appDto = APKDetail.populateAppApplicationByAPKDetail(apkDetail, new AppApplication());
						
						// if app uploaded from  
						appDto.setStatus(Utility.getUploadingStatus(uploadedFrom));
						
						appDto.setApkFilesize(apkFileSize);
						appDto.setTotalDownloads(0);
						appDto.setTotalInstall(0);
						appDto.setTotalUninstall(0);
						appDto.setAppCategory(appCategory);
						appDto.setUserId(userId);
						appDto.setUpdateType(DbConstant.UPDATE_TYPE_NEW);
						appDto.setLanguage(language);
						appDto.setTitle(title);
						// appDto.setPublicKey((String)apkDetailMap.get("publicKey"));
						appDto.setApplicationType(appTypeDto);

						String apkUrl = appAppService.uploadApkToS3(appDto);

						Path path = Paths.get(Constants.FILEPATH + userId);
						if (Files.exists(path)) {
							appAppService.deleteTempApkFolder(Constants.FILEPATH + userId);
						}

						// appAppService.deleteTempApkFolder(
						// apkDetail.getApkUrl().substring(0, apkDetail.getApkUrl().lastIndexOf("/")));
						appDto.setApkUrl(apkUrl);

						appDto = appAppService.saveAppApllication(appDto);
						appCategoryService.updateAppCategoryCountByCatId(appDto.getAppCategory().getAppCategoryId(), 1);
						if (null != appDto) {
							// appService.saveAppApllicationRequest(devUser.getUserId());
							org.bitvault.appstore.cloud.model.AppDetail appDetail = appDto.populateAppDetail(appDto);
							appDetail.setAppApplication(appDto.populateAppApplication(appDto));
							appDetail.setStatus(Utility.getUploadingStatus(uploadedFrom));
							appDetailDto = appDetailService.saveAppDetail(appDetail);

						}
						AppInfoDto appInfoDto = new AppInfoDto();
						appInfoDto = appInfoDto.populateAppInfoDtoToCompare(appDetailDto, appDto);

						appHistoryService.saveAppHistory(null, appInfoDto, appDto.populateAppApplication(appDto),
								"updateType");
						response = GeneralResponseModel.of(Constants.SUCCESS, appDto);
						return new ResponseEntity<GeneralResponseModel>(response, HttpStatus.OK);
					}

				}

			} else {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put(Constants.MESSAGE, errorMap);
				response = GeneralResponseModel.of(Constants.FAILED, map);
			}
		} catch (BitVaultException e) {

			Path path = Paths.get(Constants.FILEPATH + userId);
			if (Files.exists(path)) {
				appAppService.deleteTempApkFolder(Constants.FILEPATH + userId);
			}

			response = GeneralResponseModel.of(Constants.FAILED, new BitVaultResponse(e));
		}
		return new ResponseEntity<GeneralResponseModel>(response, HttpStatus.OK);
	}

	@PostMapping(value = APIConstants.UPLOAD_APK_ICON + "/{applicationId}")
	public ResponseEntity<GeneralResponseModel> uploadApkIcon(@PathVariable Integer applicationId,
			@RequestParam MultipartFile appIcon, HttpServletRequest request) {
		GeneralResponseModel response = null;
		AppDetail appDetailDto = null;
		Map<String, Object> responseMap = null;
		try {
			AppApplication appDto = appAppService.findApplicationByAppId(applicationId);
			if (null != appDto) {
				if (appDto.getStatus().equals(Constants.PENDING)) {
					throw new BitVaultException(ErrorMessageConstant.REQUEST_ACTION_PENDING);
				}
				String userId = getUserIdByAuthRequest(request);
				CompareUser(userId, appDto.getUserId());

				String appIconUrl = appAppService.uploadApkIcon(appIcon, appDto);
				if (null != appIconUrl) {
					appDto.setAppIconUrl(appIconUrl);
					appDto = appAppService.saveAppApllication(appDto);
					appDetailDto = appDetailService.findApplicationDetailByAppIdAndVersion(appDto.getAppApplicationId(),
							appDto.getLatestVersionName(), "");
					if (null != appDetailDto) {
						appDetailDto.setAppIconUrl(appIconUrl);

						appDetailService.saveAppDetail(appDetailDto.populateAppDetail(appDetailDto));
					}
					Application applicationDto = appService.findApplicationByAppId(appDto.getAppApplicationId());
					if (null != applicationDto) {
						// applicationDto.setAppIconUrl(appIconUrl);
						// appService.saveApllication(applicationDto.populateApplication(applicationDto));

						appService.upadteAppIconById(applicationDto.getApplicationId(), appIconUrl);

					}

				}
				responseMap = new HashMap<String, Object>();
				responseMap.put(Constants.MESSAGE, Constants.SUCCESS_UPLOADED);
				responseMap.put(Constants.IMAGE_URL, appIconUrl);
				return new ResponseEntity<GeneralResponseModel>(GeneralResponseModel.of(Constants.SUCCESS, responseMap),
						HttpStatus.OK);

			} else {
				throw new BitVaultException(ErrorMessageConstant.RESULT_NOT_FOUND);
			}
		} catch (BitVaultException e) {
			e.printStackTrace();
			return new ResponseEntity<GeneralResponseModel>(
					GeneralResponseModel.of(Constants.FAILED, new BitVaultResponse(e)), HttpStatus.OK);
		}

	}

	@PostMapping(value = APIConstants.UPLOAD_APK_IMAGES + "/{applicationId}")
	public ResponseEntity<GeneralResponseModel> uploadApkImages(@PathVariable Integer applicationId,
			@RequestParam List<MultipartFile> appImages,
			@RequestParam(defaultValue = DbConstant.TYPE_IMAGE) String imageType, HttpServletRequest request) {
		GeneralResponseModel response = null;
		AppDetail appDetailDto = null;
		Map<String, String> errorMap = null;

		if (null != appImages && appImages.size() > 0 && (imageType.equalsIgnoreCase(DbConstant.TYPE_IMAGE)
				|| imageType.equalsIgnoreCase(DbConstant.TYPE_BANNER))) {
			try {
				AppApplication appDto = appAppService.findApplicationByAppId(applicationId);
				if (null != appDto) {
					if (appDto.getStatus().equals(Constants.PENDING)) {
						throw new BitVaultException(ErrorMessageConstant.REQUEST_ACTION_PENDING);
					}

					String userId = getUserIdByAuthRequest(request);
					CompareUser(userId, appDto.getUserId());

					int imageCount = appImageService.getAppImageCountByType(DbConstant.TYPE_IMAGE,
							appDto.getAppApplicationId());
					int bannerCount = appImageService.getAppImageCountByType(DbConstant.TYPE_BANNER,
							appDto.getAppApplicationId());

					if (imageCount == 8 && imageType.equalsIgnoreCase(DbConstant.TYPE_IMAGE)) {
						errorMap = Utility.ErrorMap("appImage", ErrorMessageConstant.APP_IMAGES_MAX_ERROR, errorMap);
					}

					if (null != errorMap && errorMap.size() > 0) {
						response = GeneralResponseModel.of(Constants.FAILED, errorMap);
						return new ResponseEntity<GeneralResponseModel>(response, HttpStatus.OK);

					}
					if (bannerCount > 0 && imageType.equalsIgnoreCase(DbConstant.TYPE_BANNER)) {
						List<AppImage> appImageList = appImageService.findAppImagesByApplicatinIdAndType(applicationId,
								imageType);
						for (AppImage appImageDTo : appImageList) {
							appImageService.deleteAppImageById(appImageDTo.getAppImagesId());

						}
					}
					String imageStatus = Constants.UNPUBLISHED;
					if (appDto.getStatus().equalsIgnoreCase(Constants.PUBLISHED)
							|| appDto.getStatus().equalsIgnoreCase(Constants.BETA_PUBLISHED)
							|| appDto.getStatus().equalsIgnoreCase(Constants.ALPHA_PUBLISHED)
							|| imageType.equalsIgnoreCase(DbConstant.TYPE_BANNER)) {
						imageStatus = Constants.PUBLISHED;
					}
					List<String> imageUrlList = appAppService.uploadApkImages(appImages, appDto, imageType);

					List<org.bitvault.appstore.cloud.dto.AppImage> listAppImages = appImageService
							.saveAppImage(imageUrlList, appDto.getAppApplicationId(), imageType, imageStatus);
					response = GeneralResponseModel.of(Constants.SUCCESS, listAppImages);
				} else {
					throw new BitVaultException(ErrorMessageConstant.RESULT_NOT_FOUND);
				}
				return new ResponseEntity<GeneralResponseModel>(response, HttpStatus.OK);
			} catch (BitVaultException e) {
				e.printStackTrace();
				return new ResponseEntity<GeneralResponseModel>(
						GeneralResponseModel.of(Constants.FAILED, new BitVaultResponse(e)), HttpStatus.OK);
			}
		} else {
			return new ResponseEntity<GeneralResponseModel>(GeneralResponseModel.of(Constants.FAILED,
					new BitVaultResponse(ErrorMessageConstant.FILE_IS_INVALID)), HttpStatus.OK);
		}
	}

	@GetMapping(value = APIConstants.FIND_APP_BY_APPID + "/{applicationId}")
	public ResponseEntity<GeneralResponseModel> findAppByAppId(@PathVariable Integer applicationId) {
		GeneralResponseModel response = null;
		try {
			AppApplication appDto = appAppService.findApplicationByAppId(applicationId);
			if (null != appDto) {
				response = response.of(Constants.SUCCESS, appDto);
				return new ResponseEntity<GeneralResponseModel>(response, HttpStatus.OK);
			} else {
				response = response.of(Constants.FAILED, new BitVaultResponse(ErrorMessageConstant.RESULT_NOT_FOUND));

				return new ResponseEntity<GeneralResponseModel>(response, HttpStatus.OK);

			}
		} catch (BitVaultException e) {
			e.printStackTrace();
			return new ResponseEntity<GeneralResponseModel>(
					GeneralResponseModel.of(Constants.FAILED, new BitVaultResponse(e)), HttpStatus.OK);
		}

	}

	@PostMapping(value = APIConstants.SAVE_APP_INFO)
	public ResponseEntity<GeneralResponseModel> saveAppInfo(@RequestBody AppInfoDto appInfoDto,
			HttpServletRequest request) {
		GeneralResponseModel response = null;
		AppApplication appDto = null;
		Map<String, String> errorMap = null;
		AppInfoDto appInfoDtoOld = null;
		try {

			if (!Utility.isIntegerEmpty(appInfoDto.getApplicationId())
					&& !Utility.isIntegerEmpty(appInfoDto.getAppCategoryId())
					&& !Utility.isIntegerEmpty(appInfoDto.getAppTypeId())
					&& !Utility.isStringEmpty(appInfoDto.getVersionName())) {
				appDto = appAppService.findApplicationByAppId(appInfoDto.getApplicationId());

				String userId = getUserIdByAuthRequest(request);
				CompareUser(userId, appDto.getUserId());

				AppDetail appDetailDto = appDetailService.findApplicationDetailByAppIdAndVersion(
						appDto.getAppApplicationId(), appDto.getLatestVersionName(), "");
				appInfoDtoOld = appInfoDto.populateAppInfoDtoToCompare(appDetailDto, appDto);
				ApplicationTypeDto appTypeDto = appTypeService.findAppTypeById(appInfoDto.getAppTypeId());
				AppCategory appCategoryDto = appCategoryService.findCategoryById(appInfoDto.getAppCategoryId());
				if (null != appTypeDto && null != appCategoryDto) {
					if (appTypeDto.getAppTypeId() != appCategoryDto.getAppTypeId()
							|| (!appCategoryDto.getStatus().equalsIgnoreCase(Constants.ACTIVE))) {
						throw new BitVaultException(ErrorMessageConstant.INVALID_CATEGORYID);
					}

				} else {
					throw new BitVaultException(ErrorMessageConstant.INVALID_APPTYPEID);
				}

				if (null != appDto) {
					if (appDto.getStatus().equals(Constants.PENDING)) {
						throw new BitVaultException(ErrorMessageConstant.REQUEST_ACTION_PENDING);
					}

					if (appDto.getStatus().equals(Constants.PUBLISHED)
							|| appDto.getStatus().equals(Constants.UNPUBLISHED)
							|| appDto.getStatus().equals(Constants.BETA_PUBLISHED)
							|| appDto.getStatus().equals(Constants.BETA_UNPUBLISHED)
							|| appDto.getStatus().equals(Constants.ALPHA_PUBLISHED)
							|| appDto.getStatus().equals(Constants.ALPHA_UNPUBLISHED)
							) {

						int imageCount = appImageService.getAppImageCountByType(DbConstant.TYPE_IMAGE,
								appDto.getAppApplicationId());
						int bannerCount = appImageService.getAppImageCountByType(DbConstant.TYPE_BANNER,
								appDto.getAppApplicationId());

						errorMap = appInfoDto.validateAppInfoDto(appInfoDto, appDto.getAppIconUrl(), appDto.getApkUrl(),
								imageCount, bannerCount);

						if (null != errorMap && errorMap.size() > 0) {
							response = GeneralResponseModel.of(Constants.FAILED, errorMap);
							return new ResponseEntity<GeneralResponseModel>(response, HttpStatus.OK);

						}
					}

					if (appDto.getAppCategory().getAppCategoryId().intValue() != appCategoryDto.getAppCategoryId()
							.intValue()) {
						appCategoryService.updateAppCategoryCountByCatId(appDto.getAppCategory().getAppCategoryId(),
								-1);
						appCategoryService.updateAppCategoryCountByCatId(appCategoryDto.getAppCategoryId(), 1);
					}

					appDto = appInfoDto.populateAppApplication(appInfoDto, appDto);
					appDto.setAppCategory(appCategoryDto);
					appDto.setApplicationType(appTypeDto);
					appDto = appAppService.saveAppApllication(appDto);

					Application publishedAppDto = appService.findApplicationByAppId(appDto.getAppApplicationId());

					if (null != publishedAppDto && appDto.getStatus().equals(Constants.PUBLISHED)) {
						publishedAppDto = appService.saveApllication(appDto.populatePublisheApp(appDto));

					}

					if (null == appDetailDto) {
						appDetailDto = new AppDetail();
					}
					appDetailDto = appInfoDto.populateAppDetail(appInfoDto, appDetailDto);
					// appDetailDto.setStatus(publishedAppDto.getStatus());
					appDetailDto = appDetailService.saveAppDetail(appDetailDto.populateAppDetail(appDto, appDetailDto));
					appInfoDto = appInfoDto.populateAppInfoDtoToCompare(appDetailDto, appDto);

					appHistoryService.saveAppHistory(appInfoDtoOld, appInfoDto, appDto.populateAppApplication(appDto),
							null);

					AppApplicationDetailDto appApplicationDetailDto = AppApplicationDetailDto
							.populateAppApplicationDetailDto(appDetailDto, appDto);
					response = response.of(Constants.SUCCESS, appApplicationDetailDto);
				} else {
					throw new BitVaultException(ErrorMessageConstant.RESULT_NOT_FOUND);
				}

			} else {

				response = response.of(Constants.FAILED, new BitVaultResponse(ErrorMessageConstant.FIELD_IS_EMPTY));

			}
			return new ResponseEntity<GeneralResponseModel>(response, HttpStatus.OK);

		} catch (BitVaultException e) {

			response = GeneralResponseModel.of(Constants.FAILED, new BitVaultResponse(e));
			return new ResponseEntity<GeneralResponseModel>(response, HttpStatus.OK);

		}

	}

	@DeleteMapping(value = APIConstants.DELETE_APP_IMAGE + "/{appImageId}")
	public ResponseEntity<GeneralResponseModel> deleteAppImage(@PathVariable Integer appImageId,
			HttpServletRequest request) {
		GeneralResponseModel response = null;
		org.bitvault.appstore.cloud.dto.AppImage appImageDto = null;
		AppApplication appApplicationDto = null;

		Map<String, String> errorMap = null;
		try {
			String userId = getUserIdByAuthRequest(request);
			appImageDto = appImageService.findImageByImageId(appImageId);
			if (null == appImageDto) {
				throw new BitVaultException(ErrorMessageConstant.RESULT_NOT_FOUND);
			}
			appApplicationDto = appAppService.findApplicationByAppId(appImageDto.getApplicationId());

			if (null == appImageDto || null == appApplicationDto) {
				throw new BitVaultException(ErrorMessageConstant.RESULT_NOT_FOUND);
			}
			if (appImageDto.getImageType().equalsIgnoreCase(Constants.BANNER)) {
				throw new BitVaultException(ErrorMessageConstant.INVALID_REQUEST);
			}

			if (appApplicationDto.getStatus().equals(Constants.PUBLISHED)
					|| appApplicationDto.getStatus().equals(Constants.BETA_PUBLISHED)
					|| appApplicationDto.getStatus().equals(Constants.ALPHA_PUBLISHED)
					) {
				int imageCount = appImageService.getAppImageCountByType(DbConstant.TYPE_IMAGE,
						appImageDto.getApplicationId());

				if (imageCount == 3) {
					errorMap = Utility.ErrorMap("appImage", ErrorMessageConstant.APP_IMAGES_MAX_DELETE_ERROR, errorMap);

				}

				if (null != errorMap && errorMap.size() > 0) {
					response = GeneralResponseModel.of(Constants.FAILED, errorMap);
					return new ResponseEntity<GeneralResponseModel>(response, HttpStatus.OK);

				}
			}
			appAppService.deleteAppImage(appImageDto, userId);
			response = response.of(Constants.SUCCESS, new BitVaultResponse(Constants.SUCCESS_DELETED));

		} catch (BitVaultException e) {

			response = GeneralResponseModel.of(Constants.FAILED, new BitVaultResponse(e));

		}

		return new ResponseEntity<GeneralResponseModel>(response, HttpStatus.OK);
	}

	@PostMapping(value = APIConstants.APP_STATS)
	public ResponseEntity<GeneralResponseModel> appStats(@RequestBody Map<String, Object> requestBody) {

		GeneralResponseModel genralResponse = null;
		List<ChartStatsDto> chartDto = null;
		try {
			Integer appId = (Integer) requestBody.get("applicationId");
			Integer year = (Integer) requestBody.get("year");
			String status = (String) requestBody.get("status");
			String startDate = (String) requestBody.get("startDate");
			String endDate = (String) requestBody.get("endDate");
			if (!Utility.isIntegerEmpty(appId) && !Utility.isStringEmpty(status)) {
				AppApplication appAppDto = appAppService.findApplicationByAppId(appId);
				if (appAppDto == null) {
					throw new BitVaultException(ErrorMessageConstant.RESULT_NOT_FOUND);
				}
				if (!Utility.isIntegerEmpty(year)) {
					chartDto = mobileUserAppService.getChartStatsbyAppIdAndYear(appId, year, status);
				} else if (!Utility.isStringEmpty(startDate) && !Utility.isStringEmpty(endDate)) {
					String format = "yyyy-MM-dd";
					if (Utility.isValidDateFormat(format, startDate) && Utility.isValidDateFormat(format, endDate))
						chartDto = mobileUserAppService.getChartStatsbyAppIdAndDates(appId, startDate, endDate, status);

				} else {
					throw new BitVaultException(ErrorMessageConstant.FIELD_IS_EMPTY);
				}
				// for (ChartStatsDto chartStatsDto : chartDto) {
				//
				// }

				genralResponse = GeneralResponseModel.of(Constants.SUCCESS, chartDto);
			} else {
				throw new BitVaultException(ErrorMessageConstant.FIELD_IS_EMPTY);

			}
			return new ResponseEntity<GeneralResponseModel>(genralResponse, HttpStatus.OK);

		} catch (BitVaultException e) {
			genralResponse = GeneralResponseModel.of(Constants.FAILED, new BitVaultResponse(e));

			return new ResponseEntity<GeneralResponseModel>(genralResponse, HttpStatus.OK);

		}

	}

	@PostMapping(value = APIConstants.APP_AVERAGE_STATS)
	public ResponseEntity<GeneralResponseModel> appAverageStats(@RequestBody Map<String, Object> requestBody) {

		GeneralResponseModel genralResponse = null;
		List<AverageRatingChartDto> chartDto = null;
		try {
			Integer appId = (Integer) requestBody.get("applicationId");
			Integer year = (Integer) requestBody.get("year");

			String startDate = (String) requestBody.get("startDate");
			String endDate = (String) requestBody.get("endDate");
			if (!Utility.isIntegerEmpty(appId)) {
				AppApplication appAppDto = appAppService.findApplicationByAppId(appId);
				if (appAppDto == null) {
					throw new BitVaultException(ErrorMessageConstant.RESULT_NOT_FOUND);
				}

				if (!Utility.isIntegerEmpty(year)) {
					chartDto = appRateReviewService.getChartStatsbyAppIdAndYear(appId, year);
				} else if (!Utility.isStringEmpty(startDate) && !Utility.isStringEmpty(endDate)) {
					String format = "yyyy-MM-dd";
					if (Utility.isValidDateFormat(format, startDate) && Utility.isValidDateFormat(format, endDate)) {
						chartDto = appRateReviewService.getChartStatsbyAppIdAndDates(appId, startDate, endDate);
					} else {
						throw new BitVaultException(ErrorMessageConstant.INVALID_DATE_FORMAT);

					}

				} else {
					throw new BitVaultException(ErrorMessageConstant.FIELD_IS_EMPTY);
				}

				genralResponse = GeneralResponseModel.of(Constants.SUCCESS, chartDto);
			} else {
				throw new BitVaultException(ErrorMessageConstant.FIELD_IS_EMPTY);

			}
			return new ResponseEntity<GeneralResponseModel>(genralResponse, HttpStatus.OK);

		} catch (BitVaultException e) {
			genralResponse = GeneralResponseModel.of(Constants.FAILED, new BitVaultResponse(e));

			return new ResponseEntity<GeneralResponseModel>(genralResponse, HttpStatus.OK);

		}

	}

	@PostMapping(value = APIConstants.RELEASE_MANGEMENT)
	public ResponseEntity<GeneralResponseModel> appArtifact(@RequestBody Map<String, Object> requestBody,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size,
			@RequestParam(defaultValue = Constants.UPDATED_AT) String orderBy,
			@RequestParam(defaultValue = Constants.ALL_APP_STATUS) List<String> status,
			@RequestParam(defaultValue = DbConstant.DESC) String direction) {
		Map<String, Object> responseMap = null;
		Map<String, Object> detailMap = null;
		GeneralResponseModel genralResponse = null;
		Integer applicationId = (Integer) requestBody.get("applicationId");

		if (page > 0) {
			page = page - 1;
		}

		try {
			if (!Utility.isIntegerEmpty(applicationId) && !Utility.isCollectionEmpty(status)) {
				AppApplication appAppDto = appAppService.findApplicationByAppId(applicationId);

				if (null == appAppDto) {
					throw new BitVaultException(ErrorMessageConstant.RESULT_NOT_FOUND);
				}

				Application appDto = appService.findApplicationByAppId(applicationId);
				detailMap = appDetailService.appDetailArtifact(applicationId, status, page, size, direction, orderBy);
				// AppDetail latestAction =
				// appDetailService.findApplicationDetailByAppIdAndVersion(
				// appAppDto.getAppApplicationId(),
				// appAppDto.getLatestVersionName(), null);
				// latestAction.setAppImage(null);
				responseMap = new HashMap<String, Object>();
				if (null == appDto) {
					responseMap.put("production", null);
				} else {
					AppDetail production = appDetailService.findApplicationDetailByAppIdAndVersion(
							appDto.getApplicationId(), appDto.getLatestVersion(), null);
					production.setAppImage(null);

					responseMap.put("production", production);
				}
				responseMap.put("artifact", detailMap);
				// responseMap.put("latestAction", latestAction);
				genralResponse = GeneralResponseModel.of(Constants.SUCCESS, responseMap);

			} else {
				throw new BitVaultException(ErrorMessageConstant.FIELD_IS_EMPTY);
			}
		} catch (BitVaultException e) {
			genralResponse = GeneralResponseModel.of(Constants.FAILED, new BitVaultResponse(e));

		}

		return new ResponseEntity<GeneralResponseModel>(genralResponse, HttpStatus.OK);
	}

	private DevUser getUserByAuthRequest(HttpServletRequest request) {
		String userEmail = null;
		DevUser devUser = null;

		try {
			userEmail = (String) request.getAttribute("email");
			devUser = devUserService.findByEmailId(userEmail);

		} catch (Exception e) {
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED, ErrorCode.SOME_ERROR_OCCURED_CODE);
		}
		if (null == devUser) {
			throw new BitVaultException(ErrorMessageConstant.USER_NOT_FOUND, ErrorCode.FIELD_IS_EMPTY_CODE);

		}
		return devUser;
	}

	@GetMapping(value = APIConstants.APP_ACTIVE_STATS)
	public ResponseEntity<?> appActiveStats(@RequestParam int applicationId) {
		GeneralResponseModel genralResponse = null;
		Map<String, Object> responseMap = null;
		try {

			AppStatisticsDto appStats = appStatisticsService.findAppStatisticsByAppId(applicationId);

			int totalRating = appRateReviewService.findTotalRatingByAppId(applicationId);
			int reviewWithRating = appRateReviewService.findReviewWithRatingByAppId(applicationId);
			float avgRating = appRateReviewService.getAverageRating(applicationId);
			responseMap = new HashMap<String, Object>();
			responseMap.put("avgRating", avgRating);
			responseMap.put("totalRating", totalRating);
			responseMap.put("reviewWithRating", reviewWithRating);
			if (appStats != null) {
				responseMap.put("totalDownloads", appStats.getAppDownloadCount());
				responseMap.put("activeInstall", appStats.getAppInstallCount());
				responseMap.put("activeUnistall", appStats.getAppUninstallCount());
			} else {
				responseMap.put("totalDownloads", 0);
				responseMap.put("activeInstall", 0);
				responseMap.put("activeUnistall", 0);
			}

			genralResponse = GeneralResponseModel.of(Constants.SUCCESS, responseMap);
		} catch (BitVaultException e) {
			genralResponse = GeneralResponseModel.of(Constants.FAILED, new BitVaultResponse(e));
		}
		return new ResponseEntity<GeneralResponseModel>(genralResponse, HttpStatus.OK);

	}

	private String getUserIdByAuthRequest(HttpServletRequest request) {
		String userId = null;

		try {
			userId = (String) request.getAttribute("userId");

		} catch (Exception e) {
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED, ErrorCode.SOME_ERROR_OCCURED_CODE);
		}
		if (null == userId) {
			throw new BitVaultException(ErrorMessageConstant.USER_NOT_FOUND, ErrorCode.FIELD_IS_EMPTY_CODE);

		}
		return userId;
	}

	private void CompareUser(String userFromRequest, String userFromApplication) {
		if (!userFromRequest.equals(userFromApplication)) {
			throw new BitVaultException(ErrorMessageConstant.INVALID_APP_USER);

		}
	}

	@GetMapping(value = APIConstants.RATE_REVIEW_LIST)
	public ResponseEntity<?> rateReviewList(@RequestParam Integer applicationId,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size,
			@RequestParam(defaultValue = Constants.UPDATED_AT) String orderBy) throws Exception {

		Response response = null;
		GeneralResponseModel genralResponse = null;
		Map<String, Object> allAppMap = null;
		Map<String, Object> allMap = null;

		if (page > 0) {
			page = page - 1;
		}

		try {

			if (Utility.isIntegerEmpty(applicationId)) {
				throw new BitVaultException(ErrorMessageConstant.FIELD_IS_EMPTY);
			}

			org.bitvault.appstore.cloud.dto.AppApplication appAppDto = appAppService
					.findApplicationByAppId(applicationId);
			if (null == appAppDto) {
				throw new BitVaultException(ErrorMessageConstant.NO_REQUEST_FOUND);

			}
			allAppMap = appRateReviewService.findAppRateReviewByAppId(applicationId, page, size, DbConstant.ASC,
					orderBy);

			int totalRating = appRateReviewService.findTotalRatingByAppId(applicationId);
			int reviewWithRating = appRateReviewService.findReviewWithRatingByAppId(applicationId);
			float avgRating = appRateReviewService.getAverageRating(applicationId);
			allMap = new HashMap<String, Object>();

			allMap.put("avgRating", avgRating);
			allMap.put("totalRating", totalRating);
			allMap.put("reviewWithRating", reviewWithRating);

			allMap.put("appList", allAppMap.get("appRateReviewDtoList"));

			response = new Response();

			response.setResult(allMap);

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

	@RequestMapping(value = APIConstants.ELASTIC_SEARCH_APP, method = RequestMethod.GET)
	public ResponseEntity<?> elasticSearchApplication(@RequestParam String name,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size,
			@RequestParam(defaultValue = DbConstant.APPNAME) String orderBy, HttpServletRequest request) {
		Response response = new Response();
		GeneralResponseModel genralResponse = null;
		Map<String, Object> allAppMap = null;
		if (page > 0) {
			page = page - 1;
		}
		if (!Utility.isStringEmpty(name)) {
			// if (name.length() > 2) {
			try

			{
				String userId = getUserIdByAuthRequest(request);
				logger.info("Application get searched");
				allAppMap = appAppService.searchAppApplicationByAppName(name, userId, page, size,
						DbConstant.DESC.toString(), orderBy);
				response.setResult(allAppMap.get("appList"));
				// response.setType(APIConstants.SEARCH_APPLICATION);
				logger.info("Application Searched Successfully");
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
				logger.error("Error occured during Application Searching");
				genralResponse = GeneralResponseModel.of(Constants.FAILED, new BitVaultResponse(e));

				return new ResponseEntity<GeneralResponseModel>(genralResponse, HttpStatus.OK);

			}

			// }
		} else {
			genralResponse = GeneralResponseModel.of(Constants.FAILED,
					new BitVaultResponse(new BitVaultException(ErrorMessageConstant.FIELD_IS_EMPTY)));
			return new ResponseEntity<GeneralResponseModel>(genralResponse, HttpStatus.OK);

		}
		// return new ResponseEntity<Response>(response, HttpStatus.OK);

	}

	@GetMapping(value = APIConstants.APP_AUDIT_TRAIL)
	public ResponseEntity<?> appAuditTrail(@RequestParam Integer applicationId,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size,
			@RequestParam(defaultValue = Constants.UPDATED_AT) String orderBy) throws Exception {

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
              logger.info("findApplicationByAppId method calling");
			org.bitvault.appstore.cloud.dto.AppApplication appAppDto = appAppService
					.findApplicationByAppId(applicationId);
			  logger.info("AppApplicationDto get successfully....");
			if (null == appAppDto) {
				throw new BitVaultException(ErrorMessageConstant.NO_REQUEST_FOUND);

			}
			 logger.info("appHistory method calling...");
			allAppMap = appHistoryService.appHistory(applicationId, page, size, DbConstant.ASC, orderBy);
			 logger.info("Map get successfully");

			response = new Response();

			response.setResult(allAppMap.get("appHistoryList"));
			
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

	/*
	 * private List<String> checkForAppUpdateAndHitForNotification(AppApplication
	 * appAppDto, Integer appId) {
	 * 
	 * List<String> publicAddressList;
	 * 
	 * try { publicAddressList = new ArrayList<String>();
	 * 
	 * if (appAppDto.getStatus().equals(Constants.DRAFT)) {
	 * 
	 * Application application = appService.findApplicationByAppId(appId); if
	 * (application != null) {
	 * 
	 * if (!application.getLatestVersion().equals(appAppDto.getLatestVersionName())
	 * && application.getLatestVersionNo() != appAppDto.getLatestVersionNo()) {
	 * 
	 * List<MobileUserAppStatus> mobileUserAppStatusList = mobileUserAppStatus
	 * .findAllMobileUserFromAppIdAndStatus(appId, DbConstant.INSTALL);
	 * 
	 * if (mobileUserAppStatusList != null && mobileUserAppStatusList.size() > 0) {
	 * 
	 * for (int i = 0; i < mobileUserAppStatusList.size(); i++) { String
	 * publicAddress =
	 * mobileUserAppStatusList.get(i).getMobileUser().getPublicAdd(); // Integer
	 * mobileUserId = //
	 * mobileUserAppStatusList.get(i).getMobileUser().getMobileUserId();
	 * publicAddressList.add(publicAddress); } } } } } } catch (Exception e) {
	 * e.printStackTrace(); return null; }
	 * 
	 * return publicAddressList; }
	 */

}
