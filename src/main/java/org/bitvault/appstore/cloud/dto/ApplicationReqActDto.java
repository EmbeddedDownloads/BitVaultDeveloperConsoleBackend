package org.bitvault.appstore.cloud.dto;

public class ApplicationReqActDto {
	private Integer appApplicationId;
	private String appName;
	private String latestVersionName;

	private float latestVersionNo;

	public Integer getAppApplicationId() {
		return appApplicationId;
	}

	public void setAppApplicationId(Integer appApplicationId) {
		this.appApplicationId = appApplicationId;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getLatestVersionName() {
		return latestVersionName;
	}

	public void setLatestVersionName(String latestVersionName) {
		this.latestVersionName = latestVersionName;
	}

	public float getLatestVersionNo() {
		return latestVersionNo;
	}

	public void setLatestVersionNo(float latestVersionNo) {
		this.latestVersionNo = latestVersionNo;
	}

}
