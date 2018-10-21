package org.bitvault.appstore.cloud.dto;

import java.util.Map;

import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.bitvault.appstore.cloud.model.AppHistory;
import org.bitvault.appstore.cloud.utils.Utility;

public class AppInfoDto {
	private Integer applicationId;
	private Integer versionCode;

	private String versionName;
	private String email;
	private String website;
	private float app_price;
	private Integer subscription;
	private String fullDescription;
	private String shortDescription;
	private String whatsNew;
	private String title;
	private String privacyPolicyUrl;
	private Integer appCategoryId;
	private Integer appTypeId;
	private String language;
	private String status;
	private AppCategory appCategory;
	private ApplicationTypeDto appType;
	private String company;
	private String updateType;

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public Integer getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(Integer versionCode) {
		this.versionCode = versionCode;
	}

	public Integer getAppCategoryId() {
		return appCategoryId;
	}

	public void setAppCategoryId(Integer appCategoryId) {
		this.appCategoryId = appCategoryId;
	}

	public Integer getAppTypeId() {
		return appTypeId;
	}

	public void setAppTypeId(Integer appTypeId) {
		this.appTypeId = appTypeId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public float getApp_price() {
		return app_price;
	}

	public void setApp_price(float app_price) {
		this.app_price = app_price;
	}

	public Integer getSubscription() {
		return subscription;
	}

	public void setSubscription(Integer subscription) {
		this.subscription = subscription;
	}

	public String getFullDescription() {
		return fullDescription;
	}

	public void setFullDescription(String fullDescription) {
		this.fullDescription = fullDescription;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public Integer getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(Integer applicationId) {
		this.applicationId = applicationId;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public String getWhatsNew() {
		return whatsNew;
	}

	public void setWhatsNew(String whatsNew) {
		this.whatsNew = whatsNew;
	}

	public AppCategory getAppCategory() {
		return appCategory;
	}

	public void setAppCategory(AppCategory appCategory) {
		this.appCategory = appCategory;
	}

	public ApplicationTypeDto getAppType() {
		return appType;
	}

	public void setAppType(ApplicationTypeDto appType) {
		this.appType = appType;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPrivacyPolicyUrl() {
		return privacyPolicyUrl;
	}

	public void setPrivacyPolicyUrl(String privacyPolicyUrl) {
		this.privacyPolicyUrl = privacyPolicyUrl;
	}

	public AppApplication populateAppApplication(AppInfoDto dto, AppApplication appDto) {
		// if (!Utility.isStringEmpty(dto.getEmail())) {
		//
		// appDto.setEmail(dto.getEmail());
		//
		// }
		// if (!Utility.isStringEmpty(dto.getWebsite())) {
		//
		// appDto.setWebsite(dto.getWebsite());
		//
		// }
		// if (dto.getApp_price() != 0) {appInfoDto
		//
		// appDto.setAppPrice(dto.getApp_price());
		//
		// }
		// if (!Utility.isIntegerEmpty(dto.getSubscription())) {
		//
		// appDto.setSubscription(dto.getSubscription());
		//
		// }
		// if (!Utility.isStringEmpty(dto.getTitle())) {
		//
		// appDto.setTitle(dto.getTitle());
		//
		// }
		// if (!Utility.isStringEmpty(dto.getPrivacyPolicyUrl())) {
		// appDto.setPrivacyPolicyUrl(dto.getPrivacyPolicyUrl());
		//
		// }

		appDto.setPrivacyPolicyUrl(dto.getPrivacyPolicyUrl());
		appDto.setTitle(dto.getTitle());
		appDto.setSubscription(dto.getSubscription());
		appDto.setAppPrice(dto.getApp_price());
		appDto.setWebsite(dto.getWebsite());
		appDto.setEmail(dto.getEmail());

		return appDto;
	}

	public AppDetail populateAppDetail(AppInfoDto dto, AppDetail appDetailDto) {
		appDetailDto.setFullDescription(dto.getFullDescription());
		appDetailDto.setShortDescription(dto.getShortDescription());
		return appDetailDto;
	}

	public AppInfoDto populateAppInfoDto(AppDetail appDetailDto, AppApplication appDto, AppInfoDto appInfoDto) {

		appInfoDto.setLanguage(appDto.getLanguage());
		appInfoDto.setWebsite(appDto.getWebsite());
		appInfoDto.setPrivacyPolicyUrl(appDto.getPrivacyPolicyUrl());
		appInfoDto.setTitle(appDto.getTitle());
		appInfoDto.setWhatsNew(appDetailDto.getWhatsNew());
		appInfoDto.setEmail(appDto.getEmail());
		appInfoDto.setFullDescription(appDetailDto.getFullDescription());
		appInfoDto.setShortDescription(appDetailDto.getShortDescription());
		return appInfoDto;

	}

	public static Boolean validateSequence(String sequence, int minLength, int maxLength) {

		if (null == sequence) {
			return false;
		} else if (sequence.trim().isEmpty()) {
			return false;
		} else if (sequence.length() < minLength || sequence.length() > maxLength) {
			return false;
		} else {
			return true;
		}
	}

	public Map<String, String> validateAppInfoDto(AppInfoDto dto, String appIconUrl, String apkUrl, int imageCount,
			int bannerCount) {
		Map<String, String> errorMap = null;

		if (Utility.isStringEmpty(dto.getTitle())) {
			errorMap = Utility.ErrorMap("title", ErrorMessageConstant.TITLE_ERROR, errorMap);

		}

		if (!Utility.isStringEmpty(dto.getTitle()) && !validateSequence(dto.getTitle(), 3, 30)) {
			errorMap = Utility.ErrorMap("title", ErrorMessageConstant.TITLE_SIZE_ERROR, errorMap);

		}

		if (Utility.isStringEmpty(dto.getLanguage())) {
			errorMap = Utility.ErrorMap("language", ErrorMessageConstant.LANGUAGE_ERROR, errorMap);
		}
		
		

		if (Utility.isStringEmpty(dto.getShortDescription())) {
			errorMap = Utility.ErrorMap("shortDescription", ErrorMessageConstant.SHORT_DESCRIPTION_ERROR, errorMap);

		}
		if (!Utility.isStringEmpty(dto.getShortDescription()) && !validateSequence(dto.getShortDescription(), 10, 80)) {
			errorMap = Utility.ErrorMap("shortDescription", ErrorMessageConstant.SHORT_DESCRIPTION_SIZE_ERROR,
					errorMap);

		}
		
		if (Utility.isStringEmpty(dto.getFullDescription())) {
			errorMap = Utility.ErrorMap("fullDescription", ErrorMessageConstant.FULL_DESCRIPTION_ERROR, errorMap);

		}

		if (!Utility.isStringEmpty(dto.getFullDescription())
				&& !validateSequence(dto.getFullDescription(), 200, 4000)) {
			errorMap = Utility.ErrorMap("fullDescription", ErrorMessageConstant.FULL_DESCRIPTION_SIZE_ERROR, errorMap);

		}
		if (Utility.isStringEmpty(appIconUrl)) {
			errorMap = Utility.ErrorMap("appIconUrl", ErrorMessageConstant.APP_ICON_ERROR, errorMap);

		}
		if (bannerCount < 1) {
			errorMap = Utility.ErrorMap("bannerImage", ErrorMessageConstant.APP_BANNER_ERROR, errorMap);

		}

		if (imageCount < 3) {
			errorMap = Utility.ErrorMap("appImage", ErrorMessageConstant.APP_IMAGES_ERROR, errorMap);

		}
		
		if (Utility.isStringEmpty(dto.getWebsite())) {
			errorMap = Utility.ErrorMap("website", ErrorMessageConstant.WEBSITE_ERROR, errorMap);

		}
		if (!Utility.isStringEmpty(dto.getWebsite()) && !Utility.isValidURL(dto.getWebsite())) {
			errorMap = Utility.ErrorMap("website", ErrorMessageConstant.WEBSITE_URL_VALIDATION_ERROR, errorMap);

		}
		if (Utility.isStringEmpty(dto.getEmail())) {
			errorMap = Utility.ErrorMap("email", ErrorMessageConstant.INVALID_EMAIL, errorMap);

		}
		if (!Utility.isStringEmpty(dto.getEmail()) && !Utility.isValidEmailID(dto.getEmail())) {
			errorMap = Utility.ErrorMap("email", ErrorMessageConstant.INVALID_EMAIL, errorMap);

		}
		if (Utility.isStringEmpty(dto.getPrivacyPolicyUrl())) {
			errorMap = Utility.ErrorMap("privacyPolicyUrl", ErrorMessageConstant.PRIVACY_POLICY_URL_ERROR, errorMap);

		}
		if (!Utility.isStringEmpty(dto.getPrivacyPolicyUrl()) && !Utility.isValidURL(dto.getPrivacyPolicyUrl())) {
			errorMap = Utility.ErrorMap("privacyPolicyUrl", ErrorMessageConstant.PRIVACY_POLICY_URL_VALIDATION_ERROR,
					errorMap);

		}
		
		if (Utility.isStringEmpty(apkUrl)) {
			errorMap = Utility.ErrorMap("apkUrl", ErrorMessageConstant.APK_URL_ERROR, errorMap);

		}

		return errorMap;
	}

	// public Map<String, String> fullShortDescriptionError() {
	// Map<String, String> errorMap = null;
	// errorMap = Utility.ErrorMap("fullDescription",
	// ErrorMessageConstant.FULL_DESCRIPTION_ERROR, errorMap);
	// errorMap = Utility.ErrorMap("shortDescription",
	// ErrorMessageConstant.SHORT_DESCRIPTION_ERROR, errorMap);
	// return errorMap;
	// }

	public AppInfoDto populateAppInfoDtoToCompare(AppDetail appDetailDto, AppApplication appDto) {
		AppInfoDto appInfoDto = new AppInfoDto();
		appInfoDto = populateAppInfoDto(appDetailDto, appDto, appInfoDto);
		appInfoDto.setApp_price(appDto.getAppPrice());
		appInfoDto.setAppTypeId(appDto.getApplicationType().getAppTypeId());
		appInfoDto.setAppCategoryId(appDto.getAppCategory().getAppCategoryId());
		appInfoDto.setWhatsNew(appDetailDto.getWhatsNew());
		appInfoDto.setSubscription(appDto.getSubscription());
		// appInfoDto.setAppType(appDto.getApplicationType());
		// appInfoDto.setAppCategory(appDto.getAppCategory());
		appInfoDto.setStatus(appDto.getStatus());
		appInfoDto.setVersionName(appDto.getLatestVersionName());
		appInfoDto.setVersionCode(appDto.getLatestVersionNo());
		appInfoDto.setApplicationId(appDto.getAppApplicationId());
		appInfoDto.setWebsite(appDto.getWebsite());
		appInfoDto.setCompany(appDto.getCompany());
		appInfoDto.setUpdateType(appDto.getUpdateType());
		return appInfoDto;

	}

	public AppHistory populateListOfChanges(AppInfoDto oldAppInfo, AppInfoDto newAppInfo) {
		StringBuffer feilds = new StringBuffer();
		AppHistory appHistory = new AppHistory();
		AppInfoDto appInfo = new AppInfoDto();
		try {

			BeanMap map = new BeanMap(oldAppInfo);

			PropertyUtilsBean propUtils = new PropertyUtilsBean();

			for (Object propNameObject : map.keySet()) {
				String propertyName = (String) propNameObject;
				Object property1 = propUtils.getProperty(oldAppInfo, propertyName);
				Object property2 = propUtils.getProperty(newAppInfo, propertyName);

				if (null == property1 || null == property2) {
					if (property1 != property2) {

						feilds.append(propertyName).append(",");
						propUtils.setProperty(appInfo, propertyName, property2);
					}
				} else if (!property1.equals(property2)) {

					feilds.append(propertyName).append(",");
					propUtils.setProperty(appInfo, propertyName, property2);

				}
			}
			if (feilds.length() > 0) {
				feilds.setLength(feilds.length() - 1);
			}
			appInfo.setVersionCode(newAppInfo.getVersionCode());
			appInfo.setVersionName(newAppInfo.getVersionName());
			appHistory = appInfo.populateAppHistory(appInfo);
			appHistory.setUpdatedFeilds(feilds.toString());
		} catch (Exception e) {
			e.printStackTrace();

		}
		return appHistory;
	}

	public String getUpdateType() {
		return updateType;
	}

	public void setUpdateType(String updateType) {
		this.updateType = updateType;
	}

	public AppHistory populateAppHistory(AppInfoDto appInfoDto) {
		AppHistory appHistory = new AppHistory();
		try {

			appHistory.setCompany(appInfoDto.getCompany());
			appHistory.setEmail(appInfoDto.getEmail());
			appHistory.setFullDescription(appInfoDto.getFullDescription());
			appHistory.setLanguage(appInfoDto.getLanguage());
			appHistory.setPrivacyPolicyUrl(appInfoDto.getPrivacyPolicyUrl());
			appHistory.setShortDescription(appInfoDto.getShortDescription());
			appHistory.setStatus(appInfoDto.getStatus());
			appHistory.setTitle(appInfoDto.getTitle());
			appHistory.setVersionName(appInfoDto.getVersionName());
			appHistory.setVersionNo(appInfoDto.getVersionCode());
			appHistory.setWebsite(appInfoDto.getWebsite());
			appHistory.setWhatsNew(appInfoDto.getWhatsNew());
			appHistory.setUpdateType(appInfoDto.getUpdateType());

		} catch (Exception e) {
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);
		}
		return appHistory;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
