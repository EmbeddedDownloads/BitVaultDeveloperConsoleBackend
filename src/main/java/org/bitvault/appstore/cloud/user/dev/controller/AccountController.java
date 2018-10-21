package org.bitvault.appstore.cloud.user.dev.controller;

import org.bitvault.appstore.cloud.constant.APIConstants;
import org.bitvault.appstore.cloud.dto.AccountDto;
import org.bitvault.appstore.cloud.user.dev.service.AccountService;
import org.bitvault.appstore.cloud.validator.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = APIConstants.DEV_API_BASE)
public class AccountController {
	@Autowired
	AccountService accountService;
	
	@RequestMapping(value = "/saveaccount", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> saveAccount(@RequestBody AccountDto accountDto){
		boolean isValidEmail = EmailValidator.isValidEmailID(accountDto.getEmail());
		if(!isValidEmail){
			return null;
		}
//		accountService.saveAccount(accountDto);
		return new ResponseEntity<String>("Success",HttpStatus.OK);
	}
}
