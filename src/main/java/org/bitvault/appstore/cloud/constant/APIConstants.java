package org.bitvault.appstore.cloud.constant;

public class APIConstants {
	/* Base URL for different modules */
	public final static String MOBILE_API_BASE = "/rest/api/v1/mobile";
	public final static String DEV_API_BASE = "/rest/api/v1/dev";
	public final static String ADMIN_API_BASE = "/rest/api/v1/admin";
	public final static String AUTH_BASE = "/rest/api/v1/auth";
	public final static String COMMON_API_BASE = "/rest/api/v1/common";

	public final static String SERVER_URL = "cloud";

	/*
	 * request mapping
	 */
	public final static String LIST_OF_ALL_APPLICATION = "/listOfAllApplication"; // developer to get the list of all
																					// applications
	public final static String APPLICATION_BY_PACKAGENAME = "/applicationByPackageName"; // developer to get app by
																							// package name
	public final static String FIND_APPLICATION_BY_USERID = "/applicationByUserId"; // used by admin and developer to
																					// get list of application for a
																					// particular user by user ID
	public final static String FIND_APPLICATION_BY_CATEGORY = "/applicationByCategory"; // return the list of
																						// applications by their
																						// category
	public final static String FIND_APPLICATION_BY_STATUS = "/applicationByStatus"; // fetch list of applications by
																					// their status
	public final static String PUBLSHED_APPLICATION_BY_PACKAGENAME = "/publishedApplicationByPackageName"; // for
																											// mobile**
																											// only
	public final static String LIST_OF_ALL_PUBLSHED_APPLICATION = "/listOfAllPublishedApplication"; // for mobile** only
	public final static String FIND_PUBLISHED_APPLICATION_BY_USERID = "/publishedApplicationByUserId"; // for mobile**
																										// only
	public final static String FIND_PUBLISHED_APPLICATION_BY_CATEGORY = "/publishedApplicationByCategory"; // for
																											// mobile**
																											// only
	public final static String SEARCH_PUBLISHED_APPLICATION = "/searchPublishedApp"; // for mobile** only
	public final static String FIND_APPDETAIL_BY_APPID = "/appDetailByAppId"; // get application details by its ID
	public final static String FIND_ALL_APP_CATEGORY = "/appCategoryList"; // to get list for all categories
	public final static String FIND_APP_CATEGORY_BANNER = "/appCategoryBanner"; // to get list for all categories
	public final static String FIND_APP_BY_CATEGORY = "/categoryApp"; // to get list for all app by category map.
	public final static String SAVECATEGORY = "/saveCategory";
	public final static String CATEGORY_STATUS = "/upddateCategoryStatus";
	public final static String CATEGORY_BY_APPTYPE = "/categoryByappType";
	public final static String APPLICATION_TYPE = "/applicationType";

	public final static String CHANGE_APP_STATUS = "/updateAppStatus"; // used to publish/un-publish/rejected/approved
																		// an application
	public final static String SEARCH_APPLICATION = "/searchAppByTitle"; // used to get the application by its title
	public final static String UPLOAD_APK = "/uploadApk"; // to upload an APK file
	public final static String UPLOAD_APK_ICON = "/uploadApkIcon"; // to upload an APK icon image file
	public final static String UPLOAD_APK_IMAGES = "/uploadApImages"; // to upload an APK screenshot image file
	public final static String FIND_APP_BY_APPID = "/findAppByAppId"; // get application by its ID

	public final static String SAVE_APP_INFO = "/saveAppInfo"; // get application by its ID
	public final static String DELETE_APP_IMAGE = "/deleteAppImage"; // delete appImageById
	public final static String APPCATEGORY_BY_CATEGORYID = "/categoryById"; // category by id
	public final static String CATEGORY_DETAIL = "/categoryDetail"; // categoryDetail
	public final static String APP_STATS = "/appStats"; // statistics
	public final static String APP_AVERAGE_STATS = "/appAverageStats"; // statistics

	public final static String RELEASE_MANGEMENT = "/release/mgmt"; // release management

