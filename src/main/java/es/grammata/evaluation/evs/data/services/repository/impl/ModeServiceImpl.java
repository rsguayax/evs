package es.grammata.evaluation.evs.data.services.repository.impl;

import java.util.List;

import javax.persistence.TypedQuery;

import org.hibernate.ejb.QueryHints;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import es.grammata.evaluation.evs.data.model.repository.Mode;
import es.grammata.evaluation.evs.data.services.base.impl.BaseServiceImpl;
import es.grammata.evaluation.evs.data.services.repository.ModeService;


@Repository
@Transactional(readOnly = true)
public class ModeServiceImpl extends BaseServiceImpl<Mode> implements ModeService {
	public Mode findByCode(String code) {
		TypedQuery<Mode> query = this.em.createQuery("select r from " + Mode.class.getSimpleName() + 
				" r where r.code='" + code + "'", Mode.class);
		
		List<Mode> modes = query.getResultList();
		Mode mode = null;
		if(modes != null && modes.size() > 0) {
			mode = modes.get(0);
		}
		
		return mode;
	}
	
	@Override
	public List<Mode> findAll() {
		TypedQuery<Mode> query = this.em.createQuery("from " + Mode.class.getName(), 
				Mode.class);
	    query.setHint(QueryHints.HINT_CACHEABLE, true);
	    return query.getResultList();
	}  
	
	@Override
	public List<Mode> findNotDeleted() {
		TypedQuery<Mode> query = this.em.createQuery("select m from " + Mode.class.getName() + " m where m.deleted=False", 
				Mode.class);
	    return query.getResultList();
	}
	
	@Override
	public void deleteAll() {
		String query = "UPDATE mode SET deleted=True";
		em.createNativeQuery(query).executeUpdate();	
		em.flush();
	}
}
