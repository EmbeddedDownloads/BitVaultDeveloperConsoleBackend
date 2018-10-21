package org.bitvault.appstore.commons.application.dao;

import java.util.List;

import org.bitvault.appstore.cloud.model.AppImage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AppImagesRepository extends JpaRepository<AppImage, Integer> {

	@Query("select appImage from AppImage appImage where appImage.appApplication.appApplicationId = ?1")
	List<AppImage> findAppImagesByApplicatinId(Integer applicationId);

	@Query("select appImage from AppImage appImage where appImage.appApplication.appApplicationId = ?1 and appImage.status = ?2")
	List<AppImage> findAppImagesByApplicatinIdAndImageStatus(Integer applicationId, String status);

	@Query("select appImage from AppImage appImage where appImage.appApplication.appApplicationId = ?1 and appImage.imageType=?2")
	List<AppImage> findAppImagesByApplicatinIdAndType(Integer applicationId, String imageType);

	@Query("select count(appImage)  from AppImage appImage where appImage.imageType = ?1 and appImage.appApplication.appApplicationId = ?2")
	int getAppImageCountByTypeAndAppId(String type, Integer applicationId);

	@Query("select appImage from AppImage appImage where appImage.imageType = ?1 and appImage.appApplication.status = ?2")
	List<AppImage> findBanner(String type, String status, Pageable pageable);

	@Modifying
	@Query("update AppImage appImage set appImage.status = ?1 where  appImage.appApplication.appApplicationId = ?2 and appImage.imageType != 'banner'")
	int updateImageStatus(String status, Integer applicationId);

	@Modifying
    @Query("delete  From AppImage appImage where appImage.appApplication.appApplicationId=?1")
    void deleteAppImageByApplicationId(Integer applicationId);

}
