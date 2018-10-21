package org.bitvault.appstore.cloud.user.admin.controller;

import java.util.List;

import org.bitvault.appstore.cloud.aws.service.AwsS3Service;
import org.bitvault.appstore.cloud.constant.APIConstants;
import org.bitvault.appstore.cloud.constant.Constants;
import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.dto.AppApplication;
import org.bitvault.appstore.cloud.dto.AppImage;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.bitvault.appstore.cloud.exception.BitVaultResponse;
import org.bitvault.appstore.cloud.model.DevUser;
import org.bitvault.appstore.cloud.user.admin.service.AdminUserService;
import org.bitvault.appstore.cloud.user.admin.service.DeleteService;
import org.bitvault.appstore.cloud.user.common.GeneralResponseModel;
import org.bitvault.appstore.cloud.user.dev.service.DevUserService;
import org.bitvault.appstore.cloud.utils.Utility;
import org.bitvault.appstore.commons.application.service.AppApplicationService;
import org.bitvault.appstore.commons.application.service.AppImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(APIConstants.MOBILE_API_BASE)
public class DeleteController {
	@Autowired
	private AppApplicationService appApplicationService;
	@Autowired
	AdminUserService adminService;
	@Autowired
	DevUserService devUserService;
	@Autowired
	DeleteService deleteService;
	@Autowired
	ElasticsearchTemplate elasticSearchTemplate;
	@Autowired
	AwsS3Service awsService;
	@Autowired
	AppImageService appImageService;

	@DeleteMapping(value = "/delete" + "/{applicationId}")
	public ResponseEntity<GeneralResponseModel> deleteApplication(@PathVariable Integer applicationId) {
		GeneralResponseModel customResponse = null;

		try {
			AppApplication app = appApplicationService.findApplicationByAppId(applicationId);
			List<AppImage> appImages = appImageService.findAppImagesByApplicatinId(applicationId);
			if (app != null) {
				if (appImages != null) {
					for (AppImage appImage : appImages) {
						String appImageUrl = appImage.getAppImageUrl();
						String path[] = appImageUrl.split(app.getUserId());
						String realPath = app.getUserId() + path[1];
						awsService.deleteImage(realPath);
					}
				}
				String apkUrl = app.getApkUrl();
				String pathapk[] = apkUrl.split(app.getUserId());
				String realPathapk = app.getUserId() + pathapk[1];
				if (!Utility.isStringEmpty(realPathapk)) {
					awsService.deleteImage(realPathapk);
				}
				String appIconUrl = app.getAppIconUrl();
				String path[] = appIconUrl.split(app.getUserId());
				String realPath = app.getUserId() + path[1];

				if (!Utility.isStringEmpty(realPath)) {
					awsService.deleteImage(realPath);
				}
				deleteService.deleteApplicationData(applicationId);
				customResponse = GeneralResponseModel.of(Constants.SUCCESS, new BitVaultResponse(Constants.DELETE));
			} else {
				customResponse = GeneralResponseModel.of(Constants.FAILED,
						new BitVaultResponse(ErrorMessageConstant.RESULT_NOT_FOUND));
			}
		} catch (BitVaultException e) {
			e.printStackTrace();
			customResponse = GeneralResponseModel.of(Constants.FAILED, new BitVaultResponse(e));
		}

		return new ResponseEntity<GeneralResponseModel>(customResponse, HttpStatus.OK);
	}

	@DeleteMapping(value = "/deleteUser" + "/{userId}")
	public ResponseEntity<GeneralResponseModel> deleteUsers(@PathVariable String userId) {
		GeneralResponseModel customResponse = null;
		try {
			DevUser devUser = devUserService.findByUserId(userId);
			if (devUser != null) {
				if (userId.equals(devUser.getParentId())) {

					deleteService.deleteUsersByUserId(userId, devUser.getRole().getRoleName());

				} else {
					deleteService.deleteSubDeveloper(userId);
				}
				
				deleteService.deleteDocument(userId);
				
			} else {
				customResponse = GeneralResponseModel.of(Constants.FAILED,
						new BitVaultResponse(ErrorMessageConstant.RESULT_NOT_FOUND));
			}

		} catch (BitVaultException e) {
			e.printStackTrace();
			customResponse = GeneralResponseModel.of(Constants.FAILED, new BitVaultResponse(e));
		}
		return new ResponseEntity<GeneralResponseModel>(customResponse, HttpStatus.OK);
	}

	@DeleteMapping(value = "/deleteElasticUser" + "/{userId}")
	public ResponseEntity<GeneralResponseModel> deleteElasticUsers(@PathVariable String userId) {
		GeneralResponseModel customResponse = null;
		try {
			deleteService.deleteDocument(userId);
		}

		catch (BitVaultException e) {
			e.printStackTrace();
			customResponse = GeneralResponseModel.of(Constants.FAILED, new BitVaultResponse(e));
		}
		return new ResponseEntity<GeneralResponseModel>(customResponse, HttpStatus.OK);
	}

	@DeleteMapping(value = "/deleteElasticApp" + "/{applicationId}")
	public ResponseEntity<GeneralResponseModel> deleteElasticApp(@PathVariable Integer applicationId) {
		GeneralResponseModel customResponse = null;
		try {
			deleteService.deleteDocumentApplication(applicationId.toString());
		}

		catch (BitVaultException e) {
			e.printStackTrace();
			customResponse = GeneralResponseModel.of(Constants.FAILED, new BitVaultResponse(e));
		}
		return new ResponseEntity<GeneralResponseModel>(customResponse, HttpStatus.OK);
	}

	@DeleteMapping(value = "/deleteElasticAppApp" + "/{appApplicationId}")
	public ResponseEntity<GeneralResponseModel> deleteElasticAppApp(@PathVariable Integer appApplicationId) {
		GeneralResponseModel customResponse = null;
		try {
			deleteService.deleteDocumentAppApplication(appApplicationId.toString());
		}

		catch (BitVaultException e) {
			e.printStackTrace();
			customResponse = GeneralResponseModel.of(Constants.FAILED, new BitVaultResponse(e));
		}
		return new ResponseEntity<GeneralResponseModel>(customResponse, HttpStatus.OK);
	}
}
