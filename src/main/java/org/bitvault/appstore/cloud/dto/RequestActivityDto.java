package org.bitvault.appstore.cloud.dto;

import java.util.Date;

import org.bitvault.appstore.cloud.constant.ErrorCode;
import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.bitvault.appstore.cloud.model.Request;
import org.springframework.beans.BeanUtils;

public class RequestActivityDto {
	private int activityId;
	private String user_id;
	private Integer requestId;
	private String status;
	private RequestTypeDto requestType;
	private ApplicationReqActDto application;
	private Date createdAt;
	private Date updatedAt;
    private Integer childCount;
    private String seenStatus ;
    
    
    
    
	public String getSeenStatus() {
		return seenStatus;
	}

	public void setSeenStatus(String seenStatus) {
		this.seenStatus = seenStatus;
	}


	private Integer subDevReqId; 
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

	public int getActivityId() {
		return activityId;
	}

	public void setActivityId(int activityId) {
		this.activityId = activityId;
	}

	
	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public Integer getRequestId() {
		return requestId;
	}

	public void setRequestId(Integer requestId) {
		this.requestId = requestId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	

	

	public ApplicationReqActDto getApplication() {
		return application;
	}

	public void setApplication(ApplicationReqActDto application) {
		this.application = application;
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

	public RequestTypeDto getRequestType() {
		return requestType;
	}

	public void setRequestType(RequestTypeDto requestType) {
		this.requestType = requestType;
	}


	public RequestActivityDto populateRequest(RequestDto requestDto,RequestActivityDto requestActivityDto) {
		
		if(requestDto == null) {
			return null ;
		}
		
		BeanUtils.copyProperties(requestDto,requestActivityDto);
		requestActivityDto.setRequestId(requestDto.getRequesttId());
		
		return requestActivityDto ;
	}
	
	

}
