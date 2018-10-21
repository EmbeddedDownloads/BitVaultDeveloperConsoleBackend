package org.bitvault.appstore.cloud.constant;

public final class Constants {

	/*
	 * Admin Request Actions
	 */

	public static final String APPROVED = "APPROVED";
	public static final String REJECTED = "REJECTED";
	public static final String PENDING = "PENDING";
	public static final String PUBLISHED = "PUBLISHED";
	public static final String UNPUBLISHED = "UNPUBLISHED";
	public static final String BETA_PUBLISHED = "BETA_PUBLISHED";
	public static final String ALPHA_PUBLISHED = "ALPHA_PUBLISHED";
	public static final String BETA_UNPUBLISHED = "BETA_UNPUBLISHED";
	public static final String ALPHA_UNPUBLISHED = "ALPHA_UNPUBLISHED";
	
	public static final String DRAFT = "DRAFT";
	public static final String BETA_DRAFT = "BETA_DRAFT";
	public static final String ALPHA_DRAFT = "ALPHA_DRAFT";
	public static final String ALL_APP_STATUS = "DRAFT,PUBLISHED,UNPUBLISHED,REJECTED,PENDING,APPROVED";

	public static final String SELF = "SELF";
	public static final String SUBDEV = "SUBDEV";

	public static final String ACTIVE = "ACTIVE";
	public static final String TOTAL_PAGES = "totalPages";
	public static final String TOTAL_RECORDS = "totalRecords";
	public static final String SORT = "sort";
	public static final String CREATED_AT = "createdAt";
	public static final String UPDATED_AT = "updatedAt";
	public static final String APP_NAME = "appName";

	public static final String SUCCESS = "success";
	public static final String FAILED = "failed";

	public static final String MESSAGE = "message";

	public final static String TYPE_ERROR = "error";

	public final static String REG_SUCCESS = "Email id verified successfully! A request has been submitted to Admin for your account approval.";
	public static final String SUCCESS_UPDATED = "Successfully updated";
	public static final String SUCCESS_UPLOADED = "Successfully uploaded";
	public static final String SUCCESS_DELETED = "Successfully deleted";
	public static final String UPDATED_VERSION = "UPDATED_VERSION";
	
	// Mail constants
	public static final String ACCOUNT_APPROVAL = "Dear User,<br><br/><b>Greetings from Bitvault Appstore.</b><br></br>Your account has been activated by Appstore Admin.<br></br><p style=\"color:RED\"> Please login with your credentials to start with us.<br><br/></p><b>Warm Regards,<br><br/>Bitvault Appstore Team</b>";
	public static final String ACCOUNT_ACTION = "Your Appstore account has ";
	public static final String ACCOUNT_REJECTED = "Dear User,<br></br>Sorry! Your account activation request has been disapproved by Appstore Admin.<br></br>Reason:<br></br>";
	public static final String ACCESS_TOKEN = "accessToken";
	public static final String REFRESH_TOKEN = "refreshToken";
	public static final String DEV = "Individual";
	public static final String ORG = "Organization";
	public static final String INACTIVE = "INACTIVE";
	public static final String ACCOUNT_DEACTIVATED = "Dear User,<br></br>Your account has deactivated by Appstore Admin.<br></br>Reason:<br></br>";
	public static final String MAIL_SIGN = "<b><br></br>Regards,<br></br>Bitvault Appstore Team</b>";
	public static final String APP_APPROVAL = "Dear User,<br><br/><b>Greetings from Bitvault Appstore.</b><br></br>Your application has been published by Appstore Admin.<br></br><b>Warm Regards,<br><br/>Bitvault Appstore Team</b>";
	public static final String APP_REJECTED = "Dear User,<br><br/><b>Greetings from Bitvault Appstore.</b><br></br>Your application has been unpublished by Appstore Admin.<br></br><b>Warm Regards,<br><br/>Bitvault Appstore Team</b>";
	
	// Apk parsing constant
	public static final String DEBUG_MODE = "DEBUG_MODE";
	public static final String RELEASE_MODE = "RELEASE_MODE";

	// Request param key
	public static final String PAGE = "page";
	public static final String SIZE = "size";
	public static final String ORDERBY = "orderBy";
	public static final String PACKAGENAME = "packageName";
	public static final String USERID = "userId";
	public static final String CATEGORYIDS = "categoryIdList";
	public static final String APPID = "appId";
	public static final String STATUS = "status";
	public static final String REQUEST_ACTION = "requestAction";

	public static final String APPNAME = "appName";
	public static final String REASON = "reason";
	public static final String DEFAULT_REASON = "reason unkown!";

	public static final String REQUESTID = "requestId";
	public static final String APP_VERSION_NAME = "appVersionName";

	public static final String DEACTIVATED = "Deactivated";
	public static final String DEV_COUNT = "devCount";
	public static final String FREE_APPS_COUNT = "freeAppsCount";
	public static final String PAID_APPS_COUNT = "paidAppsCount";
	public static final String ACTIVE_APPS_COUNT = "activeAppsCount";
	public static final String APPS_STATUS = "PUBLISHED,PENDING,REJECTED,UNPUBLISHED";

	public static final String PUBLISHING = "PUBLISHING";
	public static final String UNPUBLISHING = "UNPUBLISHING";
	public static final String PRE_AUTH_ADMIN = "hasRole('ADMIN')";
	public static final String PRE_AUTH_DEV = "hasRole('DEVELOPER')";
	public static final String IMAGE_URL = "imageUrl";

