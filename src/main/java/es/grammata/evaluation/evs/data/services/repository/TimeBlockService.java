package es.grammata.evaluation.evs.data.services.repository;

import java.util.List;

import es.grammata.evaluation.evs.data.model.repository.TimeBlock;
import es.grammata.evaluation.evs.data.services.base.BaseService;

public interface TimeBlockService extends BaseService<TimeBlock> {
	public List<TimeBlock> findRelatedByEvaluationEvent(Long evaluationEventId);
}
