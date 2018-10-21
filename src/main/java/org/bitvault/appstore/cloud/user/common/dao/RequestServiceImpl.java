package org.bitvault.appstore.cloud.user.common.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.bitvault.appstore.cloud.constant.Constants;
import org.bitvault.appstore.cloud.constant.DbConstant;
import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.dto.AppApplication;
import org.bitvault.appstore.cloud.dto.ApplicationReqActDto;
import org.bitvault.appstore.cloud.dto.RequestActivityDto;
import org.bitvault.appstore.cloud.dto.RequestDto;
import org.bitvault.appstore.cloud.dto.RequestTypeDto;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.bitvault.appstore.cloud.model.Request;
import org.bitvault.appstore.cloud.model.RequestActivity;
import org.bitvault.appstore.cloud.model.RequestType;
import org.bitvault.appstore.cloud.user.common.service.RequestActivityService;
import org.bitvault.appstore.cloud.user.common.service.RequestService;
import org.bitvault.appstore.commons.application.service.AppApplicationService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class RequestServiceImpl implements RequestService {

	static final Logger logger = org.slf4j.LoggerFactory.getLogger(RequestService.class);

	@Autowired
	RequestRepository requestRepository;

	@Autowired
	RequestTypeRepository requestTypeRepository;
	
	@Autowired
	AppApplicationService appService;

	@Autowired
	RequestActivityService requestActivity ;

	@Override
	public Page<Request> findAllUserRequests(int page, int size, String orderBy, List<String> status,
			String direction) {
		Sort sort = null;
		if (direction.equalsIgnoreCase(DbConstant.ASC))
			new Sort(Direction.ASC, orderBy);
		else
			new Sort(Direction.DESC, orderBy);
		Page<Request> userRequest = requestRepository.findAllUserRequests(status, new PageRequest(page, size, sort));
		return userRequest;
	}

	@Override
	public Request findByRequestID(Integer reqId) {
		return requestRepository.findOne(reqId);
	}

	@Override
	public Request persistRequest(Request request) {
		logger.info("request type is in progress to update...");
		request = requestRepository.saveAndFlush(request);
		RequestType requestType = requestTypeRepository.getOne(request.getRequestType().getRequestTypeId());
		requestType.setCount(requestType.getCount() + 1);
		requestTypeRepository.saveAndFlush(requestType);
		return request;
	}

	@Override
	public void updateRequest(Request request) {
		logger.info("request type is in progress to update...");
		requestRepository.saveAndFlush(request);
	}

	@Override
	public Request findRequestByAppId(Integer appId) throws BitVaultException {
		Request req = null;
		try {
			req = requestRepository.findRequestByAppId(appId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return req;
	}

	@Override
	public Request updateRequestStatus(Request req) throws BitVaultException {

		try {

			requestRepository.saveAndFlush(req);

		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.UNABLE_TO_UPDATE);
		}
		return req;
	}

	@Override
	public Page<Request> getListOfAllAppsRequest(int page, int size, String orderBy, List<String> status,
			List<Integer> requestTypeId) {
		Page<Request> pageRequest = null;
		try {
			pageRequest = requestRepository.getListOfAllAppsRequest(status, requestTypeId,
					new PageRequest(page, size, new Sort(Direction.DESC, orderBy)));

		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);
		}
		return pageRequest;
	}

	
	
	@Override
	public Page<Request> getRequestsForUse(String userId, List<String> status, List<Integer> requestType,
			Pageable page) {
		return requestRepository.findByUserIdAndStatusInAndRequestTypeIn(userId, status, requestType,page);
	}

	@Override
	public Map<String, Object> findAllRequestByUser(String userId, int page, int size, String direction,
			String property) {
		RequestDto requestDto = null;
		Page<org.bitvault.appstore.cloud.model.Request> requestList = null;
		Map<String, Object> allAppMap = null;
		// Map<AppApplication, List<RequestActivityDto>> appRequest = null;
		// Map<String, Object> requestActivtyDto = null;

		RequestActivityDto requestActivity ;
		
		AppApplication app = null;
		// List<RequestActivityDto> requestDTO = new
		// ArrayList<RequestActivityDto>();
		List<Object> requestDTOList = new ArrayList<Object>();

		try {
			if (direction.equals(DbConstant.ASC.toString())) {
				requestList = requestRepository.findRequestActivityByUserId(userId,
						new PageRequest(page, size, Sort.Direction.ASC, property));
			} else {
				requestList = requestRepository.findRequestActivityByUserId(userId,
						new PageRequest(page, size, Sort.Direction.DESC, property));

			}
			// appRequest = new HashMap<AppApplication,
			// List<RequestActivityDto>>();

			for (org.bitvault.appstore.cloud.model.Request request : requestList) {
				requestDto = request.populateRequest(request);
				
				requestActivity = new RequestActivityDto() ;
				
				requestActivity = requestActivity.populateRequest(requestDto, requestActivity);
				
				
				if (null != request.getApplicationId() && request.getApplicationId() != -1) {
					app = appService.findApplicationByAppId(request.getApplicationId());
					if (app != null) {
						ApplicationReqActDto applicationReqActDto = app.populateApplication(app);
						requestActivity.setApplication(applicationReqActDto);
					}
				} else {

					requestActivity.setApplication(null);
				}

				RequestType requestType = requestTypeRepository.findOne(request.getRequestType().getRequestTypeId());
				if (null != requestType) {
					RequestTypeDto requestTypeDto = requestType.populateRequestTypeDto(requestType);
					requestActivity.setRequestType(requestTypeDto);
				} else {
					requestActivity.setRequestType(null);

				}
				
				requestActivity.setUser_id(userId);
				
				requestDTOList.add(requestActivity);
				
				// if (appRequest.containsKey(app)) {
				// requestDTO = appRequest.get(app);
				// requestDTO.add(requestActivity);
				// appRequest.put(app, requestDTO);
				// } else {
				// requestDTO.add(requestActivity);
				// appRequest.put(app, requestDTO);
				// }
			}
			// for (Map.Entry<AppApplication, List<RequestActivityDto>> entry :
			// appRequest.entrySet()) {
			// requestActivtyDto = new HashMap<String, Object>();
			// requestActivtyDto.put("appDetail", entry.getKey());
			// requestActivtyDto.put("request", entry.getValue());
			// requestDTOList.add(requestActivtyDto);
			// }

			allAppMap = new HashMap<String, Object>();
			allAppMap.put("requestList", requestDTOList);
			allAppMap.put(Constants.TOTAL_PAGES, requestList.getTotalPages());
			allAppMap.put(Constants.TOTAL_RECORDS, requestList.getTotalElements());
			allAppMap.put(Constants.SIZE, requestList.getNumberOfElements());
			allAppMap.put(Constants.SORT, requestList.getSort());
		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);
		}

		return allAppMap;		
	}
	
}
