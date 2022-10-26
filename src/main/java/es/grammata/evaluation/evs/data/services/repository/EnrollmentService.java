package es.grammata.evaluation.evs.data.services.repository;

import java.util.List;

import es.grammata.evaluation.evs.data.model.repository.Enrollment;
import es.grammata.evaluation.evs.data.services.base.BaseService;
import es.grammata.evaluation.evs.mvc.controller.util.EnrollmentInfo;

public interface EnrollmentService extends BaseService<Enrollment>{

	/**
	 * Metodo que permite eliminar una inscripcion cambiando su estado de active a
	 * false.
	 * 
	 * @param entity objeto inscripcion
	 */
	public void deletLogic(Enrollment enrollment);;

	/**
	 * Recupera una lista de inscripciones, con estado active = true
	 * 
	 * @return Lista de tipo inscripcion con estado activo = true
	 */
	public List<Enrollment> findAllAtive();
	
	/**
	 * Recupera una lista de inscripciones de un evento de evaluacion
	 * 
	 * @return Lista de tipo inscripcion 
	 */

	public List<Enrollment> findByEvaluationEvent(Long evaluationEventId);
	
	public List<EnrollmentInfo> findInfoByEvaluationEvent(Long evaluationEventId);
	
	public List<Enrollment> findByEvaluationEventOrderByUser(Long evaluationEventId);
	
	public List<EnrollmentInfo> findInfoByEvaluationEventOrderByUser(Long evaluationEventId);
	
	public void saveT(Enrollment entity);
	
	public List<Enrollment> findbyEvaluationEventAndUser(Long evaluationEventID,Long userID);
	
	public Enrollment findbyEvaluationEventAndUserWithPriority1(Long evaluationEventId,Long userId);
	
	public Enrollment findbyEvaluationEventAndUserAndDegree(Long evaluationEventId,Long userId,Long degreeId);
	
	public List<Enrollment> findbyEvaluationEventAndDegree(Long evaluationEventID,Long degreeID);
	
	public List<Enrollment> findbyEvaluationEventAndDegreeOrderByGrade(Long evaluationEventId, Long degreeId);
	
	public List<Enrollment> findbyEvaluationEventWithPriority(Long evaluationEventId, int priority);
}
