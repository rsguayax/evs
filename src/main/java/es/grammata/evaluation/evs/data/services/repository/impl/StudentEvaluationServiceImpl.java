package es.grammata.evaluation.evs.data.services.repository.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.json.JSONObject;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import es.grammata.evaluation.evs.data.model.repository.MatterTestStudent;
import es.grammata.evaluation.evs.data.model.repository.StudentEvaluation;
import es.grammata.evaluation.evs.data.services.base.impl.BaseServiceImpl;
import es.grammata.evaluation.evs.data.services.repository.StudentEvaluationService;
import es.grammata.evaluation.evs.mvc.controller.util.GenericInfo;

@Repository
@Transactional(readOnly = true)
public class StudentEvaluationServiceImpl extends BaseServiceImpl<StudentEvaluation> implements StudentEvaluationService {

	@Override
	public List<StudentEvaluation> findByEvaluationAssignment(Long evaluationAssignmentId) {
		TypedQuery<StudentEvaluation> query = this.em.createQuery(
				"SELECT se FROM " + StudentEvaluation.class.getSimpleName() + 
				" se WHERE se.evaluationAssignment.id=:evaluationAssignmentId", StudentEvaluation.class);
		
		query.setParameter("evaluationAssignmentId", evaluationAssignmentId);
		List<StudentEvaluation> studentEvaluations = query.getResultList();
		
		return studentEvaluations;
	}
	
	@Override
	public List<StudentEvaluation> findByEvaluationEventAndUser(Long evaluationEventId, String identification) {
		TypedQuery<StudentEvaluation> query = this.em.createQuery(
				"SELECT se FROM " + StudentEvaluation.class.getSimpleName() + 
				" se WHERE se.evaluationAssignment.user.identification=:identification AND " +
				" se.evaluationAssignment.evaluationEvent.id=:evaluationEventId", StudentEvaluation.class);
		
		query.setParameter("identification", identification);
		query.setParameter("evaluationEventId", evaluationEventId);
		List<StudentEvaluation> studentEvaluations = query.getResultList();
		
		return studentEvaluations;
	}
	
	@Override
	public List<StudentEvaluation> findByEvaluationEvent(Long evaluationEventId) {
		TypedQuery<StudentEvaluation> query = this.em.createQuery(
				"SELECT se FROM " + StudentEvaluation.class.getSimpleName() + 
				" se WHERE se.evaluationAssignment.evaluationEvent.id=:evaluationEventId", StudentEvaluation.class);
		
		query.setParameter("evaluationEventId", evaluationEventId);
		List<StudentEvaluation> studentEvaluations = query.getResultList();
		
		return studentEvaluations;
	}
	
	@Override
	public List<Long> findIdsByEvaluationEvent(Long evaluationEventId) {
		TypedQuery<Long> query = this.em.createQuery(
				"SELECT se.id FROM " + StudentEvaluation.class.getSimpleName() + 
				" se WHERE se.evaluationAssignment.evaluationEvent.id=:evaluationEventId", Long.class);
		
		query.setParameter("evaluationEventId", evaluationEventId);
		List<Long> studentEvaluationsIds = query.getResultList();
		
		return studentEvaluationsIds;
	}
	
	@Override
	public StudentEvaluation findByEvaluationAssignmentAndClassroomTimeBlock(Long evaluationAssignmentId, Long classroomTimeBlockId) {
		TypedQuery<StudentEvaluation> query = this.em.createQuery(
				"SELECT se FROM " + StudentEvaluation.class.getSimpleName() + 
				" se WHERE se.evaluationAssignment.id=:evaluationAssignmentId AND se.classroomTimeBlock.id=:classroomTimeBlockId", StudentEvaluation.class);
		
		query.setParameter("evaluationAssignmentId", evaluationAssignmentId);
		query.setParameter("classroomTimeBlockId", classroomTimeBlockId);
		
		List<StudentEvaluation> studentEvaluations = query.getResultList();
		if (studentEvaluations.size() > 0) {
			return studentEvaluations.get(0);
		}
		
		return null;
	}
	
	
	@Override
	public List<StudentEvaluation> findByEvaluationAssignmentAndClassroomTimeBlockList(Long evaluationAssignmentId, Long classroomTimeBlockId) {
		TypedQuery<StudentEvaluation> query = this.em.createQuery(
				"SELECT se FROM " + StudentEvaluation.class.getSimpleName() + 
				" se WHERE se.evaluationAssignment.id=:evaluationAssignmentId AND se.classroomTimeBlock.id=:classroomTimeBlockId", StudentEvaluation.class);
		
		query.setParameter("evaluationAssignmentId", evaluationAssignmentId);
		query.setParameter("classroomTimeBlockId", classroomTimeBlockId);
		
		List<StudentEvaluation> studentEvaluations = query.getResultList();
		
		return studentEvaluations;
	}
	

	@Override
	public List<GenericInfo> findInfoByClassroomTimeBlock(Long classroomTimeBlockId) {
		TypedQuery<Object[]> query = this.em.createQuery(
				"SELECT se.id, se.evaluationAssignment.user.username, se.evaluationAssignment.user.firstName, se.evaluationAssignment.user.lastName FROM " + StudentEvaluation.class.getSimpleName() + 
				" se WHERE se.classroomTimeBlock.id=:classroomTimeBlockId", Object[].class);
		
		query.setParameter("classroomTimeBlockId", classroomTimeBlockId);
		List<Object[]> studentEvaluationDataList = query.getResultList();
		
		List<GenericInfo> studentEvaluationInfos = new ArrayList<GenericInfo>();
		for (Object[] studentEvaluationData : studentEvaluationDataList) {
			JSONObject studentEvaluationInfoJson = new JSONObject();
			studentEvaluationInfoJson.put("id", studentEvaluationData[0]);
			studentEvaluationInfoJson.put("studentUsername", studentEvaluationData[1]);
			studentEvaluationInfoJson.put("studentFirstName", studentEvaluationData[2]);
			studentEvaluationInfoJson.put("studentLastName", studentEvaluationData[3]);
			
			String fullName = studentEvaluationInfoJson.getString("studentFirstName");
			if (!studentEvaluationInfoJson.getString("studentLastName").isEmpty()) {
				fullName += " " + studentEvaluationInfoJson.getString("studentLastName");
			}
			studentEvaluationInfoJson.put("studentFullName", fullName);
			
			studentEvaluationInfos.add(new GenericInfo(studentEvaluationInfoJson));
		}
		
		return studentEvaluationInfos;
	}
	
	@Override
	public void deleteById(Long studentEvaluationId) {
		String query = "DELETE FROM student_evaluation WHERE id = " + studentEvaluationId;
		em.createNativeQuery(query).executeUpdate();
		em.flush();
	}
}
