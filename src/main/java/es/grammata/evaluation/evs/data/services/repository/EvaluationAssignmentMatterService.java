package es.grammata.evaluation.evs.data.services.repository;

import java.util.List;

import es.grammata.evaluation.evs.data.model.repository.EvaluationAssignmentMatter;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEventMatter;
import es.grammata.evaluation.evs.data.model.repository.Matter;
import es.grammata.evaluation.evs.data.services.base.BaseService;
import es.grammata.evaluation.evs.mvc.controller.util.GenericInfo;



public interface EvaluationAssignmentMatterService extends BaseService<EvaluationAssignmentMatter> {
	public List<Matter> findMattersByStudentId(Long evaluationEventId, Long studentId);	
	public EvaluationAssignmentMatter find(Long evaluationEventId, Long studentId, Long matterId);
	public List<EvaluationAssignmentMatter> findByMatter(Long evaluationEventId, Long matterId);
	public List<EvaluationEventMatter> findEvaluationEventMattersByStudentId(Long evaluationEventId, Long studentId);
	public List<EvaluationAssignmentMatter> findWithoutTest(Long evaluationEventId);
	public GenericInfo getMatterInfoById(Long id);
	public List<EvaluationAssignmentMatter> findByEvent(Long evaluationEventId);
	public EvaluationAssignmentMatter findEvaluationEventMattersByStudentIdentification(Long evaluationEventId, String identification, String matterCode,
			String periodCode, String mode);
	public List<EvaluationAssignmentMatter> findByEvaluationAssignment(Long evaluationAssignmentId);
	public EvaluationAssignmentMatter findByEvaluationAssignmentAndEvalutionEventMatter(Long evaluationAssignmentId, Long evalutionEventMatterId);
}
