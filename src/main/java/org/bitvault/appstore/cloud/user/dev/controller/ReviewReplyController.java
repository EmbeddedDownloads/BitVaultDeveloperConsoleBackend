package org.bitvault.appstore.cloud.user.dev.controller;

import javax.servlet.http.HttpServletRequest;

import org.bitvault.appstore.cloud.constant.APIConstants;
import org.bitvault.appstore.cloud.constant.Constants;
import org.bitvault.appstore.cloud.constant.ErrorCode;
import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.dto.AppApplication;
import org.bitvault.appstore.cloud.dto.AppRateReviewDto;
import org.bitvault.appstore.cloud.dto.AppReviewReplydto;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.bitvault.appstore.cloud.exception.BitVaultResponse;
import org.bitvault.appstore.cloud.model.AdminUser;
import org.bitvault.appstore.cloud.model.AppReviewReply;
import org.bitvault.appstore.cloud.user.admin.service.AdminUserService;
import org.bitvault.appstore.cloud.user.common.GeneralResponseModel;
import org.bitvault.appstore.cloud.user.dev.service.ReviewReplyService;
import org.bitvault.appstore.cloud.utils.Utility;
import org.bitvault.appstore.commons.application.service.AppApplicationService;
import org.bitvault.appstore.commons.application.service.AppRateReviewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = APIConstants.DEV_API_BASE)
public class ReviewReplyController {
	private static final Logger logger = LoggerFactory.getLogger(ReviewReplyController.class);
	@Autowired
	ReviewReplyService reviewReplyService;
	@Autowired
	AppRateReviewService appRateReviewService;
	@Autowired
	AppApplicationService appApplicationService;
	@Autowired
	AdminUserService adminUserService;

	@PostMapping(value = APIConstants.REPLY_ON_REVIEW)
	public ResponseEntity<GeneralResponseModel> saveReviewReply(@RequestBody AppReviewReplydto reviewReplyDto,
			HttpServletRequest request) {

		GeneralResponseModel customResponse = null;
		if (!Utility.isIntegerEmpty(reviewReplyDto.getAppRateReviewId())
				&& !Utility.isStringEmpty(reviewReplyDto.getReplyResponse())) {
			try {
				String userId = getUserIdByAuthRequest(request);

				AppRateReviewDto appreviewdto = appRateReviewService
						.findAppRateReviewByAppRateReviewId(reviewReplyDto.getAppRateReviewId());
				if (appreviewdto != null) {
					if (!Utility.isStringEmpty(appreviewdto.getAppReview())
							&& appreviewdto.getAppReview().length() > 2) {
						Integer applicationId = appreviewdto.getApplicationId();
						AppApplication application = appApplicationService.findApplicationByAppId(applicationId);
						CompareUser(userId, application.getUserId());
						logger.info("User Comparison done");
						AppReviewReply appReviewReply = reviewReplyService
								.findByappRateReviewId(reviewReplyDto.getAppRateReviewId());
						if (appReviewReply == null) {

							reviewReplyDto.setReplyFrom(userId);
							appReviewReply = reviewReplyDto.populateAppReviewReply(reviewReplyDto);
							appReviewReply.setAppRateReview(appreviewdto.populateAppRateReview(appreviewdto));
							reviewReplyDto = reviewReplyService.saveReviewReply(appReviewReply);
							logger.info("reply on review saved successfully");
						} else {
							appReviewReply.setReplyFrom(userId);
							appReviewReply.setReplyResponse(reviewReplyDto.getReplyResponse());
							reviewReplyDto = reviewReplyService.saveReviewReply(appReviewReply);
							// throw new
							// BitVaultException(ErrorMessageConstant.REVIEW_REPLY_ERROR);
							logger.info("reply edited successfully");
						}
						

						customResponse = GeneralResponseModel.of(Constants.SUCCESS, reviewReplyDto);
					} else {

						throw new BitVaultException(ErrorMessageConstant.REVIEW_NOT_EXIT);

					}
				} else {
					customResponse = GeneralResponseModel.of(Constants.FAILED,
							new BitVaultResponse(ErrorMessageConstant.RESULT_NOT_FOUND));

				}

			} catch (BitVaultException e) {
				logger.error("Error occured during reply on review.");
				e.printStackTrace();
				customResponse = GeneralResponseModel.of(Constants.FAILED, new BitVaultResponse(e));
			}
		} else {
			customResponse = GeneralResponseModel.of(Constants.FAILED,
					new BitVaultResponse(ErrorMessageConstant.FIELD_IS_EMPTY));
		}
		return new ResponseEntity<GeneralResponseModel>(customResponse, HttpStatus.OK);
	}

	@DeleteMapping(value = APIConstants.DELETE_REVIEW)
	public ResponseEntity<GeneralResponseModel> deleteReview(@PathVariable Integer appRateReviewId) {
		GeneralResponseModel customResponse = null;
		if (!Utility.isIntegerEmpty(appRateReviewId)) {
			try {

				reviewReplyService.deleteReviewReply(appRateReviewId);
				customResponse = GeneralResponseModel.of(Constants.SUCCESS, new BitVaultResponse(Constants.DELETE));
				logger.info("Review deleted successfully ");

			} catch (BitVaultException e) {
				logger.error("Error occured during review delete process.");
				e.printStackTrace();
				customResponse = GeneralResponseModel.of(Constants.FAILED, new BitVaultResponse(e));
			}
		} else {
			customResponse = GeneralResponseModel.of(Constants.FAILED, new BitVaultResponse(
					new BitVaultException(ErrorMessageConstant.FIELD_IS_EMPTY, ErrorCode.FIELD_IS_EMPTY_CODE)));
		}
		return new ResponseEntity<GeneralResponseModel>(customResponse, HttpStatus.OK);
	}

	private String getUserIdByAuthRequest(HttpServletRequest request) {
		String userId = null;

		try {
			userId = (String) request.getAttribute("userId");

		} catch (Exception e) {
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED, ErrorCode.SOME_ERROR_OCCURED_CODE);
		}
		if (null == userId) {
			throw new BitVaultException(ErrorMessageConstant.USER_NOT_FOUND, ErrorCode.FIELD_IS_EMPTY_CODE);

		}
		return userId;
	}

	private void CompareUser(String userFromRequest, String userFromApplication) {
		if (!userFromRequest.equals(userFromApplication)) {
			AdminUser adminUser = adminUserService.getByUserId(userFromRequest);
			if (adminUser == null) {
				throw new BitVaultException(ErrorMessageConstant.INVALID_APP_USER);
			}
		}
	}
}