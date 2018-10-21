package org.bitvault.appstore.commons.application.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bitvault.appstore.cloud.constant.APIConstants;
import org.bitvault.appstore.cloud.constant.Constants;
import org.bitvault.appstore.cloud.constant.DbConstant;
import org.bitvault.appstore.cloud.constant.ErrorCode;
import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.dto.AppCategory;
import org.bitvault.appstore.cloud.dto.AppRateReviewDto;
import org.bitvault.appstore.cloud.dto.Application;
import org.bitvault.appstore.cloud.dto.ApplicationDetailDto;
import org.bitvault.appstore.cloud.dto.ApplicationTypeDto;
import org.bitvault.appstore.cloud.dto.MobileUser;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.bitvault.appstore.cloud.exception.BitVaultResponse;
import org.bitvault.appstore.cloud.user.common.GeneralResponseModel;
import org.bitvault.appstore.cloud.user.dev.controller.DevUserController;
import org.bitvault.appstore.cloud.utils.Response;
import org.bitvault.appstore.cloud.utils.Utility;
import org.bitvault.appstore.commons.application.service.AppCategoryService;
import org.bitvault.appstore.commons.application.service.AppImageService;
import org.bitvault.appstore.commons.application.service.AppRateReviewService;
import org.bitvault.appstore.commons.application.service.AppTypeService;
import org.bitvault.appstore.commons.application.service.ApplicationService;
import org.bitvault.appstore.mobile.dto.ApplicationLatestVersionDTO;
import org.bitvault.appstore.mobile.service.MobileUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = APIConstants.MOBILE_API_BASE)
public class ApplicationController {
	private static final Logger logger = LoggerFactory.getLogger(ApplicationController.class);
	@Autowired
	ApplicationService appService;

	@Autowired
	AppCategoryService appCategoryService;

	@Autowired
	AppImageService appImageService;

	@Autowired
	AppRateReviewService appRateReveiwService;
	@Autowired
	AppTypeService appTypeService;
	@Autowired
	MobileUserService mobileUserService;

