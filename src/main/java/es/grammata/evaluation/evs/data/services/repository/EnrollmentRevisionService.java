package es.grammata.evaluation.evs.data.services.repository;

import java.util.List;

import es.grammata.evaluation.evs.data.model.repository.EnrollmentRevision;
import es.grammata.evaluation.evs.data.services.base.BaseService;
import es.grammata.evaluation.evs.mvc.controller.util.EnrollmentRevisionInfo;

public interface EnrollmentRevisionService extends BaseService<EnrollmentRevision>{

	/**
	 * Metodo que permite eliminar una inscripcion cambiando su estado de active a
	 * false.
	 * 
	 * @param entity objeto inscripcion
	 */
	public void deletLogic(EnrollmentRevision enrollment);

	/**
	 * Recupera una lista de inscripciones, con estado active = true
	 * 
	 * @return Lista de tipo inscripcion con estado activo = true
	 */
	public List<EnrollmentRevision> findAllAtive();
	
	/**
	 * Recupera una lista de inscripciones de un evento de evaluacion
	 * 
	 * @return Lista de tipo inscripcion 
	 */

	public List<EnrollmentRevision> findByEvaluationEvent(Long evaluationEventId);
	
	public List<EnrollmentRevisionInfo> findInfoByEvaluationEventOrderByUser(Long evaluationEventId);
	
	/**
	 * Recupera una lista de inscripciones de una titulacion
	 * 
	 * @return Lista de tipo inscripcion 
	 */
	public List<EnrollmentRevision> findb();
	
	public void saveT(EnrollmentRevision entity);
	
	public List<EnrollmentRevision> findbyEvaluationEventAndUser(Long evaluationEventID,Long userID);
	
	public EnrollmentRevision findByEnrollmentId(Long enrollmentId);
	
	
	public List<EnrollmentRevision> findbyEvaluationEventAndDegree(Long evaluationEventID,Long degree);
	
	public List<EnrollmentRevision> findbyEvaluationEventAndDegreeStatusAvailibles(Long evaluationEventID,Long degree,String status);
	
	public EnrollmentRevision findbyEvaluationEventAndUserAndPriority(Long evaluationEventID,Long userId,int priority);
	
	public List<EnrollmentRevision> findbyEvaluationEventAndDegreeOrderByGrade(Long evaluationEventId, Long degreeId);
}
