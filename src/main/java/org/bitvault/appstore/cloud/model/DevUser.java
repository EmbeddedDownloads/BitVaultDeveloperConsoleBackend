package org.bitvault.appstore.cloud.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.bitvault.appstore.cloud.constant.ErrorCode;
import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.springframework.beans.BeanUtils;
import org.springframework.data.elasticsearch.annotations.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The persistent class for the dev_users database table.
 * 
 */
@Entity
@Table(name = "dev_users")
@NamedQuery(name = "DevUser.findAll", query = "SELECT d FROM DevUser d")
@Document(indexName = "appstore_cloud", type = "devuser")
public class DevUser extends Auditable<String> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3910728504414357025L;

	@Id
	@org.springframework.data.annotation.Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator", parameters = {
			@Parameter(name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
	@Column(name = "user_id", unique = true, nullable = false, length = 255, updatable = false)
	private String userId;

	@Column(name = "alt_email", nullable = false, length = 255)
	private String altEmail;

	@Column(name = "verification_link", nullable = false, length = 255)
	private String verificationLink;

	public String getVerificationLink() {
		return verificationLink;
	}

	public void setVerificationLink(String verificationLink) {
		this.verificationLink = verificationLink;
	}

	@Column(name = "child_count", nullable = false)
	private int childCount;

	private String privateKey;

	private String publicKey;

	@OneToOne
	@JoinColumn(name = "acc_id")
	@JsonProperty("acc_id")
	@JsonIgnore
	private Account account;

	@Column(name = "lastPasswordResetDate")
	Date lastPasswordResetDate;
	@JsonProperty("avatarurl")
	private String avatarURL;

	@Column(name = "rejection_reason")
	private String rejectionReason;

	public String getRejectionReason() {
		return rejectionReason;
	}

	public void setRejectionReason(String rejectionReason) {
		this.rejectionReason = rejectionReason;
	}

	public String getAvatarURL() {
		return avatarURL;
	}

	public void setAvatarURL(String avatarURL) {
		this.avatarURL = avatarURL;
	}

	public Date getLastPasswordResetDate() {
		return lastPasswordResetDate;
	}

	public void setLastPasswordResetDate(Date lastPasswordResetDate) {
		this.lastPasswordResetDate = lastPasswordResetDate;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	@Column(nullable = false, length = 255)
	private String email;

	@Column(nullable = false, length = 1)
	private String gender;

	@Size(min = 6)
	@Column(nullable = false, length = 45)
	private String password;

	@Column(name = "signup_reason", nullable = false, length = 255)
	private String signupReason;

	@Column(name = "parent_id")
	@JsonProperty("parent_id")
	private String parentId;

	@Column(name = "status")
	private String status;

	// @OneToMany(mappedBy = "devUser")
	// List<Request> requests;
	//
	// public List<Request> getRequests() {
	// return requests;
	// }
	//
	// public void setRequests(List<Request> requests) {
	// this.requests = requests;
	// }

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	@Size(min = 3, max = 30)
	@Column(nullable = false, length = 45)
	private String username;

	// bi-directional many-to-one association to Role
	// @OneToMany(fetch = FetchType.EAGER)
	// @JoinColumn(name = "role_id")
	// private List<Role> role;

	public DevUser() {
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getAltEmail() {
		return this.altEmail;
	}

	public void setAltEmail(String altEmail) {
		this.altEmail = altEmail;
	}

	public int getChildCount() {
		return this.childCount;
	}

	public void setChildCount(int childCount) {
		this.childCount = childCount;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getGender() {
		return this.gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSignupReason() {
		return this.signupReason;
	}

	public void setSignupReason(String signupReason) {
		this.signupReason = signupReason;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	// public List<Role> getRole() {
	// return this.role;
	// }
	//
	// public void setRole(List<Role> role) {
	// this.role = role;
	// }

	@Column(name = "child_created")
	private int childCreated;

	public int getChildCreated() {
		return childCreated;
	}

	public void setChildCreated(int childCreated) {
		this.childCreated = childCreated;
	}

	@OneToOne
	@JoinColumn(name = "role_id")
	@JsonProperty("role_id")
	private Role role;

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return "user_id:" + getUserId() + "\tusername:" + getUsername() + "\trole:" + getRole().toString() + "\temail:"
				+ getEmail() + "\talt_email:" + getAltEmail();
	}

	public org.bitvault.appstore.cloud.dto.DevUserElasticDto populateDevUserElasticDTO(DevUser devUser) {
		org.bitvault.appstore.cloud.dto.DevUserElasticDto userDTO = new org.bitvault.appstore.cloud.dto.DevUserElasticDto();

		try {

			BeanUtils.copyProperties(devUser, userDTO);

		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED, ErrorCode.SOME_ERROR_OCCURED_CODE);
		}
		return userDTO;
	}
}