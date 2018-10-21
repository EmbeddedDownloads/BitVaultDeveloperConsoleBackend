package org.bitvault.appstore.cloud.dto;

import org.bitvault.appstore.cloud.constant.ErrorCode;
import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.bitvault.appstore.cloud.model.ApplicationType;
import org.springframework.beans.BeanUtils;

public class ApplicationTypeDto {

	private Integer appTypeId;

	private String appTypeName;
	
	private String appTypeIcon;

	public Integer getAppTypeId() {
		return appTypeId;
	}

	public void setAppTypeId(Integer appTypeId) {
		this.appTypeId = appTypeId;
	}

	public String getAppTypeName() {
		return appTypeName;
	}

	public void setAppTypeName(String appTypeName) {
		this.appTypeName = appTypeName;
	}

	public String getAppTypeIcon() {
		return appTypeIcon;
	}

	public void setAppTypeIcon(String appTypeIcon) {
		this.appTypeIcon = appTypeIcon;
	}

	public ApplicationType populateApplicationTypeDto(ApplicationTypeDto appTypeDto) {
		ApplicationType appType = new ApplicationType();
		try {
			BeanUtils.copyProperties(appTypeDto, appType);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED, ErrorCode.SOME_ERROR_OCCURED_CODE);
		}
		return appType;
	}
}
