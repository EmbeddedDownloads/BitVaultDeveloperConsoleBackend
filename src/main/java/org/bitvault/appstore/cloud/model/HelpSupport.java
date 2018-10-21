package org.bitvault.appstore.cloud.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.dto.HelpSupportDto;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.springframework.beans.BeanUtils;

@Entity
@Table(name = "help_support")
@NamedQuery(name = "HelpSupport.findAll", query = "SELECT u FROM HelpSupport u")
public class HelpSupport extends Auditable<String> implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "help_support_id", unique = true)
	private Integer helpSupportId;
	@Column(name = "from_add", nullable = false, length = 50)
	private String fromAdd;
	@Column(name = "message", nullable = true, length = 50)
	private String message;
	@Column(name = "phone_no", nullable = true, length = 50)
	private String phoneNo;
	@Column(name = "type", nullable = false, length = 50)
	private String type;
	@Column(name="image_url",nullable = false, length = 50)
    private String imageUrl; 
  	public Integer getHelpSupportId() {
		return helpSupportId;
	}

	public void setHelpSupportId(Integer helpSupportId) {
		this.helpSupportId = helpSupportId;
	}

	

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
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

}
