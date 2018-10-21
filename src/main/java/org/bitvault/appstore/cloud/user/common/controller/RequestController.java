package org.bitvault.appstore.cloud.user.common.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bitvault.appstore.cloud.constant.APIConstants;
import org.bitvault.appstore.cloud.constant.Constants;
import org.bitvault.appstore.cloud.constant.DbConstant;
import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.constant.RoleConstant;
import org.bitvault.appstore.cloud.dto.AppApplicationDetailDto;
import org.bitvault.appstore.cloud.dto.AppDetail;
import org.bitvault.appstore.cloud.dto.RequestAppApplicationDto;
import org.bitvault.appstore.cloud.dto.RequestUserDto;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.bitvault.appstore.cloud.exception.BitVaultResponse;
import org.bitvault.appstore.cloud.mail.MailService;
import org.bitvault.appstore.cloud.model.Account;
import org.bitvault.appstore.cloud.model.DevPayment;
import org.bitvault.appstore.cloud.model.DevUser;
import org.bitvault.appstore.cloud.model.Request;
import org.bitvault.appstore.cloud.security.GeneratePrivatePublicKeyPair;
import org.bitvault.appstore.cloud.user.common.GeneralResponseModel;
import org.bitvault.appstore.cloud.user.common.service.DevPaymentService;
import org.bitvault.appstore.cloud.user.common.service.RequestActivityService;
import org.bitvault.appstore.cloud.user.common.service.RequestService;
import org.bitvault.appstore.cloud.user.dev.service.AccountService;
import org.bitvault.appstore.cloud.user.dev.service.DevUserService;
import org.bitvault.appstore.cloud.utils.Response;
import org.bitvault.appstore.commons.application.service.AppApplicationService;
import org.bitvault.appstore.commons.application.service.AppDetailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = APIConstants.ADMIN_API_BASE)

@PreAuthorize("hasRole('ADMIN')")
public class RequestController {
	final static Logger logger = LoggerFactory.getLogger(RequestController.class);

	@Autowired
	private RequestService requestService;

	@Autowired
	private DevUserService devUserService;

	@Autowired
	private AccountService accountService;

	@Autowired
	private MailService mailService;

	@Autowired
	AppApplicationService appApplicationService;

	@Autowired
	AppDetailService appDetailService;

	@Autowired
	DevPaymentService devPaymentService;
	@Autowired
	RequestActivityService requestActivityService;

	private Map<String, String> resultMap = null;

