package org.bitvault.appstore.cloud.user.dev.service;

import java.util.List;
import java.util.Map;

import org.bitvault.appstore.cloud.model.DevPayment;
import org.bitvault.appstore.cloud.model.SubDevReq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SubDevReqService {
	public void saveSubDevRequest(SubDevReq subDev);
	public Page<SubDevReq> allSubDevRequestsForUser(String userId, List<String> status, Pageable page);
	public Page<SubDevReq> allSubDevRequests(List<String> status, Pageable page);	
	
	public Integer getNumberOfChildForATransaction(Integer devpaymentId);
	
	SubDevReq findById(Integer id);
	
	SubDevReq findByDevPayment(DevPayment devPayment);
	public Page<SubDevReq> allSubDevRequests(String userId, Pageable page);
}
