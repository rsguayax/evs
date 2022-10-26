package es.grammata.evaluation.evs.data.services.repository.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.json.JSONObject;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import es.grammata.evaluation.evs.data.model.repository.Enrollment;
import es.grammata.evaluation.evs.data.services.base.impl.BaseServiceImpl;
import es.grammata.evaluation.evs.data.services.repository.EnrollmentService;
import es.grammata.evaluation.evs.mvc.controller.util.EnrollmentInfo;

/**
 * 
 * Clase EnrollmentServiceImpl Hereda los servicios
 * 
 * @author andres
 *
 */
@Repository
@Transactional(readOnly = true)
public class EnrollmentServiceImpl extends BaseServiceImpl<Enrollment> implements EnrollmentService {

	@PersistenceContext
	protected EntityManager em1;

	@Override
	public List<Enrollment> findAllAtive() {
		TypedQuery<Enrollment> query = this.em.createQuery(
				"select r from " + Enrollment.class.getSimpleName() + " r where r.active='" + true + "'",
				Enrollment.class);
		List<Enrollment> enrollments = query.getResultList();
		return enrollments;
	}

	@Override
	public void deletLogic(Enrollment enrollment) {
		// TODO Auto-generated method stub
		if (enrollment.getId() != null) {
			em.merge(enrollment);
			em.flush();
		}
	}

	@Override
	public void saveT(Enrollment entity) {
		em1.merge(entity);
		em1.flush();
	}

	@Override
	public List<Enrollment> findByEvaluationEvent(Long evaluationEventId) {
		TypedQuery<Enrollment> query = em.createQuery("select c from " + Enrollment.class.getSimpleName()
				+ " c where c.evaluationEvent.id = " + evaluationEventId + " and c.active=" + true
				+ " order by c.grade desc", Enrollment.class);
		List<Enrollment> es = query.getResultList();
		return es;
	}
	
	@Override
	public List<EnrollmentInfo> findInfoByEvaluationEvent(Long evaluationEventId) {
		TypedQuery<Object[]> query = this.em.createQuery("select e.id, e.user.firstName, e.user.lastName, e.user.identification, e.user.email, e.degree.name, e.evaluationCenter.name, e.priority from " + Enrollment.class.getSimpleName()
				+ " e where e.evaluationEvent.id = " + evaluationEventId + " and e.active=" + true, Object[].class);
		
		List<Object[]> enrollmentDataList = query.getResultList();

		List<EnrollmentInfo> enrollmentInfos = new ArrayList<EnrollmentInfo>();
		for (Object[] enrollmentData : enrollmentDataList) {
			JSONObject enrollmentInfoJson = new JSONObject();
			enrollmentInfoJson.put("id", enrollmentData[0]);
			enrollmentInfoJson.put("userFirstName", enrollmentData[1]);
			enrollmentInfoJson.put("userLastName", enrollmentData[2]);
			enrollmentInfoJson.put("userIdentification", enrollmentData[3]);
			enrollmentInfoJson.put("userEmail", enrollmentData[4]);
			enrollmentInfoJson.put("degreeName", enrollmentData[5]);
			enrollmentInfoJson.put("evaluationCenterName", enrollmentData[6]);
			enrollmentInfoJson.put("priority", enrollmentData[7]);
			enrollmentInfos.add(new EnrollmentInfo(enrollmentInfoJson));
		}
		
		return enrollmentInfos;
	}
	
	@Override
	public List<Enrollment> findByEvaluationEventOrderByUser(Long evaluationEventId) {
		TypedQuery<Enrollment> query = em.createQuery("select c from " + Enrollment.class.getSimpleName()
				+ " c where c.evaluationEvent.id = " + evaluationEventId + " and c.active=" + true
				+ " order by c.user.id, c.priority", Enrollment.class);
		List<Enrollment> es = query.getResultList();
		return es;
	}
	
