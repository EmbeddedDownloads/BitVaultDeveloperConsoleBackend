package org.bitvault.appstore.commons.application.dao;

import java.util.List;

import org.bitvault.appstore.cloud.model.AppDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AppDetailRepository extends JpaRepository<AppDetail, Integer> {

	@Query("select appDetail from AppDetail appDetail where appDetail.appApplication.appApplicationId = ?1 and appDetail.appVersionName = ?2")
	AppDetail findApplicationDetailByAppIdAndVersion(Integer applicationId, String appVersionName);

	@Modifying
	@Query(" Update  AppDetail appDetail set appDetail.status = ?1 where appDetail.appApplication.appApplicationId = ?2 and appDetail.appVersionName = ?3")
	Integer updateAppDetailStatus(String status, Integer applicationId, String appVersionName);

	@Query("select appDetail from AppDetail appDetail where appDetail.appApplication.appApplicationId = ?1 and appDetail.status in ?2 and appDetail.status !='published'")
	Page<AppDetail> appDetailArtifact(Integer applicationId, List<String> status, Pageable pageable);

	@Modifying
    @Query("delete  From AppDetail appDetail where appDetail.appApplication.appApplicationId =?1")
    void deleteAppDetailByApplicationId(Integer applicationId);

}
