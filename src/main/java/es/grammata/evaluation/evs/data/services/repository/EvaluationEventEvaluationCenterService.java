package es.grammata.evaluation.evs.data.services.repository;

import java.util.List;

import es.grammata.evaluation.evs.data.model.repository.Center;
import es.grammata.evaluation.evs.data.model.repository.EvaluationCenter;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEventEvaluationCenter;
import es.grammata.evaluation.evs.data.services.base.BaseService;
import es.grammata.evaluation.evs.mvc.controller.util.EvaluationEventEvaluationCenterInfo;

public interface EvaluationEventEvaluationCenterService extends BaseService<EvaluationEventEvaluationCenter> {
	public EvaluationCenter findEvaluationCenter(Long evaluationEventEvaluationCenterId);
	public List<EvaluationEventEvaluationCenter> findByEvaluationEvent(Long evaluationEventId);
	public List<EvaluationEventEvaluationCenterInfo> findInfoByEvaluationEvent(Long evaluationEventId);
	public List<EvaluationEventEvaluationCenter> findByEvaluationEventAndCenter(Long evaluationEventId, Long centerId);
	public List<EvaluationEventEvaluationCenterInfo> findInfoByEvaluationEventAndCenter(Long evaluationEventId, Long centerId);
	public List<Center> findCentersByEvaluationEventId(Long eeId);
	public EvaluationEventEvaluationCenter findByEvaluationEventAndEvaluationCenter(Long evaluationEventId, Long evaluationCenterId);
}
