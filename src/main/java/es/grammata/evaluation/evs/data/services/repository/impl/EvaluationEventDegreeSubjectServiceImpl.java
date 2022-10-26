package es.grammata.evaluation.evs.data.services.repository.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import es.grammata.evaluation.evs.data.model.repository.EvaluationEventDegreeSubject;
import es.grammata.evaluation.evs.data.services.base.impl.BaseServiceImpl;
import es.grammata.evaluation.evs.data.services.repository.EvaluationEventDegreeSubjectService;

@Repository
@Transactional(readOnly = true)
public class EvaluationEventDegreeSubjectServiceImpl extends BaseServiceImpl<EvaluationEventDegreeSubject>
		implements EvaluationEventDegreeSubjectService {

	@PersistenceContext
	protected EntityManager em;

	@Override
	public List<EvaluationEventDegreeSubject> findByEvaluationEventDegreeSubjects(Long evaluationEventId,
			Long degreeId) {
		TypedQuery<EvaluationEventDegreeSubject> query = this.em.createQuery(
				"select ea from " + EvaluationEventDegreeSubject.class.getSimpleName()
						+ " ea where ea.evaluationEvent.id = " + evaluationEventId + " and ea.degree.id = " + degreeId,
				EvaluationEventDegreeSubject.class);
		List<EvaluationEventDegreeSubject> evaluationEventDegrees = query.getResultList();
		return evaluationEventDegrees;
	}

	@Override
	public List<EvaluationEventDegreeSubject> findByEvaluationEventAndDegree(Long evaluationEventId, Long degreeId) {
		TypedQuery<EvaluationEventDegreeSubject> query = this.em.createQuery(
				"select ea from " + EvaluationEventDegreeSubject.class.getSimpleName()
						+ " ea where ea.evaluationEvent.id = " + evaluationEventId + " and ea.degree.id = " + degreeId,
				EvaluationEventDegreeSubject.class);
		List<EvaluationEventDegreeSubject> evaluationEventDegrees = query.getResultList();
		return evaluationEventDegrees;
	}

	/**
	 * 
	 */
	@Override
	public List<EvaluationEventDegreeSubject> findByEvaluationEventDegree(Long evaluationEventId,
			Long evaluationEventDegree) {
		TypedQuery<EvaluationEventDegreeSubject> query = em.createQuery("select c from "
				+ EvaluationEventDegreeSubject.class.getSimpleName() + " c where c.evaluationEvent.id = "
				+ evaluationEventId + "and c.degree.id =" + evaluationEventDegree, EvaluationEventDegreeSubject.class);
		List<EvaluationEventDegreeSubject> evaluationEventDegrees = query.getResultList();
		return evaluationEventDegrees;

	}

	@Override
	public List<EvaluationEventDegreeSubject> findByEvaluationEventDegree(Long evaluationEventDegreeSubject) {
		TypedQuery<EvaluationEventDegreeSubject> query = em.createQuery(
				"select c from " + EvaluationEventDegreeSubject.class.getSimpleName() + " c ",
				EvaluationEventDegreeSubject.class);
		List<EvaluationEventDegreeSubject> evaluationEventDegrees = query.getResultList();
		return evaluationEventDegrees;
	}

	@Override
	public List<EvaluationEventDegreeSubject> findByEvaluationEventa(Long evaluationEventId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<EvaluationEventDegreeSubject> findByEvaluationEventDegreeSubject(Long evaluationEventId,
			Long evaluationEventDegreeId, Long evaluationEventDegreeSubjectId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public EvaluationEventDegreeSubject findByUnique(Long evaluationEventId, Long degreeId, Long subjectId) {
		TypedQuery<EvaluationEventDegreeSubject> query = this.em.createQuery(
				"select ea from " + EvaluationEventDegreeSubject.class.getSimpleName() + " ea where ea.evaluationEvent.id = "
						+ evaluationEventId + " and ea.degree.id = " + degreeId
						+ " and ea.subject.id = " + subjectId,
						EvaluationEventDegreeSubject.class);
		
		List<EvaluationEventDegreeSubject> evaluationEventDegreeSubjects = query.getResultList();
		EvaluationEventDegreeSubject evaluationEventDegreeSubject = null;
		
		if (evaluationEventDegreeSubjects != null && evaluationEventDegreeSubjects.size() > 0) {
			evaluationEventDegreeSubject = evaluationEventDegreeSubjects.get(0);
		}
		
		return evaluationEventDegreeSubject;
	}
}