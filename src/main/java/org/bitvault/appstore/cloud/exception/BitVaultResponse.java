package org.bitvault.appstore.cloud.exception;

import java.util.Map;

public class BitVaultResponse {

	private Object message;

	public BitVaultResponse(Map<String, Object> responseMap) {
		this.message = responseMap;

	}

	public BitVaultResponse(BitVaultException e) {
		this.message = e.getMessage();

	}

	public BitVaultResponse(String message) {
		this.message = message;

	}

	public Object getMessage() {
		return message;
	}

}
