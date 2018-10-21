package org.bitvault.appstore.commons.application.service;

import java.util.ArrayList;
import java.util.List;

import org.bitvault.appstore.cloud.dto.AppApplication;
import org.bitvault.appstore.cloud.dto.AppTesterDto;
import org.bitvault.appstore.cloud.model.AppTester;
import org.bitvault.appstore.commons.application.controller.AppTesterController;
import org.bitvault.appstore.commons.application.dao.AppTesterRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AppTesterServiceImpl implements AppTesterService {
	@Autowired
	AppTesterRepository appTesterRepository;
	private static final Logger logger = LoggerFactory.getLogger(AppTesterServiceImpl.class);

	public AppTesterDto saveAppTester(AppTesterDto appSupportDto, AppApplication appApplicationDto) {

		try {
			AppTester appTester = appSupportDto.populateAppTesterDto(appSupportDto);
			appTester.setApplication(appApplicationDto.populateAppApplication(appApplicationDto));
			appTester = appTesterRepository.saveAndFlush(appTester);
			logger.info("AppTesterDto saved successfully");
			appSupportDto = appTester.populateAppTesterDto(appTester);
			return appSupportDto;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public List<AppTesterDto> findByPublicAddress(String publicAddress) {
		List<AppTesterDto> appTesterDtoList = new ArrayList<AppTesterDto>();
		List<AppTester> appTesterList = appTesterRepository.FindByPublicAddress(publicAddress);
		for (AppTester appTester : appTesterList) {
			AppTesterDto appTesterDto = appTester.populateAppTesterDto(appTester);
			logger.info("Dto populated sucessfully");
			appTesterDtoList.add(appTesterDto);
		}
		logger.info("List fetched successfully");
		return appTesterDtoList;
	}

}
