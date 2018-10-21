package org.bitvault.appstore.cloud.dto;

public class StarCountDto {

	private Integer count;
	private Integer rating;

	StarCountDto(Integer count, Integer rating) {
		this.count = count;
		this.rating = rating;

	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

}
