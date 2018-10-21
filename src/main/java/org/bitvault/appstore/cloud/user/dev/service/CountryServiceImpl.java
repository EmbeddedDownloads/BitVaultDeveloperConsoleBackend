package org.bitvault.appstore.cloud.user.dev.service;

import java.util.List;

import org.bitvault.appstore.cloud.model.Country;
import org.bitvault.appstore.cloud.user.dev.dao.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CountryServiceImpl implements CountryService{
	
	@Autowired
	CountryRepository countryRepository;

	@Override
	public List<Country> getListOfCountries() {
		return countryRepository.findAll(new Sort(Direction.ASC, "cId"));
	}

	@Override
	public Country getListOfStates(int id) {
		return countryRepository.findOne(id);
	}

}
