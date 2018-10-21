package org.bitvault.appstore.commons.application.service;

import java.util.List;

import org.bitvault.appstore.cloud.dto.AppCategory;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.springframework.web.multipart.MultipartFile;

public interface AppCategoryService {

	List<AppCategory> findAllAppCategory() throws BitVaultException;

//	@Secured("ADMIN")
	AppCategory findCategoryById(Integer id) throws BitVaultException;

	void updateAppCategoryCountByCatId(int catId, int updatedValue) throws BitVaultException;

	// AppCategory saveAppCategory(AppCategory appCategory);

	AppCategory saveAppCategory(org.bitvault.appstore.cloud.model.AppCategory appCategory);

	String uploadCategoryIcon(MultipartFile file, String OldIconUrl) throws BitVaultException;
	
	String uploadCategoryBanner(MultipartFile file, String OldBannerUrl) throws BitVaultException;

	AppCategory findCategoryByName(String categoryName) throws BitVaultException;

	List<AppCategory> findAppCategoryByStatus(List<String> status, int page, int size, String direction,
			String property);

	void updateAppCategoryByCategoryId(String status, Integer categoryId);

	List<AppCategory> findAppCategoryByAppTypeId(Integer appTypeId, List<String> status);

	List<AppCategory> findCategoryWithApplication(int page, int size, String direction, String property);
}
