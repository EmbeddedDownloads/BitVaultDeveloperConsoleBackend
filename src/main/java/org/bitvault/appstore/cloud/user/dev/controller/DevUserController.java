package org.bitvault.appstore.cloud.user.dev.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.bitvault.appstore.cloud.config.PropertiesConfig;
import org.bitvault.appstore.cloud.constant.APIConstants;
import org.bitvault.appstore.cloud.constant.Constants;
import org.bitvault.appstore.cloud.constant.DbConstant;
import org.bitvault.appstore.cloud.constant.ErrorCode;
import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.constant.RoleConstant;
import org.bitvault.appstore.cloud.dto.AccountDto;
import org.bitvault.appstore.cloud.dto.AppApplication;
import org.bitvault.appstore.cloud.dto.DevPaymentDto;
import org.bitvault.appstore.cloud.dto.DevUserDto;
import org.bitvault.appstore.cloud.dto.DevUserDtoMapper;
import org.bitvault.appstore.cloud.dto.UserActivityDto;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.bitvault.appstore.cloud.exception.BitVaultResponse;
import org.bitvault.appstore.cloud.model.DevPayment;
import org.bitvault.appstore.cloud.model.DevUser;
import org.bitvault.appstore.cloud.model.Request;
import org.bitvault.appstore.cloud.model.RequestActivity;
import org.bitvault.appstore.cloud.model.RequestType;
import org.bitvault.appstore.cloud.model.SubDevReq;
import org.bitvault.appstore.cloud.user.common.GeneralResponseModel;
import org.bitvault.appstore.cloud.user.common.dao.RequestActivityRepository;
import org.bitvault.appstore.cloud.user.common.service.DevPaymentService;
import org.bitvault.appstore.cloud.user.common.service.RequestActivityService;
import org.bitvault.appstore.cloud.user.common.service.RequestService;
import org.bitvault.appstore.cloud.user.dev.dao.UserActivityTypeRepository;
import org.bitvault.appstore.cloud.user.dev.service.AccountService;
import org.bitvault.appstore.cloud.user.dev.service.DevUserService;
import org.bitvault.appstore.cloud.user.dev.service.SubDevReqService;
import org.bitvault.appstore.cloud.user.dev.service.UserActivityService;
import org.bitvault.appstore.cloud.utils.Response;
import org.bitvault.appstore.cloud.utils.Utility;
import org.bitvault.appstore.cloud.validator.SignupValidator;
import org.bitvault.appstore.commons.application.service.AppApplicationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = APIConstants.DEV_API_BASE)
public class DevUserController {

	private static final Logger logger = LoggerFactory.getLogger(DevUserController.class);

	private static final String cONST = null;

	@Autowired
	private DevUserService devUserService;

	@Autowired
	private UserActivityService userActivityService;

	@Autowired
	private AccountService accountService;

	@Autowired
	private AppApplicationService appApplicationService;

	@Autowired
	private RequestService requestService;

	@Autowired
	DevPaymentService devPaymentService;

	@Autowired
	BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private SubDevReqService devReqService;

	@Autowired
	private PropertiesConfig propertiesConfig;

	@Autowired
	private RequestActivityService requestActivityService;

	@Autowired
	private RequestActivityRepository requestRepository;

	@Autowired
	SubDevReqService subDevReqService;

	@Autowired
	UserActivityTypeRepository userActvityTypeRepository;

	private Map<String, Object> result = null;

