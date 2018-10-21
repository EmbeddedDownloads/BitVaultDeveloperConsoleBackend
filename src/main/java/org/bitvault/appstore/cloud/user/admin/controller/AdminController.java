package org.bitvault.appstore.cloud.user.admin.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import org.bitvault.appstore.cloud.constant.APIConstants;
import org.bitvault.appstore.cloud.constant.Constants;
import org.bitvault.appstore.cloud.constant.DbConstant;
import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.constant.RoleConstant;
import org.bitvault.appstore.cloud.dto.AdminUserDto;
import org.bitvault.appstore.cloud.dto.AppApplication;
import org.bitvault.appstore.cloud.dto.AppDetail;
import org.bitvault.appstore.cloud.dto.AppInfoDto;
import org.bitvault.appstore.cloud.dto.DevUserDto;
import org.bitvault.appstore.cloud.dto.NotificationAdditionalProrerties;
import org.bitvault.appstore.cloud.dto.NotificationData;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.bitvault.appstore.cloud.exception.BitVaultResponse;
import org.bitvault.appstore.cloud.mail.MailService;
import org.bitvault.appstore.cloud.model.Account;
import org.bitvault.appstore.cloud.model.AdminUser;
import org.bitvault.appstore.cloud.model.AppAddonServices;
import org.bitvault.appstore.cloud.model.DevPayment;
import org.bitvault.appstore.cloud.model.DevUser;
import org.bitvault.appstore.cloud.model.MobileUserAppStatus;
import org.bitvault.appstore.cloud.model.Request;
import org.bitvault.appstore.cloud.model.SubDevReq;
import org.bitvault.appstore.cloud.model.UserActivityType;
import org.bitvault.appstore.cloud.user.admin.service.AdminUserService;
import org.bitvault.appstore.cloud.user.common.GeneralResponseModel;
import org.bitvault.appstore.cloud.user.common.service.AppAddonServicesService;
import org.bitvault.appstore.cloud.user.common.service.DevPaymentService;
import org.bitvault.appstore.cloud.user.common.service.RequestActivityService;
import org.bitvault.appstore.cloud.user.dev.service.DevUserService;
import org.bitvault.appstore.cloud.user.dev.service.SubDevReqService;
import org.bitvault.appstore.cloud.utils.Response;
import org.bitvault.appstore.cloud.utils.Utility;
import org.bitvault.appstore.commons.application.service.AppApplicationService;
import org.bitvault.appstore.commons.application.service.AppDetailService;
import org.bitvault.appstore.commons.application.service.AppHistoryService;
import org.bitvault.appstore.commons.application.service.AppImageService;
import org.bitvault.appstore.commons.application.service.ApplicationService;
import org.bitvault.appstore.mobile.service.MobileUserAppStatusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping(APIConstants.ADMIN_API_BASE)
public class AdminController {

	@Autowired
	private DevUserService devUserService;

	@Autowired
	private AppApplicationService appApplicationService;

	@Autowired
	private ApplicationService applicationService;

	@Autowired
	private MailService mailService;

	@Autowired
	AdminUserService adminService;

	@Autowired
	AppImageService appImageService;

	@Autowired
	AppDetailService appDetailService;

	@Autowired
	private DevPaymentService devPaymentService;

	@Autowired
	AppHistoryService appHistoryService;

	@Autowired
	ApplicationService appService;

	@Autowired
	MobileUserAppStatusService mobileUserAppStatusService;

	@Autowired
	AppAddonServicesService appAddOnServices;

	@Autowired
	private SubDevReqService devReqService;
	@Autowired
	private RequestActivityService requestActivityService;

	private Map<String, Object> result = null;

	private final Logger logger = LoggerFactory.getLogger(AdminController.class);

	@ApiOperation(value = "Get list of all developer users", notes = "Requires page and size i.e. no. of records need to be shown.", response = DevUserDto.class, responseContainer = "List")
	@ApiResponses(@ApiResponse(code = 200, message = "Successfully fetched the list of all developers"))
	@GetMapping(value = APIConstants.LIST_OF_USERS)
	public ResponseEntity<?> getAllDevUsers(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size,
			@RequestParam(required = false, defaultValue = "email") String orderBy,
			@RequestParam(required = false, defaultValue = "active,inactive") List<String> status,
			@RequestParam(required = false, defaultValue = "DESC") String direction) {
		if (page > 0) {
			page--;
		}
		return ResponseEntity.ok(devUserService.findAllUsers(page, size, orderBy, status, direction));
	}

	public void registerUser() {

	}

