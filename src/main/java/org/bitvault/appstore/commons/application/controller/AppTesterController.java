package org.bitvault.appstore.commons.application.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bitvault.appstore.cloud.constant.APIConstants;
import org.bitvault.appstore.cloud.constant.Constants;
import org.bitvault.appstore.cloud.constant.DbConstant;
import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.dto.AppApplication;
import org.bitvault.appstore.cloud.dto.AppTesterDto;
import org.bitvault.appstore.cloud.dto.MobileUser;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.bitvault.appstore.cloud.exception.BitVaultResponse;
import org.bitvault.appstore.cloud.user.common.GeneralResponseModel;
import org.bitvault.appstore.cloud.utils.Response;
import org.bitvault.appstore.cloud.utils.Utility;
import org.bitvault.appstore.commons.application.service.AppApplicationService;
import org.bitvault.appstore.commons.application.service.AppTesterService;
import org.bitvault.appstore.mobile.service.HelpSupportServiceImpl;
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

@RestController
@RequestMapping(value = APIConstants.DEV_API_BASE)
public class AppTesterController {

	@Autowired
	AppApplicationService appAppplicationService;
	@Autowired
	MobileUserService mobileService;
	@Autowired
	AppTesterService appTesterService;
	private static final Logger logger = LoggerFactory.getLogger(AppTesterController.class);

	@RequestMapping(value = APIConstants.APPTESTER, method = RequestMethod.POST)
	public ResponseEntity<?> appTester(@RequestParam List<String> publicAddresses,
			@RequestParam Integer applicationId) {

		GeneralResponseModel genralResponse = null;
		List<AppTesterDto> testList = new ArrayList<AppTesterDto>();
		try {
			if (publicAddresses.size() == 0) {
				throw new BitVaultException(ErrorMessageConstant.FIELD_IS_EMPTY);
			}
			if (Utility.isIntegerEmpty(applicationId)) {
				throw new BitVaultException(ErrorMessageConstant.FIELD_IS_EMPTY);
			}
			AppApplication appApplication = appAppplicationService.findApplicationByAppId(applicationId);
			logger.info("AppApplication Object fetched successfully");
			if (appApplication == null) {
				throw new BitVaultException(ErrorMessageConstant.RESULT_NOT_FOUND);
			}
			for (String publicAddress : publicAddresses) {
				try {
					AppTesterDto appTesterDto = new AppTesterDto();
					MobileUser mobileUser = mobileService.findMobileUserByPublicAddress(publicAddress);
					if (mobileUser == null) {
						throw new BitVaultException(ErrorMessageConstant.RESULT_NOT_FOUND);
					}

					appTesterDto.setAppApplicationId(appApplication.getAppApplicationId());
					appTesterDto.setPackageName(appApplication.getPackageName());
					appTesterDto.setPublicAddress(publicAddress);
					logger.info("ApptesterDto get saved");
					appTesterDto = appTesterService.saveAppTester(appTesterDto, appApplication);
					testList.add(appTesterDto);
				} catch (BitVaultException e) {
				}
			}
			genralResponse = GeneralResponseModel.of(Constants.SUCCESS, testList);
		} catch (BitVaultException e) {
			genralResponse = GeneralResponseModel.of(Constants.FAILED, new BitVaultResponse(e));

		}
		return new ResponseEntity<GeneralResponseModel>(genralResponse, HttpStatus.OK);
	}

	@RequestMapping(value = APIConstants.TESTLIST, method = RequestMethod.GET)
	public ResponseEntity<?> listOfAllApplication(@RequestParam(defaultValue = "0") int page,
			@RequestParam String publicAddress, @RequestParam(defaultValue = "5") int size,
			@RequestParam(defaultValue = Constants.UPDATED_AT) String orderBy,
			@RequestParam(defaultValue = DbConstant.DESC) String direction) {
		List<String> statusList = new ArrayList<String>();
		List<Integer> applicationIdList = new ArrayList<Integer>();
		Response response = null;
		GeneralResponseModel genralResponse = null;
		Map<String, Object> allAppMap = null;

		if (page > 0) {
			page = page - 1;
		}

		try {
			statusList.add("UNPUBLISHED");
			statusList.add("Beta-Published");
			statusList.add("Alpha-Draft");
			statusList.add("Beta-Draft");
			response = new Response();
			if (Utility.isStringEmpty(publicAddress)) {
				throw new BitVaultException(ErrorMessageConstant.FIELD_IS_EMPTY);
			}
			MobileUser mobileUser = mobileService.findMobileUserByPublicAddress(publicAddress);
			if (mobileUser == null) {
				throw new BitVaultException(ErrorMessageConstant.RESULT_NOT_FOUND);
			}
			List<AppTesterDto> appTesterDtoList = appTesterService.findByPublicAddress(publicAddress);
			for (AppTesterDto appTesterDto : appTesterDtoList) {
				applicationIdList.add(appTesterDto.getAppApplicationId());
			}
			logger.info("find By Status method calling");
			allAppMap = appAppplicationService.findAppApplicationByStatusList(statusList, applicationIdList, page, size,
					direction, orderBy);
			logger.info("Map Fetched successfully");
			response.setResult(allAppMap.get("appList"));
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
			logger.info("Error occured during list fetching"+e.getMessage());
			genralResponse = GeneralResponseModel.of(Constants.FAILED, new BitVaultResponse(e));

			return new ResponseEntity<GeneralResponseModel>(genralResponse, HttpStatus.OK);

		}

	}
}
