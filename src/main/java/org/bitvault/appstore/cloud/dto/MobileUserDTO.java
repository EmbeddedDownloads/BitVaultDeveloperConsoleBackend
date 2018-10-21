package org.bitvault.appstore.cloud.dto;

import java.util.Calendar;

import org.bitvault.appstore.cloud.constant.ErrorCode;
import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.bitvault.appstore.cloud.model.MobileUser;
import org.springframework.beans.BeanUtils;

public class MobileUserDTO {

	private Integer mobileUserId;

	private Calendar createdAt;

	private String publicAdd;

	private int reprocessedCount;

	private Calendar updatedAt;

	public Integer getMobileUserId() {
		return mobileUserId;
	}

	public void setMobileUserId(Integer mobileUserId) {
		this.mobileUserId = mobileUserId;
	}

	public Calendar getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Calendar createdAt) {
		this.createdAt = createdAt;
	}

	public String getPublicAdd() {
		return publicAdd;
	}

	public void setPublicAdd(String publicAdd) {
		this.publicAdd = publicAdd;
	}

	public int getReprocessedCount() {
		return reprocessedCount;
	}

	public void setReprocessedCount(int reprocessedCount) {
		this.reprocessedCount = reprocessedCount;
	}

	public Calendar getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Calendar updatedAt) {
		this.updatedAt = updatedAt;
	}

	public MobileUser populateMobileUser(MobileUserDTO mobileUserDTO) {
		MobileUser mobileUser = new MobileUser();
		try {
			BeanUtils.copyProperties(mobileUserDTO, mobileUser);
		} catch (Exception e) {
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED, ErrorCode.SOME_ERROR_OCCURED_CODE);
		}
		return mobileUser;

	}
}
