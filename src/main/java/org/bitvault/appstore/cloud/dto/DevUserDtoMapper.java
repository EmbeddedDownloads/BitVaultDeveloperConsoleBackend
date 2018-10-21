package org.bitvault.appstore.cloud.dto;

import org.bitvault.appstore.cloud.constant.Constants;
import org.bitvault.appstore.cloud.constant.RoleConstant;
import org.bitvault.appstore.cloud.model.Account;
import org.bitvault.appstore.cloud.model.DevUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

public class DevUserDtoMapper {
	
	public static final Logger logger = LoggerFactory.getLogger(DevUserDtoMapper.class);
	
	public static DevUser toDevUser(DevUserDto dto){
		try {
			DevUser user = new DevUser();
			BeanUtils.copyProperties(dto, user);
			logger.info("Sucessfully copy all UserDto properties to UserModel.");
			return user;
		} catch (Exception e) {
			logger.error("Exception occured..."+ e.getMessage());
			return null;
			//throw new NullArgumentException();
		}
	}
	
	public static DevUserDto toDevUserDto(DevUser user){
		try {
			DevUserDto dto = new DevUserDto();
			BeanUtils.copyProperties(user, dto);
			dto.setPassword("");
			Account account = user.getAccount();			
			String signUpAs = account.getRole().getRoleName();
			if(signUpAs.equals("ROLE_" + RoleConstant.DEVELOPER))
				dto.setSignupAs(Constants.DEV);
			else{
				BeanUtils.copyProperties(account, dto);
				dto.setSignupAs(Constants.ORG);
			}
//			dto.setRole(signUpAs.substring(5));
			dto.setRole(RoleConstant.DEVELOPER);
			logger.info("Sucessfully copy all UserModel properties to UserDto.");
			return dto;
		} catch (Exception e) {
			logger.error("Exception occured..."+ e.getMessage());
			return null;
		}
	}

}
