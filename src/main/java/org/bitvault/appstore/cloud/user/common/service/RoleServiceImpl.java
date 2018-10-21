package org.bitvault.appstore.cloud.user.common.service;

import java.util.ArrayList;
import java.util.List;

import org.bitvault.appstore.cloud.dto.RoleDto;
import org.bitvault.appstore.cloud.dto.RoleDtoMapper;
import org.bitvault.appstore.cloud.model.Role;
import org.bitvault.appstore.cloud.user.common.dao.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

	@Autowired
	RoleRepository roleRepository;

	@Override
	public List<RoleDto> getAllRoles() {
		List<Role> roles = roleRepository.findAll();
		if (roles == null) {
			return null;
		}
		List<RoleDto> dtos = new ArrayList<RoleDto>();
		for (Role role : roles) {
			dtos.add(RoleDtoMapper.toRoleDto(role));
		}
		return dtos;
	}

	@Override
	public RoleDto findById(Integer id) {
		try {
			Role role = roleRepository.getOne(id);
			return RoleDtoMapper.toRoleDto(role);
		} catch (Exception e) {
			return null;
		}
	}

}
