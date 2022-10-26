package es.grammata.evaluation.evs.data.services.repository;

import java.util.List;

import es.grammata.evaluation.evs.data.model.repository.StudentEvaluation;
import es.grammata.evaluation.evs.data.services.base.BaseService;
import es.grammata.evaluation.evs.mvc.controller.util.GenericInfo;

public interface StudentEvaluationService extends BaseService<StudentEvaluation> {
	public List<StudentEvaluation> findByEvaluationAssignment(Long evaluationAssignmentId);
	public StudentEvaluation findByEvaluationAssignmentAndClassroomTimeBlock(Long evaluationAssignmentId, Long classroomTimeBlockId);
	public List<StudentEvaluation> findByEvaluationAssignmentAndClassroomTimeBlockList(Long evaluationAssignmentId, Long classroomTimeBlockId);
	public List<GenericInfo> findInfoByClassroomTimeBlock(Long classroomTimeBlockId);
	public List<StudentEvaluation> findByEvaluationEventAndUser(Long evaluationEventId, String identification);
	public List<StudentEvaluation> findByEvaluationEvent(Long evaluationEventId);
	public List<Long> findIdsByEvaluationEvent(Long evaluationEventId);
	public void deleteById(Long studentEvaluationId);
}
