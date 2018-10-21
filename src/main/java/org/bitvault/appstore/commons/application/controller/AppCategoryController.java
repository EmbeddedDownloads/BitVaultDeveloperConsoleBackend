package org.bitvault.appstore.commons.application.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bitvault.appstore.cloud.constant.APIConstants;
import org.bitvault.appstore.cloud.constant.Constants;
import org.bitvault.appstore.cloud.constant.DbConstant;
import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.dto.AppApplication;
import org.bitvault.appstore.cloud.dto.AppCategory;
import org.bitvault.appstore.cloud.dto.ApplicationTypeDto;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.bitvault.appstore.cloud.exception.BitVaultResponse;
import org.bitvault.appstore.cloud.user.common.GeneralResponseModel;
import org.bitvault.appstore.cloud.utils.Utility;
import org.bitvault.appstore.commons.application.service.AppApplicationService;
import org.bitvault.appstore.commons.application.service.AppCategoryService;
import org.bitvault.appstore.commons.application.service.AppTypeService;
import org.bitvault.appstore.commons.application.service.ApplicationService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping(value = APIConstants.DEV_API_BASE)
public class AppCategoryController {

	private final Logger logger = org.slf4j.LoggerFactory.getLogger(AppCategoryController.class);

	@Autowired
	AppCategoryService appCategoryService;

	@Autowired
	AppTypeService appTypeService;

	@Autowired
	AppApplicationService appAppService;

	@Autowired
	ApplicationService applicationService;

