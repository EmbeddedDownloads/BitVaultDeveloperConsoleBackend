package org.bitvault.appstore.cloud.validator;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bitvault.appstore.cloud.constant.Constants;
import org.bitvault.appstore.cloud.constant.DbConstant;
import org.bitvault.appstore.cloud.constant.ErrorCode;
import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.dto.AppApplication;
import org.bitvault.appstore.cloud.dto.AppCategory;
import org.bitvault.appstore.cloud.dto.Application;
import org.bitvault.appstore.cloud.dto.ApplicationTypeDto;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.bitvault.appstore.cloud.utils.APKDetail;
import org.bitvault.appstore.cloud.utils.Utility;
import org.bitvault.appstore.commons.application.service.AppApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class ApkValidator {
	@Autowired
	AppApplicationService appService;

	public final static String packageRegex = "^[a-z][a-z0-9_]+(\\.[a-z][a-z0-9_]+)+$";
	public final static String versionNameRegex = "^[0-9_]+(\\.[0-9_]+)+$";
	public final static String versionCodeRegex = "^[0-9]+$";

	public final static String PACKAGE_NAME = "packageName";
	public final static String APP_NAME = "appName";
	public final static String VERSION_NAME = "versionName";
	public final static String VERSION_CODE = "versionCode";
	public final static String SIGNED_STATUS = "signed_status";
	public final static String REALEASE_MODE = "release_mode";
	public final static String COMPANY = "company";

	public final static String SIGNED_KEY = "signed_key";
	// public final static String PACKAGE_COMPARE = "package_compare";
	public final static String VERSION_CODE_COMPARE = "version_code_compare";
	public final static String VERSION_NAME_COMPARE = "version_name_compare";
	public final static String COMPANY_EROOR = "company is missing in build";

	public final static String PACKAGE_NAME_ERROR = "invalid package name";
	public final static String APP_NAME_ERROR = "invalid app name";
	public final static String VERSION_NAME_ERROR = "invalid version name";
	public final static String VERSION_CODE_ERROR = "invalid version code";
	public final static String SIGNED_STATUS_ERROR = "invalid version name";
	public final static String REALEASE_MODE_ERROR = "build must be signed";
	public final static String SIGNED_KEY_ERROR = "invalid signed key";
	// public final static String PACKAGE_COMPARE_ERROR = "invalid package
	// name";
	public final static String VERSION_CODE_COMPARE_ERROR = "invalid version code, last version was ";
	public final static String VERSION_NAME_COMPARE_ERROR = "invalid version name, last version was ";

	public static Boolean validateReleaseMode(String mode) {
		if (mode.equals(Constants.DEBUG_MODE)) {
			return false;
		} else {
			return true;
		}
	}

	public static Boolean validateOrganization(String organization) {
		if (Utility.isStringEmpty(organization)) {
			return false;
		} else {
			return true;
		}
	}

	public static Map<String, Object> compareApks(APKDetail apkDetail, AppApplication app, Map<String, Object> map) {
		Map<String, Object> resultMap = map;

		try {

			if (!compareVersionNames(app.getLatestVersionName(), apkDetail.getVersionName())) {
				if (!(apkDetail.getStatus().equalsIgnoreCase(DbConstant.UPLOADED_FROM_PROD)
						&& (app.getStatus().equalsIgnoreCase(Constants.BETA_PUBLISHED)
								|| app.getStatus().equalsIgnoreCase(Constants.BETA_PUBLISHED)
								|| app.getStatus().equalsIgnoreCase(Constants.BETA_UNPUBLISHED)
								|| app.getStatus().equalsIgnoreCase(Constants.ALPHA_PUBLISHED)
								|| app.getStatus().equalsIgnoreCase(Constants.ALPHA_UNPUBLISHED)))) {
					resultMap.put(VERSION_NAME_COMPARE, VERSION_NAME_COMPARE_ERROR + app.getLatestVersionName());
				}
			}

			if (!compareVersionCode(app.getLatestVersionNo(), Integer.parseInt(apkDetail.getVersionNo()))) {
				if (!(apkDetail.getStatus().equalsIgnoreCase(DbConstant.UPLOADED_FROM_PROD)
						&& (app.getStatus().equalsIgnoreCase(Constants.BETA_PUBLISHED)
								|| app.getStatus().equalsIgnoreCase(Constants.BETA_PUBLISHED)
								|| app.getStatus().equalsIgnoreCase(Constants.BETA_UNPUBLISHED)
								|| app.getStatus().equalsIgnoreCase(Constants.ALPHA_PUBLISHED)
								|| app.getStatus().equalsIgnoreCase(Constants.ALPHA_UNPUBLISHED)))) {
					resultMap.put(VERSION_CODE_COMPARE, VERSION_CODE_COMPARE_ERROR + app.getLatestVersionNo());
				}
			}

		} catch (Exception e) {
			e.printStackTrace();

			throw new BitVaultException(ErrorMessageConstant.UNABLE_TO_VALIDATE, ErrorCode.FIELD_IS_EMPTY_CODE);
		}
		return resultMap;

	}

	public static Boolean compareVersionCode(Integer oldVersionCode, Integer newVersionCode) {

		if (oldVersionCode >= newVersionCode) {
			return false;
		}

		return true;

	}

	public static Boolean compareVersionNames(String oldVersionName, String newVersionName) {
		int versionCheck = 0;

		String[] oldNumbers = oldVersionName.split("\\.");
		String[] newNumbers = newVersionName.split("\\.");

		int minIndex = Math.min(oldNumbers.length, newNumbers.length);

		for (int i = 0; i < minIndex; i++) {
			int oldVersionPart = Integer.valueOf(oldNumbers[i]);
			int newVersionPart = Integer.valueOf(newNumbers[i]);

			if (oldVersionPart < newVersionPart) {
				versionCheck = 1;
				break;
			} else if (oldVersionPart > newVersionPart) {
				versionCheck = -1;
				break;
			}
		}

		if (versionCheck == 0 && oldNumbers.length != newNumbers.length) {
			versionCheck = (oldNumbers.length > newNumbers.length) ? -1 : 1;
		}

		if (versionCheck == -1) {
			return false;
		} else {
			return true;
		}

	}

	public static Boolean comparePackageName(String oldPackage, String newPackage) {
		if (oldPackage.equals(newPackage)) {
			return true;
		} else {
			return false;
		}
	}

	public static Map<String, Object> validateApk(APKDetail apkDetail) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// TODO: signed key validation is left
		try {

			if (!validatePackageName(apkDetail.getPackageName())) {
				resultMap.put(PACKAGE_NAME, PACKAGE_NAME_ERROR);
			}
			if (!validateAppName(apkDetail.getAppName())) {
				resultMap.put(APP_NAME, APP_NAME_ERROR);

			}
			if (!validateVersionName(apkDetail.getVersionName())) {
				resultMap.put(VERSION_NAME, VERSION_NAME_ERROR);

			}
			if (!validateVersionCode(apkDetail.getVersionNo())) {
				resultMap.put(VERSION_CODE, VERSION_CODE_ERROR);

			}

			if (!validateReleaseMode(apkDetail.getMode())) {
				resultMap.put(REALEASE_MODE, REALEASE_MODE_ERROR);

			}

			if (!validateOrganization(apkDetail.getOrganization())) {
				resultMap.put(COMPANY, COMPANY_EROOR);

			}
			// resultMap.put(PACKAGE_NAME_IS_VALID,
			// validatePackageName(apkDetail.getPackageName()));
			// resultMap.put(APP_NAME_IS_VALID,
			// validateAppName(apkDetail.getAppName()));
			//
			// resultMap.put(VERSION_NAME_IS_VALID,
			// validateVersionName(apkDetail.getVersionName()));
			//
			// resultMap.put(VERSION_CODE_IS_VALID,
			// validateVersionCode(apkDetail.getVersionNo()));
			// resultMap.put(SIGNE_STATUS_IS_VALID, apkDetail.getMode());
			// resultMap.put(REALEASE_MODE_IS_VALID,
			// validateReleaseMode(apkDetail.getMode()));
			// resultMap.put(SIGNED_KEY_IS_VALID, "");

		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.UNABLE_TO_VALIDATE);
		}

		return resultMap;
	}

	public static Boolean validatePackageName(String packageName) {
		// Pattern pattern = Pattern.compile(packageRegex);
		// Matcher matcher = pattern.matcher(packageName);
		//
		// return matcher.matches();

		return Utility.isValid(packageName, packageRegex);

	}

	private static Boolean validateVersionName(String versionName) {
		// Pattern pattern = Pattern.compile(versionNameRegex);
		// Matcher matcher = pattern.matcher(versionName);
		//
		// return matcher.matches();
		//
		return Utility.isValid(versionName, versionNameRegex);
	}

	private static boolean validateAppName(String appName) {
		if (null == appName || appName.trim().isEmpty()) {
			return false;
		} else {

			return true;
		}
	}

	private static Boolean validateVersionCode(String versionCode) {
		// Pattern pattern = Pattern.compile(versionCodeRegex);
		// Matcher matcher = pattern.matcher(versionCode);
		//
		// return matcher.matches();

		return Utility.isValid(versionCode, versionCodeRegex);

	}

	public static Map<String, String> validateApkUploadForm(String fileName, String title, AppCategory appCategory,
			ApplicationTypeDto appTypeDto, Map<String, String> errorMap) {
		boolean exeCheck = false;

		// Code commented by vaibhav for apk

		/*
		 * if (null != fileName && fileName.contains(".")) { String[] splitWithDot =
		 * fileName.split("\\.");
		 * 
		 * exeCheck = splitWithDot[splitWithDot.length - 1].equalsIgnoreCase("apk");
		 * 
		 * }
		 * 
		 * if (!exeCheck) { errorMap = Utility.ErrorMap("apkFile",
		 * ErrorMessageConstant.FILE_IS_INVALID, errorMap);
		 * 
		 * }
		 */

		// Code commented by vaibhav for dpk start

		if (null != fileName && fileName.contains(".")) {
			String[] splitWithDot = fileName.split("\\.");

			exeCheck = splitWithDot[splitWithDot.length - 1].equalsIgnoreCase("bvk");

		}

		if (!exeCheck) {
			errorMap = Utility.ErrorMap("bvkFile", ErrorMessageConstant.FILE_IS_INVALID, errorMap);

		}

		if (Utility.isStringEmpty(title)) {
			errorMap = Utility.ErrorMap("title", ErrorMessageConstant.FIELD_IS_EMPTY, errorMap);

		}
		if (null == appTypeDto) {
			errorMap = Utility.ErrorMap("appCategoryId", ErrorMessageConstant.INVALID_APP_TYPE, errorMap);

		}
		if (null == appCategory) {
			errorMap = Utility.ErrorMap("appTypeId", ErrorMessageConstant.INVALID_CATEGORYID, errorMap);

		}

		if (null != appCategory && !appCategory.getStatus().equalsIgnoreCase(Constants.ACTIVE)) {
			errorMap = Utility.ErrorMap("appTypeId", ErrorMessageConstant.INVALID_CATEGORYID, errorMap);

		}

		if (null != appTypeDto && null != appCategory) {
			if (appTypeDto.getAppTypeId().intValue() != appCategory.getAppTypeId().intValue()) {
				errorMap = Utility.ErrorMap("appTypeId", ErrorMessageConstant.INVALID_CATEGORYID, errorMap);
			}
		}

		return errorMap;
	}

}
