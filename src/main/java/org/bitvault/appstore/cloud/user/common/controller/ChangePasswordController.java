package org.bitvault.appstore.cloud.user.common.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.bitvault.appstore.cloud.constant.APIConstants;
import org.bitvault.appstore.cloud.constant.Constants;
import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.model.AdminUser;
import org.bitvault.appstore.cloud.model.DevUser;
import org.bitvault.appstore.cloud.user.admin.service.AdminUserService;
import org.bitvault.appstore.cloud.user.common.GeneralResponseModel;
import org.bitvault.appstore.cloud.user.dev.service.DevUserService;
import org.bitvault.appstore.cloud.validator.PasswordValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(APIConstants.COMMON_API_BASE)
public class ChangePasswordController {

	private static final Logger logger = LoggerFactory.getLogger(ChangePasswordController.class);

	@Autowired
	private DevUserService devUserService;

	@Autowired
	private AdminUserService adminUserService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	private Map<String, Object> result = null;

	private String oldPass = "oldPassword";
	private String newPass = "newPassword";
	private String confirmPass = "confirmPassword";

	@RequestMapping(value = APIConstants.UPDATE_PASSWORD, method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updatePassword(HttpServletRequest request, @RequestBody Map<String, String> passwordMap) {
		result = new HashMap<>();
		try {
			String oldPassword = passwordMap.get(oldPass);
			String newPassword = passwordMap.get(newPass);
			String confirmPassword = passwordMap.get(confirmPass);
			String type = passwordMap.get("type").toUpperCase();
			String userId = (String) request.getAttribute(Constants.USERID);
			if (userId == null) {
				return ResponseEntity
						.ok(GeneralResponseModel.of(Constants.FAILED, ErrorMessageConstant.USER_NOT_FOUND));
			}
			if (newPassword == null || newPassword.trim().isEmpty()) {
				result.put(Constants.MESSAGE, ErrorMessageConstant.PASSWORD_CANT_EMPTY);
				return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED, result));
			}
			DevUser devUser = null;
			AdminUser adminUser = null;
			String currentUserPassword = null;
			if (!type.equalsIgnoreCase(Constants.ADMIN)) {
				devUser = devUserService.findByUserId(userId);
				if (devUser == null) {
					return ResponseEntity
							.ok(GeneralResponseModel.of(Constants.FAILED, ErrorMessageConstant.USER_NOT_FOUND));
				}
				currentUserPassword = devUser.getPassword();
				if (checkPassword(oldPassword, newPassword, confirmPassword, currentUserPassword)
						.containsKey(Constants.MESSAGE)) {
					return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED, result));
				}
				devUser.setPassword(passwordEncoder.encode(newPassword));
				devUser.setLastPasswordResetDate(new Date(System.currentTimeMillis()));
				devUserService.changePassword(devUser);
			} else {
				adminUser = adminUserService.getByUserId(userId);
				currentUserPassword = adminUser.getPassword();
				if (checkPassword(oldPassword, newPassword, confirmPassword, currentUserPassword)
						.containsKey(Constants.MESSAGE)) {
					return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED, result));
				}
				adminUser.setPassword(passwordEncoder.encode(newPassword));
				adminUser.setLastPasswordResetDate(new Date(System.currentTimeMillis()));
				adminUserService.changePassword(adminUser);
			}
			result.put(Constants.MESSAGE, Constants.SUCCESS_UPDATED);
			return ResponseEntity.ok(GeneralResponseModel.of(Constants.SUCCESS, result));
		} catch (Exception e) {
			logger.error(e.getMessage());
			result.put(Constants.MESSAGE, e.getMessage());
			return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED, result));
		}
	}

	private Map<String, Object> checkPassword(String oldPassword, String newPassword, String confirmPassword,
			String currentUserPassword) {
		if (!passwordEncoder.matches(oldPassword, currentUserPassword)) {
			result.put(Constants.MESSAGE, ErrorMessageConstant.OLD_PASSWORD_MISMATCH);
		} else if (!PasswordValidator.isValidPassword(newPassword)) {
			result.put(Constants.MESSAGE, ErrorMessageConstant.PASSWORD_SYNTAX_ERROR);
		} else if (oldPassword.equals(newPassword)) {
			result.put(Constants.MESSAGE, ErrorMessageConstant.OLD_NEW_PASSWORD_ARE_SAME);
		} else if (!PasswordValidator.compareNewAndConfirmPassword(newPassword, confirmPassword)) {
			result.put(Constants.MESSAGE, ErrorMessageConstant.PASSWORD_MISMATCH);
		}
		return result;
	}
}
