package es.grammata.evaluation.evs.data.services.repository;

import java.util.List;

import es.grammata.evaluation.evs.data.model.repository.EvaluationEvent;
import es.grammata.evaluation.evs.data.services.base.BaseService;
import es.grammata.evaluation.evs.mvc.controller.util.EvaluationEventInfo;

public interface EvaluationEventService extends BaseService<EvaluationEvent> {
	//public EvaluationEvent findById(Long id);
	public EvaluationEvent findByCode(String code);
	public EvaluationEvent findByIdAndUsername(Long id, String username);
	public EvaluationEventInfo findInfoByIdAndUsername(Long id, String username);
	List<Object[]> findAllWithStudentCount();
}
