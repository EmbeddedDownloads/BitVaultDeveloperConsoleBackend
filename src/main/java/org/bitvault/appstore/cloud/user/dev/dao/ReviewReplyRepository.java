package org.bitvault.appstore.cloud.user.dev.dao;

import org.bitvault.appstore.cloud.model.AppReviewReply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewReplyRepository extends JpaRepository<AppReviewReply,Integer>
{

@Query("Select rep From AppReviewReply rep where rep.appRateReview.appRateReviewId=?1 ")
	 AppReviewReply findByappRateReviewId(Integer appRateReviewId) ;

@Modifying
@Query("delete  From AppReviewReply rep where rep.appRateReview.appRateReviewId=?1 ")
void deleteByappRateReviewId(Integer appRateReviewId) ;

}
