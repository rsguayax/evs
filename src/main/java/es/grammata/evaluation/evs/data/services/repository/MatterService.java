package es.grammata.evaluation.evs.data.services.repository;

import java.util.List;

import es.grammata.evaluation.evs.data.model.repository.Matter;
import es.grammata.evaluation.evs.data.services.base.BaseService;

public interface MatterService extends BaseService<Matter> {
	
	public List<Matter> getMattersByEvaluationEvent(Long id, boolean hasBank);
	public void updateData(Matter newMatter);
	public Matter findByCode(String code);
	public Matter saveOrLoadByCode(Matter matter);
	public List<Matter> getMattersByEvaluationEventType(Long evaluationEventTypeId);
	public List<Matter> getMattersWithoutBank();
	public int countWithoutBank();
}