	public static final String CATEGORY = "category";
	public static final String CATEGORY_ID = "categoryId";
	public static final String APPLICATIONS = "application";
	public static final String CATEGORY_NAME = "categoryName";
	public static final String BANNER = "banner";
	public static final String ADMIN = "ADMIN";
	public static final String RESET_EMAIL_SENT = "A reset password link has been sent to your email Id";
	public static final String RESET_PASSWORD_EMAIL_1 = "Dear User,<br><br/>You need to <a href=\"";
	public static final String RESET_PASSWORD_EMAIL_2 = "\">click here</a> to reset your password.";
	
	public static final String FILEPATH = "raw/";
	public static final String SERVICE_ID = "serviceId";
	public static final String CATEGORY_ICON_FILE_PATH = "/category/icon";
	public static final String CATEGORY_BANNER_FILE_PATH = "/category/banner";
	public static final String TEMPBVK = "temp/";

	// Star constant
	
	public static final String STAR_5 = "star5";
	public static final String STAR_4 = "star4";
	public static final String STAR_3 = "star3";
	public static final String STAR_2 = "star2";
	public static final String STAR_1 = "star1";
	
	public static final String USD = "USD";
	public static final String PAYMENT_DESCRIPTION = "Payment done for the developer console login.";
	public static final String CATEGORY_STATUS_ALL = "ACTIVE,INACTIVE";
	public static final String AVATAR_FOLDER = "profilePic";
	
	
	public static final String PRIVATE_KEY = "private_key";
	public static final String PUBLIC_KEY = "public_key";
	
	public static final String FILE_SEPERATOR = "$$@@$$##$$";
	
	
	public static final String VERIFY_EMAIL_SUB = "Verify your email address";
	public static final String VERIFY_EMAIL_BODY_1 = "Welcome to Bitvault AppStore!<br><br/>To get started, you need to verify your email address. <a href=\"";
	public static final String VERIFY_EMAIL_BODY_2 = "\">Click here</a> to verify your email address.<br><br/>";
	public static final String VERIFY_EMAIL_1 = "An account verification email has been sent to '";
	public static final String VERIFY_EMAIL_2 = "'. Please verify your account by clicking the verification link.";
	public static final String INVALID_PAYMENT_AMOUNT = "Payment should not be negative number.";
	public static final String PAYMENT_DONE = "Payment done successfully.";
	public static final String PAY_CHECK = "payCheck";
	public static final String PAYMENT_ALREADY_DONE = "Payment has already done";
	public static final String SUCCESS_ADDED = "Record successfully added.";
	public static final String DELETE = "Record Successfully deleted";

	public static final String TXN_MAIL_1 = "<!DOCTYPE html><html><head><style>table, th, td {    border: 1px solid black;    border-collapse: collapse;}th, td {    padding: 5px;    text-align: left;}</style></head><body><table style=\"width:70%\">  <caption>Transaction Details</caption>  <tr>    <td>Id:</td><td>";
	public static final String TXN_MAIL_2 = "</td>  </tr>  <tr>    <td>Amount:</td>    <td>";
	public static final String TXN_MAIL_3 = "</td>  </tr>    <tr>    <td>Status:</td>    <td>";
	public static final String TXN_MAIL_4 = "</td>  </tr>    <tr>    <td>Date:</td>    <td>";
	public static final String TXN_MAIL_5 = "</td>  </tr></table></body></html>";
	public static final String REQUEST_SUCCESSFULLY_SUBMITTED = "Request to add more users has been successfully submitted to Admin for his approval";
	public static final String EMAIL_VERIFIED_SUCCESS = "Your email id has been successfully verified. Now you can login";
	
	public static final String NO_TRANSACTION = "No Transaction Found.";
	public static final String KEY_FILE = "Key.bitvault";
	
	public static final String USER_NAME = "userName";
	public static final String PROFILE_IMAGE = "profileImage";
	
	public static final String AVATAR_URL = "avatar_url";
	
	
	
	public static final String NEW_USER = "new_user";
	public static final String NEW_SUBDEV_USER = "new_subdev_user";
	public static final String EDIT_PROFILE = "edit_profile";
	public static final String PAYMENT = "payment";
	
	public static final String PAYMENT_STATUS = "payment_status";
	public static final String PAYMENT_MODE = "payment_mode";
	public static final String PAYMENT_FOR = "payment_for";
	public static final String PAYMENT_TXN_ID = "payment_txn_id";
	public static final String AMOUNT_PAID = "amount_paid";
	
	public static final String PAYPAL = "paypal";
	
	public static final String SUB_DEV_USERID = "sub_dev_userId";
	public static final String SUB_DEV_USERNAME = "sub_dev_userName";
	public static final String SUB_DEV_EMAIL = "sub_dev_email";
	
	public static final String PAYMENT_RECEIVED = "payment_received";
	public static final String ORDER = "order";
	public static final String GOURL = "gourl";
	
//	public enum userActivityTypeEnum{NEW_USER,NEW_SUBDEV_USER,EDIT_PROFILE,PAYMENT};
	
	public static final String WEB_SERVER_KEY = "";
	public static final String TAG = "play-store-app-update";
	public static final String UNSEEN = "unseen";
	public static final String SEEN = "seen";
	public static final String TRUE = "true";
	public static final String FALSE = "false";
	
	public static final String RECORD_SUCCESSFULLY_SAVED = "Record Successfuly Saved";
	public static final String UPLOADED_FROM = "uploaded_from";
	
	public static final String FEEDBACK = "Feedback";
	
}
