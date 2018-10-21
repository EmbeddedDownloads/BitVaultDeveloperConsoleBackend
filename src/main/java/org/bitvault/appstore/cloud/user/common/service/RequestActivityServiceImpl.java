package org.bitvault.appstore.cloud.user.common.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bitvault.appstore.cloud.constant.Constants;
import org.bitvault.appstore.cloud.constant.DbConstant;
import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.dto.AppApplication;
import org.bitvault.appstore.cloud.dto.ApplicationReqActDto;
import org.bitvault.appstore.cloud.dto.RequestActivityDto;
import org.bitvault.appstore.cloud.dto.RequestTypeDto;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.bitvault.appstore.cloud.model.Request;
import org.bitvault.appstore.cloud.model.RequestActivity;
import org.bitvault.appstore.cloud.model.RequestType;
import org.bitvault.appstore.cloud.model.SubDevReq;
import org.bitvault.appstore.cloud.user.common.dao.RequestActivityRepository;
import org.bitvault.appstore.cloud.user.common.dao.RequestRepository;
import org.bitvault.appstore.cloud.user.common.dao.RequestTypeRepository;
import org.bitvault.appstore.cloud.user.dev.controller.DevUserController;
import org.bitvault.appstore.commons.application.service.AppApplicationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service("RequestActivityService")
public class RequestActivityServiceImpl implements RequestActivityService {

	@Autowired
	RequestActivityRepository requestActivityRepository;

	@Autowired
	AppApplicationService appService;

