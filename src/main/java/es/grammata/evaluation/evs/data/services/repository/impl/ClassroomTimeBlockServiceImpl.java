package es.grammata.evaluation.evs.data.services.repository.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.TypedQuery;

import org.json.JSONObject;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import es.grammata.evaluation.evs.data.model.repository.Cap;
import es.grammata.evaluation.evs.data.model.repository.ClassroomTimeBlock;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEventEvaluationCenterCenter;
import es.grammata.evaluation.evs.data.model.repository.StudentEvaluation;
import es.grammata.evaluation.evs.data.services.base.impl.BaseServiceImpl;
import es.grammata.evaluation.evs.data.services.repository.ClassroomTimeBlockService;
import es.grammata.evaluation.evs.mvc.controller.util.ClassroomTimeBlockInfo;
import es.grammata.evaluation.evs.mvc.controller.util.ClassroomTimeBlockInfo2;

@Repository
@Transactional(readOnly = true)
public class ClassroomTimeBlockServiceImpl extends BaseServiceImpl<ClassroomTimeBlock> implements ClassroomTimeBlockService {

	@Override
	public List<ClassroomTimeBlock> findByEventCenter(Long eventCenterId) {
		TypedQuery<ClassroomTimeBlock> query = this.em.createQuery(
				"SELECT ctb FROM " + ClassroomTimeBlock.class.getSimpleName() + 
				" ctb WHERE ctb.evaluationEventClassroom.evaluationEventEvaluationCenter.id=:eventCenterId ORDER BY ctb.evaluationEventClassroom.id ASC, ctb.startDate ASC", ClassroomTimeBlock.class);
		
		query.setParameter("eventCenterId", eventCenterId);
		List<ClassroomTimeBlock> classroomTimeBlocks = query.getResultList();
		
		return classroomTimeBlocks;
	}
	
	@Override
	public List<ClassroomTimeBlockInfo> findInfoByEventCenter(Long eventCenterId) {
		TypedQuery<Object[]> query = this.em.createQuery(
				"SELECT ctb.id, ctb.startDate, ctb.endDate, ctb.seats, ctb.availableState.value," + 
				" ctb.evaluationEventClassroom.evaluationEventEvaluationCenter.id, ctb.evaluationEventClassroom.evaluationEventEvaluationCenter.evaluationCenter.name," +
				" ctb.evaluationEventClassroom.classroom.id, ctb.evaluationEventClassroom.classroom.name FROM " + ClassroomTimeBlock.class.getSimpleName() + 
				" ctb WHERE ctb.evaluationEventClassroom.evaluationEventEvaluationCenter.id=:eventCenterId ORDER BY ctb.startDate ASC", Object[].class);
		
		query.setParameter("eventCenterId", eventCenterId);
		List<Object[]> classroomTimeBlockDataList = query.getResultList();
		
		List<ClassroomTimeBlockInfo> classroomTimeBlockInfos = new ArrayList<ClassroomTimeBlockInfo>();
		for (Object[] classroomTimeBlockData : classroomTimeBlockDataList) {
			JSONObject classroomTimeBlockInfoJson = new JSONObject();
			classroomTimeBlockInfoJson.put("id", classroomTimeBlockData[0]);
			classroomTimeBlockInfoJson.put("startDate", classroomTimeBlockData[1]);
			classroomTimeBlockInfoJson.put("endDate", classroomTimeBlockData[2]);
			classroomTimeBlockInfoJson.put("seats", classroomTimeBlockData[3]);
			classroomTimeBlockInfoJson.put("availableState", classroomTimeBlockData[4]);
			classroomTimeBlockInfoJson.put("evaluationEventEvaluationCenterId", classroomTimeBlockData[5]);
			classroomTimeBlockInfoJson.put("evaluationCenterName", classroomTimeBlockData[6]);
			classroomTimeBlockInfoJson.put("classroomId", classroomTimeBlockData[7]);
			classroomTimeBlockInfoJson.put("classroomName", classroomTimeBlockData[8]);
			
			TypedQuery<Long> query2 = this.em.createQuery(
					"SELECT count(*) FROM " + StudentEvaluation.class.getSimpleName() + 
							" se WHERE se.classroomTimeBlock.id=:classroomTimeBlockId", Long.class);
			query2.setParameter("classroomTimeBlockId", classroomTimeBlockInfoJson.getLong("id"));
			
			Long occupiedSeats = query2.getSingleResult();
			Long availableSeats = classroomTimeBlockInfoJson.getInt("seats") - occupiedSeats;
			
			classroomTimeBlockInfoJson.put("occupiedSeats", occupiedSeats);
			classroomTimeBlockInfoJson.put("availableSeats", availableSeats);

			classroomTimeBlockInfos.add(new ClassroomTimeBlockInfo(classroomTimeBlockInfoJson));
		}
		
		return classroomTimeBlockInfos;
	}

