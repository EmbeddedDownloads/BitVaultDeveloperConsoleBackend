package org.bitvault.appstore.cloud.user.common.dao;

import java.util.List;

import org.bitvault.appstore.cloud.model.Request;
import org.bitvault.appstore.cloud.model.RequestActivity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestActivityRepository extends JpaRepository<RequestActivity, Integer> {
	@Query("Select request From RequestActivity request where request.user_id=?1 ")
	Page<RequestActivity> findRequestActivityByUserId(String userId, Pageable page);
	
	@Query("Select request From RequestActivity request where request.user_id=?1 And request.seenStatus=?2")
	Page<RequestActivity> findAlertRequestActivityByUserId(String userId,String seenStatus ,Pageable page);

	@Query("Select request From RequestActivity request where request.applicationId=?1 And request.user_id=?2")
	Page<RequestActivity> findRequestActivityByUserIdAndApplicationId(Integer applicationId, String userId, Pageable page);

    @Modifying
    @Query("delete  From RequestActivity request where request.applicationId=?1")
    void deleteRequestActivityByApplicationId(Integer applicationId);
    
    @Query("FROM RequestActivity r where r.user_id = :userId and r.status in (:status) and r.requestTypeId in (:requestTypeId) and r.applicationId = :applicationId")
	Page<RequestActivity> findByUserIdAndStatusInAndRequestTypeIn(@Param("userId") String userId,
			@Param("status") List<String> status, @Param("requestTypeId") List<Integer> requestTypeId,
			@Param("applicationId") Integer applicationId, Pageable page);
    
    
    @Query("FROM RequestActivity r where r.status in (:status) and r.seenStatus = :seenStatus")
	Page<RequestActivity> findByUserIdAndStatusIn(
			@Param("status") List<String> status,@Param("seenStatus") String seenStatus, Pageable page);
    
    @Query("Select request From RequestActivity request where request.requestId=?1 ")
	Page<RequestActivity> findRequestActivityByRequestId(Integer requestId, Pageable page);

    List<RequestActivity> findByActivityIdIn(List<Integer> requestIds);
    
    
    
//    @Query("Select request From RequestActivity request where request.activityId IN(?1) ")
//    void markAlertsAsSeen(List<Integer> requestIds);
    
//    @Query("update RequestActivity request SET where request.seenStatus=?1 where request.activityId IN(?2) ")
//    void f(String status,List<Integer> requestIds);

}
