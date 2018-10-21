package org.bitvault.appstore.cloud.utils;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

import org.apache.commons.io.FileUtils;
import org.bitvault.appstore.cloud.constant.Constants;
import org.bitvault.appstore.cloud.constant.DbConstant;
import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.constant.ValidationConstants;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.bitvault.appstore.cloud.security.EncryptAndGenerateWithAES;
import org.bitvault.appstore.cloud.security.EncryptDecryptData;
import org.bitvault.appstore.cloud.security.SecurityConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Utility {

	public static final Logger logger = LoggerFactory.getLogger(Utility.class);

	public static Boolean isStringEmpty(String value) {
		if (null == value || value.trim().isEmpty()) {
			return true;
		} else {

			return false;
		}
	}

	public static Boolean isStringEmpty(String... values) {
		boolean flag = false;
		try {
			for (String value : values) {
				if (null == value || value.trim().isEmpty()) {
					flag = true;
				}
			}
			return flag;
		} catch (Exception e) {
			return true;
		}
	}

	public static Boolean isCollectionEmpty(Collection<?> collection) {
		if (null == collection || collection.size() == 0) {
			return true;
		} else {

			return false;
		}
	}

	public static Boolean isIntegerEmpty(Integer value) {
		try {
			if (null == value) {
				return true;
			} else {

				return false;
			}
		} catch (Exception e) {
			return true;
		}
	}

	public static Boolean isDoubleEmpty(Double value) {
		try {
			if (null == value) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return true;
		}
	}

	public static String getFileExtension(String fileName) {
		if (null != fileName) {
			int lastIndexOfDot = fileName.lastIndexOf(".");
			return fileName.substring(lastIndexOfDot + 1);
		}
		return null;
	}

	public static Map<String, String> ErrorMap(String key, String errorMessage, Map<String, String> map) {
		if (null == map || map.size() == 0) {
			map = new HashMap<String, String>();
		}
		map.put(key, errorMessage);
		return map;
	}

	public static boolean isValidEmailID(String emailId) {
		// Pattern pattern = Pattern.compile(ValidationConstants.EMAIL_PATTERN);
		// Matcher matcher = pattern.matcher(emailId);
		// return matcher.matches();
		return isValid(emailId, ValidationConstants.EMAIL_PATTERN);
	}

	// public static boolean isValidPackageName(String packageName) {
	//// Pattern pattern =
	// Pattern.compile(ValidationConstants.PACKAGE_NAME_PATTERN);
	//// Matcher matcher = pattern.matcher(packageName);
	//// return matcher.matches();
	// return isValid(packageName, ValidationConstants.PACKAGE_NAME_PATTERN);
	// }

	public static boolean isValidURL(String url) {
		return isValid(url, ValidationConstants.URL_PATTERN);
	}

	public static boolean isValid(String toMatch, String regex) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(toMatch);
		return matcher.matches();
	}

	public static String replaceFileName(String fileName) {
		String replacedFileName = null;
		try {
			if (null != fileName) {
				String extention = getFileExtension(fileName);
				String uuid = getUuid();
				replacedFileName = uuid + "." + extention;
				return replacedFileName;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	public static String getUuid() {
		return UUID.randomUUID().toString();
	}

	public static Boolean isValidScreenShot(int width, int height) {

		int min = 320;
		int max = 3840;
		if (!(min < width && width < max) || !(min < height && height < max)) {
			return false;
		}

		if (width > height) {
			if (!((height * 2) > width)) {
				return false;
			}
		} else if (width < height) {
			if (!((width * 2) > height)) {
				return false;
			}
		} else if (height == width) {
			return false;
		}

		return true;
	}

	public static void releaseByteArray(byte[] bytes) {
		if (bytes.length != 0) {
			Arrays.fill(bytes, (byte) 0);
		}
		bytes = null;

	}

	public static boolean isValidDateFormat(String format, String value) {
		Date date = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			date = sdf.parse(value);
			if (!value.equals(sdf.format(date))) {
				date = null;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.INVALID_DATE_FORMAT);
		}
		return date != null;
	}

	public static String formatStringDate(String value) {
		Date date = null;
		String stringDate = null;
		try {

			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			date = format.parse(value);
			stringDate = format.format(date);

		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.INVALID_DATE_FORMAT);
		}
		return stringDate;
	}

	public static Date converStringToDate(String value) {
		Date date = null;
		try {
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			date = format.parse(value);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.INVALID_DATE_FORMAT);
		}
		return date;
	}

	public static Set<String> getListOfDatesBetweenDates(Date startdate, Date enddate) {
		Set<String> dates = new LinkedHashSet<String>();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(startdate);
		enddate = IncrementDateByOneDay(enddate);
		while (calendar.getTime().before(enddate)) {
			Date result = calendar.getTime();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			dates.add(format.format(result));
			calendar.add(Calendar.DATE, 1);
		}
		return dates;
	}

	public static Date IncrementDateByOneDay(Date date) {
		try {
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			c.add(Calendar.DATE, 1);
			date = c.getTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}

	public static Set<String> getListOfAllMonth() {
		Set<String> listOfMonth = new LinkedHashSet<String>();
		Calendar cal = Calendar.getInstance();
		String currentMonth = new SimpleDateFormat("MMMM").format(cal.getTime());
		DateFormatSymbols dfs = new DateFormatSymbols();
		String[] months = dfs.getMonths();
		for (String month : months) {
			listOfMonth.add(month);
			if (month.equals(currentMonth))
				break;
		}
		return listOfMonth;
	}

	public static Path writeInputStreamEncryptThenInput(String filePath, byte[] inputstreamData) {

		Path path = null;

		try {
			EncryptDecryptData encryptData = new EncryptDecryptData();
			// byte[] encryptedByteArray = encryptData.encryptData(inputstreamData,
			// SecurityConstants.BTC_PUBLIC_KEY);

			byte[] encryptedByteArray = encryptData.encryptData(inputstreamData, SecurityConstants.BTC_PUBLIC_KEY);

			path = Paths.get(filePath);
			if (Files.exists(path)) {
				deleteTempApkFolder(Constants.FILEPATH + Constants.TEMPBVK);
			}
			Files.createDirectories(path.getParent());
			System.out.println("directory created");
			Files.write(path, encryptedByteArray);
			System.out.println("file uplaoded");
		} catch (Exception e) {
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);
		}

		return path;
	}

	public static Path encryptSymKeyAndWrite(String mobileUserPublicKey, String filePath, InputStream inputStream) {

		Path path = null;

		try {

			logger.info("Generating symmetric key");

			// Generate a symmetric key
			SecretKey secretKey = EncryptAndGenerateWithAES.getSecretEncryptionKey();

			logger.info("Encrypt symmetric key with server public key");

			// Encrypt above symmetric with server public key
			byte[] encryptedServerKey = new EncryptDecryptData()
					.encryptData(Base64.getEncoder().encode(secretKey.getEncoded()), SecurityConstants.BTC_PUBLIC_KEY);

			logger.info("Encrypt above encrypted key with mobile user public key");

			// Encrypt above resultant byte array with mobile user key
			String encryptedkey = Base64.getEncoder()
					.encodeToString((new EncryptDecryptData().encryptData(encryptedServerKey, mobileUserPublicKey)));

			path = Paths.get(filePath);

			if (Files.exists(path)) {
				// deleteTempApkFolder(Constants.FILEPATH + Constants.TEMPBVK);
				new File(filePath).delete();
			}

			Files.createDirectories(path.getParent());

			System.out.println("directory created");

			String value = "";

			value += encryptedkey;
			value += Constants.FILE_SEPERATOR;

			// write encrypted key with seperater in file
			Files.write(path, value.getBytes(), StandardOpenOption.CREATE);

			logger.info("Encrypt file with above generated key");

			// write data file data in chunks after encryption in the file
			EncryptAndGenerateWithAES.encryptFileAESInChunksInput(inputStream, secretKey, Cipher.ENCRYPT_MODE,
					path.toFile());

			inputStream.close();

			System.out.println("file uplaoded");

		} catch (Exception e) {
			logger.info("Error while generating download key " + e.getMessage());

			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);
		}

		return path;
	}

	public static void deleteTempApkFolder(String path) throws BitVaultException {
		try {
			FileUtils.deleteDirectory(new File(path));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getUploadingStatus(String uploadedFrom) {

		switch (uploadedFrom.toUpperCase()) {

		case DbConstant.UPLOADED_FROM_PROD:
			return Constants.DRAFT;

		case DbConstant.UPLOADED_FROM_ALPHA:
			return Constants.ALPHA_DRAFT;

		case DbConstant.UPLOADED_FROM_BETA:
			return Constants.BETA_DRAFT;

		default:
			throw new BitVaultException(ErrorMessageConstant.INVALID_APPSTATUS);

		}

	}

}
