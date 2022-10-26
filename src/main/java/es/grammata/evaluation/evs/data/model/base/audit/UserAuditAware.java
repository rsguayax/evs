package es.grammata.evaluation.evs.data.model.base.audit;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserAuditAware implements AuditorAware<String> {

	@Override
	public String getCurrentAuditor() {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		 
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        
        if(authentication.getPrincipal() instanceof es.grammata.evaluation.evs.data.model.repository.User) {
        	return ((es.grammata.evaluation.evs.data.model.repository.User)authentication.getPrincipal()).getUsername();
        } else if(authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.User) {
        	return ((org.springframework.security.core.userdetails.User)authentication.getPrincipal()).getUsername();
        }
 
        return null; 
	}
}
