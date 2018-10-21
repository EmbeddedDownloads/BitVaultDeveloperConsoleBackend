package org.bitvault.appstore.cloud.dto;

import java.io.Serializable;
import java.util.Date;

public class SubDevReqDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8268929791277571136L;
	
	private String userId;
	
	private Integer childCount;
	
	private Integer subDevReqId;
	
	private Date createdAt;
	
	private Date updatedAt;
	
	private String email;
	
	private String orgName;
	
	private String status;
	
	private String txnStatus;
	
	private double payment;
	
	public double getPayment() {
		return payment;
	}

	public void setPayment(double payment) {
		this.payment = payment;
	}

	private String rejectionReason;

	public String getRejectionReason() {
		return rejectionReason;
	}

	public void setRejectionReason(String rejectionReason) {
		this.rejectionReason = rejectionReason;
	}

	public String getTxnStatus() {
		return txnStatus;
	}

	public void setTxnStatus(String txnStatus) {
		this.txnStatus = txnStatus;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public Integer getSubDevReqId() {
		return subDevReqId;
	}

	public void setSubDevReqId(Integer subDevReqId) {
		this.subDevReqId = subDevReqId;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

}
