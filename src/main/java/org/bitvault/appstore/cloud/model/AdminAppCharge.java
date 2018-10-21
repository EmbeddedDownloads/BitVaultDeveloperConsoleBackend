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
 * The persistent class for the admin_app_charges database table.
 * 
 */
@Entity
@Table(name="admin_app_charges")
@NamedQuery(name="AdminAppCharge.findAll", query="SELECT a FROM AdminAppCharge a")
public class AdminAppCharge implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="app_charges_id", unique=true, nullable=false)
	private int appChargesId;

	@Column(name="admin_charges", nullable=false)
	private float adminCharges;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="created_at", nullable=false)
	private Date createdAt;

	@Column(name="created_by", nullable=false, length=255)
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="updated_at", nullable=false)
	private Date updatedAt;

	@Column(name="updated_by", nullable=false, length=255)
	private String updatedBy;

	//bi-directional many-to-one association to Application
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="application_id", nullable=false)
	@JsonIgnore
	@JsonManagedReference("Application-AdminAppCharge")
	private Application application;

	public AdminAppCharge() {
	}

	public int getAppChargesId() {
		return this.appChargesId;
	}

	public void setAppChargesId(int appChargesId) {
		this.appChargesId = appChargesId;
	}

	public float getAdminCharges() {
		return this.adminCharges;
	}

	public void setAdminCharges(float adminCharges) {
		this.adminCharges = adminCharges;
	}

	public Date getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getUpdatedAt() {
		return this.updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Application getApplication() {
		return this.application;
	}

	public void setApplication(Application application) {
		this.application = application;
	}

}