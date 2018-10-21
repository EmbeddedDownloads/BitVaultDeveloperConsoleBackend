package org.bitvault.appstore.cloud.paypal;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bitvault.appstore.cloud.constant.APIConstants;
import org.bitvault.appstore.cloud.constant.Constants;
import org.bitvault.appstore.cloud.constant.ErrorCode;
import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.bitvault.appstore.cloud.exception.BitVaultResponse;
import org.bitvault.appstore.cloud.mail.MailService;
import org.bitvault.appstore.cloud.model.DevPayment;
import org.bitvault.appstore.cloud.model.DevUser;
import org.bitvault.appstore.cloud.model.SubDevReq;
import org.bitvault.appstore.cloud.model.UserActivityType;
import org.bitvault.appstore.cloud.user.common.GeneralResponseModel;
import org.bitvault.appstore.cloud.user.common.service.DevPaymentService;
import org.bitvault.appstore.cloud.user.dev.service.DevUserService;
import org.bitvault.appstore.cloud.user.dev.service.SubDevReqService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

@PropertySource("applications-${spring.profiles.active}.properties")
@RestController
@RequestMapping(value = APIConstants.MOBILE_API_BASE)
public class PaypalController {

	private final Logger logger = LoggerFactory.getLogger(PaypalController.class);

	@Autowired
	private PaypalService paypalService;

	@Autowired
	private DevPaymentService devPaymentService;

	@Autowired
	private DevUserService devUserService;

	@Autowired
	private SubDevReqService subDevReqService;

	@Autowired
	private MailService mailService;

	private final String PAY_URL_SUCCESS_FAILED = "#/thanks?txnId=";
	private final String CANCEL_URL = "#/thanks?txnId=";

	@Value("${server.host}")
	private String host;

