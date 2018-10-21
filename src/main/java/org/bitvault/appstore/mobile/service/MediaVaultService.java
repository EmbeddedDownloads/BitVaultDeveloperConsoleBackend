package org.bitvault.appstore.mobile.service;

import java.util.List;

import org.bitvault.appstore.cloud.model.MediaVault;
import org.springframework.data.domain.Page;

public interface MediaVaultService {
	MediaVault getOne(String hashId);
	void save(MediaVault mediaVault);
	void delete(MediaVault mediaVault);
	MediaVault getFileInfoByIdAndAppID(String id, Integer appId);
	Page<MediaVault> getFileListOfAppForAllWallets(String appId, List<String> walletAddresses, int page, int size, String orderBy, String direction);
}
