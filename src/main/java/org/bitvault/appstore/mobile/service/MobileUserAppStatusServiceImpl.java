package org.bitvault.appstore.mobile.service;

import java.util.List;

import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.dto.AppApplication;
import org.bitvault.appstore.cloud.dto.MobileUser;
import org.bitvault.appstore.cloud.dto.MobileUserAppStatusDto;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.bitvault.appstore.cloud.model.MobileUserAppStatus;
import org.bitvault.appstore.mobile.dao.MobileUserAppStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MobileUserAppStatusServiceImpl implements MobileUserAppStatusService {
	@Autowired
	MobileUserAppStatusRepository mobileUserAppStatusRep;
	@Autowired
	MobileUserService mobileUserService;

	@Override
	public MobileUserAppStatus saveMobileUserAppStatus(AppApplication appAppDto, MobileUser mobileUser, String status,
			String appTxnId) {
		MobileUserAppStatus mobileUserAppStatus = null;
		org.bitvault.appstore.cloud.dto.MobileUserAppStatusDto mobileUserAppStatusDto = new MobileUserAppStatusDto();
		try {

			mobileUserAppStatus = mobileUserAppStatusRep
					.findMobileStatusByAppIdAndMobUserId(appAppDto.getAppApplicationId(), mobileUser.getMobileUserId());
			if (mobileUserAppStatus != null) {
				mobileUserAppStatus.setStatus(status);
				mobileUserAppStatus = mobileUserAppStatusRep.saveAndFlush(mobileUserAppStatus);

			} else {
				mobileUserAppStatusDto.setApplication(appAppDto);
				mobileUserAppStatusDto.setAppPrice(appAppDto.getAppPrice());
				mobileUserAppStatusDto.setAppTxnId(appTxnId);
				mobileUserAppStatusDto.setMobileUser(mobileUser);
				mobileUserAppStatusDto.setStatus(status);

				mobileUserAppStatus = mobileUserAppStatusDto.populateMobileUserAppStatus(mobileUserAppStatusDto);
				mobileUserAppStatus = mobileUserAppStatusRep.saveAndFlush(mobileUserAppStatus);
			}
			return mobileUserAppStatus;
		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);
		}

	}

	@Override
	public List<MobileUserAppStatus> findAllMobileUserFromAppIdAndStatus(Integer applicationId,String status) {

		try {
			List<MobileUserAppStatus> mobileUserAppStatusList = mobileUserAppStatusRep
					.findMobileStatusByAppIdAndStatus(applicationId, status);

			return mobileUserAppStatusList;
		} catch (Exception e) {
			return null;
		}
	}
	
	@Override
	public List<MobileUserAppStatus> findAllMobileUserFromAppId(Integer applicationId) {

		try {
			List<MobileUserAppStatus> mobileUserAppStatusList = mobileUserAppStatusRep
					.findMobileStatusByAppId(applicationId);

			return mobileUserAppStatusList;
		} catch (Exception e) {
			return null;
		}
	}

}
