package org.bitvault.appstore.cloud.security.auth.jwt.model;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;

public class UserContext {
    private final String username;
    private final String avatar;
    private final String avatarName;
    private final List<GrantedAuthority> authorities;
    private String userId;
    private String paymentStatus;
    private double paybleAmount;
    private String loginType;

	public String getLoginType() {
		return loginType;
	}

	public void setLoginType(String loginType) {
		this.loginType = loginType;
	}

	public double getPaybleAmount() {
		return paybleAmount;
	}

	public void setPaybleAmount(double paybleAmount) {
		this.paybleAmount = paybleAmount;
	}

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	private UserContext(String username, List<GrantedAuthority> authorities, String avatar, String avatarName) {
        this.username = username;
        this.authorities = authorities;
        this.avatar = avatar;
        this.avatarName = avatarName;
    }
    
    public static UserContext create(String username, List<GrantedAuthority> authorities, String avatar, String avatarName) {
        if (StringUtils.isBlank(username)) throw new IllegalArgumentException("Username is blank: " + username);
        return new UserContext(username, authorities, avatar, avatarName);
    }

    public String getAvatar() {
		return avatar;
	}

	public String getAvatarName() {
		return avatarName;
	}

	public String getUsername() {
        return username;
    }

    public List<GrantedAuthority> getAuthorities() {
        return authorities;
    }
}
