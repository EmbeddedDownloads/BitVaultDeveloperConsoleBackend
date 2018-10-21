package org.bitvault.appstore.commons.application.service;

import javax.transaction.Transactional;

import org.bitvault.appstore.cloud.dto.RejectedAppDto;
import org.bitvault.appstore.cloud.model.RejectedApp;
import org.bitvault.appstore.commons.application.dao.RejectedAppRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class RejectedAppServiceImpl implements RejectedAppService {

	@Autowired
	RejectedAppRepository rejectedAppRepository;

	@Override
	public RejectedAppDto saveRejectedApp(RejectedAppDto rejAppDto) {
		RejectedApp rejApp = null;
		
		try {
			rejApp = rejAppDto.populateRejectedApp(rejAppDto);
			rejApp = rejectedAppRepository.saveAndFlush(rejApp);
			rejAppDto = rejApp.populateRejectedAppDto(rejApp);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return rejAppDto;
	}

}