	/**
	 * This API serves the purpose to create a developer account with the initial
	 * status of developer is PENDING
	 * 
	 * @param userDto
	 * @return JSON with message
	 */
	@RequestMapping(value = { APIConstants.SAVE_DEV,
			APIConstants.ADD_USER }, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> saveDevUser(@RequestBody DevUserDto user, HttpServletRequest request) {
		result = new HashMap<>();
		try {
			String signupAs = user.getSignupAs();
			String owner = (String) request.getAttribute(Constants.USERID);
			DevUser userParent = null;
			if (owner != null) {
				try {
					userParent = devUserService.findByUserId(owner);
					if (!userParent.getAccount().getRole().getRoleName().contains(RoleConstant.ORG)
							|| !userParent.getStatus().equalsIgnoreCase(DbConstant.ACTIVE))
						throw new BitVaultException(ErrorMessageConstant.INVALID_REQUEST);
					int childCountDiff = userParent.getChildCount() - userParent.getChildCreated();
					if (childCountDiff > 0) {
						List<DevPayment> devPaymentList = devPaymentService
								.findByUserIdAndPaymentFor(owner, Constants.SUBDEV,
										new PageRequest(0, 1, new Sort(Direction.DESC, Constants.CREATED_AT)))
								.getContent();
						if (!devPaymentList.isEmpty()) {
							for (DevPayment devPayment : devPaymentList) {
								// DevPayment devPayment =
								// devPaymentList.get(0);
								if (!devPayment.getTxnStatus().equalsIgnoreCase(Constants.SUCCESS)) {
									return ResponseEntity
											.ok(GeneralResponseModel.of(Constants.FAILED, new BitVaultResponse(
													ErrorMessageConstant.PAYMENT_NOT_DONE + " to add more users")));
								}
							}
						}
					}

					if (childCountDiff <= 0) {
						throw new BitVaultException(
								ErrorMessageConstant.USER_CREATION_NOT_ALLOWED + userParent.getChildCount());
					}

				} catch (Exception e) {
					throw new BitVaultException(e.getMessage());
				}
			}
			Map<String, String> validateUser = null;
			if (signupAs != null) {
				validateUser = SignupValidator.validateUserDetails(user, signupAs.toUpperCase());
				if (validateUser.size() > 0) {
					result.put(Constants.MESSAGE, validateUser);
					return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED, result));
				}
			} else {
				result.put(Constants.MESSAGE, "Incomplete information provided.");
				return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED, result));
			}
			logger.info("Validation of UI paramenters: \n" + validateUser);

			if (validateUser.toString().contains("false")) {
				return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED, validateUser));
			}

			else if (devUserService.isUserExist(user)) {
				result.put(Constants.MESSAGE,
						"Unable to create. A User with this email " + user.getEmail() + " is already exist.");
				return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED, result));
			}
			accountService.saveAccount(user, userParent);
			logger.info("User and it's associated account has been succesfully persisted into DB.");
			return ResponseEntity.ok(GeneralResponseModel.of(Constants.SUCCESS,
					new BitVaultResponse(Constants.VERIFY_EMAIL_1 + user.getEmail() + Constants.VERIFY_EMAIL_2)));

		}
		// catch(DataIntegrityViolationException e){
		// result.put(Constants.MESSAGE, ErrorMessageConstant.UNABLE_TO_SAVE);
		// return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED,
		// result));
		// }
		catch (BitVaultException | NullPointerException e) {
			logger.error("Error occured during save the user.");
			result.put(Constants.MESSAGE, e.getMessage());
			return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED, result));
		}
	}

	/*
	 * update a user details i.e. gender, alternate email, username (Profile name)
	 */
	/**
	 * @param userId
	 * @param user
	 * @return
	 */
	// @PreAuthorize("hasRole('DEVELOPER')")
	// @Secured("READ_DEVELOPER")
	@PutMapping(value = APIConstants.UPDATE_USER_INFO, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateDevUser(@RequestBody DevUserDto userDto, HttpServletRequest request) {
		result = new HashMap<>();
		try {
			DevUser currentUser = devUserService.findByEmailId((String) request.getAttribute("email"));
			if (currentUser == null) {
				return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED,
						new BitVaultResponse(ErrorMessageConstant.USER_UPDATE_FAILED)));
			}
			String profileName = userDto.getUsername();
			if (Utility.isStringEmpty(profileName)) {
				return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED,
						new BitVaultResponse(ErrorMessageConstant.FIELD_IS_EMPTY)));
			}

			getDifferentField(currentUser, userDto);

			devUserService.updateUser(currentUser, userDto);

			logger.info("Update User info -- Done.");
			result.put(Constants.MESSAGE, Constants.SUCCESS_UPDATED);
			return ResponseEntity.ok(GeneralResponseModel.of(Constants.SUCCESS, result));
		} catch (BitVaultException e) {
			logger.info("Error occured: " + e.getMessage());
			return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED, new BitVaultResponse(e.getMessage())));
		}
	}

	/**
	 * Method used to make entry in User Auditing
	 * 
	 * @param currentUser
	 *            -- current user details
	 * @param updatedUser
	 *            -- new user details
	 */
	private void getDifferentField(DevUser currentUser, DevUserDto updatedUser) {

		try {

			UserActivityDto userActivityDto = null;

			if (!currentUser.getUsername().equals(updatedUser.getUsername())) {

				userActivityDto = new UserActivityDto();

				userActivityDto = userActivityDto.populateUserDetails(currentUser, userActivityDto);

				userActivityDto.setUserName(updatedUser.getUsername());

				userActivityDto.setUserActivityType(userActvityTypeRepository.getOne(1));

				userActivityService.saveUserHistory(userActivityDto);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED, ErrorCode.SOME_ERROR_OCCURED_CODE);
		}
	}

	@GetMapping(value = APIConstants.GET_USER_INFO, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getUserDetails(HttpServletRequest request) {
		result = new HashMap<>();
		DevUserDto userDto = devUserService.findUserDtoByUserId((String) request.getAttribute(Constants.USERID));
		if (userDto == null) {
			return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED,
					new BitVaultResponse(ErrorMessageConstant.USER_NOT_FOUND)));
		}
		return ResponseEntity.ok(GeneralResponseModel.of(Constants.SUCCESS, userDto));
	}

	// @Autowired
	// AuthTokenService authTokenService;

	@GetMapping(value = APIConstants.VERIFY_EMAIL, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> verifyEmailId(@PathVariable String userId, HttpServletRequest request) {
		try {
			DevUser user = devUserService.findByUserId(userId);
			if (user == null) {
				return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED,
						new BitVaultResponse(ErrorMessageConstant.USER_NOT_FOUND)));
			}
			if (user.getVerificationLink().isEmpty()) {
				return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED,
						new BitVaultResponse(ErrorMessageConstant.LINK_EXPIRED)));
			}
			String parentId = user.getParentId();
			String successMessage = Constants.REG_SUCCESS;
			request.setAttribute(Constants.USERID, userId);
			if (userId.equals(parentId)) {
				Request accRequest = new Request();
				accRequest.setUserId(user.getUserId());
				RequestType requestType = new RequestType();
				requestType.setRequestTypeId(1);
				accRequest.setRequestType(requestType);
				accRequest.setStatus(Constants.PENDING);
				requestService.persistRequest(accRequest);
				if (accRequest != null) {
					requestActivityService.saveRequestActivity(accRequest, null);
				}
			} else {
				request.setAttribute(Constants.USERID, userId);
				user.getAccount().setStatus(DbConstant.ACTIVE);
				user.setStatus(DbConstant.ACTIVE);
				successMessage = Constants.EMAIL_VERIFIED_SUCCESS;
			}
			user.setVerificationLink("");
			devUserService.saveUser(user);
			return ResponseEntity.ok(GeneralResponseModel.of(Constants.SUCCESS, new BitVaultResponse(successMessage)));
		} catch (Exception e) {
			logger.error(e.getMessage());
			return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED,
					new BitVaultResponse(ErrorMessageConstant.UNABLE_TO_VERIFY_EMAIL)));
		}
	}

	private String tokenHeader;

	/**
	 * invalidate the access token
	 * 
	 * @param request
	 * @return JSON
	 */
	// @RequestMapping(value = APIConstants.LOGOUT, method = RequestMethod.POST,
	// produces = MediaType.APPLICATION_JSON_VALUE)
	// public ResponseEntity<?> releaseAuthToken(HttpServletRequest request) {
	// String token = request.getHeader(tokenHeader);
	// return null;
	//
	// // if(!authTokenService.isTokenExists(token)){
	// // return ResponseEntity.badRequest().body("{\"messgage\" : \"No session
	// // found.\"}");
	// // }
	// //
	// // authTokenService.deleteToken(token);
	// // return ResponseEntity.ok("{\"messgage\" : \"Successfully logged
	// // out.\"}");
	// }

	@PutMapping(value = APIConstants.UPDATE_DEV_PAYMENT)
	public ResponseEntity<?> updateDevPayment(HttpServletRequest request) {
		String pay = (String) request.getAttribute(Constants.PAY_CHECK);
		if (pay != null)
			if (pay.equals("done"))
				return ResponseEntity
						.ok(GeneralResponseModel.of(Constants.SUCCESS, new BitVaultResponse(Constants.PAYMENT_DONE)));
			else
				return ResponseEntity.ok(GeneralResponseModel.of(Constants.SUCCESS,
						new BitVaultResponse(Constants.PAYMENT_ALREADY_DONE)));
		else
			return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED,
					new BitVaultResponse(ErrorMessageConstant.SOME_ERROR_OCCURED)));
	}

	@PutMapping(value = APIConstants.REQUEST_MORE_USER)
	public ResponseEntity<?> requestMoreUser(@PathVariable int count, HttpServletRequest request) {
		try {
			if (Utility.isIntegerEmpty(count) || count <= 0) {
				throw new BitVaultException(ErrorMessageConstant.INVALID_COUNT);
			}
			String userId = (String) request.getAttribute(Constants.USERID);
			DevUser orgUser = devUserService.findByUserId(userId);
			if (!orgUser.getAccount().getRole().getRoleName().contains(RoleConstant.ORG)
					|| !orgUser.getStatus().equalsIgnoreCase(DbConstant.ACTIVE)) {
				throw new BitVaultException(ErrorMessageConstant.INVALID_REQUEST);
			}
			List<SubDevReq> listSubDevReq = (devReqService.allSubDevRequestsForUser(userId,
					Arrays.asList(DbConstant.PENDING),
					new PageRequest(0, 1, new Sort(Direction.DESC, Constants.CREATED_AT)))).getContent();
			if (!listSubDevReq.isEmpty()) {
				return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED,
						new BitVaultResponse(ErrorMessageConstant.SUBDEV_REQUEST_PENDING)));
			}
			SubDevReq subDev = new SubDevReq();
			subDev.setChildCount(count);
			subDev.setUserId(userId);
			subDev.setStatus(DbConstant.PENDING);
			devReqService.saveSubDevRequest(subDev);
			if (subDev != null) {
				requestActivityService.saveRequestActivity(null, subDev);
			}
			return ResponseEntity.ok(GeneralResponseModel.of(Constants.SUCCESS,
					new BitVaultResponse(Constants.REQUEST_SUCCESSFULLY_SUBMITTED)));
		} catch (Exception e) {
			logger.error("Exception occured while generating more user request " + e.getMessage());
			return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED,
					new BitVaultResponse(ErrorMessageConstant.SOME_ERROR_OCCURED)));
		}
	}

	@GetMapping(value = { APIConstants.GET_SUB_DEV_LIST })
	public ResponseEntity<?> getSubDevList(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(required = false, defaultValue = "createdAt") String orderBy,
			@RequestParam(required = false, defaultValue = DbConstant.ACTIVE + "," + DbConstant.REVIEW + ","
					+ DbConstant.REJECTED + "," + DbConstant.INACTIVE) List<String> status,
			@RequestParam(required = false, defaultValue = "ASC") String direction, HttpServletRequest request) {
		try {
			if (page > 0) {
				page--;
			}
			String userId = (String) request.getAttribute(Constants.USERID);
			if (userId == null) {
				return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED,
						new BitVaultResponse(ErrorMessageConstant.RESULT_NOT_FOUND)));
			}
			return ResponseEntity
					.ok(devUserService.getSubDevList(userId, userId, page, size, orderBy, status, direction));
		} catch (Exception e) {
			logger.error("Exception occure while generating more user request " + e.getMessage());
			return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED,
					new BitVaultResponse(ErrorMessageConstant.SOME_ERROR_OCCURED)));
		}
	}

	@GetMapping(value = { APIConstants.GET_SUB_DEV })
	public ResponseEntity<?> getSubDevDetails(@PathVariable("userId") String childUserId, HttpServletRequest request) {
		try {
			String parentId = (String) request.getAttribute(Constants.USERID);
			DevUser childUser = validateSubDevUser(parentId, childUserId);
			DevUserDto devUserDto = DevUserDtoMapper.toDevUserDto(childUser);
			devUserDto.setSignupAs("Organization Developer");
			return ResponseEntity.ok(GeneralResponseModel.of(Constants.SUCCESS, devUserDto));
		} catch (Exception e) {
			logger.error("Exception occure while generating more user request " + e.getMessage());
			return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED,
					new BitVaultResponse(ErrorMessageConstant.SOME_ERROR_OCCURED)));
		}
	}

	@PutMapping(value = { APIConstants.GET_SUB_DEV })
	public ResponseEntity<?> updateSubDevDetails(@PathVariable("userId") String childUserId, HttpServletRequest request,
			@RequestBody Map<String, String> inputMap) {
		try {
			String parentId = (String) request.getAttribute(Constants.USERID);
			DevUser childUser = validateSubDevUser(parentId, childUserId);
			// if(childUser.getStatus().equalsIgnoreCase(DbConstant.REVIEW)){
			// throw new
			// BitVaultException(ErrorMessageConstant.USER_EMAIL_NOT_VERIFIED);
			// }
			String email = inputMap.get("email");
			String username = inputMap.get("username");
			String password = inputMap.get("password");
			String status = inputMap.get("status");
			if (!SignupValidator.isValidEmailID(email) || !SignupValidator.isValidUsername(username)
					|| Utility.isStringEmpty(status)
					|| !status.equalsIgnoreCase(DbConstant.ACTIVE) && !status.equalsIgnoreCase(DbConstant.INACTIVE)
							&& !status.equalsIgnoreCase(DbConstant.REVIEW)) {
				throw new BitVaultException(ErrorMessageConstant.REQUIRED_FIELD_MISSING_2);
			}
			if (!childUser.getEmail().equalsIgnoreCase(email)) {
				DevUser userExists = devUserService.findByEmailId(email);
				if (userExists != null) {
					throw new BitVaultException(ErrorMessageConstant.USER_ALREADY_EXISTS);
				}
			}
			if (!Utility.isStringEmpty(password)) {
				childUser.setPassword(passwordEncoder.encode(password));
			}
			if (!childUser.getStatus().equalsIgnoreCase(DbConstant.REVIEW)) {
				childUser.setStatus(status);
			}
			childUser.setUsername(username);
			if (!email.equalsIgnoreCase(childUser.getEmail())) {
				AccountDto account = accountService.getAccountByEmail(email);
				if (account != null) {
					throw new BitVaultException(ErrorMessageConstant.USER_ALREADY_EXISTS);
				}
				String verificationLink = devUserService.generateVerificationLink(propertiesConfig.getHost(),
						childUser.getUserId(), email);
				childUser.setEmail(email);
				childUser.setVerificationLink(verificationLink);
				childUser.setStatus(DbConstant.REVIEW);
				childUser.getAccount().setAccEmail(email);
				devUserService.saveUser(childUser);
				return ResponseEntity.ok(GeneralResponseModel.of(Constants.SUCCESS,
						new BitVaultResponse(Constants.VERIFY_EMAIL_1 + email + Constants.VERIFY_EMAIL_2)));
			}
			devUserService.saveUser(childUser);
			return ResponseEntity.ok(GeneralResponseModel.of(Constants.SUCCESS, Constants.SUCCESS_UPDATED));
		} catch (Exception e) {
			logger.error("Exception occure while generating more user request " + e.getMessage());
			return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED, new BitVaultResponse(e.getMessage())));
		}
	}

	private DevUser validateSubDevUser(String userId, String childUserId) {
		if (childUserId == null || userId == null) {
			throw new BitVaultException(ErrorMessageConstant.RESULT_NOT_FOUND);
		}
		DevUser childUser = devUserService.findByUserId(childUserId);
		if (childUser == null || !childUser.getParentId().equalsIgnoreCase(userId)) {
			throw new BitVaultException(ErrorMessageConstant.INVALID_REQUEST);
		}
		return childUser;
	}

	@GetMapping(value = { APIConstants.GET_TRANSACTION_LIST })
	public ResponseEntity<?> getAllTransactions(HttpServletRequest request, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size,
			@RequestParam(defaultValue = Constants.UPDATED_AT) String orderBy,
			@RequestParam(defaultValue = DbConstant.DESC) String direction) {

		List<DevPaymentDto> updatedPaymentList = null;
		Map<String, Object> allMap = null;
		Response response = null;

		try {

			logger.info("Getting transaction list of the logged in user");

			String userId = (String) request.getAttribute(Constants.USERID);

			if (userId == null) {
				return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED,
						new BitVaultResponse(ErrorMessageConstant.USER_NOT_FOUND)));
			}

			allMap = getAllTransactionsByUserId(userId, page, size, orderBy, direction);

			if (allMap == null) {

				response = new Response();
				response.setResult(new ArrayList<DevPaymentDto>());
				response.setStatus(Constants.SUCCESS);
				return new ResponseEntity<Response>(response, HttpStatus.OK);
			}

			updatedPaymentList = (List<DevPaymentDto>) allMap.get("paymentList");

			if (updatedPaymentList == null) {
				response = new Response();
				response.setStatus(Constants.SUCCESS);
				response.setResult(new ArrayList<DevPaymentDto>());
				return new ResponseEntity<Response>(response, HttpStatus.OK);
			} else {
				int totalPages = (int) allMap.get(Constants.TOTAL_PAGES);
				int totalRecord = ((Long) allMap.get(Constants.TOTAL_RECORDS)).intValue();
				int pageSize = (int) allMap.get(Constants.SIZE);
				response = new Response();
				response.setResult(updatedPaymentList);
				response.setSize(pageSize);
				response.setTotalPages(totalPages);
				response.setTotalRecords(totalRecord);
				response.setSort(orderBy);
				response.setStatus(Constants.SUCCESS);

				return new ResponseEntity<Response>(response, HttpStatus.OK);
			}
		} catch (Exception e) {
			logger.error("Getting transaction list of the logged in user " + e.getMessage());
			return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED,
					new BitVaultResponse(ErrorMessageConstant.SOME_ERROR_OCCURED)));
		}
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping(value = { APIConstants.GET_ADMIN_TRANSACTION_LIST + "/{userId}" })
	public ResponseEntity<?> getAllTransactionsAdmin(HttpServletRequest request, @PathVariable("userId") String userId,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size,
			@RequestParam(defaultValue = Constants.UPDATED_AT) String orderBy,
			@RequestParam(defaultValue = DbConstant.DESC) String direction) {

		List<DevPaymentDto> updatedPaymentList = null;
		Map<String, Object> allMap = null;
		Response response = null;

		try {

			logger.info("Getting transaction list of the user by Admin");

			if (userId == null) {
				return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED,
						new BitVaultResponse(ErrorMessageConstant.USER_NOT_FOUND)));
			}

			DevUser devUser = devUserService.findByUserId(userId);

			if (devUser == null) {
				return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED,
						new BitVaultResponse(ErrorMessageConstant.USER_NOT_FOUND)));
			}

			allMap = getAllTransactionsByUserId(userId, page, size, orderBy, direction);

			if (allMap == null) {

				response = new Response();
				response.setResult(new ArrayList<DevPaymentDto>());
				response.setStatus(Constants.SUCCESS);
				return new ResponseEntity<Response>(response, HttpStatus.OK);
			}

			updatedPaymentList = (List<DevPaymentDto>) allMap.get("paymentList");

			if (updatedPaymentList == null) {
				response = new Response();
				response.setStatus(Constants.SUCCESS);
				response.setResult(new ArrayList<DevPaymentDto>());
				return new ResponseEntity<Response>(response, HttpStatus.OK);
			} else {
				int totalPages = (int) allMap.get(Constants.TOTAL_PAGES);
				int totalRecord = ((Long) allMap.get(Constants.TOTAL_RECORDS)).intValue();
				int pageSize = (int) allMap.get(Constants.SIZE);
				response = new Response();
				response.setResult(updatedPaymentList);
				response.setSize(pageSize);
				response.setTotalPages(totalPages);
				response.setTotalRecords(totalRecord);
				response.setSort(orderBy);
				response.setStatus(Constants.SUCCESS);
				response.setUserName(devUser.getUsername());
				return ResponseEntity.ok(response);
			}
		} catch (Exception e) {
			logger.error("Exception occure while getting transaction list of the user by admin" + e.getMessage());
			return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED,
					new BitVaultResponse(ErrorMessageConstant.SOME_ERROR_OCCURED)));
		}
	}

	@GetMapping(value = { APIConstants.GET_KEY_FILE })
	public ResponseEntity<?> getKeyFile(HttpServletRequest request, HttpServletResponse response) {

		String userId = "", tempFilepath = "", fileName = Constants.KEY_FILE;

		logger.info("Getting key file to download");

		try {

			userId = (String) request.getAttribute(Constants.USERID);

			if (userId == null) {
				return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED,
						new BitVaultResponse(ErrorMessageConstant.USER_NOT_FOUND)));
			}

			DevUser devUser = devUserService.findByUserId(userId);

			if (devUser == null) {
				return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED,
						new BitVaultResponse(ErrorMessageConstant.USER_NOT_FOUND)));
			}

			return ResponseEntity.ok(GeneralResponseModel.of(Constants.SUCCESS, devUser.getPublicKey().trim()));

		} catch (Exception e) {
			logger.error("Exception occure while generating key file " + e.getMessage());
			return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED,
					new BitVaultResponse(ErrorMessageConstant.SOME_ERROR_OCCURED)));
		}
	}

	public void deleteTempApkFolder(String path) throws BitVaultException {
		try {
			FileUtils.deleteDirectory(new File(path));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Map<String, Object> getAllTransactionsByUserId(String userId, int page, int size, String orderBy,
			String direction) {

		List<DevPayment> paymentList = null;
		List<DevPaymentDto> updatedPaymentList = null;
		Page<DevPayment> appList = null;
		DevPaymentDto devPaymentDto;
		Integer childCount = 0;
		Map<String, Object> allAppMap = new HashMap<String, Object>();

		if (page > 0) {
			page--;
		}

		appList = devPaymentService.findByUserId(userId, new PageRequest(page, size,
				direction.equalsIgnoreCase(DbConstant.DESC) ? Sort.Direction.DESC : Sort.Direction.ASC, orderBy));

		// appList = paymentList ;

		if (appList != null && appList.getNumberOfElements() == 0) {
			return null;
		} else {
			updatedPaymentList = new ArrayList<>();
		}

		for (DevPayment devPayment : appList) {

			if (devPayment.getPaymentFor().equals(Constants.SUBDEV)) {
				childCount = devReqService.getNumberOfChildForATransaction(devPayment.getDevPaymentId());
			} else {
				childCount = 0;
			}

			if (childCount == null) {
				childCount = 0;
			}

			devPaymentDto = devPayment.populateDto(devPayment);
			devPaymentDto.setChildCount(childCount);
			updatedPaymentList.add(devPaymentDto);
		}

		allAppMap.put("paymentList", updatedPaymentList);
		allAppMap.put(Constants.TOTAL_PAGES, appList.getTotalPages());
		allAppMap.put(Constants.TOTAL_RECORDS, appList.getTotalElements());
		allAppMap.put(Constants.SIZE, appList.getNumberOfElements());
		allAppMap.put(Constants.SORT, appList.getSort());

		return allAppMap;

	}

	@GetMapping(value = { APIConstants.GET_REQUESTS_ACTIVITY_DETAIL + "/{userId}",
			APIConstants.GET_USER_APP_REQUEST_DETAIL })
	public ResponseEntity<Response> getListOfRequest(@RequestParam(required = false, defaultValue = "1") int page,
			@RequestParam(required = false, defaultValue = "5") int size,
			@RequestParam(required = false, defaultValue = Constants.UPDATED_AT) String orderBy,
			@PathVariable(required = false, value = "") String userId,
			@RequestParam(required = false, defaultValue = "-1") int applicationId, HttpServletRequest request) {
		if (page > 0) {
			page = page - 1;
		}
		Response response = new Response();
		Map<String, Object> allAppMap = null;
		String userIdRequest;

		try {
			if (request.getRequestURI().contains(APIConstants.GET_REQUESTS_ACTIVITY_DETAIL)) {
				allAppMap = requestActivityService.findRequest(applicationId, userId, page, size, DbConstant.ASC,
						orderBy);
				logger.info("All Request of particular user fetched succesfully..");
			} else {
				userIdRequest = getUserIdByAuthRequest(request);
				if (!Utility.isIntegerEmpty(applicationId)) {
					AppApplication app = appApplicationService.findApplicationByAppId(applicationId);
					if (app != null) {
						allAppMap = requestActivityService.findRequest(applicationId, userIdRequest, page, size,
								DbConstant.ASC, orderBy);
					} else {
						throw new BitVaultException(ErrorMessageConstant.RESULT_NOT_FOUND);
					}

				} else {
					throw new BitVaultException(ErrorMessageConstant.FIELD_IS_EMPTY);
				}
				logger.info("All Request of particular application fetched successfully..");
			}
			response.setResult(allAppMap.get("requestList"));
			int totalPages = (int) allAppMap.get(Constants.TOTAL_PAGES);
			int totalRecord = ((Long) allAppMap.get(Constants.TOTAL_RECORDS)).intValue();
			int pageSize = (int) allAppMap.get(Constants.SIZE);
			response.setStatus(Constants.SUCCESS);
			response.setSize(pageSize);
			response.setTotalPages(totalPages);
			response.setTotalRecords(totalRecord);
			response.setSort(orderBy);
		}

		catch (BitVaultException e) {
			logger.error("Error occured During List fetching");
			response.setResult(new BitVaultResponse(e));

			response.setStatus(Constants.FAILED);
			return new ResponseEntity<Response>(response, HttpStatus.EXPECTATION_FAILED);

		}
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}

	private String getUserIdByAuthRequest(HttpServletRequest request) {
		String userId = null;

		try {
			userId = (String) request.getAttribute("userId");
		} catch (Exception e) {
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);
		}
		if (null == userId) {
			throw new BitVaultException(ErrorMessageConstant.USER_NOT_FOUND);

		}
		return userId;
	}

	@RequestMapping(value = APIConstants.ELASTIC_SEARCH_DEV_USER, method = RequestMethod.GET)
	public ResponseEntity<?> elasticSearchUser(@RequestParam String username,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size,
			@RequestParam(defaultValue = DbConstant.USERNAME) String orderBy, HttpServletRequest request) {
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
				String userId = getUserIdByAuthRequest(request);
				allAppMap = devUserService.searchUserByUserName(username, userId, page, size,
						DbConstant.DESC.toString(), orderBy);
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
				logger.error("Error occured during ");
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

	@GetMapping(value = APIConstants.GET_USER_All_REQUEST_DETAIL)
	public ResponseEntity<Response> getListOfRequest(@RequestParam(required = false, defaultValue = "1") int page,
			@RequestParam(required = false, defaultValue = "5") int size,
			@RequestParam(required = false, defaultValue = Constants.UPDATED_AT) String orderBy,
			HttpServletRequest request) {
		if (page > 0) {
			page = page - 1;
		}
		Response response = new Response();
		Map<String, Object> allAppMap = null;

		try {
			String userId = getUserIdByAuthRequest(request);

			allAppMap = requestService.findAllRequestByUser(userId, page, size, DbConstant.DESC, orderBy);
			response.setResult(allAppMap.get("requestList"));
			int totalPages = (int) allAppMap.get(Constants.TOTAL_PAGES);
			int totalRecord = ((Long) allAppMap.get(Constants.TOTAL_RECORDS)).intValue();
			int pageSize = (int) allAppMap.get(Constants.SIZE);
			response.setStatus(Constants.SUCCESS);
			response.setSize(pageSize);
			response.setTotalPages(totalPages);
			response.setTotalRecords(totalRecord);
			response.setSort(orderBy);
		}

		catch (BitVaultException e) {
			response.setResult(new BitVaultResponse(e));

			response.setStatus(Constants.FAILED);
			return new ResponseEntity<Response>(response, HttpStatus.EXPECTATION_FAILED);

		}
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}

	@GetMapping(value = APIConstants.GET_ALL_REQUEST)
	public ResponseEntity<Response> getReqActListByReqId(@RequestParam Integer requestId,
			@RequestParam(required = false, defaultValue = "1") int page,
			@RequestParam(required = false, defaultValue = "5") int size,
			@RequestParam(required = false, defaultValue = Constants.UPDATED_AT) String orderBy) {
		if (page > 0) {
			page = page - 1;
		}
		Response response = new Response();
		Map<String, Object> allAppMap = null;

		try {

			allAppMap = requestActivityService.findAllRequestByRequestId(requestId, page, size, DbConstant.DESC,
					orderBy);
			response.setResult(allAppMap.get("requestList"));
			int totalPages = (int) allAppMap.get(Constants.TOTAL_PAGES);
			int totalRecord = ((Long) allAppMap.get(Constants.TOTAL_RECORDS)).intValue();
			int pageSize = (int) allAppMap.get(Constants.SIZE);
			response.setStatus(Constants.SUCCESS);
			response.setSize(pageSize);
			response.setTotalPages(totalPages);
			response.setTotalRecords(totalRecord);
			response.setSort(orderBy);
		}

		catch (BitVaultException e) {
			response.setResult(new BitVaultResponse(e));

			response.setStatus(Constants.FAILED);
			return new ResponseEntity<Response>(response, HttpStatus.EXPECTATION_FAILED);

		}
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}

	@GetMapping(value = APIConstants.GET_ALL_USER_REQUEST)
	public ResponseEntity<Response> getUserRequest(@RequestParam(required = false, defaultValue = "1") int page,
			@RequestParam(required = false, defaultValue = "5") int size,
			@RequestParam(required = false, defaultValue = Constants.UPDATED_AT) String orderBy,
			HttpServletRequest request) {
		if (page > 0) {
			page = page - 1;
		}

		Response response = new Response();

		try {
			String userId = getUserIdByAuthRequest(request);

			getUserAuditList(userId, page, size, orderBy, response);
		}

		catch (BitVaultException e) {
			response.setResult(new BitVaultResponse(e));

			response.setStatus(Constants.FAILED);
			return new ResponseEntity<Response>(response, HttpStatus.EXPECTATION_FAILED);

		}
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping(value = APIConstants.GET_ALL_USER_REQUEST_BY_ADMIN + "/{userId}")
	public ResponseEntity<Response> getUserRequestByAdmin(@RequestParam(required = false, defaultValue = "1") int page,
			@RequestParam(required = false, defaultValue = "5") int size,
			@RequestParam(required = false, defaultValue = Constants.UPDATED_AT) String orderBy,
			HttpServletRequest request, @PathVariable String userId) {
		if (page > 0) {
			page = page - 1;
		}

		Response response = new Response();

		try {

			getUserAuditList(userId, page, size, orderBy, response);
		}

		catch (BitVaultException e) {
			response.setResult(new BitVaultResponse(e));

			response.setStatus(Constants.FAILED);
			return new ResponseEntity<Response>(response, HttpStatus.EXPECTATION_FAILED);

		}
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}

	private Response getUserAuditList(String userId, int page, int size, String orderBy, Response response) {
		Map<String, Object> allAppMap = null;
		try {
			DevUser devUser = devUserService.findByUserId(userId);

			if (devUser == null) {
				throw new BitVaultException(ErrorMessageConstant.USER_NOT_FOUND);
			}

			String userName = devUser.getUsername();
			String email = devUser.getEmail();
			allAppMap = userActivityService.findAllRequestByUser(userId, userName, email, page, size, DbConstant.ASC,
					orderBy);
			response.setResult(allAppMap.get("userList"));
			int totalPages = (int) allAppMap.get(Constants.TOTAL_PAGES);
			int totalRecord = ((Long) allAppMap.get(Constants.TOTAL_RECORDS)).intValue();
			int pageSize = (int) allAppMap.get(Constants.SIZE);
			response.setStatus(Constants.SUCCESS);
			response.setSize(pageSize);
			response.setTotalPages(totalPages);
			response.setTotalRecords(totalRecord);
			response.setSort(orderBy);
		} catch (BitVaultException e) {
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);
		}
		return response;
	}

	/**
	 * Getting all alerts for developer
	 * 
	 * @param page
	 * @param size
	 * @param direction
	 * @param property
	 * @param request
	 * @return
	 */
	@GetMapping(value = APIConstants.GET_ALL_ALERTS)
	public ResponseEntity<Response> getAlertData(@RequestParam(required = false, defaultValue = "1") int page,
			@RequestParam(required = false, defaultValue = "5") int size,
			@RequestParam(required = false, defaultValue = DbConstant.DESC) String direction,
			@RequestParam(required = false, defaultValue = Constants.UPDATED_AT) String property,
			HttpServletRequest request) {

		Response response;

		try {

			String userId = getUserIdByAuthRequest(request);

			response = getAllAlerts(userId, page, size, direction, property);

			return new ResponseEntity<Response>(response, HttpStatus.OK);

		} catch (BitVaultException e) {
			e.printStackTrace();
			response = new Response();
			response.setResult(new BitVaultResponse(e));

			response.setStatus(Constants.FAILED);
			return new ResponseEntity<Response>(response, HttpStatus.EXPECTATION_FAILED);
		}
	}

	/**
	 * Getting all alerts from admin for a developer
	 * 
	 * @param page
	 * @param size
	 * @param direction
	 * @param property
	 * @param request
	 * @param userId
	 * @return
	 */
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping(value = APIConstants.GET_ALL_ALERTS_BY_USERID)
	public ResponseEntity<Response> getAdminAlertData(@RequestParam(required = false, defaultValue = "1") int page,
			@RequestParam(required = false, defaultValue = "5") int size,
			@RequestParam(required = false, defaultValue = DbConstant.DESC) String direction,
			@RequestParam(required = false, defaultValue = Constants.UPDATED_AT) String property,
			HttpServletRequest request) {

		Response response;

		try {

			// DevUser devUser = devUserService.findByUserId(userId);
			//
			// if (devUser == null) {
			// throw new BitVaultException(ErrorMessageConstant.USER_NOT_FOUND);
			// }

			response = getAllAlerts(page, size, direction, property);

			return new ResponseEntity<Response>(response, HttpStatus.OK);

		} catch (BitVaultException e) {
			e.printStackTrace();
			response = new Response();
			response.setResult(new BitVaultResponse(e));
			response.setStatus(Constants.FAILED);
			return new ResponseEntity<Response>(response, HttpStatus.EXPECTATION_FAILED);
		}
	}

	/**
	 * Common method used to get the list of alerts from request Activity
	 * 
	 * @param userId
	 * @param page
	 * @param size
	 * @param direction
	 * @param property
	 * @return
	 */
	private Response getAllAlerts(String userId, int page, int size, String direction, String property) {

		Response response;
		List<Object> arrListData;
		try {

			logger.info("Getting all alerts list");

			Map<String, Object> requestResult = requestActivityService.findAllAlertRequestByUser(userId, page, size,
					direction, property);

			if (requestResult == null) {

				logger.info("Empty result found.");
				response = new Response();
				response.setStatus(Constants.SUCCESS);
				response.setResult(new ArrayList<>());

				return response;
			}

			if (requestResult.get("requestList") == null) {
				response = new Response();
				response.setStatus(Constants.SUCCESS);
				response.setResult(new ArrayList<>());

				return response;
			}

			arrListData = (List<Object>) requestResult.get("requestList");

			if (arrListData.size() == 0) {

				logger.info("No alerts found");

				response = new Response();
				response.setStatus(Constants.SUCCESS);
				response.setResult(new ArrayList<>());

				return response;
			}

			response = new Response();
			response.setResult(arrListData);
			int totalPages = (int) requestResult.get(Constants.TOTAL_PAGES);
			int totalRecord = ((Long) requestResult.get(Constants.TOTAL_RECORDS)).intValue();
			int pageSize = (int) requestResult.get(Constants.SIZE);
			response.setStatus(Constants.SUCCESS);
			response.setSize(pageSize);
			response.setTotalPages(totalPages);
			response.setTotalRecords(totalRecord);
			response.setSort(property);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Exception in getting all alerts " + e.getMessage());
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);
		}

		return response;
	}

	/**
	 * Common method used to get the list of alerts from request Activity
	 * 
	 * @param userId
	 * @param page
	 * @param size
	 * @param direction
	 * @param property
	 * @return
	 */
	private Response getAllAlerts(int page, int size, String direction, String property) {

		Response response;
		List<Object> arrListData;
		try {

			logger.info("Getting all alerts list");

			Map<String, Object> requestResult = requestActivityService.findAllAlertRequestByStatus(page, size,
					direction, property);

			if (requestResult == null) {

				logger.info("Empty result found.");
				response = new Response();
				response.setStatus(Constants.SUCCESS);
				response.setResult(new ArrayList<>());

				return response;
			}

			if (requestResult.get("requestList") == null) {
				response = new Response();
				response.setStatus(Constants.SUCCESS);
				response.setResult(new ArrayList<>());

				return response;
			}

			arrListData = (List<Object>) requestResult.get("requestList");

			if (arrListData.size() == 0) {

				logger.info("No alerts found");

				response = new Response();
				response.setStatus(Constants.SUCCESS);
				response.setResult(new ArrayList<>());

				return response;
			}

			response = new Response();
			response.setResult(arrListData);
			int totalPages = (int) requestResult.get(Constants.TOTAL_PAGES);
			int totalRecord = ((Long) requestResult.get(Constants.TOTAL_RECORDS)).intValue();
			int pageSize = (int) requestResult.get(Constants.SIZE);
			response.setStatus(Constants.SUCCESS);
			response.setSize(pageSize);
			response.setTotalPages(totalPages);
			response.setTotalRecords(totalRecord);
			response.setSort(property);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Exception in getting all alerts " + e.getMessage());
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);
		}

		return response;
	}

	/**
	 * Method is used to make alerts seen
	 * 
	 * @param requestIds
	 * @return
	 */
	@RequestMapping(value = APIConstants.READ_ALERTS, method = RequestMethod.POST)
	public ResponseEntity<GeneralResponseModel> changeSeenStatus(@RequestParam List<Integer> requestIds) {

		logger.info("Change alert seen status");

		try {

			List<RequestActivity> requestList = requestActivityService.markAlertsAsSeen(requestIds);

			if (requestList != null && requestList.size() > 0) {

				for (RequestActivity request : requestList) {
					request.setSeenStatus(Constants.SEEN);
					requestRepository.saveAndFlush(request);
				}

			} else {

				GeneralResponseModel response = GeneralResponseModel.of(Constants.SUCCESS,
						new BitVaultResponse(ErrorMessageConstant.NO_REQUEST_FOUND));

				return new ResponseEntity<GeneralResponseModel>(response, HttpStatus.OK);
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Exception occured at " + e.getMessage());

			GeneralResponseModel response = GeneralResponseModel.of(Constants.FAILED,
					new BitVaultResponse(ErrorMessageConstant.SOME_ERROR_OCCURED));

			return new ResponseEntity<GeneralResponseModel>(response, HttpStatus.OK);
		}

		GeneralResponseModel response = GeneralResponseModel.of(Constants.SUCCESS,
				new BitVaultResponse(Constants.SUCCESS_UPDATED));

		return new ResponseEntity<GeneralResponseModel>(response, HttpStatus.OK);

	}

}
