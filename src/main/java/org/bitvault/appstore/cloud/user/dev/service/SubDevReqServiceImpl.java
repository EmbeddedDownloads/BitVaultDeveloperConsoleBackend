package org.bitvault.appstore.cloud.user.dev.service;

import java.util.List;
import java.util.Map;

import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.bitvault.appstore.cloud.model.DevPayment;
import org.bitvault.appstore.cloud.model.SubDevReq;
import org.bitvault.appstore.cloud.user.dev.dao.SubDevReqRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class SubDevReqServiceImpl implements SubDevReqService {

	@Autowired
	SubDevReqRepository subDevReqRepository;

	@Override
	public void saveSubDevRequest(SubDevReq subDev) {
		subDevReqRepository.saveAndFlush(subDev);
	}

	@Override
	public Page<SubDevReq> allSubDevRequestsForUser(String userId, List<String> status, Pageable page) {
		return subDevReqRepository.findByUserIdAndStatusIn(userId, status, page);
	}

	@Override
	public Page<SubDevReq> allSubDevRequests(List<String> status, Pageable page) {
		return subDevReqRepository.findByStatusIn(status, page);
	}

	@Override
	public SubDevReq findById(Integer id) {
		return subDevReqRepository.findOne(id);
	}

	@Override
	public Integer getNumberOfChildForATransaction(Integer devpaymentId) {

		int childCount = 0;

		try {
			childCount = subDevReqRepository.getNumberOfChildByDevpaymentId(devpaymentId);
		} catch (Exception e) {
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);
		}
		return childCount;
	}

	@Override
	public SubDevReq findByDevPayment(DevPayment devPayment) {
		return subDevReqRepository.findByDevPaymentId(devPayment);
	}

	@Override
	public Page<SubDevReq> allSubDevRequests(String userId, Pageable page) {

		return subDevReqRepository.findByUserId(userId, page);

	}
}
