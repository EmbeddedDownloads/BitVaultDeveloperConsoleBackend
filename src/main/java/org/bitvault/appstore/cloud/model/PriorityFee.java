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
 * The persistent class for the priority_fee database table.
 * 
 */
@Entity
@Table(name="priority_fee")
@NamedQuery(name="PriorityFee.findAll", query="SELECT p FROM PriorityFee p")
public class PriorityFee implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="priority_fee_id", unique=true, nullable=false)
	private int priorityFeeId;

	@Column(name="app_priority_fee", nullable=false)
	private float appPriorityFee;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="created_at", nullable=false)
	private Date createdAt;

	@Column(name="created_by", nullable=false)
	private int createdBy;

	@Column(name="priority_fee_type", nullable=false, length=1)
	private String priorityFeeType;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="updated_at", nullable=false)
	private Date updatedAt;

	@Column(name="updated_by", nullable=false)
	private int updatedBy;

	public PriorityFee() {
	}

	public int getPriorityFeeId() {
		return this.priorityFeeId;
	}

	public void setPriorityFeeId(int priorityFeeId) {
		this.priorityFeeId = priorityFeeId;
	}

	public float getAppPriorityFee() {
		return this.appPriorityFee;
	}

	public void setAppPriorityFee(float appPriorityFee) {
		this.appPriorityFee = appPriorityFee;
	}

	public Date getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public int getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public String getPriorityFeeType() {
		return this.priorityFeeType;
	}

	public void setPriorityFeeType(String priorityFeeType) {
		this.priorityFeeType = priorityFeeType;
	}

	public Date getUpdatedAt() {
		return this.updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public int getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
	}

}