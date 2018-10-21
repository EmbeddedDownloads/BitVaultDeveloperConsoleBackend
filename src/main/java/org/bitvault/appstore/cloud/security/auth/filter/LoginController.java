package org.bitvault.appstore.cloud.security.auth.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bitvault.appstore.cloud.constant.Constants;
import org.bitvault.appstore.cloud.constant.DbConstant;
import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.constant.RoleConstant;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.bitvault.appstore.cloud.exception.BitVaultResponse;
import org.bitvault.appstore.cloud.model.AdminUser;
import org.bitvault.appstore.cloud.model.DevPayment;
import org.bitvault.appstore.cloud.model.DevUser;
import org.bitvault.appstore.cloud.model.Role;
import org.bitvault.appstore.cloud.model.Token;
import org.bitvault.appstore.cloud.security.auth.jwt.model.UserContext;
import org.bitvault.appstore.cloud.security.service.TokenService;
import org.bitvault.appstore.cloud.user.admin.service.AdminUserService;
import org.bitvault.appstore.cloud.user.common.GeneralResponseModel;
import org.bitvault.appstore.cloud.user.common.service.DevPaymentService;
import org.bitvault.appstore.cloud.user.dev.service.DevUserService;
import org.bitvault.appstore.cloud.utils.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class LoginController {

	@Autowired
	private DevUserService userService;
	@Autowired
	private AdminUserService adminUserService;
	private final Logger Log = LoggerFactory.getLogger(RequestAuthenticationProvider.class);

	@Autowired
	DevPaymentService devPaymentService;

	@Autowired
	TokenService tokenService;

	@Autowired
	RequestAwareAuthenticationSuccessHandler successHandler;

	@PostMapping(value = "/access/token")
	public ResponseEntity<?> token(@RequestBody Map<String, String> requestBody) {
		GeneralResponseModel genralResponse = null;
		String token = (String) requestBody.get("token");
		try {
			if (!Utility.isStringEmpty(token)) {
				Token tokenEntity = tokenService.findTokenByTokenId(token);
				if (null != tokenEntity && null != tokenEntity.getPaymentStatus()) {
					genralResponse = GeneralResponseModel.of(Constants.SUCCESS,
							tokenEntity.populateTokenPaymentDto(tokenEntity));
					tokenService.deleteToken(tokenEntity.getTokenId());
				} else if (null != tokenEntity) {
					genralResponse = GeneralResponseModel.of(Constants.SUCCESS,
							tokenEntity.populateTokenDto(tokenEntity));
					tokenService.deleteToken(tokenEntity.getTokenId());

				} else {
					genralResponse = GeneralResponseModel.of(Constants.FAILED,
							new BitVaultResponse(ErrorMessageConstant.RESULT_NOT_FOUND));
				}
			} else {
				genralResponse = GeneralResponseModel.of(Constants.FAILED,
						new BitVaultResponse(ErrorMessageConstant.FIELD_IS_EMPTY));
			}

		} catch (BitVaultException e) {
			genralResponse = GeneralResponseModel.of(Constants.FAILED, new BitVaultResponse(e.getMessage()));
		}
		return new ResponseEntity<GeneralResponseModel>(genralResponse, HttpStatus.OK);

	}

	@GetMapping("/samlToken")
	public ModelAndView login(HttpServletRequest request, HttpServletResponse response) {
		GeneralResponseModel genralResponse = null;
		AdminUser adminUser = null;
		DevUser devUser = null;
		String email = null;
		ModelAndView mav = new ModelAndView();
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			Log.info("creating token ");

			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

			if (authentication == null) {
				// return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			} else {

				User user = (User) authentication.getPrincipal();
				email = user.getUsername();
				Log.info("user email " + email);

				UserContext userContext = validateSamlLogin(email, authentication, request);
				Log.info("setting usercontext::: " + objectMapper.writeValueAsString(userContext));
				String tokenId = successHandler.onAuthenticationSuccess(request, response, userContext).getTokenId();
				mav.addObject("tokenJson", tokenId);
			}

		} catch (BitVaultException e) {
			genralResponse = GeneralResponseModel.of(Constants.FAILED, new BitVaultResponse(e));
			mav.addObject("response", genralResponse);

			// return new ResponseEntity<GeneralResponseModel>(genralResponse,
			// HttpStatus.OK);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			genralResponse = GeneralResponseModel.of(Constants.FAILED, ErrorMessageConstant.SOME_ERROR_OCCURED);
			mav.addObject("response", genralResponse);
			// return new ResponseEntity<GeneralResponseModel>(genralResponse,
			// HttpStatus.OK);
		}
		mav.setViewName("token_index");
		return mav;
	}

	@GetMapping("/sso-logout")
	public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mav = new ModelAndView();
		try {
			request.logout();
		} catch (Exception e) {
			e.printStackTrace();
		}
		mav.setViewName("logout");
		return mav;
	}

	private UserContext validateSamlLogin(String username, Authentication authentication, HttpServletRequest request) {

		AdminUser adminUser = null;
		DevUser user = null;
		Role role = null;
		try {
			user = userService.findByEmailId(username);
			adminUser = adminUserService.findByEmail(username);
			if (null != user) {
				role = user.getRole();
			} else {
				role = adminUser.getRole();
			}

			if (user != null) {
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

				if (!user.getStatus().equalsIgnoreCase(DbConstant.ACTIVE)) {
					if (!user.getParentId().equalsIgnoreCase(user.getUserId())) {
						throw new BitVaultException(ErrorMessageConstant.ACCOUNT_EXPIRED_REJECTED
								.replace(RoleConstant.ADMIN, RoleConstant.ORG));
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

			String requestAttribute = role.getRoleName().split("_")[1];
			request.setAttribute("ownBy", requestAttribute);
			// user.getRole().stream()
			// .map(authority -> new
			// SimpleGrantedAuthority(authority.getRole().authority()))
			// .collect(Collectors.toList());

			UserContext userContext = null;
			if (user != null) {
				userContext = UserContext.create(user.getEmail(), authorities, user.getAvatarURL(), user.getUsername());
				List<DevPayment> devPaymentList = devPaymentService.findByUserIdAndPaymentFor(user.getUserId(),
						Constants.SELF, new PageRequest(0, 1, new Sort(Direction.DESC, Constants.CREATED_AT)))
						.getContent();
				if (!devPaymentList.isEmpty()) {
					DevPayment devPayment = devPaymentList.get(0);
					String payStatus = devPayment.getTxnStatus();
					userContext.setPaymentStatus(payStatus);
					if (!payStatus.equalsIgnoreCase(Constants.SUCCESS)) {
						userContext.setPaybleAmount(devPayment.getPayment());
					}
					userContext.setLoginType(user.getAccount().getRole().getRoleName());
				}
				if (!user.getUserId().equalsIgnoreCase(user.getParentId())) {
					userContext.setPaymentStatus(Constants.SUCCESS);
				}
				userContext.setUserId(user.getUserId());
			} else {
				userContext = UserContext.create(adminUser.getEmail(), authorities, adminUser.getAvatarURL(),
						adminUser.getFirstName() + " " + adminUser.getLastName());
				userContext.setUserId(adminUser.getUserId());
				userContext.setPaymentStatus(Constants.SUCCESS);
				userContext.setLoginType(adminUser.getRole().getRoleName());

			}

			return userContext;
			// authentication = new UsernamePasswordAuthenticationToken(userContext, null,
			// userContext.getAuthorities());
			//
			// SecurityContextHolder.getContext().setAuthentication(authentication);
		} catch (Exception e) {
			if (e instanceof BitVaultException) {
				throw new BitVaultException(e.getMessage());
			}
			throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);

		}
	}
}