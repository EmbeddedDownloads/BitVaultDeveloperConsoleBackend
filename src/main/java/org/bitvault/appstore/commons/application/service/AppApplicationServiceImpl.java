package org.bitvault.appstore.commons.application.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.bitvault.appstore.cloud.aws.service.AwsS3Service;
import org.bitvault.appstore.cloud.constant.Constants;
import org.bitvault.appstore.cloud.constant.DbConstant;
import org.bitvault.appstore.cloud.constant.ErrorCode;
import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.dto.AppCategory;
import org.bitvault.appstore.cloud.dto.AppImage;
import org.bitvault.appstore.cloud.dto.Application;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.bitvault.appstore.cloud.model.AppApplication;
import org.bitvault.appstore.cloud.model.ApplicationType;
import org.bitvault.appstore.cloud.model.Request;
import org.bitvault.appstore.cloud.model.RequestType;
import org.bitvault.appstore.cloud.security.EncryptDecryptData;
import org.bitvault.appstore.cloud.user.common.dao.RequestTypeRepository;
import org.bitvault.appstore.cloud.user.common.service.RequestActivityService;
import org.bitvault.appstore.cloud.user.common.service.RequestService;
import org.bitvault.appstore.cloud.utils.APKDetail;
import org.bitvault.appstore.cloud.utils.APKparser;
import org.bitvault.appstore.cloud.utils.Utility;
import org.bitvault.appstore.commons.application.controller.AppApplicationController;
import org.bitvault.appstore.commons.application.dao.AppApplicationRepository;
import org.bitvault.appstore.commons.application.elasticdao.AppApplicationElasticRepository;
import org.bouncycastle.util.encoders.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service("AppApplicationService")
@Transactional
public class AppApplicationServiceImpl implements AppApplicationService {

	public static final Logger logger = LoggerFactory.getLogger(AppApplicationServiceImpl.class);
	
	@Autowired
	AppApplicationRepository appApplicationRepository;

	@Autowired
	RequestService requestService;

	@Autowired
	RequestTypeRepository requestTypeRepository;

	@Autowired
	AwsS3Service awsS3Service;

	@Autowired
	AppImageService appImageService;

	@Autowired
	RequestActivityService requestActivityService;
	@Autowired
	AppApplicationElasticRepository elasticRepository;

	@Autowired
	ApplicationService applicationService;
		@Autowired
	ElasticsearchTemplate elasticSearchTemplate;

	@Override
	public org.bitvault.appstore.cloud.dto.AppApplication saveAppApllication(
			org.bitvault.appstore.cloud.dto.AppApplication application) throws BitVaultException {
		AppApplication app = null;
		org.bitvault.appstore.cloud.dto.AppApplication appDto = null;
		try {
			app = appApplicationRepository.saveAndFlush(application.populateAppApplication(application));
			if (null != app) {
				appDto = app.populateAppApplicationDTO(app);
				// org.bitvault.appstore.cloud.model.AppCategory appCategory =
				// app.getAppCategory();
				// appDto.setAppCategory(appCategory.populateAppCategoryDTO(appCategory));
				// ApplicationType appType = app.getApplicationType();

				// appDto.setApplicationType(appType.populateAppTypeDto(appType));

			}
		} catch (Exception e) {
			e.printStackTrace();

			throw new BitVaultException(ErrorMessageConstant.UNABLE_TO_SAVE);
		}
		return appDto;

	}

	@Override
	public Map<String, Object> listOfAllAppApplication(int page, int size, String direction, String property)
			throws BitVaultException {
		Page<AppApplication> appList = null;

		List<org.bitvault.appstore.cloud.dto.AppApplication> appListDTO = new ArrayList<org.bitvault.appstore.cloud.dto.AppApplication>();
		org.bitvault.appstore.cloud.dto.AppApplication appDTO = null;
		AppCategory appCategoryDTO = null;
		Map<String, Object> allAppMap = null;
		try {
			if (direction.equals(DbConstant.ASC.toString())) {
				appList = appApplicationRepository.findAll(new PageRequest(page, size, Sort.Direction.ASC, property));
			} else {
				appList = appApplicationRepository.findAll(new PageRequest(page, size, Sort.Direction.DESC, property));

			}
			for (AppApplication appApplication : appList) {
				// org.bitvault.appstore.cloud.model.AppCategory appCategory =
				// appApplication.getAppCategory();
				appDTO = appApplication.populateAppApplicationDTO(appApplication);
				// appDTO.setAppCategory(appCategory.populateAppCategoryDTO(appCategory));
				appListDTO.add(appDTO);
			}
			allAppMap = new HashMap<String, Object>();
			allAppMap.put("appList", appListDTO);
			allAppMap.put(Constants.TOTAL_PAGES, appList.getTotalPages());
			allAppMap.put(Constants.TOTAL_RECORDS, appList.getTotalElements());
			allAppMap.put(Constants.SIZE, appList.getNumberOfElements());
			allAppMap.put(Constants.SORT, appList.getSort());

		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED, ErrorCode.SOME_ERROR_OCCURED_CODE);
		}

