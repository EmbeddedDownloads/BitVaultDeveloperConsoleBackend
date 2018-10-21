package org.bitvault.appstore.cloud.user.common.dao;

import java.util.List;

import org.bitvault.appstore.cloud.model.Request;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestRepository extends JpaRepository<Request, Integer> {
	@Query("FROM Request r where r.status in (:status) and r.requestType = 1")
	Page<Request> findAllUserRequests(@Param("status") List<String> status, Pageable pageable);

	@Query("select request from Request request where request.applicationId = ?1")
	Request findRequestByAppId(Integer appId);

	@Query("from Request request where request.status in ?1 and request.requestType.requestTypeId in ?2")
	Page<Request> getListOfAllAppsRequest(List<String> status, List<Integer> requestTypeId, Pageable pageable);

	@Modifying
	@Query("delete  From Request request where request.applicationId=?1")
	void deleteRequestByApplicationId(Integer applicationId);

//	@Query("FROM Request r where r.userId = :userId and r.applicationId = :applicationId and r.status in (:status) and r.requestType.requestTypeId in (:requestType)")
	
	@Query("FROM Request r where r.userId = :userId and r.status in (:status) and r.requestType.requestTypeId in (:requestType)")
	Page<Request> findByUserIdAndStatusInAndRequestTypeIn(@Param("userId") String userId,
			@Param("status") List<String> status, @Param("requestType") List<Integer> requestType, Pageable page);

	@Modifying
	@Query("delete  From Request request where request.userId=?1")
	void deleteRequestByuserId(String userId);
	
	@Query("Select request From Request request where request.userId=?1 ")
	Page<Request> findRequestActivityByUserId(String userId, Pageable page);

}
