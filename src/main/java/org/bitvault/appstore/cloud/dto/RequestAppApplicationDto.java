package org.bitvault.appstore.cloud.dto;

import java.util.Date;

public class RequestAppApplicationDto {

	private Integer request_Id;
	
	private String status;

	private String rejectionReason;

	private String requestFor;	

	private String appName;
	private String title;

	private String email;

	private String language;

	private String latestVersionName;
	
	private String appIconUrl;

	private Date createdAt ;
	
	
	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getAppIconUrl() {
		return appIconUrl;
	}

	public void setAppIconUrl(String appIconUrl) {
		this.appIconUrl = appIconUrl;
	}

	



	public Integer getRequest_Id() {
		return request_Id;
	}

	public void setRequest_Id(Integer request_Id) {
		this.request_Id = request_Id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRejectionReason() {
		return rejectionReason;
	}

	public void setRejectionReason(String rejectionReason) {
		this.rejectionReason = rejectionReason;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getLatestVersionName() {
		return latestVersionName;
	}

	public void setLatestVersionName(String latestVersionName) {
		this.latestVersionName = latestVersionName;
	}

	public String getRequestFor() {
		return requestFor;
	}

	public void setRequestFor(String requestFor) {
		this.requestFor = requestFor;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
