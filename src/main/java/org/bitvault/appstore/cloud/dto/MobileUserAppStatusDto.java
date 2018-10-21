package org.bitvault.appstore.cloud.dto;

import java.util.Date;

import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.bitvault.appstore.cloud.model.MobileUserAppStatus;
import org.springframework.beans.BeanUtils;

public class MobileUserAppStatusDto {

	private int mobileUserAppStatusId;

	private float appPrice;

	private String appTxnId;

	private String status;

	private AppApplication application;

	private MobileUser mobileUser;

	private Date createdAt;

	private Date updatedAt;

	public int getMobileUserAppStatusId() {
		return mobileUserAppStatusId;
	}

	public void setMobileUserAppStatusId(int mobileUserAppStatusId) {
		this.mobileUserAppStatusId = mobileUserAppStatusId;
	}

	public float getAppPrice() {
		return appPrice;
	}

	public void setAppPrice(float appPrice) {
		this.appPrice = appPrice;
	}

	public String getAppTxnId() {
		return appTxnId;
	}

	public void setAppTxnId(String appTxnId) {
		this.appTxnId = appTxnId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public AppApplication getApplication() {
		return application;
	}

	public void setApplication(AppApplication application) {
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

	public MobileUser getMobileUser() {
		return mobileUser;
	}

	public void setMobileUser(MobileUser mobileUser) {
		this.mobileUser = mobileUser;
	}
	public MobileUserAppStatus populateMobileUserAppStatus(MobileUserAppStatusDto mobileUserAppDto) {
		MobileUserAppStatus mobileUserApp = new MobileUserAppStatus();
		try {
			BeanUtils.copyProperties(mobileUserAppDto, mobileUserApp);
			mobileUserApp.setMobileUser(
					mobileUserAppDto.getMobileUser().populateMobileUser(mobileUserAppDto.getMobileUser()));
			mobileUserApp.setApplication(
					mobileUserAppDto.getApplication().populateAppApplication(mobileUserAppDto.getApplication()));
		} catch (Exception e) {
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);
		}
		return mobileUserApp;

	}
}
