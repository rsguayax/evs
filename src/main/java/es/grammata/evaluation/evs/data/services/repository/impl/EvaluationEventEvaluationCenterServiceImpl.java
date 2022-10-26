package es.grammata.evaluation.evs.data.services.repository.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;

import org.json.JSONObject;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import es.grammata.evaluation.evs.data.model.repository.Center;
import es.grammata.evaluation.evs.data.model.repository.EvaluationCenter;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEventEvaluationCenter;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEventEvaluationCenterCenter;
import es.grammata.evaluation.evs.data.services.base.impl.BaseServiceImpl;
import es.grammata.evaluation.evs.data.services.repository.EvaluationEventEvaluationCenterService;
import es.grammata.evaluation.evs.mvc.controller.util.EvaluationEventEvaluationCenterInfo;

@Repository
@Transactional(readOnly = true)
public class EvaluationEventEvaluationCenterServiceImpl extends BaseServiceImpl<EvaluationEventEvaluationCenter> implements EvaluationEventEvaluationCenterService {

	
	public EvaluationCenter findEvaluationCenter(Long evaluationEventEvaluationCenterId) {
		TypedQuery<EvaluationCenter> query = this.em.createQuery(
				"SELECT ec.evaluationCenter FROM " + EvaluationEventEvaluationCenter.class.getSimpleName() + 
				" ec WHERE ec.id=:evaluationEventEvaluationCenterId", EvaluationCenter.class);
		
		query.setParameter("evaluationEventEvaluationCenterId", evaluationEventEvaluationCenterId);
		List<EvaluationCenter> eventCenterList = query.getResultList();
		EvaluationCenter evaluationCenter = null; 
		if(eventCenterList != null && eventCenterList.size() > 0) {
			evaluationCenter = eventCenterList.get(0);
		}
			
		return evaluationCenter;
	}
	
	
	
	public List<EvaluationEventEvaluationCenter> findByEvaluationEvent(Long evaluationEventId) {
		TypedQuery<EvaluationEventEvaluationCenter> query = this.em.createQuery(
				"SELECT ec FROM " + EvaluationEventEvaluationCenter.class.getSimpleName() + 
				" ec WHERE ec.evaluationEvent.id=:evaluationEventId" +
				" order by ec.evaluationCenter.name ASC", EvaluationEventEvaluationCenter.class);
		
		query.setParameter("evaluationEventId", evaluationEventId);
		List<EvaluationEventEvaluationCenter> eventCenters = query.getResultList();
		
		return eventCenters;
	}
	
	public List<EvaluationEventEvaluationCenterInfo> findInfoByEvaluationEvent(Long evaluationEventId) {
		TypedQuery<Object[]> query = this.em.createQuery(
				"SELECT ec.id, ec.evaluationEvent.id, ec.evaluationCenter.id, ec.evaluationEvent.name," + 
				" ec.evaluationCenter.name, ec.evaluationCenter.code FROM " + EvaluationEventEvaluationCenter.class.getSimpleName() + 
				" ec WHERE ec.evaluationEvent.id=:evaluationEventId", Object[].class);
		
		query.setParameter("evaluationEventId", evaluationEventId);
		List<Object[]> eventCenterDataList = query.getResultList();
		
		List<EvaluationEventEvaluationCenterInfo> eventCenterInfos = new ArrayList<EvaluationEventEvaluationCenterInfo>();
		for (Object[] eventCenterData : eventCenterDataList) {
			JSONObject eventCenterInfoJson = new JSONObject();
			eventCenterInfoJson.put("id", eventCenterData[0]);
			eventCenterInfoJson.put("evaluationEventId", eventCenterData[1]);
			eventCenterInfoJson.put("evaluationCenterId", eventCenterData[2]);
			eventCenterInfoJson.put("evaluationEventName", eventCenterData[3]);
			eventCenterInfoJson.put("evaluationCenterName", eventCenterData[4]);
			eventCenterInfoJson.put("evaluationCenterCode", eventCenterData[5]);

			eventCenterInfos.add(new EvaluationEventEvaluationCenterInfo(eventCenterInfoJson));
		}
		
		return eventCenterInfos;
	}
	
