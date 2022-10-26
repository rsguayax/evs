package es.grammata.evaluation.evs.data.services.repository;

import java.util.List;

import es.grammata.evaluation.evs.data.model.repository.User;
import es.grammata.evaluation.evs.data.services.base.BaseService;

public interface UserService extends BaseService<User> {
	
	public List<User> findByRole(String role);
	public User saveOrLoadByUsername(User user);
	public User findByUsername(String username);
	public User findByIdentification(String identification);
	public List<User> findByRoleAndSearchText(String role, String searchText);
}

