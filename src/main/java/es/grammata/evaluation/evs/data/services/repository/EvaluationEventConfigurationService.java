package es.grammata.evaluation.evs.data.services.repository;

import es.grammata.evaluation.evs.data.model.repository.EvaluationEventConfiguration;
import es.grammata.evaluation.evs.data.services.base.BaseService;

public interface EvaluationEventConfigurationService extends BaseService<EvaluationEventConfiguration> {
	public EvaluationEventConfiguration findByEvaluationEvent(Long evaluationEventId);
}
