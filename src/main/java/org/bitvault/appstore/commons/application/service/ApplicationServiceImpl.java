package org.bitvault.appstore.commons.application.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bitvault.appstore.cloud.constant.Constants;
import org.bitvault.appstore.cloud.constant.DbConstant;
import org.bitvault.appstore.cloud.constant.ErrorCode;
import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.dto.AppDetail;
import org.bitvault.appstore.cloud.dto.Application;
import org.bitvault.appstore.cloud.dto.ApplicationDetailDto;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.bitvault.appstore.cloud.model.AppApplication;
import org.bitvault.appstore.cloud.model.AppCategory;
import org.bitvault.appstore.cloud.model.ApplicationType;
import org.bitvault.appstore.commons.application.dao.AppCategoryRepository;
import org.bitvault.appstore.commons.application.dao.ApplicationRepository;
import org.bitvault.appstore.commons.application.elasticdao.ApplicationElasticRepository;
import org.bitvault.appstore.mobile.dto.ApplicationLatestVersionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service("ApplicationService")
@Transactional
public class ApplicationServiceImpl implements ApplicationService {
	@Autowired
	ApplicationRepository appRepository;

	@Autowired
	AppDetailService appDetailService;

	@Autowired
	AppCategoryService appCategoryService;

	@Autowired
	AppCategoryRepository appCategoryRepository;
	@Autowired
	ElasticsearchTemplate elasticSearchTemplate;

	@Autowired
	ApplicationElasticRepository elasticRepository;
	
	public static final Logger logger = LoggerFactory.getLogger(ApplicationServiceImpl.class);

	@Override
	public Application saveApllication(org.bitvault.appstore.cloud.model.Application application)
			throws BitVaultException {
		Application appDto = null;
		try {
			org.bitvault.appstore.cloud.model.Application app = appRepository.saveAndFlush(application);
			appDto = app.populateApplicationDTO(app);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.UNABLE_TO_SAVE);
		}

