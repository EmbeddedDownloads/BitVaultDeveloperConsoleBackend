package org.bitvault.appstore.cloud.security.auth.filter;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginRequest {
    private String username;
    private String password;
    
    private String loginType;

    public String getLoginType() {
		return loginType;
	}

	@JsonCreator
    public LoginRequest(@JsonProperty("username") String username, @JsonProperty("password") String password, @JsonProperty("loginType") String loginType) {
        this.username = username;
        this.password = password;
        this.loginType = loginType;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
