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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.bitvault.appstore.cloud.constant.ErrorCode;
import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
 * The persistent class for the app_category database table.
 * 
 */
@Entity
@Table(name = "app_category")
@NamedQuery(name = "AppCategory.findAll", query = "SELECT a FROM AppCategory a")
//@EntityListeners(AuditingEntityListener.class)
public class AppCategory extends Auditable<String> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "app_category_id", unique = true, nullable = false)
	private Integer appCategoryId;

	@Column(name = "category_count", nullable = false)
	private Integer categoryCount;

	@Column(name = "category_type", nullable = false, length = 255)
	private String categoryType;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCategoryIconUrl() {
		return categoryIconUrl;
	}

	public void setCategoryIconUrl(String categoryIconUrl) {
		this.categoryIconUrl = categoryIconUrl;
	}

	@Column(name = "description", nullable = false, length = 255)
	private String description;

	@Column(name = "category_icon_url", length = 255)
	private String categoryIconUrl;
	
	@Column(name = "category_banner_url", length = 255)
	private String categoryBannerUrl;

//	@CreatedDate
//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name = "created_at")
//	protected Date createdAt;
//
//	@Column(name = "created_by", length = 255)
//	private String createdBy;

	@Column(nullable = false, length = 1)
	private String status;

//	@LastModifiedDate
//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name = "updated_at")
//	protected Date updatedAt;
//
//	@Column(name = "updated_by", length = 255)
//	private String updatedBy;

	// bi-directional many-to-one association to AppApplication
	@OneToMany(mappedBy = "appCategory")
	@JsonIgnore
	@JsonBackReference("AppApplication-AppCategory")
	private List<AppApplication> appApplications;

	// bi-directional many-to-one association to Application
	@OneToMany(mappedBy = "appCategory")
	@JsonIgnore
	@JsonBackReference("Application-AppCategory")
	private List<Application> applications;

	// bi-directional many-to-one association to Application
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "app_type_id")
	@JsonIgnore
	@JsonManagedReference("ApplicationType-AppCategory")
	private ApplicationType applicationType;

	public ApplicationType getApplicationType() {
		return applicationType;
	}

	public void setApplicationType(ApplicationType applicationType) {
		this.applicationType = applicationType;
	}

	public void setAppCategoryId(Integer appCategoryId) {
		this.appCategoryId = appCategoryId;
	}

	public void setCategoryCount(Integer categoryCount) {
		this.categoryCount = categoryCount;
	}

	public AppCategory() {
	}
	
	

	public String getCategoryBannerUrl() {
		return categoryBannerUrl;
	}

	public void setCategoryBannerUrl(String categoryBannerUrl) {
		this.categoryBannerUrl = categoryBannerUrl;
	}

	public Integer getAppCategoryId() {
		return this.appCategoryId;
	}

	public void setAppCategoryId(int appCategoryId) {
		this.appCategoryId = appCategoryId;
	}

	public Integer getCategoryCount() {
		return this.categoryCount;
	}

	public void setCategoryCount(int categoryCount) {
		this.categoryCount = categoryCount;
	}

	public String getCategoryType() {
		return this.categoryType;
	}

	public void setCategoryType(String categoryType) {
		this.categoryType = categoryType;
	}

//	public Date getCreatedAt() {
//		return this.createdAt;
//	}
//
//	public void setCreatedAt(Date createdAt) {
//		this.createdAt = createdAt;
//	}
//
//	public String getCreatedBy() {
//		return this.createdBy;
//	}
//
//	public void setCreatedBy(String createdBy) {
//		this.createdBy = createdBy;
//	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

//	public Date getUpdatedAt() {
//		return this.updatedAt;
//	}
//
//	public void setUpdatedAt(Date updatedAt) {
//		this.updatedAt = updatedAt;
//	}
//
//	public String getUpdatedBy() {
//		return this.updatedBy;
//	}
//
//	public void setUpdatedBy(String updatedBy) {
//		this.updatedBy = updatedBy;
//	}

	public List<AppApplication> getAppApplications() {
		return this.appApplications;
	}

	public void setAppApplications(List<AppApplication> appApplications) {
		this.appApplications = appApplications;
	}

	public AppApplication addAppApplication(AppApplication appApplication) {
		getAppApplications().add(appApplication);
		appApplication.setAppCategory(this);

		return appApplication;
	}

	public AppApplication removeAppApplication(AppApplication appApplication) {
		getAppApplications().remove(appApplication);
		appApplication.setAppCategory(null);

		return appApplication;
	}

	public List<Application> getApplications() {
		return this.applications;
	}

	public void setApplications(List<Application> applications) {
		this.applications = applications;
	}

	public Application addApplication(Application application) {
		getApplications().add(application);
		application.setAppCategory(this);

		return application;
	}

	public Application removeApplication(Application application) {
		getApplications().remove(application);
		application.setAppCategory(null);

		return application;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (int) (prime * result + ((appCategoryId == null) ? 0 : appCategoryId.hashCode()));
		return result;
	}

	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (!(obj instanceof AppCategory))
			return false;
		AppCategory equalCheck = (AppCategory) obj;
		if ((appCategoryId == null && equalCheck.appCategoryId != null)
				|| (appCategoryId != null && equalCheck.appCategoryId == null))
			return false;
		if (appCategoryId != null && !appCategoryId.equals(equalCheck.appCategoryId))
			return false;
		return true;
	}

	public org.bitvault.appstore.cloud.dto.AppCategory populateAppCategoryDTO(AppCategory appCategory) {
		org.bitvault.appstore.cloud.dto.AppCategory appCategoryDTO = new org.bitvault.appstore.cloud.dto.AppCategory();
		try {
			BeanUtils.copyProperties(appCategory, appCategoryDTO);
			appCategoryDTO.setAppTypeId(appCategory.getApplicationType().getAppTypeId());
		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED, ErrorCode.SOME_ERROR_OCCURED_CODE);
		}
		return appCategoryDTO;
	}
}