	@RequestMapping(method = RequestMethod.POST, value = { APIConstants.PAYPAL, APIConstants.PAYPAL + "/" })
	public ResponseEntity<GeneralResponseModel> pay(HttpServletRequest request,
			@RequestBody Map<String, String> requestBody) {
		String apiHost = host;
		if (!apiHost.contains(":8081"))
			apiHost = apiHost.replaceAll("/$", ":8081/");

		// if(apiHost.contains("52.10.154.132")) {
		// apiHost = apiHost.replace("52.10.154.132", "192.168.11.85");
		// }

		DevPayment devPaymentModel = null;
		GeneralResponseModel customResponse = null;
		String userId = requestBody.get(Constants.USERID);

		String payableAmount = "";

		if (requestBody.get("paybleAmount") != null)
			payableAmount = requestBody.get("paybleAmount");

		try {
			devPaymentModel = getDevPaymentModel(request.getRequestURI(), userId, payableAmount);
			if (devPaymentModel.getTxnStatus().equalsIgnoreCase(Constants.PENDING)
					|| devPaymentModel.getTxnStatus().equalsIgnoreCase(Constants.FAILED)) {

				if (devPaymentModel.getPayment() != 0) {
					String successUrl = APIConstants.PAYPAL_SUCCESS_URL + "/" + userId + "/" + payableAmount;
					String cancelUrl = APIConstants.PAYPAL_CANCEL_URL + "/" + userId;
					if (request.getRequestURI().endsWith("/")) {
						successUrl = successUrl + "/";
						cancelUrl = cancelUrl + "/";
					}
					Payment payment = paypalService.createPayment(devPaymentModel.getPayment(), Constants.USD,
							PaypalPaymentMethod.paypal, PaypalPaymentIntent.sale, Constants.PAYMENT_DESCRIPTION,
							apiHost + APIConstants.SERVER_URL + APIConstants.MOBILE_API_BASE + cancelUrl,
							apiHost + APIConstants.SERVER_URL + APIConstants.MOBILE_API_BASE + successUrl);

					for (Links links : payment.getLinks()) {
						if (links.getRel().equals("approval_url")) {
							Map<String, String> responseMap = new HashMap<>();
							responseMap.put("paymentURL", links.getHref());
							customResponse = GeneralResponseModel.of(Constants.SUCCESS, responseMap);
						}
					}

				} else {
					throw new BitVaultException(ErrorMessageConstant.AMOUNT_CANNOT_BE_ZERO);
				}

			} else {
				throw new BitVaultException(ErrorMessageConstant.NOT_ELIGBLE_FOR_PAYMENT);
			}
		} catch (BitVaultException e) {
			customResponse = GeneralResponseModel.of(Constants.FAILED, new BitVaultResponse(e.getMessage()));
		} catch (PayPalRESTException e) {
			e.printStackTrace();
			customResponse = GeneralResponseModel.of(Constants.FAILED, new BitVaultResponse(
					new BitVaultException(ErrorMessageConstant.FIELD_IS_EMPTY, ErrorCode.FIELD_IS_EMPTY_CODE)));
		}
		return new ResponseEntity<GeneralResponseModel>(customResponse, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET, value = APIConstants.PAYPAL_CANCEL_URL + "/{user_id}")
	public void cancelPay(@PathVariable(value = "user_id") String userId, HttpServletResponse response) {

		try {
			response.sendRedirect(host + CANCEL_URL);
		} catch (Exception e) {

		}

	}

	@RequestMapping(method = RequestMethod.GET, value = {
			APIConstants.PAYPAL_SUCCESS_URL + "/{user_id}" + "/{payableAmount}" })
	public void successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId,
			@PathVariable(value = "user_id") String userId, @PathVariable(value = "payableAmount") String payableAmount,
			HttpServletResponse response, HttpServletRequest request) {

		DevPayment devPaymentModel = getDevPaymentModel(request.getRequestURI(), userId, payableAmount);
		Payment payment = null;
		request.setAttribute(Constants.USERID, userId);
		if (devPaymentModel == null) {
			throw new BitVaultException(ErrorMessageConstant.RESULT_NOT_FOUND);
		}
		String txnId = null;
		try {
			payment = paypalService.executePayment(paymentId, payerId);
			if (payment != null) {

				Map<String, String> dataMap = new HashMap<>();
				if (payment.getState().equals("approved")) {
					txnId = payment.getTransactions().get(0).getRelatedResources().get(0).getSale().getId();
					devPaymentModel.setTxnId(txnId);
					devPaymentModel.setPaymentMode("PayPal");
					;
					devPaymentModel.setTxnStatus(Constants.SUCCESS);
					devPaymentModel
							.setAmountPaid(Double.parseDouble(payment.getTransactions().get(0).getAmount().getTotal()));
					devPaymentModel.setUpdatedBy(userId);

					devPaymentService.savePaymentRecord(devPaymentModel);
					dataMap.put(Constants.PAYMENT_FOR, Constants.SELF);
					DevUser devUser = devUserService.findByUserId(userId);
					if (request.getRequestURI().endsWith("/")) {

						SubDevReq subDevReq = subDevReqService.findByDevPayment(devPaymentModel);
						// List<SubDevReq> listSubDevReq = subDevReqService
						// .allSubDevRequestsForUser(userId, Arrays.asList(Constants.APPROVED),
						// new PageRequest(0, 1, new Sort(Direction.DESC, Constants.CREATED_AT)))
						// .getContent();
						if (subDevReq == null) {
							throw new BitVaultException(ErrorMessageConstant.RESULT_NOT_FOUND);
						}
						devUser.setChildCount(devUser.getChildCount() + subDevReq.getChildCount());
						subDevReqService.saveSubDevRequest(subDevReq);
						devUserService.saveUser(devUser);
						dataMap.put(Constants.PAYMENT_FOR, Constants.SUBDEV);
					}

					response.sendRedirect(host + PAY_URL_SUCCESS_FAILED + txnId);

					dataMap.put(Constants.PAYMENT_STATUS, Constants.SUCCESS);
					dataMap.put(Constants.PAYMENT_MODE, Constants.PAYPAL);
					dataMap.put(Constants.PAYMENT_TXN_ID, txnId);
					dataMap.put(Constants.AMOUNT_PAID,
							Double.parseDouble(payment.getTransactions().get(0).getAmount().getTotal()) + "");
					dataMap.put(Constants.USERID, userId);
					dataMap.put(Constants.USER_NAME, devUser.getUsername());

					trackAvatarForUserAuditing(dataMap);

				} else if (payment.getState().equals("failed")) {
					devPaymentModel.setTxnId(txnId);
					devPaymentModel.setUpdatedBy(userId);
					devPaymentModel.setTxnStatus(Constants.FAILED);
					devPaymentService.savePaymentRecord(devPaymentModel);
					response.sendRedirect(host + PAY_URL_SUCCESS_FAILED + txnId);
				}
			}
		} catch (Exception e) {
			try {
				response.sendRedirect(host + PAY_URL_SUCCESS_FAILED + txnId);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);
		}
	}

