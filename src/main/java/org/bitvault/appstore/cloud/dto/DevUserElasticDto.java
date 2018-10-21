package org.bitvault.appstore.cloud.dto;

public class DevUserElasticDto {
	private String email;
	private String userId;
	private String username;
	private String status;
	private String avatarURL;
	private String signupAs;
	public String getAvatarURL() {
		return avatarURL;
	}

	public String getSignupAs() {
		return signupAs;
	}

	public void setSignupAs(String signupAs) {
		this.signupAs = signupAs;
	}

	public void setAvatarURL(String avatarURL) {
		this.avatarURL = avatarURL;
	}

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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
