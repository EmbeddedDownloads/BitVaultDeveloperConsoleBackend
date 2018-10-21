package org.bitvault.appstore.commons.application.dao;

import java.util.List;

import org.bitvault.appstore.cloud.model.AppApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AppApplicationRepository extends JpaRepository<AppApplication, Integer> {

	@Query("select app from AppApplication app where app.packageName = ?1")
	AppApplication findAppApplicationByPackageName(String packageName);

	@Query("select app from AppApplication app where app.appApplicationId = ?1")
	AppApplication findAppApplicationByAppId(Integer appId);

	@Query("select app from AppApplication app where app.userId = ?1")
	Page<AppApplication> findAppApplicationByUserId(String userId, Pageable pageable);

	@Query("select app from AppApplication app where app.userId = ?1 and app.status in ?2")
	Page<AppApplication> findAppApplicationByUserIdAndStatus(String userId, List<String> status, Pageable pageable);

	@Query("select app from AppApplication app where app.appCategory.appCategoryId in ?1")
	Page<AppApplication> findAppApplicationByCategory(List<Integer> categoryIdList, Pageable pageable);

	@Query("select app from AppApplication app where app.status in ?1")
	Page<AppApplication> findAppApplicationByStatus(List<String> status, Pageable pageable);

	@Modifying
	@Query("Update AppApplication app set app.status = ?1 where app.appApplicationId = ?2")
	Integer changeApplicationStatus(String status, Integer appId);

	@Query("select app from AppApplication app where app.appName like %?1%")
	Page<AppApplication> findAppApplicationByAppName(String appName, Pageable pageable);

	@Query("select count(app.appApplicationId) from AppApplication app where app.appPrice = :price and app.status in (:status)")
	int getFreeAppsCount(@Param("price") float price, @Param("status") List<String> appStatus);

	@Query("select count(app.appApplicationId) from AppApplication app where app.appPrice > :price and app.status in (:status)")
	int getPaidAppsCount(@Param("price") float price, @Param("status") List<String> appStatus);

	@Query("select count(app) from AppApplication app where app.appCategory.appCategoryId = ?1")
	int getCountAppByCategoryId(Integer categoryId);
	
	@Modifying
	@Query("update AppApplication app set app.appCategory.appCategoryId = :categoryId where app.appApplicationId in (:appIdList)")
	void updtateAppApplication(@Param("appIdList") List<Integer> appIdList, @Param("categoryId") Integer categoryId);
	
	@Modifying
    @Query("delete  From AppApplication app where app.appApplicationId=?1")
    void deleteAppApplicationByApplicationId(Integer applicationId);
	@Query("select app from AppApplication app where app.status in ?1 and app.appApplicationId in ?2")
	Page<AppApplication> findAppApplicationByStatusList(List<String> status, List<Integer> id,Pageable pageable);
	
}