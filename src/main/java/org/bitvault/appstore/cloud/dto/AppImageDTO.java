package org.bitvault.appstore.cloud.dto;

import java.util.Calendar;

import org.bitvault.appstore.cloud.constant.ErrorCode;
import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.bitvault.appstore.cloud.model.AppImage;
import org.springframework.beans.BeanUtils;

public class AppImageDTO {

	private int appImagesId;
	private String appBannerUrl;
	private String appImageUrl;
	private Calendar createdAt;
	private String createdBy;
	private Calendar updatedAt;

	private String updatedBy;

	public int getAppImagesId() {
		return appImagesId;
	}

	public void setAppImagesId(int appImagesId) {
		this.appImagesId = appImagesId;
	}

	public String getAppBannerUrl() {
		return appBannerUrl;
	}

	public void setAppBannerUrl(String appBannerUrl) {
		this.appBannerUrl = appBannerUrl;
	}

	public String getAppImageUrl() {
		return appImageUrl;
	}

	public void setAppImageUrl(String appImageUrl) {
		this.appImageUrl = appImageUrl;
	}

	public Calendar getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Calendar createdAt) {
		this.createdAt = createdAt;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Calendar getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Calendar updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public AppImage populateAppImag(AppImageDTO appImageDTO) throws BitVaultException {
		AppImage appImage = new AppImage();
		try {
			BeanUtils.copyProperties(appImageDTO, appImage);

		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED, ErrorCode.SOME_ERROR_OCCURED_CODE);
		}

		return appImage;

	}
}
