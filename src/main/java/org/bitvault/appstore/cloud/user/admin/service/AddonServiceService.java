package org.bitvault.appstore.cloud.user.admin.service;

import org.bitvault.appstore.cloud.model.AddonService;
import org.springframework.data.domain.Page;

public interface AddonServiceService {
	void save(AddonService addonService);
	Page<AddonService> getAllAddonService(int page, int size, String orderBy);
	AddonService findByServiceName(String serviceName);
}
