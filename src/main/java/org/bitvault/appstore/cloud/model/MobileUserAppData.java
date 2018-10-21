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
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;


/**
 * The persistent class for the mobile_user_app_data database table.
 * 
 */
@Entity
@Table(name="mobile_user_app_data")
@NamedQuery(name="MobileUserAppData.findAll", query="SELECT m FROM MobileUserAppData m")
public class MobileUserAppData implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="data_id", unique=true, nullable=false)
	private int dataId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="created_at", nullable=false)
	private Date createdAt;

	@Column(nullable=false, length=255)
	private String key;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="updated_at", nullable=false)
	private Date updatedAt;

	@Lob
	@Column(nullable=false)
	private String value;

	//bi-directional many-to-one association to AppDetail
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="app_detail_id", nullable=false)
	@JsonIgnore
	@JsonManagedReference("AppDetail-MobileUserAppData")
	private AppDetail appDetail;

	//bi-directional many-to-one association to MobileUser
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="mobile_user_id", nullable=false)
	@JsonIgnore
	@JsonManagedReference("AppDetail-MobileUser")
	private MobileUser mobileUser;

	public MobileUserAppData() {
	}

	public int getDataId() {
		return this.dataId;
	}

	public void setDataId(int dataId) {
		this.dataId = dataId;
	}

	public Date getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getKey() {
		return this.key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Date getUpdatedAt() {
		return this.updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
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