package org.bitvault.appstore.cloud.user.common.service;

import org.bitvault.appstore.cloud.model.ForgetPassword;

public interface ForgetPasswordService {
	void save(ForgetPassword password);
	ForgetPassword findByEmail(String emailId);
	ForgetPassword findByUniqueId(String id);
	void deletePasswordLink(ForgetPassword password);
}
