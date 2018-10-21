package org.bitvault.appstore.commons.application.service;


import org.bitvault.appstore.cloud.model.AppDownloadUrl;

public interface AppDownloadService {
	AppDownloadUrl saveAppDownloadUrl(String url,String publicKey);

	AppDownloadUrl findAppDownloadById(String urlId);

	Integer deleteAppDownloadById(String urlId);

}
