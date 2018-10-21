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
 * The persistent class for the app_crash_log database table.
 * 
 */
@Entity
@Table(name="app_crash_log")
@NamedQuery(name="AppCrashLog.findAll", query="SELECT a FROM AppCrashLog a")
public class AppCrashLog implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="log_id", unique=true, nullable=false)
	private int logId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="created_at", nullable=false)
	private Date createdAt;

	@Column(name="stack_trace", nullable=false, length=255)
	private String stackTrace;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="updated_at", nullable=false)
	private Date updatedAt;

	//bi-directional many-to-one association to AppDetail
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="app_detail_id", nullable=false)
	@JsonIgnore
	@JsonManagedReference("AppDetail-AppCrashLog")
	private AppDetail appDetail;

	public AppCrashLog() {
	}

	public int getLogId() {
		return this.logId;
	}

	public void setLogId(int logId) {
		this.logId = logId;
	}

	public Date getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getStackTrace() {
		return this.stackTrace;
	}

	public void setStackTrace(String stackTrace) {
		this.stackTrace = stackTrace;
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

}