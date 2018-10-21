package org.bitvault.appstore.cloud.dto;

import org.bitvault.appstore.cloud.model.Role;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;

public class RoleDtoMapper {

	public static Role toRole(RoleDto dto) {
		try {
			Role role = new Role();
			BeanUtils.copyProperties(dto, role);
			return role;
		} catch (BeansException e) {
			return null;
		}
	}

	public static RoleDto toRoleDto(Role role) {
		try {
			RoleDto dto = new RoleDto();
			BeanUtils.copyProperties(role, dto);
			return dto;
		} catch (BeansException e) {
			return null;
		}
	}
}
