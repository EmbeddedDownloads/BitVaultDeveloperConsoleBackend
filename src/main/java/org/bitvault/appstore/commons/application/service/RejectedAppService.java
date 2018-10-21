package org.bitvault.appstore.commons.application.service;

import org.bitvault.appstore.cloud.dto.RejectedAppDto;

public interface RejectedAppService {
	
	RejectedAppDto saveRejectedApp(RejectedAppDto rejAppDto);

}
