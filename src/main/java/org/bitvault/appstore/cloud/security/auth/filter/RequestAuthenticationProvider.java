package org.bitvault.appstore.cloud.security.auth.filter;

import java.util.ArrayList;
import java.util.List;

import org.bitvault.appstore.cloud.constant.Constants;
import org.bitvault.appstore.cloud.constant.DbConstant;
import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.constant.RoleConstant;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.bitvault.appstore.cloud.model.AdminUser;
import org.bitvault.appstore.cloud.model.DevPayment;
import org.bitvault.appstore.cloud.model.DevUser;
import org.bitvault.appstore.cloud.model.Role;
import org.bitvault.appstore.cloud.security.auth.jwt.model.UserContext;
import org.bitvault.appstore.cloud.user.admin.service.AdminUserService;
import org.bitvault.appstore.cloud.user.common.service.DevPaymentService;
import org.bitvault.appstore.cloud.user.dev.service.DevUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class RequestAuthenticationProvider implements AuthenticationProvider {

	private final BCryptPasswordEncoder encoder;
	private final DevUserService userService;

	private final Logger logger = LoggerFactory.getLogger(RequestAuthenticationProvider.class);

	@Autowired
	private AdminUserService adminUserService;

	@Autowired
	DevPaymentService devPaymentService;

	@Autowired
	public RequestAuthenticationProvider(final DevUserService userService, final BCryptPasswordEncoder encoder) {
		this.userService = userService;
		this.encoder = encoder;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		Assert.notNull(authentication, "No authentication data provided");

		String username = (String) authentication.getPrincipal();
		String password = (String) authentication.getCredentials();
		String loginType = (String) authentication.getDetails();

		AdminUser adminUser = null;
		DevUser user = null;
		Role role = null;
		try {
			if (loginType == null || !loginType.equalsIgnoreCase(RoleConstant.ADMIN)) {
				user = userService.findByEmailId(username);
				role = user.getRole();

			} else {
				adminUser = adminUserService.findByEmail(username);
				role = adminUser.getRole();
			}
		} catch (Exception e) {
			logger.error("error occured in RequestAuthProvider ...");
			throw new UsernameNotFoundException(ErrorMessageConstant.USER_NOT_FOUND);
		}

		if (adminUser != null && !encoder.matches(password, adminUser.getPassword())) {
			throw new BadCredentialsException(ErrorMessageConstant.BAD_CREDENTIALS);
		}

		// need to implement logic to throw user back to login screen if user is
		// not ACTIVE
		else if (user != null) {
			if (user.getStatus().equals("REVIEW")) {
				if (user.getVerificationLink().length() > 0) {
					throw new BitVaultException(ErrorMessageConstant.EMAIL_NOT_VERIFIED);
				}
				List<DevPayment> devPaymentList = devPaymentService.findByUserIdAndPaymentFor(user.getUserId(),
						Constants.SELF, new PageRequest(0, 1, new Sort(Direction.DESC, Constants.CREATED_AT)))
						.getContent();
				if (!devPaymentList.isEmpty()) {
					String txnStatus = devPaymentList.get(0).getTxnStatus();
					if (!txnStatus.equalsIgnoreCase(Constants.SUCCESS))
						throw new BitVaultException(ErrorMessageConstant.PAYMENT_NOT_DONE);
				}
				throw new BitVaultException(ErrorMessageConstant.ACCOUNT_NOT_APPROVED);
			}
			if (!encoder.matches(password, user.getPassword())) {
				throw new BadCredentialsException(ErrorMessageConstant.BAD_CREDENTIALS);
			}
			if (!user.getStatus().equalsIgnoreCase(DbConstant.ACTIVE)) {
				if(!user.getParentId().equalsIgnoreCase(user.getUserId())){
					throw new BitVaultException(ErrorMessageConstant.ACCOUNT_EXPIRED_REJECTED.replace(RoleConstant.ADMIN, RoleConstant.ORG));
				}
				throw new BitVaultException(ErrorMessageConstant.ACCOUNT_EXPIRED_REJECTED);
			}
			if (!user.getUserId().equalsIgnoreCase(user.getParentId())) {
				DevUser parentUser = userService.findByUserId(user.getParentId());
				if (!parentUser.getStatus().equalsIgnoreCase(DbConstant.ACTIVE)) {
					throw new BitVaultException(ErrorMessageConstant.ORG_ACCOUNT_INACTIVATED);
				}
			}
		}

		if (role == null)
			throw new InsufficientAuthenticationException("User has no roles assigned");

		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
		// user.getRole().stream()
		// .map(authority -> new
		// SimpleGrantedAuthority(authority.getRole().authority()))
		// .collect(Collectors.toList());

		UserContext userContext = null;
		if (user != null) {
			userContext = UserContext.create(user.getEmail(), authorities, user.getAvatarURL(), user.getUsername());
			List<DevPayment> devPaymentList = devPaymentService.findByUserIdAndPaymentFor(user.getUserId(),
					Constants.SELF, new PageRequest(0, 1, new Sort(Direction.DESC, Constants.CREATED_AT))).getContent();
			if (!devPaymentList.isEmpty()) {
				DevPayment devPayment = devPaymentList.get(0);
				String payStatus = devPayment.getTxnStatus();
				userContext.setPaymentStatus(payStatus);
				if (!payStatus.equalsIgnoreCase(Constants.SUCCESS)) {
					userContext.setPaybleAmount(devPayment.getPayment());
				}
				userContext.setLoginType(user.getAccount().getRole().getRoleName());
			}
			if (!user.getUserId().equalsIgnoreCase(user.getParentId())){
				userContext.setPaymentStatus(Constants.SUCCESS);
			}
			userContext.setUserId(user.getUserId());	
		} else {
			userContext = UserContext.create(adminUser.getEmail(), authorities, adminUser.getAvatarURL(),
					adminUser.getFirstName() + " " + adminUser.getLastName());
			userContext.setUserId(adminUser.getUserId());
			userContext.setPaymentStatus(Constants.SUCCESS);
		}
		return new UsernamePasswordAuthenticationToken(userContext, null, userContext.getAuthorities());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
	}

	// public static void main(String...strings ){
	// BCryptPasswordEncoder b = new BCryptPasswordEncoder();
	// System.out.println(b.encode("Inception@3"));
	// }
}
