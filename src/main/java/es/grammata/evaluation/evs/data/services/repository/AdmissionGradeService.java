package es.grammata.evaluation.evs.data.services.repository;

import java.util.List;

import es.grammata.evaluation.evs.data.model.repository.AdmissionGrade;
import es.grammata.evaluation.evs.data.services.base.BaseService;

public interface AdmissionGradeService extends BaseService<AdmissionGrade>{
		
	public List<AdmissionGrade>	findAll();
	public List<AdmissionGrade>	findByEvaluationEventId(Long evaluationEventId);
	public List<AdmissionGrade>	findByEvaluationEventOrderByGrade(Long evaluationEventId);
	public List<Object> findAllQualificationByDegreeAndUser(Long degreeId,Long userId);
}
