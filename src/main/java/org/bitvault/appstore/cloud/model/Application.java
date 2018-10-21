package org.bitvault.appstore.cloud.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.bitvault.appstore.cloud.constant.ErrorCode;
import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.springframework.beans.BeanUtils;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The persistent class for the application database table.appDto
 * 
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "application")
@NamedQuery(name = "Application.findAll", query = "SELECT a FROM Application a")
@Document(indexName="appstore_cloud",type="application")
public class Application extends Auditable<String> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@org.springframework.data.annotation.Id
	
	@Column(name = "application_id", unique = true, nullable = false)
	@JsonProperty("application_id")
	private Integer applicationId;

	@Column(name = "app_name", nullable = false, length = 255)
	@JsonProperty("app_name")
	private String appName;

	@Column(name = "language", nullable = false, length = 255)
	
	private String language;

	@Column(name = "title", length = 255)
	
	private String title;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "update_type", length = 255)
	
	private String updateType;

	@Column(name = "app_icon_url", length = 255)
	@JsonProperty("app_icon_url")
	private String appIconUrl;

	@Column(name = "apk_file_size", length = 255)
	@JsonProperty("apk_file_size")
	private long apkFilesize;

	@Column(name = "total_install", length = 255)
	@JsonProperty("total_install")
	private Integer totalInstall;

	@Column(name = "total_uninstall", length = 255)
	@JsonProperty("total_uninstall")
	private Integer totalUninstall;

	@Column(name = "total_downloads", length = 255)
	@JsonProperty("total_downloads")
	private Integer totalDownloads;
	

	public long getApkFilesize() {
		return apkFilesize;
	}

	public void setApkFilesize(long apkFilesize) {
		this.apkFilesize = apkFilesize;
	}

	public Integer getTotalInstall() {
		return totalInstall;
	}

	public void setTotalInstall(Integer totalInstall) {
		this.totalInstall = totalInstall;
	}

	public Integer getTotalUninstall() {
		return totalUninstall;
	}

	public void setTotalUninstall(Integer totalUninstall) {
		this.totalUninstall = totalUninstall;
	}

	public Integer getTotalDownloads() {
		return totalDownloads;
	}

	public void setTotalDownloads(Integer totalDownloads) {
		this.totalDownloads = totalDownloads;
	}

	@Column(name = "apk_url", length = 255)
	@JsonProperty("apk_url")
	private String apkUrl;

	@Column(name = "app_price")
	@JsonProperty("app_price")
	private float appPrice;

	@Column(name = "company", length = 255)
	private String company;

	@Column(name = "app_permission", length = 255)
	@JsonProperty("app_permission")
	private String appPermission;

	@Column(name = "average_rating")
	private float averageRating;

	// @Temporal(TemporalType.TIMESTAMP)
//	@CreatedDate
//	@Column(name = "created_at", nullable = false)
//	private Calendar createdAt;

//	@Column(name = "created_by", nullable = false, length = 255)
//	private String createdBy;

	@Column(name = "email", length = 255)
	private String email;

	@Column(name = "status", length = 1)
	private String status;

	@Column(name = "latest_version", nullable = false, length = 50)
	@JsonProperty("latest_version")
	private String latestVersion;

	@Column(name = "latest_version_no", nullable = false)
	@JsonProperty("latest_version_no")
	private Integer latestVersionNo;

	@Column(name = "package_name", nullable = false, length = 255)
	@JsonProperty("package_name")
	private String packageName;

	@Column(name = "privacy_policy_url", length = 255)
	@JsonProperty("privacy_policy_url")
	private String privacyPolicyUrl;

	@Lob
	@Column(name = "signed_key")
	@JsonProperty("signed_key")
	private String signedKey;

	@Column(nullable = false)
	private int subscription;

	// @Temporal(TemporalType.TIMESTAMP)
