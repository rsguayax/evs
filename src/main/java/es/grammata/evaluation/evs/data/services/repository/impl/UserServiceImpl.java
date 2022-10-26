package es.grammata.evaluation.evs.data.services.repository.impl;

import java.security.Principal;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import es.grammata.evaluation.evs.data.model.repository.User;
import es.grammata.evaluation.evs.data.services.base.impl.BaseServiceImpl;
import es.grammata.evaluation.evs.data.services.repository.UserService;
import siette.models.Usuario;

@Repository
@Transactional(readOnly = true)
public class UserServiceImpl extends BaseServiceImpl<User> implements UserService {

	public Usuario usuario;
	
	@RequestMapping(value = "/index.html", method = RequestMethod.GET) 
	public ModelAndView indexView(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("index");
		String userName = "not logged in"; // Any default user name
		Principal principal = request.getUserPrincipal(); 
		if (principal != null) { 
			userName = principal.getName(); 
			} 
		mv.addObject("username", userName); // By adding a little code (same way) you can check if user has any // roles you need, for example: 
		boolean fAdmin = request.isUserInRole("ROLE_ADMIN");
		mv.addObject("isAdmin", fAdmin);
		return mv; 
		}
	




	
	public List<User> findByRole(String role) {
		TypedQuery<User> query = this.em.createQuery("SELECT u FROM " + User.class.getSimpleName() +
				" u JOIN u.roles r WHERE r.code='" + role + "' order by u.lastName asc", User.class);
		return query.getResultList();
	}

	public User findByUsername(String username) {
		TypedQuery<User> query = this.em.createQuery("SELECT u FROM " + User.class.getSimpleName() +
				" u WHERE u.username='" + username+ "'", User.class);

		List<User> users = query.getResultList();
		User user = null;
		if(users != null && users.size() > 0) {
			user = users.get(0);
		}

		return user;
	}

	public User saveOrLoadByUsername(User user) {
		User userAux = this.findByUsername(user.getUsername());
		if(userAux == null) {
			this.save(user);
		} else {
			user = userAux;
		}

		return user;
	}

	public List<User> findByRoleAndSearchText(String role, String searchText) {
		TypedQuery<User> query = this.em.createQuery("SELECT u FROM " + User.class.getSimpleName() +
				" u JOIN u.roles r WHERE r.code=:role " +
				"AND (lower(u.identification) LIKE lower(:searchText) OR lower(u.firstName) LIKE lower(:searchText) " +
				"OR lower(u.lastName) LIKE lower(:searchText)" +
				"OR lower(u.username) LIKE lower(:searchText)) " +
				"order by u.lastName asc", User.class);

		query.setParameter("role", role);
		query.setParameter("searchText", "%" + searchText + "%");

		return query.getResultList();
	}






	@Override
	public User findByIdentification(String identification) {
			TypedQuery<User> query = this.em.createQuery("SELECT u FROM " + User.class.getSimpleName() +
					" u WHERE u.identification='" + identification+ "'", User.class);

			List<User> users = query.getResultList();
			User user = null;
			if(users != null && users.size() > 0) {
				user = users.get(0);
			}

			return user;
	
	}
}

