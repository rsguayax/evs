package es.grammata.evaluation.evs.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import es.grammata.evaluation.evs.data.model.repository.Role;
import es.grammata.evaluation.evs.data.model.repository.User;
import es.grammata.evaluation.evs.data.services.repository.UserService;

public class LocalAuthProvider implements AuthenticationProvider {

	@Autowired
    private UserService userService;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String username = authentication.getName();
        String password = (String) authentication.getCredentials();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        EvsAuthenticationDetails authDetails = (EvsAuthenticationDetails)authentication.getDetails();
        
        if(authDetails != null && authDetails.getRole() != null) {
        	User user = userService.findByUsername(username);
        	String roleAuth = authDetails.getRole();
        	
        	if (user != null && passwordEncoder.matches(password, user.getPassword()) && user.getEnabled() == 1) {
        		List<GrantedAuthority> grantedAuths = new ArrayList<GrantedAuthority>();
        		for (Role role : user.getRoles()) {
        			grantedAuths.add(new SimpleGrantedAuthority(role.getCode()));
        		}
        		
        		if(checkRole(roleAuth, grantedAuths)) {
        			UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, password, grantedAuths);
            		auth.setDetails(user);
            		return auth;
        		}
        	}
        }
        
        return null;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

	private boolean checkRole(String roleAuth, List<GrantedAuthority> grantedAuths) {
		for (GrantedAuthority grantedAuth : grantedAuths) {
			String role = grantedAuth.getAuthority();
			
			if (roleAuth.equals(Role.STAFF) && (role.equals(Role.ADMIN) || role.equals(Role.EVENT_ADMIN) || role.equals(Role.STAFF))) {
				return true;
			}
			
			if (roleAuth.equals(Role.STUDENT) && role.equals(Role.STUDENT)) {
				return true;
			}
			
			if (role.equals(Role.ADMIN)) {
				return true;
			}
		}
		
		return false;
	}
}
