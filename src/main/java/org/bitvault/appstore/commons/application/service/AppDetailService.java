package org.bitvault.appstore.commons.application.service;

import java.util.List;
import java.util.Map;

import org.bitvault.appstore.cloud.dto.AppDetail;
import org.bitvault.appstore.cloud.exception.BitVaultException;

public interface AppDetailService {

	AppDetail findApplicationDetailByAppIdAndVersion(Integer applicationId, String appVersionName, String ImageStatus)
			throws BitVaultException;

	AppDetail saveAppDetail(org.bitvault.appstore.cloud.model.AppDetail appDetail) throws BitVaultException;

	AppDetail findAppdetailById(Integer id) throws BitVaultException;

	Integer updateAppDetailStatus(String status, Integer applicationId, String appVersionName);

	Map<String, Object> appDetailArtifact(Integer applicationId, List<String> status, int page, int size,
			String direction, String property);

}
