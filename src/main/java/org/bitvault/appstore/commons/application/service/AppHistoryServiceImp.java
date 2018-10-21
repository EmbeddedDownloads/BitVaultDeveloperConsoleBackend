package org.bitvault.appstore.commons.application.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bitvault.appstore.cloud.constant.Constants;
import org.bitvault.appstore.cloud.constant.DbConstant;
import org.bitvault.appstore.cloud.constant.ErrorCode;
import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.dto.AppHistoryDto;
import org.bitvault.appstore.cloud.dto.AppInfoDto;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.bitvault.appstore.cloud.model.AppApplication;
import org.bitvault.appstore.cloud.model.AppHistory;
import org.bitvault.appstore.commons.application.controller.AppApplicationController;
import org.bitvault.appstore.commons.application.dao.AppHistoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("AppHistoryService")
@Transactional
public class AppHistoryServiceImp implements AppHistoryService {

	@Autowired
	AppHistoryRepository appHistoryRepository;
	@Autowired
	AppApplicationService appAppService;
	public static final Logger logger = LoggerFactory.getLogger(AppHistoryServiceImp.class);
	@Override
	public AppHistoryDto saveAppHistory(AppInfoDto oldAppInfo, AppInfoDto newAppInfo, AppApplication appApp, String feild) {
		AppHistory appHistory = null;
		AppHistoryDto appHistoryDto = null;
		
		try {
			if(null==oldAppInfo){
				newAppInfo.setVersionCode(appApp.getLatestVersionNo());
				newAppInfo.setVersionName(appApp.getLatestVersionName());
				appHistory = newAppInfo.populateAppHistory(newAppInfo);
				appHistory.setUpdatedFeilds(feild);
				
			}else{
				appHistory = newAppInfo.populateListOfChanges(oldAppInfo,newAppInfo);
			}
			
			
			appHistory.setAppApplication(appApp);
			
			
			appHistory = appHistoryRepository.save(appHistory);
			appHistoryDto = appHistory.populateAppHistoryDto(appHistory);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);
		}
		return appHistoryDto;
	}

	@Override
	public Map<String, Object> appHistory(Integer applicationId,int page, int size, String direction, String property) {
		Page<AppHistory> appHistoryList = null;
		AppHistoryDto appHistoryDto = null;
		List<AppHistoryDto> appHistoryDTOList = new ArrayList<AppHistoryDto>();
		Map<String, Object> allAppMap = null;

		try {
			org.bitvault.appstore.cloud.dto.AppApplication appAppDto = appAppService
					.findApplicationByAppId(applicationId);
			if (null == appAppDto) {
				throw new BitVaultException(ErrorMessageConstant.NO_REQUEST_FOUND);

			}
			if (direction.equals(DbConstant.ASC)) {
				appHistoryList = appHistoryRepository.findAppHistoryByAppApplicationAppApplicationId(applicationId,
						new PageRequest(page, size, Sort.Direction.ASC, property));
			} else {
				appHistoryList = appHistoryRepository.findAppHistoryByAppApplicationAppApplicationId(applicationId,
						new PageRequest(page, size, Sort.Direction.DESC, property));

			}
			for (AppHistory appHistory : appHistoryList) {
				appHistoryDto = appHistory.populateAppHistoryDto(appHistory);
				appHistoryDTOList.add(appHistoryDto);
			}
			allAppMap = new HashMap<String, Object>();
			allAppMap.put("appHistoryList", appHistoryDTOList);
			allAppMap.put(Constants.TOTAL_PAGES, appHistoryList.getTotalPages());
			allAppMap.put(Constants.TOTAL_RECORDS, appHistoryList.getTotalElements());
			allAppMap.put(Constants.SORT, appHistoryList.getSort().getOrderFor(property).getDirection());
			allAppMap.put(Constants.SIZE, appHistoryList.getNumberOfElements());

		} catch (Exception e) {
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED, ErrorCode.SOME_ERROR_OCCURED_CODE);
		}

		return allAppMap;

	}

}
