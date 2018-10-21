package org.bitvault.appstore.cloud.dto;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.apache.commons.beanutils.BeanUtils;
import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.bitvault.appstore.cloud.model.DevUser;
import org.bitvault.appstore.cloud.model.UserActivity;
import org.bitvault.appstore.cloud.model.UserActivityType;

public class UserActivityDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private int activityId;

	private String status;

	private String userId;

	private Date updatedAt;

	private String updatedBy;
	
	private Date createdAt;
	private String createdBy;
	
	
	private String userName;

	private String avatarURL;

	private String txnId;
	
	private String amountPaid;
	
	private String txnStatus;
	
	private String paymentFor;
	
	private String paymentMode;

	private String subDevUserId ;
	
	private String subDevUserName ;
	
	private String subDevEmail ;
	
	
	
	public String getSubDevEmail() {
		return subDevEmail;
	}



	public void setSubDevEmail(String subDevEmail) {
		this.subDevEmail = subDevEmail;
	}


	private UserActivityType userActivityType;

	public int getActivityId() {
		return this.activityId;
	}

	
	
	public String getSubDevUserName() {
		return subDevUserName;
	}



	public void setSubDevUserName(String subDevUserName) {
		this.subDevUserName = subDevUserName;
	}



	public void setActivityId(int activityId) {
		this.activityId = activityId;
	}

	public Date getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getUpdatedAt() {
		return this.updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getUserId() {
		return this.userId;
	}

	
	
	public String getSubDevUserId() {
		return subDevUserId;
	}

	public void setSubDevUserId(String subDevUserId) {
		this.subDevUserId = subDevUserId;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public UserActivityType getUserActivityType() {
		return this.userActivityType;
	}

	public void setUserActivityType(UserActivityType userActivityType) {
		this.userActivityType = userActivityType;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getAvatarURL() {
		return avatarURL;
	}

	public void setAvatarURL(String avatarURL) {
		this.avatarURL = avatarURL;
	}

	public String getTxnId() {
		return txnId;
	}

	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}

	public String getAmountPaid() {
		return amountPaid;
	}

	public void setAmountPaid(String amountPaid) {
		this.amountPaid = amountPaid;
	}

	public String getTxnStatus() {
		return txnStatus;
	}

	public void setTxnStatus(String txnStatus) {
		this.txnStatus = txnStatus;
	}

	public String getPaymentFor() {
		return paymentFor;
	}

	public void setPaymentFor(String paymentFor) {
		this.paymentFor = paymentFor;
	}

	public String getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}

	public UserActivity populateUserActivity(UserActivityDto userActivityDto) {

		if(userActivityDto == null) {
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED, org.bitvault.appstore.cloud.constant.ErrorCode.SOME_ERROR_OCCURED_CODE);
		}
		
		UserActivity userActivty = new UserActivity() ;
		
		try {
			
			BeanUtils.copyProperties(userActivty,userActivityDto);
			userActivty.setUserActivityType(userActivityDto.getUserActivityType());
		}catch(Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED,org.bitvault.appstore.cloud.constant.ErrorCode.SOME_ERROR_OCCURED_CODE);
		}
		
		return userActivty;
	}
	

	public UserActivityDto populateUserDetails(DevUser devUserDto, UserActivityDto userActivityDto) {

		if(devUserDto == null) {
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED, org.bitvault.appstore.cloud.constant.ErrorCode.SOME_ERROR_OCCURED_CODE);
		}
		
		
		try {
			
			userActivityDto.setUserId(devUserDto.getUserId());
			userActivityDto.setUserName(devUserDto.getUsername());
			
		}catch(Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED,org.bitvault.appstore.cloud.constant.ErrorCode.SOME_ERROR_OCCURED_CODE);
		}
		
		return userActivityDto;
	}
	
}