package es.grammata.evaluation.evs.data.services.repository.impl;

import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import es.grammata.evaluation.evs.data.model.repository.AdmissionGrade;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEventDegreeSubject;
import es.grammata.evaluation.evs.data.services.base.impl.BaseServiceImpl;
import es.grammata.evaluation.evs.data.services.repository.AdmissionGradeService;

@Repository
@Transactional(readOnly = true)
public class AdmissionGradeServiceImpl extends BaseServiceImpl<AdmissionGrade> implements AdmissionGradeService {

	@Override
	public List<Object> findAllQualificationByDegreeAndUser(Long degreeId, Long userId) {
		// TODO Auto-generated method stub
		Query query = em.createQuery("select ad.matter.id,ad.grade||' ', ev.weight||' ',(ad.grade*ev.weight)||' ' from "+AdmissionGrade.class.getSimpleName()+
				" ad,"+ EvaluationEventDegreeSubject.class.getSimpleName()+" ev "+
				" where ad.matter.id = ev.subject.id"+
				" and ad.evaluation_event.id = ev.evaluationEvent.id"+
				" and ad.user.id ="+userId+
				" and ev.degree.id ="+degreeId);
		return query.getResultList();
	}

	@Override
	public List<AdmissionGrade> findByEvaluationEventId(Long evaluationEventId) {
		// TODO Auto-generated method stub
		TypedQuery<AdmissionGrade> query = em.createQuery("select c from " + AdmissionGrade.class.getSimpleName()
				+ " c where c.evaluation_event.id = "+evaluationEventId, AdmissionGrade.class);
		List<AdmissionGrade> evaluationEventDegrees = query.getResultList();
		
		return evaluationEventDegrees;
	}

	@Override
	public List<AdmissionGrade> findByEvaluationEventOrderByGrade(Long evaluationEventId) {
		TypedQuery<AdmissionGrade> query = em.createQuery("select c from " + AdmissionGrade.class.getSimpleName()
				+ " c where c.evaluation_event.id = "+evaluationEventId 
				+ " ORDER BY c.grade DESC", AdmissionGrade.class);
		List<AdmissionGrade> evaluationEventDegrees = query.getResultList();
		
		return evaluationEventDegrees;
	}	
}
