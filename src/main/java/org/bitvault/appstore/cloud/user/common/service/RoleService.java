package org.bitvault.appstore.cloud.user.common.service;

import java.util.List;

import org.bitvault.appstore.cloud.dto.RoleDto;

public interface RoleService {
	RoleDto findById(Integer id);

	List<RoleDto> getAllRoles();
}