	@GetMapping(value = APIConstants.PAYPAL_DETAILS)
	public ResponseEntity<?> getPaymentDetails(@PathVariable String txnId, @RequestParam(required = false) String iam) {
		try {
			DevPayment devPayment = null;
			List<DevPayment> devPaymentList = devPaymentService
					.findByTxnId(txnId, iam, new PageRequest(0, 1, new Sort(Direction.DESC, Constants.CREATED_AT)))
					.getContent();
			if (!devPaymentList.isEmpty()) {
				devPayment = devPaymentList.get(0);
				DevUser devUser = devUserService.findByUserId(devPayment.getUserId());
				mailService.sendMail(devUser.getEmail(), "Transaction details: Bitvault AppStore",
						"Dear User,<br><br/>Below is your transaction details:<br><br/>" + Constants.TXN_MAIL_1
								+ devPayment.getTxnId() + Constants.TXN_MAIL_2 + devPayment.getAmountPaid() + " USD"
								+ Constants.TXN_MAIL_3 + devPayment.getTxnStatus().toUpperCase() + Constants.TXN_MAIL_4
								+ devPayment.getUpdatedAt() + Constants.TXN_MAIL_5 + Constants.MAIL_SIGN);
				return ResponseEntity
						.ok(GeneralResponseModel.of(Constants.SUCCESS, devPayment.populateDto(devPayment)));
			} else
				return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED,
						new BitVaultResponse(ErrorMessageConstant.RESULT_NOT_FOUND)));
		} catch (Exception e) {
			logger.error(e.getMessage());
			return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED,
					new BitVaultResponse(ErrorMessageConstant.SOME_ERROR_OCCURED)));
		}
	}

	private DevPayment getDevPaymentModel(String requestUri, String userId, String payableAmount) {
		List<DevPayment> devPaymentList = null;
		DevPayment devPaymentModel = null;
		try {
			if (requestUri.endsWith("/")) {
				devPaymentList = devPaymentService.findByUserIdAndPaymentFor(userId, Constants.SUBDEV,
						new PageRequest(0, 5, new Sort(Direction.DESC, Constants.CREATED_AT))).getContent();
				if (devPaymentList.isEmpty()) {
					throw new BitVaultException(ErrorMessageConstant.RESULT_NOT_FOUND);
				}

				for (int i = 0; i < devPaymentList.size(); i++) {
					if ((devPaymentList.get(i).getPayment() == Double.parseDouble(payableAmount))
							&& !devPaymentList.get(i).getTxnStatus().equalsIgnoreCase("SUCCESS")) {
						devPaymentModel = devPaymentList.get(i);
						break;
					}
				}

				// devPaymentModel = devPaymentList.get(0);
			} else {
				devPaymentList = devPaymentService.findByUserIdAndPaymentFor(userId, Constants.SELF,
						new PageRequest(0, 1, new Sort(Direction.DESC, Constants.CREATED_AT))).getContent();
				if (devPaymentList.isEmpty()) {
					throw new BitVaultException(ErrorMessageConstant.RESULT_NOT_FOUND);
				}
				devPaymentModel = devPaymentList.get(0);
			}
			return devPaymentModel;
		} catch (Exception e) {
			logger.error("Error during making or paying the amount");
			throw new BitVaultException(e.getMessage());
		}

	}

	private DevPayment getDevPaymentModelGoUrl(String order, String userId, String payableAmount) {
		List<DevPayment> devPaymentList = null;
		DevPayment devPaymentModel = null;
		try {
			if (!order.equals("signuppageNewUser")) {
				devPaymentList = devPaymentService.findByUserIdAndPaymentFor(userId, Constants.SUBDEV,
						new PageRequest(0, 5, new Sort(Direction.DESC, Constants.CREATED_AT))).getContent();
				if (devPaymentList.isEmpty()) {
					throw new BitVaultException(ErrorMessageConstant.RESULT_NOT_FOUND);
				}
				for (int i = 0; i < devPaymentList.size(); i++) {
					if ((devPaymentList.get(i).getPayment() == Double.parseDouble(payableAmount))
							&& !devPaymentList.get(i).getTxnStatus().equalsIgnoreCase("SUCCESS")) {
						devPaymentModel = devPaymentList.get(i);
						break;
					}
				}
			} else {
				devPaymentList = devPaymentService.findByUserIdAndPaymentFor(userId, Constants.SELF,
						new PageRequest(0, 1, new Sort(Direction.DESC, Constants.CREATED_AT))).getContent();
				if (devPaymentList.isEmpty()) {
					throw new BitVaultException(ErrorMessageConstant.RESULT_NOT_FOUND);
				}
				devPaymentModel = devPaymentList.get(0);
			}
			return devPaymentModel;
		} catch (Exception e) {
			logger.error("Error during making or paying the amount");
			throw new BitVaultException(e.getMessage());
		}

	}

	/**
	 * Method used to track user auditing
	 * 
	 * @param userId
	 * @param userName
	 */
	public void trackAvatarForUserAuditing(Map<String, String> dataMap) {

		try {
			UserActivityType userActivityType = new UserActivityType();
			userActivityType.setActivityType(Constants.PAYMENT);

			devUserService.saveUserActivity(userActivityType, dataMap);
		} catch (Exception e) {
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);
		}
	}

	@PostMapping(value = APIConstants.GOURL_PAY)
	public String goUrlPay(HttpServletRequest request, @RequestParam String tx, @RequestParam String order,
			@RequestParam String status, @RequestParam String amountusd, @RequestParam String user) {

		logger.info("GoURl payment api called");

		String userId = user;
		DevPayment devPaymentModel = null;
		Map<String, String> dataMap = new HashMap<>();

		try {

			if (userId == null) {
				throw new BitVaultException(ErrorMessageConstant.USER_NOT_FOUND);
			}

			DevUser devUser = devUserService.findByUserId(userId);

			if (devUser == null) {
				throw new BitVaultException(ErrorMessageConstant.USER_NOT_FOUND);
			}

			logger.info("Status received is " + status);

			if (status != null && status.equalsIgnoreCase(Constants.PAYMENT_RECEIVED)) {

				if (order != null) {

					devPaymentModel = getDevPaymentModelGoUrl(order, userId, amountusd);

					if (!devPaymentModel.getTxnStatus().equalsIgnoreCase(Constants.SUCCESS)) {

						devPaymentModel.setAmountPaid(Double.parseDouble(amountusd));
						devPaymentModel.setPaymentMode(Constants.GOURL);
						devPaymentModel.setTxnId(tx);
						devPaymentModel.setTxnStatus(Constants.SUCCESS);
						devPaymentService.savePaymentRecord(devPaymentModel);
						dataMap.put(Constants.PAYMENT_FOR, Constants.SELF);
						if (!order.equals("signuppageNewUser")) {
							SubDevReq subDevReq = subDevReqService.findByDevPayment(devPaymentModel);

							if (subDevReq == null) {
								throw new BitVaultException(ErrorMessageConstant.RESULT_NOT_FOUND);
							}
							devUser.setChildCount(devUser.getChildCount() + subDevReq.getChildCount());
							subDevReqService.saveSubDevRequest(subDevReq);
							devUserService.saveUser(devUser);
							dataMap.put(Constants.PAYMENT_FOR, Constants.SUBDEV);
						}

						logger.info("Record successfully saved for " + order);

						dataMap.put(Constants.PAYMENT_STATUS, Constants.SUCCESS);
						dataMap.put(Constants.PAYMENT_MODE, Constants.GOURL);
						dataMap.put(Constants.PAYMENT_TXN_ID, tx);
						dataMap.put(Constants.AMOUNT_PAID, amountusd);
						dataMap.put(Constants.USERID, userId);
						dataMap.put(Constants.USER_NAME, devUser.getUsername());

						trackAvatarForUserAuditing(dataMap);

					} else {
						throw new BitVaultException(ErrorMessageConstant.NOT_ELIGBLE_FOR_PAYMENT);
					}
				}
			} else {
				return "Only POST Data Allowed";
			}

		} catch (BitVaultException e) {
			e.printStackTrace();
			logger.info("Exception occcured " + e.getMessage());
			return "Only POST Data Allowed";
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.info("Exception occcured " + ex.getMessage());
			return "Only POST Data Allowed";
		}
		return "cryptobox_newrecord";
	}

	@GetMapping(value = APIConstants.GOURL_PAY)
	public String payGoUrl() {
		return "Only POST Data Allowed";
	}

}
