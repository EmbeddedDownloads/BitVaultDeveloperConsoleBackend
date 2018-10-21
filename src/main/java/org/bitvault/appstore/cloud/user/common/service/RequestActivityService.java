package org.bitvault.appstore.cloud.user.common.service;

import java.util.List;
import java.util.Map;

import org.bitvault.appstore.cloud.dto.RequestActivityDto;
import org.bitvault.appstore.cloud.model.Request;
import org.bitvault.appstore.cloud.model.RequestActivity;
import org.bitvault.appstore.cloud.model.SubDevReq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface RequestActivityService 
{

RequestActivityDto	saveRequestActivity(Request request,SubDevReq subDevReqrequest);

//Map<String, Object> findReqActivityListByUserId(String userId,int page, int size, String direction,
	//	String property);

Map<String, Object> findReqActivityListByUserIdAndApplicationId(Integer applicationId,String userId,int page, int size, String direction,
		String property);
	
Map<String, Object> findAllRequestByUser(String userId,int page, int size, String direction,
		String property);

Map<String, Object> findAllAlertRequestByUser(String userId,int page, int size, String direction,
		String property);

Map<String, Object> findAllAlertRequestByStatus(int page, int size, String direction,
		String property);



Map<String, Object> findAllRequestByRequestId(Integer requestId,int page, int size, String direction,
		String property);


Page<RequestActivity> getRequestsForUser(String userId, List<String> status, List<Integer> requestType,Integer appApplicationId,Pageable page);


Map<String, Object> findRequest(Integer applicationId,String userId,int page, int size, String direction,
		String property);

List<RequestActivity> markAlertsAsSeen(List<Integer> requestIds);
	

}
