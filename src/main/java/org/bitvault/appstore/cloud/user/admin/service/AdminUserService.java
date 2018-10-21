package org.bitvault.appstore.cloud.user.admin.service;

import java.util.Map;

import org.bitvault.appstore.cloud.dto.AppApplication;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.bitvault.appstore.cloud.model.AdminUser;
import org.bitvault.appstore.cloud.model.Request;

public interface AdminUserService {
	AdminUser findByEmail(String email);

	AdminUser getByUserId(String userId);

	void updatePassword(AdminUser userModel);

	int changeAppStatus(AppApplication appDto, String status, String reason, String appRequestType, String adminId)
			throws BitVaultException;

	Request findRequestById(Integer requestId) throws BitVaultException;

	String findRequestTypeByReqId(Integer requestId) throws BitVaultException;

	void save(AdminUser adminUser);

	void changePassword(AdminUser adminUser);

	

	Map<String, Object> searchAppApplicationByAppName(String appName, int page, int size, String desc, String orderBy);

	Map<String, Object> searchUserByUserName(String username, int page, int size, String direction,String property);
	
	
}
