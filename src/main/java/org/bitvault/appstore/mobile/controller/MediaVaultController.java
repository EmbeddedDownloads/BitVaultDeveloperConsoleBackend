package org.bitvault.appstore.mobile.controller;

import java.util.ArrayList;
import java.util.List;

import org.bitvault.appstore.cloud.constant.APIConstants;
import org.bitvault.appstore.cloud.constant.Constants;
import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.bitvault.appstore.cloud.exception.BitVaultResponse;
import org.bitvault.appstore.cloud.model.MediaVault;
import org.bitvault.appstore.cloud.user.common.GeneralResponseModel;
import org.bitvault.appstore.cloud.utils.Response;
import org.bitvault.appstore.cloud.utils.Utility;
import org.bitvault.appstore.mobile.dto.MediaVaultDto;
import org.bitvault.appstore.mobile.service.MediaVaultService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = APIConstants.MOBILE_API_BASE)
public class MediaVaultController {

	private final Logger logger = LoggerFactory.getLogger(MediaVaultController.class);

	@Autowired
	MediaVaultService mediaVaultService;

	@PostMapping(value = APIConstants.MEDIAVAULT_BASE)
	public ResponseEntity<?> save(@RequestBody MediaVaultDto mediaVaultDto) {
		try {
			String hashId = mediaVaultDto.getId();
			if (Utility.isStringEmpty(mediaVaultDto.getEncFileKey(), mediaVaultDto.getEncTxnID(),
					mediaVaultDto.getFilename(), hashId, mediaVaultDto.getType(), mediaVaultDto.getWalletAddress(), mediaVaultDto.getAppId())) {
				return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED,
						new BitVaultResponse(ErrorMessageConstant.REQUIRED_FIELD_MISSING_2)));
			}
			MediaVault mediaVault = mediaVaultService.getOne(hashId);
			if (mediaVault != null) {
				return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED,
						new BitVaultResponse(ErrorMessageConstant.RECORD_ALREADY_EXISTS)));
			}
			mediaVault = new MediaVault();
			BeanUtils.copyProperties(mediaVaultDto, mediaVault);
			mediaVaultService.save(mediaVault);
			return ResponseEntity
					.ok(GeneralResponseModel.of(Constants.SUCCESS, new BitVaultResponse(Constants.SUCCESS_ADDED)));
		} catch (BeansException e) {
			logger.error(e.getMessage());
			return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED,
					new BitVaultResponse(ErrorMessageConstant.SOME_ERROR_OCCURED)));
		}
	}

	@PutMapping(value = APIConstants.MEDIAVAULT_BASE)
	public ResponseEntity<?> update(@RequestBody MediaVaultDto mediaVaultDto) {
		try {
			String hashId = mediaVaultDto.getId();
			String newFilename = mediaVaultDto.getFilename();
			MediaVault mediaVault = checkDetails(mediaVaultDto, "update", hashId, newFilename);
			mediaVault.setFilename(newFilename);
			mediaVaultService.save(mediaVault);
			return ResponseEntity
					.ok(GeneralResponseModel.of(Constants.SUCCESS, new BitVaultResponse(Constants.SUCCESS_UPDATED)));
		} catch (Exception e) {
			logger.error(e.getMessage());
			return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED, new BitVaultResponse(e.getMessage())));
		}
	}

	private MediaVault checkDetails(MediaVaultDto mediaVaultDto, String status, String... values) {
		if (Utility.isStringEmpty(values)) {
			throw new BitVaultException(ErrorMessageConstant.REQUIRED_FIELD_MISSING_2);
		}
		MediaVault mediaVault = mediaVaultService.getOne(mediaVaultDto.getId());
		if (mediaVault == null) {
			throw new BitVaultException(ErrorMessageConstant.RESULT_NOT_FOUND);
		} else if (!mediaVault.getAppId().equals(mediaVaultDto.getAppId())) {
			throw new BitVaultException(ErrorMessageConstant.INVALID_APP_ID);
		} else if (mediaVault.getFilename().equals(mediaVaultDto.getFilename()) && status != null) {
			throw new BitVaultException(ErrorMessageConstant.NOTHING_TO_UPDATE);
		}
		return mediaVault;
	}

	@PostMapping(value = APIConstants.MEDIAVAULT_BASE+ "/delete")
	public ResponseEntity<?> delete(@RequestBody MediaVaultDto mediaVaultDto) {
		try {
			String hashId = mediaVaultDto.getId();
			MediaVault mediaVault = checkDetails(mediaVaultDto, null, hashId);
			mediaVaultService.delete(mediaVault);
			return ResponseEntity
					.ok(GeneralResponseModel.of(Constants.SUCCESS, new BitVaultResponse(Constants.SUCCESS_DELETED)));
		} catch (Exception e) {
			logger.error(e.getMessage());
			return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED, new BitVaultResponse(e.getMessage())));
		}
	}

	@GetMapping(value = APIConstants.MEDIAVAULT_GET_FILEINFO)
	public ResponseEntity<?> getFileInfo(@PathVariable String appId, @PathVariable String hashId) {
		try {
			MediaVaultDto mediaVaultDto = new MediaVaultDto();
			mediaVaultDto.setAppId(appId);
			mediaVaultDto.setId(hashId);
			MediaVault mediaVault = checkDetails(mediaVaultDto, null, hashId);
			BeanUtils.copyProperties(mediaVault, mediaVaultDto);
			return ResponseEntity.ok(GeneralResponseModel.of(Constants.SUCCESS, mediaVaultDto));
		} catch (Exception e) {
			logger.error(e.getMessage());
			return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED, new BitVaultResponse(e.getMessage())));
		}
	}

	@GetMapping(value = APIConstants.MEDIAVAULT_GET_LIST_FILEINFO)
	public ResponseEntity<?> getFileInfoList(@PathVariable String appId, @RequestParam List<String> walletAddresses, 
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "walletAddress") String orderBy,
			@RequestParam(defaultValue = "DESC") String direction) {
		try {
			if(page > 0){
				page--;
			}
			
			if(walletAddresses.size() == 0){
				throw new BitVaultException(ErrorMessageConstant.WALLET_ADDRESSES_MISSING);
			}
			Page<MediaVault> mediaVaultList = mediaVaultService.getFileListOfAppForAllWallets(appId, walletAddresses, page, size, orderBy, direction);
			if (mediaVaultList.getTotalElements() == 0){
				return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED, new BitVaultResponse(ErrorMessageConstant.RESULT_NOT_FOUND)));
			}
			List<MediaVaultDto> resultList = new ArrayList<>();
			for(MediaVault vault : mediaVaultList.getContent()){
				MediaVaultDto mediaVaultDto = new MediaVaultDto();
				BeanUtils.copyProperties(vault, mediaVaultDto);
				resultList.add(mediaVaultDto);
			}
			return ResponseEntity.ok(new Response(Constants.SUCCESS, resultList, mediaVaultList.getTotalPages(),
					mediaVaultList.getTotalElements(), mediaVaultList.getSize(), orderBy));
		} catch (Exception e) {
			logger.error(e.getMessage());
			return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED,
					new BitVaultResponse(e.getMessage())));
		}
	}

}
