package org.bitvault.appstore.cloud.user.common;

/**
 * Error model for interacting with client.
 */
public class GeneralResponseModel {

    private final String status;

    private final Object result;

    protected GeneralResponseModel(String status, Object result) {
        this.result = result;
        this.status = status;
    }

    public static GeneralResponseModel of(String status, Object result) {
        return new GeneralResponseModel(status, result);
    }

    public String getStatus() {
        return status;
    }

    public Object getResult() {
        return result;
    }

}
