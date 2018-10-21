package org.bitvault.appstore.cloud.dto;

public class NotificationAdditionalProrerties {

	private String app_icon_url ;
	private String app_name ;
	private String package_name ;
	private String app_id ;
	private String updated_on;
	private long app_size;
	
	
	
	public NotificationAdditionalProrerties(String app_icon_url, String app_name, String package_name, String app_id,
			String updated_on, long app_size) {
		super();
		this.app_icon_url = app_icon_url;
		this.app_name = app_name;
		this.package_name = package_name;
		this.app_id = app_id;
		this.updated_on = updated_on;
		this.app_size = app_size;
	}
	public String getApp_icon_url() {
		return app_icon_url;
	}
	public void setApp_icon_url(String app_icon_url) {
		this.app_icon_url = app_icon_url;
	}
	public String getApp_name() {
		return app_name;
	}
	public void setApp_name(String app_name) {
		this.app_name = app_name;
	}
	public String getPackage_name() {
		return package_name;
	}
	public void setPackage_name(String package_name) {
		this.package_name = package_name;
	}
	public String getApp_id() {
		return app_id;
	}
	public void setApp_id(String app_id) {
		this.app_id = app_id;
	}
	
	
	
	public String getUpdated_on() {
		return updated_on;
	}
	public void setUpdated_on(String updated_on) {
		this.updated_on = updated_on;
	}
	public long getApp_size() {
		return app_size;
	}
	public void setApp_size(long app_size) {
		this.app_size = app_size;
	}
	
	
	
}
