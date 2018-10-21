package org.bitvault.appstore.cloud.model;

import java.io.Serializable;
import javax.persistence.*;

import org.bitvault.appstore.cloud.constant.ErrorCode;
import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.dto.RequestTypeDto;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonBackReference;

import java.util.Date;
import java.util.List;

/**
 * The persistent class for the request_type database table.
 * 
 */
@Entity
@Table(name = "request_type")
@NamedQuery(name = "RequestType.findAll", query = "SELECT r FROM RequestType r")
public class RequestType extends Auditable<String> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "request_type_id", unique = true, nullable = false)
	private int requestTypeId;

	@Column(nullable = false)
	private int count;
	
	@Column(name = "alert_activated")
	private String alertActivated ;
	
	
	// @Temporal(TemporalType.TIMESTAMP)
	// @Column(name = "created_at", nullable = false)
	// private Date createdAt;

	@Column(name = "request_type", nullable = false, length = 255)
	private String requestType;

	@Column(nullable = false, length = 1)
	private String status;

	// @Temporal(TemporalType.TIMESTAMP)
	// @Column(name = "updated_at", nullable = false)
	// private Date updatedAt;

	// bi-directional many-to-one association to Request
	@OneToMany(mappedBy = "requestType")
	@JsonBackReference("Request-RequestType")
	private List<Request> requests;

	// bi-directional many-to-one association to Request

	public RequestType() {
	}
	
	

	public String getAlertActivated() {
		return alertActivated;
	}

	public void setAlertActivated(String alertActivated) {
		this.alertActivated = alertActivated;
	}



	public int getRequestTypeId() {
		return this.requestTypeId;
	}

	public void setRequestTypeId(int requestTypeId) {
		this.requestTypeId = requestTypeId;
	}

	public int getCount() {
		return this.count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	// public Date getCreatedAt() {
	// return this.createdAt;
	// }
	//
	// public void setCreatedAt(Date createdAt) {
	// this.createdAt = createdAt;
	// }

	public String getRequestType() {
		return this.requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	// public Date getUpdatedAt() {
	// return this.updatedAt;
	// }
	//
	// public void setUpdatedAt(Date updatedAt) {
	// this.updatedAt = updatedAt;
	// }

	public List<Request> getRequests() {
		return this.requests;
	}

	public void setRequests(List<Request> requests) {
		this.requests = requests;
	}

	public Request addRequest(Request request) {
		getRequests().add(request);
		request.setRequestType(this);

		return request;
	}

	public Request removeRequest(Request request) {
		getRequests().remove(request);
		request.setRequestType(null);

		return request;
	}

	public RequestTypeDto populateRequestTypeDto(RequestType requestType) {
		RequestTypeDto requestTypeDto = new RequestTypeDto();
		try {
			BeanUtils.copyProperties(requestType, requestTypeDto);

		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED, ErrorCode.SOME_ERROR_OCCURED_CODE);
		}
		return requestTypeDto;

	}

}