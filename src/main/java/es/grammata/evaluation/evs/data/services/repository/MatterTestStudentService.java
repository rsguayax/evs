package es.grammata.evaluation.evs.data.services.repository;

import java.util.Date;
import java.util.List;

import es.grammata.evaluation.evs.data.model.repository.EvaluationAssignment;
import es.grammata.evaluation.evs.data.model.repository.EvaluationAssignmentMatter;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEventMatterTest;
import es.grammata.evaluation.evs.data.model.repository.MatterTestStudent;
import es.grammata.evaluation.evs.data.model.repository.StudentEvaluation;
import es.grammata.evaluation.evs.data.services.base.BaseService;
import es.grammata.evaluation.evs.mvc.controller.util.GenericInfo;
import es.grammata.evaluation.evs.mvc.controller.util.MatterTestStudentInfo;

public interface MatterTestStudentService extends BaseService<MatterTestStudent> {

	public MatterTestStudent findByUnique(EvaluationEventMatterTest evaluationEventMatterTest, EvaluationAssignmentMatter evaluationAssignmentMatter);
	public List<MatterTestStudent> findByEvaluationEventMatterTest(EvaluationEventMatterTest evaluationEventMatterTest, boolean published);
	public List<MatterTestStudent> findByMatter(Long evaluationEventId, Long matterId);
	public List<MatterTestStudentInfo> findInfoWithoutStudentEvaluationByEvaluationAssignment(Long evaluationAssignmentId);
	public List<MatterTestStudent> findWithoutStudentEvaluationByEvaluationAssignment(Long evaluationAssignmentId);
	public List<MatterTestStudent> findWithStudentEvaluationByEvaluationAssignment(Long evaluationAssignmentId);
	public List<MatterTestStudentInfo> findInfoByEvaluationAssignment(Long evaluationAssignmentId);
	public MatterTestStudentInfo findInfoById(Long id);
	public void updateStudentEvaluation(StudentEvaluation studentEvaluation, List<MatterTestStudentInfo> matterTestStudentInfos);
	public void deleteStudentEvaluation(Long matterTestStudentId);
	public void deleteAllByStudentEvaluation(Long studentEvaluationId);
	public List<MatterTestStudent> findByEvaluationAssignmentMatter(EvaluationAssignmentMatter evaluationAssignmentMatter);
	public List<MatterTestStudentInfo> findInfoByEvaluationAssignmentMatter(Long evaluationAssignmentMatterId);
	public List<MatterTestStudent> findByEvaluationAssignment(EvaluationAssignment evaluationAssignment);
	public void deleteByEvaluationAssignmentMatter(EvaluationAssignmentMatter evaluationAssignmentMatter);
	public List<MatterTestStudent> findByEvaluationEventMatter(Long evaluationEventMatterId);
	public List<MatterTestStudent> findByEvaluationEventMatterTestWithSession(Long evaluationEventMatterTestId);
	public GenericInfo getTestsSchedulesInfo(Long evaluationEventId);
	public List<MatterTestStudent> findWithoutStudentEvaluationByEvaluationEvent(Long evaluationEventId);
	public List<MatterTestStudent> findByEvaluationEventMatterTestPublishedOrNotified(EvaluationEventMatterTest evaluationEventMatterTest);
	public Long countByEvaluationEventMatterTestEvaluationType(Long eemId, Long etId);
	public List<MatterTestStudent> findByEvaluationEventMatterTest(EvaluationEventMatterTest evaluationEventMatterTest);
	public List<MatterTestStudentInfo> findInfoByEvaluationEventMatterTest(Long evaluationEventMatterTestId);
	public boolean checkEvaluationEventByParameters(Long evaluationEventId, Long userId, Long testId, Date testDate);
	List<Object[]> findEvaluationSchedule(Long evaluationAssignmentId);
	public List<MatterTestStudent> findByStudentEvaluation(Long studentEvaluationId);
	public List<MatterTestStudentInfo> findInfoWithoutStudentEvaluationByEvaluationEvent(Long evaluationEventId);
	public List<MatterTestStudentInfo> findInfoWithStudentEvaluationByEvaluationEvent(Long evaluationEventId);
}

