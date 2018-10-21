package org.bitvault.appstore.cloud.dto;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

public class DevUserDto implements Serializable {
	private static final long serialVersionUID = 2639507546075453967L;

	private String userId;

	@NotEmpty
	private String altEmail = "default@abc.com";

	private int childCount;

	@NotNull
	@NotEmpty
	private String email;

	@NotNull
	@NotEmpty
	private String gender="MALE";

	@NotNull
	@NotEmpty
	private String password;
	
	private String parentId = "";

	private Date createdAt ;
	
	private Double totalPaymentForSubDev ;
	
	public String getParentId() {
		return parentId;
	}


	public void setParentId(String parentId) {
		this.parentId = parentId;
	}


	@NotNull
	@NotEmpty
	private String signupReason;

	@NotNull
	@NotEmpty
	private String username;
	
	private String signupAs;
	
	private String status;
	
	private Double payment;
	
	private String txnStatus;
	
	private String ownBy;
	
//	private Double totalPayment ;

	
	
	public String getOwnBy() {
		return ownBy;
	}


	public Double getTotalPaymentForSubDev() {
		return totalPaymentForSubDev;
	}


	public void setTotalPaymentForSubDev(Double totalPaymentForSubDev) {
		this.totalPaymentForSubDev = totalPaymentForSubDev;
	}


	public void setOwnBy(String ownBy) {
		this.ownBy = ownBy;
	}


	public String getTxnStatus() {
		return txnStatus;
	}


	public void setTxnStatus(String txnStatus) {
		this.txnStatus = txnStatus;
	}


	public Double getPayment() {
		return payment;
	}


	public void setPayment(Double payment) {
		this.payment = payment;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public String getSignupAs() {
		return signupAs;
	}


	public void setSignupAs(String signupAs) {
		this.signupAs = signupAs;
	}


	private String country;
	
	private String accEmail;
	
	private String orgName;
	
	private String website;
	
	private String state;
	
	private String role;

	private String avatarURL;
	
//	private String privateKey;
//	
//	private String publicKey;
	
	private String rejectionReason;

	public String getRejectionReason() {
		return rejectionReason;
	}


	public void setRejectionReason(String rejectionReason) {
		this.rejectionReason = rejectionReason;
	}


	public String getAvatarURL() {
		return avatarURL;
	}


	public void setAvatarURL(String avatarURL) {
		this.avatarURL = avatarURL;
	}


	public String getRole() {
		return role;
	}


	public void setRole(String role) {
		this.role = role;
	}


	public String getUserId() {
		return userId;
	}


	public void setUserId(String userId) {
		this.userId = userId;
	}


	public String getAltEmail() {
		return altEmail;
	}


	public void setAltEmail(String altEmail) {
		this.altEmail = altEmail;
	}


	public int getChildCount() {
		return childCount;
	}


	public void setChildCount(int childCount) {
		this.childCount = childCount;
	}
	
	
	

//	public String getPrivateKey() {
//		return privateKey;
//	}
//
//
//	public void setPrivateKey(String privateKey) {
//		this.privateKey = privateKey;
//	}
//
//
//	public String getPublicKey() {
//		return publicKey;
//	}
//
//
//	public void setPublicKey(String publicKey) {
//		this.publicKey = publicKey;
//	}


	public Date getCreatedAt() {
		return createdAt;
	}


	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getGender() {
		return gender;
	}


	public void setGender(String gender) {
		this.gender = gender;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getSignupReason() {
		return signupReason;
	}


	public void setSignupReason(String signupReason) {
		this.signupReason = signupReason;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getCountry() {
		return country;
	}


	public void setCountry(String country) {
		this.country = country;
	}


	public String getAccEmail() {
		return accEmail;
	}


	public void setAccEmail(String accEmail) {
		this.accEmail = accEmail;
	}


	public String getOrgName() {
		return orgName;
	}


	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}


	public String getWebsite() {
		return website;
	}


	public void setWebsite(String website) {
		this.website = website;
	}


	public String getState() {
		return state;
	}


	public void setState(String state) {
		this.state = state;
	}


	@Override
	public String toString() {
		return "user_id:" + getUserId() + "\tusername:" + getUsername() + "\trole:" + "\temail:" + getEmail()
				+ "\talt_email:" + getAltEmail();
	}
}