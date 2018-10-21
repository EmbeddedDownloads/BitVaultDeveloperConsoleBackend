package org.bitvault.appstore.commons.application.service;

import java.util.ArrayList;
import java.util.List;

import org.bitvault.appstore.cloud.constant.ErrorCode;
import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.dto.ApplicationTypeDto;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.bitvault.appstore.cloud.model.ApplicationType;
import org.bitvault.appstore.commons.application.dao.AppTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("AppTypeService")
@Transactional
public class AppTypeServiceImpl implements AppTypeService {

	@Autowired
	AppTypeRepository appTypeRepository;

	@Override
	public ApplicationTypeDto findAppTypeById(Integer id) throws BitVaultException {
		ApplicationType appType = null;
		ApplicationTypeDto appTypeDto = null;
		try {
			appType = appTypeRepository.findAppTypeById(id);
			if(null!=appType)
			appTypeDto = appType.populateAppTypeDto(appType);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED, ErrorCode.SOME_ERROR_OCCURED_CODE);
		}
		return appTypeDto;
	}

	@Override
	public List<ApplicationTypeDto> appType() throws BitVaultException {
		List<ApplicationType> appType = null;
		List<ApplicationTypeDto> appTypeDto = null;
		try {
			appType = appTypeRepository.findAll();
			appTypeDto = new ArrayList<ApplicationTypeDto>();
			for (ApplicationType applicationType : appType) {
				appTypeDto.add(applicationType.populateAppTypeDto(applicationType));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);
		}
		return appTypeDto;
	}

}
