package org.bitvault.appstore.cloud.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.OK)
public class BitVaultException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8937026700749628068L;

	private String message;

	public BitVaultException(String message, String status) {
		this.message = message;
	}
	
	public BitVaultException(String message, int errorCode) {
		this.message = message;
	}

	public BitVaultException(String message) {
		super(message);
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	

	@Override
	public String toString() {

		StringBuilder jsonBuffer = new StringBuilder();
		jsonBuffer.append("{");
		jsonBuffer.append("\"message\":").append("\"").append(message).append("\"");
		jsonBuffer.append("}");
		return jsonBuffer.toString();

	}

}
