package org.bitvault.appstore.cloud.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the user_activation database table.
 * 
 */
@Entity
@Table(name="user_activation")
@NamedQuery(name="UserActivation.findAll", query="SELECT u FROM UserActivation u")
public class UserActivation implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="activation_id", unique=true, nullable=false)
	private int activationId;

	@Column(name="activation_url", nullable=false, length=45)
	private String activationUrl;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="created_at", nullable=false)
	private Date createdAt;

	@Column(nullable=false, length=1)
	private String status;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="updated_at", nullable=false)
	private Date updatedAt;

	@Column(name="user_id", nullable=false, length=255)
	private String userId;

	public UserActivation() {
	}

	public int getActivationId() {
		return this.activationId;
	}

	public void setActivationId(int activationId) {
		this.activationId = activationId;
	}

	public String getActivationUrl() {
		return this.activationUrl;
	}

	public void setActivationUrl(String activationUrl) {
		this.activationUrl = activationUrl;
	}

	public Date getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getUpdatedAt() {
		return this.updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}