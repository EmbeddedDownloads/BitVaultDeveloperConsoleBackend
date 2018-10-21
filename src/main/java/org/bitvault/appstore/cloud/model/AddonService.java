package org.bitvault.appstore.cloud.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "addon_service")
public class AddonService extends Auditable<String>{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "addon_service_id")
	private Integer addonServiceId;
	
	@Column(name = "addon_service_name")
	private String addonServiceName;

	public Integer getAddonServiceId() {
		return addonServiceId;
	}

	public void setAddonServiceId(Integer addonServiceId) {
		this.addonServiceId = addonServiceId;
	}

	public String getAddonServiceName() {
		return addonServiceName;
	}

	public void setAddonServiceName(String addonServiceName) {
		this.addonServiceName = addonServiceName;
	}
	
}
