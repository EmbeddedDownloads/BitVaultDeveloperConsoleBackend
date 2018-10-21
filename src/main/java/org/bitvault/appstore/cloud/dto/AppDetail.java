package org.bitvault.appstore.cloud.dto;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.bitvault.appstore.cloud.constant.ErrorCode;
import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.bitvault.appstore.cloud.model.AppApplication;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.BeanUtils;

public class AppDetail {

	private Integer appDetailId;

	@NotNull(message = ErrorMessageConstant.APPLICATION_ID_ERROR)
	private Integer applicationId;

	private String apkUrl;

	private String appIconUrl;

	private String appVersionName;

	private Integer appVersionNo;

	@NotBlank(message = ErrorMessageConstant.FULL_DESCRIPTION_ERROR)
	private String fullDescription;

	@NotBlank(message = ErrorMessageConstant.SHORT_DESCRIPTION_ERROR)
	private String shortDescription;

	private Date updatedAt;

	private Date createdAt;

	private String whatsNew;
	
	private String status;

	private List<AppImage> appImage;

	public Integer getAppDetailId() {
		return appDetailId;
	}

	public void setAppDetailId(Integer appDetailId) {
		this.appDetailId = appDetailId;
	}

	public String getApkUrl() {
		return apkUrl;
	}

	public void setApkUrl(String apkUrl) {
		this.apkUrl = apkUrl;
	}

	public String getAppIconUrl() {
		return appIconUrl;
	}

	public void setAppIconUrl(String appIconUrl) {
		this.appIconUrl = appIconUrl;
	}

	public String getAppVersionName() {
		return appVersionName;
	}

	public void setAppVersionName(String appVersionName) {
		this.appVersionName = appVersionName;
	}

	public String getFullDescription() {
		return fullDescription;
	}

	public void setFullDescription(String fullDescription) {
		this.fullDescription = fullDescription;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	
	public String getWhatsNew() {
		return whatsNew;
	}

	public void setWhatsNew(String whatsNew) {
		this.whatsNew = whatsNew;
	}

	public Integer getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(Integer applicationId) {
		this.applicationId = applicationId;
	}

	public List<AppImage> getAppImage() {
		return appImage;
	}

	public void setAppImage(List<AppImage> appImage) {
		this.appImage = appImage;
	}

	public org.bitvault.appstore.cloud.model.AppDetail populateAppDetail(AppDetail appDetailDTO)
			throws BitVaultException {
		org.bitvault.appstore.cloud.model.AppDetail appDetail = new org.bitvault.appstore.cloud.model.AppDetail();
		try {
			BeanUtils.copyProperties(appDetailDTO, appDetail);
			AppApplication app = new AppApplication();
			app.setAppApplicationId(appDetailDTO.getApplicationId());
			appDetail.setAppApplication(app);

		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED, ErrorCode.SOME_ERROR_OCCURED_CODE);
		}

		return appDetail;
	}

	public org.bitvault.appstore.cloud.model.AppDetail populateAppDetail(
			org.bitvault.appstore.cloud.dto.AppApplication appDto, AppDetail appDetail) {
		org.bitvault.appstore.cloud.model.AppApplication app = new org.bitvault.appstore.cloud.model.AppApplication();
		
		app.setAppApplicationId(appDto.getAppApplicationId());
		org.bitvault.appstore.cloud.model.AppDetail appDetailModel = new org.bitvault.appstore.cloud.model.AppDetail();
		appDetailModel.setAppDetailId(appDetail.getAppDetailId());
		appDetailModel.setAppApplication(app);
		appDetailModel.setApkUrl(appDto.getApkUrl());
		appDetailModel.setAppIconUrl(appDto.getAppIconUrl());

		appDetailModel.setAppVersionName(appDto.getLatestVersionName());
		appDetailModel.setAppVersionNo(appDto.getLatestVersionNo());
		appDetailModel.setFullDescription(appDetail.getFullDescription());
		appDetailModel.setShortDescription(appDetail.getShortDescription());
		appDetailModel.setStatus(appDto.getStatus());
		appDetailModel.setCreatedAt(appDto.getCreatedAt());
		return appDetailModel;

	}

	public Integer getAppVersionNo() {
		return appVersionNo;
	}

	public void setAppVersionNo(Integer appVersionNo) {
		this.appVersionNo = appVersionNo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}