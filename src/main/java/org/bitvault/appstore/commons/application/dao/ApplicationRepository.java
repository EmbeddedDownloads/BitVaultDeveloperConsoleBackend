package org.bitvault.appstore.commons.application.dao;

import java.util.List;

import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.bitvault.appstore.cloud.model.Application;
import org.bitvault.appstore.mobile.dto.ApplicationLatestVersionDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository("Application")
public interface ApplicationRepository extends JpaRepository<Application, Integer> {

	@Query("select app from Application app where app.packageName = ?1")
	Application findApplicationByPackageName(String packageName);

	@Query("select app from Application app where app.applicationId = ?1")
	Application findApplicationByAppId(Integer applicationId);

	@Query("select app from Application app where app.userId = ?1")
	Page<Application> findApplicationByUserId(String userId, Pageable pageable);
	
	@Query("select app from Application app where app.applicationType.appTypeId = ?1")
	Page<Application> findApplicationByAppTypeId(Integer appTypeId, Pageable pageable);

	@Query("select app from Application app where app.appCategory.appCategoryId in ?1")
	Page<Application> findApplicationByCategory(List<Integer> categoryIdList, Pageable pageable);

	@Query("select app from Application app where app.appCategory.appCategoryId = ?1")
	Page<Application> findApplicationByCategoryById(Integer categoryId, Pageable pageable);

	@Query("delete from Application app where app.packageName = ?1")
	Integer deleteUnpublishedAppByPackageName(String packageName);

	@Query("select app.apkUrl from Application app where app.applicationId = ?1")
	String findAppApkUrlByAppId(Integer appId);

	@Query("select app.apkUrl from Application app where app.packageName = ?1")
	String findAppApkUrlByPackageName(String packageName);

	@Query("select app from Application app where app.appName like %?1%   or app.title like  %?1%")
	Page<Application> findApplicationByAppName(String appName, Pageable pageable);

	@Query("select count(1) from Application app")
	int getActiveAppsCount();

	@Modifying
	@Query("Update Application app set app.appIconUrl = ?2 where app.applicationId = ?1  ")
	int upadateAppIconById(Integer appId, String appIconUrl);
	
	@Modifying
	@Query("Update Application app set app.appCategory.appCategoryId = :categoryId where app.applicationId in (:appIdList)")
	void upadateApplication(@Param("appIdList") List<Integer> appIdList, @Param("categoryId") Integer categoryId);

	/**
	 * Dao : get latest versions of all appIds
	 * 
	 * @param appIds
	 *            list lof app Ids
	 * @return appIds with their latest version numbers
	 * 
	 */
	@Query("select new org.bitvault.appstore.mobile.dto.ApplicationLatestVersionDTO(app.applicationId, app.latestVersionNo, app.packageName, app.averageRating) from Application app where app.packageName in ?1")
	List<ApplicationLatestVersionDTO> findAppsLatestVersions(List<String> packageNames);
	@Modifying
    @Query("delete  From Application app where app.applicationId=?1")
    void deletApplicationByApplicationId(Integer applicationId);
	
	@Query("select app from Application app  where status=?1" )
	Page<Application>findNativeApplication(String status,Pageable page);
}