	@ApiOperation(value = "Get details of  developer account from his associated userId", notes = "Requires dveloper associated userId.", response = DevUserDto.class)
	@ApiResponses(@ApiResponse(code = 200, message = "Return the developer details."))
	@GetMapping(value = APIConstants.FIND_BY_USER_ID, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getUserByUserId(@PathVariable String userId) {
		result = new HashMap<>();
		DevUserDto userDto = devUserService.findUserDtoByUserId(userId);
		List<DevPayment> devPaymentList = devPaymentService.findByUserIdAndPaymentFor(userId, Constants.SELF,
				new PageRequest(0, 1, new Sort(Direction.DESC, Constants.CREATED_AT))).getContent();

		if (userDto == null && devPaymentList.isEmpty()) {
			result.put(Constants.MESSAGE, ErrorMessageConstant.USER_NOT_FOUND);
			return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED, result));
		} else if (userDto.getParentId().equalsIgnoreCase(userDto.getUserId())) {
			DevPayment devPayment = devPaymentList.get(0);

			List<DevPayment> devPaymentListForSubDev = devPaymentService.findByUserIdAndPaymentForSubDev(userId,
					Constants.SUBDEV);

			double paymentForSubDev = 0;
			if (devPaymentListForSubDev != null && devPaymentListForSubDev.size() > 0) {
				for (DevPayment payment : devPaymentListForSubDev) {
					paymentForSubDev += payment.getAmountPaid();
				}
			}

			userDto.setTotalPaymentForSubDev(paymentForSubDev);
			userDto.setPayment(devPayment.getPayment());
			userDto.setTxnStatus(devPayment.getTxnStatus());
		} else {
			userDto.setSignupAs(RoleConstant.ORG_DEV);
			userDto.setTxnStatus(Constants.SUCCESS);
		}
		return ResponseEntity.ok(GeneralResponseModel.of(Constants.SUCCESS, userDto));
	}

	@PostMapping(value = APIConstants.ACTION_ON_DEV_ACCOUNT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> actionOnDevAccount(@PathVariable String userId,
			@RequestBody Map<String, String> actionMapper) {
		result = new HashMap<>();
		String reason;
		try {

			String action = actionMapper.get("requestAction");
			if (action != null) {
				action = action.toUpperCase();
			}
			DevUser user = devUserService.findByUserId(userId);
			if (user == null) {
				result.put(Constants.MESSAGE, ErrorMessageConstant.USER_NOT_FOUND);
				return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED, result));
			}
			String currentStatus = user.getStatus();
			if (currentStatus.equalsIgnoreCase(DbConstant.REVIEW)) {
				return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED,
						new BitVaultResponse(ErrorMessageConstant.USER_EMAIL_NOT_VERIFIED)));
			}
			if (currentStatus.equals(Constants.INACTIVE) && action.equals(DbConstant.ACTIVE)) {
				user.setRejectionReason("");
				updateDeveloperAccStatus(user, DbConstant.ACTIVE);
				mailService.sendMail(user.getEmail(), Constants.ACCOUNT_ACTION + "been activated",
						Constants.ACCOUNT_APPROVAL);
				result.put(Constants.MESSAGE, Constants.ACTIVE);
			} else if (currentStatus.equals(DbConstant.ACTIVE) && action.equals(Constants.INACTIVE)) {
				reason = actionMapper.get("reason");
				if (reason == null) {
					reason = "Unknown";
				}
				user.setRejectionReason(reason);
				updateDeveloperAccStatus(user, Constants.INACTIVE);
				mailService.sendMail(user.getEmail(), Constants.ACCOUNT_ACTION + "been de-activated",
						Constants.ACCOUNT_DEACTIVATED + "<i>" + reason + "</i>");
				result.put(Constants.MESSAGE, Constants.DEACTIVATED);
			} else {
				result.put(Constants.MESSAGE, ErrorMessageConstant.NO_PROPER_ACTION_TAKEN);
				return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED, result));
			}
			return ResponseEntity.ok(GeneralResponseModel.of(Constants.SUCCESS, result));
		} catch (Exception e) {
			e.printStackTrace();
			result.put(Constants.MESSAGE, ErrorMessageConstant.UNABLE_TO_UPDATE);
			return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED, result));
		}
	}

	private void updateDeveloperAccStatus(DevUser user, String status) {
		Account account = user.getAccount();
		account.setStatus(status);
		user.setAccount(account);
		user.setStatus(status);
		devUserService.saveUser(user);
	}

	@GetMapping(value = APIConstants.GET_COUNTS)
	public ResponseEntity<?> getCounts(
			@RequestParam(required = false, defaultValue = "active,inactive") List<String> userStatus,
			@RequestParam(required = false, defaultValue = Constants.APPS_STATUS) List<String> appStatus) {
		result = new HashMap<String, Object>();
		int devCount = devUserService.getUserCount(userStatus);
		result.put(Constants.DEV_COUNT, devCount);
		int freeApps = appApplicationService.getFreeAppsCount(0, appStatus);
		result.put(Constants.FREE_APPS_COUNT, freeApps);
		int paidApps = appApplicationService.getPaidAppsCount(0, appStatus);
		result.put(Constants.PAID_APPS_COUNT, paidApps);
		int activeApps = applicationService.getActiveAppsCount();
		result.put(Constants.ACTIVE_APPS_COUNT, activeApps);
		return ResponseEntity.ok(GeneralResponseModel.of(Constants.SUCCESS, result));
	}

	@PostMapping(value = APIConstants.UPDATE_APP_STATUS_REQUEST
			+ "/{requestId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateAppStatus(@RequestBody Map<String, Object> requestMap,
			@PathVariable Integer requestId) {
		GeneralResponseModel genralResponse = null;
		String status = null;
		String reason = null;
		String appRequestType = null;
		String adminId = "1";

		status = (String) requestMap.get(Constants.REQUEST_ACTION);
		reason = (String) requestMap.get(Constants.REASON);
		// requestId = (Integer) requestMap.get(Constants.REQUESTID);

		if (!Utility.isStringEmpty(status) && !Utility.isIntegerEmpty(requestId)) {
			if (null == reason && status.equalsIgnoreCase(DbConstant.APPROVED)) {
				reason = "";
			} else if (Utility.isStringEmpty(reason) && status.equalsIgnoreCase(DbConstant.REJECTED)) {
				reason = Constants.DEFAULT_REASON;
			}
			try {
				Request req = adminService.findRequestById(requestId);
				if (null != req) {
					appRequestType = req.getRequestType().getRequestType();
					if ((appRequestType.equals(DbConstant.APP_PUBLISHED_REQUEST)
							|| appRequestType.equals(DbConstant.APP_UNPUBLISHED_REQUEST))
							&& !appRequestType.equals(DbConstant.PENDING)) {
						AppApplication appDto = appApplicationService.findApplicationByAppId(req.getApplicationId());

						Integer update = adminService.changeAppStatus(appDto, status, reason, appRequestType, adminId);

						genralResponse = GeneralResponseModel.of(Constants.SUCCESS, new BitVaultResponse(status));

						// Prepare and send notification data
						if (appRequestType.equals(DbConstant.APP_PUBLISHED_REQUEST)
								&& status.equalsIgnoreCase(DbConstant.APPROVED)) {

							List<String> publicAddresslist = checkForAppUpdateAndHitForNotification(appDto,
									req.getApplicationId());

							AppAddonServices appAddOnService = appAddOnServices.getAppAddOnService(1,
									"com.bitvault.appstore");

							if (appAddOnService == null)
								logger.info("Application not registered for notification add on services");

							if (appAddOnService != null && appAddOnService.getWebServerKey() != null
									&& publicAddresslist != null && publicAddresslist.size() > 0) {
								logger.info("Found active user to send update notification");
								NotificationData notificationData = new NotificationData();
								notificationData.setPublic_address(publicAddresslist);
								notificationData.setWeb_server_key(appAddOnService.getWebServerKey());
								notificationData.setTag(Constants.TAG);
								notificationData.setData(new NotificationAdditionalProrerties(appDto.getAppIconUrl(),
										appDto.getAppName(), appDto.getPackageName(), req.getApplicationId() + "",
										appDto.getUpdatedAt().toString(), appDto.getApkFilesize()));

								/**
								 * Code used to send notification data to notification server
								 */

								RestTemplate restTemplet = new RestTemplate();
								String response = restTemplet.postForObject(APIConstants.SEND_NOTIFICATION_URL,
										notificationData, String.class);

								logger.info("Public addresses of users send to notification server");

							}

						}

						sendMailToDeveloper(req, status, appRequestType, appDto, reason);

					} else {
						throw new BitVaultException(ErrorMessageConstant.INVALID_REQUEST);

					}
				} else {
					throw new BitVaultException(ErrorMessageConstant.RESULT_NOT_FOUND);
				}
			} catch (BitVaultException e) {
				genralResponse = GeneralResponseModel.of(Constants.FAILED, new BitVaultResponse(e));

				return new ResponseEntity<GeneralResponseModel>(genralResponse, HttpStatus.OK);

			}
		} else {
			genralResponse = GeneralResponseModel.of(Constants.FAILED,
					new BitVaultResponse(new BitVaultException(ErrorMessageConstant.FIELD_IS_EMPTY)));
			return new ResponseEntity<GeneralResponseModel>(genralResponse, HttpStatus.OK);

		}
		return new ResponseEntity<GeneralResponseModel>(genralResponse, HttpStatus.OK);

	}

	@PostMapping(value = APIConstants.UPDATE_APP_STATUS_BY_ADMIN + "/{appId}")
	public ResponseEntity<GeneralResponseModel> updateAppStatusByAdmin(@RequestBody Map<String, Object> requestBody,
			@PathVariable Integer appId) {
		GeneralResponseModel genralResponse = null;
		Map<String, String> errorMap = null;

		String status = (String) requestBody.get(Constants.REQUEST_ACTION);
		String reason = (String) requestBody.get(Constants.REASON);
		if (!Utility.isStringEmpty(status) && !Utility.isIntegerEmpty(appId)) {
			try {
				if (Utility.isStringEmpty(reason)) {
					reason = Constants.DEFAULT_REASON;
				}
				AppApplication appDto = appApplicationService.findApplicationByAppId(appId);
				if (null != appDto) {
					AppDetail appDetailDto = appDetailService.findApplicationDetailByAppIdAndVersion(
							appDto.getAppApplicationId(), appDto.getLatestVersionName(), "");
					AppInfoDto oldAppInfoDto = new AppInfoDto();
					oldAppInfoDto = oldAppInfoDto.populateAppInfoDtoToCompare(appDetailDto, appDto);
					if ((appDto.getStatus().equals(DbConstant.PUBLISHED)
							|| appDto.getStatus().equalsIgnoreCase(Constants.BETA_PUBLISHED)
							|| appDto.getStatus().equalsIgnoreCase(Constants.ALPHA_PUBLISHED))
							&& status.equalsIgnoreCase(DbConstant.UNPUBLISHED)) {
						appDto.setStatus(DbConstant.UNPUBLISHED);
						appDto.setReason(reason);
						appDto = appApplicationService.saveAppApllication(appDto);
						appDetailService.updateAppDetailStatus(Constants.UNPUBLISHED, appDto.getAppApplicationId(),
								appDto.getLatestVersionName());
						appImageService.updateImageStatus(Constants.UNPUBLISHED, appDto.getAppApplicationId());
						applicationService.deleteApp(appId);

					} else if ((appDto.getStatus().equals(DbConstant.UNPUBLISHED)
							|| appDto.getStatus().equalsIgnoreCase(Constants.BETA_UNPUBLISHED)
							|| appDto.getStatus().equalsIgnoreCase(Constants.ALPHA_UNPUBLISHED))
							&& status.equalsIgnoreCase(DbConstant.PUBLISHED)) {
						int imageCount = appImageService.getAppImageCountByType(DbConstant.TYPE_IMAGE,
								appDto.getAppApplicationId());
						int bannerCount = appImageService.getAppImageCountByType(DbConstant.TYPE_BANNER,
								appDto.getAppApplicationId());
						AppInfoDto appInfoDto = new AppInfoDto();

						appInfoDto = appInfoDto.populateAppInfoDto(appDetailDto, appDto, appInfoDto);
						errorMap = appInfoDto.validateAppInfoDto(appInfoDto, appDto.getAppIconUrl(), appDto.getApkUrl(),
								imageCount, bannerCount);

						if (null != errorMap && errorMap.size() > 0) {
							genralResponse = GeneralResponseModel.of(Constants.FAILED, errorMap);
							return new ResponseEntity<GeneralResponseModel>(genralResponse, HttpStatus.OK);

						}

						reason = "";
						appDto.setStatus(DbConstant.PUBLISHED);
						appDto.setReason(reason);

						appDto = appApplicationService.saveAppApllication(appDto);

						applicationService.saveApllication(appDto.populatePublisheApp(appDto));
						appDetailService.updateAppDetailStatus(Constants.PUBLISHED, appDto.getAppApplicationId(),
								appDto.getLatestVersionName());
						appImageService.updateImageStatus(Constants.PUBLISHED, appDto.getAppApplicationId());

						/**
						 * In case of auto approval by admin send push data
						 */
						if (status.equalsIgnoreCase(DbConstant.PUBLISHED)) {

							List<String> publicAddresslist = checkForAppUpdateAndHitForNotification(appDto, appId);

							AppAddonServices appAddOnService = appAddOnServices.getAppAddOnService(1,
									"com.bitvault.appstore");

							if (appAddOnService == null)
								logger.info("Application not registered for notification add-on-services");

							if (appAddOnService != null && appAddOnService.getWebServerKey() != null
									&& publicAddresslist != null && publicAddresslist.size() > 0) {

								logger.info("Found active user to send update notification");
								NotificationData notificationData = new NotificationData();
								notificationData.setPublic_address(publicAddresslist);
								notificationData.setWeb_server_key(appAddOnService.getWebServerKey());
								notificationData.setTag(Constants.TAG);
								notificationData.setData(new NotificationAdditionalProrerties(appDto.getAppIconUrl(),
										appDto.getAppName(), appDto.getPackageName(), appId + "",
										appDto.getUpdatedAt().toString(), appDto.getApkFilesize()));

								/**
								 * Code used to send notification data to notification server
								 */
								RestTemplate restTemplet = new RestTemplate();

								List<HttpMessageConverter<?>> msgConverters = restTemplet.getMessageConverters();
								msgConverters.add(new MappingJackson2HttpMessageConverter());
								// msgConverters.add( )
								restTemplet.setMessageConverters(msgConverters);
								// restTemplet.postForEntity(APIConstants.SEND_NOTIFICATION_URL,
								// notificationData, String.class);
								String response = restTemplet.postForObject(APIConstants.SEND_NOTIFICATION_URL,
										notificationData, String.class);

								System.out.println(response);

								logger.info(response);

								logger.info("Public addresses of user has been sent to notification server");
							}
						}

					} else {
						throw new BitVaultException(ErrorMessageConstant.INVALID_REQUEST);
					}

					sendMailToDeveloper(status, appDto, reason);
					AppInfoDto appInfoDto = new AppInfoDto();
					appInfoDto = appInfoDto.populateAppInfoDtoToCompare(appDetailDto, appDto);
					appHistoryService.saveAppHistory(oldAppInfoDto, appInfoDto, appDto.populateAppApplication(appDto),
							null);
					genralResponse = GeneralResponseModel.of(Constants.SUCCESS,
							new BitVaultResponse(Constants.SUCCESS_UPDATED));
				} else {
					throw new BitVaultException(ErrorMessageConstant.RESULT_NOT_FOUND);
				}
			} catch (BitVaultException e) {
				genralResponse = GeneralResponseModel.of(Constants.SUCCESS, new BitVaultResponse(e));
			}
		}
		return new ResponseEntity<GeneralResponseModel>(genralResponse, HttpStatus.OK);

	}

	@GetMapping(value = APIConstants.GET_USER_INFO)
	public ResponseEntity<?> getAdminDetails(HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<>();
		try {
			String userId = (String) request.getAttribute(Constants.USERID);
			AdminUser user = adminService.getByUserId(userId);
			if (user == null) {
				resultMap.put(Constants.MESSAGE, ErrorMessageConstant.USER_NOT_FOUND);
				return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED, resultMap));
			}
			AdminUserDto userDto = new AdminUserDto();
			BeanUtils.copyProperties(user, userDto);
			userDto.setPassword("");
			return ResponseEntity.ok(GeneralResponseModel.of(Constants.SUCCESS, userDto));
		} catch (Exception e) {
			resultMap.put(Constants.MESSAGE, e.getMessage());
			return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED, resultMap));
		}
	}

	@PutMapping(value = APIConstants.UPDATE_USER_INFO)
	public ResponseEntity<?> updateAdminInfo(HttpServletRequest request, @RequestBody AdminUserDto userDto) {
		Map<String, Object> resultMap = new HashMap<>();
		try {
			String userId = (String) request.getAttribute(Constants.USERID);
			AdminUser user = adminService.getByUserId(userId);
			if (user == null) {
				resultMap.put(Constants.MESSAGE, ErrorMessageConstant.USER_NOT_FOUND);
				return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED, resultMap));
			}
			String firstName = userDto.getFirstName();
			String lastName = userDto.getLastName();
			if (firstName == null || lastName == null) {
				resultMap.put(Constants.MESSAGE, ErrorMessageConstant.REQUIRED_NAME_FIELD_MISSING);
				return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED, resultMap));
			}
			if (firstName.trim().isEmpty() || lastName.trim().isEmpty()) {
				resultMap.put(Constants.MESSAGE, ErrorMessageConstant.FIELD_IS_EMPTY);
				return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED, resultMap));
			}
			user.setFirstName(firstName);
			user.setLastName(lastName);
			adminService.save(user);

			// avatarController.trackAvatarForUserAuditing(userId, firstName + "
			// " +
			// lastName, avatarURL);
			trackAvatarForUserAuditing(userId, firstName + " " + lastName);

			resultMap.put(Constants.MESSAGE, Constants.SUCCESS_UPDATED);
			return ResponseEntity.ok(GeneralResponseModel.of(Constants.SUCCESS, resultMap));
		} catch (Exception e) {
			resultMap.put(Constants.MESSAGE, e.getMessage());
			return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED, resultMap));
		}
	}

	// @GetMapping(value = APIConstants.SUB_DEV_REQUEST)
	// public ResponseEntity<?> getSubDevRequests(@RequestParam(defaultValue =
	// DbConstant.PENDING) List<String> status,
	// @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue =
	// "10") int size,
	// @RequestParam(defaultValue = "DESC") String direction,
	// @RequestParam(defaultValue = Constants.CREATED_AT) String orderBy) {
	// if(page > 0){
	// page--;
	// }
	// Sort sort = null;
	// if (direction.equalsIgnoreCase(DbConstant.ASC))
	// sort = new Sort(Direction.ASC, orderBy);
	// else
	// sort = new Sort(Direction.ASC, orderBy);
	// Page<SubDevReq> subDevRequestPage =
	// devReqService.allSubDevRequests(status,
	// new PageRequest(page, size, sort));
	// return ResponseEntity.ok(new Response(Constants.SUCCESS,
	// populateSubDevReqDto(subDevRequestPage.getContent()),
	// subDevRequestPage.getTotalPages(), subDevRequestPage.getTotalElements(),
	// size, orderBy));
	// }

	// private List<SubDevReqDto> populateSubDevReqDto(List<SubDevReq>
	// subDevReqList) {
	// List<SubDevReqDto> subDevReqDtoList = new ArrayList<SubDevReqDto>();
	// for (SubDevReq subDevReq : subDevReqList) {
	// subDevReqDtoList.add(generateSubDevReqDto(subDevReq));
	// }
	// return subDevReqDtoList;
	// }

	// private SubDevReqDto generateSubDevReqDto(SubDevReq subDevReq){
	// SubDevReqDto subDevReqDto = new SubDevReqDto();
	// DevPayment devPayment = subDevReq.getDevPaymentId();
	// if( devPayment != null){
	// subDevReqDto.setTxnStatus(devPayment.getTxnStatus());
	// subDevReqDto.setPayment(devPayment.getPayment());
	// }
	// DevUser devUser = devUserService.findByUserId(subDevReq.getUserId());
	// BeanUtils.copyProperties(devUser, subDevReqDto);
	// BeanUtils.copyProperties(devUser.getAccount(), subDevReqDto);
	// BeanUtils.copyProperties(subDevReq, subDevReqDto);
	// return subDevReqDto;
	// }

	@PutMapping(value = APIConstants.SUB_DEV_REQUEST + "/{userId}/{reqId}")
	public ResponseEntity<?> actionOn(@PathVariable String userId, @PathVariable Integer reqId,
			@RequestBody Map<String, Object> inputMap) {
		try {
			SubDevReq subDevReq = devReqService.findById(reqId);
			if (subDevReq == null) {
				return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED,
						new BitVaultResponse(ErrorMessageConstant.RESULT_NOT_FOUND)));
			}
			String requestAction = (String) inputMap.get("requestAction");
			double payableAmount = Double.parseDouble(inputMap.get("payment").toString());
			String rejectionReason = (String) inputMap.get("rejectionReason");
			if (Utility.isStringEmpty(requestAction) || Utility.isDoubleEmpty(payableAmount)) {
				return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED,
						new BitVaultResponse(ErrorMessageConstant.REQUIRED_FIELD_MISSING_2)));
			}
			String currentStatus = subDevReq.getStatus();
			if (currentStatus.equalsIgnoreCase(DbConstant.APPROVED)) {
				return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED,
						new BitVaultResponse(ErrorMessageConstant.NO_ALLOWED)));
			} else if (currentStatus.equalsIgnoreCase(DbConstant.REJECTED)
					&& requestAction.equalsIgnoreCase(DbConstant.REJECTED)) {
				return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED,
						new BitVaultResponse(ErrorMessageConstant.INVALID_REQUEST)));
			}
			if (requestAction.equalsIgnoreCase(DbConstant.REJECTED) && Utility.isStringEmpty(rejectionReason)) {
				rejectionReason = "Unknown reason!";
			} else if (requestAction.equalsIgnoreCase(DbConstant.APPROVED)) {
				rejectionReason = "";
				DevPayment devPayment = new DevPayment();
				devPayment.setPayment(payableAmount);
				devPayment.setUserId(userId);
				devPayment.setPaymentFor(Constants.SUBDEV);
				if (payableAmount == 0) {
					devPayment.setTxnStatus(Constants.SUCCESS);
					devPayment.setTxnId("Free");
					devPayment.setPaymentMode(RoleConstant.ADMIN);
					devPayment.setAmountPaid(payableAmount);
					DevUser devUser = devUserService.findByUserId(userId);
					devUser.setChildCount(devUser.getChildCount() + subDevReq.getChildCount());
					devUserService.saveUser(devUser);
				} else
					devPayment.setTxnStatus(Constants.PENDING);
				devPaymentService.savePaymentRecord(devPayment);
				subDevReq.setDevPaymentId(devPayment);
			}
			subDevReq.setRejectionReason(rejectionReason);
			subDevReq.setStatus(requestAction);
			devReqService.saveSubDevRequest(subDevReq);
			if (subDevReq != null) {
				requestActivityService.saveRequestActivity(null, subDevReq);
			}
			if (requestAction.equalsIgnoreCase(DbConstant.REJECTED))
				return ResponseEntity
						.ok(GeneralResponseModel.of(Constants.SUCCESS, new BitVaultResponse(DbConstant.REJECTED)));
			return ResponseEntity
					.ok(GeneralResponseModel.of(Constants.SUCCESS, new BitVaultResponse(DbConstant.APPROVED)));
		} catch (Exception e) {
			logger.error(e.getMessage());
			return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED, new BitVaultResponse(e.getMessage())));
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
			// if (name.length() > 2) {
			try

			{
				allAppMap = adminService.searchAppApplicationByAppName(name, page, size, DbConstant.DESC, orderBy);
				logger.info("Application Searched Successfully");
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
				logger.error("Error occured during List fetching");
				response.setResult(new BitVaultResponse(e));
				// response.setType(APIConstants.TYPE_ERROR);
				response.setStatus(Constants.FAILED);
				return new ResponseEntity<Response>(response, HttpStatus.OK);

			}
			// }
		} else {
			response.setResult(new BitVaultResponse(ErrorMessageConstant.FIELD_IS_EMPTY));
			// response.setType(APIConstants.TYPE_ERROR);
			response.setStatus(Constants.FAILED);
			return new ResponseEntity<Response>(response, HttpStatus.OK);
		}
		// return new ResponseEntity<Response>(response, HttpStatus.OK);

	}
	// @GetMapping(value = APIConstants.SUB_DEV_REQUEST + "/{reqId}")
	// public ResponseEntity<?> getSubDevRequest(@PathVariable Integer reqId){
	// SubDevReq subDevReq = devReqService.findById(reqId);
	// if (subDevReq == null) {
	// return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED,
	// new BitVaultResponse(ErrorMessageConstant.RESULT_NOT_FOUND)));
	// }
	// return ResponseEntity.ok(GeneralResponseModel.of(Constants.SUCCESS,
	// generateSubDevReqDto(subDevReq)));
	// }

	/**
	 * Method used to track user auditing
	 * 
	 * @param userId
	 * @param userName
	 */
	public void trackAvatarForUserAuditing(String userId, String userName) {

		try {
			UserActivityType userActivityType = new UserActivityType();
			userActivityType.setActivityType(Constants.EDIT_PROFILE);

			Map<String, String> dataMap = new HashMap<>();
			dataMap.put(Constants.USERID, userId);
			dataMap.put(Constants.USER_NAME, userName);

			if (userName != null && userId != null) {
				devUserService.saveUserActivity(userActivityType, dataMap);
			}
		} catch (Exception e) {
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);
		}
	}

	/**
	 * Method used to get list of mobile users public address to send data to push
	 * notification server
	 * 
	 * @param appAppDto
	 * @param appId
	 * @return
	 */
	private List<String> checkForAppUpdateAndHitForNotification(AppApplication appAppDto, Integer appId) {

		List<String> publicAddressList;

		try {
			publicAddressList = new ArrayList<String>();

			if (appAppDto.getStatus().equals(Constants.PUBLISHED)
					&& appAppDto.getUpdateType().equals(Constants.UPDATED_VERSION)) {

				List<MobileUserAppStatus> mobileUserAppStatusList = mobileUserAppStatusService
						.findAllMobileUserFromAppIdAndStatus(appId, DbConstant.INSTALL);

				if (mobileUserAppStatusList != null && mobileUserAppStatusList.size() > 0) {

					for (int i = 0; i < mobileUserAppStatusList.size(); i++) {
						String publicAddress = mobileUserAppStatusList.get(i).getMobileUser().getPublicAdd();
						publicAddressList.add(publicAddress);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return publicAddressList;
	}

	@RequestMapping(value = APIConstants.ELASTIC_SEARCH_USER, method = RequestMethod.GET)
	public ResponseEntity<?> elasticSearchUser(@RequestParam String username,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size,
			@RequestParam(defaultValue = DbConstant.USERNAME) String orderBy) {
		Response response = new Response();
		GeneralResponseModel genralResponse = null;
		Map<String, Object> allAppMap = null;
		if (page > 0) {
			page = page - 1;
		}
		if (!Utility.isStringEmpty(username)) {
			// if (username.length() > 2) {
			try

			{
				allAppMap = adminService.searchUserByUserName(username, page, size, DbConstant.DESC.toString(),
						orderBy);
				logger.info("Application searched Successfully");
				response.setResult(allAppMap.get("userList"));
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
				logger.error("Error occured during searching");
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

	/**
	 * Method used to send mail to the developer when admin has taken some action on
	 * the request made by the developer.
	 * 
	 * @param req
	 * @param status
	 * @param appRequestType
	 * @param appDto
	 * @param reason
	 */
	private void sendMailToDeveloper(Request req, String status, String appRequestType, AppApplication appDto,
			String reason) {
		DevUser devUserDeveloper = devUserService.findByUserId(appDto.getUserId());

		ArrayList<DevUser> arrDevUser = new ArrayList<>();

		if (devUserDeveloper.getParentId().equals(devUserDeveloper.getUserId())) {

			arrDevUser.add(devUserDeveloper);

		} else {
			arrDevUser.add(devUserDeveloper);
			arrDevUser.add(devUserService.findByUserId(devUserDeveloper.getParentId()));
		}

		for (DevUser devUser : arrDevUser) {
			try {

				String body = "Dear User," + "<br><br/>";

				body += status.equalsIgnoreCase(DbConstant.APPROVED) ? "<b>Greetings from Bitvault Appstore.</b>" : "";

				body += "<br></br>" + "Your request of " + appRequestType + " for application " + appDto.getAppName()
						+ " has been " + status + " by Appstore Admin.";

				body += "<br></br>" + (status.equalsIgnoreCase(DbConstant.REJECTED) && reason != null
						? "<b>Reason : " + reason + ".</b>"
						: "");
				body += "<br></br>" + Constants.MAIL_SIGN;

				if (devUser != null && devUser.getStatus().equalsIgnoreCase(Constants.ACTIVE)) {
					mailService.sendMail(devUser.getEmail(),
							"Your Application " + appDto.getAppName() + " " + appRequestType + " " + status, body);
					logger.info("Sending mail to developer");
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("Exception occured while sending email");
			}
		}
	}

	/**
	 * Method is used to send mail to the developer when admin himself changes the
	 * status of the application
	 * 
	 * @param status
	 *            - status of the application
	 * @param appDto
	 *            - appDto
	 * @param reason
	 *            - reason for rejection
	 */
	private void sendMailToDeveloper(String status, AppApplication appDto, String reason) {
		DevUser devUserDeveloper = devUserService.findByUserId(appDto.getUserId());

		ArrayList<DevUser> arrDevUser = new ArrayList<>();

		if (devUserDeveloper.getParentId().equals(devUserDeveloper.getUserId())) {

			arrDevUser.add(devUserDeveloper);

		} else {
			arrDevUser.add(devUserDeveloper);
			arrDevUser.add(devUserService.findByUserId(devUserDeveloper.getParentId()));
		}

		for (DevUser devUser : arrDevUser) {
			try {

				String body = "Dear User," + "<br><br/>";

				body += status.equalsIgnoreCase(DbConstant.PUBLISHED) ? "<b>Greetings from Bitvault Appstore.</b>" : "";

				body += "<br></br>" + "Appstore Admin has " + status + " your application " + appDto.getAppName() + ".";

				body += "<br></br>" + (status.equalsIgnoreCase(DbConstant.UNPUBLISHED) && reason != null
						? "<b>Reason : " + reason + ".</b>"
						: "");

				body += "<br></br>" + Constants.MAIL_SIGN;

				if (devUser != null && devUser.getStatus().equalsIgnoreCase(Constants.ACTIVE)) {
					mailService.sendMail(devUser.getEmail(),
							"Your Application " + appDto.getAppName() + " " + status + " by Admin", body);
					logger.info("Sending mail to developer");
				}

			} catch (Exception e) {
				e.printStackTrace();
				logger.info("Exception occured while sending email");
			}
		}
	}
}