	@RequestMapping(value = APIConstants.FIND_ALL_APP_CATEGORY, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GeneralResponseModel> listOfAllApplication(
			@RequestParam(defaultValue = Constants.CATEGORY_STATUS_ALL) List<String> status,
			@RequestParam(defaultValue = Constants.UPDATED_AT) String orderBy,
			@RequestParam(defaultValue = DbConstant.DESC) String direction) {
		GeneralResponseModel response = null;
		try {
			List<AppCategory> listOfAppAppCategory = appCategoryService.findAppCategoryByStatus(status, 0,
					Integer.MAX_VALUE, direction, orderBy);
			response = GeneralResponseModel.of(Constants.SUCCESS, listOfAppAppCategory);
			return new ResponseEntity<GeneralResponseModel>(response, HttpStatus.OK);

		} catch (BitVaultException e) {
			response = GeneralResponseModel.of(Constants.FAILED, new BitVaultResponse(e));

			return new ResponseEntity<GeneralResponseModel>(response, HttpStatus.OK);

		}

	}

	@RequestMapping(value = APIConstants.APPLICATION_TYPE, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GeneralResponseModel> applicationType() {
		GeneralResponseModel response = null;
		try {
			List<ApplicationTypeDto> listOfApptype = appTypeService.appType();
			response = GeneralResponseModel.of(Constants.SUCCESS, listOfApptype);

		} catch (BitVaultException e) {
			response = GeneralResponseModel.of(Constants.FAILED, new BitVaultResponse(e));

		}
		return new ResponseEntity<GeneralResponseModel>(response, HttpStatus.OK);
	}

	@GetMapping(value = APIConstants.APPCATEGORY_BY_CATEGORYID)
	public ResponseEntity<GeneralResponseModel> findAppCategoryById(@RequestParam Integer appCategoryId) {
		GeneralResponseModel response = null;
		try {
			if (!Utility.isIntegerEmpty(appCategoryId)) {
				AppCategory appCategory = appCategoryService.findCategoryById(appCategoryId);

				if (null == appCategory) {
					throw new BitVaultException(ErrorMessageConstant.RESULT_NOT_FOUND);
				}

				response = GeneralResponseModel.of(Constants.SUCCESS, appCategory);
			} else {
				throw new BitVaultException(ErrorMessageConstant.FIELD_IS_EMPTY);

			}
		} catch (BitVaultException e) {
			response = GeneralResponseModel.of(Constants.FAILED, new BitVaultResponse(e));

		}
		return new ResponseEntity<GeneralResponseModel>(response, HttpStatus.OK);

	}

	@PostMapping(value = APIConstants.CATEGORY_STATUS)
	public ResponseEntity<GeneralResponseModel> updateCategoryStatus(@RequestBody Map<String, Object> requestMap) {
		GeneralResponseModel response = null;
		String status = (String) requestMap.get(Constants.STATUS);
		Integer categoryId = (Integer) requestMap.get(Constants.CATEGORY_ID);

		try {
			if (!Utility.isStringEmpty(status) && !Utility.isIntegerEmpty(categoryId)) {

				AppCategory appCategoryDto = appCategoryService.findCategoryById(categoryId);
				if (null != appCategoryDto) {
					int count = appAppService.getCountAppByCategoryId(categoryId);
					if (count > 0) {
						response = GeneralResponseModel.of(Constants.FAILED,
								count + ErrorMessageConstant.APPCOUTBYCATEGORY_ERROR);
					} else {
						appCategoryService.updateAppCategoryByCategoryId(status, categoryId);
					}
				} else {
					throw new BitVaultException(ErrorMessageConstant.RESULT_NOT_FOUND);

				}
			} else {
				throw new BitVaultException(ErrorMessageConstant.FIELD_IS_EMPTY);
			}
		} catch (BitVaultException e) {
			// TODO: handle exception
		}
		return null;
	}

	@GetMapping(value = APIConstants.CATEGORY_BY_APPTYPE + "/{appTypeId}")
	public ResponseEntity<GeneralResponseModel> findAppCategoryByAppTypeId(@PathVariable Integer appTypeId,
			@RequestParam(defaultValue = DbConstant.ACTIVE) List<String> status) {
		GeneralResponseModel response = null;
		try {
			List<AppCategory> listOfAppAppCategory = appCategoryService.findAppCategoryByAppTypeId(appTypeId, status);
			response = GeneralResponseModel.of(Constants.SUCCESS, listOfAppAppCategory);
			return new ResponseEntity<GeneralResponseModel>(response, HttpStatus.OK);

		} catch (BitVaultException e) {
			response = GeneralResponseModel.of(Constants.FAILED, new BitVaultResponse(e));

			return new ResponseEntity<GeneralResponseModel>(response, HttpStatus.OK);

		}

	}

	@PostMapping(value = APIConstants.SAVECATEGORY)
	public ResponseEntity<GeneralResponseModel> saveAppCategory(@RequestParam(required = false) Integer appCategoryId,
			@RequestParam Integer appTypeId, @RequestParam String categoryType, @RequestParam String description,
			@RequestParam(defaultValue = Constants.ACTIVE) String status,
			@RequestParam(required = false) MultipartFile categoryIcon,
			@RequestParam(required = false) MultipartFile categoryBanner) {
		GeneralResponseModel response = null;
		AppCategory appCategoryDto = null;

		if (!Utility.isStringEmpty(categoryType) && !Utility.isIntegerEmpty(appTypeId)) {
			try {
				ApplicationTypeDto appType = appTypeService.findAppTypeById(appTypeId);
				if (null == appType) {
					throw new BitVaultException(ErrorMessageConstant.INVALID_APPTYPEID);
				}
				appCategoryDto = appCategoryService.findCategoryByName(categoryType);
				if (null != appCategoryDto) {

					if (Utility.isIntegerEmpty(appCategoryId)
							|| appCategoryDto.getAppCategoryId().intValue() != appCategoryId.intValue()) {
						throw new BitVaultException(ErrorMessageConstant.INVALID_CATEGORY_EXIST);
					}
				} else if (!Utility.isIntegerEmpty(appCategoryId)) {
					appCategoryDto = appCategoryService.findCategoryById(appCategoryId);

				}

				if (null != appCategoryDto) {
					if (appCategoryDto.getAppTypeId().intValue() != appTypeId.intValue()) {
						throw new BitVaultException(ErrorMessageConstant.INVALID_APPTYPEID);

					}
					if (status.equalsIgnoreCase(Constants.INACTIVE)) {
						int count = appAppService.getCountAppByCategoryId(appCategoryId);
						if (count > 0) {
							response = GeneralResponseModel.of(Constants.FAILED,
									new BitVaultResponse(count + ErrorMessageConstant.APPCOUTBYCATEGORY_ERROR));
							return new ResponseEntity<GeneralResponseModel>(response, HttpStatus.OK);
						}

					}
					appCategoryDto.setStatus(status);
					appCategoryDto.setCategoryType(categoryType);
					appCategoryDto.setDescription(description);
					if (null != categoryIcon && !categoryIcon.isEmpty()) {

						appCategoryDto.setCategoryIconUrl(appCategoryService.uploadCategoryIcon(categoryIcon,
								appCategoryDto.getCategoryIconUrl()));

					}

					// upload category banner

					if (null != categoryBanner && !categoryBanner.isEmpty()) {

						appCategoryDto.setCategoryBannerUrl(appCategoryService.uploadCategoryBanner(categoryBanner,
								appCategoryDto.getCategoryBannerUrl()));

					}

					org.bitvault.appstore.cloud.model.AppCategory appCategoryEntity = appCategoryDto
							.populateAppCategoryDTO(appCategoryDto);
					appCategoryEntity.setApplicationType(appType.populateApplicationTypeDto(appType));
					appCategoryDto = appCategoryService.saveAppCategory(appCategoryEntity);
				} else {
					String iconUrl = null, bannerUrl = null;

					if (null != categoryIcon && !categoryIcon.isEmpty()) {
						iconUrl = appCategoryService.uploadCategoryIcon(categoryIcon, null);

					} else {
						throw new BitVaultException(ErrorMessageConstant.NO_FILE_SELECTED);
					}

					if (null != categoryBanner && !categoryBanner.isEmpty()) {
						bannerUrl = appCategoryService.uploadCategoryBanner(categoryBanner, null);

					} else {
						throw new BitVaultException(ErrorMessageConstant.NO_BANNER_SELECTED);
					}

					org.bitvault.appstore.cloud.model.AppCategory appCategoryEntity = new org.bitvault.appstore.cloud.model.AppCategory();
					appCategoryEntity.setCategoryIconUrl(iconUrl);
					appCategoryEntity.setStatus(status);
					appCategoryEntity.setCategoryCount(0);
					appCategoryEntity.setCategoryType(categoryType);
					appCategoryEntity.setDescription(description);
					appCategoryEntity.setCategoryBannerUrl(bannerUrl);
					appCategoryEntity.setApplicationType(appType.populateApplicationTypeDto(appType));

					appCategoryDto = appCategoryService.saveAppCategory(appCategoryEntity);
				}

				response = GeneralResponseModel.of(Constants.SUCCESS, appCategoryDto);

				return new ResponseEntity<GeneralResponseModel>(response, HttpStatus.OK);

			} catch (BitVaultException e) {
				e.printStackTrace();
				return new ResponseEntity<GeneralResponseModel>(
						GeneralResponseModel.of(Constants.FAILED, new BitVaultResponse(e)), HttpStatus.OK);
			}
		}

		else {
			return new ResponseEntity<GeneralResponseModel>(GeneralResponseModel.of(Constants.FAILED,
					new BitVaultResponse(ErrorMessageConstant.FIELD_IS_EMPTY)), HttpStatus.OK);
		}
	}

	@PutMapping(value = APIConstants.UPDATE_CATEGORY)
	public ResponseEntity<?> updateCategory(@RequestBody Map<String, Object> inputMap) {
		try {
			Integer appTypeId = (Integer) inputMap.get("appTypeId");
			Integer appCategoryId = (Integer) inputMap.get("appCategoryId");
			if (Utility.isIntegerEmpty(appTypeId) || Utility.isIntegerEmpty(appCategoryId)) {
				return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED,
						new BitVaultResponse(ErrorMessageConstant.FIELD_IS_EMPTY)));
			}
			ApplicationTypeDto appTypeDto = appTypeService.findAppTypeById(appTypeId);
			if (appTypeDto == null) {
				return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED,
						new BitVaultResponse(ErrorMessageConstant.INVALID_APP_TYPE_ID)));
			}
			AppCategory appCategoryDto = appCategoryService.findCategoryById(appCategoryId);
			if (appCategoryDto == null || (appCategoryDto.getAppTypeId().intValue() != appTypeId.intValue())) {
				return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED,
						new BitVaultResponse(ErrorMessageConstant.INVALID_CATEGORYID)));
			}
			List<Integer> appIdList = (ArrayList<Integer>) inputMap.get("checked");
			if (appIdList.size() == 0) {
				return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED,
						new BitVaultResponse(ErrorMessageConstant.NO_APPLICATION_SELECTED)));
			}

