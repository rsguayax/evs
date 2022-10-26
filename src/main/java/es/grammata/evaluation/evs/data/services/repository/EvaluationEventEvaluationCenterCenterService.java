package es.grammata.evaluation.evs.data.services.repository;

import java.util.List;

import es.grammata.evaluation.evs.data.model.repository.Center;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEventEvaluationCenterCenter;
import es.grammata.evaluation.evs.data.services.base.BaseService;

public interface EvaluationEventEvaluationCenterCenterService 
	extends BaseService<EvaluationEventEvaluationCenterCenter> {
	
	public List<Center> findCentersByEvaluationEventEvaluationCenterId(Long id);
	public void updateByEventEvaluationCenterId(Long evaluationEventId, 
			Long evaluationEventEvaluationCenterID,  List<Center> centers);
	public List<Center> findCentersByEvaluationEventId(Long eeId);

}
