package es.grammata.evaluation.evs.data.services.repository.impl;

import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import es.grammata.evaluation.evs.data.model.repository.GenericScheduleLog;
import es.grammata.evaluation.evs.data.model.repository.StudentTestScheduleLog;
import es.grammata.evaluation.evs.data.services.base.impl.BaseServiceImpl;
import es.grammata.evaluation.evs.data.services.repository.GenericScheduleLogService;

@Repository
@Transactional(readOnly = true)
public class GenericScheduleLogServiceImpl extends BaseServiceImpl<GenericScheduleLog> implements GenericScheduleLogService {
	
	public List<GenericScheduleLog> getLogGenericByEvaluationEvent(Long evaluationEventId) {
		TypedQuery<GenericScheduleLog> query = this.em.createQuery(
				"SELECT gsl FROM " + GenericScheduleLog.class.getSimpleName() + 
				" gsl WHERE gsl.read=FALSE AND gsl.evaluationEvent.id=:evaluationEventId", GenericScheduleLog.class);
		
		query.setParameter("evaluationEventId", evaluationEventId);
		List<GenericScheduleLog> logs = query.getResultList();
		
		
		return logs;
	}
	
	public void markAsReadByEvaluationEvent(Long evaluationEventId) {
		Query query = em.createQuery("UPDATE " + GenericScheduleLog.class.getSimpleName() +" gsl SET gsl.read=TRUE WHERE gsl.read=FALSE" +
				" AND gsl.evaluationEvent.id=:evaluationEventId");
		
		query.setParameter("evaluationEventId", evaluationEventId);
		
        query.executeUpdate();
	}
	
	public Long getCountByEvaluationEvent(Long evaluationEventId) {
		TypedQuery<Long> query = this.em.createQuery(
				"SELECT COUNT(*) FROM " + GenericScheduleLog.class.getSimpleName() + 
				" gsl WHERE gsl.evaluationEvent.id=:evaluationEventId AND gsl.read=FALSE", Long.class);
		
		query.setParameter("evaluationEventId", evaluationEventId);
		List<Long> dataList = query.getResultList();
		
		return dataList.get(0);
	}
	
	public void deleteByEvaluationEvent(Long evaluationEventId) {

		String query = "delete from generic_schedule_log gsl where gsl.evaluation_event_id=" + evaluationEventId;
		
		em.createNativeQuery(query).executeUpdate();	
		em.flush();
	}

}
