package org.bitvault.appstore.cloud.dto;

import java.util.List;

public class NotificationData {

	private String web_server_key ;
	private String tag ;
	private List<String> public_address ;
	private NotificationAdditionalProrerties data ;
	
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public List<String> getPublic_address() {
		return public_address;
	}
	public void setPublic_address(List<String> public_address) {
		this.public_address = public_address;
	}
	public NotificationAdditionalProrerties getData() {
		return data;
	}
	public void setData(NotificationAdditionalProrerties data) {
		this.data = data;
	}
	public String getWeb_server_key() {
		return web_server_key;
	}
	public void setWeb_server_key(String web_server_key) {
		this.web_server_key = web_server_key;
	}
}
