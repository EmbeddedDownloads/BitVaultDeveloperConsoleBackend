package org.bitvault.appstore.cloud.user.dev.service;

import javax.transaction.Transactional;

import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.dto.AppReviewReplydto;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.bitvault.appstore.cloud.model.AppRateReview;
import org.bitvault.appstore.cloud.model.AppReviewReply;
import org.bitvault.appstore.cloud.user.dev.dao.ReviewReplyRepository;
import org.bitvault.appstore.commons.application.dao.AppRateReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ReviewReplyServiceImpl implements ReviewReplyService {
	@Autowired
	ReviewReplyRepository reviewReplyRepository;
	@Autowired
	AppRateReviewRepository appRateReviewRespository;

	@Override
	public AppReviewReply findByappRateReviewId(Integer appRateReviewId) {
		AppReviewReply appReply = reviewReplyRepository.findByappRateReviewId(appRateReviewId);
		return appReply;

	}

	@Override
	public AppReviewReplydto saveReviewReply(AppReviewReply appReviewReply) {

		AppReviewReplydto appReviewReplyDto = null;
		try {
			appReviewReply = reviewReplyRepository.saveAndFlush(appReviewReply);

			appReviewReplyDto = appReviewReply.populateAppReviewReply(appReviewReply);
			appReviewReplyDto.setAppRateReviewId(appReviewReply.getAppRateReview().getAppRateReviewId());
			// appReviewReplyDto.setCreatedBy(appReviewReply.getCreatedBy());
			// appReviewReplyDto.setUpdatedBy(appReviewReply.getUpdatedBy());
			
		} catch (Exception e) {

			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);
		}
		return appReviewReplyDto;
	}

	@Override
	public void deleteReviewReply(Integer appRateReviewId) {
		try {
			AppRateReview appRateReview = appRateReviewRespository.findOne(appRateReviewId);
			if (null == appRateReview) {
				throw new BitVaultException(ErrorMessageConstant.RESULT_NOT_FOUND);
			}

			AppReviewReply appReply = reviewReplyRepository.findByappRateReviewId(appRateReviewId);

			if (appReply != null) {
				reviewReplyRepository.delete(appReply.getAppReviewReplyId());
				// appRateReviewRespository.delete(appRateReviewId);

			}

			appRateReview.setAppReview("");
			appRateReviewRespository.saveAndFlush(appRateReview);
		} catch (Exception e) {

			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);
		}
	}
}
