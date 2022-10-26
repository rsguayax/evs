package es.grammata.evaluation.evs.data.services.repository.impl;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import es.grammata.evaluation.evs.data.model.repository.EvaluationEventType;
import es.grammata.evaluation.evs.data.services.base.impl.BaseServiceImpl;
import es.grammata.evaluation.evs.data.services.repository.EvaluationEventTypeService;


@Repository
@Transactional(readOnly = true)
public class EvaluationEventTypeServiceImpl extends BaseServiceImpl<EvaluationEventType> implements EvaluationEventTypeService {

	@Override
	public List<EvaluationEventType> findAllAtive() {
		TypedQuery<EvaluationEventType> query = this.em.createQuery("select r from " + EvaluationEventType.class.getSimpleName() + 
				" r where r.active='" + true + "' ORDER BY id ASC", EvaluationEventType.class);
		
		List<EvaluationEventType> evaluationEventTypes = query.getResultList();
		return evaluationEventTypes;
	}

	@Override
	public EvaluationEventType findbyName(String name) {
	
		TypedQuery<EvaluationEventType > query = this.em.createQuery("select r from " + EvaluationEventType .class.getSimpleName() +
				" r where r.name='" + name + "'", EvaluationEventType .class);

		List<EvaluationEventType> evaluationEvents = query.getResultList();
		EvaluationEventType evaluationEventType  = null;
		if(evaluationEvents != null && evaluationEvents.size() > 0) {
			evaluationEventType  = evaluationEvents.get(0);
		}

		return evaluationEventType ;
	}

}
