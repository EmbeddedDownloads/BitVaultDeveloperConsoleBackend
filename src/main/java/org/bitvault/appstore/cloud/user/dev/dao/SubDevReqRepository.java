package org.bitvault.appstore.cloud.user.dev.dao;

import java.util.List;

import org.bitvault.appstore.cloud.model.DevPayment;
import org.bitvault.appstore.cloud.model.SubDevReq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SubDevReqRepository extends JpaRepository<SubDevReq, Integer>{

	Page<SubDevReq> findByUserIdAndStatusIn(String userId, List<String> status, Pageable page);

	Page<SubDevReq> findByStatusIn(List<String> status, Pageable page);
	
	
	@Query("select childCount from SubDevReq subDevReq where subDevReq.devPaymentId.devPaymentId = ?1")
	public Integer getNumberOfChildByDevpaymentId(Integer devPaymantId);
	
//	@Query("from SubDevReq subDevReq where subDevReq.devPaymentId.devPaymentId = ?1")
	SubDevReq findByDevPaymentId(DevPayment devPayment);
	Page<SubDevReq> findByUserId(String userId, Pageable page);
}
