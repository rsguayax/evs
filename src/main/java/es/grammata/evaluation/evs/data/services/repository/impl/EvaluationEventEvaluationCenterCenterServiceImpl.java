package es.grammata.evaluation.evs.data.services.repository.impl;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import es.grammata.evaluation.evs.data.model.repository.Center;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEvent;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEventEvaluationCenter;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEventEvaluationCenterCenter;
import es.grammata.evaluation.evs.data.services.base.impl.BaseServiceImpl;
import es.grammata.evaluation.evs.data.services.repository.EvaluationEventEvaluationCenterCenterService;

@Repository
@Transactional(readOnly = true)
public class EvaluationEventEvaluationCenterCenterServiceImpl extends BaseServiceImpl<EvaluationEventEvaluationCenterCenter> implements EvaluationEventEvaluationCenterCenterService {
	public List<Center> findCentersByEvaluationEventEvaluationCenterId(Long eeecId) {
		TypedQuery<Center> query = this.em.createQuery("select ea.center from " + EvaluationEventEvaluationCenterCenter.class.getSimpleName() + 
				" ea where ea.evaluationEventEvaluationCenter.id = " + eeecId, Center.class);
		
		List<Center> users = query.getResultList();

		return users;
	}
	
	public List<Center> findCentersByEvaluationEventId(Long eeId) {
		TypedQuery<Center> query = this.em.createQuery("select ea.center from " + EvaluationEventEvaluationCenterCenter.class.getSimpleName() + 
				" ea where ea.evaluationEventEvaluationCenter.evaluationEvent.id = " + eeId, Center.class);
		
		List<Center> users = query.getResultList();

		return users;
	}
	
	@Transactional
	public void updateByEventEvaluationCenterId(Long evaluationEventId, 
				Long evaluationEventEvaluationCenterId,  List<Center> centers) {	
		String query = "DELETE FROM evaluation_event_evaluation_center_center " +
				" WHERE evaluation_event_evaluation_center_id = " + evaluationEventEvaluationCenterId;
		
		em.createNativeQuery(query).executeUpdate();	
		em.flush();
		
		EvaluationEventEvaluationCenter evaluationEventEvaluationCenter = new EvaluationEventEvaluationCenter();
		evaluationEventEvaluationCenter.setId(evaluationEventEvaluationCenterId);
		for(Center center : centers) {
			EvaluationEventEvaluationCenterCenter eeecc = new EvaluationEventEvaluationCenterCenter(center, evaluationEventEvaluationCenter);
			this.save(eeecc);
		}
		
	}
}
