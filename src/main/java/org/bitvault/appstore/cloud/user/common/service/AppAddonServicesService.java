package org.bitvault.appstore.cloud.user.common.service;

import org.bitvault.appstore.cloud.model.AppAddonServices;
import org.springframework.data.domain.Page;

public interface AppAddonServicesService {
	void save(AppAddonServices appService, int serviceName);
	void update(AppAddonServices appService);
	Page<AppAddonServices> getAllAppAddonServices(String userId, int page, int size, String orderBy);
	Page<AppAddonServices> getAllAppAddonServicesForAnApp(String packageName, int page, int size, String orderBy);
	AppAddonServices getAppAddOnService(String userId, int addonServiceId,String packageName);
	AppAddonServices findByAppAddOnId(int id);
	AppAddonServices getAppAddOnService(int addonServiceId,String packageName);
	
}
