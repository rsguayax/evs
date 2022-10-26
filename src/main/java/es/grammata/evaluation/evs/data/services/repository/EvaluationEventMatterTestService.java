package es.grammata.evaluation.evs.data.services.repository;

import java.util.List;

import es.grammata.evaluation.evs.data.model.repository.EvaluationEventMatter;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEventMatterTest;
import es.grammata.evaluation.evs.data.model.repository.Test;
import es.grammata.evaluation.evs.data.services.base.BaseService;
import es.grammata.evaluation.evs.mvc.controller.util.EvaluationReport;



public interface EvaluationEventMatterTestService extends BaseService<EvaluationEventMatterTest> {
	public EvaluationEventMatterTest findByUnique(Test test, EvaluationEventMatter evaluationEventMatter);
	public boolean isAssignedTest(Test test);
	public List<EvaluationEventMatterTest> findByEvaluationEvent(Long id, boolean trestComplete);
	public List<EvaluationEventMatterTest> findByEvaluationEventCode(String code, boolean testComplete);
	public List<EvaluationEventMatterTest> findByEvaluationEventMatter(Long eemId);
	public List<EvaluationReport> resportEvaluationTests(Long evId, List<Long> matterIds);
	public List<EvaluationEventMatterTest> getTestsByEvaluationEventAndBankDepartment(Long evaluationEventId, Long departmentId);
}
