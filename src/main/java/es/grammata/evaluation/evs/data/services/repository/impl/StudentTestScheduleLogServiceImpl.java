package es.grammata.evaluation.evs.data.services.repository.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import es.grammata.evaluation.evs.data.model.repository.Center;
import es.grammata.evaluation.evs.data.model.repository.ClassroomTimeBlock;
import es.grammata.evaluation.evs.data.model.repository.EvaluationAssignment;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEventEvaluationCenter;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEventEvaluationCenterCenter;
import es.grammata.evaluation.evs.data.model.repository.MatterTestStudent;
import es.grammata.evaluation.evs.data.model.repository.StudentTestScheduleLog;
import es.grammata.evaluation.evs.data.services.base.impl.BaseServiceImpl;
import es.grammata.evaluation.evs.data.services.repository.EvaluationAssignmentMatterService;
import es.grammata.evaluation.evs.data.services.repository.MatterTestStudentService;
import es.grammata.evaluation.evs.data.services.repository.StudentTestScheduleLogService;
import es.grammata.evaluation.evs.mvc.controller.util.GenericInfo;
import es.grammata.evaluation.evs.mvc.controller.util.MatterTestStudentInfo;
import es.grammata.evaluation.evs.util.DateUtil;

@Repository
@Transactional(readOnly = true)
public class StudentTestScheduleLogServiceImpl extends BaseServiceImpl<StudentTestScheduleLog> implements StudentTestScheduleLogService {
	
	@Autowired
	MatterTestStudentService matterTestStudentService;
	
	@Autowired
	EvaluationAssignmentMatterService evaluationAssignmentMatterService;

	public void markAllAsRead() {
		Query query = em.createQuery("UPDATE " + StudentTestScheduleLog.class.getSimpleName() +" stsl SET stsl.read=TRUE WHERE stsl.read=FALSE");
        query.executeUpdate();
	}
	
	public void deleteByEvaluationEvent(Long evaluationEventId) {

		String query = "delete from student_test_schedule_log stsl where stsl.id in " +
		" (select stsl2.id from student_test_schedule_log stsl2 " +
		" INNER JOIN evaluation_assignment ea ON ea.id=stsl2.evaluation_assignment_id where ea.evaluationevent_id=" + evaluationEventId + ")";
		
		em.createNativeQuery(query).executeUpdate();	
		em.flush();
	}
	
	
	public void markAsReadByEvaluationEvent(Long evaluationEventId) {
		Query query = em.createQuery("UPDATE " + StudentTestScheduleLog.class.getSimpleName() +" stsl SET stsl.read=TRUE WHERE stsl.read=FALSE" +
				" AND stsl.evaluationAssignment.id IN (select ea.id from " + EvaluationAssignment.class.getSimpleName() + 
				" ea WHERE ea.evaluationEvent.id=:evaluationEventId)");
		query.setParameter("evaluationEventId", evaluationEventId);
		
		query.executeUpdate();
	}

	public Long getCountByEvaluationEvent(Long evaluationEventId) {
		TypedQuery<Long> query = this.em.createQuery(
				"SELECT COUNT(*) FROM " + StudentTestScheduleLog.class.getSimpleName() + 
				" stsl WHERE stsl.evaluationAssignment.evaluationEvent.id=:evaluationEventId AND stsl.read=FALSE", Long.class);
		
		query.setParameter("evaluationEventId", evaluationEventId);
		List<Long> dataList = query.getResultList();
		
		return dataList.get(0);
	}

	
	public List<GenericInfo> getStudentsInfoByEvaluationEvent(Long evaluationEventId) {
		TypedQuery<EvaluationAssignment> query = this.em.createQuery(
				"SELECT DISTINCT stsl.evaluationAssignment FROM " + StudentTestScheduleLog.class.getSimpleName() + 
				" stsl WHERE stsl.evaluationAssignment.evaluationEvent.id=:evaluationEventId AND stsl.read=FALSE", EvaluationAssignment.class);
		
		query.setParameter("evaluationEventId", evaluationEventId);
		List<EvaluationAssignment> students = query.getResultList();
		
		List<GenericInfo> studentsInfo = new ArrayList<GenericInfo>();
		for (EvaluationAssignment student : students) {
			JSONObject studentInfoJson = new JSONObject();
			studentInfoJson.put("evaluationAssignmentId", student.getId());
			studentInfoJson.put("fullName", student.getUser().getFullName());
			studentInfoJson.put("identification", student.getUser().getIdentification());
			studentInfoJson.put("studentType", student.getStudentType() != null ? student.getStudentType().getValue() : "Sin especificar");
			
			studentsInfo.add(new GenericInfo(studentInfoJson));
		}

		return studentsInfo;
	}

