package org.bitvault.appstore.cloud.dto;

import java.io.Serializable;
import java.util.Date;

public class RoleDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4354668954209836922L;
	
	private Integer roleId;

	private Date createdAt;

	private String createdBy;

	private String roleName;

	private String status;

	private Date updatedAt;

	private String updatedBy;

//	private List<DevUserDto> devUsers;

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

//	public List<DevUserDto> getDevUsers() {
//		return devUsers;
//	}
//
//	public void setDevUsers(List<DevUserDto> devUsers) {
//		this.devUsers = devUsers;
//	}

}
