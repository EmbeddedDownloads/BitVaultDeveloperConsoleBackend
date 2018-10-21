package org.bitvault.appstore.cloud.model;

import java.io.Serializable;
import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
 * The persistent class for the app_purchase database table.
 * 
 */
@Entity
@Table(name = "app_purchase")
@NamedQuery(name = "AppPurchase.findAll", query = "SELECT a FROM AppPurchase a")
public class AppPurchase implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "purchase_id", unique = true, nullable = false)
	private int purchaseId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_at", nullable = false)
	private Date createdAt;

	@Column(name = "payment_value", nullable = false)
	private float paymentValue;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_at", nullable = false)
	private Date updatedAt;

	// bi-directional many-to-one association to AppDetail
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "app_detail_id", nullable = false)
	@JsonIgnore
	@JsonManagedReference("AppDetail-AppPurchase")
	private AppDetail appDetail;

	// bi-directional many-to-one association to MobileUser
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "mobile_user_id", nullable = false)
	@JsonIgnore
	@JsonManagedReference("MobileUser-AppPurchase")
	private MobileUser mobileUser;

	public AppPurchase() {
	}

	public int getPurchaseId() {
		return this.purchaseId;
	}

	public void setPurchaseId(int purchaseId) {
		this.purchaseId = purchaseId;
	}

	public Date getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public float getPaymentValue() {
		return this.paymentValue;
	}

	public void setPaymentValue(float paymentValue) {
		this.paymentValue = paymentValue;
	}

	public Date getUpdatedAt() {
		return this.updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public AppDetail getAppDetail() {
		return this.appDetail;
	}

	public void setAppDetail(AppDetail appDetail) {
		this.appDetail = appDetail;
	}

	public MobileUser getMobileUser() {
		return this.mobileUser;
	}

	public void setMobileUser(MobileUser mobileUser) {
		this.mobileUser = mobileUser;
	}

}