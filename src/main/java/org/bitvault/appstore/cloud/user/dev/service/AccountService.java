package org.bitvault.appstore.cloud.user.dev.service;

import org.bitvault.appstore.cloud.dto.AccountDto;
import org.bitvault.appstore.cloud.dto.DevUserDto;
import org.bitvault.appstore.cloud.model.Account;
import org.bitvault.appstore.cloud.model.DevUser;

public interface AccountService {
	public DevUser saveAccount(DevUserDto user, DevUser userParent);

	AccountDto getAccountById(Integer id);

	Account getAccount(Integer id);

	AccountDto getAccountByEmail(String email);

	boolean isExists(AccountDto dto);

	Account createDefaultAccount(String email);

	public void updateAccount(Account account);
}
