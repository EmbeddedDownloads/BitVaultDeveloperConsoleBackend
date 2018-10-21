package org.bitvault.appstore.mobile.dao;

import java.util.List;

import org.bitvault.appstore.cloud.model.MediaVault;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaVaultRepository extends JpaRepository<MediaVault, String>{
	
	MediaVault findByIdAndAppId(String id, Integer appId);
	
	@Query("from MediaVault media where media.appId = :appId and media.walletAddress in (:walletAddresses)")
	Page<MediaVault> getFileListOfAppForAllWallets(@Param("appId") String appId, @Param("walletAddresses") List<String> walletAddresses, Pageable pageable);
}
