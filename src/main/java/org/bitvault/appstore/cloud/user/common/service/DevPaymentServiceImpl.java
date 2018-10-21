package org.bitvault.appstore.cloud.user.common.service;

import java.util.List;

import javax.transaction.Transactional;

import org.bitvault.appstore.cloud.model.DevPayment;
import org.bitvault.appstore.cloud.user.dev.dao.DevPaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class DevPaymentServiceImpl implements DevPaymentService{
	
	@Autowired
	private DevPaymentRepository devPaymentRepository;

	@Override
	public Page<DevPayment> findByUserIdAndPaymentFor(String userId, String paymentFor, Pageable page) {
		return devPaymentRepository.findByUserIdAndPaymentFor(userId, paymentFor, page);
	}
	
	@Override
	public List<DevPayment> findByUserIdAndPaymentForSubDev(String userId, String paymentFor) {
		return devPaymentRepository.findByUserIdAndPaymentFor(userId, paymentFor);
	}

	@Override
	public void savePaymentRecord(DevPayment devPayment) {
		devPaymentRepository.saveAndFlush(devPayment);
	}

	@Override
	public Page<DevPayment> findByTxnId(String txnId, String userId, Pageable page) {
		return devPaymentRepository.findByTxnIdOrUserId(txnId, userId, page);
	}
	
	@Override
	public Page<DevPayment> findByUserId(String userId, Pageable page) {
		return devPaymentRepository.findByUserId(userId, page);
	}
 
}
