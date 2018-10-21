package org.bitvault.appstore.cloud.dto;

public class RequestTypeDto {
	private int requestTypeId;

	private int count;

	private String requestType;

	private String status;
	
	private String alertActivated ;

	public String getAlertActivated() {
		return alertActivated;
	}

	public void setAlertActivated(String alertActivated) {
		this.alertActivated = alertActivated;
	}

	public int getRequestTypeId() {
		return requestTypeId;
	}

	public void setRequestTypeId(int requestTypeId) {
		this.requestTypeId = requestTypeId;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}


}