	@Autowired
	RequestTypeRepository requestTypeRepository;
	private static final Logger logger = LoggerFactory.getLogger(RequestActivityServiceImpl.class);
	@Override
	public RequestActivityDto saveRequestActivity(Request request, SubDevReq subDevrequest) {
		RequestActivityDto requestActivity = null;
		RequestActivity reqActivityModel = null;
		try {
			if (request != null) {
				reqActivityModel = request.populateRequestActivity(request);
				logger.info(" Request object get saved in ReqActivity");
				reqActivityModel = requestActivityRepository.saveAndFlush(reqActivityModel);
				logger.info("Request object get saved successfully");
				requestActivity = reqActivityModel.populateRequestActivity(reqActivityModel);
			} else {
				reqActivityModel = subDevrequest.populateRequestActivity(subDevrequest);
				logger.info(" SubDevrequest object get saved in ReqActivity");
				reqActivityModel = requestActivityRepository.saveAndFlush(reqActivityModel);
				logger.info("SubDevrequest object get saved successfully");
				
				requestActivity = reqActivityModel.populateRequestActivity(reqActivityModel);
			}
		} catch (Exception e) {
			logger.info("Error occured "+e.getMessage());
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);
		}
		return requestActivity;

	}

	// @Override
	// public Map<String, Object> findReqActivityListByUserId(String userId, int
	// page, int size, String direction,
	// String property) {
	// RequestActivityDto requestActivity = null;
	// Page<org.bitvault.appstore.cloud.model.RequestActivity> requestList =
	// null;
	// Map<String, Object> allAppMap = null;
	// Map<AppApplication, List<RequestActivityDto>> appRequest = null;
	// Map<String, Object> requestActivtyDto = null;
	//
	// AppApplication app = null;
	// List<RequestActivityDto> requestDTO = new
	// ArrayList<RequestActivityDto>();
	// List<Object> requestDTOList = new ArrayList<Object>();
	//
	// try {
	// if (direction.equals(DbConstant.ASC.toString())) {
	// requestList =
	// requestActivityRepository.findRequestActivityByUserId(userId,
	// new PageRequest(page, size, Sort.Direction.ASC, property));
	// } else {
	// requestList =
	// requestActivityRepository.findRequestActivityByUserId(userId,
	// new PageRequest(page, size, Sort.Direction.DESC, property));
	//
	// }
	// appRequest = new HashMap<AppApplication, List<RequestActivityDto>>();
	//
	// for (org.bitvault.appstore.cloud.model.RequestActivity request :
	// requestList) {
	// requestActivity = request.populateRequestActivity(request);
	// app = appService.findApplicationByAppId(request.getApplicationId());
	// RequestType requestType =
	// requestTypeRepository.findOne(request.getRequestTypeId());
	// if(null!=requestType){
	// RequestTypeDto requestTypeDto =
	// requestType.populateRequestTypeDto(requestType);
	// requestActivity.setRequestType(requestTypeDto);
	// }else{
	// requestActivity.setRequestType(null);
	//
	// }
	// if (appRequest.containsKey(app)) {
	// requestDTO = appRequest.get(app);
	// requestDTO.add(requestActivity);
	// appRequest.put(app, requestDTO);
	// } else {
	// requestDTO.add(requestActivity);
	// appRequest.put(app, requestDTO);
	// }
	// }
	// for (Map.Entry<AppApplication, List<RequestActivityDto>> entry :
	// appRequest.entrySet()) {
	// requestActivtyDto = new HashMap<String, Object>();
	// requestActivtyDto.put("appDetail", entry.getKey());
	// requestActivtyDto.put("request", entry.getValue());
	// requestDTOList.add(requestActivtyDto);
	// }
	//
	// allAppMap = new HashMap<String, Object>();
	// allAppMap.put("requestList", requestDTOList);
	// allAppMap.put(Constants.TOTAL_PAGES, requestList.getTotalPages());
	// allAppMap.put(Constants.TOTAL_RECORDS, requestList.getTotalElements());
	// allAppMap.put(Constants.SIZE, requestList.getNumberOfElements());
	// allAppMap.put(Constants.SORT, requestList.getSort());
	// } catch (Exception e) {
	// e.printStackTrace();
	// throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);
	// }
	//
	// return allAppMap;
	//
	// }

	@Override
	public Map<String, Object> findReqActivityListByUserIdAndApplicationId(Integer applicationId, String userId,
			int page, int size, String direction, String property) {
		RequestActivityDto requestActivity = null;
		Page<org.bitvault.appstore.cloud.model.RequestActivity> requestList = null;
		Map<String, Object> allAppMap = null;

		Map<String, Object> requestActivtyMap = null;

		AppApplication app = null;
		List<Object> requestDTOList = new ArrayList<Object>();

		try {
			logger.info("findApplicationByAppId method calling");
			app = appService.findApplicationByAppId(applicationId);
            logger.info("AppApplication object fetched successfully");
			if (null == app) {
				throw new BitVaultException(ErrorMessageConstant.NO_REQUEST_FOUND);
			}
               logger.info("findRequestActivityByUserIdAndApplicationId  method calling");
			if (direction.equals(DbConstant.ASC.toString())) {
				requestList = requestActivityRepository.findRequestActivityByUserIdAndApplicationId(applicationId,
						userId, new PageRequest(page, size, Sort.Direction.ASC, property));
			} else {
				requestList = requestActivityRepository.findRequestActivityByUserIdAndApplicationId(applicationId,
						userId, new PageRequest(page, size, Sort.Direction.DESC, property));

			}
               logger.info("RequestList fetched successfully");
			requestActivtyMap = new HashMap<String, Object>();

			for (org.bitvault.appstore.cloud.model.RequestActivity request : requestList) {
				requestActivity = request.populateRequestActivity(request);
				RequestType requestType = requestTypeRepository.findOne(request.getRequestTypeId());
				RequestTypeDto requestTypeDto = requestType.populateRequestTypeDto(requestType);
				requestActivity.setRequestType(requestTypeDto);
				requestDTOList.add(requestActivity);
			}
			requestActivtyMap.put("appDetail", app);
			requestActivtyMap.put("request", requestDTOList);
			allAppMap = new HashMap<String, Object>();
			allAppMap.put("requestList", requestActivtyMap);
			allAppMap.put(Constants.TOTAL_PAGES, requestList.getTotalPages());
			allAppMap.put(Constants.TOTAL_RECORDS, requestList.getTotalElements());
			allAppMap.put(Constants.SIZE, requestList.getNumberOfElements());
			allAppMap.put(Constants.SORT, requestList.getSort());
		} catch (Exception e) {
			logger.info("Error occured"+e.getMessage());
			if (e instanceof BitVaultException) {
				throw new BitVaultException(e.getMessage());

			}
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);
		}

		return allAppMap;

	}

	@Override
	public Map<String, Object> findRequest(Integer applicationId, String userId, int page, int size, String direction,
			String property) {
		Map<String, Object> allAppMap = null;

		try {
			if (applicationId == -1) {
				
				allAppMap = findAllRequestByUser(userId, page, size, direction, property);
			} else {
				allAppMap = findReqActivityListByUserIdAndApplicationId(applicationId, userId, page, size, direction,
						property);
			}

		} catch (Exception e) {
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);
		}

		return allAppMap;
	}

	@Override
	public Page<RequestActivity> getRequestsForUser(String userId, List<String> status, List<Integer> requestType,
			Integer appApplicationId, Pageable page) {
		return requestActivityRepository.findByUserIdAndStatusInAndRequestTypeIn(userId, status, requestType,
				appApplicationId, page);
	}

	@Override
	public Map<String, Object> findAllRequestByUser(String userId, int page, int size, String direction,
			String property) {
		RequestActivityDto requestActivity = null;
		Page<org.bitvault.appstore.cloud.model.RequestActivity> requestList = null;
		Map<String, Object> allAppMap = null;
		// Map<AppApplication, List<RequestActivityDto>> appRequest = null;
		// Map<String, Object> requestActivtyDto = null;

		AppApplication app = null;
		// List<RequestActivityDto> requestDTO = new
		// ArrayList<RequestActivityDto>();
		List<Object> requestDTOList = new ArrayList<Object>();

		try {
			if (direction.equals(DbConstant.ASC.toString())) {
				requestList = requestActivityRepository.findRequestActivityByUserId(userId,
						new PageRequest(page, size, Sort.Direction.ASC, property));
			} else {
				requestList = requestActivityRepository.findRequestActivityByUserId(userId,
						new PageRequest(page, size, Sort.Direction.DESC, property));

			}
			// appRequest = new HashMap<AppApplication,
			// List<RequestActivityDto>>();

			for (org.bitvault.appstore.cloud.model.RequestActivity request : requestList) {
				requestActivity = request.populateRequestActivity(request);
				if (null != request.getApplicationId() && request.getApplicationId() != -1) {
					app = appService.findApplicationByAppId(request.getApplicationId());
					if (app != null) {
						ApplicationReqActDto applicationReqActDto = app.populateApplication(app);
						requestActivity.setApplication(applicationReqActDto);
					}
				} else {

					requestActivity.setApplication(null);
				}

				RequestType requestType = requestTypeRepository.findOne(request.getRequestTypeId());
				if (null != requestType) {
					RequestTypeDto requestTypeDto = requestType.populateRequestTypeDto(requestType);
					requestActivity.setRequestType(requestTypeDto);
				} else {
					requestActivity.setRequestType(null);

				}
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

	@Override
	public Map<String, Object> findAllRequestByRequestId(Integer requestId, int page, int size, String direction,
			String property) {
		RequestActivityDto requestActivity = null;
		Page<org.bitvault.appstore.cloud.model.RequestActivity> requestList = null;
		Map<String, Object> allAppMap = null;
		// Map<AppApplication, List<RequestActivityDto>> appRequest = null;
		// Map<String, Object> requestActivtyDto = null;

		AppApplication app = null;
		// List<RequestActivityDto> requestDTO = new
		// ArrayList<RequestActivityDto>();
		List<Object> requestDTOList = new ArrayList<Object>();

		try {
			if (direction.equals(DbConstant.ASC.toString())) {
				requestList = requestActivityRepository.findRequestActivityByRequestId(requestId,
						new PageRequest(page, size, Sort.Direction.ASC, property));
			} else {
				requestList = requestActivityRepository.findRequestActivityByRequestId(requestId,
						new PageRequest(page, size, Sort.Direction.DESC, property));

			}
			// appRequest = new HashMap<AppApplication,
			// List<RequestActivityDto>>();

			for (org.bitvault.appstore.cloud.model.RequestActivity request : requestList) {
				requestActivity = request.populateRequestActivity(request);
				if (null != request.getApplicationId() && request.getApplicationId() != -1) {
					app = appService.findApplicationByAppId(request.getApplicationId());
					if (app != null) {
						ApplicationReqActDto applicationReqActDto = app.populateApplication(app);
						requestActivity.setApplication(applicationReqActDto);
					}
				} else {

					requestActivity.setApplication(null);
				}

				RequestType requestType = requestTypeRepository.findOne(request.getRequestTypeId());
				if (null != requestType) {
					RequestTypeDto requestTypeDto = requestType.populateRequestTypeDto(requestType);
					requestActivity.setRequestType(requestTypeDto);
				} else {
					requestActivity.setRequestType(null);

				}
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

	@Override
	public Map<String, Object> findAllAlertRequestByUser(String userId, int page, int size, String direction,
			String property) {
		RequestActivityDto requestActivity = null;
		Page<org.bitvault.appstore.cloud.model.RequestActivity> requestList = null;
		Map<String, Object> allAppMap = null;
		// Map<AppApplication, List<RequestActivityDto>> appRequest = null;
		// Map<String, Object> requestActivtyDto = null;

		AppApplication app = null;
		// List<RequestActivityDto> requestDTO = new
		// ArrayList<RequestActivityDto>();
		List<Object> requestDTOList = new ArrayList<Object>();

		try {
			if (direction.equals(DbConstant.ASC.toString())) {
				requestList = requestActivityRepository.findAlertRequestActivityByUserId(userId, Constants.UNSEEN,
						new PageRequest(page, size, Sort.Direction.ASC, property));
			} else {
				requestList = requestActivityRepository.findAlertRequestActivityByUserId(userId, Constants.UNSEEN,
						new PageRequest(page, size, Sort.Direction.DESC, property));

			}
			// appRequest = new HashMap<AppApplication,
			// List<RequestActivityDto>>();

			for (org.bitvault.appstore.cloud.model.RequestActivity request : requestList) {

				RequestType requestType = requestTypeRepository.findOne(request.getRequestTypeId());

				if (requestType.getAlertActivated().equalsIgnoreCase(Constants.TRUE)) {

					requestActivity = request.populateRequestActivity(request);
					if (null != request.getApplicationId() && request.getApplicationId() != -1) {
						app = appService.findApplicationByAppId(request.getApplicationId());
						if (app != null) {
							ApplicationReqActDto applicationReqActDto = app.populateApplication(app);
							requestActivity.setApplication(applicationReqActDto);
						}
					} else {

						requestActivity.setApplication(null);
					}

//					RequestType requestType = requestTypeRepository.findOne(request.getRequestTypeId());
					if (null != requestType) {
						RequestTypeDto requestTypeDto = requestType.populateRequestTypeDto(requestType);
						requestActivity.setRequestType(requestTypeDto);
					} else {
						requestActivity.setRequestType(null);

					}
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
	
	
	
	@Override
	public Map<String, Object> findAllAlertRequestByStatus(int page, int size, String direction,
			String property) {
		RequestActivityDto requestActivity = null;
		Page<org.bitvault.appstore.cloud.model.RequestActivity> requestList = null;
		Map<String, Object> allAppMap = null;
		// Map<AppApplication, List<RequestActivityDto>> appRequest = null;
		// Map<String, Object> requestActivtyDto = null;

		AppApplication app = null;
		// List<RequestActivityDto> requestDTO = new
		// ArrayList<RequestActivityDto>();
		
		List<String> statusList = new ArrayList<>();
		statusList.add(DbConstant.PENDING);
		List<Object> requestDTOList = new ArrayList<Object>();

		try {
			if (direction.equals(DbConstant.ASC.toString())) {
				requestList = requestActivityRepository.findByUserIdAndStatusIn(statusList, Constants.UNSEEN,
						new PageRequest(page, size, Sort.Direction.ASC, property));
			} else {
				requestList = requestActivityRepository.findByUserIdAndStatusIn(statusList, Constants.UNSEEN,
						new PageRequest(page, size, Sort.Direction.DESC, property));

			}
			// appRequest = new HashMap<AppApplication,
			// List<RequestActivityDto>>();

			for (org.bitvault.appstore.cloud.model.RequestActivity request : requestList) {

				RequestType requestType = requestTypeRepository.findOne(request.getRequestTypeId());

				if (requestType.getAlertActivated().equalsIgnoreCase(Constants.TRUE)) {

					requestActivity = request.populateRequestActivity(request);
					if (null != request.getApplicationId() && request.getApplicationId() != -1) {
						app = appService.findApplicationByAppId(request.getApplicationId());
						if (app != null) {
							ApplicationReqActDto applicationReqActDto = app.populateApplication(app);
							requestActivity.setApplication(applicationReqActDto);
						}
					} else {

						requestActivity.setApplication(null);
					}

//					RequestType requestType = requestTypeRepository.findOne(request.getRequestTypeId());
					if (null != requestType) {
						RequestTypeDto requestTypeDto = requestType.populateRequestTypeDto(requestType);
						requestActivity.setRequestType(requestTypeDto);
					} else {
						requestActivity.setRequestType(null);

					}
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

	@Override
	public List<RequestActivity> markAlertsAsSeen(List<Integer> requestIds) {
		
		try {
			
			return requestActivityRepository.findByActivityIdIn(requestIds);
			
		}catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);
		}
		
	}

}
