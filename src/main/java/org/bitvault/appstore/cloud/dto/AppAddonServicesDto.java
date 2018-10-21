package org.bitvault.appstore.cloud.dto;

public class AppAddonServicesDto {
	
	private Integer addonServiceId;
	
	private Integer appAddonServicesId;
	

	public Integer getAppAddonServicesId() {
		return appAddonServicesId;
	}

	public void setAppAddonServicesId(Integer appAddonServicesId) {
		this.appAddonServicesId = appAddonServicesId;
	}

	public Integer getAddonServiceId() {
		return addonServiceId;
	}

	public void setAddonServiceId(Integer addonServiceId) {
		this.addonServiceId = addonServiceId;
	}

	private String addonServiceName;
	
	private String webServerKey;
	
	private String applicationKey;
	
	private String packageName;
	
	private String description;

	public String getAddonServiceName() {
		return addonServiceName;
	}

	public void setAddonServiceName(String addonServiceName) {
		this.addonServiceName = addonServiceName;
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
