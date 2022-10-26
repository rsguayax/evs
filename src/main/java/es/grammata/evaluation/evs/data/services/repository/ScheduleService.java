package es.grammata.evaluation.evs.data.services.repository;

import java.util.List;

import es.grammata.evaluation.evs.data.model.repository.Schedule;
import es.grammata.evaluation.evs.data.services.base.BaseService;

public interface ScheduleService extends BaseService<Schedule> {
	  public Schedule findByName(Long evaluationEventId, String name);
	  public List<Schedule> findByEvaluationEvent(Long evaluationEventId);
}
