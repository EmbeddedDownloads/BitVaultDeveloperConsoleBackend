package org.bitvault.appstore.mobile.service;

import java.util.List;

import org.bitvault.appstore.cloud.model.MobileUserAppStatus;
import org.springframework.stereotype.Service;

@Service
public interface MobileUserAppStatusService 
{
	 MobileUserAppStatus  saveMobileUserAppStatus(org.bitvault.appstore.cloud.dto.AppApplication appApplication,org.bitvault.appstore.cloud.dto.MobileUser mobileUser,String status,String appTxnId);

	 List<MobileUserAppStatus> findAllMobileUserFromAppId(Integer applicationId);
	 
	 List<MobileUserAppStatus> findAllMobileUserFromAppIdAndStatus(Integer applicationId,String status);
}