	@GetMapping(value = APIConstants.ACCOUNT_REQUEST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getAccountRequest(@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "5") int size,
			@RequestParam(required = false, defaultValue = "request_Id") String orderBy,
			@RequestParam(required = false, defaultValue = "PENDING") List<String> status,
			@RequestParam(required = false, defaultValue = "DESC") String direction) {
		try {
			if (page > 0) {
				page = page - 1;
			}
			Page<Request> requests = requestService.findAllUserRequests(page, size, orderBy, status, direction);
			logger.info(
					"fetched_count\t" + requests.getNumberOfElements() + "\ntotalPages\t" + requests.getTotalPages());
			List<RequestUserDto> requestUserDto = new ArrayList<RequestUserDto>();
			for (Request req : requests) {
				requestUserDto.add(processRequest(req));
			}
			Response res = new Response(Constants.SUCCESS, requestUserDto, requests.getTotalPages(),
					requests.getTotalElements(), requests.getSize(), orderBy);
			return ResponseEntity.ok(res);
		} catch (BeansException e) {
			logger.error("Error occured during to fetch user account request: " + e.getMessage());
			return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED, resultMap));
		}
	}

	private RequestUserDto processRequest(Request req) {
		try {
			RequestUserDto requestDto = new RequestUserDto();
			requestDto.setReqId(req.getRequest_Id());
			requestDto.setReason(req.getRejectionReason());
			DevUser userModel = devUserService.findByUserId(req.getUserId());
			String signUpAs = userModel.getAccount().getRole().getRoleName();
			BeanUtils.copyProperties(userModel, requestDto);
			if (signUpAs.equals("ROLE_" + RoleConstant.DEVELOPER))
				requestDto.setSignupAs(Constants.DEV);
			else
				requestDto.setSignupAs(Constants.ORG);
			requestDto.setStatus(req.getStatus());
			return requestDto;
		} catch (BeansException e) {
			logger.error("Exception occured during to fetch the pending request details: " + e.getMessage());
			return null;
		}
	}

	@PostMapping(value = APIConstants.ACTION_ON_ACC_REQUEST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> actionOnAccountRequest(@PathVariable Integer reqId,
			@RequestBody Map<String, String> requestMapper) {
		try {
			resultMap = new HashMap<>();
			String requestAction = requestMapper.get("requestAction");
			int noOfChild = 0;
			double payment = 0.0;
			try{
				noOfChild = Integer.parseInt(requestMapper.get("noOfChild"));
				payment = Double.parseDouble(requestMapper.get("payment"));
			}
			catch(Exception e){
				throw new BitVaultException(ErrorMessageConstant.REQUIRED_FIELD_MISSING_2);
			}
			if (requestAction != null) {
				requestAction = requestAction.toUpperCase();
			}
			Request userRequest = requestService.findByRequestID(reqId);
			if (userRequest == null) {
				throw new BitVaultException(ErrorMessageConstant.NO_REQUEST_FOUND + reqId);
			}
			String userId = userRequest.getUserId();
			DevUser user = devUserService.findByUserId(userId);
			if (user == null) {
				return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED, new BitVaultResponse(ErrorMessageConstant.USER_NOT_FOUND)));
			}
			Account account = user.getAccount();
			DevPayment devPayment = null;
			List<DevPayment> devPaymentList = devPaymentService.findByUserIdAndPaymentFor(userId, Constants.SELF,
					new PageRequest(0, 1, new Sort(Direction.DESC, Constants.CREATED_AT))).getContent();
			if (devPaymentList.isEmpty()) {
				devPayment = new DevPayment();
			}
			else{
				devPayment = devPaymentList.get(0);
			}
			String currentReqStatus = userRequest.getStatus();
			String reason = requestMapper.get("rejectionReason");
			if (currentReqStatus.equals(Constants.PENDING)) {
				if (requestAction.equals(Constants.APPROVED)) {
					// && !(user.getStatus().equals("ACTIVE"))
					userRequest.setRejectionReason("");
					if(noOfChild == 0 && user.getAccount().getRole().getRoleName().contains(RoleConstant.ORG))
						user.setChildCount(5);
					else
						user.setChildCount(noOfChild);
					devPayment.setUserId(userId);
					devPayment.setTxnStatus(DbConstant.PENDING);
					if(payment < 0){
						return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED, new BitVaultResponse(Constants.INVALID_PAYMENT_AMOUNT)));
					}
					else if(payment == 0.0){
						devPayment.setPaymentFor(Constants.SELF);
						devPayment.setTxnStatus(Constants.SUCCESS);
						devPayment.setAmountPaid(payment);
						devPayment.setTxnId("Free");
						devPayment.setPaymentMode("Admin");
					}
					devPayment.setPayment(payment);
					devPaymentService.savePaymentRecord(devPayment);
					
					// Generate private public key for the developer and save in DB
					/*Map<String, String> keyPair = GeneratePrivatePublicKeyPair.generatePrivatePublicKey();
					if (keyPair != null && keyPair.get(Constants.PRIVATE_KEY) != null
							&& keyPair.get(Constants.PUBLIC_KEY) != null) {
						user.setPrivateKey(keyPair.get(Constants.PRIVATE_KEY));
						user.setPublicKey(keyPair.get(Constants.PUBLIC_KEY));
					}*/
					
					savePublicPrivateKey(user);

					approveAccRequest(account, user, userRequest, "ACTIVE", Constants.APPROVED);
					resultMap.put(Constants.MESSAGE, Constants.APPROVED);
					
					
					
					
					mailService.sendMail(user.getEmail(), Constants.ACCOUNT_ACTION + " been approved",
							Constants.ACCOUNT_APPROVAL);
				} else if (requestAction.equals(Constants.REJECTED)) {
					if (reason == null || reason.trim().equals("")) {
						reason = "Unknown";
					}
					userRequest.setRejectionReason(reason);
					approveAccRequest(account, user, userRequest, Constants.REJECTED, Constants.REJECTED);
					/* Setting the response values */
					resultMap.put(Constants.MESSAGE, Constants.REJECTED);
					mailService.sendMail(user.getEmail(), Constants.ACCOUNT_ACTION + " been rejected",
							Constants.ACCOUNT_REJECTED + "<i>" + reason + "</i>" + Constants.MAIL_SIGN);
				}
			} else if (currentReqStatus.equals(Constants.APPROVED) && requestAction.equals(Constants.REJECTED)) {
				if (reason == null || reason.trim().equals("")) {
					reason = "Unknown";
				}
				userRequest.setRejectionReason(reason);
				approveAccRequest(account, user, userRequest, Constants.INACTIVE, Constants.REJECTED);
				resultMap.put(Constants.MESSAGE, Constants.REJECTED);
				mailService.sendMail(user.getEmail(), Constants.ACCOUNT_ACTION,
						Constants.ACCOUNT_DEACTIVATED + "<i>" + reason + "</i>" + Constants.MAIL_SIGN);
			} else if (currentReqStatus.equals(Constants.REJECTED) && requestAction.equals(Constants.APPROVED)) {
				userRequest.setRejectionReason("");
				if( noOfChild == 0 && user.getAccount().getRole().getRoleName().contains(RoleConstant.ORG))
					user.setChildCount(5);
				else
					user.setChildCount(noOfChild);
				devPayment.setUserId(userId);
				if(payment == 0.0){
					devPayment.setPaymentFor(Constants.SELF);
					devPayment.setTxnStatus(Constants.SUCCESS);
					devPayment.setAmountPaid(payment);
					devPayment.setTxnId("Free");
					devPayment.setPaymentMode("Admin");
				}
				else
					devPayment.setTxnStatus(DbConstant.PENDING);
				devPayment.setPayment(payment);
				devPaymentService.savePaymentRecord(devPayment);
				
				savePublicPrivateKey(user);
				
				approveAccRequest(account, user, userRequest, Constants.ACTIVE, Constants.APPROVED);
				resultMap.put(Constants.MESSAGE, Constants.APPROVED);
				mailService.sendMail(user.getEmail(), Constants.ACCOUNT_ACTION, Constants.ACCOUNT_APPROVAL);

			} else {
				resultMap.put(Constants.MESSAGE, ErrorMessageConstant.NO_PROPER_ACTION_TAKEN);
				return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED, resultMap));
			}
			return ResponseEntity.ok(GeneralResponseModel.of(Constants.SUCCESS, resultMap));

		} catch (Exception e) {
			logger.error("Error during admin action for account approval: " + e.getMessage());
			if (resultMap.isEmpty())
				resultMap.put(Constants.MESSAGE, e.getMessage());
			return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED, resultMap));
		}
	}

	private void approveAccRequest(Account account, DevUser user, Request userRequest, String status,
			String reqStatus) {
		account.setStatus(status);
		accountService.updateAccount(account);
		user.setStatus(status);
		devUserService.saveUser(user);
		userRequest.setStatus(reqStatus);
		requestService.updateRequest(userRequest);
		if(userRequest!=null){
		requestActivityService.saveRequestActivity(userRequest,null);}
	}

	@GetMapping(value = APIConstants.GET_REQUEST_BY_ID, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getRequestById(@PathVariable Integer id) {
		Request request = requestService.findByRequestID(id);
		resultMap = new HashMap<String, String>();
		if (request != null) {
			RequestUserDto reqDto = processRequest(request);
			return ResponseEntity.ok(GeneralResponseModel.of(Constants.SUCCESS, reqDto));
		} else {
			resultMap.put(Constants.MESSAGE, ErrorMessageConstant.NO_REQUEST_FOUND + id);
			return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED, resultMap));
		}
	}

	@GetMapping(value = APIConstants.GET_APP_REQUESTS, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> listOfAllAppRequests(@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "5") int size,
			@RequestParam(required = false, defaultValue = "request_Id") String orderBy,
			@RequestParam(required = false, defaultValue = "PENDING") List<String> status,
			@RequestParam(required = false, defaultValue = "2,3") List<Integer> requestTypeId) {
//		resultMap = new HashMap<String, String>();
		try {
			if (page > 0) {
				page--;
			}
			Page<Request> appsRequestList = requestService.getListOfAllAppsRequest(page, size, orderBy, status,
					requestTypeId);
			List<RequestAppApplicationDto> requestAppDtoList = new ArrayList<RequestAppApplicationDto>();
			for (Request appRequest : appsRequestList) {
				RequestAppApplicationDto reqAppDto = new RequestAppApplicationDto();
				org.bitvault.appstore.cloud.dto.AppApplication appApplication = appApplicationService
						.findApplicationByAppId(appRequest.getApplicationId());
				BeanUtils.copyProperties(appApplication, reqAppDto);
				DevUser user = devUserService.findByUserId(appRequest.getUserId());
				BeanUtils.copyProperties(user, reqAppDto);
				BeanUtils.copyProperties(appRequest, reqAppDto);
				reqAppDto.setAppIconUrl(appApplication.getAppIconUrl());
				String requestFor = "";
				if (appRequest.getRequestType().getRequestType().equals(DbConstant.APP_UNPUBLISHED_REQUEST)) {
					requestFor = Constants.UNPUBLISHING;
				} else {
					requestFor = Constants.PUBLISHING;
				}
				reqAppDto.setRequestFor(requestFor);
				reqAppDto.setRequest_Id(appRequest.getRequest_Id());
				requestAppDtoList.add(reqAppDto);
			}
			return ResponseEntity.ok(new Response(Constants.SUCCESS, requestAppDtoList, appsRequestList.getTotalPages(),
					appsRequestList.getTotalElements(), appsRequestList.getSize(), orderBy));
			// return
			// ResponseEntity.ok(GeneralResponseModel.of(Constants.SUCCESS,
			// requestAppDtoList));
		} catch (BeansException e) {
			e.printStackTrace();
			//resultMap.put(Constants.MESSAGE, ErrorMessageConstant.SOME_ERROR_OCCURED);
			return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED, new BitVaultResponse(ErrorMessageConstant.SOME_ERROR_OCCURED)));
		} catch (BitVaultException e) {
			e.printStackTrace();
			//resultMap.put(Constants.MESSAGE,new BitVaultResponse(e));
			return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED, new BitVaultResponse(e)));
		}
	}

	@GetMapping(value = APIConstants.GET_APP_DETAIL_BY_REQUEST
			+ "/{requestId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> findAppDetailByRequestId(@PathVariable Integer requestId) {
		GeneralResponseModel response = null;
		Request req = null;
		AppDetail appDetailDTO = null;
		try {
			req = requestService.findByRequestID(requestId);
			if (null != req) {
				org.bitvault.appstore.cloud.dto.AppApplication appDto = appApplicationService
						.findApplicationByAppId(req.getApplicationId());
				if (null != appDto) {
					appDetailDTO = appDetailService.findApplicationDetailByAppIdAndVersion(appDto.getAppApplicationId(),
							appDto.getLatestVersionName(),"");

					if (null != appDetailDTO) {
						AppApplicationDetailDto appApplicationDetailDto = AppApplicationDetailDto
								.populateAppApplicationDetailDto(appDetailDTO, appDto);
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("appDetail", appApplicationDetailDto);
						map.put("reqId", requestId);
						map.put("status", req.getStatus());
						map.put("reason", req.getRejectionReason());
						map.put("requester", devUserService.findByUserId(appDto.getUserId()).getEmail());

						String requestFor = null;
						if (req.getRequestType().getRequestType().equals(DbConstant.APP_UNPUBLISHED_REQUEST)) {
							requestFor = Constants.UNPUBLISHING;
						} else {
							requestFor = Constants.PUBLISHING;
						}
						map.put("requestFor", requestFor);
						response = GeneralResponseModel.of(Constants.SUCCESS, map);
					} else {
						throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);
					}
				} else {
					throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);
				}

				return new ResponseEntity<GeneralResponseModel>(response, HttpStatus.OK);

			} else {
				throw new BitVaultException(ErrorMessageConstant.RESULT_NOT_FOUND);
			}
		} catch (BitVaultException e) {

			response = GeneralResponseModel.of(Constants.FAILED, new BitVaultResponse(e));
			return new ResponseEntity<GeneralResponseModel>(response, HttpStatus.OK);

		}
	}

	// Generate private public key for the developer and update Model
	private void savePublicPrivateKey(DevUser user) {

		try {
			
			Map<String, String> keyPair = GeneratePrivatePublicKeyPair.generatePrivatePublicKey();
			if (keyPair != null && keyPair.get(Constants.PRIVATE_KEY) != null
					&& keyPair.get(Constants.PUBLIC_KEY) != null) {
				user.setPrivateKey(keyPair.get(Constants.PRIVATE_KEY));
				user.setPublicKey(keyPair.get(Constants.PUBLIC_KEY));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
