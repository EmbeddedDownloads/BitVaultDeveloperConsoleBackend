package org.bitvault.appstore.cloud.dto;

import org.bitvault.appstore.cloud.model.Account;
import org.bitvault.appstore.cloud.model.Role;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;

public class AccountDtoMapper {
	public static Account toAccount(AccountDto dto){
		try {
			Account account = new Account();
			Role role = new Role();
			BeanUtils.copyProperties(dto, account);
			account.setRole(role);
			return account;
		} catch (BeansException e) {
			return null;
		}
	}
	public static AccountDto toAccountDto(Account account){
		try {
			AccountDto dto = new AccountDto();
			BeanUtils.copyProperties(account, dto);
			return dto;
		} catch (BeansException e) {
			return null;
		}
	}
}
