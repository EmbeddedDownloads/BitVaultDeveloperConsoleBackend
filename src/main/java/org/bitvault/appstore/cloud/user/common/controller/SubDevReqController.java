package org.bitvault.appstore.cloud.user.common.controller;

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
import org.bitvault.appstore.cloud.dto.RequestAppApplicationDto;
import org.bitvault.appstore.cloud.dto.RequestUserDto;
import org.bitvault.appstore.cloud.dto.SubDevReqDto;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.bitvault.appstore.cloud.exception.BitVaultResponse;
import org.bitvault.appstore.cloud.model.DevPayment;
import org.bitvault.appstore.cloud.model.DevUser;
import org.bitvault.appstore.cloud.model.Request;
import org.bitvault.appstore.cloud.model.RequestActivity;
import org.bitvault.appstore.cloud.model.SubDevReq;
import org.bitvault.appstore.cloud.user.common.GeneralResponseModel;
import org.bitvault.appstore.cloud.user.common.service.RequestActivityService;
import org.bitvault.appstore.cloud.user.common.service.RequestService;
import org.bitvault.appstore.cloud.user.dev.service.DevUserService;
import org.bitvault.appstore.cloud.user.dev.service.SubDevReqService;
import org.bitvault.appstore.cloud.utils.Response;
import org.bitvault.appstore.commons.application.service.AppApplicationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = APIConstants.COMMON_API_BASE)
@RestController
public class SubDevReqController {

	@Autowired
	private SubDevReqService devReqService;

	@Autowired
	private DevUserService devUserService;

	@Autowired
	private RequestService requestService;
	
	@Autowired
	private RequestActivityService requestActivityService;

	@Autowired
	private AppApplicationService appApplicationService;

	private final Logger logger = LoggerFactory.getLogger(SubDevReqController.class);

