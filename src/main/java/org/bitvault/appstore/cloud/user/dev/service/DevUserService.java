package org.bitvault.appstore.cloud.user.dev.service;

import java.util.List;
import java.util.Map;

import org.bitvault.appstore.cloud.dto.DevUserDto;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.bitvault.appstore.cloud.model.DevUser;
import org.bitvault.appstore.cloud.model.UserActivityType;
import org.bitvault.appstore.cloud.utils.Response;

public interface DevUserService {
	
	DevUserDto findByEmail(String email);
	
	DevUser findByEmailId(String email);
	
	DevUser findByUserId(String userId);

	DevUser saveUser(DevUser user);

	void updateUser(DevUser currentUser, DevUserDto userDto);

	void deleteUserById(Long id);

	Response findAllUsers(int page, int size, String orderBy, List<String> status, String direction);

	void saveUserActivityForNewUser(UserActivityType userActivityType,String userId,String userName);
	
	void saveUserActivity(UserActivityType userActivityType,Map<String,String> dataMap);
	
//	@Secured({"ROLE_ADMIN"})
	boolean isUserExist(DevUserDto user);

	DevUserDto findUserDtoByUserId(String userId);

	void changePassword(DevUser currentUser);
	
	int getUserCount(List<String> userStatus);
	
	Response getSubDevList(String createdBy, String userId, int page, int size, String orderBy, List<String> status, String direction);
	
	public String generateVerificationLink(String verificationLink, String createdBy, String email);
	
	Map<String, Object> searchUserByUserName(String username, String userId,int page, int size, String direction,
			String property) throws BitVaultException;
	
}
