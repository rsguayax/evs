package es.grammata.evaluation.evs.security;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import es.grammata.evaluation.evs.data.model.repository.Role;
import es.grammata.evaluation.evs.data.model.repository.User;
import es.grammata.evaluation.evs.data.model.repository.UserProfile;
import es.grammata.evaluation.evs.data.services.repository.RoleService;
import es.grammata.evaluation.evs.data.services.repository.UserProfileService;
import es.grammata.evaluation.evs.data.services.repository.UserService;
import es.grammata.evaluation.evs.services.restservices.LoginClient;

@Component
@Configurable
public class UtplAuthProvider implements AuthenticationProvider {
	
	private static org.apache.log4j.Logger log = Logger.getLogger(UtplAuthProvider.class);
	
	private MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private UserProfileService userProfileService;

	@Autowired
	private LoginClient loginClient;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String username = (String)authentication.getPrincipal(); 
		EvsAuthenticationDetails authDetails = (EvsAuthenticationDetails)authentication.getDetails();
		String password = (String)authentication.getCredentials();
	
		try {
		    if(authDetails != null && authDetails.getRole() != null) {
				String roleAuth = authDetails.getRole();
				User user = null;
				// Si es personal UTPL tendrá rol STAFF
				if (roleAuth.equals(Role.STAFF)) {
				    user = authClient(username, password, Role.STAFF);
				    /*if (user == null) {
				    	user = authClient(username, password, Role.TEACHER);
				    }*/
				} else {
				    user = authClient(username, password, roleAuth);
				}
		
				if (user != null) {
					// podría ser ADMIN
					Role userRole = (Role)user.getRoles().iterator().next();
					String userRoleStr = userRole.getCode();
					
				    List<GrantedAuthority> grantedAuths = new ArrayList<GrantedAuthority>();
				    grantedAuths.add(new SimpleGrantedAuthority(userRoleStr));
				    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, password, grantedAuths);
				    auth.setDetails(user);
				    
				    return auth;
				} 
		    }
		} catch (Exception e) {
		    log.error("Error al obtener usuario en autenticación");
		    e.printStackTrace();
		}
	
		return null;
    }
    
    
    private User authClient(String username, String password, String role) throws Exception {
		Map<String, String> data = loginClient.login(username, password, rolesMap.get(role));
		if (data != null && !data.isEmpty()) {
		    User user = userService.findByUsername(username);
		    if(user == null) {
				user = new User();
				user.setEmail(data.get("email"));
				user.setExternalId(data.get("externalId"));
				user.setFirstName(data.get("firstname"));
				user.setLastName(data.get("lastname"));
				user.setUsername(username);
				user.setEnabled(1);
				userService.save(user);
		    }
		    
		    if(user.getRoles() == null || user.getRoles().size() == 0) {
			    Role roleEntity = roleService.findByCode(role);
			    user.getRoles().add(roleEntity);
			   
			    UserProfile defaultProfile = userProfileService.findByCode(UserProfile.DEFAULT);
			    user.getUserProfiles().add(defaultProfile);
			    
			    userService.update(user);
		    }
		    
		    //String cryptedPassword = passwordEncoder.encode(password);
		    //user.setPassword(cryptedPassword);
		    //userService.update(user);
		    
		    return user;
		} 
		
		return null;
    }
    

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
    private Map<String, String> rolesMap = new HashMap<String, String>() {
		{
		    put(Role.TEACHER, "OU_DOCENTES");
		    put(Role.STUDENT, "OU_ESTUDIANTES");
		    put(Role.ADMIN, "OU_ADMINISTRATIVOS");
		    put(Role.STAFF, "OU_ADMINISTRATIVOS");
		}
    };
    
}