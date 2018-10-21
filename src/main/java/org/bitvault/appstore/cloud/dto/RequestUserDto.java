package org.bitvault.appstore.cloud.dto;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

public class RequestUserDto implements Serializable {
	private static final long serialVersionUID = 2639507546075453967L;

	private Integer reqId;

	public Integer getReqId() {
		return reqId;
	}

	public void setReqId(Integer reqId) {
		this.reqId = reqId;
	}

	@NotNull
	@NotEmpty
	private String email;

	@NotNull
	@NotEmpty
	private String username;
	
	private String signupAs;
	
	private String signupReason;
	
	private String reason;
	
	private int childCount;
	
	public int getChildCount() {
		return childCount;
	}

	public void setChildCount(int childCount) {
		this.childCount = childCount;
	}

	private Date createdAt;

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getSignupReason() {
		return signupReason;
	}

	public void setSignupReason(String signupReason) {
		this.signupReason = signupReason;
	}

	public String getSignupAs() {
		return signupAs;
	}

	public void setSignupAs(String signupAs) {
		this.signupAs = signupAs;
	}

	private String status;
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}