	@GetMapping(value = APIConstants.SUB_DEV_REQUEST + "/{reqId}")
	public ResponseEntity<?> getSubDevRequest(@PathVariable Integer reqId, HttpServletRequest request) {
		SubDevReq subDevReq = devReqService.findById(reqId);
		if (subDevReq == null) {
			return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED,
					new BitVaultResponse(ErrorMessageConstant.RESULT_NOT_FOUND)));
		} else if (!((String) request.getAttribute("role")).contains("ADMIN")
				&& !subDevReq.getUserId().equalsIgnoreCase((String) request.getAttribute(Constants.USERID))) {
			return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED,
					new BitVaultResponse(ErrorMessageConstant.INVALID_REQUEST)));
		}
		return ResponseEntity.ok(GeneralResponseModel.of(Constants.SUCCESS, generateSubDevReqDto(subDevReq)));
	}

	private SubDevReqDto generateSubDevReqDto(SubDevReq subDevReq) {
		SubDevReqDto subDevReqDto = new SubDevReqDto();
		DevPayment devPayment = subDevReq.getDevPaymentId();
		if (devPayment != null) {
			subDevReqDto.setTxnStatus(devPayment.getTxnStatus());
			subDevReqDto.setPayment(devPayment.getPayment());
		}
		DevUser devUser = devUserService.findByUserId(subDevReq.getUserId());
		BeanUtils.copyProperties(devUser, subDevReqDto);
		BeanUtils.copyProperties(devUser.getAccount(), subDevReqDto);
		BeanUtils.copyProperties(subDevReq, subDevReqDto);
		return subDevReqDto;
	}

	@GetMapping(value = APIConstants.SUB_DEV_REQUEST)
	public ResponseEntity<?> getSubDevRequests(@RequestParam(defaultValue = DbConstant.PENDING) List<String> status,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "DESC") String direction,
			@RequestParam(defaultValue = Constants.CREATED_AT) String orderBy, HttpServletRequest request) {
		if (page > 0) {
			page--;
		}
		Sort sort = null;
		if (direction.equalsIgnoreCase(DbConstant.ASC))
			sort = new Sort(Direction.ASC, orderBy);
		else
			sort = new Sort(Direction.DESC, orderBy);
		Page<SubDevReq> subDevRequestPage = null;
		if (!((String) request.getAttribute("role")).contains("ADMIN")) {
			subDevRequestPage = devReqService.allSubDevRequestsForUser((String) request.getAttribute(Constants.USERID),
					status, new PageRequest(page, size, sort));
		} else
			subDevRequestPage = devReqService.allSubDevRequests(status, new PageRequest(page, size, sort));

		return ResponseEntity.ok(
				new Response(Constants.SUCCESS, populateSubDevReqDto(subDevRequestPage.getContent()), subDevRequestPage.getTotalPages(), subDevRequestPage.getTotalElements(),
						size, orderBy));
	}

	private List<SubDevReqDto> populateSubDevReqDto(List<SubDevReq> subDevReqList) {
		List<SubDevReqDto> subDevReqDtoList = new ArrayList<SubDevReqDto>();
		for (SubDevReq subDevReq : subDevReqList) {
			subDevReqDtoList.add(generateSubDevReqDto(subDevReq));
		}
		return subDevReqDtoList;
	}

	@GetMapping(value = APIConstants.GET_USER_REQUESTS)
	public ResponseEntity<?> getUserAppRequestList(
			@RequestParam(defaultValue = Constants.ALL_APP_STATUS) List<String> status,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "1") List<Integer> requestType,
			@RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "DESC") String direction,
			@RequestParam(defaultValue = Constants.CREATED_AT) String orderBy, HttpServletRequest request,
			@PathVariable(required = false) String userId) {
		try {
			if (page > 0) {
				page--;
			}
			Sort sort = null;
			if (direction.equalsIgnoreCase(DbConstant.ASC))
				sort = new Sort(Direction.ASC, orderBy);
			else
				sort = new Sort(Direction.DESC, orderBy);
			List<Request> requestList = null;
			List<SubDevReq> subDevReqList = null;
			Page<SubDevReq> subDevReqPage = null;
			Page<Request> accRequestPage = null;
			if (!((String) request.getAttribute("role")).contains("ADMIN")) {
				userId = (String) request.getAttribute(Constants.USERID);
				subDevReqPage = devReqService.allSubDevRequestsForUser(userId, status, new PageRequest(page, size, sort));
				subDevReqList = subDevReqPage.getContent();
				accRequestPage = requestService.getRequestsForUse(userId, status, requestType,
						new PageRequest(0, size, sort));
				requestList = accRequestPage.getContent();
			} else {
				subDevReqPage = devReqService.allSubDevRequestsForUser(userId, status, new PageRequest(page, size, sort));
				subDevReqList = subDevReqPage.getContent();
				accRequestPage = requestService.getRequestsForUse(userId, status, requestType,
						new PageRequest(0, size, sort));
				requestList = accRequestPage.getContent();
			}
			 Map<String, Object> map = new HashMap<>();
			 if(requestList != null && requestList.size() > 0) {
				 map.put("primary", processAccRequest(requestList.get(0)));
			 }
			 map.put("secondary", populateSubDevReqDto(subDevReqList));
			 map.put("size", subDevReqPage.getSize());
			 map.put("sort", orderBy);
			 map.put("totalPages", subDevReqPage.getTotalPages());
			 map.put("totalRecords", subDevReqPage.getTotalElements());
			 return ResponseEntity.ok(GeneralResponseModel.of(Constants.SUCCESS,map));
		} catch (Exception e) {
			logger.error(e.getMessage());
			return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED,ErrorMessageConstant.SOME_ERROR_OCCURED));
		}
