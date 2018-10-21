package org.bitvault.appstore.commons.application.service;

import java.util.ArrayList;
import java.util.List;

import org.bitvault.appstore.cloud.aws.service.AwsS3Service;
import org.bitvault.appstore.cloud.constant.DbConstant;
import org.bitvault.appstore.cloud.constant.ErrorCode;
import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.dto.AppCategory;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.bitvault.appstore.cloud.utils.Utility;
import org.bitvault.appstore.commons.application.dao.AppCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service("AppCategoryService")
@Transactional
public class AppCategoryServiceImpl implements AppCategoryService {

	@Autowired
	AppCategoryRepository appCategoryRepository;

	@Autowired
	AwsS3Service awsService;

	@Override
	public List<AppCategory> findAllAppCategory() throws BitVaultException {
		AppCategory appCategoryDTO = null;
		List<AppCategory> appCategoryDTOList = new ArrayList<AppCategory>();
		List<org.bitvault.appstore.cloud.model.AppCategory> appCategoryList = null;
		try {
			appCategoryList = appCategoryRepository.findAll();

			for (org.bitvault.appstore.cloud.model.AppCategory appCategory : appCategoryList) {
				appCategoryDTO = appCategory.populateAppCategoryDTO(appCategory);
				appCategoryDTOList.add(appCategoryDTO);

			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED, ErrorCode.SOME_ERROR_OCCURED_CODE);
		}
		if (null == appCategoryList || appCategoryList.size() == 0) {
			throw new BitVaultException(ErrorMessageConstant.RESULT_NOT_FOUND, ErrorCode.RESULT_NOT_FOUND_CODE);
		}

		return appCategoryDTOList;
	}

