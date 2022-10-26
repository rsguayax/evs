package es.grammata.evaluation.evs.data.services.repository.impl;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import es.grammata.evaluation.evs.data.model.repository.CorrectionRule;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEventConfiguration;
import es.grammata.evaluation.evs.data.services.base.impl.BaseServiceImpl;
import es.grammata.evaluation.evs.data.services.repository.CorrectionRuleService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationEventConfigurationService;

@Repository
@Transactional(readOnly = true)
public class EvaluationEventConfigurationServiceImpl extends BaseServiceImpl<EvaluationEventConfiguration> implements EvaluationEventConfigurationService {
	
	@Autowired
	private CorrectionRuleService correctionRuleService;
	
	@Transactional
	public void update(EvaluationEventConfiguration entity) {
		CorrectionRule correctionRule = entity.getCorrectionRule();
		if (correctionRule != null) {
			correctionRule.setEvaluationEventConfiguration(entity);
			
			if (correctionRule.getId() == null) {
				correctionRuleService.save(correctionRule);
			} else {
				correctionRuleService.update(correctionRule);
			}
		}
		
		super.update(entity);
	}
	
	public EvaluationEventConfiguration findByEvaluationEvent(Long evaluationEventId) {
		TypedQuery<EvaluationEventConfiguration> query = this.em.createQuery(
			"SELECT ec FROM " + EvaluationEventConfiguration.class.getSimpleName() + 
			" ec WHERE ec.evaluationEvent.id=:evaluationEventId", EvaluationEventConfiguration.class);
		
		query.setParameter("evaluationEventId", evaluationEventId);
		List<EvaluationEventConfiguration> configurations = query.getResultList();
		
		if(!configurations.isEmpty()) {
			return configurations.get(0);
		}
		
		return null;
	}
}
