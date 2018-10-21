package org.bitvault.appstore.cloud.security.service;

import java.util.ArrayList;
import java.util.List;

import org.bitvault.appstore.cloud.constant.ErrorCode;
import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.bitvault.appstore.cloud.model.AdminUser;
import org.bitvault.appstore.cloud.model.DevUser;
import org.bitvault.appstore.cloud.user.admin.dao.AdminUserRepository;
import org.bitvault.appstore.cloud.user.dev.dao.DevUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("userDetailsService")
public class CustomUserDetailsService
implements UserDetailsService {

	@Autowired
	private DevUserRepository devUserRepository;

	@Autowired
	private AdminUserRepository adminUserRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		DevUser user = devUserRepository.findByEmail(email);
		if (user == null) {
			AdminUser adminUser = adminUserRepository.findByEmail(email);
			if (adminUser == null) {
				throw new BitVaultException(ErrorMessageConstant.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND);
			}
			return new User(adminUser.getEmail(), adminUser.getPassword(), grantedAuthorities(adminUser));
		}
		boolean isEnabled = user.getStatus().equals("ACTIVE");
		// return new User(user.getEmail(), user.getPassword(), true, true,
		// true, true, grantedAuthorities(user));
		return new User(user.getEmail(), user.getPassword(), grantedAuthorities(user));
	}

	private List<GrantedAuthority> grantedAuthorities(DevUser user) {
		try {
			List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
			authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().getRoleName()));
			return authorities;
		} catch (Exception e) {
			return null;
		}
	}
	
	private List<GrantedAuthority> grantedAuthorities(AdminUser user) {
		try {
			List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
			authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().getRoleName()));
			return authorities;
		} catch (Exception e) {
			return null;
		}
	}

}
