package org.bitvault.appstore.cloud.model;

import java.io.Serializable;
import javax.persistence.*;

import org.apache.commons.beanutils.BeanUtils;
import org.bitvault.appstore.cloud.constant.ErrorCode;
import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.dto.UserActivityDto;
import org.bitvault.appstore.cloud.exception.BitVaultException;

/**
 * The persistent class for the user_activity database table.
 * 
 */
@Entity
@Table(name = "user_activity")
@NamedQuery(name = "UserActivity.findAll", query = "SELECT u FROM UserActivity u")
public class UserActivity extends Auditable<String> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "activity_id", unique = true, nullable = false)
	private int activityId;

	@Column(nullable = true, length = 1)
	private String status;
	
	@Column(name = "user_id", nullable = false, length = 255)
	private String userId;
	
	@Column(name = "username", nullable = false, length = 255)
	private String userName;

	@Column(name = "sub_dev_email", nullable = true, length = 255)
	private String subDevEmail;
	
	@Column(name = "sub_dev_user_id", nullable = true, length = 255)
	private String subDevUserId;
	
	@Column(name = "sub_dev_username", nullable = true, length = 255)
	private String subDevUserName;
	
	@Column(name = "avatarURL", nullable = false, length = 255)
	private String avatarURL;

	@Column(name = "txn_id", nullable = false, length = 255)
	private String txnId;
	
	@Column(name = "amount_paid", nullable = false, length = 255)
	private String amountPaid;
	
	@Column(name = "txn_status", nullable = false, length = 255)
	private String txnStatus;
	
	// bi-directional many-to-one association to UserActivityType
//	@ManyToOne(fetch = FetchType.LAZY)
	
	@OneToOne
	@JoinColumn(name = "activity_type_id", nullable = false)
	private UserActivityType userActivityType;

	@Column(name = "payment_for", length = 255,nullable = true)
	private String paymentFor;
	
	@Column(name = "payment_mode", length = 255,nullable = true)
	private String paymentMode;
	
	
	public String getSubDevUserId() {
		return subDevUserId;
	}

	public void setSubDevUserId(String subDevUserId) {
		this.subDevUserId = subDevUserId;
	}

	public String getSubDevUserName() {
		return subDevUserName;
	}

	public void setSubDevUserName(String subDevUserName) {
		this.subDevUserName = subDevUserName;
	}

	public int getActivityId() {
		return this.activityId;
	}

	public void setActivityId(int activityId) {
		this.activityId = activityId;
	}

	public String getStatus() {
		return this.status;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getAvatarURL() {
		return avatarURL;
	}

	public void setAvatarURL(String avatarURL) {
		this.avatarURL = avatarURL;
	}

	public String getTxnId() {
		return txnId;
	}

	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}

	public String getAmountPaid() {
		return amountPaid;
	}

	public void setAmountPaid(String amountPaid) {
		this.amountPaid = amountPaid;
	}

	public String getTxnStatus() {
		return txnStatus;
	}

	public void setTxnStatus(String txnStatus) {
		this.txnStatus = txnStatus;
	}

	public String getPaymentFor() {
		return paymentFor;
	}

	public void setPaymentFor(String paymentFor) {
		this.paymentFor = paymentFor;
	}

	public String getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public UserActivityType getUserActivityType() {
		return this.userActivityType;
	}

	public void setUserActivityType(UserActivityType userActivityType) {
		this.userActivityType = userActivityType;
	}

	public UserActivityDto populateUserActivityDto(UserActivity userActivity) {

		if (userActivity == null) {
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED, ErrorCode.SOME_ERROR_OCCURED_CODE);
		}
		UserActivityDto userActivityDto = new UserActivityDto();

		try {
			BeanUtils.copyProperties(userActivityDto,userActivity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED, ErrorCode.SOME_ERROR_OCCURED_CODE);
		}

		return userActivityDto;
	}

	public String getSubDevEmail() {
		return subDevEmail;
	}

	public void setSubDevEmail(String subDevEmail) {
		this.subDevEmail = subDevEmail;
	}

	
	
}