package org.bitvault.appstore.cloud.security;

import java.io.Serializable;

import org.bitvault.appstore.cloud.security.auth.jwt.model.UserContext;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

@Component
public class CustomPermissionEvaluator implements PermissionEvaluator{

	@Override
	public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
		// TODO Auto-generated method stub
		
		try {
		
		if(authentication == null) {
			return false;
		}
		
		if(permission == null) {
			return false ;
		}
		
		String username =  ((UserContext)authentication.getPrincipal()).getUsername();		
		
		targetDomainObject.getClass().getName();
		StandardMultipartHttpServletRequest servletRequest = (StandardMultipartHttpServletRequest)targetDomainObject ;
		
		if(servletRequest.getAttribute("userId").toString().equalsIgnoreCase(permission.toString())) {
			return true ;
		}else {
			return false ;
		}
		
		
		}catch(Exception e) {
			e.printStackTrace();
			return false ;
		}
//		authentication.getAuthorities().contains(permission);
		
		
		
//		return true;
	}

	@Override
	public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType,
			Object permission) {
		// TODO Auto-generated method stub
		return false;
	}

}
