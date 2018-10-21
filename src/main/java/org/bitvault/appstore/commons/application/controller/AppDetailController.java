package org.bitvault.appstore.commons.application.controller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.bitvault.appstore.cloud.constant.APIConstants;
import org.bitvault.appstore.cloud.constant.Constants;
import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.dto.AppApplication;
import org.bitvault.appstore.cloud.dto.AppApplicationDetailDto;
import org.bitvault.appstore.cloud.dto.AppDetail;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.bitvault.appstore.cloud.exception.BitVaultResponse;
import org.bitvault.appstore.cloud.user.common.GeneralResponseModel;
import org.bitvault.appstore.cloud.user.dev.service.DevUserService;
import org.bitvault.appstore.cloud.utils.Utility;
import org.bitvault.appstore.commons.application.service.AppApplicationService;
import org.bitvault.appstore.commons.application.service.AppDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = APIConstants.DEV_API_BASE)
public class AppDetailController {
	@Autowired
	AppDetailService appDetailService;

	@Autowired
	DevUserService devUserService;

	@Autowired
	AppApplicationService appApplicationService;

	@RequestMapping(value = "/appDetail", method = RequestMethod.GET)
	public ResponseEntity<GeneralResponseModel> appDetail(@RequestParam Integer applicationId,
			@RequestParam String appVersionName) {
		GeneralResponseModel response = null;
		AppApplication appDto = null;
		Map<String, Object> responseMap = null;
		if (!Utility.isIntegerEmpty(applicationId) && !Utility.isStringEmpty(appVersionName)) {
			try {
				AppDetail appDetailDTO = appDetailService.findApplicationDetailByAppIdAndVersion(applicationId,
						appVersionName,"");
				appDto = appApplicationService.findApplicationByAppId(applicationId);

				if (null != appDetailDTO) {
					if (null != appDto) {
						AppApplicationDetailDto appApplicationDetailDto = AppApplicationDetailDto
								.populateAppApplicationDetailDto(appDetailDTO, appDto);
						responseMap = new HashMap<String, Object>();
						responseMap.put("appDetail", appApplicationDetailDto);
						responseMap.put("requester", devUserService.findByUserId(appDto.getUserId()).getEmail());

						response = GeneralResponseModel.of(Constants.SUCCESS, responseMap);

					}
				} else {
					throw new BitVaultException(ErrorMessageConstant.RESULT_NOT_FOUND);

				}

				return new ResponseEntity<GeneralResponseModel>(response, HttpStatus.OK);

			} catch (BitVaultException e) {
				response = GeneralResponseModel.of(Constants.FAILED, new BitVaultResponse(e));

				return new ResponseEntity<GeneralResponseModel>(response, HttpStatus.OK);

			}
		} else {
			response = GeneralResponseModel.of(Constants.FAILED,
					new BitVaultResponse(ErrorMessageConstant.FIELD_IS_EMPTY));

			return new ResponseEntity<GeneralResponseModel>(response, HttpStatus.OK);
		}

	}

	@PostMapping(value = "/saveAppDetail")
	public ResponseEntity<GeneralResponseModel> saveAppDetail(@Valid @RequestBody AppDetail appDetail, Errors errors) {
		Map<String, String> errorMap = null;
		GeneralResponseModel response = null;
		try {

			if (errors.hasErrors()) {
				errorMap = new HashMap<String, String>();
				for (ObjectError errorMessage : errors.getAllErrors()) {
					FieldError fe = (FieldError) errorMessage;
					errorMap.put(fe.getField(), errorMessage.getDefaultMessage());
					response = GeneralResponseModel.of(Constants.FAILED, errorMap);
				}

				return new ResponseEntity<GeneralResponseModel>(response, HttpStatus.OK);
			} else {
				AppApplication appApplication = appApplicationService
						.findApplicationByAppId(appDetail.getApplicationId());
				if (null != appApplication) {
					AppDetail appDetailEntity = null;
					AppDetail existingAppDetail = appDetailService.findApplicationDetailByAppIdAndVersion(
							appApplication.getAppApplicationId(), appApplication.getLatestVersionName(),"");
					if (null != existingAppDetail) {
						existingAppDetail.setFullDescription(appDetail.getFullDescription());
						existingAppDetail.setShortDescription(appDetail.getShortDescription());
						appDetailEntity = appDetailService
								.saveAppDetail(existingAppDetail.populateAppDetail(existingAppDetail));

					} else {

						appDetailEntity = appDetailService
								.saveAppDetail(appDetail.populateAppDetail(appApplication, appDetail));

					}
					if (null != appDetailEntity) {
						response = GeneralResponseModel.of(Constants.SUCCESS, appDetailEntity);
					}
					return new ResponseEntity<GeneralResponseModel>(response, HttpStatus.OK);

				} else {
					return new ResponseEntity<GeneralResponseModel>(GeneralResponseModel.of(Constants.FAILED,
							new BitVaultResponse(ErrorMessageConstant.RESULT_NOT_FOUND)), HttpStatus.OK);

				}
			}
		} catch (BitVaultException e) {
			e.printStackTrace();
			return new ResponseEntity<GeneralResponseModel>(
					GeneralResponseModel.of(Constants.FAILED, new BitVaultResponse(e)), HttpStatus.OK);
		}
	}

}
