package es.grammata.evaluation.evs.data.services.repository.impl;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import es.grammata.evaluation.evs.data.model.repository.Cap;
import es.grammata.evaluation.evs.data.model.repository.Classroom;
import es.grammata.evaluation.evs.data.services.base.impl.BaseServiceImpl;
import es.grammata.evaluation.evs.data.services.repository.CapService;

@Repository
@Transactional(readOnly = true)
public class CapServiceImpl extends BaseServiceImpl<Cap> implements CapService {
	public List<Cap> findUnassigned(Long evaluationCenterId, Long capId) {
		String query = "select c from " + Cap.class.getSimpleName() + 
				" c  where (c.evaluationCenter.id = :evaluationCenterId and " +
					" NOT EXISTS (select cr from " + Classroom.class.getSimpleName() + " cr " +
							" where cr.cap.id = c.id))";
		
		if(capId != null) {
			query += " OR c.id = :capId";
		}
			
		query += " order by name asc";
	
		TypedQuery<Cap> typedQuery = this.em.createQuery(query, Cap.class);
		
		typedQuery.setParameter("evaluationCenterId", evaluationCenterId);
		
		if(capId != null) {
			typedQuery.setParameter("capId", capId);
		}
		
		List<Cap> caps = typedQuery.getResultList();

		return caps;
	}
}
