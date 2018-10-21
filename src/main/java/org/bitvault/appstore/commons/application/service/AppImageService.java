package org.bitvault.appstore.commons.application.service;

import java.util.List;

import org.bitvault.appstore.cloud.dto.AppImage;
import org.bitvault.appstore.cloud.exception.BitVaultException;

public interface AppImageService {
	List<AppImage> findAppImagesByApplicatinId(Integer applicationId) throws BitVaultException;

	List<AppImage> findAppImagesByApplicatinIdAndImageStatus(Integer applicationId, String status)
			throws BitVaultException;

	List<AppImage> saveAppImage(List<String> appImageList, Integer applicationId, String imageType, String imageStatus);

	int getAppImageCountByType(String type, Integer applicationId);

	void deleteAppImageById(int Id);

	AppImage findImageByImageId(int imageId);

	List<AppImage> findAppImagesByApplicatinIdAndType(Integer applicationId, String imageType);

	List<AppImage> findBanner(String type, String status, int page, int size);

	int updateImageStatus(String status, Integer applicationId);

}
