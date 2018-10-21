package org.bitvault.appstore.mobile.service;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.bitvault.appstore.cloud.aws.service.AwsS3Service;
import org.bitvault.appstore.cloud.constant.Constants;
import org.bitvault.appstore.cloud.constant.ErrorCode;
import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.dto.MobileUser;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.bitvault.appstore.cloud.security.EncryptDecryptData;
import org.bitvault.appstore.cloud.security.SecurityConstants;
import org.bitvault.appstore.mobile.controller.MobileController;
import org.bitvault.appstore.mobile.dao.MobileUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service("MobileUserService")
@Transactional
public class MobileUserServiceImp implements MobileUserService {
	@Autowired
	MobileUserRepository mobileUserRepository;
	
	@Autowired
	AwsS3Service awsService;
	private static final Logger logger = LoggerFactory.getLogger(MobileUserServiceImp.class);
	@Override
	public MobileUser saveMobileUser(MobileUser mobileUserDTO) throws BitVaultException {
		org.bitvault.appstore.cloud.model.MobileUser mobileUser = mobileUserDTO.populateMobileUser(mobileUserDTO);
		try {
            logger.info("MobileUserByPublicAddress method calling");
			org.bitvault.appstore.cloud.model.MobileUser existingMobileUser = mobileUserRepository
					.findMobileUserByPublicAddress(mobileUser.getPublicAdd());
			logger.info("MobileUser Data fetched successfully");
			if (null != existingMobileUser) {
				// int reprocessedCounter = mobileUser.getReprocessedCount();
				// reprocessedCounter++;
				// existingMobileUser.setReprocessedCount(reprocessedCounter);
				// mobileUser =
				// mobileUserRepository.saveAndFlush(existingMobileUser);
				mobileUserDTO = mobileUser.populateMobileUserDTO(mobileUser);
			} else {
				logger.info("MobileUser get saved");
				mobileUser = mobileUserRepository.saveAndFlush(mobileUser);
				logger.info("MobileUser data saved successfully");
				mobileUserDTO = mobileUser.populateMobileUserDTO(mobileUser);

			}
		} catch (Exception e) {
			throw new BitVaultException(ErrorMessageConstant.UNABLE_TO_SAVE, ErrorCode.UNABLE_TO_SAVE_CODE);
		}

		return mobileUserDTO;
	}

