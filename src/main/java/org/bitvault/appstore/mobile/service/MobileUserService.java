package org.bitvault.appstore.mobile.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bitvault.appstore.cloud.dto.MobileUser;
import org.bitvault.appstore.cloud.exception.BitVaultException;

public interface MobileUserService {

	MobileUser saveMobileUser(MobileUser mobileUser) throws BitVaultException;

	MobileUser findMobileUserByPublicAddress(String publicAddress) throws BitVaultException;

	List<MobileUser> findAllMobileUser() throws BitVaultException;
	
	Boolean downloadApkFile(String filePath, HttpServletRequest request, HttpServletResponse response,String publicKey)
			throws BitVaultException;
}
