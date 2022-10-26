package es.grammata.evaluation.evs.data.services.repository;

import es.grammata.evaluation.evs.data.model.repository.CorrectionRule;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEventMatterTest;
import es.grammata.evaluation.evs.data.services.base.BaseService;

public interface CorrectionRuleService extends BaseService<CorrectionRule> {
	public CorrectionRule findByTest(Long testId);
	public CorrectionRule findByMatter(Long matterId);
	public CorrectionRule findByEvaluationEvent(Long evaluationEventId);
	public CorrectionRule getDefaultCorrectionRule();
	public CorrectionRule getEvaluationEventMatterTestCorrectionRule(EvaluationEventMatterTest evaluationEventMatterTest);
}
