package org.bitvault.appstore.cloud.dto;

import java.util.Date;

public class ChartStatsDto {

	private long count;
	private String label = "";
	private Date date;

	public ChartStatsDto() {
	}

	public ChartStatsDto(long count, String label, Date date) {
		this.count = count;
		this.label = label;
		this.date = date;
	}

	
	
	
	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
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
