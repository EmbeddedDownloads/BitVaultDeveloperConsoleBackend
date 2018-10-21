package org.bitvault.appstore.cloud.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table (name = "app_addon_services")
public class AppAddonServices extends Auditable<String>{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "app_addon_services_id")
	private Integer appAddonServicesId;
	
	@OneToOne
	@JoinColumn(name = "addon_service_id")
//	@Column(name = "addon_service_id")
	private AddonService addonService;
	
	@Column(name = "user_id")
	private String userId;
	
	@Column(name = "web_server_key")
	private String webServerKey;
	
	@Column(name = "application_key")
	private String applicationKey;
	
	@Column(name = "package_name")
	private String packageName;
	
	@Column(name = "description")
	private String description;

	public Integer getAppAddonServicesId() {
		return appAddonServicesId;
	}

	public void setAppAddonServicesId(Integer appAddonServicesId) {
		this.appAddonServicesId = appAddonServicesId;
	}

	public AddonService getAddonService() {
		return addonService;
	}

	public void setAddonService(AddonService addonService) {
		this.addonService = addonService;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getWebServerKey() {
		return webServerKey;
	}

	public void setWebServerKey(String webServerKey) {
		this.webServerKey = webServerKey;
	}

	public String getApplicationKey() {
		return applicationKey;
	}

	public void setApplicationKey(String applicationKey) {
		this.applicationKey = applicationKey;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
