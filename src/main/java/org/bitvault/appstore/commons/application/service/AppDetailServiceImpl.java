package org.bitvault.appstore.commons.application.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bitvault.appstore.cloud.constant.Constants;
import org.bitvault.appstore.cloud.constant.DbConstant;
import org.bitvault.appstore.cloud.constant.ErrorCode;
import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.dto.AppDetail;
import org.bitvault.appstore.cloud.dto.AppImage;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.bitvault.appstore.commons.application.dao.AppDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("AppDetailService")
@Transactional
public class AppDetailServiceImpl implements AppDetailService {
	@Autowired
	AppDetailRepository appDetailRepository;

	@Autowired
	AppImageService appImagesService;

	@Autowired
	AppApplicationService appApplicationService;

	@Override
	public AppDetail findApplicationDetailByAppIdAndVersion(Integer applicationId, String appVersionName,
			String ImageStatus) throws BitVaultException {
		org.bitvault.appstore.cloud.model.AppDetail appDetail = null;
		AppDetail appDetailDTO = null;
		List<AppImage> appImage = null;
		try {
			appDetail = appDetailRepository.findApplicationDetailByAppIdAndVersion(applicationId, appVersionName);
			if (null != appDetail) {
				if (ImageStatus !=null && (ImageStatus.equalsIgnoreCase(Constants.PUBLISHED)
						|| ImageStatus.equalsIgnoreCase(Constants.BETA_PUBLISHED)
						|| ImageStatus.equalsIgnoreCase(Constants.ALPHA_PUBLISHED)
						)) {
					appImage = appImagesService.findAppImagesByApplicatinIdAndImageStatus(applicationId, ImageStatus);

				} else {
					appImage = appImagesService.findAppImagesByApplicatinId(applicationId);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);
		}
		if (null != appDetail) {
			appDetailDTO = appDetail.populateAppDetailDto(appDetail);
			// appDetailDTO.setApplicationId(appDetail.getAppApplication().getAppApplicationId());
			if (null != appImage) {
				appDetailDTO.setAppImage(appImage);
			}
		}
		return appDetailDTO;
	}

	@Override
	public AppDetail saveAppDetail(org.bitvault.appstore.cloud.model.AppDetail appDetail) throws BitVaultException {

		AppDetail appDetailDto = null;
		try {
			appDetail = appDetailRepository.saveAndFlush(appDetail);
			appDetailDto = appDetail.populateAppDetailDto(appDetail);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.UNABLE_TO_SAVE);
		}

		return appDetailDto;
	}

	@Override
	public AppDetail findAppdetailById(Integer id) throws BitVaultException {
		org.bitvault.appstore.cloud.model.AppDetail appDetailEntity = null;
		AppDetail appDetailDto = null;
		try {
			appDetailEntity = appDetailRepository.getOne(id);
			appDetailDto = appDetailEntity.populateAppDetailDto(appDetailEntity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);
		}
		return appDetailDto;
	}

	@Override
	public Integer updateAppDetailStatus(String status, Integer applicationId, String appVersionName) {
		int update = 0;
		try {
			update = appDetailRepository.updateAppDetailStatus(status, applicationId, appVersionName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);
		}
		return update;
	}

	@Override
	public Map<String,Object> appDetailArtifact(Integer applicationId, List<String> status, int page, int size,
			String direction, String property) {
		Page<org.bitvault.appstore.cloud.model.AppDetail> appDetailList = null;
		List<AppDetail> appDetailDtoList = null;
		AppDetail appDetail = null;
		Map<String, Object> appDetailMap = null;

		try {
			if (direction.equals(DbConstant.ASC.toString())) {
				appDetailList = appDetailRepository.appDetailArtifact(applicationId, status,
						new PageRequest(page, size, Sort.Direction.DESC, property));

			} else {
				appDetailList = appDetailRepository.appDetailArtifact(applicationId, status,
						new PageRequest(page, size, Sort.Direction.DESC, property));
			}
			appDetailDtoList = new ArrayList<AppDetail>();
			for (org.bitvault.appstore.cloud.model.AppDetail appDetailModel : appDetailList) {
				appDetail = appDetailModel.populateAppDetailDto(appDetailModel);
				appDetailDtoList.add(appDetail);
			}
			appDetailMap = new HashMap<String, Object>();
			appDetailMap.put("appDetailList", appDetailDtoList);
			appDetailMap.put(Constants.TOTAL_PAGES, appDetailList.getTotalPages());
			appDetailMap.put(Constants.TOTAL_RECORDS, appDetailList.getTotalElements());
			appDetailMap.put(Constants.SORT, appDetailList.getSort().getOrderFor(property).getProperty());
			appDetailMap.put(Constants.SIZE, appDetailList.getNumberOfElements());
		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);
		}
		return appDetailMap;
	}

}
