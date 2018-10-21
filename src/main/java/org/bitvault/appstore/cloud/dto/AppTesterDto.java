package org.bitvault.appstore.cloud.dto;

import java.util.Date;

import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.bitvault.appstore.cloud.model.AppTester;
import org.bitvault.appstore.cloud.model.HelpSupport;
import org.springframework.beans.BeanUtils;

public class AppTesterDto 
{
	private Integer appTesterId;
	private String publicAddress;
	private String packageName;
	private Integer appApplicationId;
	private Date createdAt;
	private Date updatedAt;
	public Integer getAppTesterId() {
		return appTesterId;
	}
	public void setAppTesterId(Integer appTesterId) {
		this.appTesterId = appTesterId;
	}
	public String getPublicAddress() {
		return publicAddress;
	}
	public void setPublicAddress(String publicAddress) {
		this.publicAddress = publicAddress;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public Integer getAppApplicationId() {
		return appApplicationId;
	}
	public void setAppApplicationId(Integer appApplicationId) {
		this.appApplicationId = appApplicationId;
	}
	public HelpSupportDto populateHelpSupportDto(HelpSupport helpSupport) {
		HelpSupportDto helpSupportDto = new HelpSupportDto();
		try {
			BeanUtils.copyProperties(helpSupport, helpSupportDto);
			helpSupportDto.setCreatedAt(helpSupport.getCreatedAt());
			helpSupportDto.setUpdatedAt(helpSupport.getUpdatedAt());

		} catch (Exception e) {
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);
		}
		return helpSupportDto;

		
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public Date getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
	
	public AppTester populateAppTesterDto(AppTesterDto appTesterDto) {
		AppTester appTester = new AppTester();
		try {
			BeanUtils.copyProperties(appTesterDto, appTester);
			
		//	appTester.setApplication(appTesterDto.);
			
		} catch (Exception e) {
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);
		}
		return appTester;

		
	}
		
	
		
}
