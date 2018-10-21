package org.bitvault.appstore.cloud.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


/**
 * The persistent class for the roles database table.
 * 
 */
@Entity
@Table(name="roles")
@NamedQuery(name="Role.findAll", query="SELECT r FROM Role r")
@EntityListeners(AuditingEntityListener.class)
public class Role implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="role_id", unique=true, nullable=false)
	private Integer roleId;

//	@Temporal(TemporalType.TIMESTAMP)
	@CreatedDate
	@Column(name="created_at", nullable=false)
	private Date createdAt;

	@Column(name="created_by", nullable=false)
	private String createdBy;

	@Column(name="role_name", nullable=true, length=255)
	private String roleName;

	@Column(nullable=true, length=1)
	private String status;

//	@Temporal(TemporalType.TIMESTAMP)
	@LastModifiedDate
	@Column(name="updated_at", nullable=false)
	private Date updatedAt;

	@Column(name="updated_by", nullable=false)
	private String updatedBy;

	//bi-directional many-to-one association to AccountUser
//	@OneToMany(mappedBy="role")
//	private List<AccountUser> accountUsers;

//	bi-directional many-to-one association to AdminUser
//	@OneToOne(mappedBy="role")
//	private AdminUser adminUser;

	//bi-directional many-to-one association to DevUser
//	@OneToMany(fetch = FetchType.LAZY)
//	@JoinColumn(name = "user_id")
//	private List<DevUser> devUsers;

	public Role() {
	}
	
	public Role(Integer i) {
		roleId = i;
	}

	public Integer getRoleId() {
		return this.roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
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

	public String getRoleName() {
		return this.roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
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

	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

//	public List<AccountUser> getAccountUsers() {
//		return this.accountUsers;
//	}
//
//	public void setAccountUsers(List<AccountUser> accountUsers) {
//		this.accountUsers = accountUsers;
//	}
//
//	public AccountUser addAccountUser(AccountUser accountUser) {
//		getAccountUsers().add(accountUser);
//		accountUser.setRole(this);
//
//		return accountUser;
//	}
//
//	public AccountUser removeAccountUser(AccountUser accountUser) {
//		getAccountUsers().remove(accountUser);
//		accountUser.setRole(null);
//
//		return accountUser;
//	}

//	public List<AdminUser> getAdminUsers() {
//		return this.adminUsers;
//	}
//
//	public void setAdminUsers(List<AdminUser> adminUsers) {
//		this.adminUsers = adminUsers;
//	}
//
//	public AdminUser addAdminUser(AdminUser adminUser) {
//		getAdminUsers().add(adminUser);
//		adminUser.setRole(this);
//
//		return adminUser;
//	}
//
//	public AdminUser removeAdminUser(AdminUser adminUser) {
//		getAdminUsers().remove(adminUser);
//		adminUser.setRole(null);
//
//		return adminUser;
//	}

//	public List<DevUser> getDevUsers() {
//		return this.devUsers;
//	}
//
//	public void setDevUsers(List<DevUser> devUsers) {
//		this.devUsers = devUsers;
//	}
//
//	public DevUser addDevUser(DevUser devUser) {
//		getDevUsers().add(devUser);
//		devUser.setRole(this);
//
//		return devUser;
//	}
//
//	public DevUser removeDevUser(DevUser devUser) {
//		getDevUsers().remove(devUser);
//		devUser.setRole(null);
//
//		return devUser;
//	}

	@Override
	public String toString() {
		return getRoleName();
	}
	
}