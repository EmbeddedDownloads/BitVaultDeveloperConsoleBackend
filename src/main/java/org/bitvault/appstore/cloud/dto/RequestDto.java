package org.bitvault.appstore.cloud.dto;

import java.util.Date;

import org.bitvault.appstore.cloud.constant.ErrorCode;
import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.springframework.beans.BeanUtils;

public class RequestDto {
	private int requesttId;
	private String userId;
	private String rejectionReason;
	private String status;
	private int requestTypeId;
	private int applicationId;
	private Date createdAt;
	private Date updatedAt;

	
	public int getRequesttId() {
		return requesttId;
	}

	public void setRequesttId(int requesttId) {
		this.requesttId = requesttId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getRejectionReason() {
		return rejectionReason;
	}

	public void setRejectionReason(String rejectionReason) {
		this.rejectionReason = rejectionReason;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getRequestTypeId() {
		return requestTypeId;
	}

	public void setRequestTypeId(int requestTypeId) {
		this.requestTypeId = requestTypeId;
	}

	public int getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(int applicationId) {
		this.applicationId = applicationId;
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
	public org.bitvault.appstore.cloud.model.Request populateRequest(RequestDto requestdto)
			throws BitVaultException {
		org.bitvault.appstore.cloud.model.Request requestModel = new org.bitvault.appstore.cloud.model.Request();
		try {
			BeanUtils.copyProperties(requestdto, requestModel);

		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED, ErrorCode.SOME_ERROR_OCCURED_CODE);
		}

		return requestModel;

	}
}
