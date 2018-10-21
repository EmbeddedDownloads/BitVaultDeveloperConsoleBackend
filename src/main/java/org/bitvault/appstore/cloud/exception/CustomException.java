package org.bitvault.appstore.cloud.exception;

public class CustomException {

	static final long serialVersionUID = 434264781435l;

	String message;
	int errorCode;

	public CustomException() {
	}

	public CustomException(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	@Override
	public String toString() {
		return "{" + "\"error_message\":\"" + getMessage() + "\"}";
	}
}
