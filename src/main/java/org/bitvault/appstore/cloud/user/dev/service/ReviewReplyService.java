package org.bitvault.appstore.cloud.user.dev.service;

import org.bitvault.appstore.cloud.dto.AppReviewReplydto;
import org.bitvault.appstore.cloud.model.AppReviewReply;

public interface ReviewReplyService {
	AppReviewReply findByappRateReviewId(Integer appRateReviewId);

	AppReviewReplydto saveReviewReply(AppReviewReply appReviewReply);

	void deleteReviewReply(Integer appRateReviewId);
}
