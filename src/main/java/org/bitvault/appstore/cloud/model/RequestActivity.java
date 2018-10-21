package org.bitvault.appstore.cloud.model;

import java.io.Serializable;
import javax.persistence.*;

import org.bitvault.appstore.cloud.constant.ErrorCode;
import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonManagedReference;


/**
 * The persistent class for the request_activity database table.
 * 
 */
@Entity
@Table(name = "request_activity")
@NamedQuery(name = "RequestActivity.findAll", query = "SELECT r FROM RequestActivity r")
public class RequestActivity extends Auditable<String> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "activity_id", unique = true, nullable = false)
	private int activityId;

	// @Temporal(TemporalType.TIMESTAMP)
	// @Column(name="created_at", nullable=false)
	// private Date createdAt;
//
//	@Column(name = "request_id", nullable = false)
//	private int requestId;

	@Column(nullable = false, length = 50)
	private String status;

	@Column(name = "seen_status")
	private String seenStatus;

	
	//
	// @Temporal(TemporalType.TIMESTAMP)
	// @Column(name="updated_at", nullable=false)
	// private Date updatedAt;

	
	@Column(name = "user_id")
	private String user_id;
	@Column(name = "application_id")
	private Integer applicationId=-1;

	@Column(name = "request_type_id")
	private Integer requestTypeId=4;

	@Column(name = "request_id")
	private Integer requestId=-1;

	
	
	public String getSeenStatus() {
		return seenStatus;
	}

	public void setSeenStatus(String seenStatus) {
		this.seenStatus = seenStatus;
	}

	public Integer getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(Integer applicationId) {
		this.applicationId = applicationId;
	}

	public RequestActivity() {
	}

	public int getActivityId() {
		return this.activityId;
	}

	public void setActivityId(int activityId) {
		this.activityId = activityId;
	}
	@Column(name = "sub_dev_req_id")
	private Integer subDevReqId=-1;
	
	public Integer getSubDevReqId() {
		return subDevReqId;
	}

	public void setSubDevReqId(Integer subDevReqId) {
		this.subDevReqId = subDevReqId;
	}
	// public Date getCreatedAt() {
	// return this.createdAt;
	// }
	//
	// public void setCreatedAt(Date createdAt) {
	// this.createdAt = createdAt;
	// }
//
//	public int getRequestId() {
//		return this.requestId;
//	}
//
//	public void setRequestId(int requestId) {
//		this.requestId = requestId;
//	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	//
	// public Date getUpdatedAt() {
	// return this.updatedAt;
	// }
	//
	// public void setUpdatedAt(Date updatedAt) {
	// this.updatedAt = updatedAt;
	// }

	@Column(name = "child_count")
	private Integer childCount=0;
	public Integer getChildCount() {
		return childCount;
	}

	public void setChildCount(Integer childCount) {
		this.childCount = childCount;
	}
	@Column(name="dev_payment_id")
	private Integer devPaymentId=0;
	

	public Integer getDevPaymentId() {
		return devPaymentId;
	}

	public void setDevPaymentId(Integer devPaymentId) {
		this.devPaymentId = devPaymentId;
	}

	public org.bitvault.appstore.cloud.dto.RequestActivityDto populateRequestActivity(RequestActivity requestActivity)
			throws BitVaultException {
		org.bitvault.appstore.cloud.dto.RequestActivityDto requestActivityDto = new org.bitvault.appstore.cloud.dto.RequestActivityDto();
		try {
			BeanUtils.copyProperties(requestActivity, requestActivityDto);
			
			
			requestActivityDto.setRequestId(requestActivity.getRequestId());
		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED, ErrorCode.SOME_ERROR_OCCURED_CODE);
		}

		return requestActivityDto;

	}

	public Integer getRequestTypeId() {
		return requestTypeId;
	}

	public void setRequestTypeId(Integer requestTypeId) {
		this.requestTypeId = requestTypeId;
	}

	public Integer getRequestId() {
		return requestId;
	}

	public void setRequestId(Integer requestId) {
		this.requestId = requestId;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
}