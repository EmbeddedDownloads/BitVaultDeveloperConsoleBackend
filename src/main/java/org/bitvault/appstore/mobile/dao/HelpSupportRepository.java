package org.bitvault.appstore.mobile.dao;

import org.bitvault.appstore.cloud.model.HelpSupport;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HelpSupportRepository extends JpaRepository<HelpSupport, Integer>
{

	
	
}
