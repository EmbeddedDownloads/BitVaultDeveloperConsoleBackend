package org.bitvault.appstore.cloud.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;


/**
 * The persistent class for the admin_users database table.
 * 
 */
@Entity
@Table(name="admin_users")
@NamedQuery(name="AdminUser.findAll", query="SELECT a FROM AdminUser a")
public class AdminUser extends Auditable<String> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(
		name = "UUID",
		strategy = "org.hibernate.id.UUIDGenerator",
		parameters = {
			@Parameter(
				name = "uuid_gen_strategy_class",
				value = "org.hibernate.id.uuid.CustomVersionOneStrategy"
			)
		}
	)
	@Column(name="user_id", unique=true, nullable=false, length=255, updatable = false)
	private String userId;

	@Column(nullable=false, length=255)
	private String email;

	@Column(name="first_name", nullable=false, length=255)
	private String firstName;

	@Column(name="last_name", nullable=false, length=255)
	private String lastName;

	@Column(nullable=false, length=255)
	private String password;

	@Column(nullable=false, length=1)
	private String status;
	
	@Column(name = "lastPasswordResetDate")
	Date lastPasswordResetDate;

	private String avatarURL;
	
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

	//bi-directional many-to-one association to Role
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="role_id", nullable=false)
	private Role role;

	public AdminUser() {
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Role getRole() {
		return this.role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

}