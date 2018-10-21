package org.bitvault.appstore.mobile.dao;

import org.bitvault.appstore.cloud.model.MobileUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MobileUserRepository extends JpaRepository<MobileUser, Long> {

	@Query("select mobileUser from MobileUser mobileUser where mobileUser.publicAdd = ?1")
	MobileUser findMobileUserByPublicAddress(String publicAddress);
	
	

	//Set<MobileUser> getAllMobileUserByFirstAndLastDate(Calendar firstDate, Calendar lastDate);
}
