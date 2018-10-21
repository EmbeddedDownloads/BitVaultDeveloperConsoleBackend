package org.bitvault.appstore.cloud.config;

import java.io.Serializable;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

public class BasePermissionEvaluator implements PermissionEvaluator{

	@Override
	public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
		boolean hasPermission = false;
		  if ( authentication != null &&  permission instanceof String){
		   //implement the permission checking of your application here   
		   //you can just check if the input permission is within your permission list
		   //In my example, the user object contains a HashMap which stored the permission of the user.
		   //The HashMap<String, PrivilegeResult> is populated during using login by filter. This will not be shown in this example 

//		   User user = SecurityUtil.getUserCredential();
//		   HashMap<String, PrivilegeResult> pMap =user.getPrivilegeMap();
//		   PrivilegeResult privResult = pMap.get(permission); 
//		   hasPermission =  privResult.isAllowAccess();
		  } else {
		   hasPermission =false; 
		  }
		  return hasPermission;
	}

	@Override
	public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType,
			Object permission) {
		// TODO Auto-generated method stub
		return false;
	}

}
