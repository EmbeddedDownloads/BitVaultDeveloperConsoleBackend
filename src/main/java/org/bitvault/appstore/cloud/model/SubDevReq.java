package org.bitvault.appstore.cloud.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.bitvault.appstore.cloud.constant.ErrorCode;
import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.springframework.beans.BeanUtils;

@Entity
@Table(name = "sub_dev_req")
public class SubDevReq extends Auditable<String> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2076019192085399905L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	@Column(name = "sub_dev_req_id")
	private Integer subDevReqId;
	
	public Integer getSubDevReqId() {
		return subDevReqId;
	}

	public void setSubDevReqId(Integer subDevReqId) {
		this.subDevReqId = subDevReqId;
	}

	@Column(name = "user_id")
	private String userId;
	
	private String status;
	
	@Column(name = "child_count")
	private Integer childCount;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "dev_payment_id")
	private DevPayment devPaymentId;
	
	private String rejectionReason;

	public String getRejectionReason() {
		return rejectionReason;
	}

	public void setRejectionReason(String rejectionReason) {
		this.rejectionReason = rejectionReason;
	}

	public DevPayment getDevPaymentId() {
		return devPaymentId;
	}

	public void setDevPaymentId(DevPayment devPaymentId) {
		this.devPaymentId = devPaymentId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getChildCount() {
		return childCount;
	}

	public void setChildCount(Integer childCount) {
		this.childCount = childCount;
	}
	
	public org.bitvault.appstore.cloud.model.RequestActivity populateRequestActivity(SubDevReq subDevReq)
			throws BitVaultException {
		org.bitvault.appstore.cloud.model.RequestActivity requestActivity = new org.bitvault.appstore.cloud.model.RequestActivity();
		try {
			BeanUtils.copyProperties(subDevReq, requestActivity);
			requestActivity.setUser_id(subDevReq.getUserId());
		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED, ErrorCode.SOME_ERROR_OCCURED_CODE);
		}

		return requestActivity;

	}

}
