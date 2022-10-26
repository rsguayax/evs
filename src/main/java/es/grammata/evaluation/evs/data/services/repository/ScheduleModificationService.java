package es.grammata.evaluation.evs.data.services.repository;

import java.util.List;

import es.grammata.evaluation.evs.data.model.repository.ScheduleModification;
import es.grammata.evaluation.evs.data.services.base.BaseService;
import es.grammata.evaluation.evs.mvc.controller.util.GenericInfo;

public interface ScheduleModificationService extends BaseService<ScheduleModification> {
	
	public List<ScheduleModification> findByEvaluationAssignment(Long evaluationAssignmentId);
	public List<ScheduleModification> findByEvaluationAssignmentAndCreatedBy(Long evaluationAssignmentId, String username);
	public ScheduleModification getLastByEvaluationAssignment(Long evaluationAssignmentId);
	public GenericInfo getLastInfoByEvaluationAssignment(Long evaluationAssignmentId);
	public ScheduleModification getLastByEvaluationAssignmentAndCreatedBy(Long evaluationAssignmentId, String username);
	public GenericInfo getLastInfoByEvaluationAssignmentAndCreatedBy(Long evaluationAssignmentId, String username);
}
