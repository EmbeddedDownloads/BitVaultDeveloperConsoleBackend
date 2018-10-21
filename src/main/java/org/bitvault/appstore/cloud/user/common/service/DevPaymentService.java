package org.bitvault.appstore.cloud.user.common.service;

import java.util.List;

import org.bitvault.appstore.cloud.model.DevPayment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DevPaymentService {
	
	Page<DevPayment> findByUserIdAndPaymentFor(String userId, String paymentFor, Pageable page);
	
	List<DevPayment> findByUserIdAndPaymentForSubDev(String userId, String paymentFor);
	
	Page<DevPayment> findByTxnId(String txnId, String userId, Pageable page);
	
	Page<DevPayment> findByUserId(String userId, Pageable page);

	void savePaymentRecord(DevPayment devPayment);
	
	
	
}
