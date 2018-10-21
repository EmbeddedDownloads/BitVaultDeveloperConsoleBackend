package org.bitvault.appstore.cloud.dto;

import java.util.Date;

import org.bitvault.appstore.cloud.model.DevPayment;
import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class DevPaymentDto {

	private Integer devPaymentId;

	private double payment;

	private String txnId;
	
	private String paymentMode ;
	
	private String paymentFor ;

	private String txnStatus;

	private String userId;
	
	protected Date createdAt;

	protected Date updatedAt;
	
	private Integer childCount ;
	
	@JsonIgnore
	private String updated_by;
	
	@JsonIgnore
	private String created_by;
	
	public String getUpdated_by() {
		return updated_by;
	}

	public void setUpdated_by(String updated_by) {
		this.updated_by = updated_by;
	}

	public String getCreated_by() {
		return created_by;
	}

	public void setCreated_by(String created_by) {
		this.created_by = created_by;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Integer getDevPaymentId() {
		return devPaymentId;
	}

	public void setDevPaymentId(Integer devPaymentId) {
		this.devPaymentId = devPaymentId;
	}


	public String getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}

	public String getPaymentFor() {
		return paymentFor;
	}

	public void setPaymentFor(String paymentFor) {
		this.paymentFor = paymentFor;
	}

	public double getPayment() {
		return payment;
	}

	public void setPayment(double payment) {
		this.payment = payment;
	}


	public String getTxnId() {
		return txnId;
	}

	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}

	public String getTxnStatus() {
		return txnStatus;
	}

	public void setTxnStatus(String txnStatus) {
		this.txnStatus = txnStatus;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	
	
	
public Integer getChildCount() {
		return childCount;
	}

	public void setChildCount(Integer childCount) {
		this.childCount = childCount;
	}

public DevPayment populateModel(DevPaymentDto devPaymentDto) {
		
		DevPayment devPaymentModel = new DevPayment() ;
		try {
		BeanUtils.copyProperties(devPaymentDto, devPaymentModel);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return devPaymentModel ;
		
		
	}
	
	
}
