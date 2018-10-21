package org.bitvault.appstore.cloud.dto;

import java.util.List;

public class UserActivityResultDto {

	private String userId ;
	private String userName;
	private String email ;
	private List<UserData> data ;
	
		
	
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public List<UserData> getData() {
		return data;
	}

	public void setData(List<UserData> data) {
		this.data = data;
	}
}