	public final static String SEARCH__PUBLISHED_APPLICATION = "/searchPublishedApp"; // used for mobile** only

	public final static String REGISTER_MOBILE = "/registerMobile";
	public final static String GET_MOBILE_USER_BY_PUBLICADD = "/getMobileUser";
	public final static String FIND_ALL_MOBILE_USER = "/findAllMobileUser";
	public final static String GET_APK_URL = "/getBitVaultApKUrl";
	public final static String DOWNLOAD_APK = "/downloadAPK";
	public final static String SAVE_RATE_REVIEW = "/saveRateReview";
	public final static String GET_STAR_RATING = "/getStarRating";
	public final static String GET_APPS_LATEST_VERSION = "/getAppsLatestVersion";
	public final static String GET_APPS_PERMISION = "/appPermisions";

	public final static String GET_BANNER_APPLICATION = "/getBannerApplication";
	public final static String RATE_REVIEW_LIST = "/rateReviewList";
	public final static String UPDATE_INTSALL_UNISTALL_DOWNLOAD = "/apkOnDeviceAction";
	public final static String APP_AUDIT_TRAIL = "/app/auditTrail";
	public static final String ELASTIC_SEARCH_APP = "/search/app";;
	public static final String UNINSTALL_MULTI_APK = "/uninstallMultiApk";
	/*
	 * DevUserController api's
	 */
	public final static String FIND_BY_USER_ID = "/user/{userId}"; // used to get the user from
	public final static String SAVE_DEV = "/user"; // used to register individual as well as Org.
	public final static String UPDATE_USER_INFO = "/user/update"; // update user by it user-id
	public final static String UPDATE_PASSWORD = "/user/updatePassword"; // update password during logged in
	public final static String LOGOUT = "/logout"; // in progress ---> don't implement
	public static final String LIST_OF_USERS = "/listofAllUsers";
	public static final String ELASTIC_SEARCH_DEV_USER = "/search/devuser";
	public static final String LOGIN = "/login"; // used to login with valid credentials

	/*
	 * Admin API's
	 */
	public final static String ACCOUNT_REQUEST = "/user/accountRequest"; // used to get list of pending and rejected
																			// accounts
	public final static String ACTION_ON_ACC_REQUEST = "/user/accreq/action/{reqId}"; // used to approve and reject the
																						// account request
	public static final String GET_REQUEST_BY_ID = "/user/request/{id}"; // used to get the details of an particular
																			// account request by its ID
	public static final String ACTION_ON_DEV_ACCOUNT = "/user/acc/update/{userId}";
	public static final String GET_COUNTS = "/getCount";

	public final static String GET_ACCESS_TOKEN = "/token"; // used by developer and admin to get a new accessToken by
															// using the issued refresh token
	public static final String UPLOAD_AVATAR = "/upload";
	public static final String AVATAR_BASE = "/rest/api/v1/avatar";
	public static final String AVATAR_IMAGE = "/getAvatar/{filename:.+}";
	public static final String AVATAR_DELETE = "/remove";
	public static final String UPDATE_APP_STATUS_REQUEST = "/updateAppStatusRequest";

	public static final String GET_REQUEST_BY_ID_AND_STATUS = "getRequestByStatusId"; // used to get the details of an
																						// particular request by its
																						// requestTypeId And status

	public static final String GET_APP_REQUESTS = "/listOfAllAppRequests";
	public static final String GET_APP_DETAIL_BY_REQUEST = "/getAppDetailByRequest";

	public static final String SAVE_APP_ADDON_SERVICE = "/addon/service/opt";
	public static final String GET_ALL_ADDONS_FOR_AN_APP = "/addon/service/all/{packageName}/";
	public static final String GET_ADDONS_FOR_ALL_USER_APP = "/addon/service/user";
	public static final String GET_ADDONS_FOR_ALL_USER_APP_ADMIN = "/addon/service/user/{userId}";
	public static final String SAVE_ADDON_SERVICE = "/addon/service";
	public static final String GET_ALL_ADDON_SERVICE = "/addon/service/all";
	public static final String UPDATE_APP_STATUS_BY_ADMIN = "appStatusAdmin";

