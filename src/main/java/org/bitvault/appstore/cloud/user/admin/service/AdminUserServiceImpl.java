package org.bitvault.appstore.cloud.user.admin.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bitvault.appstore.cloud.constant.Constants;
import org.bitvault.appstore.cloud.constant.DbConstant;
import org.bitvault.appstore.cloud.constant.ErrorCode;
import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.constant.RoleConstant;
import org.bitvault.appstore.cloud.dto.AccountDto;
import org.bitvault.appstore.cloud.dto.AppApplication;
import org.bitvault.appstore.cloud.dto.AppDetail;
import org.bitvault.appstore.cloud.dto.AppInfoDto;
import org.bitvault.appstore.cloud.dto.Application;
import org.bitvault.appstore.cloud.dto.RoleDto;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.bitvault.appstore.cloud.model.Account;
import org.bitvault.appstore.cloud.model.AdminUser;
import org.bitvault.appstore.cloud.model.AppRateReview;
import org.bitvault.appstore.cloud.model.DevUser;
import org.bitvault.appstore.cloud.model.Request;
import org.bitvault.appstore.cloud.user.admin.dao.AdminUserRepository;
import org.bitvault.appstore.cloud.user.common.dao.RequestActivityRepository;
import org.bitvault.appstore.cloud.user.common.dao.RequestRepository;
import org.bitvault.appstore.cloud.user.common.service.RequestActivityService;
import org.bitvault.appstore.cloud.user.common.service.RequestService;
import org.bitvault.appstore.cloud.user.common.service.RoleService;
import org.bitvault.appstore.cloud.user.dev.dao.ReviewReplyRepository;
import org.bitvault.appstore.cloud.user.dev.service.AccountService;
import org.bitvault.appstore.commons.application.dao.AppApplicationRepository;
import org.bitvault.appstore.commons.application.dao.AppDetailRepository;
import org.bitvault.appstore.commons.application.dao.AppHistoryRepository;
import org.bitvault.appstore.commons.application.dao.AppImagesRepository;
import org.bitvault.appstore.commons.application.dao.AppRateReviewRepository;
import org.bitvault.appstore.commons.application.dao.AppStatisticsRepository;
import org.bitvault.appstore.commons.application.dao.ApplicationRepository;
import org.bitvault.appstore.commons.application.elasticdao.AppApplicationElasticRepository;
import org.bitvault.appstore.commons.application.elasticdao.DevUserElasticRepository;
import org.bitvault.appstore.commons.application.service.AppApplicationService;
import org.bitvault.appstore.commons.application.service.AppApplicationServiceImpl;
import org.bitvault.appstore.commons.application.service.AppDetailService;
import org.bitvault.appstore.commons.application.service.AppHistoryService;
import org.bitvault.appstore.commons.application.service.AppImageService;
import org.bitvault.appstore.commons.application.service.ApplicationService;
import org.bitvault.appstore.commons.application.service.RejectedAppService;
import org.bitvault.appstore.mobile.dao.MobileUserAppRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AdminUserServiceImpl implements AdminUserService {
	public static final Logger logger = LoggerFactory.getLogger(AdminUserServiceImpl.class);
	@Autowired
	private AdminUserRepository adminUserRepository;
	@Autowired
	private AppApplicationService appApplicationService;
	@Autowired
	private ApplicationService applicationService;
	@Autowired
	AppDetailService appDetailService;
	@Autowired
	AppImageService appImageService;
	@Autowired
	AppDetailRepository appDetailRepository;
	@Autowired
	RequestActivityService requestActivityService;
	@Autowired
	RequestService requestService;
	@Autowired
	AppHistoryService appHistoryService;
	@Autowired
	RequestRepository requestRepository;
	@Autowired
	DevUserElasticRepository devUserElasticRep;
	@Autowired
	AppApplicationElasticRepository elasticRepository;
	@Autowired
	RoleService roleService;
	@Autowired
	AccountService accountService;

	@Override
	public AdminUser findByEmail(String email) {
		return adminUserRepository.findByEmail(email);
	}

	@Override
	public AdminUser getByUserId(String userId) {
		return adminUserRepository.findByUserId(userId);
	}

	@Override
	public void updatePassword(AdminUser userModel) {
		// TODO Auto-generated method stub

	}

	@Override
	public int changeAppStatus(AppApplication appDto, String status, String reason, String appRequestType,
			String adminId) throws BitVaultException {
		int update = 0;
		String finalStatus = null;
		org.bitvault.appstore.cloud.dto.Application app = null;
		try {

			Application publishedAppDto = applicationService.findApplicationByAppId(appDto.getAppApplicationId());

			AppDetail appDetailDto = appDetailService.findApplicationDetailByAppIdAndVersion(
					appDto.getAppApplicationId(), appDto.getLatestVersionName(), "");
			AppInfoDto oldAppInfoDto = new AppInfoDto();
			oldAppInfoDto = oldAppInfoDto.populateAppInfoDtoToCompare(appDetailDto, appDto);
			if (status.equalsIgnoreCase(Constants.APPROVED)
					&& appRequestType.equalsIgnoreCase(DbConstant.APP_PUBLISHED_REQUEST)) {
				finalStatus = Constants.PUBLISHED;
				appDto.setStatus(Constants.PUBLISHED);

				update = appApplicationService.updateAppStatus(Constants.PUBLISHED, appDto.getAppApplicationId());
				appDto.setCreatedBy(adminId);
				app = applicationService.saveApllication(appDto.populatePublisheApp(appDto));
				updateRequest(appDto.getAppApplicationId(), status, reason);

				if (null != publishedAppDto) {
					appDetailService.updateAppDetailStatus(Constants.UNPUBLISHED, publishedAppDto.getApplicationId(),
							publishedAppDto.getLatestVersion());
				}

				appDetailService.updateAppDetailStatus(Constants.PUBLISHED, appDto.getAppApplicationId(),
						appDto.getLatestVersionName());
				appImageService.updateImageStatus(Constants.PUBLISHED, appDto.getAppApplicationId());
			} else if (status.equalsIgnoreCase(Constants.REJECTED)
					&& appRequestType.equalsIgnoreCase(DbConstant.APP_PUBLISHED_REQUEST)) {
				finalStatus = Constants.REJECTED;
				update = appApplicationService.updateAppStatus(Constants.REJECTED, appDto.getAppApplicationId());
				appDetailService.updateAppDetailStatus(Constants.REJECTED, appDto.getAppApplicationId(),
						appDto.getLatestVersionName());
				// Application publishedAppDto =
				// applicationService.findApplicationByAppId(appDto.getAppApplicationId());

				if (null != publishedAppDto) {
					if (publishedAppDto.getLatestVersion().equals(appDto.getLatestVersionName())
							&& publishedAppDto.getLatestVersionNo().equals(appDto.getLatestVersionNo())) {
						applicationService.deleteApp(appDto.getAppApplicationId());

					}

				}

				updateRequest(appDto.getAppApplicationId(), status, reason);

			} else if (status.equalsIgnoreCase(DbConstant.APPROVED)
					&& appRequestType.equalsIgnoreCase(DbConstant.APP_UNPUBLISHED_REQUEST)) {
				finalStatus = Constants.UNPUBLISHED;

				// app =
				// applicationService.findApplicationByAppId(appDto.getAppApplicationId());

				if (null == publishedAppDto) {
					throw new BitVaultException(ErrorMessageConstant.APP_ALREADY_UNPUBLISHED);

				}
				update = appApplicationService.updateAppStatus(Constants.UNPUBLISHED, appDto.getAppApplicationId());
				appDetailService.updateAppDetailStatus(Constants.UNPUBLISHED, appDto.getAppApplicationId(),
						appDto.getLatestVersionName());
				appImageService.updateImageStatus(Constants.UNPUBLISHED, appDto.getAppApplicationId());

				Request req = updateRequest(appDto.getAppApplicationId(), status, reason);
				if (null != req) {
					applicationService.deleteApp(appDto.getAppApplicationId());
				}

			} else if (status.equalsIgnoreCase(Constants.REJECTED)
					&& appRequestType.equalsIgnoreCase(DbConstant.APP_UNPUBLISHED_REQUEST)) {
				finalStatus = Constants.PUBLISHED;

				update = appApplicationService.updateAppStatus(Constants.PUBLISHED, appDto.getAppApplicationId());
				appDetailService.updateAppDetailStatus(Constants.PUBLISHED, appDto.getAppApplicationId(),
						appDto.getLatestVersionName());
				updateRequest(appDto.getAppApplicationId(), status, reason);
			} else {
				throw new BitVaultException(ErrorMessageConstant.INVALID_REQUEST);

			}

			AppInfoDto appInfoDto = new AppInfoDto();
			appDto.setStatus(finalStatus);
			appInfoDto = appInfoDto.populateAppInfoDtoToCompare(appDetailDto, appDto);
			appHistoryService.saveAppHistory(oldAppInfoDto, appInfoDto, appDto.populateAppApplication(appDto), null);

		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Exception Occured on changing app status "+e.getMessage() );
			throw new BitVaultException(e.getMessage());
		}
		return update;
	}

	private Request updateRequest(Integer appId, String status, String reason) {
		Request req = requestService.findRequestByAppId(appId);
		req.setStatus(status);
		req.setRejectionReason(reason);
		req = requestService.updateRequestStatus(req);
		requestActivityService.saveRequestActivity(req, null);

		return req;
	}

	@Override
	public Request findRequestById(Integer requestId) throws BitVaultException {
		Request req = null;
		try {
			req = requestRepository.findOne(requestId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);
		}
		return req;
	}

	@Override
	public String findRequestTypeByReqId(Integer requestId) throws BitVaultException {
		String requestType = null;
		try {
			requestType = findRequestById(requestId).getRequestType().getRequestType();
		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);
		}
		return requestType;
	}

	@Override
	public void save(AdminUser adminUser) {
		adminUserRepository.saveAndFlush(adminUser);
	}

	@Override
	public void changePassword(AdminUser adminUser) {
		adminUserRepository.saveAndFlush(adminUser);
	}

	@Override
	public Map<String, Object> searchAppApplicationByAppName(String appName, int page, int size, String direction,
			String property) throws BitVaultException {
		Page<org.bitvault.appstore.cloud.model.AppApplication> appList = null;
		org.bitvault.appstore.cloud.dto.ApplicationElasticDto appDTO = null;
		List<org.bitvault.appstore.cloud.dto.ApplicationElasticDto> appDTOList = new ArrayList<org.bitvault.appstore.cloud.dto.ApplicationElasticDto>();
		Map<String, Object> allAppMap = null;

		try {
			logger.info("searchAppApplicationByAppName method calling");
			if (direction.equals(DbConstant.ASC)) {
				appList = elasticRepository.searchAppApplicationByAppName(appName,
						new PageRequest(page, size, Sort.Direction.ASC, property));
			} else {
				appList = elasticRepository.searchAppApplicationByAppName(appName,
						new PageRequest(page, size, Sort.Direction.DESC, property));

			}
            logger.info("Applist fetched successfullly");
			for (org.bitvault.appstore.cloud.model.AppApplication appApplication : appList) {
				appDTO = appApplication.populateApplicationElasticDTO(appApplication);
				appDTOList.add(appDTO);
			}
			allAppMap = new HashMap<String, Object>();
			allAppMap.put("appList", appDTOList);
			allAppMap.put(Constants.TOTAL_PAGES, appList.getTotalPages());
			allAppMap.put(Constants.TOTAL_RECORDS, appList.getTotalElements());
			allAppMap.put(Constants.SORT, appList.getSort());
			allAppMap.put(Constants.SIZE, appList.getNumberOfElements());

		} catch (Exception e) {
			logger.info("Error occured searching"+e.getMessage());
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED, ErrorCode.SOME_ERROR_OCCURED_CODE);
		}

		return allAppMap;

	}

	@Override
	public Map<String, Object> searchUserByUserName(String username, int page, int size, String direction,
			String property) {
		Page<DevUser> userList = null;
		org.bitvault.appstore.cloud.dto.DevUserElasticDto userDTO = null;
		List<org.bitvault.appstore.cloud.dto.DevUserElasticDto> userDTOList = new ArrayList<org.bitvault.appstore.cloud.dto.DevUserElasticDto>();
		Map<String, Object> allAppMap = null;

		try {
			logger.info("findDevUserByUsername method calling");
			if (direction.equals(DbConstant.ASC)) {
				userList = devUserElasticRep.findDevUserByUsername(username,
						new PageRequest(page, size, Sort.Direction.ASC, property));
			} else {
				userList = devUserElasticRep.findDevUserByUsername(username,
						new PageRequest(page, size, Sort.Direction.DESC, property));

			}
            logger.info("Userlist fetched successfully");
			for (DevUser devuser : userList) {
				userDTO = devuser.populateDevUserElasticDTO(devuser);
                logger.info("getAccountById Method calling");
				Account account = accountService.getAccount(devuser.getAccount().getAccId());
                logger.info("account object get ");
				if (!devuser.getParentId().equalsIgnoreCase(devuser.getUserId())) {
					userDTO.setSignupAs(RoleConstant.ORG_DEV);
				} else {
					String signUpAs = account.getRole().getRoleName();
					if (signUpAs.equals("ROLE_" + RoleConstant.DEVELOPER))
						userDTO.setSignupAs(Constants.DEV);
					else {

						userDTO.setSignupAs(Constants.ORG);
					}
				}

				userDTOList.add(userDTO);
			}
			allAppMap = new HashMap<String, Object>();
			allAppMap.put("userList", userDTOList);
			allAppMap.put(Constants.TOTAL_PAGES, userList.getTotalPages());
			allAppMap.put(Constants.TOTAL_RECORDS, userList.getTotalElements());
			allAppMap.put(Constants.SORT, userList.getSort());
			allAppMap.put(Constants.SIZE, userList.getNumberOfElements());

		} catch (Exception e) {
			logger.info("Error occured during searching"+e.getMessage());
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED, ErrorCode.SOME_ERROR_OCCURED_CODE);
		}

		return allAppMap;

	}

}
