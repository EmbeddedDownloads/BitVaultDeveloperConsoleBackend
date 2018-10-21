package org.bitvault.appstore.cloud.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the link_verification database table.
 * 
 */
@Entity
@Table(name="link_verification")
@NamedQuery(name="ForgetPassword.findAll", query="SELECT l FROM ForgetPassword l")
public class ForgetPassword extends Auditable<String> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="forgot_link_verify_id", unique=true, nullable=false)
	private int forgotLinkVerifyId;

	@Column(name="forgot_link", nullable=false, length=255)
	private String forgotLink;

	@Column(nullable=false, length=255)
	private String hashcode;

	@Column(name="user_id", nullable=false, length=255)
	private String userId;

	public ForgetPassword() {
	}

	public int getForgotLinkVerifyId() {
		return this.forgotLinkVerifyId;
	}

	public void setForgotLinkVerifyId(int forgotLinkVerifyId) {
		this.forgotLinkVerifyId = forgotLinkVerifyId;
	}

	public String getForgotLink() {
		return this.forgotLink;
	}

	public void setForgotLink(String forgotLink) {
		this.forgotLink = forgotLink;
	}

	public String getHashcode() {
		return this.hashcode;
	}

	public void setHashcode(String hashcode) {
		this.hashcode = hashcode;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}