//			for (Integer appId : appIdList) {
				AppApplication appApplication = appAppService.findApplicationByAppId(appIdList.get(0));
				int currentAppCategoryCount = appApplication.getAppCategory().getAppCategoryId();
				AppCategory appCategoryPrevious = appCategoryService.findCategoryById(currentAppCategoryCount);
				
				if (appCategoryPrevious.getCategoryCount() - appIdList.size() >= 0) {
					appCategoryPrevious.setCategoryCount(appCategoryPrevious.getCategoryCount() - appIdList.size());
					appCategoryService.saveAppCategory(appCategoryPrevious.populateAppCategoryDTOWithAppType(appCategoryPrevious));
				}

//			}

			appAppService.updateAppApplication(appIdList, appCategoryId);
			applicationService.updateApplication(appIdList, appCategoryId);
			AppCategory appCategory = appCategoryService.findCategoryById(appCategoryId);

			appCategory.setCategoryCount(appCategory.getCategoryCount() + appIdList.size());
			appCategoryService.saveAppCategory(appCategory.populateAppCategoryDTOWithAppType(appCategory));

			return ResponseEntity
					.ok(GeneralResponseModel.of(Constants.SUCCESS, new BitVaultResponse(Constants.SUCCESS_UPDATED)));
		} catch (Exception e) {
			logger.error(e.getMessage());
			return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED, new BitVaultResponse(e.getMessage())));
		}
	}

	@GetMapping(value = APIConstants.CATEGORY_DETAIL)
	public ResponseEntity<?> categoryDetail(@RequestParam Integer appCategoryId,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = Constants.UPDATED_AT) String orderBy,
			@RequestParam(defaultValue = DbConstant.DESC) String direction,
			@RequestParam(defaultValue = DbConstant.ACTIVE) List<String> status) {
		GeneralResponseModel genralResponse = null;

		Map<String, Object> allAppMap = null;
		Map<String, Object> responseMap = null;

		try {
			if (!Utility.isIntegerEmpty(appCategoryId)) {
				AppCategory appCategory = appCategoryService.findCategoryById(appCategoryId);

				if (null == appCategory) {
					throw new BitVaultException(ErrorMessageConstant.RESULT_NOT_FOUND);
				}
				if (page > 0) {
					page = page - 1;
				}
				List<Integer> catList = new ArrayList<Integer>();
				catList.add(appCategoryId);

				allAppMap = appAppService.findAppApplicationByCategory(catList, page, size, direction, orderBy);

				List<AppCategory> listOfAppAppCategory = appCategoryService
						.findAppCategoryByAppTypeId(appCategory.getAppTypeId(), status);

				responseMap = new HashMap<String, Object>();
				responseMap.put("application", allAppMap);
				responseMap.put("categoryDetail", appCategory);
				responseMap.put("categoryByAppType", listOfAppAppCategory);
				genralResponse = GeneralResponseModel.of(Constants.SUCCESS, responseMap);
			} else {
				throw new BitVaultException(ErrorMessageConstant.FIELD_IS_EMPTY);

			}
		} catch (BitVaultException e) {
			genralResponse = GeneralResponseModel.of(Constants.FAILED, new BitVaultResponse(e));

		}
		return new ResponseEntity<GeneralResponseModel>(genralResponse, HttpStatus.OK);

	}
}