	@Override
	public AppCategory findCategoryById(Integer id) throws BitVaultException {
		AppCategory appCategoryDTO = null;
		try {
			org.bitvault.appstore.cloud.model.AppCategory appCategory = appCategoryRepository.findCategoryById(id);
			if (null != appCategory)
				appCategoryDTO = appCategory.populateAppCategoryDTO(appCategory);

		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED, ErrorCode.SOME_ERROR_OCCURED_CODE);
		}
		return appCategoryDTO;
	}

	@Override
	public void updateAppCategoryCountByCatId(int catId, int updatedValue) throws BitVaultException {
		try {
			org.bitvault.appstore.cloud.model.AppCategory appCategory = appCategoryRepository.getOne(catId);
			if (!(appCategory.getCategoryCount() == 0 && updatedValue < 0)) {
				appCategory.setCategoryCount(appCategory.getCategoryCount() + (updatedValue));
				appCategoryRepository.saveAndFlush(appCategory);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.UNABLE_TO_UPDATE);
		}

	}

	// @Override
	// public AppCategory saveAppCategory(AppCategory appCategoryDto) {
	// org.bitvault.appstore.cloud.model.AppCategory appCategoryEntity = null;
	// try {
	// appCategoryEntity =
	// appCategoryDto.populateAppCategoryDTO(appCategoryDto);
	// // appCategoryEntity.setCategoryCount(categoryCount);
	// appCategoryEntity =
	// appCategoryRepository.saveAndFlush(appCategoryEntity);
	// } catch (Exception e) {
	// // TODO: handle exception
	// }
	// return null;
	// }

	@Override
	public AppCategory saveAppCategory(org.bitvault.appstore.cloud.model.AppCategory appCategory) {
		AppCategory appCategorydto = null;
		try {
			appCategory = appCategoryRepository.saveAndFlush(appCategory);
			appCategorydto = appCategory.populateAppCategoryDTO(appCategory);
			appCategorydto.setAppTypeId(appCategory.getApplicationType().getAppTypeId());

		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.UNABLE_TO_SAVE);
		}

		return appCategorydto;
	}

	@Override
	public String uploadCategoryIcon(MultipartFile file, String OldIconUrl) throws BitVaultException {
		String iconUrl = null;
		try {
			if (file.getContentType().contains("image")) {
				String fileName = Utility.replaceFileName(file.getOriginalFilename());
				if (null != fileName) {
					iconUrl = awsService.uploadCategoryIcon(file, fileName, OldIconUrl);

				}
			} else {
				throw new BitVaultException(ErrorMessageConstant.INVALID_FILE_TYPE);
			}
		} catch (Exception e) {
			if (e instanceof BitVaultException) {
				throw new BitVaultException(e.getMessage());
			}
			throw new BitVaultException(ErrorMessageConstant.UNABLE_TO_UPLOAD_FILE);
		}
		return iconUrl;
	}

	@Override
	public String uploadCategoryBanner(MultipartFile file, String OldBannerUrl) throws BitVaultException {
		String bannerUrl = null;
		try {
			if (file.getContentType().contains("image")) {
				String fileName = Utility.replaceFileName(file.getOriginalFilename());
				if (null != fileName) {
					bannerUrl = awsService.uploadCategoryBanner(file, fileName, OldBannerUrl);

				}
			} else {
				throw new BitVaultException(ErrorMessageConstant.INVALID_FILE_TYPE);
			}
		} catch (Exception e) {
			if (e instanceof BitVaultException) {
				throw new BitVaultException(e.getMessage());
			}
			throw new BitVaultException(ErrorMessageConstant.UNABLE_TO_UPLOAD_FILE);
		}
		return bannerUrl;
	}

	@Override
	public AppCategory findCategoryByName(String categoryName) throws BitVaultException {
		AppCategory appCategoryDTO = null;
		try {
			org.bitvault.appstore.cloud.model.AppCategory appCategory = appCategoryRepository
					.findCategoryByName(categoryName);
			if (null != appCategory)
				appCategoryDTO = appCategory.populateAppCategoryDTO(appCategory);

		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED, ErrorCode.SOME_ERROR_OCCURED_CODE);
		}
		return appCategoryDTO;
	}

	@Override
	public List<AppCategory> findAppCategoryByStatus(List<String> status, int page, int size, String direction,
			String property) {
		AppCategory appCategoryDTO = null;
		List<AppCategory> appCategoryDTOList = new ArrayList<AppCategory>();
		Page<org.bitvault.appstore.cloud.model.AppCategory> appCategoryList = null;
		try {
			if (direction.equalsIgnoreCase(DbConstant.ASC)) {
				appCategoryList = appCategoryRepository.findAppCategoryByStatus(status,
						new PageRequest(page, size, Sort.Direction.ASC, property));
			} else {
				appCategoryList = appCategoryRepository.findAppCategoryByStatus(status,
						new PageRequest(page, size, Sort.Direction.DESC, property));

			}
			for (org.bitvault.appstore.cloud.model.AppCategory appCategory : appCategoryList) {
				appCategoryDTO = appCategory.populateAppCategoryDTO(appCategory);
				appCategoryDTOList.add(appCategoryDTO);

			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);
		}

		return appCategoryDTOList;
	}

	@Override
	public void updateAppCategoryByCategoryId(String status, Integer categoryId) {

		try {
			appCategoryRepository.updateAppCategoryByCategoryId(status, categoryId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);
		}

	}

	@Override
	public List<AppCategory> findAppCategoryByAppTypeId(Integer appTypeId, List<String> status) {
		AppCategory appCategoryDTO = null;
		List<AppCategory> appCategoryDTOList = new ArrayList<AppCategory>();
		List<org.bitvault.appstore.cloud.model.AppCategory> appCategoryList = null;
		try {
			appCategoryList = appCategoryRepository.findAppCategoryByAppTypeId(appTypeId, status);

			for (org.bitvault.appstore.cloud.model.AppCategory appCategory : appCategoryList) {
				appCategoryDTO = appCategory.populateAppCategoryDTO(appCategory);
				appCategoryDTOList.add(appCategoryDTO);

			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);
		}

		return appCategoryDTOList;
	}

	@Override
	public List<AppCategory> findCategoryWithApplication(int page, int size, String direction, String property) {
		AppCategory appCategoryDTO = null;
		List<AppCategory> appCategoryDTOList = new ArrayList<AppCategory>();
		Page<org.bitvault.appstore.cloud.model.AppCategory> appCategoryList = null;
		try {
			appCategoryList = appCategoryRepository
					.findCategoryWithApplication(new PageRequest(page, size, new Sort(Direction.DESC, property)));

			for (org.bitvault.appstore.cloud.model.AppCategory appCategory : appCategoryList) {
				appCategoryDTO = appCategory.populateAppCategoryDTO(appCategory);
				appCategoryDTOList.add(appCategoryDTO);

			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);
		}

		return appCategoryDTOList;
	}

}
