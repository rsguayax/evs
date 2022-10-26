package es.grammata.evaluation.evs.data.services.repository;

import java.util.List;

import es.grammata.evaluation.evs.data.model.repository.EvaluationAssignment;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEvent;
import es.grammata.evaluation.evs.data.model.repository.User;
import es.grammata.evaluation.evs.data.services.base.BaseService;
import es.grammata.evaluation.evs.mvc.controller.util.EvaluationAssignmentInfo;
import es.grammata.evaluation.evs.mvc.controller.util.StudentSearchResult;



public interface EvaluationAssignmentService extends BaseService<EvaluationAssignment> {
	public List<User> findUsersByEvaluationEvent(EvaluationEvent evaluationEvent);
	public List<User> findUsersByEvaluationEvent(Long evaluationEventId);
	public List<User> findUnnotifiedUsersByEvaluationEvent(Long evaluationEventId);
	public EvaluationAssignment findByUnique(EvaluationEvent evaluationEvent, User user);
	public List<EvaluationAssignment> findByUsername(String username);
	public List<EvaluationAssignmentInfo> findInfoByUsername(String username);
	public EvaluationAssignment findByUsernameAndEvaluationEvent(String username, Long evaluationEventId);
	public EvaluationAssignmentInfo findInfoByUsernameAndEvaluationEvent(String username, Long evaluationEventId);
	public List<StudentSearchResult> findStudents(EvaluationEvent evaluationEvent, String searchFor);
	public List<User> findUsersByEvaluationEvent(EvaluationEvent evaluationEvent, int page, int pageSize);
	public long totalEvaluationEventStudents(Long evaluationEventId);
	public List<User> findUsersByEvaluationEvent(EvaluationEvent evaluationEvent, int page, int pageSize, String q);
	public long totalEvaluationEventStudents(Long evaluationEventId, String q);
	public List<EvaluationAssignment> findWithTestsWithoutScheduleByEvaluationEvent(Long evaluationEventId);
	public List<EvaluationAssignment> findByEvaluationEvent(Long evaluationEventId);
	public EvaluationAssignmentInfo findInfoById(Long id);
}
