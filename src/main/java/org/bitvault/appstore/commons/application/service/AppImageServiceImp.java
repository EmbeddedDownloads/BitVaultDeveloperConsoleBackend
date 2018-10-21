package org.bitvault.appstore.commons.application.service;

import java.util.ArrayList;
import java.util.List;

import org.bitvault.appstore.cloud.aws.service.AwsS3Service;
import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.dto.AppImage;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.bitvault.appstore.cloud.model.AppApplication;
import org.bitvault.appstore.commons.application.dao.AppApplicationRepository;
import org.bitvault.appstore.commons.application.dao.AppImagesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("AppImageService")
@Transactional
public class AppImageServiceImp implements AppImageService {
	@Autowired
	AppImagesRepository appImagesRepository;

	@Autowired
	AppApplicationRepository appApplicationRepository;
	@Autowired
	AwsS3Service awsService;
	
	@Override
	public List<AppImage> findAppImagesByApplicatinId(Integer applicationId) throws BitVaultException {
		List<org.bitvault.appstore.cloud.model.AppImage> appImageList = null;
		List<AppImage> appDtoList = new ArrayList<AppImage>();
		AppImage appImageDTo = null;
		try {
			appImageList = appImagesRepository.findAppImagesByApplicatinId(applicationId);
			for (org.bitvault.appstore.cloud.model.AppImage appImage : appImageList) {
				appImageDTo = appImage.populateAppImage(appImage);
				appDtoList.add(appImageDTo);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);
		}

		return appDtoList;
	}

	@Override
	public List<AppImage> findAppImagesByApplicatinIdAndImageStatus(Integer applicationId, String status) throws BitVaultException {
		List<org.bitvault.appstore.cloud.model.AppImage> appImageList = null;
		List<AppImage> appDtoList = new ArrayList<AppImage>();
		AppImage appImageDTo = null;
		try {
			appImageList = appImagesRepository.findAppImagesByApplicatinIdAndImageStatus(applicationId, status);
			for (org.bitvault.appstore.cloud.model.AppImage appImage : appImageList) {
				appImageDTo = appImage.populateAppImage(appImage);
				appDtoList.add(appImageDTo);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);
		}

		return appDtoList;
	}

	
	
	@Override
	public List<AppImage> saveAppImage(List<String> appImageList, Integer applicationId, String imageType,
			String imageStatus) {
		AppImage appImageDTo = null;
		List<AppImage> appDtoList = new ArrayList<AppImage>();
		try {
			org.bitvault.appstore.cloud.model.AppImage appImage = null;
			AppApplication app = appApplicationRepository.findAppApplicationByAppId(applicationId);

			List<org.bitvault.appstore.cloud.model.AppImage> list = new ArrayList<org.bitvault.appstore.cloud.model.AppImage>();
			for (String imageUrl : appImageList) {
				appImage = new org.bitvault.appstore.cloud.model.AppImage();
				appImage.setAppApplication(app);
				appImage.setImageType(imageType);
				appImage.setAppImageUrl(imageUrl);
				appImage.setStatus(imageStatus);
				org.bitvault.appstore.cloud.model.AppImage appImages = appImagesRepository.saveAndFlush(appImage);
				list.add(appImages);
			}
			for (org.bitvault.appstore.cloud.model.AppImage appImageModel : list) {
				appImageDTo = appImageModel.populateAppImage(appImageModel);
				appDtoList.add(appImageDTo);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);
		}

		return appDtoList;
	}

	@Override
	public int getAppImageCountByType(String type, Integer applicationId) {
		int count = 0;
		try {
			count = appImagesRepository.getAppImageCountByTypeAndAppId(type, applicationId);

		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);
		}

		return count;
	}

	@Override
	public void deleteAppImageById(int id) {

		try {
			appImagesRepository.delete(id);

		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.UNABLE_TO_DELETE);
		}

	}

	@Override
	public AppImage findImageByImageId(int imageId) {
		org.bitvault.appstore.cloud.model.AppImage appImage = null;
		AppImage appImageDto = null;
		try {
			appImage = appImagesRepository.findOne(imageId);
			if (null != appImage) {
				appImageDto = appImage.populateAppImage(appImage);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);
		}
		return appImageDto;
	}

	@Override
	public List<AppImage> findBanner(String type, String status, int page, int size) {
		AppImage appImageDTo = null;
		List<AppImage> appDtoList = null;
		List<org.bitvault.appstore.cloud.model.AppImage> appImageList = null;

		try {
			appDtoList = new ArrayList<AppImage>();
			appImageList = appImagesRepository.findBanner(type, status, new PageRequest(page, size));
			for (org.bitvault.appstore.cloud.model.AppImage appImage : appImageList) {
				appImageDTo = appImage.populateAppImage(appImage);
				appDtoList.add(appImageDTo);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);
		}
		return appDtoList;
	}

	@Override
	public List<AppImage> findAppImagesByApplicatinIdAndType(Integer applicationId, String imageType) {
		List<org.bitvault.appstore.cloud.model.AppImage> appImageList = null;
		List<AppImage> appDtoList = new ArrayList<AppImage>();
		AppImage appImageDTo = null;
		try {
			appImageList = appImagesRepository.findAppImagesByApplicatinIdAndType(applicationId, imageType);
			for (org.bitvault.appstore.cloud.model.AppImage appImage : appImageList) {
				appImageDTo = appImage.populateAppImage(appImage);
				appDtoList.add(appImageDTo);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);
		}

		return appDtoList;
	}

	@Override
	public int updateImageStatus(String status, Integer applicationId) {
         int update = 0;
		try {
			update = appImagesRepository.updateImageStatus(status, applicationId);

		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);
		}
           return update;
	}

}
