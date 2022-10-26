package es.grammata.evaluation.evs.data.services.repository.impl;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import es.grammata.evaluation.evs.data.model.repository.EvaluationAssignmentMatter;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEventMatter;
import es.grammata.evaluation.evs.data.model.repository.Matter;
import es.grammata.evaluation.evs.data.model.repository.MatterTestStudent;
import es.grammata.evaluation.evs.data.services.base.impl.BaseServiceImpl;
import es.grammata.evaluation.evs.data.services.repository.EvaluationAssignmentMatterService;
import es.grammata.evaluation.evs.mvc.controller.util.GenericInfo;


@Repository
@Transactional(readOnly = true)
public class EvaluationAssignmentMatterServiceImpl extends BaseServiceImpl<EvaluationAssignmentMatter> implements EvaluationAssignmentMatterService {
	public List<Matter> findMattersByStudentId(Long evaluationEventId, Long studentId) {
		
			String query = "select distinct eam.evaluationEventMatter.matter from " + EvaluationAssignmentMatter.class.getSimpleName() + 
					" eam, " + EvaluationEventMatter.class.getSimpleName() + " em " +
					" where eam.evaluationAssignment.evaluationEvent.id = " + evaluationEventId +
					" and eam.evaluationAssignment.user.id = " + studentId + 
					" and em.evaluationEvent.id = eam.evaluationAssignment.evaluationEvent.id";
		
			TypedQuery<Matter> typedQuery = this.em.createQuery(query, Matter.class);
			
			List<Matter> matters = typedQuery.getResultList();

			return matters;
	}
	
	public EvaluationAssignmentMatter find(Long evaluationEventId, Long studentId, Long evalutionEventMatterId) {
		String query = "select eam from " + EvaluationAssignmentMatter.class.getSimpleName() + 
				" eam where eam.evaluationAssignment.evaluationEvent.id = " + evaluationEventId +
				" and eam.evaluationAssignment.user.id = " + studentId + 
				" and eam.evaluationEventMatter.id = " + evalutionEventMatterId + 
				" and eam.evaluationEventMatter.evaluationEvent.id = " + evaluationEventId +
				" and eam.evaluationEventMatter.evaluationEvent.id =  eam.evaluationAssignment.evaluationEvent.id";
	
		TypedQuery<EvaluationAssignmentMatter> typedQuery = this.em.createQuery(query, EvaluationAssignmentMatter.class);
		
		List<EvaluationAssignmentMatter> matters = typedQuery.getResultList();
		
		EvaluationAssignmentMatter eam = null;
		if(matters != null && matters.size() > 0) {
			eam = matters.get(0);
		}

		return eam;
	}
	
	public List<EvaluationAssignmentMatter> findByMatter(Long evaluationEventId, Long matterId) {
		String query = "select eam from " + EvaluationAssignmentMatter.class.getSimpleName() + 
				" eam where eam.evaluationAssignment.evaluationEvent.id = " + evaluationEventId +
				" and eam.evaluationEventMatter.matter.id = " + matterId + 
				" and eam.evaluationEventMatter.evaluationEvent.id = " + evaluationEventId +
				" and eam.evaluationEventMatter.evaluationEvent.id =  eam.evaluationAssignment.evaluationEvent.id";
	
		TypedQuery<EvaluationAssignmentMatter> typedQuery = this.em.createQuery(query, EvaluationAssignmentMatter.class);
		
		List<EvaluationAssignmentMatter> matters = typedQuery.getResultList();

		return matters;
	}
	
	public List<EvaluationAssignmentMatter> findByEvent(Long evaluationEventId) {
		String query = "select eam from " + EvaluationAssignmentMatter.class.getSimpleName() + 
				" eam where eam.evaluationAssignment.evaluationEvent.id = " + evaluationEventId;
	
		TypedQuery<EvaluationAssignmentMatter> typedQuery = this.em.createQuery(query, EvaluationAssignmentMatter.class);
		
		List<EvaluationAssignmentMatter> matters = typedQuery.getResultList();

		return matters;
	}
	
	public List<EvaluationEventMatter> findEvaluationEventMattersByStudentId(Long evaluationEventId, Long studentId) {
		
		String query = "select distinct eam.evaluationEventMatter from " + EvaluationAssignmentMatter.class.getSimpleName() + 
				" eam, " + EvaluationEventMatter.class.getSimpleName() + " em " +
				" where eam.evaluationAssignment.evaluationEvent.id = " + evaluationEventId +
				" and eam.evaluationAssignment.user.id = " + studentId + 
				" and em.evaluationEvent.id = eam.evaluationAssignment.evaluationEvent.id";
	
		TypedQuery<EvaluationEventMatter> typedQuery = this.em.createQuery(query, EvaluationEventMatter.class);
		
		List<EvaluationEventMatter> matters = typedQuery.getResultList();

		return matters;
	}
	
