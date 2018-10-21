package org.bitvault.appstore.cloud.user.admin.service;

import org.bitvault.appstore.cloud.model.AddonService;
import org.bitvault.appstore.cloud.user.admin.dao.AddonServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AddonServiceServiceImpl implements AddonServiceService{
	
	@Autowired
	private AddonServiceRepository addonServiceRepository;
	
	@Override
	public void save(AddonService addonService) {
		addonServiceRepository.saveAndFlush(addonService);		
	}

	@Override
	public Page<AddonService> getAllAddonService(int page, int size, String orderBy) {
		return addonServiceRepository.findAll(new PageRequest(page, size, new Sort(Direction.ASC, orderBy)));
	}

	@Override
	public AddonService findByServiceName(String serviceName) {
		return addonServiceRepository.findByAddonServiceName(serviceName);
	}

}
