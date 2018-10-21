package org.bitvault.appstore.cloud.user.dev.service;

import java.util.List;

import org.bitvault.appstore.cloud.model.Country;

public interface CountryService {
	List<Country> getListOfCountries(); 
	Country getListOfStates(int id);
}
