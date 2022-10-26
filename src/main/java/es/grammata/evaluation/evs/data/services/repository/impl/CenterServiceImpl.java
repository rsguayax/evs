package es.grammata.evaluation.evs.data.services.repository.impl;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import es.grammata.evaluation.evs.data.model.repository.Center;
import es.grammata.evaluation.evs.data.services.base.impl.BaseServiceImpl;
import es.grammata.evaluation.evs.data.services.repository.CenterService;


@Repository
@Transactional(readOnly = true)
public class CenterServiceImpl extends BaseServiceImpl<Center> implements CenterService {
	public List<Center> findUnassigned() {
		String query = "select c from " + Center.class.getSimpleName() + 
				" c where c.evaluationCenter IS NULL AND c.active=True order by name asc";
	
		TypedQuery<Center> typedQuery = this.em.createQuery(query, Center.class);
		
		List<Center> centers = typedQuery.getResultList();

		return centers;
	}
	
	
	public Center findByCode(String code) {
		TypedQuery<Center> query = this.em.createQuery("select b from " + Center.class.getSimpleName() + 
				" b where b.code='" + code + "'", Center.class);
		
		List<Center> centers = query.getResultList();
		Center center = null;
		if(centers != null && centers.size() > 0) {
			center = centers.get(0);
		}
		
		return center;
	}
	
	@Override
	public Center findByUniqueCode(String uniqueCode) {
		TypedQuery<Center> query = this.em.createQuery("select b from " + Center.class.getSimpleName() + 
				" b where b.uniqueCode='" + uniqueCode + "'", Center.class);
		
		List<Center> centers = query.getResultList();
		Center center = null;
		if(centers != null && centers.size() > 0) {
			center = centers.get(0);
		}
		
		return center;
	}
	
	public Center findByExternalId(String externaId) {
		TypedQuery<Center> query = this.em.createQuery("select b from " + Center.class.getSimpleName() + 
				" b where b.externalId='" + externaId + "'", Center.class);
		
		List<Center> centers = query.getResultList();
		Center center = null;
		if(centers != null && centers.size() > 0) {
			center = centers.get(0);
		}
		
		return center;
	}

	@Override
	public List<Center> findActive() {
		TypedQuery<Center> query = this.em.createQuery("select c from " + Center.class.getName() + " c where c.active=True order by name asc", 
				Center.class);
	    return query.getResultList();
	}


	@Override
	public void updateAllAsNotActive() {
		String query = "UPDATE center SET active=False";
		em.createNativeQuery(query).executeUpdate();	
		em.flush();
	}
}