//	@LastModifiedDate
//	@Column(name = "updated_at", nullable = false)
//	private Calendar updatedAt;
//
//	@Column(name = "updated_by", nullable = false, length = 255)
//	private String updatedBy;

	@Column(name = "user_id", length = 255)
	@JsonProperty("user_id")
	private String userId;

	@Column(nullable = false, length = 255)
	private String website;

	// bi-directional many-to-one association to AdminAppCharge
	@OneToMany(mappedBy = "application")
	@JsonIgnore
	@JsonBackReference("AdminAppCharge-Application")
	private List<AdminAppCharge> adminAppCharges;

	

	// bi-directional one-to-one association to AppApplication
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "application_id", nullable = false, insertable = false, updatable = false)
	@JsonIgnore
	@JsonBackReference("AppApplication-Application")
	private AppApplication appApplication;

	// bi-directional many-to-one association to AppCategory
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "appcategory_id", nullable = false)
	@JsonIgnore
	@JsonManagedReference("AppCategory-Application")
	private AppCategory appCategory;

	// bi-directional many-to-one association to MobileUserApp
	public ApplicationType getApplicationType() {
		return applicationType;
	}

	public void setApplicationType(ApplicationType applicationType) {
		this.applicationType = applicationType;
	}



	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "app_type_id")
	@JsonIgnore
	@JsonManagedReference("ApplicationType-Application")
	private ApplicationType applicationType;

	public Application() {
	}

	public Integer getApplicationId() {
		return this.applicationId;
	}

	public void setApplicationId(int applicationId) {
		this.applicationId = applicationId;
	}

	public String getAppIconUrl() {
		return this.appIconUrl;
	}

	public void setAppIconUrl(String appIconUrl) {
		this.appIconUrl = appIconUrl;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public float getAppPrice() {
		return this.appPrice;
	}

	public void setAppPrice(float appPrice) {
		this.appPrice = appPrice;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setApplicationId(Integer applicationId) {
		this.applicationId = applicationId;
	}

	public float getAverageRating() {
		return this.averageRating;
	}

	public void setAverageRating(float averageRating) {
		this.averageRating = averageRating;
	}

	

	public String getUpdateType() {
		return updateType;
	}

	public void setUpdateType(String updateType) {
		this.updateType = updateType;
	}

	

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLatestVersion() {
		return this.latestVersion;
	}

	public void setLatestVersion(String latestVersion) {
		this.latestVersion = latestVersion;
	}

	public Integer getLatestVersionNo() {
		return this.latestVersionNo;
	}

	public void setLatestVersionNo(Integer latestVersionNo) {
		this.latestVersionNo = latestVersionNo;
	}

	public String getPackageName() {
		return this.packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getPrivacyPolicyUrl() {
		return this.privacyPolicyUrl;
	}

	public void setPrivacyPolicyUrl(String privacyPolicyUrl) {
		this.privacyPolicyUrl = privacyPolicyUrl;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getAppPermission() {
		return appPermission;
	}

	public void setAppPermission(String appPermission) {
		this.appPermission = appPermission;
	}

	public String getAppUrl() {
		return apkUrl;
	}

	public void setAppUrl(String appUrl) {
		this.apkUrl = appUrl;
	}

	public String getSignedKey() {
		return this.signedKey;
	}

	public void setSignedKey(String signedKey) {
		this.signedKey = signedKey;
	}

	public int getSubscription() {
		return this.subscription;
	}

	public void setSubscription(int subscription) {
		this.subscription = subscription;
	}

	

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getWebsite() {
		return this.website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public List<AdminAppCharge> getAdminAppCharges() {
		return this.adminAppCharges;
	}

	public void setAdminAppCharges(List<AdminAppCharge> adminAppCharges) {
		this.adminAppCharges = adminAppCharges;
	}

	public AdminAppCharge addAdminAppCharge(AdminAppCharge adminAppCharge) {
		getAdminAppCharges().add(adminAppCharge);
		adminAppCharge.setApplication(this);

		return adminAppCharge;
	}

	public AdminAppCharge removeAdminAppCharge(AdminAppCharge adminAppCharge) {
		getAdminAppCharges().remove(adminAppCharge);
		adminAppCharge.setApplication(null);

		return adminAppCharge;
	}


	public AppApplication getAppApplication() {
		return this.appApplication;
	}

	public void setAppApplication(AppApplication appApplication) {
		this.appApplication = appApplication;
	}

	public AppCategory getAppCategory() {
		return this.appCategory;
	}

	public void setAppCategory(AppCategory appCategory) {
		this.appCategory = appCategory;
	}
	
	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getApkUrl() {
		return apkUrl;
	}

	public void setApkUrl(String apkUrl) {
		this.apkUrl = apkUrl;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (int) (prime * result + ((applicationId == null) ? 0 : applicationId.hashCode()));
		return result;
	}

	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (!(obj instanceof Application))
			return false;
		Application equalCheck = (Application) obj;
		if ((applicationId == null && equalCheck.applicationId != null)
				|| (applicationId != null && equalCheck.applicationId == null))
			return false;
		if (applicationId != null && !applicationId.equals(equalCheck.applicationId))
			return false;
		return true;
	}

	public org.bitvault.appstore.cloud.dto.Application populateApplicationDTO(Application app) {
		org.bitvault.appstore.cloud.dto.Application appDTO = new org.bitvault.appstore.cloud.dto.Application();
		AppCategory appCategory = null;
		ApplicationType appType = null;
		try {
			BeanUtils.copyProperties(app, appDTO);
			appCategory = app.getAppCategory();
			appType = app.getApplicationType();
			appDTO.setAppCategory(appCategory.populateAppCategoryDTO(appCategory));
			appDTO.setApplicationType(appType.populateAppTypeDto(appType));
			appDTO.setAppPermission(app.getAppPermission());

		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED, ErrorCode.SOME_ERROR_OCCURED_CODE);
		}
		return appDTO;
	}
	
	public org.bitvault.appstore.cloud.model.AppApplication populateAppApplicationByApplication(org.bitvault.appstore.cloud.model.Application application) {
		org.bitvault.appstore.cloud.model.AppApplication appAppEntity = new org.bitvault.appstore.cloud.model.AppApplication();
//		org.bitvault.appstore.cloud.model.Application application = new org.bitvault.appstore.cloud.model.Application();
		try {
//			application = appDto.populateApplication(appDto);
			BeanUtils.copyProperties(application, appAppEntity);
			appAppEntity.setAppApplicationId(application.getApplicationId());
			appAppEntity.setAppCategory(application.getAppCategory());
			appAppEntity.setApplicationType(application.getApplicationType());
			appAppEntity.setLatestVersionName(application.getLatestVersion());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return appAppEntity;
	}
	public org.bitvault.appstore.cloud.dto.ApplicationElasticDto populateApplicationElasticDTO(Application app) {
		org.bitvault.appstore.cloud.dto.ApplicationElasticDto appDTO = new org.bitvault.appstore.cloud.dto.ApplicationElasticDto();
		
		try {
			BeanUtils.copyProperties(app, appDTO);
			appDTO.setApplicationId(app.getApplicationId());
			appDTO.setLatestVersionName(app.getLatestVersion());
			appDTO.setCreatedAt(app.getCreatedAt());
			appDTO.setUpdatedAt(app.getUpdatedAt());
			
		} catch (Exception e) {
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED, ErrorCode.SOME_ERROR_OCCURED_CODE);
		}
		return appDTO;
	}
}