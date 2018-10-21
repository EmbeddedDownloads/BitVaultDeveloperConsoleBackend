package org.bitvault.appstore.mobile.dao;
import org.bitvault.appstore.cloud.model.PaymentDetailsModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentDetailsRepository extends JpaRepository<PaymentDetailsModel, Integer> {



}
