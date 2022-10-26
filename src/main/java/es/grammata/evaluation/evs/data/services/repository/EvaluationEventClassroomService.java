package es.grammata.evaluation.evs.data.services.repository;

import java.util.List;

import es.grammata.evaluation.evs.data.model.repository.Classroom;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEventClassroom;
import es.grammata.evaluation.evs.data.services.base.BaseService;
import es.grammata.evaluation.evs.mvc.controller.util.GenericInfo;

public interface EvaluationEventClassroomService extends BaseService<EvaluationEventClassroom> {
	public List<GenericInfo> findInfoByEventCenter(Long eventCenterId);
	public List<EvaluationEventClassroom> findByEventCenter(Long eventCenterId);
	public Classroom loadClassroom(Long evaluationEventClassroomId);
}
