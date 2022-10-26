package es.grammata.evaluation.evs.data.services.repository;

import java.util.List;

import es.grammata.evaluation.evs.data.model.repository.EvaluationEventDegreeSubject;
import es.grammata.evaluation.evs.data.services.base.BaseService;


public interface EvaluationEventDegreeSubjectService extends BaseService<EvaluationEventDegreeSubject> {
	
	public List<EvaluationEventDegreeSubject> findByEvaluationEventDegree(Long evaluationEventDegree);
	
	public List<EvaluationEventDegreeSubject> findByEvaluationEventa(Long evaluationEventId);
	public List<EvaluationEventDegreeSubject> findByEvaluationEventDegree(Long evaluationEventId, Long degreID);

	public List<EvaluationEventDegreeSubject> findByEvaluationEventDegreeSubjects(Long evaluationEventId, Long degreID);
	public List<EvaluationEventDegreeSubject> findByEvaluationEventAndDegree(Long evaluationEventId, Long degreeId);
	public List<EvaluationEventDegreeSubject> findByEvaluationEventDegreeSubject(Long evaluationEventId,Long evaluationEventDegreeId,Long evaluationEventDegreeSubjectId);
	public EvaluationEventDegreeSubject findByUnique(Long evaluationEventId, Long degreeId, Long subjectId);
}