	@Override
	public List<ClassroomTimeBlock> findByEvaluationEventAndCenter(Long evaluationEventId, Long centerId) {
		TypedQuery<ClassroomTimeBlock> query = this.em.createQuery(
				"SELECT ctb FROM " + ClassroomTimeBlock.class.getSimpleName() + 
				" ctb, " + EvaluationEventEvaluationCenterCenter.class.getSimpleName() + 
				" eeecc WHERE eeecc.evaluationEventEvaluationCenter.id=ctb.evaluationEventClassroom.evaluationEventEvaluationCenter.id AND" + 
				" ctb.evaluationEventClassroom.evaluationEventEvaluationCenter.evaluationEvent.id=:evaluationEventId AND eeecc.center.id=:centerId ORDER BY ctb.startDate ASC", ClassroomTimeBlock.class);
		
		query.setParameter("evaluationEventId", evaluationEventId);
		query.setParameter("centerId", centerId);
		List<ClassroomTimeBlock> classroomTimeBlocks = query.getResultList();

		return classroomTimeBlocks;
	}
	
	@Override
	public ClassroomTimeBlock findByClassroomTimeBlock(Long evaluationEventId, String centerCode, String classroomCode, Date startDate, Date endDate) {
		TypedQuery<ClassroomTimeBlock> query = this.em.createQuery(
				"SELECT ctb FROM " + ClassroomTimeBlock.class.getSimpleName() + 
				" ctb " +
				" WHERE ctb.evaluationEventClassroom.evaluationEventEvaluationCenter.evaluationCenter.code=:centerCode" + 
				" AND ctb.evaluationEventClassroom.evaluationEventEvaluationCenter.evaluationEvent.id=:evaluationEventId " +
				" AND ctb.evaluationEventClassroom.classroom.name=:classroomCode " +
				" AND ctb.timeBlock.startDate=:startDate AND ctb.timeBlock.endDate=:endDate" +
				" ORDER BY ctb.startDate ASC", ClassroomTimeBlock.class);
		
		query.setParameter("evaluationEventId", evaluationEventId);
		query.setParameter("centerCode", centerCode);
		query.setParameter("classroomCode", classroomCode);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		List<ClassroomTimeBlock> classroomTimeBlocks = query.getResultList();

		ClassroomTimeBlock ctb = null;
		if(classroomTimeBlocks != null && classroomTimeBlocks.size() > 0) {
			ctb = classroomTimeBlocks.get(0);
		}


		return ctb;
	}
	
	
	public List<ClassroomTimeBlock> findByTimeBlock(Long timeBlockId, Long evaluatonEventId) {
		TypedQuery<ClassroomTimeBlock> query = this.em.createQuery("select ctm from " + ClassroomTimeBlock.class.getSimpleName() +
				" ctm where ctm.timeBlock.id = :timeBlockId " +
				" and ctm.evaluationEventClassroom.evaluationEventEvaluationCenter.evaluationEvent.id = :evaluationEventId ", ClassroomTimeBlock.class);
		
		query.setParameter("evaluationEventId", evaluatonEventId);
		query.setParameter("timeBlockId", timeBlockId);

		List<ClassroomTimeBlock> classroomTimeBlocks = query.getResultList();

		return classroomTimeBlocks;
	}
	
