package org.bitvault.appstore.cloud.model;

import java.io.Serializable;
import java.math.BigInteger;
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
 * The persistent class for the secure_msg_fee database table.
 * 
 */
@Entity
@Table(name="secure_msg_fee")
@NamedQuery(name="SecureMsgFee.findAll", query="SELECT s FROM SecureMsgFee s")
public class SecureMsgFee implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="secure_msg_id", unique=true, nullable=false)
	private int secureMsgId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="created_at", nullable=false)
	private Date createdAt;

	@Column(name="created_by", nullable=false, length=255)
	private String createdBy;

	@Column(name="max_size", nullable=false)
	private BigInteger maxSize;

	@Column(nullable=false)
	private float price;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="updated_at", nullable=false)
	private Date updatedAt;

	@Column(name="updated_by", nullable=false, length=255)
	private String updatedBy;

	public SecureMsgFee() {
	}

	public int getSecureMsgId() {
		return this.secureMsgId;
	}

	public void setSecureMsgId(int secureMsgId) {
		this.secureMsgId = secureMsgId;
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

	public BigInteger getMaxSize() {
		return this.maxSize;
	}

	public void setMaxSize(BigInteger maxSize) {
		this.maxSize = maxSize;
	}

	public float getPrice() {
		return this.price;
	}

	public void setPrice(float price) {
		this.price = price;
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

}