	public List<GenericInfo> getTestsLogInfoByEvaluationAssignment(Long evaluationAssignmentId) {
		TypedQuery<Long> query = this.em.createQuery(
				"SELECT DISTINCT stsl.matterTestStudent.id FROM " + StudentTestScheduleLog.class.getSimpleName() + 
				" stsl WHERE stsl.evaluationAssignment.id=:evaluationAssignmentId AND stsl.read=FALSE", Long.class);
		
		query.setParameter("evaluationAssignmentId", evaluationAssignmentId);
		List<Long> matterTestStudentIds = query.getResultList();
		
		return getTestsLogInfoByEvaluationAssignmentAndMatterTestStudentIds(evaluationAssignmentId, matterTestStudentIds);
	}
	
	
	public List<GenericInfo> getTestsLogInfoByEvaluationAssignmentAndMatterTestStudentIds(Long evaluationAssignmentId, List<Long> matterTestStudentIds) {
		List<GenericInfo> testsLogInfo = new ArrayList<GenericInfo>();
		for (Long matterTestStudentId : matterTestStudentIds) {
			MatterTestStudentInfo matterTestStudentInfo = matterTestStudentService.findInfoById(matterTestStudentId);
			
			TypedQuery<Object[]> query = this.em.createQuery(
					"SELECT stsl.id, stsl.message, stsl.classroomTimeBlock.evaluationEventClassroom.classroom.name," +
					" stsl.classroomTimeBlock.evaluationEventClassroom.evaluationEventEvaluationCenter.evaluationCenter.name," +
					" stsl.classroomTimeBlock.startDate, stsl.classroomTimeBlock.endDate, stsl.classroomTimeBlock.id FROM " + StudentTestScheduleLog.class.getSimpleName() + 
					" stsl WHERE stsl.evaluationAssignment.id=:evaluationAssignmentId AND stsl.matterTestStudent.id=:matterTestStudentId AND stsl.read=FALSE", Object[].class);
		
			query.setParameter("evaluationAssignmentId", evaluationAssignmentId);
			query.setParameter("matterTestStudentId", matterTestStudentInfo.getId());
			List<Object[]> studentTestScheduleLogs = query.getResultList();
                                                                        
			List<GenericInfo> testLogs = new ArrayList<GenericInfo>();
			for (Object[] studentTestScheduleLogData : studentTestScheduleLogs) {
				GenericInfo logInfo = new GenericInfo();
				logInfo.put("id", studentTestScheduleLogData[0]);
				logInfo.put("message", studentTestScheduleLogData[1]);
				logInfo.put("classroomName", studentTestScheduleLogData[2]);
				logInfo.put("evaluationCenterName", studentTestScheduleLogData[3]);
				logInfo.put("timeBlockStartDate", DateUtil.dateToUtcDate((Date) studentTestScheduleLogData[4]));
				logInfo.put("timeBlockEndDate", DateUtil.dateToUtcDate((Date) studentTestScheduleLogData[5]));
				
				TypedQuery<String> query2 = this.em.createQuery(
						"SELECT st.value FROM " + ClassroomTimeBlock.class.getSimpleName() + 
						" ctb JOIN ctb.studentTypes st WHERE ctb.id=:classroomTimeBlockId", String.class);
				query2.setParameter("classroomTimeBlockId", studentTestScheduleLogData[6]);
				List<String> timeBlockStudentTypes = query2.getResultList();
				
				String studentTypes = "";
				for (String studentType : timeBlockStudentTypes) {
					studentTypes += studentType + ", ";
				}
				studentTypes = !studentTypes.isEmpty() ? studentTypes.substring(0, studentTypes.length()-2): "Todos";
				logInfo.put("timeBlockStudentTypes", studentTypes);
				
				testLogs.add(logInfo);
			}
			
			GenericInfo testLogInfo = new GenericInfo();
			testLogInfo.put("matterTestStudentId", matterTestStudentInfo.getId());
			testLogInfo.put("matter", evaluationAssignmentMatterService.getMatterInfoById(matterTestStudentInfo.getEvaluationAssignmentMatterId()));
			testLogInfo.put("testName", matterTestStudentInfo.getTestName());
			testLogInfo.put("matterName", matterTestStudentInfo.getMatterName());
			testLogInfo.put("logs", testLogs);
			testsLogInfo.add(testLogInfo);
		}

		return testsLogInfo;
	}
}
