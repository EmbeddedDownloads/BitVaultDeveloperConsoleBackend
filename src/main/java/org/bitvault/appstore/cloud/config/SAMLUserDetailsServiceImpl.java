/*
 * Copyright 2017 Vincenzo De Notaris
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */

package org.bitvault.appstore.cloud.config;

import java.util.ArrayList;
import java.util.List;

import org.bitvault.appstore.cloud.constant.Constants;
import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.constant.RoleConstant;
import org.bitvault.appstore.cloud.dto.DevUserDto;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.bitvault.appstore.cloud.model.AdminUser;
import org.bitvault.appstore.cloud.model.DevUser;
import org.bitvault.appstore.cloud.model.Request;
import org.bitvault.appstore.cloud.model.RequestType;
import org.bitvault.appstore.cloud.model.Role;
import org.bitvault.appstore.cloud.user.admin.service.AdminUserService;
import org.bitvault.appstore.cloud.user.common.service.RequestActivityService;
import org.bitvault.appstore.cloud.user.common.service.RequestService;
import org.bitvault.appstore.cloud.user.dev.service.AccountService;
import org.bitvault.appstore.cloud.user.dev.service.DevUserService;
import org.bitvault.appstore.cloud.utils.Utility;
import org.opensaml.saml2.core.Attribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.saml.SAMLCredential;
import org.springframework.security.saml.userdetails.SAMLUserDetailsService;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class SAMLUserDetailsServiceImpl implements SAMLUserDetailsService {

	@Autowired
	private DevUserService userService;
	@Autowired
	private AdminUserService adminUserService;

	@Autowired
	private AccountService accountService;

	@Autowired
	private RequestService requestService;

	@Autowired
	RequestActivityService requestActivityService;

	// Logger
	private static final Logger LOG = LoggerFactory.getLogger(SAMLUserDetailsServiceImpl.class);

	public Object loadUserBySAML(SAMLCredential credential) throws UsernameNotFoundException {

		// The method is supposed to identify local account of user referenced by
		// data in the SAML assertion and return UserDetails object describing the user.
		try {

			List<Attribute> attributeList = credential.getAttributes();
			DevUserDto user = new DevUserDto();

			String givenName = null;
			String surname = null;
			Role role = null;
			AdminUser adminUser = null;
			DevUser devUser = null;
			for (Attribute attribute : attributeList) {
				String attributeName = attribute.getName();
				System.out.println(attributeName);
				String attributeValue = credential.getAttributeAsString(attributeName);
				System.out.println(attributeValue);

				if (attributeName.equals("accEmail")) {
					user.setAccEmail(attributeValue);
				} else if (attributeName.equals("orgName")) {
					user.setOrgName(attributeValue);
				} else if (attributeName.equals("signupAs")) {
					user.setSignupAs(attributeValue);
				} else if (attributeName.equals("signupReason")) {
					user.setSignupReason(attributeValue);
				} else if (attributeName.equals("website")) {
					user.setWebsite(attributeValue);
				} else if (attributeName.equals("email")) {
					user.setEmail(attributeValue);
				} else if (attributeName.equals("givenName")) {
					givenName = attributeValue;
				} else if (attributeName.equals("surname")) {
					surname = attributeValue;
				}

			}
			if (null != user.getSignupAs() && !user.getSignupAs().trim().isEmpty()
					&& user.getSignupAs().equals(RoleConstant.INDIVISUAL_DEV)) {
				user.setOrgName(null);
				user.setWebsite(null);
				user.setAccEmail(null);
			}
			user.setUsername(givenName + " " + surname);
			ObjectMapper objectMapper = new ObjectMapper();
			LOG.info("user from attribute" + objectMapper.writeValueAsString(user));
			devUser = userService.findByEmailId(user.getEmail());
			adminUser = adminUserService.findByEmail(user.getEmail());
			List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
			if (null == devUser && null == adminUser) {
				user.setPassword(Utility.getUuid());

				devUser = accountService.saveAccount(user, null);

				Request accRequest = new Request();
				accRequest.setUserId(devUser.getUserId());
				RequestType requestType = new RequestType();
				requestType.setRequestTypeId(1);
				accRequest.setRequestType(requestType);
				accRequest.setStatus(Constants.PENDING);
				requestService.persistRequest(accRequest);
				if (accRequest != null) {
					requestActivityService.saveRequestActivity(accRequest, null);
				}
				LOG.info("new user saved to db " + objectMapper.writeValueAsString(devUser));

			}
			if (null != devUser) {
				role = devUser.getRole();
				LOG.info("user role " + role.getRoleName());

				GrantedAuthority authority = new SimpleGrantedAuthority(role.getRoleName());
				authorities.add(authority);

				// Authentication authentication = new
				// UsernamePasswordAuthenticationToken(devUser, credential, authorities);
				// boolean isAuthenticated = authentication.isAuthenticated();
				// SecurityContextHolder.getContext().setAuthentication(authentication);
				return new User(devUser.getEmail(), devUser.getPassword(), true, true, true, true, authorities);

			} else {
				role = adminUser.getRole();
				LOG.info("admin role " + role.getRoleName());

				GrantedAuthority authority = new SimpleGrantedAuthority(role.getRoleName());
				authorities.add(authority);

				// Authentication authentication = new
				// UsernamePasswordAuthenticationToken(adminUser, credential, authorities);
				// boolean isAuthenticated = authentication.isAuthenticated();
				// SecurityContextHolder.getContext().setAuthentication(authentication);
				return new User(adminUser.getEmail(), adminUser.getPassword(), true, true, true, true, authorities);

			}

		} catch (Exception e) {

			LOG.error("exception in loadUserBySAML " + e.getMessage() + "/n " + e);

			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);
		}

	}

}