	@Override
	public List<EnrollmentInfo> findInfoByEvaluationEventOrderByUser(Long evaluationEventId) {
		TypedQuery<Object[]> query = this.em.createQuery("select e.id, e.user.firstName, e.user.lastName, e.user.identification, e.user.email, e.degree.name, e.evaluationCenter.name, e.priority from " + Enrollment.class.getSimpleName()
				+ " e where e.evaluationEvent.id = " + evaluationEventId + " and e.active=" + true
				+ " order by e.user.id, e.priority", Object[].class);
		
		List<Object[]> enrollmentDataList = query.getResultList();

		List<EnrollmentInfo> enrollmentInfos = new ArrayList<EnrollmentInfo>();
		for (Object[] enrollmentData : enrollmentDataList) {
			JSONObject enrollmentInfoJson = new JSONObject();
			enrollmentInfoJson.put("id", enrollmentData[0]);
			enrollmentInfoJson.put("userFirstName", enrollmentData[1]);
			enrollmentInfoJson.put("userLastName", enrollmentData[2]);
			enrollmentInfoJson.put("userIdentification", enrollmentData[3]);
			enrollmentInfoJson.put("userEmail", enrollmentData[4]);
			enrollmentInfoJson.put("degreeName", enrollmentData[5]);
			enrollmentInfoJson.put("evaluationCenterName", enrollmentData[6]);
			enrollmentInfoJson.put("priority", enrollmentData[7]);
			enrollmentInfos.add(new EnrollmentInfo(enrollmentInfoJson));
		}
		
		return enrollmentInfos;
	}

	@Override
	public List<Enrollment> findbyEvaluationEventAndUser(Long evaluationEventID, Long userID) {
		// TODO Auto-generated method stub
		TypedQuery<Enrollment> query = em
				.createQuery(
						"select c from " + Enrollment.class.getSimpleName() + " c where c.evaluationEvent.id = "
								+ evaluationEventID + " and c.user.id=" + userID + " and c.active=" + true
								+ " order by c.priority",
						Enrollment.class);
		List<Enrollment> es = query.getResultList();
		return es;
	}
	
	@Override
	public Enrollment findbyEvaluationEventAndUserWithPriority1(Long evaluationEventId,Long userId) {
		TypedQuery<Enrollment> query = em .createQuery("select c from " + Enrollment.class.getSimpleName() + " c where c.evaluationEvent.id = "
				+ evaluationEventId + " and c.user.id=" + userId 
				+ " and c.priority=1 and c.active=" + true,
				Enrollment.class);
		
		Enrollment enrollment = null;
		List<Enrollment> es = query.getResultList();
		if (es.size() > 0) {
			enrollment = es.get(0);
		}
		return enrollment;
	}

	@Override
	public Enrollment findbyEvaluationEventAndUserAndDegree(Long evaluationEventId, Long userId, Long degreeId) {
		// TODO Auto-generated method stub
		TypedQuery<Enrollment> query = em.createQuery("select c from " + Enrollment.class.getSimpleName()
				+ " c where c.evaluationEvent.id = " + evaluationEventId + " and c.user.id=" + userId + " and c.active="
				+ true + " and c.degree.id= " + degreeId, Enrollment.class);
		
		Enrollment enrollment = null;
		List<Enrollment> es = query.getResultList();
		if (es.size() > 0) {
			enrollment = es.get(0);
		}
		return enrollment;
	}

	@Override
	public List<Enrollment> findbyEvaluationEventAndDegree(Long evaluationEventID, Long degreeID) {
		TypedQuery<Enrollment> query = em.createQuery(
				"select c from " + Enrollment.class.getSimpleName() + " c where c.evaluationEvent.id = "
						+ evaluationEventID + " and c.active=" + true + " and c.degree.id= " + degreeID,
				Enrollment.class);
		List<Enrollment> es = query.getResultList();
		return es;
	}
	
	@Override
	public List<Enrollment> findbyEvaluationEventAndDegreeOrderByGrade(Long evaluationEventId, Long degreeId) {
		TypedQuery<Enrollment> query = em.createQuery(
				"select c from " + Enrollment.class.getSimpleName() + " c where c.evaluationEvent.id = "
						+ evaluationEventId + " and c.active=" + true + " and c.degree.id= " + degreeId
						+ " ORDER BY c.grade DESC",
				Enrollment.class);
		List<Enrollment> es = query.getResultList();
		return es;
	}
	
	@Override
	public List<Enrollment> findbyEvaluationEventWithPriority(Long evaluationEventId, int priority) {
		TypedQuery<Enrollment> query = em .createQuery("select c from " + Enrollment.class.getSimpleName() 
				+ " c where c.evaluationEvent.id = " + evaluationEventId
				+ " and c.priority=" + priority + " and c.active=" + true,
				Enrollment.class);
		
		List<Enrollment> es = query.getResultList();
		return es;
	}
}
