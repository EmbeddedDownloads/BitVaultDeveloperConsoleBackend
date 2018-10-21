package org.bitvault.appstore.cloud.dto;

import java.util.Date;

public class AverageRatingChartDto {

	private Double average;

	private String label = "";
	private Date date;

	public AverageRatingChartDto(Double average, String label, Date date) {
		this.average = average;
		this.label = label;
		this.date = date;
	}

	public AverageRatingChartDto() {
	}

	public Double getAverage() {
		return average;
	}

	public void setAverage(Double average) {
		this.average = average;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
