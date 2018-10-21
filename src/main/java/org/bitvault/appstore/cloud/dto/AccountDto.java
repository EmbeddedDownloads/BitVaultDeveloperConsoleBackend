package org.bitvault.appstore.cloud.dto;

import java.io.Serializable;
import java.util.Date;

public class AccountDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -769600882131418736L;

	private Integer accId;

	private String country;

	private Date createdAt;

	private String createdBy;

	private String accEmail;

	private String orgName;

	private String state;

	private String status;

	private Date updatedAt;

	public Integer getAccId() {
		return accId;
	}

	public void setAccId(Integer accId) {
		this.accId = accId;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getEmail() {
		return accEmail;
	}

	public void setEmail(String accEmail) {
		this.accEmail = accEmail;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

//	public RoleDto getRole() {
//		return role;
//	}
//
//	public void setRole(RoleDto role) {
//		this.role = role;
//	}

	private String updatedBy;

	private String website;

//	private RoleDto role;

}
