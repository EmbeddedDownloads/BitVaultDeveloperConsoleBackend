package org.bitvault.appstore.commons.application.service;

import java.util.Map;

import org.bitvault.appstore.cloud.dto.AppHistoryDto;
import org.bitvault.appstore.cloud.dto.AppInfoDto;
import org.bitvault.appstore.cloud.model.AppApplication;

public interface AppHistoryService {

	AppHistoryDto saveAppHistory(AppInfoDto oldAppInfo, AppInfoDto newAppInfo, AppApplication appApp, String feilds);
	Map<String, Object> appHistory(Integer applicationId,int page, int size, String direction, String property);

}
