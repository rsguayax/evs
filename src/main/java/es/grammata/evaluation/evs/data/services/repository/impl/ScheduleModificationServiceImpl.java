package es.grammata.evaluation.evs.data.services.repository.impl;

import java.util.List;

import javax.persistence.TypedQuery;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import es.grammata.evaluation.evs.data.model.repository.ScheduleModification;
import es.grammata.evaluation.evs.data.model.repository.ScheduleModificationInfo;
import es.grammata.evaluation.evs.data.model.repository.User;
import es.grammata.evaluation.evs.data.services.base.impl.BaseServiceImpl;
import es.grammata.evaluation.evs.data.services.repository.MatterTestStudentService;
import es.grammata.evaluation.evs.data.services.repository.ScheduleModificationService;
import es.grammata.evaluation.evs.data.services.repository.UserService;
import es.grammata.evaluation.evs.mvc.controller.util.GenericInfo;
import es.grammata.evaluation.evs.util.DateUtil;

@Repository
@Transactional(readOnly = true)
public class ScheduleModificationServiceImpl extends BaseServiceImpl<ScheduleModification> implements ScheduleModificationService {
	
	@Autowired
	private MatterTestStudentService matterTestStudentService;
	
	@Autowired
	private UserService userService;

	public List<ScheduleModification> findByEvaluationAssignment(Long evaluationAssignmentId) {
		TypedQuery<ScheduleModification> query = this.em.createQuery(
				"SELECT sm FROM " + ScheduleModification.class.getSimpleName() + 
				" sm WHERE sm.evaluationAssignment.id=:evaluationAssignmentId", ScheduleModification.class);
		
		query.setParameter("evaluationAssignmentId", evaluationAssignmentId);
		List<ScheduleModification> schedulesModifications = query.getResultList();
		
		return schedulesModifications;
	}
	
	public List<ScheduleModification> findByEvaluationAssignmentAndCreatedBy(Long evaluationAssignmentId, String username) {
		TypedQuery<ScheduleModification> query = this.em.createQuery(
				"SELECT sm FROM " + ScheduleModification.class.getSimpleName() + 
				" sm WHERE sm.evaluationAssignment.id=:evaluationAssignmentId AND sm.createBy=:username", ScheduleModification.class);
		
		query.setParameter("evaluationAssignmentId", evaluationAssignmentId);
		query.setParameter("username", username);
		List<ScheduleModification> schedulesModifications = query.getResultList();
		
		return schedulesModifications;
	}

	public ScheduleModification getLastByEvaluationAssignment(Long evaluationAssignmentId) {
		TypedQuery<ScheduleModification> query = this.em.createQuery(
				"SELECT sm FROM " + ScheduleModification.class.getSimpleName() + 
				" sm WHERE sm.evaluationAssignment.id=:evaluationAssignmentId ORDER BY sm.createdDate DESC", ScheduleModification.class);
		
		query.setParameter("evaluationAssignmentId", evaluationAssignmentId);
		List<ScheduleModification> schedulesModifications = query.getResultList();
		
		if (!schedulesModifications.isEmpty()) {
			return schedulesModifications.get(0);
		}
		
		return null;
	}
	
	public ScheduleModification getLastByEvaluationAssignmentAndCreatedBy(Long evaluationAssignmentId, String username) {
		TypedQuery<ScheduleModification> query = this.em.createQuery(
				"SELECT sm FROM " + ScheduleModification.class.getSimpleName() + 
				" sm WHERE sm.evaluationAssignment.id=:evaluationAssignmentId AND sm.createBy=:username ORDER BY sm.createdDate DESC", ScheduleModification.class);
		
		query.setParameter("evaluationAssignmentId", evaluationAssignmentId);
		query.setParameter("username", username);
		List<ScheduleModification> schedulesModifications = query.getResultList();
		
		if (!schedulesModifications.isEmpty()) {
			return schedulesModifications.get(0);
		}
		
		return null;
	}

	@Override
	public GenericInfo getLastInfoByEvaluationAssignment(Long evaluationAssignmentId) {
		ScheduleModification lastScheduleModification = getLastByEvaluationAssignment(evaluationAssignmentId);
		
		if (lastScheduleModification != null) {
			return scheduleModificationToInfo(lastScheduleModification);
		}
		
		return null;
	}

	public GenericInfo getLastInfoByEvaluationAssignmentAndCreatedBy(Long evaluationAssignmentId, String username) {
		ScheduleModification lastScheduleModification = getLastByEvaluationAssignmentAndCreatedBy(evaluationAssignmentId, username);

		if (lastScheduleModification != null) {
			return scheduleModificationToInfo(lastScheduleModification);
		}
		
		return null;
	}
	
	public GenericInfo scheduleModificationToInfo(ScheduleModification scheduleModification) {
		User createdBy = userService.findByUsername(scheduleModification.getCreateBy());
		
		GenericInfo genericInfo = new GenericInfo();
		genericInfo.put("studentName", scheduleModification.getEvaluationAssignment().getUser().getFullName());
		genericInfo.put("message", scheduleModification.getMessage());
		genericInfo.put("date", DateUtil.getUtcTime(scheduleModification.getCreatedDate()));
		genericInfo.put("createdBy", createdBy != null ? createdBy.getFullName() : scheduleModification.getCreateBy());
		
		JSONArray scheduleModificationInfosJson = new JSONArray();
		for (ScheduleModificationInfo scheduleModificationInfo : scheduleModification.getScheduleModificationInfos()) {
			JSONObject scheduleModificationInfoJson = new JSONObject();
			scheduleModificationInfoJson.put("matterName", matterTestStudentService.findInfoById(scheduleModificationInfo.getMatterTestStudent().getId()).getMatterName());
			
			JSONObject oldClassroomTimeBlockJson = new JSONObject();
			oldClassroomTimeBlockJson.put("evaluationCenterName", scheduleModificationInfo.getOldClassroomTimeBlock().getEvaluationCenter().getName());
			oldClassroomTimeBlockJson.put("classroomName", scheduleModificationInfo.getOldClassroomTimeBlock().getEvaluationEventClassroom().getClassroom().getName());
			oldClassroomTimeBlockJson.put("startDate", DateUtil.getUtcTime(scheduleModificationInfo.getOldClassroomTimeBlock().getStartDate()));
			oldClassroomTimeBlockJson.put("endDate", DateUtil.getUtcTime(scheduleModificationInfo.getOldClassroomTimeBlock().getEndDate()));
			scheduleModificationInfoJson.put("oldClassroomTimeBlock", oldClassroomTimeBlockJson);
			
			JSONObject newClassroomTimeBlockJson = new JSONObject();
			newClassroomTimeBlockJson.put("evaluationCenterName", scheduleModificationInfo.getNewClassroomTimeBlock().getEvaluationCenter().getName());
			newClassroomTimeBlockJson.put("classroomName", scheduleModificationInfo.getNewClassroomTimeBlock().getEvaluationEventClassroom().getClassroom().getName());
			newClassroomTimeBlockJson.put("startDate", DateUtil.getUtcTime(scheduleModificationInfo.getNewClassroomTimeBlock().getStartDate()));
			newClassroomTimeBlockJson.put("endDate", DateUtil.getUtcTime(scheduleModificationInfo.getNewClassroomTimeBlock().getEndDate()));
			scheduleModificationInfoJson.put("newClassroomTimeBlock", newClassroomTimeBlockJson);
			
			scheduleModificationInfosJson.put(scheduleModificationInfoJson);
		}
		
		genericInfo.put("scheduleModificationInfos", scheduleModificationInfosJson);
		
		return genericInfo;
	}
}
