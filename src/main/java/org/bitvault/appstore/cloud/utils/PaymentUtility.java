package org.bitvault.appstore.cloud.utils;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.bitvault.appstore.cloud.constant.Constants;
import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.exception.InvalidTokenException;
import org.bitvault.appstore.cloud.model.DevPayment;
import org.bitvault.appstore.cloud.user.common.service.DevPaymentService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

public class PaymentUtility {

	public static void processPayment(DevPaymentService devPaymentService, HttpServletRequest request,
			Map<String, Object> inputMap) {
		try {
			String userId = (String) inputMap.get(Constants.USERID);
			DevPayment devPayment = devPaymentService.findByUserIdAndPaymentFor(userId, Constants.SELF,
					new PageRequest(0, 1, new Sort(Direction.DESC, Constants.CREATED_AT))).getContent().get(0);
			if (devPayment == null) {
				throw new InvalidTokenException(ErrorMessageConstant.USER_NOT_FOUND);
			}
			if (!devPayment.getTxnStatus().equalsIgnoreCase(Constants.SUCCESS)) {

				String txnId = (String) inputMap.get("txnId");
				Double amountPaid = (Double) inputMap.get("amount");
				if (Utility.isStringEmpty(txnId) || Utility.isDoubleEmpty(amountPaid)) {
					throw new InvalidTokenException("Invalid amount or txnId");
				}
				double amountPayble = devPayment.getPayment();
				if ((amountPaid - amountPayble) < 0) {
					throw new InvalidTokenException(ErrorMessageConstant.INSUFFICIENT_AMOUNT);
				}
				devPayment.setTxnId(txnId);
				devPayment.setPayment(amountPaid);
				devPayment.setTxnStatus(Constants.SUCCESS);
				devPaymentService.savePaymentRecord(devPayment);
				request.setAttribute(Constants.PAY_CHECK, "done");
			} else {
				request.setAttribute(Constants.PAY_CHECK, "Already done.");
			}
		} catch (Exception e) {
			throw new InvalidTokenException(ErrorMessageConstant.ERROR_OCCURED_DURING_PAYMENT_UPDATE);
		}
	}
}