	@RequestMapping(value = APIConstants.LIST_OF_ALL_PUBLSHED_APPLICATION, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response> listOfAllApplication(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size,
			@RequestParam(defaultValue = Constants.UPDATED_AT) String orderBy) {
		if (page > 0) {
			page = page - 1;
		}
		Response response = new Response();
		Map<String, Object> allAppMap = null;
		try {
			allAppMap = appService.listOfAllApplication(page, size, DbConstant.ASC, orderBy);
			response.setResult(allAppMap.get("appList"));
			int totalPages = (int) allAppMap.get(Constants.TOTAL_PAGES);
			int totalRecord = ((Long) allAppMap.get(Constants.TOTAL_RECORDS)).intValue();
			int pageSize = (int) allAppMap.get(Constants.SIZE);
			// response.setType(APIConstants.LIST_OF_ALL_PUBLSHED_APPLICATION);
			response.setStatus(Constants.SUCCESS);
			response.setSize(pageSize);
			response.setTotalPages(totalPages);
			response.setTotalRecords(totalRecord);
			response.setSort(orderBy);
			return new ResponseEntity<Response>(response, HttpStatus.OK);

		} catch (BitVaultException e) {
			response.setResult(new BitVaultResponse(e));
			// response.setType(APIConstants.TYPE_ERROR);
			response.setStatus(Constants.FAILED);
			return new ResponseEntity<Response>(response, HttpStatus.EXPECTATION_FAILED);

		}

	}

	@RequestMapping(value = APIConstants.PUBLSHED_APPLICATION_BY_PACKAGENAME, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response> findApplicationByPackageName(@RequestParam String packageName) {
		Response response = new Response();

		if (!Utility.isStringEmpty(packageName)) {
			try

			{
				Application app = appService.findApplicationByPackageName(packageName);
				if (null == app) {
					throw new BitVaultException(ErrorMessageConstant.RESULT_NOT_FOUND);
				}
				app.setApkUrl("");
				response.setResult(app);
				// response.setType(APIConstants.PUBLSHED_APPLICATION_BY_PACKAGENAME);
				response.setStatus(Constants.SUCCESS);
				return new ResponseEntity<Response>(response, HttpStatus.OK);
			} catch (BitVaultException e) {
				e.printStackTrace();
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

	@RequestMapping(value = APIConstants.FIND_PUBLISHED_APPLICATION_BY_USERID, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response> findApplicationByUserId(@RequestParam String userId,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size,
			@RequestParam(defaultValue = Constants.UPDATED_AT) String orderBy) {
		if (page > 0) {
			page = page - 1;
		}
		Response response = new Response();
		Map<String, Object> allAppMap = null;

		if (!Utility.isStringEmpty(userId)) {
			try {
				allAppMap = appService.findApplicationByUserId(userId, page, size, DbConstant.ASC, orderBy);
				response.setResult(allAppMap.get("appList"));
				// response.setType(APIConstants.PUBLSHED_APPLICATION_BY_PACKAGENAME);
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
				e.printStackTrace();
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

	@RequestMapping(value = APIConstants.FIND_PUBLISHED_APPLICATION_BY_CATEGORY, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response> findApplicationByCategory(@RequestParam List<Integer> categoryIdList,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size,
			@RequestParam(defaultValue = Constants.UPDATED_AT) String orderBy) {
		if (page > 0) {
			page = page - 1;
		}
		Response response = new Response();
		Map<String, Object> allAppMap = null;

		if (!Utility.isCollectionEmpty(categoryIdList)) {
			try

			{
				allAppMap = appService.findApplicationByCategory(categoryIdList, page, size, DbConstant.ASC, orderBy);
				response.setResult(allAppMap.get("appList"));
				// response.setType(APIConstants.PUBLSHED_APPLICATION_BY_PACKAGENAME);
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
				e.printStackTrace();
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

	@GetMapping(value = APIConstants.FIND_APPDETAIL_BY_APPID + "/{appId}")
	public ResponseEntity<GeneralResponseModel> appDetailByAppId(@PathVariable Integer appId,
			@RequestParam String publicAddress) {
		GeneralResponseModel response = null;
		ApplicationDetailDto applicationDetailDto = null;
		Map<String, Object> responseMap = null;
		if (!Utility.isIntegerEmpty(appId)) {
			try {
				applicationDetailDto = appService.findPublishedAppDetailbyAppId(appId);
				Map<String, Long> starRatingMap = appRateReveiwService
						.findStarRatingByApplicationId(applicationDetailDto.getApplication().getApplicationId());
				MobileUser mobileUser = mobileUserService.findMobileUserByPublicAddress(publicAddress);
				AppRateReviewDto appRateReviewDto = appRateReveiwService
						.findAppRateReviewByMobileUserId(mobileUser.getMobileUserId(), appId);
				responseMap = new HashMap<String, Object>();
				responseMap.put("starRating", starRatingMap);
				applicationDetailDto.setApkUrl("");
				applicationDetailDto.getApplication().setApkUrl("");
				responseMap.put("appDetail", applicationDetailDto);
				responseMap.put("appReview", appRateReviewDto);
				response = GeneralResponseModel.of(Constants.SUCCESS, responseMap);

			} catch (BitVaultException e) {
				e.printStackTrace();
				response = GeneralResponseModel.of(Constants.FAILED, new BitVaultResponse(e));

			}
		} else {
			response = GeneralResponseModel.of(Constants.FAILED,
					new BitVaultResponse(ErrorMessageConstant.FIELD_IS_EMPTY));

		}
		return new ResponseEntity<GeneralResponseModel>(response, HttpStatus.OK);

	}

	@RequestMapping(value = APIConstants.SEARCH__PUBLISHED_APPLICATION, method = RequestMethod.POST)
	public ResponseEntity<Response> searchApplication(@RequestParam String appName,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size,
			@RequestParam(defaultValue = DbConstant.APP_NAME) String orderBy) {
		Response response = new Response();
		Map<String, Object> allAppMap = null;
		if (page > 0) {
			page = page - 1;
		}
		if (!Utility.isStringEmpty(appName)) {
			if (appName.length() > 2) {
				try

				{
					allAppMap = appService.findApplicationByAppName(appName, page, size, DbConstant.DESC, orderBy);
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
					response.setResult(new BitVaultResponse(e));
					// response.setType(APIConstants.TYPE_ERROR);
					response.setStatus(Constants.FAILED);
					return new ResponseEntity<Response>(response, HttpStatus.OK);

				}
			}
		} else {
			response.setResult(new BitVaultResponse(ErrorMessageConstant.FIELD_IS_EMPTY));
			// response.setType(APIConstants.TYPE_ERROR);
			response.setStatus(Constants.FAILED);
			return new ResponseEntity<Response>(response, HttpStatus.OK);
		}
		return new ResponseEntity<Response>(response, HttpStatus.OK);

	}

	@RequestMapping(value = APIConstants.FIND_APP_CATEGORY_BANNER, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GeneralResponseModel> categoryBanner(
			@RequestParam(defaultValue = Constants.BANNER) String type, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "6") int size, @RequestParam(defaultValue = Constants.PUBLISHED) String status,
			@RequestParam(defaultValue = Constants.UPDATED_AT) String orderBy) {
		GeneralResponseModel response = null;
		Map<String, Object> responseMap = null;
		try {
			List<AppCategory> listOfAppAppCategory = appCategoryService.findCategoryWithApplication(page, size,
					DbConstant.DESC, orderBy);

			// List<AppImage> listOfBanner = appImageService.findBanner(type,
			// status, page, size);

			responseMap = new HashMap<String, Object>();
			responseMap.put(Constants.CATEGORY, listOfAppAppCategory);
			// responseMap.put(Constants.BANNER, listOfBanner);

			response = GeneralResponseModel.of(Constants.SUCCESS, responseMap);

		} catch (BitVaultException e) {
			response = GeneralResponseModel.of(Constants.FAILED, new BitVaultResponse(e));

		}
		return new ResponseEntity<GeneralResponseModel>(response, HttpStatus.OK);

	}

	@GetMapping(value = APIConstants.FIND_APP_BY_CATEGORY)
	public ResponseEntity<?> categoryApp(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size, @RequestParam(defaultValue = DbConstant.DESC) String direction,
			@RequestParam(defaultValue = Constants.UPDATED_AT) String orderBy) {
		Response response = null;
		GeneralResponseModel genralResponse = null;
		Map<String, Object> responseMap = null;
		if (page > 0) {
			page = page - 1;
		}
		try {
			responseMap = appService.findCategoriesApplication(page, size, direction, orderBy);
			int totalPages = (int) responseMap.get(Constants.TOTAL_PAGES);
			int totalRecord = ((Long) responseMap.get(Constants.TOTAL_RECORDS)).intValue();
			int pageSize = (int) responseMap.get(Constants.SIZE);
			response = new Response();
			response.setResult(responseMap.get(Constants.CATEGORY));
			response.setStatus(Constants.SUCCESS);
			response.setSize(pageSize);
			response.setTotalPages(totalPages);
			response.setTotalRecords(totalRecord);
			response.setSort(orderBy);
			// response = GeneralResponseModel.of(Constants.SUCCESS,
			// responseMap);
			return new ResponseEntity<Response>(response, HttpStatus.OK);

		} catch (BitVaultException e) {

			genralResponse = GeneralResponseModel.of(Constants.FAILED, new BitVaultResponse(e));
			return new ResponseEntity<GeneralResponseModel>(genralResponse, HttpStatus.OK);

		}

	}

	@RequestMapping(value = APIConstants.GET_BANNER_APPLICATION + "/{application_id}", method = RequestMethod.GET)
	public ResponseEntity<Response> getBannerApplication(@PathVariable("application_id") Integer applicationID,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size,
			@RequestParam(defaultValue = Constants.UPDATED_AT) String orderBy) {

		Response response = new Response();
		List<Integer> categoryIdList = new ArrayList<>();
		Map<String, Object> allAppMap = null;

		if (page > 0) {
			page = page - 1;
		}

		if (!Utility.isIntegerEmpty(applicationID)) {
			try {
				Application appDto = appService.findApplicationByAppId(applicationID);

				if (null == appDto) {
					throw new BitVaultException(ErrorMessageConstant.RESULT_NOT_FOUND);
				}

				categoryIdList.add(appDto.getAppCategory().getAppCategoryId());

				allAppMap = appService.findApplicationByCategory(categoryIdList, page, size, DbConstant.ASC, orderBy);
				response.setResult(allAppMap.get("appList"));
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
				e.printStackTrace();
				response.setResult(new BitVaultResponse(e));
				response.setStatus(Constants.FAILED);
				return new ResponseEntity<Response>(response, HttpStatus.OK);

			}
		} else {
			response.setResult(new BitVaultResponse(ErrorMessageConstant.FIELD_IS_EMPTY));
			// response.setType(APIConstants.TYPE_ERROR);
			response.setStatus(Constants.FAILED);
			return new ResponseEntity<Response>(response, HttpStatus.OK);
		}
	}

	/**
	 * return latest versions of all appIds given by user
	 * 
	 * @param appIds
	 *            list of app Ids
	 * @return app Ids with their latest versions
	 */
	@PostMapping(value = APIConstants.GET_APPS_LATEST_VERSION)
	public ResponseEntity<GeneralResponseModel> getAppsLatestVersions(@RequestParam List<String> packageNames) {
		GeneralResponseModel customResponse = null;

		try {
			if (Utility.isCollectionEmpty(packageNames)) {
				// return error message
				customResponse = GeneralResponseModel.of(Constants.FAILED,
						new BitVaultResponse(ErrorMessageConstant.FIELD_IS_EMPTY));
			} else {
				final List<ApplicationLatestVersionDTO> appVersionList = appService.getAppsLatestVersions(packageNames);
				if (appVersionList != null) {
					customResponse = GeneralResponseModel.of(Constants.SUCCESS, appVersionList);
				} else {
					throw new BitVaultException(ErrorMessageConstant.RESULT_NOT_FOUND);
				}

			}
		} catch (BitVaultException e) {
			e.printStackTrace();
			customResponse = GeneralResponseModel.of(Constants.FAILED, new BitVaultResponse(e));
		}
		return new ResponseEntity<GeneralResponseModel>(customResponse, HttpStatus.OK);
	}

	@PostMapping(value = APIConstants.GET_APPS_PERMISION)
	public ResponseEntity<GeneralResponseModel> getAppPermisions(@RequestParam Integer appId) {
		GeneralResponseModel customResponse = null;

		try {
			if (Utility.isIntegerEmpty(appId)) {
				// return error message
				customResponse = GeneralResponseModel.of(Constants.FAILED,
						new BitVaultResponse(ErrorMessageConstant.FIELD_IS_EMPTY));
			} else {
				Application app = appService.findApplicationByAppId(appId);
				if (app != null) {
					customResponse = GeneralResponseModel.of(Constants.SUCCESS, app.getAppPermission());
				} else {
					throw new BitVaultException(ErrorMessageConstant.RESULT_NOT_FOUND);
				}

			}
		} catch (BitVaultException e) {
			e.printStackTrace();
			customResponse = GeneralResponseModel.of(Constants.FAILED, new BitVaultResponse(e));
		}
		return new ResponseEntity<GeneralResponseModel>(customResponse, HttpStatus.OK);
	}

	@PostMapping(value = APIConstants.FIND_PUBLISHED_APPLICATION_BY_APPTYPE)
	public ResponseEntity<Response> listByAppType(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size,
			@RequestParam(defaultValue = Constants.UPDATED_AT) String orderBy, @RequestParam Integer appTypeId) {
		if (page > 0) {
			page = page - 1;
		}
		Response response = new Response();
		Map<String, Object> allAppMap = null;

		if (!Utility.isIntegerEmpty(appTypeId)) {
			try {
				ApplicationTypeDto applicationType = appTypeService.findAppTypeById(appTypeId);
				if (applicationType != null) {
					allAppMap = appService.listOfAllApplicationByAppType(page, size, DbConstant.ASC, orderBy,
							appTypeId);
					logger.info("List of particular ApType fetched successfully");
					response.setResult(allAppMap.get("appList"));
					int totalPages = (int) allAppMap.get(Constants.TOTAL_PAGES);
					int totalRecord = ((Long) allAppMap.get(Constants.TOTAL_RECORDS)).intValue();
					int pageSize = (int) allAppMap.get(Constants.SIZE);
					response.setStatus(Constants.SUCCESS);
					response.setSize(pageSize);
					response.setTotalPages(totalPages);
					response.setTotalRecords(totalRecord);
					response.setSort(orderBy);
					return new ResponseEntity<Response>(response, HttpStatus.OK);
				} else {
					throw new BitVaultException(ErrorMessageConstant.INVALID_APP_TYPE_ID);
				}

			} catch (BitVaultException e) {
				logger.error("Error occured during list fetching ");
				response.setResult(new BitVaultResponse(e));

				response.setStatus(Constants.FAILED);
				return new ResponseEntity<Response>(response, HttpStatus.EXPECTATION_FAILED);

			}

		} else {
			response.setResult(new BitVaultResponse(ErrorMessageConstant.FIELD_IS_EMPTY));
			response.setStatus(Constants.FAILED);
			return new ResponseEntity<Response>(response, HttpStatus.OK);
		}
	}

	@RequestMapping(value = APIConstants.ELASTIC_SEARCH_APP, method = RequestMethod.GET)
	public ResponseEntity<Response> elasticSearchApplication(@RequestParam String name,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size,
			@RequestParam(defaultValue = DbConstant.APPNAME) String orderBy) {
		Response response = new Response();
		Map<String, Object> allAppMap = null;
		if (page > 0) {
			page = page - 1;
		}
		if (!Utility.isStringEmpty(name)) {
		//	if (name.length() > 2) {
				try

				{
					allAppMap = appService.searchApplicationByAppName(name, page, size, DbConstant.DESC, orderBy);
                    logger.info("Application Searched Successfully ");
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
					logger.error("Error occured during application searching");
					response.setResult(new BitVaultResponse(e));
					// response.setType(APIConstants.TYPE_ERROR);
					response.setStatus(Constants.FAILED);
					return new ResponseEntity<Response>(response, HttpStatus.OK);

				}
			//}
		} else {
			response.setResult(new BitVaultResponse(ErrorMessageConstant.FIELD_IS_EMPTY));
			// response.setType(APIConstants.TYPE_ERROR);
			response.setStatus(Constants.FAILED);
			return new ResponseEntity<Response>(response, HttpStatus.OK);
		}
		//return new ResponseEntity<Response>(response, HttpStatus.OK);

	}
	
	@GetMapping(value = APIConstants.FIND_NATIVE_APPLICATION)
	public ResponseEntity<Response> listByAppType(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size,
			@RequestParam(defaultValue = Constants.UPDATED_AT) String orderBy,
			@RequestParam(defaultValue = "published") String status) {
		if (page > 0) {
			page = page - 1;
		}
		Response response = new Response();
		Map<String, Object> allAppMap = null;

		if (!Utility.isStringEmpty(status)) {
			try {

				allAppMap = appService.findNativeApplication(status, page, size, DbConstant.ASC, orderBy);
				logger.info("Native Apps Fetched Successfully");
				response.setResult(allAppMap.get("appList"));
				int totalPages = (int) allAppMap.get(Constants.TOTAL_PAGES);
				int totalRecord = ((Long) allAppMap.get(Constants.TOTAL_RECORDS)).intValue();
				int pageSize = (int) allAppMap.get(Constants.SIZE);
				response.setStatus(Constants.SUCCESS);
				response.setSize(pageSize);
				response.setTotalPages(totalPages);
				response.setTotalRecords(totalRecord);
				response.setSort(orderBy);
				return new ResponseEntity<Response>(response, HttpStatus.OK);
			}

			catch (BitVaultException e) {
				logger.error("Error occured during list fetching ");
				response.setResult(new BitVaultResponse(e));

				response.setStatus(Constants.FAILED);
				return new ResponseEntity<Response>(response, HttpStatus.EXPECTATION_FAILED);

			}

		} else {
			response.setResult(new BitVaultResponse(ErrorMessageConstant.FIELD_IS_EMPTY));
			response.setStatus(Constants.FAILED);
			return new ResponseEntity<Response>(response, HttpStatus.OK);
		}
	}
}
