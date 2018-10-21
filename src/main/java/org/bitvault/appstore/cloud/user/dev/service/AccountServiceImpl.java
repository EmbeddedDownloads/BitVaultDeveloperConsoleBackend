package org.bitvault.appstore.cloud.user.dev.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.bitvault.appstore.cloud.constant.Constants;
import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.dto.AccountDto;
import org.bitvault.appstore.cloud.dto.AccountDtoMapper;
import org.bitvault.appstore.cloud.dto.DevUserDto;
import org.bitvault.appstore.cloud.dto.DevUserDtoMapper;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.bitvault.appstore.cloud.model.Account;
import org.bitvault.appstore.cloud.model.DevUser;
import org.bitvault.appstore.cloud.model.Role;
import org.bitvault.appstore.cloud.model.UserActivityType;
import org.bitvault.appstore.cloud.user.common.dao.RoleRepository;
import org.bitvault.appstore.cloud.user.common.service.RoleService;
import org.bitvault.appstore.cloud.user.dev.dao.AccountRepository;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

	private final Logger logger = org.slf4j.LoggerFactory.getLogger(AccountServiceImpl.class);

	@Autowired
	AccountRepository accountRepository;

	@Autowired
	RoleService roleService;

	@Autowired
	DevUserService devUserService;

	@Autowired
	RoleRepository roleRepository;

	@Override
	public DevUser saveAccount(DevUserDto user, DevUser userParent) {

		DevUser devUser = null;
		try {
			Account acc = null;
			Role role = null;
			String signupAs = user.getSignupAs();
			if (userParent == null && signupAs != null && signupAs.equalsIgnoreCase("Organization")
					&& user.getAccEmail() != null && user.getOrgName() != null) {
				acc = new Account();
				BeanUtils.copyProperties(user, acc);
				role = roleRepository.getOne(2);
				acc.setRole(role);
				acc.setStatus("INACTIVE");
				user.setChildCount(5);
			} else {
				acc = createDefaultAccount(user.getEmail());
				role = roleRepository.getOne(1);
				acc.setRole(role);
			}
			acc = accountRepository.save(acc);
			DevUser userModel = DevUserDtoMapper.toDevUser(user);
			userModel.setAccount(acc);
			userModel.setStatus("REVIEW");
			userModel.setLastPasswordResetDate(new Date(System.currentTimeMillis()));
			userModel.setRole(roleRepository.getOne(1));
			devUser = devUserService.saveUser(userModel);

			UserActivityType userActivtyType = new UserActivityType();
			userActivtyType.setActivityType(Constants.NEW_USER);

			Map<String, String> dataMap = new HashMap<>();

			if (userParent != null) {
				String owner = userParent.getUserId();
				acc.setCreatedBy(owner);
				userModel.setParentId(owner);
				userModel.setCreatedBy(owner);
				userModel.setPrivateKey(userParent.getPrivateKey());
				userModel.setPublicKey(userParent.getPublicKey());
				userParent.setChildCreated(userParent.getChildCreated() + 1);

				dataMap.put(Constants.SUB_DEV_USERID, devUser.getUserId());
				dataMap.put(Constants.SUB_DEV_USERNAME, devUser.getUsername());
				dataMap.put(Constants.SUB_DEV_EMAIL, devUser.getEmail());

				devUser = devUserService.saveUser(userParent);
				userActivtyType.setActivityType(Constants.NEW_SUBDEV_USER);
			}

			dataMap.put(Constants.USERID, devUser.getUserId());
			dataMap.put(Constants.USER_NAME, devUser.getUsername());

			devUserService.saveUserActivity(userActivtyType, dataMap);
			return devUser;
		} catch (Exception e) {
			logger.error(e.getMessage());
			if (e instanceof DataIntegrityViolationException) {
				throw new BitVaultException(ErrorMessageConstant.ORG_ALREADY_EXISTS);
			}
			throw new BitVaultException(e.getMessage());
		}
	}

	@Override
	public AccountDto getAccountById(Integer id) {
		try {
			Account account = accountRepository.getOne(id);
			return AccountDtoMapper.toAccountDto(account);
		} catch (Exception e) {
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.bitvault.appstore.cloud.user.dev.service.AccountService#isExists(org.
	 * bitvault.appstore.cloud.dto.AccountDto)
	 */
	@Override
	public boolean isExists(AccountDto dto) {
		try {
			AccountDto acc = getAccountByEmail(dto.getEmail());
			if (acc == null) {
				return false;
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.bitvault.appstore.cloud.user.dev.service.AccountService#
	 * getAccountByEmail(java.lang.String)
	 */
	@Override
	public AccountDto getAccountByEmail(String email) {
		try {
			Account acc = accountRepository.findByAccEmail(email);
			return AccountDtoMapper.toAccountDto(acc);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public Account createDefaultAccount(String email) {
		try {
			Account account = new Account();
			account.setAccEmail(email);
			account.setOrgName("Google");
			account.setCountry("INDIA");
			account.setState("DELHI");
			account.setWebsite("www.sample.com");
			account.setStatus("INACTIVE");
			return account;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public void updateAccount(Account account) {
		accountRepository.saveAndFlush(account);
	}

	@Override
	public Account getAccount(Integer id) {
		try {
			Account account = accountRepository.getOne(id);
			return account;
		} catch (Exception e) {
			return null;
		}
	}

}
