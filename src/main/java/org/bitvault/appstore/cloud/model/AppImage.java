package org.bitvault.appstore.cloud.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.bitvault.appstore.cloud.constant.ErrorCode;
import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
 * The persistent class for the app_images database table.
 * 
 */
@Entity
@Table(name = "app_images")
@NamedQuery(name = "AppImage.findAll", query = "SELECT a FROM AppImage a")
public class AppImage extends Auditable<String> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "app_images_id", unique = true, nullable = false)
	private Integer appImagesId;

	@Column(name = "app_image_url", nullable = false, length = 255)
	private String appImageUrl;
	
	@Column(name = "image_type", nullable = false, length = 255)
	private String imageType;
	
	@Column(name = "status", nullable = false, length = 255)
	private String status;
	


	// bi-directional many-to-one association to AppApplication
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "application_id", nullable = false)
	@JsonIgnore
	@JsonManagedReference("AppApplication-AppImage")
	private AppApplication appApplication;

	public AppImage() {
	}

	public Integer getAppImagesId() {
		return this.appImagesId;
	}

	public void setAppImagesId(Integer appImagesId) {
		this.appImagesId = appImagesId;
	}

	

	public String getImageType() {
		return imageType;
	}

	public void setImageType(String imageType) {
		this.imageType = imageType;
	}

	public String getAppImageUrl() {
		return this.appImageUrl;
	}

	public void setAppImageUrl(String appImageUrl) {
		this.appImageUrl = appImageUrl;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public AppApplication getAppApplication() {
		return this.appApplication;
	}

	public void setAppApplication(AppApplication appApplication) {
		this.appApplication = appApplication;
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (int) (prime * result + ((appImagesId == null) ? 0 : appImagesId.hashCode()));
		return result;
	}

	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (!(obj instanceof AppImage))
			return false;
		AppImage equalCheck = (AppImage) obj;
		if ((appImagesId == null && equalCheck.appImagesId != null)
				|| (appImagesId != null && equalCheck.appImagesId == null))
			return false;
		if (appImagesId != null && !appImagesId.equals(equalCheck.appImagesId))
			return false;
		return true;
	}
	
	
	public org.bitvault.appstore.cloud.dto.AppImage populateAppImage(AppImage appImage) throws BitVaultException {
		org.bitvault.appstore.cloud.dto.AppImage appImageDTO = new org.bitvault.appstore.cloud.dto.AppImage();
		try {
			BeanUtils.copyProperties(appImage, appImageDTO);
			appImageDTO.setApplicationId(appImage.getAppApplication().getAppApplicationId());
		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED, ErrorCode.SOME_ERROR_OCCURED_CODE);
		}

		return appImageDTO;

	}

}