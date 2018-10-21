package org.bitvault.appstore.commons.application.dao;

import org.bitvault.appstore.cloud.model.RejectedApp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RejectedAppRepository  extends JpaRepository<RejectedApp, Integer> {

	
}
