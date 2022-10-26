package es.grammata.evaluation.evs.data.services.repository;

import java.util.List;

import es.grammata.evaluation.evs.data.model.repository.StudentTestScheduleLog;
import es.grammata.evaluation.evs.data.services.base.BaseService;
import es.grammata.evaluation.evs.mvc.controller.util.GenericInfo;

public interface StudentTestScheduleLogService extends BaseService<StudentTestScheduleLog>  {
	
	public void markAllAsRead();
	public void markAsReadByEvaluationEvent(Long evaluationEventId);
	public Long getCountByEvaluationEvent(Long evaluationEventId);
	public List<GenericInfo> getStudentsInfoByEvaluationEvent(Long evaluationEventId);
	public List<GenericInfo> getTestsLogInfoByEvaluationAssignment(Long evaluationAssignmentId);
	public List<GenericInfo> getTestsLogInfoByEvaluationAssignmentAndMatterTestStudentIds(Long evaluationAssignmentId, List<Long> matterTestStudentIds);
	public void deleteByEvaluationEvent(Long evaluationEventId);
}