	public List<ClassroomTimeBlock> findByEvaluationEvent(Long evaluationEventId) {
		TypedQuery<ClassroomTimeBlock> query = this.em.createQuery("SELECT cr FROM " + ClassroomTimeBlock.class.getSimpleName() + " cr where " +
				" cr.evaluationEventClassroom.evaluationEventEvaluationCenter.evaluationEvent.id = " + evaluationEventId
				, ClassroomTimeBlock.class);

		List<ClassroomTimeBlock> classroomTimeBlocks = query.getResultList();


		return classroomTimeBlocks;
	}
	
	public List<ClassroomTimeBlock> findByDates(Date startDate, Date endDate, Long evaluationEventId) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String startDateStr = sdf.format(startDate);
		String endDateStr = sdf.format(endDate);
		
		
		TypedQuery<ClassroomTimeBlock> query = this.em.createQuery("SELECT cr FROM " + ClassroomTimeBlock.class.getSimpleName() + " cr where " +
				" cr.evaluationEventClassroom.evaluationEventEvaluationCenter.evaluationEvent.id = " + evaluationEventId + 
				" and cr.startDate = '" + startDateStr + "' and cr.endDate = '" + endDate + "' " +
						" order by cr.evaluationEventClassroom.evaluationEventEvaluationCenter ASC"
				, ClassroomTimeBlock.class);

		List<ClassroomTimeBlock> classroomTimeBlocks = query.getResultList();


		return classroomTimeBlocks;
	}
	
	public ClassroomTimeBlockInfo2 findInfoById(Long id) {
		String strQuery = "SELECT ctb.id, ctb.startDate, ctb.endDate, ctb.evaluationEventClassroom.cap.id," + 
				" ctb.evaluationEventClassroom.evaluationEventEvaluationCenter.evaluationCenter.code, ctb.evaluationEventClassroom.classroom.name" +
				" FROM " + ClassroomTimeBlock.class.getSimpleName() + " ctb" +
				" WHERE ctb.id=" + id;
		
		TypedQuery<Object[]> query = this.em.createQuery(strQuery, Object[].class);
		List<Object[]> classroomTimeBlockDataList = query.getResultList();
		
		if (classroomTimeBlockDataList.size() > 0) {
			Object[] ctbData = classroomTimeBlockDataList.get(0);
			ClassroomTimeBlockInfo2 classroomTimeBlockInfo2 = new ClassroomTimeBlockInfo2();
			classroomTimeBlockInfo2.setId((Long) ctbData[0]);
			classroomTimeBlockInfo2.setStartDate(ctbData[1]);
			classroomTimeBlockInfo2.setEndDate(ctbData[2]);
			classroomTimeBlockInfo2.setCapId((Long) ctbData[3]);
			classroomTimeBlockInfo2.setEvaluationCenterCode((String) ctbData[4]);
			classroomTimeBlockInfo2.setClassroomName((String) ctbData[5]);
			
			if (classroomTimeBlockInfo2.getCapId() != null) {
				TypedQuery<Object> query2 = this.em.createQuery("SELECT c.ssid FROM " + Cap.class.getSimpleName() + " c WHERE c.id=:capId", Object.class);
				query2.setParameter("capId", classroomTimeBlockInfo2.getCapId());
				List<Object> dataList = query2.getResultList();
				classroomTimeBlockInfo2.setCapSsid((String) dataList.get(0));
			}
			
			return classroomTimeBlockInfo2;
		}
		
		return null;
	}
}
