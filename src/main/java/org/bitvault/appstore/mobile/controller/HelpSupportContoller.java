package org.bitvault.appstore.mobile.controller;

import java.util.Map;

import org.bitvault.appstore.cloud.constant.APIConstants;
import org.bitvault.appstore.cloud.constant.Constants;
import org.bitvault.appstore.cloud.constant.DbConstant;
import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.dto.HelpSupportDto;
import org.bitvault.appstore.cloud.dto.MobileUser;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.bitvault.appstore.cloud.exception.BitVaultResponse;
import org.bitvault.appstore.cloud.user.common.GeneralResponseModel;
import org.bitvault.appstore.cloud.utils.Response;
import org.bitvault.appstore.cloud.utils.Utility;
import org.bitvault.appstore.mobile.service.HelpSupportService;
import org.bitvault.appstore.mobile.service.MobileUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = APIConstants.MOBILE_API_BASE)
public class HelpSupportContoller {
	private static final Logger logger = LoggerFactory.getLogger(HelpSupportContoller.class);

	@Autowired
	MobileUserService mobileService;

	@Autowired
	HelpSupportService helpSupportService;

	@RequestMapping(value = APIConstants.HELPSUPPORT, method = RequestMethod.POST)
	public ResponseEntity<?> heplSupport(@RequestParam(required = true) String publicAddress,
			@RequestParam(required = false) String message, @RequestParam(required = false) String phoneNo,
			@RequestParam(required = true) String type, @RequestParam(required = false) MultipartFile image) {

		HelpSupportDto helpSupportDto = new HelpSupportDto();
		GeneralResponseModel genralResponse = null;
		String imageUrl = null;
		try {
			if (Utility.isStringEmpty(publicAddress)) {
				throw new BitVaultException(ErrorMessageConstant.FIELD_IS_EMPTY);
			}
			MobileUser mobileUser = mobileService.findMobileUserByPublicAddress(publicAddress);
			if (mobileUser == null) {
				throw new BitVaultException(ErrorMessageConstant.RESULT_NOT_FOUND);
			}
			if (Utility.isStringEmpty(type))
				throw new BitVaultException(ErrorMessageConstant.FIELD_IS_EMPTY);

			if (type.equalsIgnoreCase(Constants.FEEDBACK)) {
				if (message != null && !Utility.isStringEmpty(message.trim())) {
					helpSupportDto.setFromAdd(publicAddress);
					helpSupportDto.setMessage(message);
					helpSupportDto.setType(type);
					if (image != null) {
						imageUrl = helpSupportService.uploadApkImages(image, helpSupportDto);
						logger.info("Image on AWS uploaded successfully ");
					}
					helpSupportDto.setImageUrl(imageUrl);
					helpSupportDto = helpSupportService.saveHelpSupport(helpSupportDto);
					logger.info("Feedback saved successfully");
				} else {
					throw new BitVaultException(ErrorMessageConstant.FIELD_IS_EMPTY);
				}
			} else {
				if (phoneNo != null && !Utility.isStringEmpty(phoneNo.trim())) {
					helpSupportDto.setFromAdd(publicAddress);
					helpSupportDto.setPhoneNo(phoneNo);
					helpSupportDto.setType(type);
					helpSupportDto.setMessage(message);
					helpSupportDto = helpSupportService.saveHelpSupport(helpSupportDto);
					logger.info("Contact saved successfully");
				} else {
					throw new BitVaultException(ErrorMessageConstant.FIELD_IS_EMPTY);
				}
			}
			genralResponse = GeneralResponseModel.of(Constants.SUCCESS, helpSupportDto);
		} catch (BitVaultException e) {
			logger.info("Error occured during help & support" + e.getMessage());
			genralResponse = GeneralResponseModel.of(Constants.FAILED, new BitVaultResponse(e));

		}
		return new ResponseEntity<GeneralResponseModel>(genralResponse, HttpStatus.OK);
	}

	@RequestMapping(value = APIConstants.LIST_OF_HELP_SUPPORT, method = RequestMethod.GET)
	public ResponseEntity<?> listOfAllApplication(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size,
			@RequestParam(defaultValue = Constants.UPDATED_AT) String orderBy) {

		Response response = null;
		GeneralResponseModel genralResponse = null;
		Map<String, Object> allAppMap = null;

		if (page > 0) {
			page = page - 1;
		}

		try {
			response = new Response();
			allAppMap = helpSupportService.listOfAllHelpSupport(page, size, DbConstant.ASC, orderBy);
			logger.info("Map fetched successfully");
			response.setResult(allAppMap.get("appList"));
			// response.setType(APIConstants.LIST_OF_ALL_APPLICATION);
			response.setStatus(Constants.SUCCESS);
			int totalPages = (int) allAppMap.get(Constants.TOTAL_PAGES);
			int totalRecord = ((Long) allAppMap.get(Constants.TOTAL_RECORDS)).intValue();
			int pageSize = (int) allAppMap.get(Constants.SIZE);
			response.setSize(pageSize);
			response.setTotalPages(totalPages);
			response.setTotalRecords(totalRecord);
			response.setSort(orderBy);
			return new ResponseEntity<Response>(response, HttpStatus.OK);

		} catch (BitVaultException e) {
			genralResponse = GeneralResponseModel.of(Constants.FAILED, new BitVaultResponse(e));

			return new ResponseEntity<GeneralResponseModel>(genralResponse, HttpStatus.OK);

		}

	}
}
