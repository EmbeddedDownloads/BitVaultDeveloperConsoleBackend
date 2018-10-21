package org.bitvault.appstore.mobile.service;

import java.util.Map;

import org.bitvault.appstore.cloud.dto.HelpSupportDto;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.springframework.web.multipart.MultipartFile;

public interface HelpSupportService
{
	HelpSupportDto saveHelpSupport(HelpSupportDto helpSupport);
	Map<String, Object> listOfAllHelpSupport(int page, int size, String direction, String property)
			throws BitVaultException;
	 String uploadApkImages(MultipartFile apkImage,HelpSupportDto helpSupportDto);
	    
}