	public List<EvaluationEventEvaluationCenter> findByEvaluationEventAndCenter(Long evaluationEventId, Long centerId) {
		TypedQuery<EvaluationEventEvaluationCenter> query = this.em.createQuery(
				"SELECT eeec FROM " + EvaluationCenter.class.getSimpleName() + 
				" ec, " + EvaluationEventEvaluationCenter.class.getSimpleName() + 
				" eeec, " + EvaluationEventEvaluationCenterCenter.class.getSimpleName() + 
				" eeecc WHERE eeec.evaluationCenter.id=ec.id AND eeecc.evaluationEventEvaluationCenter.id=eeec.id AND" + 
				" eeec.evaluationEvent.id=:evaluationEventId AND eeecc.center.id=:centerId", EvaluationEventEvaluationCenter.class);
		
		query.setParameter("evaluationEventId", evaluationEventId);
		query.setParameter("centerId", centerId);
		List<EvaluationEventEvaluationCenter> eventCenters = query.getResultList();

		return eventCenters;
	}
	
	public List<EvaluationEventEvaluationCenterInfo> findInfoByEvaluationEventAndCenter(Long evaluationEventId, Long centerId) {
		TypedQuery<Object[]> query = this.em.createQuery(
				"SELECT  eeec.id, eeec.evaluationEvent.id, eeec.evaluationCenter.id, eeec.evaluationEvent.name," + 
				" eeec.evaluationCenter.name, eeec.evaluationCenter.code FROM " + EvaluationCenter.class.getSimpleName() + 
				" ec, " + EvaluationEventEvaluationCenter.class.getSimpleName() + 
				" eeec, " + EvaluationEventEvaluationCenterCenter.class.getSimpleName() + 
				" eeecc WHERE eeec.evaluationCenter.id=ec.id AND eeecc.evaluationEventEvaluationCenter.id=eeec.id AND" + 
				" eeec.evaluationEvent.id=:evaluationEventId AND eeecc.center.id=:centerId", Object[].class);
		
		query.setParameter("evaluationEventId", evaluationEventId);
		query.setParameter("centerId", centerId);
		List<Object[]> eventCenterDataList = query.getResultList();
		
		List<EvaluationEventEvaluationCenterInfo> eventCenterInfos = new ArrayList<EvaluationEventEvaluationCenterInfo>();
		for (Object[] eventCenterData : eventCenterDataList) {
			JSONObject eventCenterInfoJson = new JSONObject();
			eventCenterInfoJson.put("id", eventCenterData[0]);
			eventCenterInfoJson.put("evaluationEventId", eventCenterData[1]);
			eventCenterInfoJson.put("evaluationCenterId", eventCenterData[2]);
			eventCenterInfoJson.put("evaluationEventName", eventCenterData[3]);
			eventCenterInfoJson.put("evaluationCenterName", eventCenterData[4]);
			eventCenterInfoJson.put("evaluationCenterCode", eventCenterData[5]);

			eventCenterInfos.add(new EvaluationEventEvaluationCenterInfo(eventCenterInfoJson));
		}
		
		return eventCenterInfos;
	}
	
	public List<Center> findCentersByEvaluationEventId(Long eeId) {
		TypedQuery<Center> query = this.em.createQuery("select eeec.center from " + EvaluationEventEvaluationCenter.class.getSimpleName() + 
				" eeec where eeec.evaluationEvent.id = " + eeId, Center.class);
		
		List<Center> centers = query.getResultList();

		return centers;
	}

	@Override
	public EvaluationEventEvaluationCenter findByEvaluationEventAndEvaluationCenter(Long evaluationEventId, Long evaluationCenterId) {
		EvaluationEventEvaluationCenter evaluationEventEvaluationCenter = null;
		
		TypedQuery<EvaluationEventEvaluationCenter> query = this.em.createQuery(
				"SELECT ec FROM " + EvaluationEventEvaluationCenter.class.getSimpleName() + 
				" ec WHERE ec.evaluationEvent.id=:evaluationEventId" +
				" AND ec.evaluationCenter.id=:evaluationCenterId", EvaluationEventEvaluationCenter.class);
		
		query.setParameter("evaluationEventId", evaluationEventId);
		query.setParameter("evaluationCenterId", evaluationCenterId);
		List<EvaluationEventEvaluationCenter> eventCenters = query.getResultList();
		
		if (eventCenters.size() > 0) {
			evaluationEventEvaluationCenter = eventCenters.get(0);
		}
		
		return evaluationEventEvaluationCenter;
	}
}
