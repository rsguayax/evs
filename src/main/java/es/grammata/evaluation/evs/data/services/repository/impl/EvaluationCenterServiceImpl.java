package es.grammata.evaluation.evs.data.services.repository.impl;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import es.grammata.evaluation.evs.data.model.repository.Address;
import es.grammata.evaluation.evs.data.model.repository.EvaluationCenter;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEventEvaluationCenter;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEventEvaluationCenterCenter;
import es.grammata.evaluation.evs.data.services.base.impl.BaseServiceImpl;
import es.grammata.evaluation.evs.data.services.repository.EvaluationCenterService;


@Repository
@Transactional(readOnly = true)
public class EvaluationCenterServiceImpl extends BaseServiceImpl<EvaluationCenter> implements EvaluationCenterService {
	
	public List<Address> findAddressesByEvaluationCenter(EvaluationCenter evaluationCenter, int page, int pageSize) {

		TypedQuery<Address> query = this.em.createQuery("select a from " + Address.class.getSimpleName() + 
				" a where a.evaluationCenter = ?1", Address.class);

		query.setParameter(1, evaluationCenter);
		query.setFirstResult(page * pageSize);
		query.setMaxResults(pageSize);

		return query.getResultList();
	}
	
	public List<EvaluationCenter> findAll() {

		TypedQuery<EvaluationCenter> query = this.em.createQuery("select c from " + EvaluationCenter.class.getSimpleName() + " c order by c.name ASC", 
				EvaluationCenter.class);
		
		return query.getResultList();
	}

	public List<EvaluationCenter> findByEvaluationEventAndCenter(Long evaluationEventId, Long centerId) {
		TypedQuery<EvaluationCenter> query = this.em.createQuery(
				"SELECT ec FROM " + EvaluationCenter.class.getSimpleName() + 
				" ec, " + EvaluationEventEvaluationCenter.class.getSimpleName() + 
				" eeec, " + EvaluationEventEvaluationCenterCenter.class.getSimpleName() + 
				" eeecc WHERE eeec.evaluationCenter.id=ec.id AND eeecc.evaluationEventEvaluationCenter.id=eeec.id AND" + 
				" eeec.evaluationEvent.id=:evaluationEventId AND eeecc.center.id=:centerId", EvaluationCenter.class);
		
		query.setParameter("evaluationEventId", evaluationEventId);
		query.setParameter("centerId", centerId);
		List<EvaluationCenter> evaluationCenters = query.getResultList();

		return evaluationCenters;
	}
	
	public List<EvaluationCenter> findByEvaluationEvent(Long evaluationEventId) {
		TypedQuery<EvaluationCenter> query = this.em.createQuery(
				"SELECT ec FROM " + EvaluationCenter.class.getSimpleName() + 
				" ec, " + EvaluationEventEvaluationCenter.class.getSimpleName() + 
				" eeec WHERE eeec.evaluationCenter.id=ec.idAND" + 
				" eeec.evaluationEvent.id=:evaluationEventId", EvaluationCenter.class);
		
		query.setParameter("evaluationEventId", evaluationEventId);
		List<EvaluationCenter> evaluationCenters = query.getResultList();

		return evaluationCenters;
	}
	
	public boolean existsEvaluationEventRelated(Long evaluationCenterId) {
		String query = "select count(*) from evaluation_event_evaluation_center where evaluation_center_id = " + 
							evaluationCenterId;
		
		long total = ((Number)em.createNativeQuery(query).getSingleResult()).longValue();
		
		return (total>0)?true:false;	
	}
}
