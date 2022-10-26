package es.grammata.evaluation.evs.data.services.repository.impl;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import es.grammata.evaluation.evs.data.model.repository.CorrectionRule;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEventMatterTest;
import es.grammata.evaluation.evs.data.services.base.impl.BaseServiceImpl;
import es.grammata.evaluation.evs.data.services.repository.CorrectionRuleService;

@Repository
@Transactional(readOnly = true)
public class CorrectionRuleServiceImpl extends BaseServiceImpl<CorrectionRule> implements CorrectionRuleService {

	@Override
	public CorrectionRule findByTest(Long testId) {
		TypedQuery<CorrectionRule> query = this.em.createQuery("select cr from " + CorrectionRule.class.getSimpleName() +
				" cr where cr.test.id=:testId", CorrectionRule.class);
		query.setParameter("testId", testId);
		
		List<CorrectionRule> correctionRules = query.getResultList();
		CorrectionRule correctionRule = null;
		if(correctionRules != null && correctionRules.size() > 0) {
			correctionRule = correctionRules.get(0);
		}

		return correctionRule;
	}

	@Override
	public CorrectionRule findByMatter(Long matterId) {
		TypedQuery<CorrectionRule> query = this.em.createQuery("select cr from " + CorrectionRule.class.getSimpleName() +
				" cr where cr.matter.id=:matterId", CorrectionRule.class);
		query.setParameter("matterId", matterId);
		
		List<CorrectionRule> correctionRules = query.getResultList();
		CorrectionRule correctionRule = null;
		if(correctionRules != null && correctionRules.size() > 0) {
			correctionRule = correctionRules.get(0);
		}

		return correctionRule;
	}

	@Override
	public CorrectionRule findByEvaluationEvent(Long evaluationEventId) {
		TypedQuery<CorrectionRule> query = this.em.createQuery("select cr from " + CorrectionRule.class.getSimpleName() +
				" cr where cr.evaluationEventConfiguration.evaluationEvent.id=:evaluationEventId", CorrectionRule.class);
		query.setParameter("evaluationEventId", evaluationEventId);
		
		List<CorrectionRule> correctionRules = query.getResultList();
		CorrectionRule correctionRule = null;
		if(correctionRules != null && correctionRules.size() > 0) {
			correctionRule = correctionRules.get(0);
		}

		return correctionRule;
	}

	@Override
	public CorrectionRule getDefaultCorrectionRule() {
		TypedQuery<CorrectionRule> query = this.em.createQuery("select cr from " + CorrectionRule.class.getSimpleName() +
				" cr where cr.test is null and cr.matter is null and cr.evaluationEventConfiguration is null", CorrectionRule.class);
		
		List<CorrectionRule> correctionRules = query.getResultList();
		CorrectionRule correctionRule = null;
		if(correctionRules != null && correctionRules.size() > 0) {
			correctionRule = correctionRules.get(0);
		}

		return correctionRule;
	}

	@Override
	public CorrectionRule getEvaluationEventMatterTestCorrectionRule(EvaluationEventMatterTest evaluationEventMatterTest) {
		CorrectionRule correctionRule = findByTest(evaluationEventMatterTest.getTest().getId());
		
		if (correctionRule == null) {
			correctionRule = findByMatter(evaluationEventMatterTest.getEvaluationEventMatter().getMatter().getId());
			
			if (correctionRule == null) {
				correctionRule = findByEvaluationEvent(evaluationEventMatterTest.getEvaluationEventMatter().getEvaluationEvent().getId());
				
				if (correctionRule == null) {
					correctionRule = getDefaultCorrectionRule();
				}
			}
		}
		
		if (evaluationEventMatterTest.getEvaluationEventMatter().getEvaluationEvent().isAdmissionOrComplexiveType()) {
			correctionRule = new CorrectionRule();
			correctionRule.setMinGrade(0f);
			correctionRule.setMaxGrade(1f);
			correctionRule.setType("UTPL_RULE");
		}
		
		return correctionRule;
	}
}
