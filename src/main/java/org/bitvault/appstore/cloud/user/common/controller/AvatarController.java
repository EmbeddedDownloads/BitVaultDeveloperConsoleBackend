package org.bitvault.appstore.cloud.user.common.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.bitvault.appstore.cloud.aws.service.AwsS3Service;
import org.bitvault.appstore.cloud.constant.APIConstants;
import org.bitvault.appstore.cloud.constant.Constants;
import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.bitvault.appstore.cloud.model.AdminUser;
import org.bitvault.appstore.cloud.model.DevUser;
import org.bitvault.appstore.cloud.model.UserActivityType;
import org.bitvault.appstore.cloud.user.admin.service.AdminUserService;
import org.bitvault.appstore.cloud.user.common.GeneralResponseModel;
import org.bitvault.appstore.cloud.user.common.service.UploadFileService;
import org.bitvault.appstore.cloud.user.dev.service.DevUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(APIConstants.AVATAR_BASE)
public class AvatarController {

	@Autowired
	private UploadFileService uploadFileService;

	@Autowired
	private DevUserService devUserService;

	@Autowired
	private AdminUserService adminUserService;

	@Autowired
	AwsS3Service service;

	@Value("${aws.base.bucket}")
	private String bucket;

	private Map<String, String> resultMap = null;

	@PostMapping(value = APIConstants.UPLOAD_AVATAR)
	public ResponseEntity<?> uploadAvatar(@RequestParam("file") MultipartFile file, HttpServletRequest request,
			@RequestParam String type) {
		resultMap = new HashMap<>();
		String userName = "";
		try {
			String userId = (String) request.getAttribute(Constants.USERID);
			if (userId == null) {
				resultMap.put(Constants.MESSAGE, ErrorMessageConstant.USER_NOT_FOUND);
				return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED, resultMap));
			}
			if (!file.getContentType().equals("image/jpeg") && !file.getContentType().equals("image/png")) {
				resultMap.put(Constants.MESSAGE, ErrorMessageConstant.INVALID_FILE_TYPE);
				return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED, resultMap));
			} else if ((file.getSize() >= 51200 && file.getSize() <= 1048576)) {
				DevUser user = null;
				AdminUser adminUser = null;
				String avatarURL = null;
				if (!type.equalsIgnoreCase(Constants.ADMIN)) {
					user = devUserService.findByUserId(userId);
					if (user == null) {
						resultMap.put(Constants.MESSAGE, ErrorMessageConstant.USER_NOT_FOUND);
						return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED, resultMap));
					}
					avatarURL = service.uploadFile(file, userId, user.getAvatarURL());
					user.setAvatarURL(avatarURL);
					devUserService.saveUser(user);

					userName = user.getUsername();

				} else {
					adminUser = adminUserService.getByUserId(userId);
					avatarURL = service.uploadFile(file, userId, adminUser.getAvatarURL());
					adminUser.setAvatarURL(avatarURL);
					adminUserService.save(adminUser);

					userName = adminUser.getFirstName() + " " + adminUser.getLastName();
				}

				trackAvatarForUserAuditing(userId, userName, avatarURL);

				resultMap.put("avatarURL", avatarURL);
				return ResponseEntity.ok(GeneralResponseModel.of(Constants.SUCCESS, resultMap));
			} else {
				resultMap.put(Constants.MESSAGE, ErrorMessageConstant.MAX_SIZE_REACHED);
				return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED, resultMap));
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put(Constants.MESSAGE, ErrorMessageConstant.UNABLE_TO_UPLOAD);
			return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED, resultMap));
		}
	}

	@GetMapping(value = APIConstants.AVATAR_IMAGE)
	public ResponseEntity<?> getAvatar(@PathVariable("filename") String filename, HttpServletRequest request) {
		// String email = (String) request.getAttribute("email");
		// if(email == null){
		// return ResponseEntity
		// .ok(GeneralResponseModel.of(Constants.FAILED,
		// ErrorMessageConstant.USER_NOT_FOUND));
		// }
		// DevUser user = devUserService.findByEmailId(email);
		if (filename != null) {
			Resource avatarFile = uploadFileService.getFile(filename);
			// avatarFile.get
			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
					.body(avatarFile);
		} else {
			return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED, "No Avatar avilable"));
		}
	}

	@DeleteMapping(value = APIConstants.AVATAR_DELETE)
	public ResponseEntity<?> deleteAvatar(@RequestBody Map<String, String> typeMap, HttpServletRequest request) {
		DevUser devUser = null;
		AdminUser adminUser = null;
		resultMap = new HashMap<>();
		String userId = (String) request.getAttribute(Constants.USERID);
		if (userId == null) {
			resultMap.put(Constants.MESSAGE, ErrorMessageConstant.UNABLE_TO_DELETE);
			return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED, resultMap));
		}
		String type = typeMap.get("type");
		String avatarURL = null, userName = "";
		if (!type.equalsIgnoreCase(Constants.ADMIN)) {
			devUser = devUserService.findByUserId(userId);
			if (devUser == null) {
				resultMap.put(Constants.MESSAGE, ErrorMessageConstant.USER_NOT_FOUND);
				return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED, resultMap));
			}
			avatarURL = devUser.getAvatarURL();
			if (avatarURL == null || avatarURL.isEmpty()) {
				resultMap.put(Constants.MESSAGE, ErrorMessageConstant.NO_AVATAR_FOUND);
				return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED, resultMap));
			}
			service.deleteImage(avatarURL.substring(avatarURL.indexOf(userId)));
			devUser.setAvatarURL("");
			devUserService.saveUser(devUser);

			userName = devUser.getUsername();

		} else {
			adminUser = adminUserService.getByUserId(userId);
			avatarURL = adminUser.getAvatarURL();
			if (avatarURL == null || avatarURL.isEmpty()) {
				resultMap.put(Constants.MESSAGE, ErrorMessageConstant.NO_AVATAR_FOUND);
				return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED, resultMap));
			}
			service.deleteImage(avatarURL.substring(avatarURL.indexOf(userId)));
			adminUser.setAvatarURL("");
			adminUserService.save(adminUser);

			userName = adminUser.getFirstName() + " " + adminUser.getLastName();
		}

		trackAvatarForUserAuditing(userId, userName, "");

		resultMap.put(Constants.MESSAGE, Constants.SUCCESS_DELETED);
		return ResponseEntity.ok(GeneralResponseModel.of(Constants.SUCCESS, resultMap));
	}

	/**
	 * Method used to track user auditing
	 * @param userId
	 * @param userName
	 * @param avatarURL
	 */
	public void trackAvatarForUserAuditing(String userId, String userName, String avatarURL) {

		try {
			UserActivityType userActivityType = new UserActivityType();
			userActivityType.setActivityType(Constants.EDIT_PROFILE);

			Map<String, String> dataMap = new HashMap<>();
			dataMap.put(Constants.USERID, userId);
			dataMap.put(Constants.USER_NAME, userName);
			dataMap.put(Constants.AVATAR_URL, avatarURL);

			if (userName != null && userId != null && avatarURL != null) {
				devUserService.saveUserActivity(userActivityType, dataMap);
			}
		} catch (Exception e) {
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);
		}
	}

}
