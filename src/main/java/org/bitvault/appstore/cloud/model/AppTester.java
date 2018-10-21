package org.bitvault.appstore.cloud.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.dto.AppTesterDto;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name="app_tester")
public class AppTester extends Auditable<String> implements Serializable
{
	private static final long serialVersionUID = 1L;
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name="app_tester_id",nullable=false,unique =true)
private Integer appTesterId;
@Column(name="public_addresses")
private String publicAddress;
@Column(name="package_name")
private String packageName;
	
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "app_application_id", nullable = false)
@JsonIgnore
@JsonManagedReference("AppApplication-AppTester")
private AppApplication application;

public Integer getAppTesterId() {
	return appTesterId;
}

public void setAppTesterId(Integer appTesterId) {
	this.appTesterId = appTesterId;
}

public String getPublicAddress() {
	return publicAddress;
}

public void setPublicAddress(String publicAddress) {
	this.publicAddress = publicAddress;
}

public String getPackageName() {
	return packageName;
}

public void setPackageName(String packageName) {
	this.packageName = packageName;
}

public AppApplication getApplication() {
	return application;
}

public void setApplication(AppApplication application) {
	this.application = application;
}
	
	
public AppTesterDto populateAppTesterDto(AppTester appTester) {
	AppTesterDto appTesterDto = new AppTesterDto();
	try {
		BeanUtils.copyProperties(appTester, appTesterDto);
		appTesterDto.setCreatedAt(appTester.getCreatedAt());
		appTesterDto.setUpdatedAt(appTester.getUpdatedAt());
		appTesterDto.setAppApplicationId(appTester.getApplication().getAppApplicationId());
		

	} catch (Exception e) {
		throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);
	}
	return appTesterDto;

	
}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
