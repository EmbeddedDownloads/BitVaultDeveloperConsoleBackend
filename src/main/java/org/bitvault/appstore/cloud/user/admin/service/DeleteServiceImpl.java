package org.bitvault.appstore.cloud.user.admin.service;

import javax.transaction.Transactional;

import org.bitvault.appstore.cloud.model.AppRateReview;
import org.bitvault.appstore.cloud.model.DevUser;
import org.bitvault.appstore.cloud.model.MobileUserAppStatus;
import org.bitvault.appstore.cloud.user.common.dao.RequestActivityRepository;
import org.bitvault.appstore.cloud.user.common.dao.RequestRepository;
import org.bitvault.appstore.cloud.user.common.service.RequestActivityService;
import org.bitvault.appstore.cloud.user.dev.dao.AccountRepository;
import org.bitvault.appstore.cloud.user.dev.dao.DevPaymentRepository;
import org.bitvault.appstore.cloud.user.dev.dao.DevUserRepository;
import org.bitvault.appstore.cloud.user.dev.dao.ReviewReplyRepository;
import org.bitvault.appstore.commons.application.dao.AppApplicationRepository;
import org.bitvault.appstore.commons.application.dao.AppDetailRepository;
import org.bitvault.appstore.commons.application.dao.AppHistoryRepository;
import org.bitvault.appstore.commons.application.dao.AppImagesRepository;
import org.bitvault.appstore.commons.application.dao.AppRateReviewRepository;
import org.bitvault.appstore.commons.application.dao.AppStatisticsRepository;
import org.bitvault.appstore.commons.application.dao.AppTesterRepository;
import org.bitvault.appstore.commons.application.dao.ApplicationRepository;
import org.bitvault.appstore.commons.application.service.AppApplicationService;
import org.bitvault.appstore.commons.application.service.AppDetailService;
import org.bitvault.appstore.commons.application.service.AppHistoryService;
import org.bitvault.appstore.commons.application.service.AppImageService;
import org.bitvault.appstore.commons.application.service.ApplicationService;
import org.bitvault.appstore.mobile.dao.MobileUserAppRepository;
import org.bitvault.appstore.mobile.dao.MobileUserAppStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class DeleteServiceImpl implements DeleteService {
	@Autowired
	DevUserRepository devUserRepository;
	@Autowired
	AccountRepository accountRepository;
	@Autowired
	DevPaymentRepository devPaymentRepository;
	@Autowired
	RequestRepository requestRepository;
	@Autowired
	private AppImageService appImageService;
	@Autowired
	private AppDetailService appDetailService;
	@Autowired
	RequestActivityRepository requestActivityRepository;
	@Autowired
	AppApplicationRepository appAppRepository;
	@Autowired
	ApplicationRepository applicationRepository;
	@Autowired
	AppStatisticsRepository appStatRepository;
	@Autowired
	AppRateReviewRepository appRateRepository;
	@Autowired
	AppImagesRepository imageRepository;
	@Autowired
	AppDetailRepository appDetailRepository;
	@Autowired
	AppTesterRepository appTesterRepository;
	@Autowired
	RequestActivityRepository requestActRepository;
	@Autowired
	MobileUserAppRepository mobileRepository;
	@Autowired
	AppHistoryRepository appHistoryRepository;
	@Autowired
	ReviewReplyRepository reviewReplyRepository;
	@Autowired
	AppHistoryService appHistoryService;
	@Autowired
	RequestActivityService requestActivityService;
	@Autowired
	ApplicationService applicationService;
	@Autowired
	MobileUserAppStatusRepository mobileuserAppStatusRepository;
	@Autowired
	AppApplicationService appApplicationService;
	@Autowired
	ElasticsearchTemplate elasticSearchTemplate;

	@Override

	public void deleteUsersByUserId(String userId, String roleName) {
		String roleOrganisation = "ROLE_ORGANIZATION";
		if (roleName.equals(roleOrganisation)) {
			devUserRepository.deleteSubDeveloperByUserId(userId);

		}
		DevUser devUser = devUserRepository.findByUserId(userId);
		int accId = devUser.getAccount().getAccId();
		devUserRepository.delete(userId);

		accountRepository.delete(accId);
		devPaymentRepository.deleteByUserId(userId);
		requestRepository.deleteRequestByuserId(userId);

	}

	@Override
	public void deleteSubDeveloper(String userId) {
		DevUser devUser = devUserRepository.findByUserId(userId);
		devUserRepository.delete(userId);
		// DevUser devUser = devUserRepository.findByUserId(userId);
		accountRepository.delete(devUser.getAccount().getAccId());
	}

	@Override
	// TODO: MOBILE_USER_APPDATA REPOSIT0RY TO BE CREATED
	public void deleteApplicationData(Integer applicationId) {
		requestActRepository.deleteRequestActivityByApplicationId(applicationId);
		requestRepository.deleteRequestByApplicationId(applicationId);
		Page<AppRateReview> appRateReviewList = null;
		appRateReviewList = appRateRepository.findAppRateReviewByAppId(applicationId,
				new PageRequest(0, Integer.MAX_VALUE, Sort.Direction.DESC, "updatedAt"));
		for (AppRateReview appRateReview : appRateReviewList) {

			reviewReplyRepository.deleteByappRateReviewId(appRateReview.getAppRateReviewId());
		}
		appRateRepository.deleteRateReviewByApplicationId(applicationId);
		appStatRepository.deleteAppStatByApplicationId(applicationId);
		imageRepository.deleteAppImageByApplicationId(applicationId);
		appDetailRepository.deleteAppDetailByApplicationId(applicationId);
		mobileuserAppStatusRepository.deletemobileUserAppStatusByApplicationId(applicationId);
		mobileRepository.deletemobileUserAppByApplicationId(applicationId);
		appHistoryRepository.deletAppHistoryByApplicationId(applicationId);
		appTesterRepository.deleteAppTesterByApplicationId(applicationId);
		applicationRepository.deletApplicationByApplicationId(applicationId);
		appAppRepository.deleteAppApplicationByApplicationId(applicationId);
		applicationService.deleteDocument(applicationId.toString());
		appApplicationService.deleteDocument(applicationId.toString());

	}

	@Override
	public String deleteDocument(String userId) {
		return elasticSearchTemplate.delete(org.bitvault.appstore.cloud.model.DevUser.class, userId);
	}

	@Override
	public String deleteDocumentApplication(String applicationId) {
		return elasticSearchTemplate.delete(org.bitvault.appstore.cloud.model.Application.class, applicationId);
	}

	@Override
	public String deleteDocumentAppApplication(String AppApplicationId) {
		return elasticSearchTemplate.delete(org.bitvault.appstore.cloud.model.AppApplication.class, AppApplicationId)
;	}

}
