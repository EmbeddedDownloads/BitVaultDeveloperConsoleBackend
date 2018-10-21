package org.bitvault.appstore.cloud.user.dev.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import org.apache.commons.lang3.RandomStringUtils;
import org.bitvault.appstore.cloud.config.PropertiesConfig;
import org.bitvault.appstore.cloud.constant.Constants;
import org.bitvault.appstore.cloud.constant.DbConstant;
import org.bitvault.appstore.cloud.constant.ErrorCode;
import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.constant.RoleConstant;
import org.bitvault.appstore.cloud.dto.DevUserDto;
import org.bitvault.appstore.cloud.dto.DevUserDtoMapper;
import org.bitvault.appstore.cloud.dto.UserActivityDto;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.bitvault.appstore.cloud.mail.MailService;
import org.bitvault.appstore.cloud.model.Account;
import org.bitvault.appstore.cloud.model.DevPayment;
import org.bitvault.appstore.cloud.model.DevUser;
import org.bitvault.appstore.cloud.model.UserActivity;
import org.bitvault.appstore.cloud.model.UserActivityType;
import org.bitvault.appstore.cloud.user.common.service.DevPaymentService;
import org.bitvault.appstore.cloud.user.dev.dao.DevUserRepository;
import org.bitvault.appstore.cloud.user.dev.dao.UserActivityTypeRepository;
import org.bitvault.appstore.cloud.utils.Response;
import org.bitvault.appstore.commons.application.elasticdao.DevUserElasticRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service("devUserService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class DevUserServiceImpl implements DevUserService {

	public static final Logger logger = LoggerFactory.getLogger(DevUserServiceImpl.class);

	@Autowired
	private DevUserRepository devUserRepository;

	@Autowired
	private MailService mailService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private DevPaymentService devPaymentService;

	@Autowired
	private PropertiesConfig propertiesConfig;

	@Autowired
	DevUserElasticRepository devUserElasticRepository;

	@Autowired
	private UserActivityTypeRepository userActvityTypeRepository;

	@Autowired
	private UserActivityService userActivityService;

	/*
	 * @param (user)
	 * 
	 * @see
	 * org.bitvault.appstore.cloud.user.dev.service.DevUserService#saveUser(org.
	 * bitvault.appstore.cloud.model.DevUser)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public DevUser saveUser(DevUser user) {
		DevUser devUser = null;
		try {
			String verificationLink = propertiesConfig.getHost();
			if (user.getUserId() == null) {
				user.setPassword(passwordEncoder.encode(user.getPassword()));
				devUser = devUserRepository.save(user);
				String createdBy = user.getUserId();
				user.setParentId(createdBy);
				user.setCreatedBy(createdBy);
				Account account = user.getAccount();
				account.setCreatedBy(createdBy);
				// verificationLink = generateVerificationLink(verificationLink, createdBy,
				// user.getEmail());
				user.setVerificationLink("");
				// Request accRequest = new Request();
				// accRequest.setUser_id(user.getUserId());
				// RequestType requestType = new RequestType();
				// requestType.setRequestTypeId(1);
				// accRequest.setRequestType(requestType);
				// accRequest.setStatus(Constants.PENDING);
				// requestService.persistRequest(accRequest);
				logger.info("An email has triggered for user_id: " + user.getUserId());
			} else {
				devUser = devUserRepository.save(user);
			}
			logger.info("All user information has been succesfully set.");
			logger.info("User successfully persist into DB.");
		} catch (Exception e) {
			logger.error("Error occured: " + e.getMessage());
			throw new BitVaultException(ErrorMessageConstant.UNABLE_TO_SAVE, HttpStatus.OK.toString());
		}

		return devUser;
	}

	@Override
	public String generateVerificationLink(String verificationLink, String createdBy, String email) {
		verificationLink = verificationLink + "#/verify/email?cid=" + RandomStringUtils.randomAlphanumeric(30) + "&iam="
				+ createdBy + "&value=" + RandomStringUtils.randomAlphanumeric(22);
		try {
			mailService.sendMail(email, Constants.VERIFY_EMAIL_SUB, Constants.VERIFY_EMAIL_BODY_1 + verificationLink
					+ Constants.VERIFY_EMAIL_BODY_2 + Constants.MAIL_SIGN);
		} catch (MessagingException e) {
			e.printStackTrace();
			logger.error("unable to send mail... " + e.getMessage());
		}
		return verificationLink;
	}

	@Override
	public DevUserDto findByEmail(String email) {
		try {
			DevUser user = devUserRepository.findByEmail(email);
			if (user == null) {
				throw new BitVaultException(ErrorMessageConstant.USER_NOT_FOUND);
			}
			return fetchUserByEmail(user);
		} catch (Exception e) {
			throw new BitVaultException(ErrorMessageConstant.USER_NOT_FOUND);
		}
	}

	@Override
	public DevUser findByUserId(String userId) {
		DevUser user = devUserRepository.findByUserId(userId);
		if (user == null) {
			throw new BitVaultException(ErrorMessageConstant.USER_NOT_FOUND);
		}
		return user;
	}

	@Override
	public DevUserDto findUserDtoByUserId(String userId) {
		try {
			return DevUserDtoMapper.toDevUserDto(findByUserId(userId));
		} catch (Exception e) {
			throw new BitVaultException(ErrorMessageConstant.UNABLE_TO_VALIDATE);
		}
	}

	@Override
	public void updateUser(DevUser currentUser, DevUserDto userDto) {
		try {
			currentUser.setUsername(userDto.getUsername());
			devUserRepository.saveAndFlush(currentUser);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.PROFILENAME_SIZE_REACHED);
		}
	}

	@Override
	public void deleteUserById(Long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isUserExist(DevUserDto userDto) {
		try {
			DevUser user = devUserRepository.findByEmail(userDto.getEmail());
			if (user == null) {
				return false;
			}
			return true;
		} catch (Exception e) {
			throw new BitVaultException(ErrorMessageConstant.UNABLE_TO_VALIDATE);
		}
	}

	@Override
	public void changePassword(DevUser currentUser) {
		devUserRepository.saveAndFlush(currentUser);
	}

	@Override
	public DevUser findByEmailId(String email) {
		try {
			DevUser user = devUserRepository.findByEmail(email);
			if (user == null) {
				return null;
			}
			return user;
		} catch (Exception e) {
			throw new BitVaultException(ErrorMessageConstant.USER_NOT_FOUND);
		}
	}

	@Override
	public Response findAllUsers(int page, int size, String orderBy, List<String> status, String direction) {
		try {
			Page<DevUser> allUsers = null;
			if (direction.equals(DbConstant.DESC)) {
				allUsers = devUserRepository.findAll(new PageRequest(page, size, new Sort(Direction.DESC, orderBy)),
						status);
			} else {
				allUsers = devUserRepository.findAll(new PageRequest(page, size, new Sort(Direction.ASC, orderBy)),
						status);
			}
			List<DevUserDto> userDtoList = getUserDtoList(allUsers, 0);
			return new Response(Constants.SUCCESS, userDtoList, allUsers.getTotalPages(), allUsers.getTotalElements(),
					allUsers.getSize(), orderBy);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(e.getMessage());
		}
	}

	private DevUserDto fetchUserByEmail(DevUser user) {
		try {
			DevUserDto userDto = DevUserDtoMapper.toDevUserDto(user);
			userDto.setPassword("");
			String userRole = user.getRole().getRoleName().substring(5);
			if (userRole.contains(RoleConstant.ORG)) {
				Account account = user.getAccount();
				BeanUtils.copyProperties(account, userDto);
			}
			userDto.setRole(userRole);
			return userDto;
		} catch (BeansException e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.USER_NOT_FOUND);
		}
	}

	private List<DevUserDto> getUserDtoList(Page<DevUser> allUsers, int flag) {
		List<DevUserDto> userDtoList = new ArrayList<DevUserDto>();
		for (DevUser user : allUsers) {
			DevUserDto userDto = fetchUserByEmail(user);
			if (flag == 0) {
				List<DevPayment> devPaymentList = devPaymentService.findByUserIdAndPaymentFor(userDto.getUserId(),
						Constants.SELF, new PageRequest(0, 1, new Sort(Direction.DESC, Constants.CREATED_AT)))
						.getContent();
				if (!devPaymentList.isEmpty()) {
					DevPayment devPayment = devPaymentList.get(0);
					userDto.setPayment(devPayment.getPayment());
					userDto.setTxnStatus(devPayment.getTxnStatus());
				}
				if (!user.getParentId().equalsIgnoreCase(user.getUserId())) {
					userDto.setSignupAs(RoleConstant.ORG_DEV);
				}
			} else
				userDto.setSignupAs(RoleConstant.ORG_DEV);
			userDtoList.add(userDto);
		}
		return userDtoList;
	}

	@Override
	public int getUserCount(List<String> userStatus) {
		return devUserRepository.getUserCount(userStatus);
	}

	@Override
	public Response getSubDevList(String createdBy, String userId, int page, int size, String orderBy,
			List<String> status, String direction) {
		Page<DevUser> allUsers = null;
		if (direction.equals(DbConstant.DESC)) {
			allUsers = devUserRepository.findByCreatedByAndUserIdNotIn(createdBy, userId,
					new PageRequest(page, size, new Sort(Direction.DESC, orderBy)));
		} else {
			allUsers = devUserRepository.findByCreatedByAndUserIdNotIn(createdBy, userId,
					new PageRequest(page, size, new Sort(Direction.ASC, orderBy)));
		}
		List<DevUserDto> userDtoList = getUserDtoList(allUsers, 1);
		return new Response(Constants.SUCCESS, userDtoList, allUsers.getTotalPages(), allUsers.getTotalElements(),
				allUsers.getSize(), orderBy);
	}

	@Override
	public Map<String, Object> searchUserByUserName(String username, String userId, int page, int size,
			String direction, String property) throws BitVaultException {
		Page<DevUser> userList = null;
		org.bitvault.appstore.cloud.dto.DevUserElasticDto userDTO = null;
		List<org.bitvault.appstore.cloud.dto.DevUserElasticDto> userDTOList = new ArrayList<org.bitvault.appstore.cloud.dto.DevUserElasticDto>();
		Map<String, Object> allAppMap = null;

		try {
			if (direction.equals(DbConstant.ASC)) {
				userList = devUserRepository.findByUserNameOrEmailId(username, userId,
						new PageRequest(page, size, Sort.Direction.ASC, property));
			} else {
				userList = devUserRepository.findByUserNameOrEmailId(username, userId,
						new PageRequest(page, size, Sort.Direction.DESC, property));

			}
			logger.info("UserList fetched succesfully");
			for (DevUser devuser : userList) {
				userDTO = devuser.populateDevUserElasticDTO(devuser);
				userDTOList.add(userDTO);
			}
			allAppMap = new HashMap<String, Object>();
			allAppMap.put("userList", userDTOList);
			allAppMap.put(Constants.TOTAL_PAGES, userList.getTotalPages());
			allAppMap.put(Constants.TOTAL_RECORDS, userList.getTotalElements());
			allAppMap.put(Constants.SORT, userList.getSort());
			allAppMap.put(Constants.SIZE, userList.getNumberOfElements());

		} catch (Exception e) {
			logger.info("Error occured during searching");
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED, ErrorCode.SOME_ERROR_OCCURED_CODE);
		}

		return allAppMap;

	}

	@Override
	public void saveUserActivity(UserActivityType userActivityType, Map<String, String> dataMap) {
		try {

			UserActivity userActivity = new UserActivity();

			switch (userActivityType.getActivityType()) {

			case Constants.NEW_USER:

				userActivity.setUserName(dataMap.get(Constants.USER_NAME));
				userActivity.setUserId(dataMap.get(Constants.USERID));

				userActivity.setUserActivityType(userActvityTypeRepository.getOne(2));

				UserActivityDto userActivityDto = userActivity.populateUserActivityDto(userActivity);

				userActivityService.saveUserHistory(userActivityDto);

				break;

			case Constants.NEW_SUBDEV_USER:

				userActivity.setUserName(dataMap.get(Constants.USER_NAME));
				userActivity.setUserId(dataMap.get(Constants.USERID));

				userActivity.setSubDevUserId(dataMap.get(Constants.SUB_DEV_USERID));
				userActivity.setSubDevUserName(dataMap.get(Constants.SUB_DEV_USERNAME));
				userActivity.setSubDevEmail(dataMap.get(Constants.SUB_DEV_EMAIL));

				userActivity.setUserActivityType(userActvityTypeRepository.getOne(3));

				userActivityService.saveUserHistory(userActivity.populateUserActivityDto(userActivity));
				break;

			case Constants.EDIT_PROFILE:

				userActivity.setUserName(dataMap.get(Constants.USER_NAME));
				userActivity.setUserId(dataMap.get(Constants.USERID));

				if (dataMap.get(Constants.AVATAR_URL) != null) {
					userActivity.setAvatarURL(dataMap.get(Constants.AVATAR_URL));
				}

				userActivity.setUserActivityType(userActvityTypeRepository.getOne(1));

				userActivityService.saveUserHistory(userActivity.populateUserActivityDto(userActivity));

				break;
			case Constants.PAYMENT:

				userActivity.setUserName(dataMap.get(Constants.USER_NAME));
				userActivity.setUserId(dataMap.get(Constants.USERID));
				userActivity.setAmountPaid(dataMap.get(Constants.AMOUNT_PAID));
				userActivity.setTxnStatus(dataMap.get(Constants.PAYMENT_STATUS));
				userActivity.setTxnId(dataMap.get(Constants.PAYMENT_TXN_ID));
				userActivity.setPaymentMode(dataMap.get(Constants.PAYMENT_MODE));
				userActivity.setPaymentFor(dataMap.get(Constants.PAYMENT_FOR));

				// if(dataMap.get(Constants.AVATAR_URL) != null) {
				// userActivity.setAvatarURL(dataMap.get(Constants.AVATAR_URL));
				// }

				userActivity.setUserActivityType(userActvityTypeRepository.getOne(4));

				userActivityService.saveUserHistory(userActivity.populateUserActivityDto(userActivity));

				break;

			}
		} catch (Exception e) {
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);
		}

	}

	@Override
	public void saveUserActivityForNewUser(UserActivityType userActivityType, String userId, String userName) {

		try {

			UserActivity userActivity = new UserActivity();

			switch (userActivityType.getActivityType()) {

			case Constants.NEW_USER:

				userActivity.setUserName(userName);
				userActivity.setUserId(userId);

				userActivity.setUserActivityType(userActvityTypeRepository.getOne(2));

				UserActivityDto userActivityDto = userActivity.populateUserActivityDto(userActivity);

				userActivityService.saveUserHistory(userActivityDto);

				break;

			case Constants.NEW_SUBDEV_USER:

				userActivity.setUserName(userName);
				userActivity.setUserId(userId);

				userActivity.setUserActivityType(userActvityTypeRepository.getOne(3));

				userActivityService.saveUserHistory(userActivity.populateUserActivityDto(userActivity));
				break;

			case Constants.EDIT_PROFILE:

				userActivity.setUserName(userName);
				userActivity.setUserId(userId);

				userActivity.setUserActivityType(userActvityTypeRepository.getOne(3));

				userActivityService.saveUserHistory(userActivity.populateUserActivityDto(userActivity));

				break;
			case Constants.PAYMENT:
				break;

			}
		} catch (Exception e) {
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);
		}
	}
}
