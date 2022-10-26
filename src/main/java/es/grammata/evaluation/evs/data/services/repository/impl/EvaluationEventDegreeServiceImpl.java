package es.grammata.evaluation.evs.data.services.repository.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import es.grammata.evaluation.evs.data.model.repository.EvaluationEventDegree;
import es.grammata.evaluation.evs.data.services.base.impl.BaseServiceImpl;
import es.grammata.evaluation.evs.data.services.repository.EvaluationEventDegreeService;

@Repository
@Transactional(readOnly = true)
public class EvaluationEventDegreeServiceImpl extends BaseServiceImpl<EvaluationEventDegree>
		implements EvaluationEventDegreeService {

	@PersistenceContext
	protected EntityManager em;

	@Transactional
	public void delete(EvaluationEventDegree entity) {
		String query = "DELETE FROM " + EvaluationEventDegree.class.getSimpleName() + " WHERE id = " + entity.getId();
		em.createNativeQuery(query).executeUpdate();
		em.flush();
	}

	@Transactional
	public void update(EvaluationEventDegree entity) {
		if (entity.getId() != null) {
			em.merge(entity);
			em.flush();
		}
	}

	@Transactional
	public void save(EvaluationEventDegree entity) {
		em.persist(entity);
		em.flush();
	}

	public void deleteAll(Long evaluationEventId, EvaluationEventDegree evaluationEventDegree) {

		String query = "DELETE FROM evaluation_event_degree_day " + " WHERE evaluationeventdegree_id = "
				+ evaluationEventDegree.getId();
		em.createNativeQuery(query).executeUpdate();
		em.flush();
		query = "DELETE FROM evaluation_event_degree_test " + " WHERE evaluationeventdegree_id = "
				+ evaluationEventDegree.getId();
		em.createNativeQuery(query).executeUpdate();
		em.flush();
		query = "DELETE FROM evaluation_assignment_degree " + " WHERE evaluationeventdegree_id = "
				+ evaluationEventDegree.getId();

		em.createNativeQuery(query).executeUpdate();
		em.flush();
		query = "DELETE FROM evaluation_assignment ea "
				+ " WHERE NOT EXISTS (SELECT eam.id FROM evaluation_assignment_degree eam "
				+ " WHERE eam.evaluationassignment_id = ea.id) and ea.evaluationevent_id = " + evaluationEventId;
		em.createNativeQuery(query).executeUpdate();
		em.flush();
		query = "DELETE FROM evaluation_event_degree_evaluation_type " + " WHERE evaluationeventdegree_id = "
				+ evaluationEventDegree.getId();
		em.createNativeQuery(query).executeUpdate();
		em.flush();
		this.delete(evaluationEventDegree);
	}

	@Override
	public EvaluationEventDegree findByUnique(Long evaluationEventId, Long degreeId) {
		TypedQuery<EvaluationEventDegree> query = this.em.createQuery(
				"select ea from " + EvaluationEventDegree.class.getSimpleName() + " ea where ea.evaluationEvent.id = "
						+ evaluationEventId + " and ea.degree.id = " + degreeId,
				EvaluationEventDegree.class);
		List<EvaluationEventDegree> evaluationEventDegrees = query.getResultList();
		EvaluationEventDegree evaluationEventDegree = null;
		if (evaluationEventDegrees != null && evaluationEventDegrees.size() > 0) {
			evaluationEventDegree = evaluationEventDegrees.get(0);
		}

		return evaluationEventDegree;
	}

	@Override
	public List<EvaluationEventDegree> findByParams(Long evaluationEventId, Long degreeId) {
		TypedQuery<EvaluationEventDegree> query = this.em.createQuery(
				"select ea from " + EvaluationEventDegree.class.getSimpleName() + " ea where ea.evaluationEvent.id = "
						+ evaluationEventId + " and ea.degree.id = " + degreeId,
				EvaluationEventDegree.class);
		List<EvaluationEventDegree> evaluationEventDegrees = query.getResultList();
		return evaluationEventDegrees;
	}

	@Override
	public List<EvaluationEventDegree> findByEvaluationEvent(Long evaluationEventId) {
		TypedQuery<EvaluationEventDegree> query = em.createQuery("select c from "
				+ EvaluationEventDegree.class.getSimpleName() + " c where c.evaluationEvent.id = " + evaluationEventId,
				EvaluationEventDegree.class);
		List<EvaluationEventDegree> evaluationEventDegrees = query.getResultList();
		return evaluationEventDegrees;
	}
}