	public EvaluationAssignmentMatter findEvaluationEventMattersByStudentIdentification(Long evaluationEventId, String identification, String matterCode,
			String periodCode, String mode) {
		
		String query = "select eam from " + EvaluationAssignmentMatter.class.getSimpleName() + 
				" eam, " + EvaluationEventMatter.class.getSimpleName() + " em " +
				" where eam.evaluationAssignment.evaluationEvent.id = " + evaluationEventId +
				" and eam.evaluationAssignment.user.identification = '" + identification + "'" +
				" and eam.evaluationEventMatter.matter.code = '" + matterCode + "'" +
				" and eam.evaluationEventMatter.academicPeriod.code = '" + periodCode + "'" +
				" and eam.evaluationEventMatter.mode.code = '" + mode + "'" +
				" and em.evaluationEvent.id = eam.evaluationAssignment.evaluationEvent.id";
	
		TypedQuery<EvaluationAssignmentMatter> typedQuery = this.em.createQuery(query, EvaluationAssignmentMatter.class);
		
		List<EvaluationAssignmentMatter> matters = typedQuery.getResultList();
		
		EvaluationAssignmentMatter eam = null;
		if(matters != null && matters.size() > 0) {
			eam = matters.get(0);
		}


		return eam;
	}
	
	

	public List<EvaluationAssignmentMatter> findWithoutTest(Long evaluationEventId) {
		TypedQuery<EvaluationAssignmentMatter> query = this.em.createQuery(
				"SELECT eam FROM " + EvaluationAssignmentMatter.class.getSimpleName() +  
				" eam WHERE eam.evaluationAssignment.evaluationEvent.id=:evaluationEventId AND eam.id NOT IN (SELECT DISTINCT mts.evaluationAssignmentMatter.id FROM " + MatterTestStudent.class.getSimpleName() +
				" mts WHERE mts.evaluationAssignmentMatter.evaluationEventMatter.evaluationEvent.id=:evaluationEventId)", EvaluationAssignmentMatter.class);
		
		query.setParameter("evaluationEventId", evaluationEventId);
		List<EvaluationAssignmentMatter> evaluationAssignmentMatters = query.getResultList();

		return evaluationAssignmentMatters;
	}
	
	public GenericInfo getMatterInfoById(Long id) {
		TypedQuery<Object[]> query = this.em.createQuery(
				"SELECT eam.evaluationEventMatter.matter.name, eam.evaluationEventMatter.matter.code, eam.evaluationEventMatter.mode.name," +
				" eam.evaluationEventMatter.academicPeriod.name, eam.evaluationEventMatter.matter.academicLevel.name," + 
				" eam.center.name FROM " + EvaluationAssignmentMatter.class.getSimpleName() + 
				" eam WHERE eam.id=:id", Object[].class);
	
		query.setParameter("id", id);
		List<Object[]> matterDataList = query.getResultList();
		
		GenericInfo matterInfo = new GenericInfo();
		for (Object[] matterData : matterDataList) {
			matterInfo.put("name", matterData[0]);
			matterInfo.put("code", matterData[1]);
			matterInfo.put("mode", matterData[2]);
			matterInfo.put("academicPeriod", matterData[3]);
			matterInfo.put("academicLevel", matterData[4]);
			matterInfo.put("center", matterData[5]);
		}
		
		return matterInfo;
	}

	@Override
	public List<EvaluationAssignmentMatter> findByEvaluationAssignment(Long evaluationAssignmentId) {
		TypedQuery<EvaluationAssignmentMatter> query = this.em.createQuery(
				"SELECT eam FROM " + EvaluationAssignmentMatter.class.getSimpleName() +  
				" eam WHERE eam.evaluationAssignment.id=:evaluationAssignmentId", EvaluationAssignmentMatter.class);
		
		query.setParameter("evaluationAssignmentId", evaluationAssignmentId);
		List<EvaluationAssignmentMatter> evaluationAssignmentMatters = query.getResultList();

		return evaluationAssignmentMatters;
	}
	
	@Override
	public EvaluationAssignmentMatter findByEvaluationAssignmentAndEvalutionEventMatter(Long evaluationAssignmentId, Long evalutionEventMatterId) {
		TypedQuery<EvaluationAssignmentMatter> query = this.em.createQuery(
				"SELECT eam FROM " + EvaluationAssignmentMatter.class.getSimpleName() +  
				" eam WHERE eam.evaluationAssignment.id=:evaluationAssignmentId AND eam.evaluationEventMatter.id=:evalutionEventMatterId", EvaluationAssignmentMatter.class);
		
		query.setParameter("evaluationAssignmentId", evaluationAssignmentId);
		query.setParameter("evalutionEventMatterId", evalutionEventMatterId);
		List<EvaluationAssignmentMatter> evaluationAssignmentMatters = query.getResultList();

		EvaluationAssignmentMatter eam = null;
		if(evaluationAssignmentMatters != null && evaluationAssignmentMatters.size() > 0) {
			eam = evaluationAssignmentMatters.get(0);
		}

		return eam;
	}
}
