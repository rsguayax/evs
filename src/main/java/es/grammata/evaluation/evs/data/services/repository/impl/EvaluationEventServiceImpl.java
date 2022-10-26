package es.grammata.evaluation.evs.data.services.repository.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.json.JSONObject;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import es.grammata.evaluation.evs.data.model.repository.EvaluationAssignment;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEvent;
import es.grammata.evaluation.evs.data.services.base.impl.BaseServiceImpl;
import es.grammata.evaluation.evs.data.services.repository.EvaluationEventService;
import es.grammata.evaluation.evs.mvc.controller.util.EvaluationEventInfo;

@Repository
@Transactional(readOnly = true)
public class EvaluationEventServiceImpl extends BaseServiceImpl<EvaluationEvent> implements EvaluationEventService {

    /*public EvaluationEvent findById(Long id) {
		TypedQuery<EvaluationEvent> query = this.em.createQuery("select r from " + EvaluationEvent.class.getSimpleName() +
			" r where r.id='" + id + "'", EvaluationEvent.class);
	
		List<EvaluationEvent> evaluationEvents = query.getResultList();
		EvaluationEvent evaluationEvent = null;
		if(evaluationEvents != null && evaluationEvents.size() > 0) {
		    evaluationEvent = evaluationEvents.get(0);
		}
	
		return evaluationEvent;
    }*/

	public EvaluationEvent findByCode(String code) {
		TypedQuery<EvaluationEvent> query = this.em.createQuery("select r from " + EvaluationEvent.class.getSimpleName() +
				" r where r.code='" + code + "'", EvaluationEvent.class);

		List<EvaluationEvent> evaluationEvents = query.getResultList();
		EvaluationEvent evaluationEvent = null;
		if(evaluationEvents != null && evaluationEvents.size() > 0) {
			evaluationEvent = evaluationEvents.get(0);
		}

		return evaluationEvent;
	}

	public EvaluationEvent findByIdAndUsername(Long id, String username) {
		TypedQuery<EvaluationEvent> query = this.em.createQuery(
				"SELECT ee FROM " + EvaluationEvent.class.getSimpleName() + " ee, " + EvaluationAssignment.class.getSimpleName() + 
				" ea WHERE ea.evaluationEvent.id=:id AND ea.user.username=:username AND ee.id=ea.evaluationEvent.id", EvaluationEvent.class);
		
		query.setParameter("id", id);
		query.setParameter("username", username);
		List<EvaluationEvent> evaluationEvents = query.getResultList();
		
		if (!evaluationEvents.isEmpty()) {
			return evaluationEvents.get(0);
		} else {
			return null;
		}
	}
	
	public EvaluationEventInfo findInfoByIdAndUsername(Long id, String username) {
		TypedQuery<Object[]> query = this.em.createQuery(
				"SELECT ee.id, ee.name, ee.code FROM " + EvaluationEvent.class.getSimpleName() + " ee, " + EvaluationAssignment.class.getSimpleName() +
				" ea WHERE ea.evaluationEvent.id=:id AND ea.user.username=:username AND ee.id=ea.evaluationEvent.id", Object[].class);

		query.setParameter("id", id);
		query.setParameter("username", username);
		List<Object[]> evaluationEventDataList = query.getResultList();

		if (evaluationEventDataList.size() > 0) {
			Object[] evaluationEventData = evaluationEventDataList.get(0);
			JSONObject evaluationEventInfoJson = new JSONObject();
			evaluationEventInfoJson.put("id", evaluationEventData[0]);
			evaluationEventInfoJson.put("name", evaluationEventData[1]);
			evaluationEventInfoJson.put("code", evaluationEventData[2]);

			return new EvaluationEventInfo(evaluationEventInfoJson);
		} else {
			return null;
		}
	}

	@Override
	public List<Object[]> findAllWithStudentCount() {
	    String strQuery = "select ee.id, ee.code, ee.name, ee.startdate, ee.enddate, ee.comment, count(ea.user_id) as student_count, ee.evaluation_event_type_id "
		    + "from evaluation_event ee "
		    + "left join evaluation_assignment ea on ee.id = ea.evaluationevent_id "
		    + "group by ee.id";
	    Query query = em.createNativeQuery(strQuery);
	    List<Object[]> aObjs = (List<Object[]>) query.getResultList();

	    List<Object[]> results = new ArrayList<Object[]>();
	    for (Object[] aObj : aObjs) {
		Object[] aObj2 = new Object[aObj.length + 2];
		for (int i = 0; i < aObj.length; i++) {
		    aObj2[i] = aObj[i];
		}
		aObj2[aObj.length] = getEmailNotificationCountByEvaluationEvent((BigInteger) aObj[0]);
		aObj2[aObj.length + 1] = getEmailNotificationReadCountByEvaluationEvent((BigInteger) aObj[0]);
		results.add(aObj2);
	    }

	    return results;
	}

	private BigInteger getEmailNotificationCountByEvaluationEvent(BigInteger evaluationEventId) {
	    String strQuery = "select count(*) as c "
		    + "from email_notifications "
		    + "where evaluationevent_id = ?";
	    Query query = em.createNativeQuery(strQuery);
	    query.setParameter(1, evaluationEventId);
	    BigInteger count = (BigInteger) query.getSingleResult();
	    return count;
	}

	private BigInteger getEmailNotificationReadCountByEvaluationEvent(BigInteger evaluationEventId) {
	    String strQuery = "select count(*) as c "
		    + "from email_notifications "
		    + "where read is not null and evaluationevent_id = ?";
	    Query query = em.createNativeQuery(strQuery);
	    query.setParameter(1, evaluationEventId);
	    BigInteger count = (BigInteger) query.getSingleResult();
	    return count;
	}
}
