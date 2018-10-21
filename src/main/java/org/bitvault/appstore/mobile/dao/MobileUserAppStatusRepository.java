package org.bitvault.appstore.mobile.dao;

import java.util.List;

import org.bitvault.appstore.cloud.model.MobileUserAppStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
@Repository
public interface MobileUserAppStatusRepository extends JpaRepository<MobileUserAppStatus, Integer>  
{
	@Query("Select mobileUserAppStatus from MobileUserAppStatus mobileUserAppStatus where mobileUserAppStatus.application.appApplicationId=?1 and mobileUserAppStatus.mobileUser.mobileUserId=?2")
	MobileUserAppStatus findMobileStatusByAppIdAndMobUserId(Integer applicationId,Integer mobileUserId);

	@Query("Select mobileUserAppStatus from MobileUserAppStatus mobileUserAppStatus where mobileUserAppStatus.application.appApplicationId=?1")
	List<MobileUserAppStatus> findMobileStatusByAppId(Integer applicationId);
	
	@Query("Select mobileUserAppStatus from MobileUserAppStatus mobileUserAppStatus where mobileUserAppStatus.application.appApplicationId=?1 and mobileUserAppStatus.status=?2")
	List<MobileUserAppStatus> findMobileStatusByAppIdAndStatus(Integer applicationId,String status);

	@Modifying
    @Query("delete  From MobileUserAppStatus mobileUserApp where mobileUserApp.application.appApplicationId=?1")
    void deletemobileUserAppStatusByApplicationId(Integer applicationId);
}

