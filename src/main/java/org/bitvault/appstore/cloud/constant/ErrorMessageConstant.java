package org.bitvault.appstore.cloud.constant;

public class ErrorMessageConstant {

	public static final String USER_NOT_FOUND = "Unable to find your account. Please Sign up.";
	public static final String RESULT_NOT_FOUND = "No record found.";
	public static final String UNABLE_TO_SAVE = "Unable to save.";
	public static final String UNABLE_TO_DELETE = "Unable to delete.";
	public static final String UNABLE_TO_GET_METADATA = "Unable to get metadata.";
	public static final String UNABLE_TO_UPLOAD_FILE = "Unable to upload file.";
	public static final String UNABLE_TO_PARSE_FILE = "Unable to parse. Please check uploaded file.";
	public static final String UNABLE_TO_GET_FILE = "Unable to find the file.";
	public static final String APP_ICON_SIZE_ERROR = "Dimensions must be 512px by 512px and maximum allowed file size is 1024KB.";
	public static final String APP_ICON_EXE_ERROR = "Application icon must be a PNG file.";
	public static final String APP_BANNER_SIZE_ERROR = "Dimensions must be 1024px by 500px.";
	public static final String APP_IMAGE_SIZE_ERROR = "Invalid dimensions: \n"
			+ "a) Must be between 320px and 3840px. \n"
			+ "b) Maximum dimension < 2*(Minimum dimension)";
	public static final String APK_DRAFT_ERROR = "Application in draft must have same version and version code.";

	public static final String SOME_ERROR_OCCURED = "Some error occured. Please try again later.";
	public static final String UNABLE_TO_SET_PERMISSION = "Unable to get permission. Please try again later.";

	public static final String FIELD_IS_EMPTY = "Required field(s) missing.";
	public static final String IS_EMPTY = " is empty or null.";
	public static final String PASSWORD_CANT_EMPTY = "Password can't be empty.";

	public static final String UNABLE_TO_UPDATE = "Unable to update.";
	public static final String FILE_IS_EMPTY = "File is empty.";
	public static final String FILE_IS_INVALID = "Invalid file";
	public static final String INVALID_PUBLIC_ADDRESS = "Invalid public address.";
	public static final String INVALID_DATE_FORMAT = "Invalid date.";

	public static final String INVALID_REQUEST = "Invalid request.";
	public static final String INVALID_APP_TYPE = "Invalid app type.";
	public static final String INVALID_CATEGORY_TYPE = "Invalid category type.";

	public static final String APP_ALREADY_UNPUBLISHED = "Application is already in UNPUBLISHED state.";
	public static final String REQUEST_ACTION_PENDING = "Already a request for this application is pending."; //

	public static final String USER_UPDATE_FAILED = "Unable to update. No user found.";
	public static final String PASSWORD_SYNTAX_ERROR = "Password length should be more than 6 characters and should contain at least one capital, small alphabet, one number and a special character from !, @, #, $, %, ^, &, ?, _ and -";
	public static final String PASSWORD_MISMATCH = "New and Confirm New Password should be same.";
	public static final String OLD_PASSWORD_MISMATCH = "Incorrect old password.";
	public static final String OLD_NEW_PASSWORD_ARE_SAME = "Old and New password should not be identical.";
	public static final String UNABLE_TO_VALIDATE = "Unable to validate."; //

	public static final String ACCOUNT_NOT_APPROVED = "ADMIN has not approved your account yet.";

	public static final String ACCOUNT_EXPIRED_REJECTED = "Your account has been deactivated or rejected by ADMIN.";

	public static final String BAD_CREDENTIALS = "Authentication Failed. Invalid Email Id or Password.";
	public static final String USER_ALREADY_EXISTS = "Email Id unavailable.";

	public static final String SUCCESS = "success";
	public static final String FAILED = "failed";

	public static final String REFRESH_TOKEN_MISUSE = "Type: REFRESH_TOKEN, not valid to make basic requests.";
	public static final String TOKEN_EXPIRED = "Token has expired.";

	public static final String AUTHENTICATION_FAILED = "Authentication failed.";
	public static final String INVALID_EMAIL = "Email Id is invalid or empty."; //
	public static final String NO_REQUEST_FOUND = "No record found. Invalid request Id: ";
	public static final String NO_PROPER_ACTION_TAKEN = "Invalid action on a request.";
	public static final String INVALID_TOKEN = "Invalid JWT token.";

