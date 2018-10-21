package org.bitvault.appstore.mobile.dto;

public class MediaVaultDto {

	String id;

	String appId;

	String walletAddress;

	String filename;

	String type;

	String encTxnID;

	String encFileKey;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getWalletAddress() {
		return walletAddress;
	}

	public void setWalletAddress(String walletAddress) {
		this.walletAddress = walletAddress;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getEncTxnID() {
		return encTxnID;
	}

	public void setEncTxnID(String encTxnID) {
		this.encTxnID = encTxnID;
	}

	public String getEncFileKey() {
		return encFileKey;
	}

	public void setEncFileKey(String encFileKey) {
		this.encFileKey = encFileKey;
	}

}
