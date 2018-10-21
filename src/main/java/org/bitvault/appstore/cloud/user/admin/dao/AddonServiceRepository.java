package org.bitvault.appstore.cloud.user.admin.dao;

import org.bitvault.appstore.cloud.model.AddonService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddonServiceRepository extends JpaRepository<AddonService, Integer>{
	AddonService findByAddonServiceName(String serviceName);
}
