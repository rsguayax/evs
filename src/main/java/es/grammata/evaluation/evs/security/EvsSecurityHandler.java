package es.grammata.evaluation.evs.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import es.grammata.evaluation.evs.data.model.repository.Role;
import es.grammata.evaluation.evs.data.services.repository.UserProfileService;

@Component("evsSecurityHandler")
public class EvsSecurityHandler {
	
	@Autowired
	public UserProfileService userProfileService;
	
	
    public boolean hasProfile(String userProfile) {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return hasProfile(authentication, userProfile);
    }
    
    
    public boolean hasProfile(Object userProfile, String test, String test2) {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return hasProfile(authentication, test);
    }
    
    public boolean hasProfile(Authentication authentication, String userProfileCode) {
    	for (GrantedAuthority auth : authentication.getAuthorities()) {
            if (Role.ADMIN.equals(auth.getAuthority()))
                return true;
        }
     		
    	boolean res = userProfileService.checkUserProfile(getUserName(authentication), userProfileCode);
    	
        return res;
    }
    
    public boolean hasMProfile(String...userProfiles) {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	for(String userProfile : userProfiles) {
    		if(hasProfile(authentication, userProfile)) {
    			return true;
    		}
    	}
    	
        return false;
    }
    
    public boolean hasMPermission(String...domainPermissions) {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	for(String domainPermission : domainPermissions) {
    		String[] parts = domainPermission.split(":");
    		if(hasPermission(authentication, null, parts[0], parts[1])) {
    			return true;
    		}
    	}
    	
        return false;
    }
    

    public boolean hasPermission(String domainObject, String permission) {
    	 return hasPermission(null, domainObject, permission);
    }
    
    public boolean hasPermission(Long objectId, String domainObject, String permission) {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return hasPermission(authentication, objectId, domainObject, permission);
    }
    
    public boolean hasPermission(Authentication authentication, Long objectId, String domainObject, 
    		String permission) {
    	for (GrantedAuthority auth : authentication.getAuthorities()) {
            if (Role.ADMIN.equals(auth.getAuthority()))
                return true;
        }
     		
    	boolean res = userProfileService.checkUserPermission(getUserName(authentication), objectId, 
    			domainObject, permission);
    	
        return res;
    }
    
    private String getUserName(Authentication authentication) {
    	String username = "";
	    if(authentication.getPrincipal() instanceof es.grammata.evaluation.evs.data.model.repository.User) {
	    	username = ((es.grammata.evaluation.evs.data.model.repository.User)authentication.getPrincipal()).getUsername();
	    } else if(authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.User) {
	    	username =  ((org.springframework.security.core.userdetails.User)authentication.getPrincipal()).getUsername();
	    }
	    
	    return username;
    }
}