		return allAppMap;

	}

	@Override
	public org.bitvault.appstore.cloud.dto.AppApplication findAppApplicationByPackageName(String packageName)
			throws BitVaultException {
		AppApplication app = null;
		org.bitvault.appstore.cloud.dto.AppApplication appDTO = null;
		try {

			app = appApplicationRepository.findAppApplicationByPackageName(packageName);
			if (null != app) {
				appDTO = app.populateAppApplicationDTO(app);
				// org.bitvault.appstore.cloud.model.AppCategory appCategory =
				// app.getAppCategory();
				// appDTO.setAppCategory(appCategory.populateAppCategoryDTO(appCategory));

			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED, ErrorCode.SOME_ERROR_OCCURED_CODE);
		}
		// if (null == app) {
		// throw new BitVaultException(ErrorMessageConstant.RESULT_NOT_FOUND,
		// ErrorCode.RESULT_NOT_FOUND_CODE);
		// } else {

		return appDTO;

	}

	@Override
	public Map<String, Object> findAppApplicationByUserId(String userId, int page, int size, String direction,
			String property) throws BitVaultException {
		Page<AppApplication> appList = null;
		org.bitvault.appstore.cloud.dto.AppApplication appDTO = null;
		List<org.bitvault.appstore.cloud.dto.AppApplication> appDTOList = new ArrayList<org.bitvault.appstore.cloud.dto.AppApplication>();
		Map<String, Object> allAppMap = null;

		try {
			if (direction.equals(DbConstant.ASC)) {

				appList = appApplicationRepository.findAppApplicationByUserId(userId,
						new PageRequest(page, size, Sort.Direction.ASC, property));
			} else {
				appList = appApplicationRepository.findAppApplicationByUserId(userId,
						new PageRequest(page, size, Sort.Direction.DESC, property));
			}
			for (AppApplication appApplication : appList) {
				// org.bitvault.appstore.cloud.model.AppCategory appCategory =
				// appApplication.getAppCategory();
				appDTO = appApplication.populateAppApplicationDTO(appApplication);

				appDTOList.add(appDTO);
			}
			allAppMap = new HashMap<String, Object>();
			allAppMap.put("appList", appDTOList);
			allAppMap.put(Constants.TOTAL_PAGES, appList.getTotalPages());
			allAppMap.put(Constants.TOTAL_RECORDS, appList.getTotalElements());
			allAppMap.put(Constants.SIZE, appList.getNumberOfElements());

			allAppMap.put(Constants.SORT, appList.getSort());
		} catch (Exception e) {
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED, ErrorCode.SOME_ERROR_OCCURED_CODE);
		}

		return allAppMap;
	}

	@Override
	public Map<String, Object> findAppApplicationByCategory(List<Integer> categoryIdList, int page, int size,
			String direction, String property) throws BitVaultException {
		Page<AppApplication> appList = null;
		org.bitvault.appstore.cloud.dto.AppApplication appDTO = null;
		List<org.bitvault.appstore.cloud.dto.AppApplication> appDTOList = new ArrayList<org.bitvault.appstore.cloud.dto.AppApplication>();
		Map<String, Object> allAppMap = null;

		try {
			if (direction.equals(DbConstant.ASC)) {
				appList = appApplicationRepository.findAppApplicationByCategory(categoryIdList,
						new PageRequest(page, size, Sort.Direction.ASC, property));
			} else {
				appList = appApplicationRepository.findAppApplicationByCategory(categoryIdList,
						new PageRequest(page, size, Sort.Direction.DESC, property));
			}
			for (AppApplication appApplication : appList) {
				// org.bitvault.appstore.cloud.model.AppCategory appCategory =
				// appApplication.getAppCategory();
				appDTO = appApplication.populateAppApplicationDTO(appApplication);
				// appDTO.setAppCategory(appCategory.populateAppCategoryDTO(appCategory));
				appDTOList.add(appDTO);
			}
			allAppMap = new HashMap<String, Object>();
			allAppMap.put("appList", appDTOList);
			allAppMap.put(Constants.TOTAL_PAGES, appList.getTotalPages());
			allAppMap.put(Constants.TOTAL_RECORDS, appList.getTotalElements());
			allAppMap.put(Constants.SORT, appList.getSort().getOrderFor(property).getProperty());
			allAppMap.put(Constants.SIZE, appList.getNumberOfElements());

		} catch (Exception e) {
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED, ErrorCode.SOME_ERROR_OCCURED_CODE);
		}

		return allAppMap;

	}

	@Override
	public Map<String, Object> findAppApplicationByStatus(List<String> status, int page, int size, String direction,
			String property) throws BitVaultException {
		Page<AppApplication> appList = null;
		org.bitvault.appstore.cloud.dto.AppApplication appDTO = null;
		List<org.bitvault.appstore.cloud.dto.AppApplication> appDTOList = new ArrayList<org.bitvault.appstore.cloud.dto.AppApplication>();
		Map<String, Object> allAppMap = null;

		try {
			if (direction.equals(DbConstant.ASC)) {
				appList = appApplicationRepository.findAppApplicationByStatus(status,
						new PageRequest(page, size, Sort.Direction.ASC, property));
			} else {
				appList = appApplicationRepository.findAppApplicationByStatus(status,
						new PageRequest(page, size, Sort.Direction.DESC, property));

			}
			for (AppApplication appApplication : appList) {
				// org.bitvault.appstore.cloud.model.AppCategory appCategory =
				// appApplication.getAppCategory();
				appDTO = appApplication.populateAppApplicationDTO(appApplication);
				// appDTO.setAppCategory(appCategory.populateAppCategoryDTO(appCategory));
				appDTOList.add(appDTO);
			}
			allAppMap = new HashMap<String, Object>();
			allAppMap.put("appList", appDTOList);
			allAppMap.put(Constants.TOTAL_PAGES, appList.getTotalPages());
			allAppMap.put(Constants.TOTAL_RECORDS, appList.getTotalElements());
			allAppMap.put(Constants.SORT, appList.getSort());
			allAppMap.put(Constants.SIZE, appList.getNumberOfElements());

		} catch (Exception e) {
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED, ErrorCode.SOME_ERROR_OCCURED_CODE);
		}

		return allAppMap;

	}

	@Override
	public Integer updateAppStatus(String status, Integer appId) throws BitVaultException {

		Integer update = 0;

		try {
			// app = appApplicationRepository.findAppApplicationByAppId(appId);

			update = appApplicationRepository.changeApplicationStatus(status, appId);

		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED, ErrorCode.SOME_ERROR_OCCURED_CODE);
		}

		return update;
	}

	@Override
	public org.bitvault.appstore.cloud.dto.AppApplication findApplicationByAppId(Integer applicationId)
			throws BitVaultException {
		AppApplication app = null;
		org.bitvault.appstore.cloud.dto.AppApplication appDTO = null;
		try {
			app = appApplicationRepository.findAppApplicationByAppId(applicationId);

		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);
		}
		if (null == app) {
			throw new BitVaultException(ErrorMessageConstant.RESULT_NOT_FOUND);
		} else {
			appDTO = app.populateAppApplicationDTO(app);
			// org.bitvault.appstore.cloud.model.AppCategory appCategory =
			// app.getAppCategory();
			// appDTO.setAppCategory(appCategory.populateAppCategoryDTO(appCategory));
			// ApplicationType appType = app.getApplicationType();
			// appDTO.setApplicationType(appType.populateAppTypeDto(appType));

		}
		return appDTO;
	}

	@Override
	public Map<String, Object> findAppApplicationByAppName(String appName, int page, int size, String direction,
			String property) throws BitVaultException {
		Page<AppApplication> appList = null;
		org.bitvault.appstore.cloud.dto.AppApplication appDTO = null;
		List<org.bitvault.appstore.cloud.dto.AppApplication> appDTOList = new ArrayList<org.bitvault.appstore.cloud.dto.AppApplication>();
		Map<String, Object> allAppMap = null;

		try {
			if (direction.equals(DbConstant.ASC)) {
				appList = appApplicationRepository.findAppApplicationByAppName(appName,
						new PageRequest(page, size, Sort.Direction.ASC, property));
			} else {
				appList = appApplicationRepository.findAppApplicationByAppName(appName,
						new PageRequest(page, size, Sort.Direction.DESC, property));

			}
			for (AppApplication appApplication : appList) {
				// org.bitvault.appstore.cloud.model.AppCategory appCategory =
				// appApplication.getAppCategory();
				appDTO = appApplication.populateAppApplicationDTO(appApplication);
				// appDTO.setAppCategory(appCategory.populateAppCategoryDTO(appCategory));
				appDTOList.add(appDTO);
			}
			allAppMap = new HashMap<String, Object>();
			allAppMap.put("appList", appDTOList);
			allAppMap.put(Constants.TOTAL_PAGES, appList.getTotalPages());
			allAppMap.put(Constants.TOTAL_RECORDS, appList.getTotalElements());
			allAppMap.put(Constants.SORT, appList.getSort());
			allAppMap.put(Constants.SIZE, appList.getNumberOfElements());

		} catch (Exception e) {
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED, ErrorCode.SOME_ERROR_OCCURED_CODE);
		}

		return allAppMap;

	}

	@Override
	public Map<String, Object> uploadApk(String appName, String language, MultipartFile dpkFile, String fileName,
			String userId, String privateKey,String uploadedFrom) throws BitVaultException {
		Map<String, Object> detailMap = null;
		if (dpkFile.isEmpty()) {
			throw new BitVaultException(ErrorMessageConstant.FILE_IS_EMPTY);
		} else {
			try {
				System.out.println("uploading file in raw folder");
				int count = 0, count_file = 0;
				int bufferSize = 1024 * 1024;
				String encryptedKey;
				String filePath = Constants.FILEPATH + userId + "/" + fileName;
				String filePathApk = Constants.FILEPATH + userId + "/" + fileName + "_updated";

				// Path path = Paths.get(filePath);
				// if (Files.exists(path)) {
				// deleteTempApkFolder(Constants.FILEPATH + userId);
				// }
				// Files.createDirectories(path.getParent());
				// System.out.println("directory created");
				// Files.write(path,dpkFile.getBytes());
				// byte[] fileBytes = new byte[bufferSize];

				// getting inputstream from multipart file
				InputStream inputStream = dpkFile.getInputStream();

				Path path = Paths.get(filePathApk);
				if (Files.exists(path)) {
					deleteTempApkFolder(Constants.FILEPATH + userId);
				}
				Files.createDirectories(path.getParent());

				FileOutputStream outputStream = new FileOutputStream(filePathApk, true);

				logger.info("Getting Encrypted Symmetric key from the file");
				
				// getting key from file
				byte[] keyBytes = new byte[148];
				inputStream.read(keyBytes);
				encryptedKey = new String(keyBytes);

				byte[] fileBytesInChunks = new byte[bufferSize];


				// 148 bytes keysize + 10 bytes seperator size = 158
				inputStream.skip(10L);

				// getting actual file
				count = inputStream.read(fileBytesInChunks);

				logger.info("Getting Encrypted file");
				
				// try {
				while (count >= 0) {
					outputStream.write(fileBytesInChunks, 0, count);
					count = inputStream.read((fileBytesInChunks));
				}
				// } catch (Exception e) {
				// e.printStackTrace();
				// }

				outputStream.flush();
				outputStream.close();

				try {

					EncryptDecryptData encryptData = new EncryptDecryptData();

					byte[] mobileSymmetricKey = null;

					logger.info("Decrypting Encrypted Symmetric key to get Actual symmetric key");
					
					// Get Symm actual key generate on console
					mobileSymmetricKey = Base64
							.decode(encryptData.decryptMessage(Base64.decode(encryptedKey.getBytes()), privateKey));

					SecretKey secretKey = new SecretKeySpec(mobileSymmetricKey, 0, mobileSymmetricKey.length, "AES");

					logger.info("Secret key successfully retrived");
					
					Path pathOriginal = Paths.get(filePath);
					if (Files.exists(pathOriginal)) {
						new File(filePath).delete();
					}
					Files.createDirectories(pathOriginal.getParent());

					encryptData.decryptMessageAES(new File(filePathApk), pathOriginal.toFile(), secretKey);

					logger.info("File successfully retrived");
					
				} catch (Exception e) {
					logger.info("Error occured in encryption decryption while uploading");
					e.printStackTrace();
					throw new BitVaultException(ErrorMessageConstant.INVALID_BVK);
				} finally {

					// Delete generated DPK file
					if (Files.exists(path)) {
						new File(filePathApk).delete();
					}
				}

				APKDetail detail = APKparser.getAPKDetail(filePath);
				detail.setApkUrl(filePath);
				detail.setStatus(uploadedFrom);
				detailMap = new HashMap<String, Object>();

				
				
				// Setting generate key-spec in map
				// detailMap.put("publicKey", "");
				detailMap.put("detail", detail);
			} catch (Exception e) {
				logger.info("Error occured while uploading");
				e.printStackTrace();
				if (e instanceof BitVaultException) {
					throw new BitVaultException(e.getMessage());
				}
				throw new BitVaultException(ErrorMessageConstant.UNABLE_TO_UPLOAD_FILE);
			}
		}
		return detailMap;
	}

	@Override
	public void saveAppApllicationRequest(String userId, Integer appId, String requestStatus) throws BitVaultException {
		Request req = null;
		RequestType requestType = null;
		try {
			req = requestService.findRequestByAppId(appId);
			if (null != req) {
				requestType = req.getRequestType();
			} else {
				req = new Request();
			}
			req.setApplicationId(appId);
			requestType = requestTypeRepository.findRequestTypeByType(requestStatus);

			req.setUserId(userId);
			req.setRequestType(requestType);
			req.setStatus(DbConstant.PENDING);
			req.setRejectionReason("");
			req = requestService.persistRequest(req);

			requestActivityService.saveRequestActivity(req, null);

		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);
		}

	}

	@Override
	public int getFreeAppsCount(float price, List<String> appStatus) {
		return appApplicationRepository.getFreeAppsCount(price, appStatus);
	}

	@Override
	public int getPaidAppsCount(float price, List<String> appStatus) {
		return appApplicationRepository.getPaidAppsCount(price, appStatus);
	}

	@Override
	public String uploadApkIcon(MultipartFile apkIcon, org.bitvault.appstore.cloud.dto.AppApplication appdto) {
		String appIconUrl = null;
		String fileName = null;
		try {
			if (!apkIcon.getContentType().contains("image/png")) {
				throw new BitVaultException(ErrorMessageConstant.APP_ICON_EXE_ERROR);

			}
			if (apkIcon.getSize() > (1024 * 1024)) {
				throw new BitVaultException(ErrorMessageConstant.APP_ICON_SIZE_ERROR);

			}

			BufferedImage image = ImageIO.read(apkIcon.getInputStream());

			Integer width = image.getWidth();
			Integer height = image.getHeight();

			if (width != 512 || height != 512) {
				throw new BitVaultException(ErrorMessageConstant.APP_ICON_SIZE_ERROR);
			}

			String extension = Utility.getFileExtension(apkIcon.getOriginalFilename());

			String filePath = appdto.getUserId() + "/" + "apk/" + appdto.getPackageName() + "/"
					+ appdto.getLatestVersionName() + "/apk_icon";
			fileName = appdto.getPackageName() + "." + extension;
			appIconUrl = awsS3Service.uploadApkImages(apkIcon, filePath, fileName);
			System.out.println(appIconUrl);
		} catch (Exception e) {
			e.printStackTrace();
			if (e instanceof BitVaultException) {
				throw new BitVaultException(e.getMessage());

			}
			throw new BitVaultException(ErrorMessageConstant.UNABLE_TO_UPLOAD_FILE);
		}

		return appIconUrl;
	}

	@Override
	public List<String> uploadApkImages(List<MultipartFile> apkImages,
			org.bitvault.appstore.cloud.dto.AppApplication appdto, String imageType) {
		String appIconUrl = null;
		String fileName = null;
		List<String> appImagesList = new ArrayList<String>();
		try {

			String filePath = appdto.getUserId() + "/" + "apk/" + appdto.getPackageName() + "/"
					+ appdto.getLatestVersionName() + "/apk_images";
			for (MultipartFile multipartFile : apkImages) {
				fileName = multipartFile.getOriginalFilename();
				String extension = Utility.getFileExtension(fileName);

				fileName = Utility.getUuid() + "." + extension;

				if (multipartFile.getContentType().contains("image/jpeg")
						|| multipartFile.getContentType().contains("image/jpg")
						|| multipartFile.getContentType().contains("image/png")) {

					BufferedImage image = ImageIO.read(multipartFile.getInputStream());

					Integer width = image.getWidth();
					Integer height = image.getHeight();

					if (imageType.equalsIgnoreCase(DbConstant.TYPE_IMAGE)) {
						if (!Utility.isValidScreenShot(width, height)) {
							throw new BitVaultException(ErrorMessageConstant.APP_IMAGE_SIZE_ERROR);

						}

					} else if (imageType.equalsIgnoreCase(DbConstant.TYPE_BANNER)) {

						if (width != 1024 || height != 500) {
							throw new BitVaultException(ErrorMessageConstant.APP_BANNER_SIZE_ERROR);
						}

					}

					// fileName = multipartFile.getOriginalFilename();
					appIconUrl = awsS3Service.uploadApkImages(multipartFile, filePath, fileName);
					appImagesList.add(appIconUrl);
				} else {
					throw new BitVaultException(ErrorMessageConstant.INVALID_BANNER_IMAGE_TYPE);
				}
			}

			System.out.println(appIconUrl);
		} catch (Exception e) {
			e.printStackTrace();
			if (e instanceof BitVaultException) {
				throw new BitVaultException(e.getMessage());

			}
			throw new BitVaultException(ErrorMessageConstant.UNABLE_TO_UPLOAD_FILE);
		}

		return appImagesList;
	}

	@Override
	public void deleteAppImage(AppImage appImageDto, String userId) {
		String filePath = null;
		try {

			if (null != appImageDto) {
				filePath = userId + appImageDto.getAppImageUrl().split(userId)[1];

				awsS3Service.deleteImage(filePath);
				appImageService.deleteAppImageById(appImageDto.getAppImagesId());

			} else {
				throw new BitVaultException(ErrorMessageConstant.RESULT_NOT_FOUND);
			}

		} catch (Exception e) {
			if (e instanceof BitVaultException) {
				throw new BitVaultException(e.getMessage());
			}
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);

		}
	}

	@Override
	public String uploadApkToS3(org.bitvault.appstore.cloud.dto.AppApplication appdto) throws BitVaultException {
		String apkUrl = null;
		String fileName = null;
		try {
			String filePath = appdto.getUserId() + "/" + "apk/" + appdto.getPackageName() + "/"
					+ appdto.getLatestVersionName();
			fileName = appdto.getPackageName() + "." + Utility.getFileExtension(appdto.getApkUrl());
			apkUrl = awsS3Service.uploadApkFiles(appdto.getApkUrl(), filePath, fileName);
			System.out.println(apkUrl);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.UNABLE_TO_UPLOAD_FILE);
		}

		return apkUrl;
	}

	@Override
	public void deleteTempApkFolder(String path) throws BitVaultException {
		try {
			FileUtils.deleteDirectory(new File(path));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Map<String, Object> findAppApplicationByUserIdAndStatus(String userId, List<String> status, int page,
			int size, String direction, String property) throws BitVaultException {
		Page<AppApplication> appList = null;
		org.bitvault.appstore.cloud.dto.AppApplication appDTO = null;
		List<org.bitvault.appstore.cloud.dto.AppApplication> appDTOList = new ArrayList<org.bitvault.appstore.cloud.dto.AppApplication>();
		Map<String, Object> allAppMap = null;

		try {
			if (direction.equals(DbConstant.ASC.toString())) {

				appList = appApplicationRepository.findAppApplicationByUserIdAndStatus(userId, status,
						new PageRequest(page, size, Sort.Direction.ASC, property));
			} else {
				appList = appApplicationRepository.findAppApplicationByUserIdAndStatus(userId, status,
						new PageRequest(page, size, Sort.Direction.DESC, property));
			}
			for (AppApplication appApplication : appList) {
				// org.bitvault.appstore.cloud.model.AppCategory appCategory =
				// appApplication.getAppCategory();
				appDTO = appApplication.populateAppApplicationDTO(appApplication);

				appDTOList.add(appDTO);
			}
			allAppMap = new HashMap<String, Object>();
			allAppMap.put("appList", appDTOList);
			allAppMap.put(Constants.TOTAL_PAGES, appList.getTotalPages());
			allAppMap.put(Constants.TOTAL_RECORDS, appList.getTotalElements());
			allAppMap.put(Constants.SIZE, appList.getNumberOfElements());

			allAppMap.put(Constants.SORT, appList.getSort());
		} catch (Exception e) {
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);
		}

		return allAppMap;
	}

	@Override
	public int getCountAppByCategoryId(Integer categoryId) {
		int count;
		try {
			count = appApplicationRepository.getCountAppByCategoryId(categoryId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);
		}
		return count;
	}

	@Override
	public void updateAppApplication(List<Integer> appIdList, Integer categoryId) {
		try {
			appApplicationRepository.updtateAppApplication(appIdList, categoryId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public Map<String, Object> searchAppApplicationByAppName(String appName, String userId,int page, int size, String direction,
			String property) throws BitVaultException {
		Page<AppApplication> appList = null;
		org.bitvault.appstore.cloud.dto.ApplicationElasticDto appDTO = null;
		List<org.bitvault.appstore.cloud.dto.ApplicationElasticDto> appDTOList = new ArrayList<org.bitvault.appstore.cloud.dto.ApplicationElasticDto>();
		Map<String, Object> allAppMap = null;

		try {
			logger.info("findAppApplicationByAppName method calling");
			if (direction.equals(DbConstant.ASC)) {
				appList = elasticRepository.findAppApplicationByAppName(appName,userId,
						new PageRequest(page, size, Sort.Direction.ASC, property));
			} else {
				appList = elasticRepository.findAppApplicationByAppName(appName,userId,
						new PageRequest(page, size, Sort.Direction.DESC, property));

			}
			logger.info("List fetched successfully");
			for (AppApplication appApplication : appList) {
				appDTO = appApplication.populateApplicationElasticDTO(appApplication);
				appDTOList.add(appDTO);
			}
		
			allAppMap = new HashMap<String, Object>();
			allAppMap.put("appList", appDTOList);
			allAppMap.put(Constants.TOTAL_PAGES, appList.getTotalPages());
			allAppMap.put(Constants.TOTAL_RECORDS, appList.getTotalElements());
			allAppMap.put(Constants.SORT, appList.getSort());
			allAppMap.put(Constants.SIZE, appList.getNumberOfElements());

		} catch (Exception e) {
			logger.info("Error occured during Application searching"+e.getMessage());
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED, ErrorCode.SOME_ERROR_OCCURED_CODE);
		}

		return allAppMap;

	}

	@Override
	public void updateAverageRating(Integer applicationId, float averageRating) {
		AppApplication app = null;
		try { logger.info("");
			app = appApplicationRepository.findOne(applicationId);
			if (app != null) {
				app.setAverageRating(averageRating);
				appApplicationRepository.saveAndFlush(app);
				applicationService.updateAverageRating(applicationId, averageRating);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	@Override
	public String deleteDocument(String appApplicationId) {
		return elasticSearchTemplate.delete(org.bitvault.appstore.cloud.model.AppApplication.class, appApplicationId);
//		return null;
	}

	@Override
	public Map<String, Object> findAppApplicationByStatusList(List<String> status, List<Integer> applicationId,
			int page, int size, String direction, String property) throws BitVaultException {
		Page<AppApplication> appList = null;
		org.bitvault.appstore.cloud.dto.AppApplication appDTO = null;
		List<org.bitvault.appstore.cloud.dto.AppApplication> appDTOList = new ArrayList<org.bitvault.appstore.cloud.dto.AppApplication>();
		Map<String, Object> allAppMap = null;

		try {
			if (direction.equals(DbConstant.ASC.toString())) {

				appList = appApplicationRepository.findAppApplicationByStatusList(status, applicationId, new PageRequest(page, size, Sort.Direction.ASC, property));
			} else {
				appList = appApplicationRepository.findAppApplicationByStatusList(status, applicationId, new PageRequest(page, size, Sort.Direction.ASC, property));
				
			}
			for (AppApplication appApplication : appList) {
				// org.bitvault.appstore.cloud.model.AppCategory appCategory =
				// appApplication.getAppCategory();
				appDTO = appApplication.populateAppApplicationDTO(appApplication);

				appDTOList.add(appDTO);
			}
			allAppMap = new HashMap<String, Object>();
			allAppMap.put("appList", appDTOList);
			allAppMap.put(Constants.TOTAL_PAGES, appList.getTotalPages());
			allAppMap.put(Constants.TOTAL_RECORDS, appList.getTotalElements());
			allAppMap.put(Constants.SIZE, appList.getNumberOfElements());

			allAppMap.put(Constants.SORT, appList.getSort());
		} catch (Exception e) {
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);
		}

		return allAppMap;
	
	}

}
