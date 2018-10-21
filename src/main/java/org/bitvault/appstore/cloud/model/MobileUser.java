package org.bitvault.appstore.cloud.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.bitvault.appstore.cloud.constant.ErrorCode;
import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * The persistent class for the mobile_user database table.
 * 
 */
@Entity
@Table(name = "mobile_user")
@EntityListeners(AuditingEntityListener.class)
@NamedQuery(name = "MobileUser.findAll", query = "SELECT m FROM MobileUser m")
public class MobileUser implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "mobile_user_id", unique = true, nullable = false)
	private Integer mobileUserId;

	// @Temporal(TemporalType.TIMESTAMP)
	@CreatedDate
	@Column(name = "created_at")
	private Date createdAt;

	@Column(name = "public_add", nullable = false, length = 45)
	private String publicAdd;

	@Column(name = "reprocessed_count", nullable = false)
	private int reprocessedCount;

	// bi-directional many-to-one association to AppApplication
		@OneToMany(mappedBy = "mobileUser")
		@JsonIgnore
		@JsonBackReference("AppRateReview-MobileUser")
		private List<AppRateReview> AppRateReview;
	
		@OneToMany(mappedBy = "mobileUser")
		@JsonIgnore
		@JsonBackReference("MobileUserApp-MobileUser")
		private List<MobileUserApp> mobileuserApp;
	
	// @Temporal(TemporalType.TIMESTAMP)
	@LastModifiedDate
	@Column(name = "updated_at")
	private Date updatedAt;

	// bi-directional many-to-one association to AppPurchase
	@OneToMany(mappedBy = "mobileUser")
	private List<AppPurchase> appPurchases;

	// bi-directional many-to-one association to MobileUserAppData
	@OneToMany(mappedBy = "mobileUser")
	private List<MobileUserAppData> mobileUserAppData;

	public MobileUser() {
	}

	public Integer getMobileUserId() {
		return this.mobileUserId;
	}

	public void setMobileUserId(Integer mobileUserId) {
		this.mobileUserId = mobileUserId;
	}

	public Date getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getPublicAdd() {
		return this.publicAdd;
	}

	public void setPublicAdd(String publicAdd) {
		this.publicAdd = publicAdd;
	}

	public int getReprocessedCount() {
		return this.reprocessedCount;
	}

	public void setReprocessedCount(int reprocessedCount) {
		this.reprocessedCount = reprocessedCount;
	}

	public Date getUpdatedAt() {
		return this.updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public List<AppPurchase> getAppPurchases() {
		return this.appPurchases;
	}

	public void setAppPurchases(List<AppPurchase> appPurchases) {
		this.appPurchases = appPurchases;
	}

	public AppPurchase addAppPurchas(AppPurchase appPurchas) {
		getAppPurchases().add(appPurchas);
		appPurchas.setMobileUser(this);

		return appPurchas;
	}

	public AppPurchase removeAppPurchas(AppPurchase appPurchas) {
		getAppPurchases().remove(appPurchas);
		appPurchas.setMobileUser(null);

		return appPurchas;
	}

	public List<MobileUserAppData> getMobileUserAppData() {
		return this.mobileUserAppData;
	}

	public void setMobileUserAppData(List<MobileUserAppData> mobileUserAppData) {
		this.mobileUserAppData = mobileUserAppData;
	}

	public MobileUserAppData addMobileUserAppData(MobileUserAppData mobileUserAppData) {
		getMobileUserAppData().add(mobileUserAppData);
		mobileUserAppData.setMobileUser(this);

		return mobileUserAppData;
	}

	public MobileUserAppData removeMobileUserAppData(MobileUserAppData mobileUserAppData) {
		getMobileUserAppData().remove(mobileUserAppData);
		mobileUserAppData.setMobileUser(null);

		return mobileUserAppData;
	}

	public org.bitvault.appstore.cloud.dto.MobileUser populateMobileUserDTO(MobileUser mobileUser) {
		org.bitvault.appstore.cloud.dto.MobileUser mobileUserDTO = new org.bitvault.appstore.cloud.dto.MobileUser();
		try {
			BeanUtils.copyProperties(mobileUser, mobileUserDTO);
			
		} catch (Exception e) {
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED, ErrorCode.SOME_ERROR_OCCURED_CODE);
		}
		return mobileUserDTO;

	}
}