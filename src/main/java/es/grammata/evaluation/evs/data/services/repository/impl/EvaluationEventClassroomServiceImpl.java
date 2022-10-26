package es.grammata.evaluation.evs.data.services.repository.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;

import org.json.JSONObject;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import es.grammata.evaluation.evs.data.model.repository.Classroom;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEventClassroom;
import es.grammata.evaluation.evs.data.services.base.impl.BaseServiceImpl;
import es.grammata.evaluation.evs.data.services.repository.EvaluationEventClassroomService;
import es.grammata.evaluation.evs.mvc.controller.util.GenericInfo;

@Repository
@Transactional(readOnly = true)
public class EvaluationEventClassroomServiceImpl extends BaseServiceImpl<EvaluationEventClassroom> implements EvaluationEventClassroomService {

	@Override
	public List<GenericInfo> findInfoByEventCenter(Long eventCenterId) {
		TypedQuery<Object[]> query = this.em.createQuery(
				"SELECT eec.id, eec.classroom.id, eec.cap.id, eec.schedule.id, eec.seats, eec.classroom.name," + 
				" eec.evaluationEventEvaluationCenter.evaluationCenter.name FROM " + EvaluationEventClassroom.class.getSimpleName() + 
				" eec WHERE eec.evaluationEventEvaluationCenter.id=:eventCenterId", Object[].class);
		
		query.setParameter("eventCenterId", eventCenterId);
		List<Object[]> eventClassroomDataList = query.getResultList();
		
		List<GenericInfo> eventClassroomInfos = new ArrayList<GenericInfo>();
		for (Object[] eventClassroomData : eventClassroomDataList) {
			JSONObject eventClassroomInfoJson = new JSONObject();
			eventClassroomInfoJson.put("id", eventClassroomData[0]);
			eventClassroomInfoJson.put("classroomId", eventClassroomData[1]);
			eventClassroomInfoJson.put("capId", eventClassroomData[2]);
//			eventClassroomInfoJson.put("capSerialNumber", eventClassroomData[3]);
			eventClassroomInfoJson.put("scheduleId", eventClassroomData[3]);
			eventClassroomInfoJson.put("seats", eventClassroomData[4]);
			eventClassroomInfoJson.put("classroomName", eventClassroomData[5]);
			eventClassroomInfoJson.put("evaluationCenterName", eventClassroomData[6]);
			
			eventClassroomInfos.add(new GenericInfo(eventClassroomInfoJson));
		}
		
		return eventClassroomInfos;
	}
	
	@Override
	public List<EvaluationEventClassroom> findByEventCenter(Long eventCenterId) {
		TypedQuery<EvaluationEventClassroom> query = this.em.createQuery(
				"SELECT eec FROM " + EvaluationEventClassroom.class.getSimpleName() + 
				" eec WHERE eec.evaluationEventEvaluationCenter.id=:eventCenterId", EvaluationEventClassroom.class);
		
		query.setParameter("eventCenterId", eventCenterId);
		List<EvaluationEventClassroom> eventClassroomDataList = query.getResultList();

		return eventClassroomDataList;
	}
	
	@Override
	public Classroom loadClassroom(Long evaluationEventClassroomId) {
		TypedQuery<Classroom> query = this.em.createQuery(
				"SELECT eec.classroom FROM " + EvaluationEventClassroom.class.getSimpleName() + 
				" eec WHERE eec.id=:evaluationEventClassroomId", Classroom.class);
		
		query.setParameter("evaluationEventClassroomId", evaluationEventClassroomId);
		List<Classroom> classrooms = query.getResultList();
		Classroom classroom = null;
		if(classrooms != null && classrooms.size() > 0) {
			classroom = classrooms.get(0);
					
		}

		return classroom;
	}
}
