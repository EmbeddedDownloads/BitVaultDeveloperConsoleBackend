package org.bitvault.appstore.mobile.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bitvault.appstore.cloud.aws.service.AwsS3Service;
import org.bitvault.appstore.cloud.constant.Constants;
import org.bitvault.appstore.cloud.constant.DbConstant;
import org.bitvault.appstore.cloud.constant.ErrorCode;
import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
//import org.bitvault.appstore.cloud.dto.AppCategory;
import org.bitvault.appstore.cloud.dto.HelpSupportDto;
import org.bitvault.appstore.cloud.exception.BitVaultException;
//import org.bitvault.appstore.cloud.model.AppApplication;
import org.bitvault.appstore.cloud.model.HelpSupport;
import org.bitvault.appstore.cloud.utils.Utility;
import org.bitvault.appstore.mobile.dao.HelpSupportRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class HelpSupportServiceImpl implements HelpSupportService {
	@Autowired
	HelpSupportRepository helpSupportRepository;
	@Autowired
	AwsS3Service awsS3Service;
	private static final Logger logger = LoggerFactory.getLogger(HelpSupportServiceImpl.class);

	@Override
	public HelpSupportDto saveHelpSupport(HelpSupportDto helpSupportDto) {
		try {

			HelpSupport helpSupport = helpSupportDto.populateHelpSupport(helpSupportDto);
			helpSupport = helpSupportRepository.saveAndFlush(helpSupport);
			logger.info("help support model fetched successfullly");
			helpSupportDto = helpSupport.populateHelpSupportDto(helpSupport);
			return helpSupportDto;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Map<String, Object> listOfAllHelpSupport(int page, int size, String direction, String property)
			throws BitVaultException {
		Page<HelpSupport> helpSupportList = null;

		List<org.bitvault.appstore.cloud.dto.HelpSupportDto> helpSupportListDto = new ArrayList<org.bitvault.appstore.cloud.dto.HelpSupportDto>();
		org.bitvault.appstore.cloud.dto.HelpSupportDto helpSupportDto = null;

		Map<String, Object> allAppMap = null;
		try {
			if (direction.equals(DbConstant.ASC.toString())) {
				helpSupportList = helpSupportRepository
						.findAll(new PageRequest(page, size, Sort.Direction.ASC, property));
			} else {
				helpSupportList = helpSupportRepository
						.findAll(new PageRequest(page, size, Sort.Direction.DESC, property));

			}
			logger.info("List from database Fetched Successfully");
			for (HelpSupport helpSupport : helpSupportList) {

				helpSupportDto = helpSupport.populateHelpSupportDto(helpSupport);

				helpSupportListDto.add(helpSupportDto);
			}

			allAppMap = new HashMap<String, Object>();
			allAppMap.put("appList", helpSupportListDto);
			allAppMap.put(Constants.TOTAL_PAGES, helpSupportList.getTotalPages());
			allAppMap.put(Constants.TOTAL_RECORDS, helpSupportList.getTotalElements());
			allAppMap.put(Constants.SIZE, helpSupportList.getNumberOfElements());
			allAppMap.put(Constants.SORT, helpSupportList.getSort());

		} catch (Exception e) {
			logger.info("Error occured during List Fetching" + e.getMessage());
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED, ErrorCode.SOME_ERROR_OCCURED_CODE);
		}

		return allAppMap;

	}

	@Override
	public String uploadApkImages(MultipartFile apkImage, HelpSupportDto helpSupportDto) {
		String appIconUrl = null;
		String fileName = null;
		try {

			String filePath = "HelpSupport/" + helpSupportDto.getFromAdd() + "/" + System.currentTimeMillis();

			fileName = apkImage.getOriginalFilename();
			String extension = Utility.getFileExtension(fileName);

			fileName = Utility.getUuid() + "." + extension;
			logger.info("Upload the image");
			appIconUrl = awsS3Service.uploadApkImages(apkImage, filePath, fileName);
			logger.info("Image Uploaded successfully");
			System.out.println(appIconUrl);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Error occured during Image Uploading" + e.getMessage());
			if (e instanceof BitVaultException) {
				throw new BitVaultException(e.getMessage());

			}
			throw new BitVaultException(ErrorMessageConstant.UNABLE_TO_UPLOAD_FILE);
		}

		return appIconUrl;
	}
}