//		return ResponseEntity.ok(new Response(Constants.SUCCESS, subDevReqPage.getTotalPages(),
//				subDevReqPage.getTotalElements(), subDevReqPage.getSize(), orderBy,
//				processAccRequest(requestList.get(0)), populateSubDevReqDto(subDevReqList)));
	}

	private RequestUserDto processAccRequest(Request req) {
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
			requestDto.setCreatedAt(req.getCreatedAt());
			requestDto.setStatus(req.getStatus());
			return requestDto;
		} catch (BeansException e) {
			logger.error("Exception occured during to fetch the pending request details: " + e.getMessage());
			return null;
		}
	}

	@GetMapping(value = APIConstants.GET_USER_APP_REQUESTS)
	public ResponseEntity<?> getUserAccRequestList(
			@RequestParam(defaultValue = Constants.ALL_APP_STATUS) List<String> status,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "2,3") List<Integer> requestType,
			@RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "DESC") String direction,
			@RequestParam(defaultValue = Constants.CREATED_AT) String orderBy, HttpServletRequest request,
			@PathVariable(required = false) String userId,
			@PathVariable(required = false) Integer appApplicationId) {
		try {
			if (page > 0) {
				page--;
			}
			Sort sort = null;
			if (direction.equalsIgnoreCase(DbConstant.ASC))
				sort = new Sort(Direction.ASC, orderBy);
			else
				sort = new Sort(Direction.DESC, orderBy);
			List<RequestActivity> requestList = null;
			Page<RequestActivity> appsRequestPage = requestActivityService.getRequestsForUser(userId, status, requestType,appApplicationId,
					new PageRequest(page, size, sort));
			if (!((String) request.getAttribute("role")).contains("ADMIN")) {
				userId = (String) request.getAttribute(Constants.USERID);
				requestList = requestActivityService
						.getRequestsForUser(userId, status, requestType, appApplicationId,new PageRequest(page, size, sort))
						.getContent();
			} else {
				requestList = requestActivityService
						.getRequestsForUser(userId, status, requestType, appApplicationId,new PageRequest(page, size, sort))
						.getContent();
			}
			List<RequestAppApplicationDto> requestDto = new ArrayList<>();
			for (RequestActivity req : requestList) {
				RequestAppApplicationDto dto = new RequestAppApplicationDto();
				org.bitvault.appstore.cloud.dto.AppApplication appApplication = appApplicationService
						.findApplicationByAppId(req.getApplicationId());
				BeanUtils.copyProperties(req, dto);
				BeanUtils.copyProperties(appApplication, dto);
				if(req.getRequestTypeId() == 2)
					dto.setRequestFor("Publishing");
				else
					dto.setRequestFor("Unpublishing");
				dto.setStatus(req.getStatus());
				dto.setCreatedAt(req.getCreatedAt());
				dto.setRequest_Id(req.getRequestId());
				requestDto.add(dto);
			}
			return ResponseEntity.ok(new Response(Constants.SUCCESS, requestDto, appsRequestPage.getTotalPages(),
					appsRequestPage.getTotalElements(), appsRequestPage.getSize(), orderBy));
		} catch (BeansException e) {
			logger.error(e.getMessage());
			return ResponseEntity
					.ok(GeneralResponseModel.of(Constants.FAILED, ErrorMessageConstant.SOME_ERROR_OCCURED));
		} catch (BitVaultException e) {
			logger.error(e.getMessage());
			return ResponseEntity
					.ok(GeneralResponseModel.of(Constants.FAILED, ErrorMessageConstant.SOME_ERROR_OCCURED));
		}
	}
	
	@GetMapping(value = APIConstants.GET_ALL_SUBDEV_REQUEST)
	public ResponseEntity<?> getSubDevRequests(
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "DESC") String direction,
			@RequestParam(defaultValue = Constants.CREATED_AT) String orderBy, HttpServletRequest request) {
		if (page > 0) {
			page--;
		}
		Sort sort = null;
		if (direction.equalsIgnoreCase(DbConstant.ASC))
			sort = new Sort(Direction.ASC, orderBy);
		else
			sort = new Sort(Direction.DESC, orderBy);
		Page<SubDevReq> subDevRequestPage = null;
	 
			subDevRequestPage = devReqService.allSubDevRequests((String) request.getAttribute(Constants.USERID),
				 new PageRequest(page, size, sort));
		

		return ResponseEntity.ok(
				new Response(Constants.SUCCESS, populateSubDevReqDto(subDevRequestPage.getContent()), subDevRequestPage.getTotalPages(), subDevRequestPage.getTotalElements(),
						size, orderBy));
	}
}
