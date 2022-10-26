package es.grammata.evaluation.evs.data.services.repository;

import java.util.List;

import es.grammata.evaluation.evs.data.model.repository.Address;
import es.grammata.evaluation.evs.data.model.repository.EvaluationCenter;
import es.grammata.evaluation.evs.data.services.base.BaseService;



public interface EvaluationCenterService extends BaseService<EvaluationCenter> {
	public List<Address> findAddressesByEvaluationCenter(EvaluationCenter evCenter, int page, int pageSize);
	public List<EvaluationCenter> findByEvaluationEventAndCenter(Long evaluationEventId, Long centerId);
	public boolean existsEvaluationEventRelated(Long evaluationCenterId);
	public List<EvaluationCenter> findByEvaluationEvent(Long evaluationEventId);
}
