package org.bitvault.appstore.cloud.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.bitvault.appstore.cloud.constant.ErrorCode;
import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.dto.ApplicationTypeDto;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
// @EntityListeners(AuditingEntityListener.class)
@Table(name = "application_type")
@NamedQuery(name = "ApplicationType.findAll", query = "SELECT a FROM ApplicationType a")
public class ApplicationType extends Auditable<String> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "app_type_id", unique = true)
	private Integer appTypeId;

	@Column(name = "app_type_name", length = 255)
	private String appTypeName;

	
	@Column(name = "app_type_icon", length = 255)
	private String appTypeIcon;
	
	
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "app_type_id")
	@JsonIgnore
	@JsonBackReference("AppApplication-ApplicationType")
	private List<AppApplication> appApplication;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "app_type_id")
	@JsonIgnore
	@JsonBackReference("Application-ApplicationType")
	private List<Application> application;

	@OneToMany(mappedBy = "applicationType")
	@JsonIgnore
	@JsonBackReference("AppCategory-ApplicationType")
	private List<AppCategory> appCategory;

	public List<AppCategory> getAppCategory() {
		return appCategory;
	}

	public void setAppCategory(List<AppCategory> appCategory) {
		this.appCategory = appCategory;
	}

	public List<Application> getApplication() {
		return application;
	}

	public void setApplication(List<Application> application) {
		this.application = application;
	}

	public List<AppApplication> getAppApplication() {
		return appApplication;
	}

	public void setAppApplication(List<AppApplication> appApplication) {
		this.appApplication = appApplication;
	}

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

	public ApplicationTypeDto populateAppTypeDto(ApplicationType appType) {
		ApplicationTypeDto applicationTypeDto = null;
		try {
			applicationTypeDto = new ApplicationTypeDto();
			try {
				BeanUtils.copyProperties(appType, applicationTypeDto);
			} catch (Exception e) {
				e.printStackTrace();
				throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED, ErrorCode.SOME_ERROR_OCCURED_CODE);
			}
			return applicationTypeDto;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return applicationTypeDto;
	}

	public String getAppTypeIcon() {
		return appTypeIcon;
	}

	public void setAppTypeIcon(String appTypeIcon) {
		this.appTypeIcon = appTypeIcon;
	}
	
	
}
