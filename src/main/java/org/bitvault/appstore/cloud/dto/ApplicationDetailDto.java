package org.bitvault.appstore.cloud.dto;

import java.util.Date;
import java.util.List;

import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.springframework.beans.BeanUtils;

public class ApplicationDetailDto {

	private Integer appDetailId;

	private org.bitvault.appstore.cloud.dto.Application application;

	public org.bitvault.appstore.cloud.dto.Application getApplication() {
		return application;
	}

	public void setApplication(org.bitvault.appstore.cloud.dto.Application application) {
		this.application = application;
	}

	private String apkUrl;

	private String appIconUrl;

	private String appVersionName;

	private Integer appVersionNo;

	private String fullDescription;

	private String shortDescription;

	private Date updatedAt;

	private Date createdAt;

	private String whats_new;

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

	public String getWhats_new() {
		return whats_new;
	}

	public void setWhats_new(String whats_new) {
		this.whats_new = whats_new;
	}

	public List<AppImage> getAppImage() {
		return appImage;
	}

	public void setAppImage(List<AppImage> appImage) {
		this.appImage = appImage;
	}

	public Integer getAppVersionNo() {
		return appVersionNo;
	}

	public void setAppVersionNo(Integer appVersionNo) {
		this.appVersionNo = appVersionNo;
	}

	public static ApplicationDetailDto populateApplicationDetailDto(AppDetail appDetail, Application app) {
		ApplicationDetailDto applicationDetailDto = new ApplicationDetailDto();
		try {
			BeanUtils.copyProperties(appDetail, applicationDetailDto);
			applicationDetailDto.setApplication(app);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);
		}
		return applicationDetailDto;
	}
}
