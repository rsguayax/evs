package es.grammata.evaluation.evs.data.services.repository.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.json.JSONObject;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import es.grammata.evaluation.evs.data.model.repository.EnrollmentRevision;
import es.grammata.evaluation.evs.data.services.base.impl.BaseServiceImpl;
import es.grammata.evaluation.evs.data.services.repository.EnrollmentRevisionService;
import es.grammata.evaluation.evs.mvc.controller.util.EnrollmentRevisionInfo;

/**
 * 
 * Clase EnrollmentServiceImpl Hereda los servicios
 * 
 * @author andres
 *
 */
@Repository
@Transactional(readOnly = true)
public class EnrollmentRevisionServiceImp extends BaseServiceImpl<EnrollmentRevision>
		implements EnrollmentRevisionService {

	@PersistenceContext
	protected EntityManager em1;

	@Override
	public void deletLogic(EnrollmentRevision enrollment) {
		// TODO Auto-generated method stub
	}

	@Override
	public List<EnrollmentRevision> findb() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveT(EnrollmentRevision entity) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<EnrollmentRevision> findByEvaluationEvent(Long evaluationEventId) {
		// TODO Auto-generated method stub
		TypedQuery<EnrollmentRevision> query = this.em.createQuery(
				"select ea from " + EnrollmentRevision.class.getSimpleName()
						+ " ea where ea.enrollment.evaluationEvent.id = " + evaluationEventId + " ",
				EnrollmentRevision.class);
		List<EnrollmentRevision> enrollmentRevision = query.getResultList();
		return enrollmentRevision;
	}
	
	@Override
	public List<EnrollmentRevisionInfo> findInfoByEvaluationEventOrderByUser(Long evaluationEventId) {
		TypedQuery<Object[]> query = this.em.createQuery("select er.id, er.enrollment.id, er.enrollment.user.firstName, er.enrollment.user.lastName, er.enrollment.user.identification, er.enrollment.user.email, er.enrollment.degree.id, er.enrollment.degree.name, er.enrollment.evaluationCenter.name, er.enrollment.priority, er.revision, er.status, er.enrollment.grade from " + EnrollmentRevision.class.getSimpleName()
				+ " er where er.enrollment.evaluationEvent.id = " + evaluationEventId
				+ " order by er.enrollment.user.id, er.enrollment.priority", Object[].class);
		
		List<Object[]> enrollmentRevisionDataList = query.getResultList();

		List<EnrollmentRevisionInfo> enrollmentInfos = new ArrayList<EnrollmentRevisionInfo>();
		for (Object[] enrollmentRevisionData : enrollmentRevisionDataList) {
			JSONObject enrollmentRevisionInfoJson = new JSONObject();
			enrollmentRevisionInfoJson.put("id", enrollmentRevisionData[0]);
			enrollmentRevisionInfoJson.put("enrollmentId", enrollmentRevisionData[1]);
			enrollmentRevisionInfoJson.put("userFirstName", enrollmentRevisionData[2]);
			enrollmentRevisionInfoJson.put("userLastName", enrollmentRevisionData[3]);
			enrollmentRevisionInfoJson.put("userIdentification", enrollmentRevisionData[4]);
			enrollmentRevisionInfoJson.put("userEmail", enrollmentRevisionData[5]);
			enrollmentRevisionInfoJson.put("degreeId", enrollmentRevisionData[6]);
			enrollmentRevisionInfoJson.put("degreeName", enrollmentRevisionData[7]);
			enrollmentRevisionInfoJson.put("evaluationCenterName", enrollmentRevisionData[8]);
			enrollmentRevisionInfoJson.put("priority", enrollmentRevisionData[9]);
			enrollmentRevisionInfoJson.put("revision", enrollmentRevisionData[10]);
			enrollmentRevisionInfoJson.put("status", enrollmentRevisionData[11]);
			enrollmentRevisionInfoJson.put("grade", enrollmentRevisionData[12]);
			enrollmentInfos.add(new EnrollmentRevisionInfo(enrollmentRevisionInfoJson));
		}
		
		return enrollmentInfos;
	}

	@Override
	public List<EnrollmentRevision> findbyEvaluationEventAndUser(Long evaluationEventID, Long userID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<EnrollmentRevision> findAllAtive() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<EnrollmentRevision> findbyEvaluationEventAndDegree(Long evaluationEventID, Long degree) {
		// TODO Auto-generated method stub
		TypedQuery<EnrollmentRevision> query = this.em.createQuery("select ea from "
				+ EnrollmentRevision.class.getSimpleName() + " ea where ea.enrollment.evaluationEvent.id = "
				+ evaluationEventID + " and ea.enrollment.degree.id = " + degree, EnrollmentRevision.class);

		List<EnrollmentRevision> enrollmentRevision = query.getResultList();
		return enrollmentRevision;
	}

	@Override
	public EnrollmentRevision findByEnrollmentId(Long enrollmentId) {
		// TODO Auto-generated method stub
		TypedQuery<EnrollmentRevision> query = this.em
				.createQuery("select b from " + EnrollmentRevision.class.getSimpleName() + " b where b.enrollment.id="
						+ enrollmentId + "  and id = (select max(id) from " + EnrollmentRevision.class.getSimpleName()
						+ " where enrollment_id=" + enrollmentId + ")", EnrollmentRevision.class);
		EnrollmentRevision e = query.getResultList().isEmpty() ? null : query.getResultList().get(0);
		return e;
	}
	
	@Override
	public List<EnrollmentRevision> findbyEvaluationEventAndDegreeStatusAvailibles(Long evaluationEventID,
			Long degree,String status) {
		// TODO Auto-generated method stub
		TypedQuery<EnrollmentRevision> query = this.em.createQuery("select er from "+EnrollmentRevision.class.getSimpleName()+" er"
				+ " where er.id in "				
				+ "(select max(ea.id) from "
				+ EnrollmentRevision.class.getSimpleName() + " ea where ea.enrollment.evaluationEvent.id = "
				+ evaluationEventID + " and ea.enrollment.degree.id = " + degree
				+ " group by ea.enrollment.id) "
				+ " and er.status in ("+status+")", EnrollmentRevision.class);
		List<EnrollmentRevision> enrollmentRevision = query.getResultList();
		return enrollmentRevision;
	}
	
	@Override
	public EnrollmentRevision findbyEvaluationEventAndUserAndPriority(Long evaluationEventID,Long userId,int priority) {
		// TODO Auto-generated method stub
		TypedQuery<EnrollmentRevision> query = this.em.createQuery("select ea from "
				+ EnrollmentRevision.class.getSimpleName() + " ea where "
				+ " id = (select max(er.id) from " + EnrollmentRevision.class.getSimpleName()+" er"
				+ " where er.enrollment.user.id = " + userId
				+ " and er.enrollment.priority = " + priority
				+ " and er.enrollment.evaluationEvent.id=" + evaluationEventID 
				+ ")", EnrollmentRevision.class);

		List<EnrollmentRevision> enrollmentRevisions = query.getResultList();
		
		EnrollmentRevision er = null;
		if(!enrollmentRevisions.isEmpty()) {
			er = enrollmentRevisions.get(0);
		}
		
		return er;
	}

	@Override
	public List<EnrollmentRevision> findbyEvaluationEventAndDegreeOrderByGrade(Long evaluationEventId, Long degreeId) {
		TypedQuery<EnrollmentRevision> query = this.em.createQuery("select ea from "
				+ EnrollmentRevision.class.getSimpleName() + " ea where ea.enrollment.evaluationEvent.id = "
				+ evaluationEventId + " and ea.enrollment.degree.id = " + degreeId
				+ " ORDER BY ea.enrollment.grade DESC", EnrollmentRevision.class);

		List<EnrollmentRevision> enrollmentRevision = query.getResultList();
		return enrollmentRevision;
	}
}
