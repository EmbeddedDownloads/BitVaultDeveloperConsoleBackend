package org.bitvault.appstore.commons.application.service;

import java.util.List;

import org.bitvault.appstore.cloud.dto.AppApplication;
import org.bitvault.appstore.cloud.dto.AppTesterDto;
import org.springframework.web.multipart.MultipartFile;

public interface AppTesterService {


	AppTesterDto saveAppTester(AppTesterDto AppSupport,AppApplication appApplicationDTO);
    List<AppTesterDto>findByPublicAddress(String publicAddress);
   

















}
