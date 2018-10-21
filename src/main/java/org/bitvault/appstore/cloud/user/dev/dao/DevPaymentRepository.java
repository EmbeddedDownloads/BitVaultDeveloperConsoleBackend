package org.bitvault.appstore.cloud.user.dev.dao;

import java.util.List;

import org.bitvault.appstore.cloud.model.DevPayment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DevPaymentRepository extends JpaRepository<DevPayment, Integer>{
	
	Page<DevPayment> findByUserIdAndPaymentFor(String userId, String paymentFor, Pageable page);
	
	List<DevPayment> findByUserIdAndPaymentFor(String userId, String paymentFor);
	
	Page<DevPayment> findByTxnIdOrUserId(String txnId, String userId, Pageable page);
	
	Page<DevPayment> findByUserId(String userId, Pageable page);
	@Modifying
	@Query("delete from DevPayment dev where dev.userId=?1")
	void deleteByUserId(String userId);
}
