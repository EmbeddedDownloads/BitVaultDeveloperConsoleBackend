package org.bitvault.appstore.cloud.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.dto.TokenDto;
import org.bitvault.appstore.cloud.dto.TokenPaymentDto;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "token")
@EntityListeners(AuditingEntityListener.class)
public class Token implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "token_id", unique = true, nullable = false)
	private String tokenId;

	@Column(name = "avatar_url", nullable = false, length = 255)
	private String avatarURL;

	@Column(name = "avatar_name", nullable = false, length = 255)
	private String avatarName;

	@Column(name = "access_token", nullable = false, length = 255)
	private String accessToken;

	@Column(name = "refresh_token", nullable = false, length = 255)
	private String refreshToken;

	@Column(name = "login_type", nullable = false, length = 255)
	private String loginType;

	@Column(name = "payment_status", length = 255)
	private String paymentStatus;

	@Column(name = "payble_amount", length = 255)
	private double paybleAmount;

	@Column(name = "user_id", nullable = false, length = 255)
	private String userId;

	@CreatedDate
	@Temporal(TemporalType.TIMESTAMP)
	@JsonProperty("created_at")
	@Column(name = "created_at")
	private Date createdAt;

	@LastModifiedDate
	@Temporal(TemporalType.TIMESTAMP)
	@JsonProperty("updated_at")
	@Column(name = "updated_at")
	private Date updatedAt;

	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	public String getAvatarURL() {
		return avatarURL;
	}

	public void setAvatarURL(String avatarURL) {
		this.avatarURL = avatarURL;
	}

	public String getAvatarName() {
		return avatarName;
	}

	public void setAvatarName(String avatarName) {
		this.avatarName = avatarName;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public String getLoginType() {
		return loginType;
	}

	public void setLoginType(String loginType) {
		this.loginType = loginType;
	}

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public double getPaybleAmount() {
		return paybleAmount;
	}

	public void setPaybleAmount(double paybleAmount) {
		this.paybleAmount = paybleAmount;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result + ((tokenId == null || tokenId.trim().isEmpty()) ? 0 : tokenId.hashCode()));
		return result;
	}

	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (!(obj instanceof Token))
			return false;
		Token equalCheck = (Token) obj;
		if ((tokenId == null && equalCheck.tokenId != null) || (tokenId != null && equalCheck.tokenId == null))
			return false;
		if (tokenId != null && !tokenId.equals(equalCheck.tokenId))
			return false;
		return true;
	}

	public TokenDto populateTokenDto(Token token) {
		try {
			TokenDto tokenDto = new TokenDto();
			BeanUtils.copyProperties(token, tokenDto);
			return tokenDto;

		} catch (Exception e) {
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);
		}
	}

	public TokenPaymentDto populateTokenPaymentDto(Token token) {
		try {
			TokenPaymentDto tokenDto = new TokenPaymentDto();
			BeanUtils.copyProperties(token, tokenDto);
			return tokenDto;

		} catch (Exception e) {
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);
		}
	}
}