	public static final String PACKAGE_ALREADY_EXIST = "Package already exists.";
	public static final String INVALID_FILE_TYPE = "Invalid image file type. Allowed: JPG/JPEG/PNG only.";
	public static final String INVALID_BANNER_IMAGE_TYPE = "Invalid image file type. Allowed: JPEG/PNG only.";

	public static final String NO_FILE_SELECTED = "Please select an image file."; //
	public static final String NO_BANNER_SELECTED = "Please select an banner image file."; //
	public static final String MAX_SIZE_REACHED = "Image file size should be between 50 Kb-1 MB.";
	public static final String UNABLE_TO_UPLOAD = "Unable to upload the avatar.";
	public static final String INVALID_CATEGORYID = "Invalid category.";
	public static final String INVALID_APPTYPEID = "Invalid App type.";
	public static final String INVALID_CATEGORY_EXIST = "This category name is already exists.";
	public static final String CATEGORY_BANNER_MISSING = "Category Banner Missing.";

	public static final String INVALID_APPSTATUS = "Invalid application status.";
	public static final String FULL_DESCRIPTION_ERROR = "Full description can't be empty.";
	public static final String SHORT_DESCRIPTION_ERROR = "Short description can't be empty.";
	public static final String FULL_DESCRIPTION_SIZE_ERROR = "Full description size should be between 200-4000 characters.";
	public static final String SHORT_DESCRIPTION_SIZE_ERROR = "Short description size should be between 10-80 characters.";

	public static final String APPLICATION_ID_ERROR = "App id can't be null or empty.";
	public static final String PAYMENT_NOT_DONE = "Payment has not been done yet";

	public static final String LANGUAGE_ERROR = "Language can't be empty.";
	public static final String TITLE_ERROR = "Title can't be empty.";
	public static final String TITLE_SIZE_ERROR = "Title should be in between 3-30 characters.";

	public static final String WEBSITE_ERROR = "Website can't be empty.";
	public static final String WEBSITE_URL_VALIDATION_ERROR = "Entered website should be in this format: 'http(s)://abc.com or http(s)://www.abc.com'.";

	public static final String PRIVACY_POLICY_URL_ERROR = "Privacy Policy URL can't be empty.";
	public static final String PRIVACY_POLICY_URL_VALIDATION_ERROR = "Privacy Policy URL should be in this format: 'http(s)://abc.com, http(s)://www.abc.com' or http(s)://www.abc.com/xyz.html/php/etc.";

	public static final String WHATS_NEW_ERROR = "What's New can't be empty.";
	public static final String APK_URL_ERROR = "Please upload apk.";
	public static final String APP_ICON_ERROR = "Please upload App Icon.";
	public static final String APP_IMAGES_ERROR = "Please upload minimum 3 screenshots of your application.";
	public static final String APP_BANNER_ERROR = "Please upload minimum 1 banner of your application.";
	
	public static final String APP_IMAGES_MAX_ERROR = "You can only upload maximum 8 images.";
	public static final String APP_BANNER_MAx_ERROR = "You can only upload maximum 1 banner.";
	public static final String APP_IMAGES_MAX_DELETE_ERROR = "Minimum 3 images required. Please upload image.";

	
	public static final String APP_NOT_FOUND = "Application not found, invalid package name.";
	public static final String PACKAGE_NAME_NOT_EMPTY = "Package name can't be empty.";
	public static final String PACKAGE_NAME_INVALID = "Invalid package name.";
	public static final String INVALID_SERVICE = "No Service found.";
	public static final String SERVICE_ALREADY_REGISTERED = "Requested package has already registered for this service.";
	public static final String PACKAGE_NAME_UNAVAILABLE = "Package name is unavailable, use something else.";
	public static final String SERVICE_ALREADY_EXISTS = "Service with this name is already existing.";
	public static final String REQUIRED_FIELD_MISSING = "Required field(s) is missing i.e. Email Id or Type.";
	public static final String REQUIRED_FIELD_PASSWORD = "Required field(s) is missing i.e New password or Confirm New Password.";
	public static final String USE_LATEST_LINK = "Invalid link. Use the latest link sent on your email to reset your password.";
	public static final String LINK_EXPIRED = "Link has expired.";
	public static final String INVALID_APP_OWNER = "Application Owner violation.";
	public static final String INVALID_APP_USER = "Application with this package name already exists with different account.";
	public static final String INVALID_APP_UPDATE = "Application already exists. Please update existing one.";
	public static final String PACKAGENAME_MISSMATCH = "Package name mismatch. Please upload this separately";

