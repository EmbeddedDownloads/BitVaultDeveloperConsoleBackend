package org.bitvault.appstore.cloud.model;

import java.io.Serializable;

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

import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.dto.RejectedAppDto;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
 * The persistent class for the rejected_app database table.
 * 
 */
@Entity
@Table(name = "rejected_app")
@NamedQuery(name = "RejectedApp.findAll", query = "SELECT r FROM RejectedApp r")
public class RejectedApp extends Auditable<String> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "rejected_app_id", unique = true, nullable = false)
	private int rejectedAppId;

	@Column(name = "admin_id", nullable = false, length = 255)
	private String adminId;

	@Column(nullable = false, length = 255)
	private String reason;

	@Column(nullable = false, length = 1)
	private String status;

	// bi-directional many-to-one association to AppDetail
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "app_detail_id", nullable = false)
	@JsonIgnore
	@JsonManagedReference("AppDetail-RejectedApp")
	private AppDetail appDetail;

	public RejectedApp() {
	}

	public int getRejectedAppId() {
		return this.rejectedAppId;
	}

	public void setRejectedAppId(int rejectedAppId) {
		this.rejectedAppId = rejectedAppId;
	}

	public String getAdminId() {
		return this.adminId;
	}

	public void setAdminId(String adminId) {
		this.adminId = adminId;
	}

	public String getReason() {
		return this.reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public AppDetail getAppDetail() {
		return this.appDetail;
	}

	public void setAppDetail(AppDetail appDetail) {
		this.appDetail = appDetail;
	}

	public RejectedAppDto populateRejectedAppDto(RejectedApp rejApp) {
		RejectedAppDto rejAppDto = new RejectedAppDto();
		try {
			BeanUtils.copyProperties(rejApp, rejAppDto);
		} catch (Exception e) {
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);
		}
		return rejAppDto;
	}

}