package org.bitvault.appstore.cloud.model;

public enum RoleType {
ADMIN("ADMIN"),
ORGANISATION("ORGANISATION"),
DEVELOPER("DEVELOPER"),
CHILD_DEVELOPER("CHILD DEVELOPER");
	
	String roleType;
	
	private RoleType(String roleType) {
		this.roleType = roleType;
	}
	
	public String getRoleType(){
		return roleType;
	}
}
