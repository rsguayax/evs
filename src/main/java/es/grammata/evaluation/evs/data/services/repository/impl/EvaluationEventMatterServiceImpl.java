package es.grammata.evaluation.evs.data.services.repository.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import es.grammata.evaluation.evs.data.model.repository.EvaluationEventMatter;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEventMatterId;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEventMatterTest;
import es.grammata.evaluation.evs.data.model.repository.WeekDay;
import es.grammata.evaluation.evs.data.services.base.impl.BaseServiceImpl;
import es.grammata.evaluation.evs.data.services.repository.EvaluationAssignmentMatterService;
import es.grammata.evaluation.evs.data.services.repository.EvaluationEventMatterService;
import es.grammata.evaluation.evs.mvc.controller.util.EvaluationEventMatterInfo;

@Repository
@Transactional(readOnly = true)
public class EvaluationEventMatterServiceImpl extends BaseServiceImpl<EvaluationEventMatter> 
		implements EvaluationEventMatterService {

	
	@PersistenceContext
	protected EntityManager em;
	
	@Autowired
	public EvaluationAssignmentMatterService evaluationAssignmentMatterService;
	
	
	@Transactional
	public void delete(EvaluationEventMatter evaluationEventMatter) {
		/*if(em.contains(evaluationEventMatter)) {
			em.remove(evaluationEventMatter);
		} else {
			em.remove(em.merge(evaluationEventMatter));
		}

		em.flush();*/
		
		String query = "DELETE FROM evaluation_event_matter " +
				" WHERE id = " + evaluationEventMatter.getId();
		
		em.createNativeQuery(query).executeUpdate();	
		em.flush();
		
	}
	
	public EvaluationEventMatter findById(EvaluationEventMatterId id) {
		return em.find(EvaluationEventMatter.class, id);
	}
	
	@Transactional
	public void update(EvaluationEventMatter entity) {
		if (entity.getId() != null) {
			em.merge(entity);
			em.flush();
		} 
	}
	
	@Transactional
	public void save(EvaluationEventMatter entity) {
			em.persist(entity);
			em.flush();
	}
	
	
	public void deleteAll(Long evaluationEventId, EvaluationEventMatter evaluationEventMatter) {
		/*Query query = em.createQuery("DELETE FROM EvaluationEventMatterTest " +
				" WHERE evaluationEventMatter.evaluationEvent.id = " + evaluationEventMatter.getEvaluationEvent().getId() + 
				" and evaluationEventMatter.matter.id = " + evaluationEventMatter.getMatter().getId());
        int deletedCount = query.executeUpdate();*/
        
		String query = "DELETE FROM evaluation_event_matter_day " +
				" WHERE evaluationeventmatter_id = " + evaluationEventMatter.getId();
		
		em.createNativeQuery(query).executeUpdate();	
		em.flush();
		
		
		query = "DELETE FROM evaluation_event_matter_test " +
				" WHERE evaluationeventmatter_id = " + evaluationEventMatter.getId();
		
		em.createNativeQuery(query).executeUpdate();	
		em.flush();
		
		query = "DELETE FROM evaluation_assignment_matter " +
				" WHERE evaluationeventmatter_id = " + evaluationEventMatter.getId();
		
		em.createNativeQuery(query).executeUpdate();	
		em.flush();

		
		query = "DELETE FROM evaluation_assignment ea " +
				" WHERE NOT EXISTS (SELECT eam.id FROM evaluation_assignment_matter eam " +
					" WHERE eam.evaluationassignment_id = ea.id) and ea.evaluationevent_id = " + evaluationEventId;
		
		em.createNativeQuery(query).executeUpdate();	
		em.flush();
		
		
		query = "DELETE FROM evaluation_event_matter_evaluation_type " +
				" WHERE evaluationeventmatter_id = " + evaluationEventMatter.getId();
		
		em.createNativeQuery(query).executeUpdate();	
		em.flush();

        this.delete(evaluationEventMatter);
	}
	
	
	public EvaluationEventMatter findByUnique(Long evaluationEventId, Long matterId, Long academicPeriodId, Long modeId) {	
		TypedQuery<EvaluationEventMatter> query = this.em.createQuery("select ea from " + EvaluationEventMatter.class.getSimpleName() + 
				" ea where ea.evaluationEvent.id = " + evaluationEventId + 
				" and ea.matter.id = " + matterId + " and ea.academicPeriod.id = " + academicPeriodId + " and ea.mode.id = " + modeId, EvaluationEventMatter.class);
		
		List<EvaluationEventMatter> evaluationEventMatters = query.getResultList();
		EvaluationEventMatter evaluationEventMatter = null;
		if(evaluationEventMatters != null && evaluationEventMatters.size() > 0) {
			evaluationEventMatter = evaluationEventMatters.get(0);
		} 

		return evaluationEventMatter;
	}
	
	public List<Long> getEvaluationTypes(Long evmId) {	

		String query = "select evaluation_type_id from evaluation_event_matter_evaluation_type where evaluationeventmatter_id = " + evmId; 
		
		List<BigInteger> ids = em.createNativeQuery(query).getResultList();	
		em.flush();
		
		List<Long> idsLong = new ArrayList<Long>();
		for(BigInteger id : ids) {
			idsLong.add(id.longValue());
		}

		return idsLong;
	}
	
	public List<EvaluationEventMatter> findByParams(Long evaluationEventId, Long academicPeriodId, Long modeId) {
		TypedQuery<EvaluationEventMatter> query = this.em.createQuery("select ea from " + EvaluationEventMatter.class.getSimpleName() + 
				" ea where ea.evaluationEvent.id = " + evaluationEventId + 
				" and ea.academicPeriod.id = " + academicPeriodId + " and ea.mode.id = " + modeId + " and ea.bank is not null", EvaluationEventMatter.class);
		
		List<EvaluationEventMatter> evaluationEventMatters = query.getResultList();
	
		return evaluationEventMatters;
	}
	
	@Override
	public List<EvaluationEventMatter> findByEvaluationEvent(
			Long evaluationEventId) {
		TypedQuery<EvaluationEventMatter> query = this.em.createQuery("select ea from " + EvaluationEventMatter.class.getSimpleName() + 
				" ea where ea.evaluationEvent.id = " + evaluationEventId + " order by ea.matter.name asc", EvaluationEventMatter.class);
		
		List<EvaluationEventMatter> evaluationEventMatters = query.getResultList();
		
		return evaluationEventMatters;
	}
	
	@Override
	public List<EvaluationEventMatterInfo> findInfoByEvaluationEvent(Long evaluationEventId) {
		TypedQuery<Object[]> query = this.em.createQuery("select ea.id, ea.matter.id, ea.matter.name, ea.matter.code, ea.academicPeriod.name, ea.matter.academicLevel.name, ea.mode.name, ea.bank.id from " + EvaluationEventMatter.class.getSimpleName() + 
				" ea where ea.evaluationEvent.id = " + evaluationEventId + " order by ea.matter.name asc", Object[].class);
		
		List<Object[]> evaluationAssignmentDataList = query.getResultList();

		List<EvaluationEventMatterInfo> evaluationEventMatterInfos = new ArrayList<EvaluationEventMatterInfo>();
		for (Object[] evaluationAssignmentData : evaluationAssignmentDataList) {
			JSONObject evaluationEventMatterInfoJson = new JSONObject();
			evaluationEventMatterInfoJson.put("id", evaluationAssignmentData[0]);
			evaluationEventMatterInfoJson.put("matterId", evaluationAssignmentData[1]);
			evaluationEventMatterInfoJson.put("matterName", evaluationAssignmentData[2]);
			evaluationEventMatterInfoJson.put("matterCode", evaluationAssignmentData[3]);
			evaluationEventMatterInfoJson.put("academicPeriodName", evaluationAssignmentData[4]);
			evaluationEventMatterInfoJson.put("academicLevelName", evaluationAssignmentData[5]);
			evaluationEventMatterInfoJson.put("modeName", evaluationAssignmentData[6]);
			evaluationEventMatterInfoJson.put("hasBank", evaluationAssignmentData[7] != null ? true : false);

			evaluationEventMatterInfos.add(new EvaluationEventMatterInfo(evaluationEventMatterInfoJson));
		}
		
		return evaluationEventMatterInfos;
	}
	
	@Override
	public List<EvaluationEventMatterInfo> findInfoByAdmissionEvaluationEvent(Long evaluationEventId) {
		TypedQuery<Object[]> query = this.em.createQuery("select ea.id, ea.matter.id, ea.matter.name, ea.matter.code, ea.bank.id from " + EvaluationEventMatter.class.getSimpleName() + 
				" ea where ea.evaluationEvent.id = " + evaluationEventId + " order by ea.matter.name asc", Object[].class);
		
		List<Object[]> evaluationAssignmentDataList = query.getResultList();

		List<EvaluationEventMatterInfo> evaluationEventMatterInfos = new ArrayList<EvaluationEventMatterInfo>();
		for (Object[] evaluationAssignmentData : evaluationAssignmentDataList) {
			JSONObject evaluationEventMatterInfoJson = new JSONObject();
			evaluationEventMatterInfoJson.put("id", evaluationAssignmentData[0]);
			evaluationEventMatterInfoJson.put("matterId", evaluationAssignmentData[1]);
			evaluationEventMatterInfoJson.put("matterName", evaluationAssignmentData[2]);
			evaluationEventMatterInfoJson.put("matterCode", evaluationAssignmentData[3]);
			evaluationEventMatterInfoJson.put("hasBank", evaluationAssignmentData[4] != null ? true : false);

			evaluationEventMatterInfos.add(new EvaluationEventMatterInfo(evaluationEventMatterInfoJson));
		}
		
		return evaluationEventMatterInfos;
	}
	
	@Override
	public EvaluationEventMatter findByEvaluationEventAndMatter(Long evaluationEventId, Long matterId) {
		TypedQuery<EvaluationEventMatter> query = this.em.createQuery("select ea from " + EvaluationEventMatter.class.getSimpleName() + 
				" ea where ea.evaluationEvent.id = " + evaluationEventId + " and ea.matter.id =" + matterId, EvaluationEventMatter.class);
		
		List<EvaluationEventMatter> evaluationEventMatters = query.getResultList();
		EvaluationEventMatter evaluationEventMatter = null;
		if(evaluationEventMatters != null && evaluationEventMatters.size() > 0) {
			evaluationEventMatter = evaluationEventMatters.get(0);
		} 

		return evaluationEventMatter;
	}
	
	@Override
	public List<EvaluationEventMatter> findByEvaluationEventWithBank(
			Long evaluationEventId) {
		TypedQuery<EvaluationEventMatter> query = this.em.createQuery("select ea from " + EvaluationEventMatter.class.getSimpleName() + 
				" ea where ea.evaluationEvent.id = " + evaluationEventId + " and ea.bank is not null", EvaluationEventMatter.class);
		
		List<EvaluationEventMatter> evaluationEventMatters = query.getResultList();
		
		return evaluationEventMatters;
	}
	
	@Override
	public Long countByEvaluationEventWithBank(Long evaluationEventId) {
		TypedQuery<Long> query = this.em.createQuery("select count(eem) from " + EvaluationEventMatter.class.getSimpleName() +
				" eem where eem.evaluationEvent.id=:evaluationEventId and eem.bank is not null", Long.class);
	
		query.setParameter("evaluationEventId", evaluationEventId);
		List<Long> dataList = query.getResultList();
		
		return dataList.get(0);
	}

	@Override
	public List<WeekDay> getDaysAllowed(Long evaluationEventMatterId) {
		TypedQuery<WeekDay> query = this.em.createQuery(
				"SELECT da FROM " + EvaluationEventMatter.class.getSimpleName() + 
				" eem join eem.daysAllowed da WHERE eem.id=:evaluationEventMatterId", WeekDay.class);
		
		query.setParameter("evaluationEventMatterId", evaluationEventMatterId);
		List<WeekDay> daysAllowed = new ArrayList<WeekDay>(query.getResultList());
		
		return daysAllowed;
	}

	@Override
	public boolean hasTests(Long evaluationEventMatterId) {
		TypedQuery<Long> query = this.em.createQuery(
	    		"SELECT COUNT(*) FROM " + EvaluationEventMatterTest.class.getSimpleName() + 
				" eemt WHERE eemt.evaluationEventMatter.id=:evaluationEventMatterId", Long.class);
		
	    query.setParameter("evaluationEventMatterId", evaluationEventMatterId);
	    long numTests = query.getSingleResult();
	    
	    return numTests > 0 ? true : false;
	}
	
	public List<EvaluationEventMatter> getEvalMattersByEvaluationEventAndBankDepartment(Long evaluationEventId, Long departmentId) {
		String queryJql = "select eem from " + EvaluationEventMatter.class.getSimpleName() + " eem " +
				"where eem.bank is not null"; 
		
		if(evaluationEventId != null && evaluationEventId > 0) {
			queryJql += " and eem.evaluationEvent.id=" + evaluationEventId;  
		}
		
		if(departmentId != null && departmentId > 0) {
			queryJql += " and eem.bank.department.id=" + departmentId;  
		}
		
		TypedQuery<EvaluationEventMatter> query = this.em.createQuery(queryJql, EvaluationEventMatter.class);
			
		return query.getResultList();
	}
}

