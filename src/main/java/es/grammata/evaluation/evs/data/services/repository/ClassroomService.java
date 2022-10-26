package es.grammata.evaluation.evs.data.services.repository;

import java.util.List;

import es.grammata.evaluation.evs.data.model.repository.Classroom;
import es.grammata.evaluation.evs.data.services.base.BaseService;



public interface ClassroomService extends BaseService<Classroom> {
	public boolean existsClassroom(String name, Long evaluationCenterId);
	public boolean existsClassroomUpdate(String name, Long evaluationCenterId, Long currentId);
	public List<Classroom> findByEvaluationCenter(Long evaluationCenterId);
}
