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
 * The persistent class for the app_detail database table.
 * 
 */
@Entity
@Table(name = "app_detail")
@NamedQuery(name = "AppDetail.findAll", query = "SELECT a FROM AppDetail a")
public class AppDetail extends Auditable<String> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "app_detail_id", unique = true, nullable = false)
	private Integer appDetailId;

	@Column(name = "apk_url", nullable = false, length = 255)
	private String apkUrl;

	@Column(name = "app_icon_url", nullable = false, length = 255)
	private String appIconUrl;

	@Column(name = "app_version_name", nullable = false, length = 255)
	private String appVersionName;



	@Column(name = "app_version_no", nullable = false)
	private Integer appVersionNo;

	// @Temporal(TemporalType.TIMESTAMP)
	// @Column(name = "created_at", nullable = false)
	// private Calendar createdAt;

	@Column(name = "full_description", nullable = false, length = 255)
	private String fullDescription;

	@Column(name = "short_description", nullable = false, length = 255)
	private String shortDescription;

	// @Temporal(TemporalType.TIMESTAMP)
	// @Column(name = "updated_at", nullable = false)
	// private Calendar updatedAt;

	@Column(name = "whats_new", nullable = false, length = 255)
	private String whatsNew;

	
	@Column(name = "status", nullable = false, length = 255)
	private String status;
	
	// bi-directional many-to-one association to AppCrashLog
	@OneToMany(mappedBy = "appDetail")
	@JsonIgnore
	@JsonBackReference("AppCrashLog-AppDetail")
	private List<AppCrashLog> appCrashLogs;

	// bi-directional many-to-one association to AppApplication
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "application_id", nullable = false)
	@JsonIgnore
	@JsonManagedReference("AppApplication-AppDetail")
	private AppApplication appApplication;

	// bi-directional many-to-one association to AppPurchase
	@OneToMany(mappedBy = "appDetail")
	@JsonIgnore
	@JsonBackReference("AppPurchase-AppDetail")
	private List<AppPurchase> appPurchases;

	// bi-directional many-to-one association to MobileUserAppData
	@OneToMany(mappedBy = "appDetail")
	@JsonIgnore
	@JsonBackReference("MobileUserAppData-AppDetail")
	private List<MobileUserAppData> mobileUserAppData;

	// bi-directional many-to-one association to RejectedApp
	@OneToMany(mappedBy = "appDetail")
	@JsonIgnore
	@JsonBackReference("RejectedApp-AppDetail")
	private List<RejectedApp> rejectedApps;

	public AppDetail() {
	}

	public Integer getAppDetailId() {
		return this.appDetailId;
	}

	public void setAppDetailId(Integer appDetailId) {
		this.appDetailId = appDetailId;
	}

	public String getApkUrl() {
		return this.apkUrl;
	}

	public void setApkUrl(String apkUrl) {
		this.apkUrl = apkUrl;
	}

	public String getAppIconUrl() {
		return this.appIconUrl;
	}

	public void setAppIconUrl(String appIconUrl) {
		this.appIconUrl = appIconUrl;
	}
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	public String getAppVersionName() {
		return this.appVersionName;
	}

	public void setAppVersionName(String appVersionName) {
		this.appVersionName = appVersionName;
	}

	public Integer getAppVersionNo() {
		return this.appVersionNo;
	}

	public void setAppVersionNo(Integer appVersionNo) {
		this.appVersionNo = appVersionNo;
	}

	public String getFullDescription() {
		return this.fullDescription;
	}

	public void setFullDescription(String fullDescription) {
		this.fullDescription = fullDescription;
	}

	public String getShortDescription() {
		return this.shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	

	public String getWhatsNew() {
		return whatsNew;
	}

	public void setWhatsNew(String whatsNew) {
		this.whatsNew = whatsNew;
	}


	public List<AppCrashLog> getAppCrashLogs() {
		return this.appCrashLogs;
	}

	public void setAppCrashLogs(List<AppCrashLog> appCrashLogs) {
		this.appCrashLogs = appCrashLogs;
	}

	public AppCrashLog addAppCrashLog(AppCrashLog appCrashLog) {
		getAppCrashLogs().add(appCrashLog);
		appCrashLog.setAppDetail(this);

		return appCrashLog;
	}

	public AppCrashLog removeAppCrashLog(AppCrashLog appCrashLog) {
		getAppCrashLogs().remove(appCrashLog);
		appCrashLog.setAppDetail(null);

		return appCrashLog;
	}

	public AppApplication getAppApplication() {
		return this.appApplication;
	}

	public void setAppApplication(AppApplication appApplication) {
		this.appApplication = appApplication;
	}

	public List<AppPurchase> getAppPurchases() {
		return this.appPurchases;
	}

	public void setAppPurchases(List<AppPurchase> appPurchases) {
		this.appPurchases = appPurchases;
	}

	public AppPurchase addAppPurchas(AppPurchase appPurchas) {
		getAppPurchases().add(appPurchas);
		appPurchas.setAppDetail(this);

		return appPurchas;
	}

	public AppPurchase removeAppPurchas(AppPurchase appPurchas) {
		getAppPurchases().remove(appPurchas);
		appPurchas.setAppDetail(null);

		return appPurchas;
	}

	public List<MobileUserAppData> getMobileUserAppData() {
		return this.mobileUserAppData;
	}

	public void setMobileUserAppData(List<MobileUserAppData> mobileUserAppData) {
		this.mobileUserAppData = mobileUserAppData;
	}

	public MobileUserAppData addMobileUserAppData(MobileUserAppData mobileUserAppData) {
		getMobileUserAppData().add(mobileUserAppData);
		mobileUserAppData.setAppDetail(this);

		return mobileUserAppData;
	}

	public MobileUserAppData removeMobileUserAppData(MobileUserAppData mobileUserAppData) {
		getMobileUserAppData().remove(mobileUserAppData);
		mobileUserAppData.setAppDetail(null);

		return mobileUserAppData;
	}

	public List<RejectedApp> getRejectedApps() {
		return this.rejectedApps;
	}

	public void setRejectedApps(List<RejectedApp> rejectedApps) {
		this.rejectedApps = rejectedApps;
	}

	public RejectedApp addRejectedApp(RejectedApp rejectedApp) {
		getRejectedApps().add(rejectedApp);
		rejectedApp.setAppDetail(this);

		return rejectedApp;
	}

	public RejectedApp removeRejectedApp(RejectedApp rejectedApp) {
		getRejectedApps().remove(rejectedApp);
		rejectedApp.setAppDetail(null);

		return rejectedApp;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (int) (prime * result + ((appDetailId == null) ? 0 : appDetailId.hashCode()));
		return result;
	}

	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (!(obj instanceof AppDetail))
			return false;
		AppDetail equalCheck = (AppDetail) obj;
		if ((appDetailId == null && equalCheck.appDetailId != null)
				|| (appDetailId != null && equalCheck.appDetailId == null))
			return false;
		if (appDetailId != null && !appDetailId.equals(equalCheck.appDetailId))
			return false;
		return true;
	}

	public org.bitvault.appstore.cloud.dto.AppDetail populateAppDetailDto(AppDetail appdetail)
			throws BitVaultException {
		org.bitvault.appstore.cloud.dto.AppDetail appDetailDTO = new org.bitvault.appstore.cloud.dto.AppDetail();
		try {
			BeanUtils.copyProperties(appdetail, appDetailDTO);
			appDetailDTO.setApplicationId(appdetail.getAppApplication().getAppApplicationId());

		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED, ErrorCode.SOME_ERROR_OCCURED_CODE);
		}

		return appDetailDTO;
	}

}