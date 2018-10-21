package org.bitvault.appstore.cloud.user.common.controller;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;
import org.bitvault.appstore.cloud.config.PropertiesConfig;
import org.bitvault.appstore.cloud.constant.APIConstants;
import org.bitvault.appstore.cloud.constant.Constants;
import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.mail.MailService;
import org.bitvault.appstore.cloud.model.AdminUser;
import org.bitvault.appstore.cloud.model.DevUser;
import org.bitvault.appstore.cloud.model.ForgetPassword;
import org.bitvault.appstore.cloud.user.admin.service.AdminUserService;
import org.bitvault.appstore.cloud.user.common.GeneralResponseModel;
import org.bitvault.appstore.cloud.user.common.service.ForgetPasswordService;
import org.bitvault.appstore.cloud.user.dev.service.DevUserService;
import org.bitvault.appstore.cloud.validator.PasswordValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = APIConstants.AUTH_BASE)
public class ForgetPasswordController {
	@Autowired
	private ForgetPasswordService forgetPasswordService;
	
	@Autowired
	private PropertiesConfig propertiesConfig;
	
	@Autowired
	private AdminUserService adminUserService;
	
	@Autowired
	private DevUserService devUserService;
	
	@Autowired
	private MailService mailService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	private final String userCheck = "0pAjlb";
	
	@PostMapping(value = APIConstants.FORGET_PASSWORD)
	public ResponseEntity<?> getResetPasswordLink(@RequestBody Map<String, String> inputMap){
		try {
			String forgetLink = propertiesConfig.getHost();
			String emailId = inputMap.get("emailId").trim();
			String userType = inputMap.get("type").trim();
			String temp = userCheck;
			inputMap.clear();
			if(emailId == null || emailId.isEmpty() || userType == null){				
				inputMap.put(Constants.MESSAGE, ErrorMessageConstant.REQUIRED_FIELD_MISSING);
				return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED, inputMap));
			}
			AdminUser adminUser = null;
			DevUser devUser = null;
			if(userType.equalsIgnoreCase(Constants.ADMIN)){
				adminUser = adminUserService.findByEmail(emailId);
				if(adminUser == null){
					inputMap.put(Constants.MESSAGE, ErrorMessageConstant.USER_NOT_FOUND);
					return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED, inputMap));
				}
				forgetLink = forgetLink + "#/admin/resetPassword/";
			}
			else{
				devUser = devUserService.findByEmailId(emailId);
				temp = "";
				if(devUser == null){
					inputMap.put(Constants.MESSAGE, ErrorMessageConstant.USER_NOT_FOUND);
					return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED, inputMap));
				}
				else if(!devUser.getStatus().equals(Constants.ACTIVE)){
					inputMap.put(Constants.MESSAGE, ErrorMessageConstant.ACCOUNT_EXPIRED_REJECTED);
					return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED, inputMap));
				}
				forgetLink = forgetLink + "#/resetPassword/";
			}
			ForgetPassword resetPassword = forgetPasswordService.findByEmail(emailId);
			if(resetPassword == null){
				resetPassword = new ForgetPassword();
			}			
			String randomLink = temp + RandomStringUtils.randomAlphanumeric(25);
			resetPassword.setHashcode(randomLink);
			resetPassword.setForgotLink(forgetLink + randomLink);
			resetPassword.setUserId(emailId);
			forgetPasswordService.save(resetPassword);
			mailService.sendMail(emailId, "Reset Password", Constants.RESET_PASSWORD_EMAIL_1 + resetPassword.getForgotLink() + Constants.RESET_PASSWORD_EMAIL_2 + Constants.MAIL_SIGN);
			inputMap.put(Constants.MESSAGE, Constants.RESET_EMAIL_SENT);
			return ResponseEntity.ok(GeneralResponseModel.of(Constants.SUCCESS, inputMap));
			
		} catch (Exception e) {
			e.printStackTrace();
			inputMap.put(Constants.MESSAGE, e.getMessage());
			return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED, inputMap));
		}
	}
	
	@PutMapping(value = APIConstants.RESET_PASSWORD)
	public ResponseEntity<?> resetPassword(@PathVariable String id,@RequestBody Map<String, String> inputMap){
		try {
			String newPassword = inputMap.get("newPassword");
			String confirmPassword = inputMap.get("confirmPassword");
			inputMap.clear();
			ForgetPassword forgetPassword = forgetPasswordService.findByUniqueId(id);
			if(forgetPassword == null){
				inputMap.put(Constants.MESSAGE, ErrorMessageConstant.USE_LATEST_LINK);
				return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED, inputMap));
			}
			else{
				long linkCreationTime = forgetPassword.getUpdatedAt().getTime();
				long currentTime = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()).getTime();
				if(currentTime - linkCreationTime > 1800000){
					inputMap.put(Constants.MESSAGE, ErrorMessageConstant.LINK_EXPIRED);
					return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED, inputMap));
				}
			}
			
			if(newPassword == null || newPassword.isEmpty() || confirmPassword == null || confirmPassword.isEmpty()){				
				inputMap.put(Constants.MESSAGE, ErrorMessageConstant.REQUIRED_FIELD_PASSWORD);
				return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED, inputMap));
			}
			if(!PasswordValidator.compareNewAndConfirmPassword(newPassword, confirmPassword)){
				inputMap.put(Constants.MESSAGE, ErrorMessageConstant.PASSWORD_MISMATCH);
				return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED, inputMap));
			}
			AdminUser adminUser = null;
			DevUser devUser = null;
			if(id.contains(userCheck)){
				adminUser = adminUserService.findByEmail(forgetPassword.getUserId());
				if(adminUser == null){
					inputMap.put(Constants.MESSAGE, ErrorMessageConstant.USER_NOT_FOUND);
					return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED, inputMap));
				}
				adminUser.setPassword(passwordEncoder.encode(newPassword));
				adminUserService.save(adminUser);
			}
			else{
				devUser = devUserService.findByEmailId(forgetPassword.getUserId());
				if(devUser == null){
					inputMap.put(Constants.MESSAGE, ErrorMessageConstant.USER_NOT_FOUND);
					return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED, inputMap));
				}
				devUser.setPassword(passwordEncoder.encode(newPassword));
				devUserService.saveUser(devUser);
			}
			forgetPasswordService.deletePasswordLink(forgetPassword);
			inputMap.put(Constants.MESSAGE, Constants.SUCCESS_UPDATED);
			return ResponseEntity.ok(GeneralResponseModel.of(Constants.SUCCESS, inputMap));
		} catch (Exception e) {
			e.printStackTrace();
			inputMap.put(Constants.MESSAGE, e.getMessage());
			return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED, inputMap));
		}
	}
}