	public static final String INVALID_APP_UPDATE_VERSION = "Package name does not match with current version.";

	public static final String INVALID_COUNTRY_CODE = "Invalid Country Code.";
	public static final String UNABLE_TO_FETCH = "Unable to fetch the record.";
	
	public static final String INVALID_APP_TYPE_ID = "Invalid App Type.";
	public static final String NOT_ELIGBLE_FOR_PAYMENT = "Not eligible for Payment.";
	public static final String AMOUNT_CANNOT_BE_ZERO = "Payment amount cannot be zero." ;
	public static final String APPCOUTBYCATEGORY_ERROR = " application(s) exist with this category. Please move all these application(s) first to another category in order to inactivate." ;
	public static final String ORG_ALREADY_EXISTS = "Organization with this Email Id is already existing!";
	public static final String NO_AVATAR_FOUND = "No Profile Image found.";
	public static final String REQUIRED_NAME_FIELD_MISSING = "Required field(s) i.e. First or Last Name is missing.";
	public static final String PROFILENAME_SIZE_REACHED = "Profile Name should be between 3 to 30 characters.";
	public static final String UNABLE_TO_VERIFY_EMAIL = "Unable to verify your Email Id. Please try again later.";
	public static final String APP_CATEGORY_MISMATCH = "Invalid updation request: application does not belong to provided category.";
	public static final String NO_APPLICATION_SELECTED = "Please select desired application(s) to move.";
	public static final String EMAIL_NOT_VERIFIED = "Your Email address has not be verified yet. Please check your inbox and click on the verification link.";
	public static final String ERROR_OCCURED_DURING_PAYMENT_UPDATE = "Error occured while updating payment information";
	public static final String REQUIRED_FIELD_MISSING_1 = "Required field(s) i.e. transaction ID or paid amount are missing";
	public static final String REQUIRED_FIELD_MISSING_2 = "Incomplete information provided, few required field(s) are missing or empty or invalid.";
	public static final String RECORD_ALREADY_EXISTS = "A record with the provided information is already exists.";
	public static final String INSUFFICIENT_AMOUNT = "Trying to pay partial amount.";
	public static final String NOTHING_TO_UPDATE = "No change(s) found to be update.";
	public static final String INVALID_APP_ID = "Invalid App Id.";
	public static final String RECORD_NOT_FOUND = "No Record Found";
	public static final String WALLET_ADDRESSES_MISSING = "No wallet address(s) has provided.";
	public static final String INVALID_REQUIRED_REQUEST_BODY = "Invalid/Missing request payload";
	public static final String ORG_NOT_FOUND = "Invalid organization Id";
	public static final String USER_CREATION_NOT_ALLOWED = "Invalid action, can't create more than approved user(s) i.e. ";
	public static final String INVALID_COUNT = "Number of requested sub-users should be numeric and greater than 0";
	public static final String SUBDEV_REQUEST_PENDING = "Already a request to add more sub-users in your account is pending with Admin.";
	public static final String REVIEW_REPLY_ERROR = "Reply Already Given";
	public static final String REVIEW_NOT_EXIT = "Proper Review Not Exist";
	public static final String REVIEW_NOT_PERMITTED = "Only 1 review Permitted";
	public static final String NO_ALLOWED = "Already approved request status can't be changed.";
	public static final String USER_EMAIL_NOT_VERIFIED = "User has not verified his email yet; later you can perform appropriate action on his account";
	public static final String ORG_ACCOUNT_INACTIVATED = "Your Organization account has beeb inactivated by ADMIN";
		public static final String INVALID_BVK = "Provided BVK is not signed with BitVault Developer key.";
public static final String INVALID_BITVAULT_KEY = "Provided BVK is not signed with correct BitVault Developer key.";

public static final String NOT_REGISTERED_FOR_NOTIFICATION = "Not registered for notification";
}

