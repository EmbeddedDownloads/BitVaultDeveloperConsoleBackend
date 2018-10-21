package org.bitvault.appstore.cloud.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonProperty;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditable<U> {

	@CreatedBy
	@Column(name = "created_by")
	protected U createdBy;

	@CreatedDate
	@Temporal(TemporalType.TIMESTAMP)
	@JsonProperty("created_at")
	@Column(name = "created_at")
	protected Date createdAt;

	@LastModifiedBy
	@Column(name = "updated_by")
	protected U updatedBy;

	@LastModifiedDate
	@Temporal(TemporalType.TIMESTAMP)
	@JsonProperty("updated_at")
	@Column(name = "updated_at")
	protected Date updatedAt;


	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	
	public U getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(U createdBy) {
		this.createdBy = createdBy;
	}

	public U getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(U updatedBy) {
		this.updatedBy = updatedBy;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
}
