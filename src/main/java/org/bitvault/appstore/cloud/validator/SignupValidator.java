package org.bitvault.appstore.cloud.validator;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bitvault.appstore.cloud.constant.ErrorCode;
import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.constant.RoleConstant;
import org.bitvault.appstore.cloud.constant.ValidationConstants;
import org.bitvault.appstore.cloud.dto.DevUserDto;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.bitvault.appstore.cloud.model.Role;

/*
 * validate all the sign-up fields i.e. 
 * username having at-least length 6 or more; 
 * gender should either MALE or FEMALE; 
 * ROLE; 
 * Sign-up reason should be available; 
 * Email should be a valid mail ID
 * Password should contains at-least one caps, one small alphabet, one number & one special character among from !, @, #, $, %, ^, &, ?, _ and -.
*/
public class SignupValidator {
	
	private static Map<String, String> result = null;

	private static final int USER_ORG_LENGTH_MIN = 3;
	private static final int USER_ORG_LENGTH_MAX = 30;
	
	public static Map<String, String>validateUserDetails(DevUserDto user, String signupAs) {
		result = new HashMap<>();
		try {
			if(signupAs.equals(RoleConstant.ORG)){
				if(!isValidUsername(user.getOrgName()) || user.getOrgName() == null)
				result.put("orgName", USER_ORG_LENGTH_MIN + ValidationConstants.USER_ORG_NAME_LENGTH);
				if(!isValidEmailID(user.getAccEmail()) || user.getAccEmail() == null)
				result.put("orgEmail", ValidationConstants.INVALID_EMAIL_ID);	
				if(!isValidWebsite(user.getWebsite()) || user.getWebsite() == null)
				result.put("website", ValidationConstants.INVALID_WEBSITE);
			}
			
			if(!isValidSignupAs(signupAs) || signupAs == null)
			result.put("signupAs", ValidationConstants.INVALID_SIGNUP_SELECTION);
			
			if(!isValidEmailID(user.getEmail()) || user.getEmail() == null)
			result.put("emailID", ValidationConstants.INVALID_EMAIL_ID);
			//result.put("password", true);			// removed for now but later must have to used ---> isValidPassword(user.getPassword())
			if(!isValidUsername(user.getUsername()) || user.getUsername() == null)
			result.put("profileName", USER_ORG_LENGTH_MIN + ValidationConstants.USER_ORG_NAME_LENGTH);
			if(!isSignupReasonAvailable(user.getSignupReason()) || user.getSignupReason() == null)
			result.put("signupReason", ValidationConstants.NOT_EMPTY);
			return result;
//					+ "\"gender_isValid\":" + isValidGender(user.getGender()) + ","
//					+ "\"altenateEmailID_isValid\":" + isValidEmailID(user.getAltEmail()) + ","

		} catch (Exception e) {
			throw new BitVaultException(ErrorMessageConstant.UNABLE_TO_VALIDATE, ErrorCode.FIELD_IS_EMPTY_CODE);
		}
	}

	/**
	 * @param username
	 * @return boolean
	 */
	public static boolean isValidUsername(String username) {
		int length = username.trim().length();
		try {
			
			return  length >= USER_ORG_LENGTH_MIN && length <= USER_ORG_LENGTH_MAX;
		} catch (Exception e) {
			return false;
		}
	}	

	public static boolean isValidGender(String gender) {
		try {
			return (gender.trim().equalsIgnoreCase(ValidationConstants.GENDER_M) || gender.trim().equalsIgnoreCase(ValidationConstants.GENDER_F));
		} catch (Exception e) {
			return false;
		}
	}
	
	public static boolean isValidSignupAs(String signupAs) {
		try {
			return (signupAs.trim().equalsIgnoreCase(ValidationConstants.SIGNUPAS_IND) || signupAs.trim().equalsIgnoreCase(ValidationConstants.SIGNUPAS_ORG));
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean isValidRole(Role role) {
		try {
			String roleName = role.getRoleName();
			if (roleName.trim().equals(role.toString())) {
				return true;
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean isSignupReasonAvailable(String signupReason) {
		try {
			if (signupReason.trim().isEmpty())
				return false;
			else
				return true;

		} catch (Exception e) {
			return false;
		}
	}

	private static String emailPattern = ValidationConstants.EMAIL_PATTERN;

	private static Pattern pattern = null;
	private static Matcher matcher = null;

	public static boolean isValidEmailID(String emailId) {
		try {
			pattern = Pattern.compile(emailPattern);
			matcher = pattern.matcher(emailId);
			return matcher.matches();
		} catch (Exception e) {
			return false;
		}
	}

	private static String passwordRegex = ValidationConstants.PASSWORD_PATTERN;

	public static boolean isValidPassword(String newPassword) {
		try {
			pattern = Pattern.compile(passwordRegex);
			matcher = pattern.matcher(newPassword);
			return matcher.matches();
		} catch (Exception e) {
			return false;
		}
	}
	
	private static String websiteRegex = ValidationConstants.WEBSITE_PATTERN;
	
	public static boolean isValidWebsite(String website) {
		try {
			pattern = Pattern.compile(websiteRegex);
			matcher = pattern.matcher(website);
			return matcher.matches();
		} catch (Exception e) {
			return false;
		}
	}
}
