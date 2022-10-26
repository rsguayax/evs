package es.grammata.evaluation.evs.data.services.repository.impl;

import java.util.List;

import javax.persistence.TypedQuery;

import org.hibernate.ejb.QueryHints;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import es.grammata.evaluation.evs.data.model.repository.Role;
import es.grammata.evaluation.evs.data.services.base.impl.BaseServiceImpl;
import es.grammata.evaluation.evs.data.services.repository.RoleService;

@Repository
@Transactional(readOnly = true)
public class RoleServiceImpl extends BaseServiceImpl<Role> implements RoleService {
	
	public Role findByCode(String code) {
		TypedQuery<Role> query = this.em.createQuery("select r from " + Role.class.getSimpleName() + 
				" r where r.code='" + code + "'", Role.class);
		
		List<Role> roles = query.getResultList();
		Role role = null;
		if(roles != null && roles.size() > 0) {
			role = roles.get(0);
		}
		
		return role;
	}
	
	@Override
	public List<Role> findAll() {
		TypedQuery<Role> query = this.em.createQuery("from " + Role.class.getName(), 
				Role.class);
	    query.setHint(QueryHints.HINT_CACHEABLE, true);
	    return query.getResultList();
	}   
}

