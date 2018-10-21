package org.bitvault.appstore.cloud.utils;

public class Response {

	private String status;
	private Object result;
	private int totalPages = 0;
	private long totalRecords = 0;
	private int size = 0;
	private String sort;
	private String userName ;

	public Response(String status, Object result, int totalPages, long totalRecords, int size, String sort) {
		this.status = status;
		this.result = result;
		this.totalPages = totalPages;
		this.totalRecords = totalRecords;
		this.size = size;
		this.sort = sort;
	}

	public Response() {

	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public long getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(long totalRecords) {
		this.totalRecords = totalRecords;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	
}
