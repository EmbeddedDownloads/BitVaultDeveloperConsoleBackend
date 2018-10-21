package org.bitvault.appstore.mobile.dto;

public class ApplicationLatestVersionDTO {
	private Integer applicationId;
	private Integer latestVersionNo;
	private String packageName;
	private float averageRating;

	public ApplicationLatestVersionDTO(Integer applicationId, Integer latestVersionNo, String packageName,
			float averageRating) {

		this.applicationId = applicationId;
		this.latestVersionNo = latestVersionNo;
		this.packageName = packageName;
		this.averageRating = averageRating;
	}

	public Integer getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(Integer applicationId) {
		this.applicationId = applicationId;
	}

	public Integer getLatestVersionNo() {
		return latestVersionNo;
	}

	public void setLatestVersionNo(Integer latestVersionNo) {
		this.latestVersionNo = latestVersionNo;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public float getAverageRating() {
		return averageRating;
	}

	public void setAverageRating(float averageRating) {
		this.averageRating = averageRating;
	}

}
