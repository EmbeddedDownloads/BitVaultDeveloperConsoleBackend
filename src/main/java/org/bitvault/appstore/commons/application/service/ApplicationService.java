package org.bitvault.appstore.commons.application.service;

import java.util.List;
import java.util.Map;

import org.bitvault.appstore.cloud.dto.Application;
import org.bitvault.appstore.cloud.dto.ApplicationDetailDto;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.bitvault.appstore.mobile.dto.ApplicationLatestVersionDTO;

public interface ApplicationService {

	Application saveApllication(org.bitvault.appstore.cloud.model.Application application) throws BitVaultException;

	Map<String, Object> listOfAllApplication(int page, int size, String direction, String property)
			throws BitVaultException;

	Map<String, Object> listOfAllApplicationByAppType(int page, int size, String direction, String property,
			int appTypeId) throws BitVaultException;

	Application findApplicationByPackageName(String packageName) throws BitVaultException;

	Map<String, Object> findApplicationByUserId(String userId, int page, int size, String direction, String property)
			throws BitVaultException;

	Map<String, Object> findApplicationByCategory(List<Integer> categoryIdList, int page, int size, String direction,
			String property) throws BitVaultException;

	Map<String, Object> findCategoriesApplication(int page, int size, String direction, String property)
			throws BitVaultException;

	Integer deleteUnpublishedAppByPackageName(String packageName) throws BitVaultException;

	Application findApplicationByAppId(Integer appId) throws BitVaultException;

	String findAppApkUrlByAppId(Integer appId) throws BitVaultException;

	String findAppApkUrlByPackageName(String packageName) throws BitVaultException;

	ApplicationDetailDto findPublishedAppDetailbyAppId(Integer appId) throws BitVaultException;

	Map<String, Object> findApplicationByAppName(String appName, int page, int size, String direction, String property)
			throws BitVaultException;

	Map<String, Object> searchApplicationByAppName(String appName, int page, int size, String direction,
			String property) throws BitVaultException;

	void deleteApp(Integer appId) throws BitVaultException;

	int getActiveAppsCount();

	void updateApplication(List<Integer> appIdList, Integer categoryId);

	int upadteAppIconById(Integer appId, String appIconUrl) throws BitVaultException;

	/**
	 * Service : get latest versions of all appIds
	 * 
	 * @param appIds
	 *            list lof app Ids
	 * @return appIds with their latest version numbers
	 * @throws BitVaultException
	 */
	List<ApplicationLatestVersionDTO> getAppsLatestVersions(List<String> packageNames) throws BitVaultException;

	void updateAverageRating(Integer applicationId, float averageRating);
	 String deleteDocument(String applicationId);  
	 
	 Map<String, Object> findNativeApplication(String status, int page, int size, String direction, String property)
				throws BitVaultException;
}
