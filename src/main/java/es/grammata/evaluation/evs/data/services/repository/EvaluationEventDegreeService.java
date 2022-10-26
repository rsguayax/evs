package es.grammata.evaluation.evs.data.services.repository;

import java.util.List;

import es.grammata.evaluation.evs.data.model.repository.EvaluationEventDegree;
import es.grammata.evaluation.evs.data.services.base.BaseService;


public interface EvaluationEventDegreeService extends BaseService<EvaluationEventDegree> {
	public void delete(EvaluationEventDegree entity);
	public void update(EvaluationEventDegree entity);
	public void save(EvaluationEventDegree entity);
	public void deleteAll(Long evaluationEventId, EvaluationEventDegree evaluationEventDegree);
	public EvaluationEventDegree findByUnique(Long evaluationEventId, Long degreeId);
	public List<EvaluationEventDegree> findByParams(Long evaluationEventId, Long degreeId);
	public List<EvaluationEventDegree> findByEvaluationEvent(Long evaluationEventId);
	}