	public static final String FORGET_PASSWORD = "/forgetPassword";
	public static final String RESET_PASSWORD = "/resetPassword/{id}";
	public static final String GET_COUNTRIES = "/country";
	public static final String GET_COUNTRY_STATES = "/country/{id}/states";

	public final static String PAYPAL = "/pay";
	public final static String PAYPAL_SUCCESS_URL = "/pay/success";
	public final static String PAYPAL_CANCEL_URL = "/pay/cancel";
	public static final String GET_USER_INFO = "/user/";
	public static final String UPDATE_ADDON_SERVICE = "/addon/service/update/{id}";
	public static final String GET_ADDON_SERVICE = "/addon/service/{id}";
	public static final String VERIFY_EMAIL = "/verify/{userId}";
	public static final String UPDATE_CATEGORY = "/category";
	public static final String UPDATE_DEV_PAYMENT = "/payment";
	public static final String MEDIAVAULT_BASE = "/mediavault";
	public static final String MEDIAVAULT_GET_FILEINFO = "/mediavault/{appId}/{hashId}";
	public static final String MEDIAVAULT_GET_LIST_FILEINFO = "/mediavault/{appId}/";
	public static final String PAYPAL_DETAILS = "/pay/{txnId}";
	public static final String ADD_USER = "/add/user";
	public static final String GET_SUB_DEV_LIST = "/all/subuser";
	public static final String REQUEST_MORE_USER = "/add/subuser/{count}";
	public static final String SUB_DEV_REQUEST = "/subuser/request";
	public static final String REPLY_ON_REVIEW = "/reviewOnReply";
	public static final String DELETE_REVIEW = "/deleteReview";
	public static final String FIND_PUBLISHED_APPLICATION_BY_APPTYPE = "/app/appType";
	public static final String GET_SUB_DEV = "/subuser/{userId}";
	public static final String APP_ACTIVE_STATS = "/appActiveStats";

	public static final String GET_TRANSACTION_LIST = "/getTxn";

	public static final String GET_REQUESTS_ACTIVITY_DETAIL = "/getAllRequestsActivities";
	public static final String GET_USER_APP_REQUEST_DETAIL = "/getUserAppRequestActDetail";
	public static final String GET_USER_All_REQUEST_DETAIL = "/getUserAllRequestActDetail";
	public static final String GET_ADMIN_TRANSACTION_LIST = "/getAdminTxn";

	public static final String GET_KEY_FILE = "/downloadKeyFile";
	public static final String GET_USER_APP_REQUESTS = "/app/{userId}/{appApplicationId}";
	public static final String GET_USER_REQUESTS = "/acc/{userId}";

	public static final String GET_ALL_USER_REQUEST = "/getAllUserRequest";

	public static final String GET_ALL_USER_REQUEST_BY_ADMIN = "/getUserRequestByAdmin";

	public static final String GOURL_PAY = "/payGoUrl";
	public static final String GET_ALL_REQUEST = "/getAllRequest";
	public static final String ELASTIC_SEARCH_USER = "/search/user";
	public static final String GET_ALL_ALERTS = "/getAllAlerts";
	public static final String GET_ALL_ALERTS_BY_USERID = "/getAllAdminAlerts";
	public static final String SEND_NOTIFICATION_URL = "http://34.209.234.181/v1/play-store-app-update";
	public static final String FIND_NATIVE_APPLICATION = "/nativeapp";
	public static final String GET_ALL_SUBDEV_REQUEST = "/getAllSubRequest";
	public static final String READ_ALERTS = "/readAlerts";
	public static final String HELPSUPPORT = "/helpSupport";
	public static final String LIST_OF_HELP_SUPPORT ="/listOfHelpSupport";
	public static final String APPTESTER = "/appTesting";
	public static final String TESTLIST ="/appTestList";
	
	public static final String GET_PAYMENT_ADDRESS ="/getPaymentAddress/{publicAddress}";
	
	
}
