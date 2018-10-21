package org.bitvault.appstore.cloud.dto;

import java.util.Date;

import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.bitvault.appstore.cloud.model.RejectedApp;
import org.springframework.beans.BeanUtils;

public class RejectedAppDto {
	private int rejectedAppId;

	private String adminId;

	private Date createdAt;

	private String reason;

	private String status;

	private Date updatedAt;

	private AppDetail appDetail;

	public int getRejectedAppId() {
		return rejectedAppId;
	}

	public void setRejectedAppId(int rejectedAppId) {
		this.rejectedAppId = rejectedAppId;
	}

	public String getAdminId() {
		return adminId;
	}

	public void setAdminId(String adminId) {
		this.adminId = adminId;
	}

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

	public AppDetail getAppDetail() {
		return appDetail;
	}

	public void setAppDetail(AppDetail appDetail) {
		this.appDetail = appDetail;
	}

	public RejectedApp populateRejectedApp(RejectedAppDto rejAppDto) {
		RejectedApp rejApp = new RejectedApp();
		try {
			BeanUtils.copyProperties(rejAppDto, rejApp);
			rejApp.setAppDetail(rejAppDto.getAppDetail().populateAppDetail(rejAppDto.getAppDetail()));
		} catch (Exception e) {
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);
		}

		return rejApp;
	}
}