	@Override
	public MobileUser findMobileUserByPublicAddress(String publicAddress) throws BitVaultException {
		org.bitvault.appstore.cloud.model.MobileUser mobileUser = null;
		MobileUser mobileUserDTO = null;

		try { logger.info("MobileUserByPublicAddress method calling");
			mobileUser = mobileUserRepository.findMobileUserByPublicAddress(publicAddress);
			logger.info("MobileUser Data fetched successfully");
		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED, ErrorCode.SOME_ERROR_OCCURED_CODE);
		}
		if (null != mobileUser) {
			
			mobileUserDTO = mobileUser.populateMobileUserDTO(mobileUser);

		}
		return mobileUserDTO;
	}

	@Override
	public List<MobileUser> findAllMobileUser() throws BitVaultException {
		List<org.bitvault.appstore.cloud.model.MobileUser> mobileUserList = null;
		MobileUser mobileUserDTO = null;
		List<MobileUser> mobileUserDTOList = new ArrayList<MobileUser>();

		try {
			logger.info("Find All method get called");
			mobileUserList = mobileUserRepository.findAll();
			logger.info("MobileUserList Fetched Successfully");
			for (org.bitvault.appstore.cloud.model.MobileUser mobileUser : mobileUserList) {
				mobileUserDTO = new MobileUser();
				mobileUserDTO = mobileUser.populateMobileUserDTO(mobileUser);
				mobileUserDTOList.add(mobileUserDTO);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED, ErrorCode.SOME_ERROR_OCCURED_CODE);

		}
		if (null == mobileUserList || mobileUserList.size() == 0) {
			throw new BitVaultException(ErrorMessageConstant.RESULT_NOT_FOUND, ErrorCode.RESULT_NOT_FOUND_CODE);

		}
		return mobileUserDTOList;

	}

	@Override
	public Boolean downloadApkFile(String filePath, HttpServletRequest request, HttpServletResponse response,String publicKey)
			throws BitVaultException {
		try {
			
			ServletContext context = request.getServletContext();
			String appPath = context.getRealPath("");
			System.out.println("appPath = " + appPath);
			
//			InputStream inputStream = null;

			String mimeType = context.getMimeType(filePath);
			if (mimeType == null) {
				mimeType = "application/octet-stream";
			}
			
			System.out.println("MIME type: " + mimeType);

			response.setContentType(mimeType);
			
			/*File mFile = new File(filePath);
			inputStream = new FileInputStream(mFile);*/
			
			Path mFile = Paths.get(filePath);
			
			
			
			String headerKey = "Content-Disposition";
			String headerValue = String.format("attachment; filename=\"%s\"",
					mFile.getFileName());
		
			
			response.setHeader(headerKey, headerValue);
//			response.setHeader("Content-Length", String.valueOf(Files.size(mFile)));
			response.setContentLengthLong(Files.size(mFile));
			
			
//			System.out.println("Content lenght  " +  String.valueOf(Files.size(mFile)));
					
			IOUtils.copyLarge(Files.newInputStream(mFile), response.getOutputStream());
					
			response.flushBuffer();
			
//			inputStream.close();
			
			if(Files.exists(mFile)) {
				
				try {
					
					File parentFile = new File(filePath.substring(0,filePath.lastIndexOf("/")));
					
					if(parentFile.exists() && parentFile.isDirectory()) {
						FileUtils.deleteDirectory(parentFile);
					}
					
				}catch (Exception e) {
					throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);
				}
				
			}
			
			
			// File downloadFile = new File(fullPath);
			// FileInputStream inputStream;
			// inputStream = new FileInputStream(downloadFile);
			//
			// String mimeType = context.getMimeType(fullPath);
			// if (mimeType == null) {
			// mimeType = "application/octet-stream";
			// }
			// System.out.println("MIME type: " + mimeType);
			//
			// response.setContentType(mimeType);
			// response.setContentLength((int) downloadFile.length());
			//
			// String headerKey = "Content-Disposition";
			// String headerValue = String.format("attachment; filename=\"%s\"",
			// downloadFile.getName());
			// response.setHeader(headerKey, headerValue);
			//
			// OutputStream outStream = response.getOutputStream();
			//
			// byte[] buffer = new byte[inputStream.available()];
			// int bytesRead = -1;
			//
			// while ((bytesRead = inputStream.read(buffer)) != -1) {
			// outStream.write(buffer, 0, bytesRead);
			// }
			//
			// inputStream.close();
			// outStream.close();
		} catch (Exception e) {
			System.out.println("FAILE: " );
			e.printStackTrace();
			return false;
		}
		System.out.println("SUCCESS: " );
		return true;

	}
	
	
	public Path writeInputStreamEncryptThenInput(String filePath,byte[] inputstreamData) {
		
		Path path = null ;
		EncryptDecryptData encryptData = new EncryptDecryptData();
		byte[] encryptedByteArray = encryptData.encryptData(inputstreamData, SecurityConstants.BTC_PUBLIC_KEY);
	
		
		try {
			 path = Paths.get(filePath);
			if (Files.exists(path)) {
				deleteTempApkFolder(Constants.FILEPATH + Constants.TEMPBVK);
			}
			Files.createDirectories(path.getParent());
			System.out.println("directory created");
			Files.write(path, encryptedByteArray);
			System.out.println("file uplaoded");
			}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		
		return path ;
	}
	
	public void deleteTempApkFolder(String path) throws BitVaultException {
		try {
			FileUtils.deleteDirectory(new File(path));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
    public static byte[] getBytesFromFile(String path) {
        File file = new File(path);
        int size = (int) file.length();
        byte[] bytes = new byte[size];
        try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
            buf.read(bytes, 0, bytes.length);
            buf.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return bytes;
    }

}
