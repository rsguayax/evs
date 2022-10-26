package es.grammata.evaluation.evs.data.services.repository;

import java.util.List;

import es.grammata.evaluation.evs.data.model.repository.GenericScheduleLog;
import es.grammata.evaluation.evs.data.services.base.BaseService;

public interface GenericScheduleLogService extends BaseService<GenericScheduleLog>  {
	
	public List<GenericScheduleLog> getLogGenericByEvaluationEvent(Long evaluationEventId);
	public void markAsReadByEvaluationEvent(Long evaluationEventId);
	public Long getCountByEvaluationEvent(Long evaluationEventId);
	public void deleteByEvaluationEvent(Long evaluationEventId);
}
