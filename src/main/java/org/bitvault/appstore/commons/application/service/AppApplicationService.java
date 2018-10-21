package org.bitvault.appstore.commons.application.service;

import java.util.List;
import java.util.Map;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.bitvault.appstore.cloud.dto.AppApplication;
import org.bitvault.appstore.cloud.dto.AppImage;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.bitvault.appstore.cloud.main.BitvaultMain;
import org.bitvault.appstore.cloud.model.Request;
import org.springframework.web.multipart.MultipartFile;

public interface AppApplicationService {

	AppApplication saveAppApllication(AppApplication application) throws BitVaultException;

	Map<String, Object> listOfAllAppApplication(int page, int size, String direction, String property)
			throws BitVaultException;

	AppApplication findAppApplicationByPackageName(String packageName) throws BitVaultException;

	Map<String, Object> findAppApplicationByUserId(String userId, int page, int size, String direction, String property)
			throws BitVaultException;

	Map<String, Object> findAppApplicationByUserIdAndStatus(String userId, List<String> status, int page, int size,
			String direction, String property) throws BitVaultException;

	Map<String, Object> findAppApplicationByCategory(List<Integer> categoryIdList, int page, int size, String direction,
			String property) throws BitVaultException;

	Map<String, Object> findAppApplicationByStatus(List<String> status, int page, int size, String direction,
			String property) throws BitVaultException;

	Integer updateAppStatus(String status, Integer appId) throws BitVaultException;

	AppApplication findApplicationByAppId(Integer applicationId) throws BitVaultException;

	Map<String, Object> findAppApplicationByAppName(String appName, int page, int size, String direction,
			String property) throws BitVaultException;
	Map<String, Object> searchAppApplicationByAppName(String appName,String userId, int page, int size, String direction,
			String property) throws BitVaultException;

	Map<String, Object> uploadApk(String appName, String language, MultipartFile apkFile, String fileName,
			String userId,String privateKeys,String uploadedFrom) throws BitVaultException;

	String uploadApkToS3(AppApplication appdto) throws BitVaultException;

	void saveAppApllicationRequest(String userId, Integer appId, String requestStatus) throws BitVaultException;

	int getFreeAppsCount(float price, List<String> appStatus);

	int getPaidAppsCount(float price, List<String> appStatus);

	String uploadApkIcon(MultipartFile apkIcon, AppApplication appdto);

	List<String> uploadApkImages(List<MultipartFile> apkImages, AppApplication appdto, String imageType);

	void deleteAppImage(AppImage appImageDto, String userId);

	void deleteTempApkFolder(String path) throws BitVaultException;

	int getCountAppByCategoryId(Integer categoryId);
	
	void updateAppApplication(List<Integer> appIdList, Integer categoryId);
	void updateAverageRating(Integer applicationId, float averageRating);
	String deleteDocument(String appApplicationId);
	Map<String, Object> findAppApplicationByStatusList(List<String> status,List<Integer> applicationId, int page, int size, String direction,
			String property) throws BitVaultException;

}
