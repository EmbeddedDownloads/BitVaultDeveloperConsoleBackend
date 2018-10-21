package org.bitvault.appstore.cloud.dto;

import org.bitvault.appstore.cloud.constant.ErrorCode;
import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.bitvault.appstore.cloud.model.ApplicationType;
import org.springframework.beans.BeanUtils;

public class AppCategory {

	private Integer appCategoryId;

	private String categoryType;
	private String status;
	private String description = "";
	private String categoryIconUrl;
	private String categoryBannerUrl ;
	private Integer categoryCount;
	Integer appTypeId;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getCategoryCount() {
		return categoryCount;
	}

	public void setCategoryCount(Integer categoryCount) {
		this.categoryCount = categoryCount;
	}

	public Integer getAppTypeId() {
		return appTypeId;
	}

	public void setAppTypeId(Integer appTypeId) {
		this.appTypeId = appTypeId;
	}
	
	
	

	public String getCategoryBannerUrl() {
		return categoryBannerUrl;
	}

	public void setCategoryBannerUrl(String categoryBannerUrl) {
		this.categoryBannerUrl = categoryBannerUrl;
	}

	public Integer getAppCategoryId() {
		return appCategoryId;
	}

	public void setAppCategoryId(Integer appCategoryId) {
		this.appCategoryId = appCategoryId;
	}

	public String getCategoryType() {
		return categoryType;
	}

	public void setCategoryType(String categoryType) {
		this.categoryType = categoryType;
	}

	public org.bitvault.appstore.cloud.model.AppCategory populateAppCategoryDTO(AppCategory appCategoryDTO)
			throws BitVaultException {
		org.bitvault.appstore.cloud.model.AppCategory appCategory = new org.bitvault.appstore.cloud.model.AppCategory();
		try {
			BeanUtils.copyProperties(appCategoryDTO, appCategory);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED, ErrorCode.SOME_ERROR_OCCURED_CODE);
		}
		return appCategory;
	}
	
	public org.bitvault.appstore.cloud.model.AppCategory populateAppCategoryDTOWithAppType(AppCategory appCategoryDTO)
			throws BitVaultException {
		org.bitvault.appstore.cloud.model.AppCategory appCategory = new org.bitvault.appstore.cloud.model.AppCategory();
		try {
			BeanUtils.copyProperties(appCategoryDTO, appCategory);
			ApplicationType appType = new ApplicationType();
			appType.setAppTypeId(appCategoryDTO.appTypeId);
			appCategory.setApplicationType(appType);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED, ErrorCode.SOME_ERROR_OCCURED_CODE);
		}
		return appCategory;
	}

	public String getCategoryIconUrl() {
		return categoryIconUrl;
	}

	public void setCategoryIconUrl(String categoryIconUrl) {
		this.categoryIconUrl = categoryIconUrl;
	}

}