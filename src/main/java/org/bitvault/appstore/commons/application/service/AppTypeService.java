package org.bitvault.appstore.commons.application.service;

import java.util.List;

import org.bitvault.appstore.cloud.dto.ApplicationTypeDto;
import org.bitvault.appstore.cloud.exception.BitVaultException;

public interface AppTypeService {
	public ApplicationTypeDto findAppTypeById(Integer id) throws BitVaultException;
	public List<ApplicationTypeDto> appType() throws BitVaultException;

}
