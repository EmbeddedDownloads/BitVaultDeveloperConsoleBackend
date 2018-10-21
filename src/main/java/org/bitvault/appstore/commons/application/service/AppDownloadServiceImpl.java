package org.bitvault.appstore.commons.application.service;

import java.util.UUID;

import org.bitvault.appstore.cloud.constant.ErrorCode;
import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.bitvault.appstore.cloud.model.AppDownloadUrl;
import org.bitvault.appstore.commons.application.dao.AppDownloadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("AppDownloadService")
@Transactional
public class AppDownloadServiceImpl implements AppDownloadService {

	@Autowired
	AppDownloadRepository appDownloadRepository;

	@Override
	public AppDownloadUrl findAppDownloadById(String urlId) {
		AppDownloadUrl appDownload = null;
		try {
			appDownload = appDownloadRepository.findAppDownloadById(urlId);

		} catch (Exception e) {
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED, ErrorCode.SOME_ERROR_OCCURED_CODE);

		}
		if (null == appDownload) {
			throw new BitVaultException(ErrorMessageConstant.RESULT_NOT_FOUND, ErrorCode.RESULT_NOT_FOUND_CODE);

		}
		return appDownload;
	}

	@Override
	public Integer deleteAppDownloadById(String urlId) {
		int delete = 0;
		try {
			delete = appDownloadRepository.deleteAppDownloadById(urlId);

		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED, ErrorCode.SOME_ERROR_OCCURED_CODE);

		}

		return delete;
	}

	@Override
	public AppDownloadUrl saveAppDownloadUrl(String url,String publicKey) {
		AppDownloadUrl appDownLoad = null;
		try {
			appDownLoad = new AppDownloadUrl();
			String uuid = UUID.randomUUID().toString();
			appDownLoad.setUrl(url);
			appDownLoad.setUrlId(uuid);
			appDownLoad.setPublicKey(publicKey);
			appDownloadRepository.saveAndFlush(appDownLoad);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.UNABLE_TO_SAVE, ErrorCode.UNABLE_TO_SAVE_CODE);
		}

		return appDownLoad;
	}

}
