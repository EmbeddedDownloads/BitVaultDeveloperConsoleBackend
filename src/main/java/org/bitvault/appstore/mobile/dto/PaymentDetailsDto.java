package org.bitvault.appstore.mobile.dto;

public class PaymentDetailsDto {

	private String bitcoinWalletAddress ;
	private String eotcoinWalletAddress ;
	
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
}
