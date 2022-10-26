package es.grammata.evaluation.evs.data.services.repository.impl;

import java.util.List;

import javax.persistence.TypedQuery;

import org.hibernate.ejb.QueryHints;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import es.grammata.evaluation.evs.data.model.repository.EvaluationType;
import es.grammata.evaluation.evs.data.services.base.impl.BaseServiceImpl;
import es.grammata.evaluation.evs.data.services.repository.EvaluationTypeService;


@Repository
@Transactional(readOnly = true)
public class EvaluationTypeServiceImpl extends BaseServiceImpl<EvaluationType> implements EvaluationTypeService {
	public EvaluationType findByCode(String code) {
		TypedQuery<EvaluationType> query = this.em.createQuery("select r from " + EvaluationType.class.getSimpleName() + 
				" r where r.code='" + code + "'", EvaluationType.class);
		
		List<EvaluationType> evaluationTypes = query.getResultList();
		EvaluationType evaluationType = null;
		if(evaluationTypes != null && evaluationTypes.size() > 0) {
			evaluationType = evaluationTypes.get(0);
		}
		
		return evaluationType;
	}
	
	public EvaluationType findByCodeWs(String code) {
		TypedQuery<EvaluationType> query = this.em.createQuery("select r from " + EvaluationType.class.getSimpleName() + 
				" r where r.codeWS='" + code + "'", EvaluationType.class);
		
		List<EvaluationType> evaluationTypes = query.getResultList();
		EvaluationType evaluationType = null;
		if(evaluationTypes != null && evaluationTypes.size() > 0) {
			evaluationType = evaluationTypes.get(0);
		}
		
		return evaluationType;
	}
	
	@Override
	public List<EvaluationType> findAll() {
		TypedQuery<EvaluationType> query = this.em.createQuery("from " + EvaluationType.class.getName(), 
				EvaluationType.class);
	    query.setHint(QueryHints.HINT_CACHEABLE, true);
	    return query.getResultList();
	}   
}
