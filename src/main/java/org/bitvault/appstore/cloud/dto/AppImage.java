package org.bitvault.appstore.cloud.dto;

import java.util.Date;

import org.bitvault.appstore.cloud.constant.ErrorCode;
import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.springframework.beans.BeanUtils;

public class AppImage {

	private int appImagesId;
	private String imageType;
	private String appImageUrl;
	private String status;
	private Date createdAt;
	private String createdBy;
	private Date updatedAt;
	private Integer applicationId;

//	private AppApplication appApplication;
	private String updatedBy;

	public int getAppImagesId() {
		return appImagesId;
	}

	public void setAppImagesId(int appImagesId) {
		this.appImagesId = appImagesId;
	}

	
	public String getAppImageUrl() {
		return appImageUrl;
	}

	public void setAppImageUrl(String appImageUrl) {
		this.appImageUrl = appImageUrl;
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

	public org.bitvault.appstore.cloud.model.AppImage populateAppImag(AppImage appImageDTO) throws BitVaultException {
		org.bitvault.appstore.cloud.model.AppImage appImage = new org.bitvault.appstore.cloud.model.AppImage();
		try {
			BeanUtils.copyProperties(appImageDTO, appImage);
      
		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED, ErrorCode.SOME_ERROR_OCCURED_CODE);
		}

		return appImage;

	}

	public String getImageType() {
		return imageType;
	}

	public void setImageType(String imageType) {
		this.imageType = imageType;
	}

	public Integer getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(Integer applicationId) {
		this.applicationId = applicationId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

//	public Integer getApplicationId() {
//		return applicationId;
//	}
//
//	public void setApplicationId(Integer applicationId) {
//		this.applicationId = applicationId;
//	}

//	public AppApplication getAppApplication() {
//		return appApplication;
//	}
//
//	public void setAppApplication(AppApplication appApplication) {
//		this.appApplication = appApplication;
//	}
}
