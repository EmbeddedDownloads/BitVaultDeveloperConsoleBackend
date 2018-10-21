package org.bitvault.appstore.cloud.dto;

import java.util.Calendar;
import java.util.List;

import org.bitvault.appstore.cloud.constant.ErrorCode;
import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.bitvault.appstore.cloud.model.AppDetail;
import org.springframework.beans.BeanUtils;

public class AppDetailDTO {

	private Integer appDetailId;

	private Integer applicationId;

	private String apkUrl;

	private String appIconUrl;

	private String appVersionName;

	private float appVersionNo;

	private String fullDescription;

	private String shortDescription;

	private Calendar updatedAt;

	private Calendar createdAt;

	private String whats_new;

	private List<AppImageDTO> appImageDTO;

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

	public float getAppVersionNo() {
		return appVersionNo;
	}

	public void setAppVersionNo(float appVersionNo) {
		this.appVersionNo = appVersionNo;
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

	public Calendar getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Calendar updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Calendar getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Calendar createdAt) {
		this.createdAt = createdAt;
	}

	public String getWhats_new() {
		return whats_new;
	}

	public void setWhats_new(String whats_new) {
		this.whats_new = whats_new;
	}

	public AppDetail populateAppDetail(AppDetailDTO appDetailDTO) throws BitVaultException {
		AppDetail appDetail = new AppDetail();
		try {
			BeanUtils.copyProperties(appDetailDTO, appDetail);

		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED, ErrorCode.SOME_ERROR_OCCURED_CODE);
		}

		return appDetail;
	}

	public Integer getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(Integer applicationId) {
		this.applicationId = applicationId;
	}

	public List<AppImageDTO> getAppImageDTO() {
		return appImageDTO;
	}

	public void setAppImageDTO(List<AppImageDTO> appImageDTO) {
		this.appImageDTO = appImageDTO;
	}
}
