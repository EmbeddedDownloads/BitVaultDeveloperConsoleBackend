package org.bitvault.appstore.cloud.constant;

public class ValidationConstants {
	/* Regex Pattern */
	//public static final String EMAIL_PATTERN = "^([a-zA-z])+?[\\w]*@[a-zA-z]+?\\.[a-z]+?$";
	
	public static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	
//	public final static String PACKAGE_NAME_PATTERN = "^([A-Za-z]{1}[A-Za-z\\d_]*\\.)*[A-Za-z][A-Za-z\\d_]*$";

	public final static String PACKAGE_NAME_PATTERN = "^[a-z][a-z0-9_]+(\\.[a-z][a-z0-9_]+)+$";

	public final static String PASSWORD_PATTERN = "^(?=[A-za-z])(?=.*\\d)(?=.*[!@#$%^&?_-]).{6,12}$";
	public final static String WEBSITE_PATTERN = "^(http://|https://)?(www.)?([a-zA-Z0-9]+).[a-zA-Z0-9]*.[a-z]{2,3}.?([a-z]+)?$";
	public final static String URL_PATTERN = "^http(s{0,1})://[a-zA-Z0-9_/\\-\\.]+\\.([A-Za-z/]{2,5})[a-zA-Z0-9_/\\&\\?\\=\\-\\.\\~\\%]*";

	public static final String USER_ORG_NAME_LENGTH = " characters atleast it should contains.";
	public static final String INVALID_WEBSITE = "Entered website should be in this format 'http(s)://abc.com or http(s)://www.abc.com'";
	public static final String INVALID_EMAIL_ID = "Invalid email.";
	public static final String NOT_EMPTY = "Should not be empty.";
	
	public static final String GENDER_M = "MALE";
	public static final String GENDER_F = "FEMALE";

	public static final String SIGNUPAS_IND = "INDIVIDUAL";
	public static final String SIGNUPAS_ORG = "ORGANIZATION";

	public static final String INVALID_SIGNUP_SELECTION = "Should be selected as either individual or organization.";

	public static final String REQUIRED_PARAMETER_MISSING = "Required parameter is missing or invalid";
	public static final String REQUIRED_PARAMETER_INTEGER = "Required parameter should be a valid number i.e. Integer";
}
