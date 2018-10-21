package org.bitvault.appstore.cloud.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "countries")
public class Country {
	
	@Id
	@Column(name = "country_id")
	private Integer cId;
	
	String sortname;
	
	String name;
	
	@OneToMany(mappedBy = "country")
	List<States> states = new ArrayList<States>();
	
	public Integer getcId() {
		return cId;
	}

	public void setcId(Integer cId) {
		this.cId = cId;
	}

	public List<States> getStates() {
		return states;
	}

	public void setStates(List<States> states) {
		this.states = states;
	}

	public String getSortname() {
		return sortname;
	}

	public void setSortname(String sortname) {
		this.sortname = sortname;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}	
}
