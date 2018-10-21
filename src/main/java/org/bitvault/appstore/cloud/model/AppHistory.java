package org.bitvault.appstore.cloud.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.bitvault.appstore.cloud.dto.AppHistoryDto;
import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
 * The persistent class for the app_history database table.
 * 
 */
@Entity
@Table(name = "app_history")
@NamedQuery(name = "AppHistory.findAll", query = "SELECT a FROM AppHistory a")
public class AppHistory extends Auditable<String> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "history_id", unique = true, nullable = false)
	private Integer historyId;

	@Column(nullable = false, length = 255)
	private String status;

	@Column(name = "version_name", nullable = false, length = 255)
	private String versionName;

	@Column(name = "version_no", nullable = false)
	private Integer versionNo;

	public Integer getVersionNo() {
		return versionNo;
	}

	public void setVersionNo(Integer versionNo) {
		this.versionNo = versionNo;
	}

	@Column(name = "email", length = 255)
	private String email;

	@Column(name = "full_description",length = 255)
	private String fullDescription;

	@Column(name = "privacy_policy_url", length = 255)
	private String privacyPolicyUrl;
	
	@Column(name = "update_type", length = 255)
	private String updateType;
	

	@Column(name = "short_description", nullable = false, length = 255)
	private String shortDescription;

	@Column(name = "whats_new", nullable = false, length = 255)
	private String whatsNew;

	@Column(name = "language", length = 255)
	private String language;

	@Column(name = "title", length = 255)
	private String title;

	@Column(name = "company", length = 255)
	private String company;

	@Column(name = "website", length = 255)
	private String website;

	@Column(name = "updated_feilds", length = 255)
	private String updatedFeilds;

	// bi-directional many-to-one association to AppApplication
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "application_id", nullable = false)
	@JsonIgnore
	@JsonManagedReference("AppApplication-AppHistory")
	private AppApplication appApplication;

	public AppHistory() {
	}

	public Integer getHistoryId() {
		return this.historyId;
	}

	public void setHistoryId(Integer historyId) {
		this.historyId = historyId;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getVersionName() {
		return this.versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	

	public AppApplication getAppApplication() {
		return this.appApplication;
	}

	public void setAppApplication(AppApplication appApplication) {
		this.appApplication = appApplication;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFullDescription() {
		return fullDescription;
	}

	public void setFullDescription(String fullDescription) {
		this.fullDescription = fullDescription;
	}

	public String getPrivacyPolicyUrl() {
		return privacyPolicyUrl;
	}

	public void setPrivacyPolicyUrl(String privacyPolicyUrl) {
		this.privacyPolicyUrl = privacyPolicyUrl;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public String getWhatsNew() {
		return whatsNew;
	}

	public void setWhatsNew(String whatsNew) {
		this.whatsNew = whatsNew;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getUpdatedFeilds() {
		return updatedFeilds;
	}

	public void setUpdatedFeilds(String updatedFeilds) {
		this.updatedFeilds = updatedFeilds;
	}

	public String getUpdateType() {
		return updateType;
	}

	public void setUpdateType(String updateType) {
		this.updateType = updateType;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (int) (prime * result + ((historyId == null) ? 0 : historyId.hashCode()));
		return result;
	}

	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (!(obj instanceof AppHistory))
			return false;
		AppHistory equalCheck = (AppHistory) obj;
		if ((historyId == null && equalCheck.historyId != null) || (historyId != null && equalCheck.historyId == null))
			return false;
		if (historyId != null && !historyId.equals(equalCheck.historyId))
			return false;
		return true;
	}

	public AppHistoryDto populateAppHistoryDto(AppHistory appHistory) {
		AppHistoryDto appHistoryDto = null;
		try {
			appHistoryDto = new AppHistoryDto();
			BeanUtils.copyProperties(appHistory, appHistoryDto);
			String feildsArray[] = appHistory.getUpdatedFeilds().split(",");
			appHistoryDto.setUpdatedFeilds(feildsArray);
			//appHistoryDto.setAppApplication(appHistory.getAppApplication().populateAppApplicationDTO(appHistory.getAppApplication()));
		} catch (Exception e) {
			// TODO: handle exception
		}
		return appHistoryDto;
	}

}