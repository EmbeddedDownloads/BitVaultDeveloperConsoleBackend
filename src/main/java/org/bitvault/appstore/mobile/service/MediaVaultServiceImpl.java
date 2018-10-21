package org.bitvault.appstore.mobile.service;

import java.util.List;

import org.bitvault.appstore.cloud.constant.DbConstant;
import org.bitvault.appstore.cloud.model.MediaVault;
import org.bitvault.appstore.mobile.dao.MediaVaultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Service
public class MediaVaultServiceImpl implements MediaVaultService {

	@Autowired
	private MediaVaultRepository mediaVaultRepository;

	@Override
	public void save(MediaVault mediaVault) {
		mediaVaultRepository.saveAndFlush(mediaVault);
	}

	@Override
	public void delete(MediaVault mediaVault) {
		mediaVaultRepository.delete(mediaVault);
	}

	@Override
	public MediaVault getFileInfoByIdAndAppID(String id, Integer appId) {
		return mediaVaultRepository.findByIdAndAppId(id, appId);
	}

	@Override
	public Page<MediaVault> getFileListOfAppForAllWallets(String appId, List<String> walletAddresses, int page,
			int size, String orderBy, String direction) {
		if (direction.equals(DbConstant.DESC))
			return mediaVaultRepository.getFileListOfAppForAllWallets(appId, walletAddresses,
					new PageRequest(page, size, new Sort(Direction.DESC, orderBy)));
		else
			return mediaVaultRepository.getFileListOfAppForAllWallets(appId, walletAddresses,
					new PageRequest(page, size, new Sort(Direction.ASC, orderBy)));
	}

	@Override
	public MediaVault getOne(String hashId) {
		return mediaVaultRepository.findOne(hashId);
	}

}
