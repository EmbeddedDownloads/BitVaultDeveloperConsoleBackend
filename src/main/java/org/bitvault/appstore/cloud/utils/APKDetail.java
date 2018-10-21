package org.bitvault.appstore.cloud.utils;

import java.util.ArrayList;
import java.util.List;

import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.dto.AppApplication;
import org.bitvault.appstore.cloud.exception.BitVaultException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class APKDetail {
	private String mode;
	private String versionNo;
	private String versionName;
	private String certBase64Md5;
	private String certMd5;
	private String apkUrl;
	private List<String> permissionList;
	private String organization;
	private String status ;

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getCertBase64Md5() {
		return certBase64Md5;
	}

	public void setCertBase64Md5(String certBase64Md5) {
		this.certBase64Md5 = certBase64Md5;
	}

	public String getCertMd5() {
		return certMd5;
	}

	public void setCertMd5(String certMd5) {
		this.certMd5 = certMd5;
	}

	public String getSignAlgorithm() {
		return signAlgorithm;
	}

	public void setSignAlgorithm(String signAlgorithm) {
		this.signAlgorithm = signAlgorithm;
	}

	public List<String> getPermissionList() {
		return permissionList;
	}

	public void setPermissionList(List<String> permissionList) {
		this.permissionList = permissionList;
	}

	private String signAlgorithm;
	private String packageName;
	private String appName;

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getVersionNo() {
		return versionNo;
	}

	public void setVersionNo(String versionNo) {
		this.versionNo = versionNo;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}
	

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public static AppApplication populateAppApplicationByAPKDetail(APKDetail apkDetail, AppApplication app) {
		StringBuilder sb = new StringBuilder();
		try {

			app.setApkUrl(apkDetail.getApkUrl());
			app.setAppName(apkDetail.getAppName());
			app.setPackageName(apkDetail.getPackageName());
			app.setLatestVersionName(apkDetail.getVersionName());
			app.setLatestVersionNo(Integer.parseInt(apkDetail.getVersionNo()));
			app.setCompany(apkDetail.getOrganization());
			List<String> permissionList = apkDetail.getPermissionList();
			int permissionListSize = permissionList.size();
			for (int i = 0; i < permissionList.size(); i++) {
				if (i == (permissionListSize - 1))
					sb.append(permissionList.get(i));
				else {
					sb.append(permissionList.get(i)).append(",");

				}
			}

			app.setAppPermission(sb.toString());
			
		
		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);
		}

		return app;
	}

	public String getApkUrl() {
		return apkUrl;
	}

	public void setApkUrl(String apkUrl) {
		this.apkUrl = apkUrl;
	}
}