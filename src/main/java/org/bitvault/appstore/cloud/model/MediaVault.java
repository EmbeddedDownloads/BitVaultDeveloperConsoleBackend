package org.bitvault.appstore.cloud.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "mediavault")
public class MediaVault extends Auditable<String> implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6408525283431256382L;

	@Id
	@Column(name = "mediavault_hash_id")
	String id;
	
	@Column(name = "app_id")
	String appId;
	
	@Column(name = "wallet_address")
	String walletAddress;
	
	@Column(name = "filename")
	String filename;
	
	@Column(name = "type")
	String type;
	
	@Column(name = "enc_txn_id")
	String encTxnID;
	
	@Column(name = "enc_file_key")
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
