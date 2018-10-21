package org.bitvault.appstore.cloud.user.common.service;

import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.bitvault.appstore.cloud.model.AddonService;
import org.bitvault.appstore.cloud.model.AppAddonServices;
import org.bitvault.appstore.cloud.user.admin.dao.AddonServiceRepository;
import org.bitvault.appstore.cloud.user.common.dao.AppAddonServicesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AppAddonServicesServiceImpl implements AppAddonServicesService{
	
	@Autowired
	private AppAddonServicesRepository appAddonServicesRepository;
	
	@Autowired
	private AddonServiceRepository addonServiceRepository;

	@Override
	public void save(AppAddonServices appService, int serviceId) {
		AddonService addonService = addonServiceRepository.findOne(serviceId);
		if(addonService == null){
			throw new BitVaultException(ErrorMessageConstant.INVALID_SERVICE);
		}
		appService.setAddonService(addonService);
		appAddonServicesRepository.saveAndFlush(appService);
	}

	@Override
	public Page<AppAddonServices> getAllAppAddonServices(String userId, int page, int size, String orderBy) {
		return appAddonServicesRepository.getAllAddonServicesByUserId(userId, new PageRequest(page, size, new Sort(Direction.ASC, orderBy)));
	}

	@Override
	public AppAddonServices getAppAddOnService(String userId, int addonServiceId,String packageName) {
		return appAddonServicesRepository.getAddonServicesByUserId(addonServiceId,packageName);
	}
	
	@Override
	public AppAddonServices getAppAddOnService(int addonServiceId,String packageName) {
		return appAddonServicesRepository.getAddonServicesByUserId(addonServiceId,packageName);
	}

	@Override
	public Page<AppAddonServices> getAllAppAddonServicesForAnApp(String packageName, int page, int size, String orderBy) {
		return appAddonServicesRepository.getAllAddonServicesByPackageName(packageName, new PageRequest(page, size, new Sort(Direction.ASC, orderBy)));
	}

	@Override
	public AppAddonServices findByAppAddOnId(int id) {
		return appAddonServicesRepository.getOne(id);
	}

	@Override
	public void update(AppAddonServices appService) {
		appAddonServicesRepository.saveAndFlush(appService);		
	}

}
