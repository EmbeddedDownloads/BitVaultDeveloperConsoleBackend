package org.bitvault.appstore.cloud.converter;

import org.bitvault.appstore.cloud.model.Role;
import org.bitvault.appstore.cloud.user.common.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class RoleIdToRoleConverter implements Converter<Integer, Role> {

	@Autowired
	RoleService roleService;

	@Override
	public Role convert(Integer source) {
		return null;
//		try {
//			Integer id = source;
//			Role role = roleService.findById(id);
//			return role;
//		} catch (NumberFormatException e) {
//			throw new BitVaultException(e, ErrorMessageConstant.UNABLE_TO_SAVE,
//					ErrorMessageConstant.UNABLE_TO_SAVE_CODE);
//		}
	}

}
