package org.bitvault.appstore.cloud.user.dev.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bitvault.appstore.cloud.constant.APIConstants;
import org.bitvault.appstore.cloud.constant.Constants;
import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.dto.CountryDto;
import org.bitvault.appstore.cloud.model.Country;
import org.bitvault.appstore.cloud.user.common.GeneralResponseModel;
import org.bitvault.appstore.cloud.user.dev.service.CountryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(APIConstants.COMMON_API_BASE)
public class CountryController {

	@Autowired
	private CountryService countryService;
	
	private Map<String, Object> countryResult = null;
	
	@GetMapping(value = APIConstants.GET_COUNTRIES)
	public ResponseEntity<?> getCountriesList(){
		countryResult = new HashMap<String, Object>();
		try {
			List<CountryDto> countries = new ArrayList<>();
			for(Country country : countryService.getListOfCountries()){
				CountryDto dto = new CountryDto();
				BeanUtils.copyProperties(country, dto);
				countries.add(dto);
			}		
//		countryResult.put(Constants.MESSAGE, countries);
			return ResponseEntity.ok(GeneralResponseModel.of(Constants.SUCCESS, countries));
		} catch (BeansException e) {
			countryResult.put(Constants.MESSAGE, ErrorMessageConstant.UNABLE_TO_FETCH);
			return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED, countryResult));
		}
	}
	
	@GetMapping(value = APIConstants.GET_COUNTRY_STATES)
	public ResponseEntity<?> getStatesList(@PathVariable int id){
		countryResult = new HashMap<String, Object>();
		try {
			Country countryWithStates = countryService.getListOfStates(id);
			if(countryWithStates == null){
				countryResult.put(Constants.MESSAGE, ErrorMessageConstant.INVALID_COUNTRY_CODE);
				return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED, countryResult));
			}
				return ResponseEntity.ok(GeneralResponseModel.of(Constants.SUCCESS, countryWithStates));
		} catch (Exception e) {
			countryResult.put(Constants.MESSAGE, ErrorMessageConstant.UNABLE_TO_FETCH);
			return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED, countryResult));
		}
	}
}
