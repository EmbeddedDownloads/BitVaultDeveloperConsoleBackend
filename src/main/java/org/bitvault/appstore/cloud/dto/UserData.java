package org.bitvault.appstore.cloud.dto;

import java.util.Date;
import java.util.Map;

public class UserData {
	private Date date;
	private String type ;
	Map<String,String> updates ;
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Map<String, String> getUpdates() {
		return updates;
	}
	public void setUpdates(Map<String, String> updates) {
		this.updates = updates;
	}
}
