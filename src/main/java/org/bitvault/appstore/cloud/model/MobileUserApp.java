package org.bitvault.appstore.cloud.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.dto.MobileUserAppDto;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
 * The persistent class for the mobile_user_app database table.
 * 
 */
@Entity
@Table(name = "mobile_user_app")
@EntityListeners(AuditingEntityListener.class)
@NamedQuery(name = "MobileUserApp.findAll", query = "SELECT m FROM MobileUserApp m")
public class MobileUserApp implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "mobile_user_app_id", unique = true, nullable = false)
	private int mobileUserAppId;

	@Column(name = "app_price", nullable = false)
	private float appPrice;

	@Column(name = "app_txn_id", nullable = false, length = 45)
	private String appTxnId;

	@Column(nullable = false, length = 1)
	private String status;

	@CreatedDate
	@Column(name = "created_at")
	private Date createdAt;

	@LastModifiedDate
	@Column(name = "updated_at")
	private Date updatedAt;

	// bi-directional many-to-one association to Application
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "application_id", nullable = false)
	@JsonIgnore
	@JsonManagedReference("AppApplication-MobileUserApp")
	private AppApplication application;

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public MobileUser getMobileUser() {
		return mobileUser;
	}

	public void setMobileUser(MobileUser mobileUser) {
		this.mobileUser = mobileUser;
	}

	// bi-directional many-to-one association to Application
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "mobile_user_id", nullable = false)
	@JsonIgnore
	@JsonManagedReference("MobileUser-MobileUserApp")
	private MobileUser mobileUser;

	public MobileUserApp() {
	}

	public int getMobileUserAppId() {
		return this.mobileUserAppId;
	}

	public void setMobileUserAppId(int mobileUserAppId) {
		this.mobileUserAppId = mobileUserAppId;
	}

	public float getAppPrice() {
		return this.appPrice;
	}

	public void setAppPrice(float appPrice) {
		this.appPrice = appPrice;
	}

	public String getAppTxnId() {
		return this.appTxnId;
	}

	public void setAppTxnId(String appTxnId) {
		this.appTxnId = appTxnId;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public AppApplication getApplication() {
		return this.application;
	}

	public void setApplication(AppApplication application) {
		this.application = application;
	}

	public MobileUserAppDto populateMobileUserAppDto(MobileUserApp mobileUserApp) {
		MobileUserAppDto mobileUserAppDto = new MobileUserAppDto();
		try {
			BeanUtils.copyProperties(mobileUserApp, mobileUserAppDto);
			mobileUserAppDto.setMobileUser(mobileUserApp.getMobileUser().populateMobileUserDTO(mobileUserApp.getMobileUser()));
			mobileUserAppDto.setApplication(mobileUserApp.getApplication().populateAppApplicationDTO(mobileUserApp.getApplication()));
		} catch (Exception e) {
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);
		}
		return mobileUserAppDto;

	}
}