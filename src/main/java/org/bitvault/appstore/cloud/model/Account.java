package org.bitvault.appstore.cloud.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * The persistent class for the account database table.
 * 
 */
@Entity
@Table(name = "account")
@NamedQuery(name = "Account.findAll", query = "SELECT a FROM Account a")
// @EntityListeners(AuditingEntityListener.class)
public class Account extends Auditable<String> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "acc_id", unique = true, nullable = false)
	private Integer accId;

	@Column(nullable = true, length = 45)
	private String country;

	@Column(name = "acc_email", nullable = true, length = 45)
	private String accEmail;

	@Column(name = "org_name", nullable = true, length = 45)
	private String orgName;

	@Column(nullable = true, length = 45)
	private String state;

	@Column(nullable = true, length = 1)
	private String status;

	@Column(nullable = true, length = 255)
	private String website;

	@OneToOne
	@JoinColumn(name = "role_id")
	@JsonIgnore
	private Role role;

	// bi-directional many-to-one association to AccountUser
	// @OneToMany(mappedBy="account")
	// private List<AccountUser> accountUsers;

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Account() {
	}

	public Account(Integer id) {
		accId = id;
	}

	public Integer getAccId() {
		return this.accId;
	}

	public void setAccId(Integer accId) {
		this.accId = accId;
	}

	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getAccEmail() {
		return this.accEmail;
	}

	public void setAccEmail(String accEmail) {
		this.accEmail = accEmail;
	}

	public String getOrgName() {
		return this.orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getWebsite() {
		return this.website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	// public Role getRole() {
	// return this.role;
	// }
	//
	// public void setRole(Role role) {
	// this.role = role;
	// }

	// public List<AccountUser> getAccountUsers() {
	// return this.accountUsers;
	// }
	//
	// public void setAccountUsers(List<AccountUser> accountUsers) {
	// this.accountUsers = accountUsers;
	// }

	// public AccountUser addAccountUser(AccountUser accountUser) {
	// getAccountUsers().add(accountUser);
	// accountUser.setAccount(this);
	//
	// return accountUser;
	// }
	//
	// public AccountUser removeAccountUser(AccountUser accountUser) {
	// getAccountUsers().remove(accountUser);
	// accountUser.setAccount(null);
	//
	// return accountUser;
	// }

	// public DevUser addDevUser(DevUser devUser) {
	// getDevUsers().add(devUser);
	// devUser.setAccount(this);
	//
	// return devUser;
	// }
	//
	// public DevUser removeDevUser(DevUser devUser) {
	// getDevUsers().remove(devUser);
	// devUser.setAccount(null);
	//
	// return devUser;
	// }

}