package org.bitvault.appstore.cloud.user.common;

/**
 * Error model for interacting with client.
 */
public class GeneralNotificationResponseModel {

	    private String message;

	    private boolean status ;

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public boolean isStatus() {
			return status;
		}

		public void setStatus(boolean status) {
			this.status = status;
		}

	
}
