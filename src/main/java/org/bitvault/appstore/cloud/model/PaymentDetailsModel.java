package org.bitvault.appstore.cloud.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.bitvault.appstore.cloud.model.Auditable;
import org.bitvault.appstore.mobile.dto.PaymentDetailsDto;
import org.springframework.beans.BeanUtils;

@Entity
@Table(name = "payment_details")
public class PaymentDetailsModel extends Auditable<String>{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "payment_detail_id", nullable = false , unique = true)
	private int paymentDetailId ;
	
	@Column(name = "bitcoin_wallet_address" , nullable = true)
	private String bitcoinWalletAddress ;
	
	
	@Column(name = "eotcoin_wallet_address" , nullable = true)
	private String eotcoinWalletAddress ;


	public int getPaymentDetailId() {
		return paymentDetailId;
	}


	public void setPaymentDetailId(int paymentDetailId) {
		this.paymentDetailId = paymentDetailId;
	}


	public String getBitcoinWalletAddress() {
		return bitcoinWalletAddress;
	}


	public void setBitcoinWalletAddress(String bitcoinWalletAddress) {
		this.bitcoinWalletAddress = bitcoinWalletAddress;
	}


	public String getEotcoinWalletAddress() {
		return eotcoinWalletAddress;
	}


	public void setEotcoinWalletAddress(String eotcoinWalletAddress) {
		this.eotcoinWalletAddress = eotcoinWalletAddress;
	}
	
	public PaymentDetailsDto populateModel(PaymentDetailsModel paymentDetailsModel) {
		
		PaymentDetailsDto paymentDetailDto = new PaymentDetailsDto();
		
		if(paymentDetailsModel == null) {
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);
		}
		
		BeanUtils.copyProperties(paymentDetailsModel, paymentDetailDto);
		
		return paymentDetailDto;
		
	}
	
	
}
