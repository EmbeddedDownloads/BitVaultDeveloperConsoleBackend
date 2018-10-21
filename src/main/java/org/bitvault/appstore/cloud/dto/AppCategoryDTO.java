package org.bitvault.appstore.cloud.dto;

import org.bitvault.appstore.cloud.constant.ErrorCode;
import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.bitvault.appstore.cloud.model.AppCategory;
import org.springframework.beans.BeanUtils;

public class AppCategoryDTO {

	private int appCategoryId;

	private String categoryType;


	public int getAppCategoryId() {
		return appCategoryId;
	}

	public void setAppCategoryId(int appCategoryId) {
		this.appCategoryId = appCategoryId;
	}

	public String getCategoryType() {
		return categoryType;
	}

	public void setCategoryType(String categoryType) {
		this.categoryType = categoryType;
	}

	public AppCategory populateAppCategoryDTO(AppCategoryDTO appCategoryDTO) throws BitVaultException {
		AppCategory appCategory = new AppCategory();
		try {
			BeanUtils.copyProperties(appCategoryDTO, appCategory);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED, ErrorCode.SOME_ERROR_OCCURED_CODE);
		}
		return appCategory;
	}

}