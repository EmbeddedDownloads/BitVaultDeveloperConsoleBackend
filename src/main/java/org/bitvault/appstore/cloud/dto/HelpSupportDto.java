package org.bitvault.appstore.cloud.dto;



import java.util.Date;

import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.bitvault.appstore.cloud.model.HelpSupport;
import org.springframework.beans.BeanUtils;

public class HelpSupportDto {
	
	private static final long serialVersionUID = 1L;

	private Integer helpSupportId;
	private String fromAdd;
	private String message;
	private String phoneNo;
	private String type;
	private Date createdAt;
	private Date updatedAt;
	private String imageUrl;
 	
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public Integer getHelpSupportId() {
		return helpSupportId;
	}
	public void setHelpSupportId(Integer helpSupportId) {
		this.helpSupportId = helpSupportId;
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

	public String getFromAdd() {
		return fromAdd;
	}
	public void setFromAdd(String fromAdd) {
		this.fromAdd = fromAdd;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
	public HelpSupport populateHelpSupport(HelpSupportDto helpSupportDto) {
		HelpSupport helpSupport = new HelpSupport();
		try {
			BeanUtils.copyProperties(helpSupportDto, helpSupport);
			
		} catch (Exception e) {
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);
		}
		return helpSupport;

	}
		
	
	
	
}
