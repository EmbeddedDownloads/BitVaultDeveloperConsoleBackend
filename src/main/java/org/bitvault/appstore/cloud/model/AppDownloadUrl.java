package org.bitvault.appstore.cloud.model;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "app_download_url")
@EntityListeners(AuditingEntityListener.class)
@NamedQuery(name = "AppDownloadUrl.findAll", query = "SELECT m FROM AppDownloadUrl m")
public class AppDownloadUrl implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "url_id", nullable = false)
	private String urlId;

	@Column(name = "url", nullable = false)
	private String url;

	@Column(name = "publicKey", nullable = false)
	private String publicKey;
	
	
	@LastModifiedDate
	@Column(name = "updated_at")
	private Calendar updatedAt;

	@CreatedDate
	@Column(name = "created_at")
	private Calendar createdAt;

	public String getUrlId() {
		return urlId;
	}

	public void setUrlId(String urlId) {
		this.urlId = urlId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Calendar getUpdatedAt() {
		return updatedAt;
	}
	
	

//	public void setUpdatedAt(Calendar updatedAt) {
//		this.updatedAt = updatedAt;
//	}

	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	public Calendar getCreatedAt() {
		return createdAt;
	}

//	public void setCreatedAt(Calendar createdAt) {
//		this.createdAt = createdAt;
//	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (int) (prime * result + ((urlId == null) ? 0 : urlId.hashCode()));
		return result;
	}

	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (!(obj instanceof AppDownloadUrl))
			return false;
		AppDownloadUrl equalCheck = (AppDownloadUrl) obj;
		if ((urlId == null && equalCheck.urlId != null) || (urlId != null && equalCheck.urlId == null))
			return false;
		if (urlId != null && !urlId.equals(equalCheck.urlId))
			return false;
		return true;
	}

	public org.bitvault.appstore.cloud.dto.AppDownloadUrl populateAppDownloadURLDTO(AppDownloadUrl appDownloadUrl) {
		org.bitvault.appstore.cloud.dto.AppDownloadUrl appDownloadURLDTO = new org.bitvault.appstore.cloud.dto.AppDownloadUrl();
		appDownloadURLDTO.setDownloadKey(appDownloadUrl.getUrlId());
		return appDownloadURLDTO;

	}
}
