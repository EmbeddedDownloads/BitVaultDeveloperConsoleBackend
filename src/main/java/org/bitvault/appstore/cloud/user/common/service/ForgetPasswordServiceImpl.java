package org.bitvault.appstore.cloud.user.common.service;

import org.bitvault.appstore.cloud.model.ForgetPassword;
import org.bitvault.appstore.cloud.user.common.dao.ForgetPasswordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ForgetPasswordServiceImpl implements ForgetPasswordService{

	@Autowired
	ForgetPasswordRepository  forgetPasswordRepository;
	
	@Override
	public void save(ForgetPassword password) {
		forgetPasswordRepository.saveAndFlush(password);
	}

	@Override
	public ForgetPassword findByEmail(String emailId) {
		return forgetPasswordRepository.findByUserId(emailId);
	}

	@Override
	public ForgetPassword findByUniqueId(String id) {
		return forgetPasswordRepository.findByHashcode(id);
	}

	@Override
	public void deletePasswordLink(ForgetPassword password) {
		forgetPasswordRepository.delete(password);
	}

}
