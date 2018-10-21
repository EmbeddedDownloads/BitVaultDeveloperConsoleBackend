package org.bitvault.appstore.cloud.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.bitvault.appstore.cloud.constant.Constants;
import org.bitvault.appstore.cloud.dto.DevPaymentDto;
import org.springframework.beans.BeanUtils;


/**
 * The persistent class for the dev_payment database table.
 * 
 */
@Entity
@Table(name="dev_payment")
@NamedQuery(name="DevPayment.findAll", query="SELECT d FROM DevPayment d")
public class DevPayment extends Auditable<String> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="dev_payment_id", unique=true, nullable=false)
	private Integer devPaymentId;

	public Integer getDevPaymentId() {
		return devPaymentId;
	}


	public void setDevPaymentId(Integer devPaymentId) {
		this.devPaymentId = devPaymentId;
	}

	@Column(nullable=false)
	private double payment;

	@Column(name="txn_id", nullable=false)
	private String txnId;

	@Column(name="txn_status", nullable=false, length=1)
	private String txnStatus;
	
	@Column(name="payment_for")
	private String paymentFor = Constants.SELF;
	
	@Column(name="payment_mode")
	private String paymentMode = "";

	public String getPaymentMode() {
		return paymentMode;
	}


	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}


	public String getPaymentFor() {
		return paymentFor;
	}

	public void setPaymentFor(String paymentFor) {
		this.paymentFor = paymentFor;
	}

	@Column(name="user_id", nullable=false, length=255)
	private String userId;
	
	@Column(name = "amount_paid")
	private double amountPaid;

	public double getAmountPaid() {
		return amountPaid;
	}


	public void setAmountPaid(double amountPaid) {
		this.amountPaid = amountPaid;
	}


	public DevPayment() {
	}	

	public double getPayment() {
		return payment;
	}


	public void setPayment(double payment) {
		this.payment = payment;
	}
	
	public String getTxnId() {
		return txnId;
	}


	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}


	public String getTxnStatus() {
		return this.txnStatus;
	}

	public void setTxnStatus(String txnStatus) {
		this.txnStatus = txnStatus;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}


	public DevPaymentDto populateDto(DevPayment devPaymentModel) {
		
		DevPaymentDto devPaymentDto = new DevPaymentDto() ;
		try {
		BeanUtils.copyProperties(devPaymentModel, devPaymentDto);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return devPaymentDto ;
		
		
	}

}