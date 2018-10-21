package org.bitvault.appstore.cloud.user.common.service;

import java.util.List;
import java.util.Map;

import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.bitvault.appstore.cloud.model.Request;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RequestService {
	Request findByRequestID(Integer req_id);

	Page<Request> findAllUserRequests(int page, int size, String orderBy, List<String> status, String direction);

	Request persistRequest(Request request);

	void updateRequest(Request request);

	Request findRequestByAppId(Integer appId) throws BitVaultException;

	Request updateRequestStatus(Request req) throws BitVaultException;

	Page<Request> getListOfAllAppsRequest(int page, int size, String orderBy, List<String> status, List<Integer> requestTypeId);
	
	Page<Request> getRequestsForUse(String userId, List<String> status, List<Integer> requestType,Pageable page);
	
	Map<String, Object> findAllRequestByUser(String userId,int page, int size, String direction,
			String property);
		

}