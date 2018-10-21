package org.bitvault.appstore.cloud.user.dev.service;

import java.util.List;
import java.util.Map;

import org.bitvault.appstore.cloud.dto.DevUserDto;
import org.bitvault.appstore.cloud.dto.UserActivityDto;

public interface UserActivityService {
	public UserActivityDto saveUserHistory(UserActivityDto userActivityDto);  
	
	public Map<String,Object> findAllRequestByUser(String userId,String userName,String email ,int page, int size, String direction,
			String property);
	
}
