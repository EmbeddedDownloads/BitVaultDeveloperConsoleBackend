package org.bitvault.appstore.commons.application.dao;

import org.bitvault.appstore.cloud.model.AppDownloadUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AppDownloadRepository extends JpaRepository<AppDownloadUrl, Long> {

	@Query("select appDownload from AppDownloadUrl appDownload where appDownload.urlId =?1")
	AppDownloadUrl findAppDownloadById(String urlId);

	@Modifying
	@Query("delete from AppDownloadUrl appDownload where appDownload.urlId = ?1")
	Integer deleteAppDownloadById(String urlId);

}
