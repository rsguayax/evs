package es.grammata.evaluation.evs.data.services.repository.impl;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import es.grammata.evaluation.evs.data.model.repository.Schedule;
import es.grammata.evaluation.evs.data.services.base.impl.BaseServiceImpl;
import es.grammata.evaluation.evs.data.services.repository.ScheduleService;

@Repository
@Transactional(readOnly = true)
public class ScheduleServiceImpl extends BaseServiceImpl<Schedule> implements ScheduleService {
    public Schedule findByName(Long evaluationEventId, String name) {
		TypedQuery<Schedule> query = this.em.createQuery("select s from " + Schedule.class.getSimpleName() +
			" s where s.evaluationEvent.id=:evaluationEventId and UPPER(s.name)=:name", Schedule.class);
	
		query.setParameter("evaluationEventId", evaluationEventId);
		query.setParameter("name", name.toUpperCase());
		
		List<Schedule> schedules = query.getResultList();
		Schedule schedule = null;
		if(schedules != null && schedules.size() > 0) {
			schedule = schedules.get(0);
		}
	
		return schedule;
    }

    
    public List<Schedule> findByEvaluationEvent(Long evaluationEventId) {
  		TypedQuery<Schedule> query = this.em.createQuery("select s from " + Schedule.class.getSimpleName() +
  			" s where s.evaluationEvent.id=:evaluationEventId", Schedule.class);
  	
  		query.setParameter("evaluationEventId", evaluationEventId);
  		
  		List<Schedule> schedules = query.getResultList();
  		
  		return schedules;
      }
}
