package org.bitvault.appstore.cloud.user.common.dao;

import org.bitvault.appstore.cloud.model.AppAddonServices;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AppAddonServicesRepository extends JpaRepository<AppAddonServices, Integer>{
	@Query("from AppAddonServices app where userId = :userId")
	Page<AppAddonServices> getAllAddonServicesByUserId(@Param("userId") String userId, Pageable page);
	
	@Query("from AppAddonServices app where app.packageName = :packageName")
	Page<AppAddonServices> getAllAddonServicesByPackageName(@Param("packageName") String packageName, Pageable page);
	
	@Query("from AppAddonServices app where app.addonService.addonServiceId = :addonServiceId and app.packageName = :packageName")
	AppAddonServices getAddonServicesByUserId(@Param("addonServiceId") int addonServiceId,@Param("packageName") String packageName);
}
