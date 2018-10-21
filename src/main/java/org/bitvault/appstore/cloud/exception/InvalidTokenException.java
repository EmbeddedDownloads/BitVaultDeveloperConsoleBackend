package org.bitvault.appstore.cloud.exception;

import org.springframework.security.core.AuthenticationException;

public class InvalidTokenException extends AuthenticationException {

	private static final long serialVersionUID = -7268617531128608140L;

	private String message;
	private int errorCode;

	public InvalidTokenException(String msg) {
		super(msg);
	}

//	public InvalidTokenException(String message, int errorCode) {
//		super
//		this.message = message;
//		this.errorCode = errorCode;
//	}

	@Override
	public String toString() {

		StringBuilder jsonBuffer = new StringBuilder();
		jsonBuffer.append("{");
		jsonBuffer.append("\"message\":").append("\"").append(message).append("\"").append(",");
		jsonBuffer.append("\"errorCode\":").append(errorCode);
		jsonBuffer.append("}");

		return jsonBuffer.toString();
	}

}
