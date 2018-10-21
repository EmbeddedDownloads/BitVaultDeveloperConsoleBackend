package org.bitvault.appstore.cloud.utils;

import javax.servlet.http.HttpServletRequest;

import org.bitvault.appstore.cloud.constant.Constants;
import org.bitvault.appstore.cloud.security.auth.JwtAuthenticationToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class AuditorAwareImpl implements AuditorAware<String>{

	private final Logger logger = LoggerFactory.getLogger(AuditorAwareImpl.class);
	
	@Override
	public String getCurrentAuditor() {
        try {
        	HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        	String username = (String) request.getAttribute(Constants.USERID);
        	if(username != null){
        		return username;
        	}
        	JwtAuthenticationToken auth = (JwtAuthenticationToken)SecurityContextHolder.getContext().getAuthentication();
			if(auth == null){
				return "";
			}
			username = auth.getUserId();
			return username;
		} catch (Exception e) {
			logger.warn(e.getMessage());
			return "";
		}
	}

}
