package org.bitvault.appstore.commons.application.dao;

import org.bitvault.appstore.cloud.model.ApplicationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository("ApplicationType")
public interface AppTypeRepository extends JpaRepository<ApplicationType, Long> {

	@Query("select appType from ApplicationType appType where appType.appTypeId = ?1")
	public ApplicationType findAppTypeById(Integer Id);

}
