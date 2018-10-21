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
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.bitvault.appstore.cloud.constant.ErrorCode;
import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.springframework.beans.BeanUtils;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Mapping;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The persistent class for the app_application database table.
 * 
 */
@Entity
@Table(name = "app_application")
@NamedQuery(name = "AppApplication.findAll", query = "SELECT a FROM AppApplication a")
@Document(indexName = "app_store", type = "app_Application")
@Mapping(mappingPath = "/mappings/mappings.json")
public class AppApplication extends Auditable<String> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@org.springframework.data.annotation.Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonProperty("app_application_id")
	@Column(name = "app_application_id", unique = true)
	private Integer appApplicationId;

	@Column(name = "app_name", length = 255)
	@JsonProperty("app_name")
	private String appName;

	@Column(name = "language", length = 255)
	private String language;

	@Column(name = "title", length = 255)
	private String title;

	@Column(name = "reason", length = 255)
	private String reason;

	@Column(name = "company", length = 255)
	private String company;

	@Column(name = "app_permission", length = 255)
	@JsonProperty("app_permission")
	private String appPermission;

	@Column(name = "app_icon_url", length = 255)
	@JsonProperty("app_icon_url")
	private String appIconUrl;

	@Column(name = "update_type", length = 255)
	@JsonProperty("update_type")
	private String updateType;

	@Column(name = "app_price")
	@JsonProperty("app_price")
	private float appPrice;

	@Column(name = "average_rating")
	@JsonProperty("average_rating")
	private float averageRating;

	// // @Temporal(TemporalType.TIMESTAMP)
	// @CreatedDate
	// @Column(name = "created_at")
	// private Date createdAt;

	// @Column(name = "created_by", length = 255)
	// private String createdBy;

	@Column(nullable = false, length = 255)
	private String email;

	@Column(name = "latest_version_name", length = 255)
	@JsonProperty("latest_version_name")
	private String latestVersionName;

	@Column(name = "latest_version_no")
	@JsonProperty("latest_version_no")
	private Integer latestVersionNo;

	@Column(name = "package_name", length = 255)
	@JsonProperty("package_name")
	private String packageName;

	@Column(name = "privacy_policy_url", length = 255)
	@JsonProperty("privacy_policy_url")
	private String privacyPolicyUrl;

	@Column(name = "signed_key", length = 255)
	@JsonProperty("signed_key")
	private String signedKey;

	@Column(nullable = false, length = 1)
	private String status;

	@Column(nullable = false)
	private int subscription;

	// @Temporal(TemporalType.TIMESTAMP)
	// @LastModifiedDate
	// @Column(name = "updated_at")
	// private Date updatedAt;
	//
	// @Column(name = "updated_by", length = 255)
	// private String updatedBy;

	@Column(name = "user_id", length = 255)
	@JsonProperty("userid")
	private String userId;

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

	@Column(name = "apk_url", length = 255)
	@JsonProperty("apk_url")
	private String apkUrl;

	@Column(name = "website", length = 255)
	private String website;

	// bi-directional many-to-one association to AppCategory
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "app_category_id")
	@JsonIgnore
	@JsonManagedReference("AppCategory-AppApplication")
	private AppCategory appCategory;

	// bi-directional many-to-one association to AppCategory
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "app_type_id")
	@JsonIgnore
	@JsonManagedReference("ApplicationType-AppApplication")
	private ApplicationType applicationType;

	// bi-directional many-to-one association to AppDetail
	@OneToMany(mappedBy = "appApplication")
	@JsonIgnore
	@JsonBackReference("AppApplication-AppDetail")
	private List<AppDetail> appDetails;

	// bi-directional many-to-one association to AppHistory
	@OneToMany(mappedBy = "appApplication")
	@JsonIgnore
	@JsonBackReference("AppHistory-AppApplication")
	private List<AppHistory> appHistories;

	// bi-directional many-to-one association to AppImage
	@OneToMany(mappedBy = "appApplication")
	@JsonIgnore
	@JsonBackReference("AppImage-AppApplication")
	private List<AppImage> appImages;

	@OneToMany(mappedBy = "application")
	@JsonIgnore
	@JsonBackReference("MobileUserApp-AppApplication")
	private List<MobileUserApp> mobileUserApps;

	public List<MobileUserApp> getMobileUserApps() {
		return mobileUserApps;
	}

	public void setMobileUserApps(List<MobileUserApp> mobileUserApps) {
		this.mobileUserApps = mobileUserApps;
	}

	// bi-directional many-to-one association to AppRateReview
	@OneToMany(mappedBy = "appApplication")
	@JsonIgnore
	@JsonBackReference("AppRateReview-AppApplication")
	private List<AppRateReview> appRateReviews;
	@OneToMany(mappedBy = "application")
	@JsonIgnore
	@JsonBackReference("AppTester-AppApplication")
	private List<AppTester> appTester;

	// bi-directional one-to-one association to Application
	@OneToOne(mappedBy = "appApplication", fetch = FetchType.LAZY)
	@JsonIgnore
	@JsonManagedReference("Application-AppApplication")
	private Application application;

	// bi-directional one-to-one association to Application
	@OneToOne(mappedBy = "application", fetch = FetchType.LAZY)
	@JsonIgnore
	@JsonManagedReference("AppStatistic-AppApplication")
	private AppStatistic appStatistic;

	public List<AppRateReview> getAppRateReviews() {
		return appRateReviews;
	}

	public void setAppRateReviews(List<AppRateReview> appRateReviews) {
		this.appRateReviews = appRateReviews;
	}

	public AppStatistic getAppStatistic() {
		return appStatistic;
	}

	public void setAppStatistic(AppStatistic appStatistic) {
		this.appStatistic = appStatistic;
	}

	public AppApplication() {
	}

	public String getAppIconUrl() {
		return this.appIconUrl;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
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

	public String getLanguage() {
		return language;
	}

	public Integer getAppApplicationId() {
		return appApplicationId;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getAppName() {
		return appName;
	}

	public String getApkUrl() {
		return apkUrl;
	}

	public void setApkUrl(String apkUrl) {
		this.apkUrl = apkUrl;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public void setAppApplicationId(Integer appApplicationId) {
		this.appApplicationId = appApplicationId;
	}

	public ApplicationType getApplicationType() {
		return applicationType;
	}

	public void setApplicationType(ApplicationType applicationType) {
		this.applicationType = applicationType;
	}

	public void setAppIconUrl(String appIconUrl) {
		this.appIconUrl = appIconUrl;
	}

	public float getAppPrice() {
		return this.appPrice;
	}

	public void setAppPrice(float appPrice) {
		this.appPrice = appPrice;
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

	public String getLatestVersionName() {
		return this.latestVersionName;
	}

	public void setLatestVersionName(String latestVersionName) {
		this.latestVersionName = latestVersionName;
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

	public String getSignedKey() {
		return this.signedKey;
	}

	public void setSignedKey(String signedKey) {
		this.signedKey = signedKey;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public AppCategory getAppCategory() {
		return this.appCategory;
	}

	public void setAppCategory(AppCategory appCategory) {
		this.appCategory = appCategory;
	}

	public List<AppDetail> getAppDetails() {
		return this.appDetails;
	}

	public void setAppDetails(List<AppDetail> appDetails) {
		this.appDetails = appDetails;
	}

	public AppDetail addAppDetail(AppDetail appDetail) {
		getAppDetails().add(appDetail);
		appDetail.setAppApplication(this);

		return appDetail;
	}

	public AppDetail removeAppDetail(AppDetail appDetail) {
		getAppDetails().remove(appDetail);
		appDetail.setAppApplication(null);

		return appDetail;
	}

	public List<AppHistory> getAppHistories() {
		return this.appHistories;
	}

	public void setAppHistories(List<AppHistory> appHistories) {
		this.appHistories = appHistories;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

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

	public AppHistory addAppHistory(AppHistory appHistory) {
		getAppHistories().add(appHistory);
		appHistory.setAppApplication(this);

		return appHistory;
	}

	public AppHistory removeAppHistory(AppHistory appHistory) {
		getAppHistories().remove(appHistory);
		appHistory.setAppApplication(null);

		return appHistory;
	}

	public List<AppImage> getAppImages() {
		return this.appImages;
	}

	public void setAppImages(List<AppImage> appImages) {
		this.appImages = appImages;
	}

	public AppImage addAppImage(AppImage appImage) {
		getAppImages().add(appImage);
		appImage.setAppApplication(this);

		return appImage;
	}

	public AppImage removeAppImage(AppImage appImage) {
		getAppImages().remove(appImage);
		appImage.setAppApplication(null);

		return appImage;
	}

	public Application getApplication() {
		return this.application;
	}

	public void setApplication(Application application) {
		this.application = application;
	}

	public org.bitvault.appstore.cloud.dto.AppApplication populateAppApplicationDTO(AppApplication appApplication) {
		org.bitvault.appstore.cloud.dto.AppApplication appDTO = new org.bitvault.appstore.cloud.dto.AppApplication();
		try {
			BeanUtils.copyProperties(appApplication, appDTO);
			AppCategory appCategory = appApplication.getAppCategory();
			ApplicationType appType = appApplication.getApplicationType();
			appCategory.setApplicationType(appType);
			appDTO.setApplicationType(appType.populateAppTypeDto(appType));
			appDTO.setAppCategory(appCategory.populateAppCategoryDTO(appCategory));
			appDTO.setAppPermission(appApplication.getAppPermission());

		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED, ErrorCode.SOME_ERROR_OCCURED_CODE);
		}
		return appDTO;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (int) (prime * result + ((appApplicationId == null) ? 0 : appApplicationId.hashCode()));
		return result;
	}

	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (!(obj instanceof AppApplication))
			return false;
		AppApplication equalCheck = (AppApplication) obj;
		if ((appApplicationId == null && equalCheck.appApplicationId != null)
				|| (appApplicationId != null && equalCheck.appApplicationId == null))
			return false;
		if (appApplicationId != null && !appApplicationId.equals(equalCheck.appApplicationId))
			return false;
		return true;
	}

	public org.bitvault.appstore.cloud.dto.ApplicationElasticDto populateApplicationElasticDTO(AppApplication app) {
		org.bitvault.appstore.cloud.dto.ApplicationElasticDto appDTO = new org.bitvault.appstore.cloud.dto.ApplicationElasticDto();

		try {
			BeanUtils.copyProperties(app, appDTO);
			appDTO.setAppApplicationId(app.getAppApplicationId());
			appDTO.setCreatedAt(app.getCreatedAt());
			appDTO.setUpdatedAt(app.getUpdatedAt());
		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED, ErrorCode.SOME_ERROR_OCCURED_CODE);
		}
		return appDTO;
	}

}