		return appDto;
	}

	@Override
	public Map<String, Object> listOfAllApplication(int page, int size, String direction, String property)
			throws BitVaultException {
		Application appDTO = null;
		Page<org.bitvault.appstore.cloud.model.Application> appList = null;
		Map<String, Object> allAppMap = null;

		List<Application> appDTOList = new ArrayList<Application>();
		try {
			if (direction.equals(DbConstant.ASC.toString())) {
				appList = appRepository.findAll(new PageRequest(page, size, Sort.Direction.ASC, property));
			} else {
				appList = appRepository.findAll(new PageRequest(page, size, Sort.Direction.DESC, property));

			}
			for (org.bitvault.appstore.cloud.model.Application application : appList) {
				// AppCategory appCategory = application.getAppCategory();

				appDTO = application.populateApplicationDTO(application);
				appDTO.setApkUrl("");
				// appDTO.setAppCategory(appCategory.populateAppCategoryDTO(appCategory));
				appDTOList.add(appDTO);
			}
			allAppMap = new HashMap<String, Object>();
			allAppMap.put("appList", appDTOList);
			allAppMap.put(Constants.TOTAL_PAGES, appList.getTotalPages());
			allAppMap.put(Constants.TOTAL_RECORDS, appList.getTotalElements());
			allAppMap.put(Constants.SIZE, appList.getNumberOfElements());
			allAppMap.put(Constants.SORT, appList.getSort());
		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);
		}

		return allAppMap;

	}

	@Override
	public Application findApplicationByPackageName(String packageName) throws BitVaultException {
		org.bitvault.appstore.cloud.model.Application app = null;
		Application appDTO = null;
		try {
			app = appRepository.findApplicationByPackageName(packageName);

		} catch (Exception e) {
			e.printStackTrace();

			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);
		}
		if (null != app) {
			appDTO = app.populateApplicationDTO(app);
		}
		return appDTO;
	}

	@Override

	public Map<String, Object> findApplicationByUserId(String userId, int page, int size, String direction,
			String property) throws BitVaultException {
		Application appDTO = null;
		Page<org.bitvault.appstore.cloud.model.Application> appList = null;
		Map<String, Object> allAppMap = null;

		List<Application> appDTOList = new ArrayList<Application>();
		try {
			if (direction.equals(DbConstant.ASC.toString())) {
				appList = appRepository.findApplicationByUserId(userId,
						new PageRequest(page, size, Sort.Direction.ASC, property));
			} else {
				appList = appRepository.findApplicationByUserId(userId,
						new PageRequest(page, size, Sort.Direction.DESC, property));

			}
			for (org.bitvault.appstore.cloud.model.Application application : appList) {

				// AppCategory appCategory = application.getAppCategory();

				appDTO = application.populateApplicationDTO(application);
				// appDTO.setApkUrl("");
				// appDTO.setAppCategory(appCategory.populateAppCategoryDTO(appCategory));
				appDTOList.add(appDTO);

			}
			allAppMap = new HashMap<String, Object>();
			allAppMap.put("appList", appDTOList);
			allAppMap.put(Constants.TOTAL_PAGES, appList.getTotalPages());
			allAppMap.put(Constants.TOTAL_RECORDS, appList.getTotalElements());
			allAppMap.put(Constants.SIZE, appList.getNumberOfElements());
			allAppMap.put(Constants.SORT, appList.getSort());
		} catch (Exception e) {
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED, ErrorCode.SOME_ERROR_OCCURED_CODE);
		}

		return allAppMap;
	}

	@Override
	public Map<String, Object> findApplicationByCategory(List<Integer> categoryIdList, int page, int size,
			String direction, String property) throws BitVaultException {
		Application appDTO = null;
		Page<org.bitvault.appstore.cloud.model.Application> appList = null;
		Map<String, Object> allAppMap = null;

		List<Application> appDTOList = new ArrayList<Application>();
		try {
			if (direction.equals(DbConstant.ASC.toString())) {
				appList = appRepository.findApplicationByCategory(categoryIdList,
						new PageRequest(page, size, Sort.Direction.ASC, property));
			} else {
				appList = appRepository.findApplicationByCategory(categoryIdList,
						new PageRequest(page, size, Sort.Direction.DESC, property));

			}
			for (org.bitvault.appstore.cloud.model.Application application : appList) {
				// AppCategory appCategory = application.getAppCategory();

				appDTO = application.populateApplicationDTO(application);
				appDTO.setApkUrl("");
				// appDTO.setAppCategory(appCategory.populateAppCategoryDTO(appCategory));
				appDTOList.add(appDTO);
			}
			allAppMap = new HashMap<String, Object>();
			allAppMap.put("appList", appDTOList);
			allAppMap.put(Constants.TOTAL_PAGES, appList.getTotalPages());
			allAppMap.put(Constants.TOTAL_RECORDS, appList.getTotalElements());
			allAppMap.put(Constants.SIZE, appList.getNumberOfElements());
			allAppMap.put(Constants.SORT, appList.getSort());
		} catch (Exception e) {
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED, ErrorCode.SOME_ERROR_OCCURED_CODE);
		}

		return allAppMap;

	}

	@Override
	public Integer deleteUnpublishedAppByPackageName(String packageName) throws BitVaultException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Application findApplicationByAppId(Integer applicationId) throws BitVaultException {
		org.bitvault.appstore.cloud.model.Application app = null;
		Application appDTO = null;
		try {
			app = appRepository.findApplicationByAppId(applicationId);

		} catch (Exception e) {

			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED, ErrorCode.SOME_ERROR_OCCURED_CODE);
		}
		if (null != app) {

			appDTO = app.populateApplicationDTO(app);
			// appDTO.setApkUrl("");
			// AppCategory appCategory = app.getAppCategory();
			// appDTO.setAppCategory(appCategory.populateAppCategoryDTO(appCategory));
			// ApplicationType appType = app.getApplicationType();
			// appDTO.setApplicationType(appType.populateAppTypeDto(appType));

		}
		return appDTO;
	}

	@Override
	public String findAppApkUrlByAppId(Integer appId) throws BitVaultException {
		String appUrl = null;

		try {
			appUrl = appRepository.findAppApkUrlByAppId(appId);

		} catch (Exception e) {

			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED, ErrorCode.SOME_ERROR_OCCURED_CODE);
		}
		if (null == appUrl || appUrl.trim().isEmpty()) {
			throw new BitVaultException(ErrorMessageConstant.RESULT_NOT_FOUND, ErrorCode.RESULT_NOT_FOUND_CODE);
		}
		return appUrl;
	}

	@Override
	public String findAppApkUrlByPackageName(String packageName) throws BitVaultException {
		String appUrl = null;

		try {
			appUrl = appRepository.findAppApkUrlByPackageName(packageName);

		} catch (Exception e) {

			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED, ErrorCode.SOME_ERROR_OCCURED_CODE);
		}
		if (null == appUrl || appUrl.trim().isEmpty()) {
			throw new BitVaultException(ErrorMessageConstant.RESULT_NOT_FOUND, ErrorCode.RESULT_NOT_FOUND_CODE);
		}
		return appUrl;
	}

	@Override
	public ApplicationDetailDto findPublishedAppDetailbyAppId(Integer appId) throws BitVaultException {
		org.bitvault.appstore.cloud.model.Application app = null;
		Application appDTO = null;
		AppDetail appDetail = null;
		ApplicationDetailDto applicationDetailDto = null;

		try {
			app = appRepository.findApplicationByAppId(appId);
			if (null != app) {
				appDetail = appDetailService.findApplicationDetailByAppIdAndVersion(app.getApplicationId(),
						app.getLatestVersion(), Constants.PUBLISHED);
				// appDetail.setApkUrl("");
				appDTO = app.populateApplicationDTO(app);
				// appDTO.setApkUrl("");
				applicationDetailDto = ApplicationDetailDto.populateApplicationDetailDto(appDetail, appDTO);
				// applicationDetailDto.setApkUrl("");

			} else {
				throw new BitVaultException(ErrorMessageConstant.RESULT_NOT_FOUND);

			}
		} catch (Exception e) {
			if (e instanceof BitVaultException) {
				throw new BitVaultException(e.getMessage());

			}
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);
		}

		return applicationDetailDto;
	}

	@Override
	public Map<String, Object> findApplicationByAppName(String appName, int page, int size, String direction,
			String property) throws BitVaultException {
		Page<org.bitvault.appstore.cloud.model.Application> appList = null;
		org.bitvault.appstore.cloud.dto.Application appDTO = null;
		List<org.bitvault.appstore.cloud.dto.Application> appDTOList = new ArrayList<org.bitvault.appstore.cloud.dto.Application>();
		Map<String, Object> allAppMap = null;

		try {
			if (direction.equals(DbConstant.ASC)) {
				appList = appRepository.findApplicationByAppName(appName,
						new PageRequest(page, size, Sort.Direction.ASC, property));
			} else {
				appList = appRepository.findApplicationByAppName(appName,
						new PageRequest(page, size, Sort.Direction.DESC, property));

			}
			for (org.bitvault.appstore.cloud.model.Application application : appList) {
				// org.bitvault.appstore.cloud.model.AppCategory appCategory =
				// application.getAppCategory();
				appDTO = application.populateApplicationDTO(application);
				// appDTO.setAppCategory(appCategory.populateAppCategoryDTO(appCategory));
				appDTO.setApkUrl("");
				appDTOList.add(appDTO);
			}
			allAppMap = new HashMap<String, Object>();
			allAppMap.put("appList", appDTOList);
			allAppMap.put(Constants.TOTAL_PAGES, appList.getTotalPages());
			allAppMap.put(Constants.TOTAL_RECORDS, appList.getTotalElements());
			allAppMap.put(Constants.SORT, appList.getSort());
			allAppMap.put(Constants.SIZE, appList.getNumberOfElements());

		} catch (Exception e) {
			e.printStackTrace();

			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED, ErrorCode.SOME_ERROR_OCCURED_CODE);
		}

		return allAppMap;

	}

	@Override
	public int getActiveAppsCount() {
		return appRepository.getActiveAppsCount();
	}

	@Override
	public void deleteApp(Integer appId) throws BitVaultException {

		try {
			appRepository.delete(appId);
			deleteDocument(appId.toString());
		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.UNABLE_TO_DELETE);
		}

	}

	@Override
	public int upadteAppIconById(Integer appId, String appIconUrl) throws BitVaultException {
		int update = 0;
		try {
			update = appRepository.upadateAppIconById(appId, appIconUrl);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.UNABLE_TO_UPDATE);
		}
		return update;
	}

	@Override
	public Map<String, Object> findCategoriesApplication(int page, int size, String direction, String property)
			throws BitVaultException {
		Map<String, Object> categoryAppMap = null;
		List<Object> responseList = null;
		Map<String, Object> categoryIdAppMap = null;

		Page<AppCategory> categoryList = null;
		Application appDto = null;

		List<Application> appDtoList = null;
		try {
			if (direction.equals(DbConstant.ASC)) {
				categoryList = appCategoryRepository
						.findCategoryWithApplication(new PageRequest(page, size, new Sort(Direction.ASC, property)));
			} else {
				categoryList = appCategoryRepository
						.findCategoryWithApplication(new PageRequest(page, size, new Sort(Direction.DESC, property)));

			}
			categoryAppMap = new HashMap<String, Object>();
			responseList = new ArrayList<Object>();

			for (AppCategory appCategory : categoryList) {
				categoryIdAppMap = new HashMap<String, Object>();
				appDtoList = new ArrayList<Application>();

				Page<org.bitvault.appstore.cloud.model.Application> appList = appRepository
						.findApplicationByCategoryById(appCategory.getAppCategoryId(),
								new PageRequest(page, size, new Sort(Direction.ASC, property)));

				for (org.bitvault.appstore.cloud.model.Application application : appList) {
					appDto = application.populateApplicationDTO(application);
					appDtoList.add(appDto);
				}
				if (appDtoList.size() != 0) {
					categoryIdAppMap.put(Constants.CATEGORY_ID, appCategory.getAppCategoryId());
					categoryIdAppMap.put(Constants.APPLICATIONS, appDtoList);
					categoryIdAppMap.put("categoryName", appCategory.getCategoryType());
					categoryIdAppMap.put(Constants.TOTAL_PAGES, appList.getTotalPages());
					categoryIdAppMap.put(Constants.TOTAL_RECORDS, appList.getTotalElements());
					categoryIdAppMap.put(Constants.SORT, appList.getSort().getOrderFor(property).getProperty());
					categoryIdAppMap.put(Constants.SIZE, appList.getNumberOfElements());
					responseList.add(categoryIdAppMap);
				}

			}
			categoryAppMap.put(Constants.CATEGORY, responseList);
			categoryAppMap.put(Constants.TOTAL_PAGES, categoryList.getTotalPages());
			categoryAppMap.put(Constants.TOTAL_RECORDS, categoryList.getTotalElements());
			categoryAppMap.put(Constants.SORT, categoryList.getSort().getOrderFor(property).getProperty());
			categoryAppMap.put(Constants.SIZE, categoryList.getNumberOfElements());
		} catch (Exception e) {

			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);
		}
		return categoryAppMap;
	}

	@Override
	public List<ApplicationLatestVersionDTO> getAppsLatestVersions(final List<String> packageNames)
			throws BitVaultException {
		List<ApplicationLatestVersionDTO> appLatestVersionList = null;
		try {
			appLatestVersionList = appRepository.findAppsLatestVersions(packageNames);

		} catch (final Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);
		}
		return appLatestVersionList;
	}

	@Override
	public void updateApplication(List<Integer> appIdList, Integer categoryId) {
		appRepository.upadateApplication(appIdList, categoryId);

	}

	@Override
	public Map<String, Object> listOfAllApplicationByAppType(int page, int size, String direction, String property,
			int appTypeId) throws BitVaultException {

		Application appDTO = null;
		Page<org.bitvault.appstore.cloud.model.Application> appList = null;
		Map<String, Object> allAppMap = null;

		List<Application> appDTOList = new ArrayList<Application>();
		try {
			if (direction.equals(DbConstant.ASC.toString())) {
				appList = appRepository.findApplicationByAppTypeId(appTypeId,
						new PageRequest(page, size, Sort.Direction.ASC, property));
			} else {
				appList = appRepository.findApplicationByAppTypeId(appTypeId,
						new PageRequest(page, size, Sort.Direction.DESC, property));

			}
			for (org.bitvault.appstore.cloud.model.Application application : appList) {

				appDTO = application.populateApplicationDTO(application);
				appDTO.setApkUrl("");
				appDTOList.add(appDTO);
			}
			allAppMap = new HashMap<String, Object>();
			allAppMap.put("appList", appDTOList);
			allAppMap.put(Constants.TOTAL_PAGES, appList.getTotalPages());
			allAppMap.put(Constants.TOTAL_RECORDS, appList.getTotalElements());
			allAppMap.put(Constants.SIZE, appList.getNumberOfElements());
			allAppMap.put(Constants.SORT, appList.getSort().getOrderFor(property).getProperty());
		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);
		}

		return allAppMap;

	}

	@Override
	public Map<String, Object> searchApplicationByAppName(String appName, int page, int size, String direction,
			String property) throws BitVaultException {
		Page<org.bitvault.appstore.cloud.model.Application> appList = null;
		org.bitvault.appstore.cloud.dto.ApplicationElasticDto appDTO = null;
		List<org.bitvault.appstore.cloud.dto.ApplicationElasticDto> appDTOList = new ArrayList<org.bitvault.appstore.cloud.dto.ApplicationElasticDto>();
		Map<String, Object> allAppMap = null;

		try {
			if (direction.equals(DbConstant.ASC)) {
				appList = elasticRepository.findApplicationByAppName(appName,
						new PageRequest(page, size, Sort.Direction.ASC, property));
			} else {
				appList = elasticRepository.findApplicationByAppName(appName,
						new PageRequest(page, size, Sort.Direction.DESC, property));

			}
			for (org.bitvault.appstore.cloud.model.Application application : appList) {
				// org.bitvault.appstore.cloud.model.AppCategory appCategory =
				// application.getAppCategory();
				appDTO = application.populateApplicationElasticDTO(application);
				appDTOList.add(appDTO);

			}
			allAppMap = new HashMap<String, Object>();
			allAppMap.put("appList", appDTOList);
			allAppMap.put(Constants.TOTAL_PAGES, appList.getTotalPages());
			allAppMap.put(Constants.TOTAL_RECORDS, appList.getTotalElements());
			allAppMap.put(Constants.SORT, appList.getSort());
			allAppMap.put(Constants.SIZE, appList.getNumberOfElements());

		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED, ErrorCode.SOME_ERROR_OCCURED_CODE);
		}

		return allAppMap;

	}

	@Override
	public void updateAverageRating(Integer applicationId, float averageRating) {
		org.bitvault.appstore.cloud.model.Application app = null;
		try {
			app = appRepository.findOne(applicationId);
			if (app != null) {
				app.setAverageRating(averageRating);
				appRepository.saveAndFlush(app);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public String deleteDocument(String applicationId) {
		return elasticSearchTemplate.delete(org.bitvault.appstore.cloud.model.Application.class, applicationId);

	}

	@Override
	public Map<String, Object> findNativeApplication(String status, int page, int size, String direction,
			String property) throws BitVaultException {

		Page<org.bitvault.appstore.cloud.model.Application> appList = null;
		org.bitvault.appstore.cloud.dto.Application appDTO = null;
		List<org.bitvault.appstore.cloud.dto.Application> appDTOList = new ArrayList<org.bitvault.appstore.cloud.dto.Application>();
		Map<String, Object> allAppMap = null;

		try {
			if (direction.equals(DbConstant.ASC)) {
				appList = appRepository.findNativeApplication(status,
						new PageRequest(page, size, Sort.Direction.ASC, property));
			} else {
				appList = appRepository.findNativeApplication(status,
						new PageRequest(page, size, Sort.Direction.DESC, property));

			}
			logger.info("Applist fetched successfully ");
			for (org.bitvault.appstore.cloud.model.Application application : appList) {
				appDTO = application.populateApplicationDTO(application);
				appDTOList.add(appDTO);
			}
			allAppMap = new HashMap<String, Object>();
			allAppMap.put("appList", appDTOList);
			allAppMap.put(Constants.TOTAL_PAGES, appList.getTotalPages());
			allAppMap.put(Constants.TOTAL_RECORDS, appList.getTotalElements());
			allAppMap.put(Constants.SORT, appList.getSort());
			allAppMap.put(Constants.SIZE, appList.getNumberOfElements());

		} catch (Exception e) {
			e.printStackTrace();

			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED, ErrorCode.SOME_ERROR_OCCURED_CODE);
		}

		return allAppMap;

	}

}
