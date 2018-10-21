package org.bitvault.appstore.cloud.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.bitvault.appstore.cloud.constant.ErrorCode;
import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
 * The persistent class for the request database table.
 * 
 */
@Entity
@Table(name = "request")
@NamedQuery(name = "Request.findAll", query = "SELECT r FROM Request r")
public class Request extends Auditable<String> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "request_id",unique = true, nullable = false)
	private Integer request_Id;

	private String status;

	@Column(name = "rejection_reason")
	private String rejectionReason;

	// @ManyToOne(fetch = FetchType.LAZY)
	// @JoinColumn(name = "user_id")
	// private DevUser devUser;
	//
	// public DevUser getDevUser() {
	// return devUser;
	// }
	//
	// public void setDevUser(DevUser devUser) {
	// this.devUser = devUser;
	// }

	public String getRejectionReason() {
		return rejectionReason;
	}

	public void setRejectionReason(String rejectionReason) {
		this.rejectionReason = rejectionReason;
	}

	@Column(name = "user_id")
	private String userId;



	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	// bi-directional many-to-one association to RequestType
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "request_type_id")
	@JsonManagedReference("RequestType-Request")
	private RequestType requestType;

	
	@Column(name = "application_id")
	private Integer applicationId=-1;

	public Integer getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(Integer applicationId) {
		this.applicationId = applicationId;
	}

	public Request() {
	}

	public Integer getRequest_Id() {
		return this.request_Id;
	}

	public void setRequest_Id(Integer request_Id) {
		this.request_Id = request_Id;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public RequestType getRequestType() {
		return this.requestType;
	}

	public void setRequestType(RequestType requestType) {
		this.requestType = requestType;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Request))
			return false;
		Request other = (Request) obj;
		if (request_Id != other.request_Id)
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (request_Id ^ (request_Id >>> 32));
		return result;
	}
		public org.bitvault.appstore.cloud.model.RequestActivity populateRequestActivity(Request request)
			throws BitVaultException {
		org.bitvault.appstore.cloud.model.RequestActivity requestActivity = new org.bitvault.appstore.cloud.model.RequestActivity();
		try {
			BeanUtils.copyProperties(request, requestActivity);
			requestActivity.setUser_id(request.getUserId());
			requestActivity.setRequestId(request.getRequest_Id());
			requestActivity.setRequestTypeId(request.getRequestType().getRequestTypeId());
		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED, ErrorCode.SOME_ERROR_OCCURED_CODE);
		}

		return requestActivity;

	}
		public org.bitvault.appstore.cloud.dto.RequestDto populateRequest(Request request)
				throws BitVaultException {
			org.bitvault.appstore.cloud.dto.RequestDto requestDto = new org.bitvault.appstore.cloud.dto.RequestDto();
			try {
				BeanUtils.copyProperties(request, requestDto);
			
				requestDto.setRequestTypeId(request.getRequestType().getRequestTypeId());
				requestDto.setRequesttId(request.getRequest_Id());
				
				
			} catch (Exception e) {
				e.printStackTrace();
				throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED, ErrorCode.SOME_ERROR_OCCURED_CODE);
			}

			return requestDto;

		}
}