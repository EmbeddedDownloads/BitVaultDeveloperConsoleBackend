package org.bitvault.appstore.cloud.user.dev.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bitvault.appstore.cloud.constant.Constants;
import org.bitvault.appstore.cloud.constant.DbConstant;
import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.dto.AppApplication;
import org.bitvault.appstore.cloud.dto.ApplicationReqActDto;
import org.bitvault.appstore.cloud.dto.DevUserDto;
import org.bitvault.appstore.cloud.dto.RequestActivityDto;
import org.bitvault.appstore.cloud.dto.RequestTypeDto;
import org.bitvault.appstore.cloud.dto.UserActivityDto;
import org.bitvault.appstore.cloud.dto.UserActivityResultDto;
import org.bitvault.appstore.cloud.dto.UserData;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.bitvault.appstore.cloud.model.RequestType;
import org.bitvault.appstore.cloud.model.UserActivity;
import org.bitvault.appstore.cloud.user.dev.dao.UserActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class UserActivityServiceImpl implements UserActivityService {

	@Autowired
	UserActivityRepository userActivityRepository;

	@Override
	public UserActivityDto saveUserHistory(UserActivityDto userActivityDto) {

		UserActivity userActivity = null;

		if (userActivityDto == null) {
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);
		}

		userActivity = userActivityDto.populateUserActivity(userActivityDto);

		userActivityRepository.saveAndFlush(userActivity);

		return null;
	}

	@Override
	public Map<String, Object> findAllRequestByUser(String userId, String userName ,String email,int page, int size, String direction,
			String property) {
		Page<UserActivity> userList = null;

		Map<String, Object> allAppMap = null;

		List<UserData> userDTOList = new ArrayList<UserData>();

		try {	
			if (direction.equals(DbConstant.ASC.toString())) {
				userList = userActivityRepository.findUserActivityByUserId(userId,
						new PageRequest(page, size, Sort.Direction.ASC, property));
			} else {
				userList = userActivityRepository.findUserActivityByUserId(userId,
						new PageRequest(page, size, Sort.Direction.DESC, property));

			}

			UserActivityResultDto userActivityResult = new UserActivityResultDto();

			userActivityResult.setUserId(userId);
			userActivityResult.setUserName(userName);
			userActivityResult.setEmail(email);

			String type;

			for (UserActivity userActivity : userList) {

				UserData userData = new UserData();

				userData.setDate(userActivity.getCreatedAt());
				type = userActivity.getUserActivityType().getActivityType();
				userData.setType(type);
				getUpdatesOnBasisType(type,userActivity,userData);

				userDTOList.add(userData);
				
			}

			userActivityResult.setData(userDTOList);
			
			 allAppMap = new HashMap<String, Object>();
			 allAppMap.put("userList", userActivityResult);
			 allAppMap.put(Constants.TOTAL_PAGES, userList.getTotalPages());
			 allAppMap.put(Constants.TOTAL_RECORDS, userList.getTotalElements());
			 allAppMap.put(Constants.SIZE, userList.getNumberOfElements());
			 allAppMap.put(Constants.SORT, userList.getSort());
		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);
		}

		return allAppMap;

	}

	private void getUpdatesOnBasisType(String type,UserActivity userActivity,UserData userData) {

		Map<String,String> dataMap = new HashMap<>();
		
		switch (type.toLowerCase()) {
		case Constants.EDIT_PROFILE:

			dataMap.put(Constants.AVATAR_URL,userActivity.getAvatarURL());
			dataMap.put(Constants.USER_NAME, userActivity.getUserName());
			userData.setUpdates(dataMap);
			
			break;

		case Constants.NEW_USER:

			break;

		case Constants.NEW_SUBDEV_USER:

			dataMap.put(Constants.SUB_DEV_USERID, userActivity.getSubDevUserId());
			dataMap.put(Constants.SUB_DEV_USERNAME, userActivity.getSubDevUserName());
			dataMap.put(Constants.SUB_DEV_EMAIL, userActivity.getSubDevEmail());
			userData.setUpdates(dataMap);
			break;

		case Constants.PAYMENT:

			dataMap.put(Constants.PAYMENT_TXN_ID,userActivity.getTxnId());
			dataMap.put(Constants.PAYMENT_STATUS,userActivity.getTxnStatus());
			dataMap.put(Constants.PAYMENT_FOR,userActivity.getPaymentFor());
			dataMap.put(Constants.PAYMENT_MODE,userActivity.getPaymentMode());
			dataMap.put(Constants.AMOUNT_PAID,userActivity.getAmountPaid());
			
			userData.setUpdates(dataMap);
			
			break;

		default:
			break;
		}

	}

}
