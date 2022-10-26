package es.grammata.evaluation.evs.data.services.repository.impl;

import java.util.List;

import javax.persistence.TypedQuery;

import org.hibernate.ejb.QueryHints;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import es.grammata.evaluation.evs.data.model.repository.AcademicPeriod;
import es.grammata.evaluation.evs.data.services.base.impl.BaseServiceImpl;
import es.grammata.evaluation.evs.data.services.repository.AcademicPeriodService;


@Repository
@Transactional(readOnly = true)
public class AcademicPeriodServiceImpl extends BaseServiceImpl<AcademicPeriod> implements AcademicPeriodService {
	public AcademicPeriod findByCode(String code) {
		TypedQuery<AcademicPeriod> query = this.em.createQuery("select b from " + AcademicPeriod.class.getSimpleName() + 
				" b where b.code='" + code + "'", AcademicPeriod.class);
		
		List<AcademicPeriod> academicPeriods = query.getResultList();
		AcademicPeriod aPeriod = null;
		if(academicPeriods != null && academicPeriods.size() > 0) {
			aPeriod = academicPeriods.get(0);
		}
		
		return aPeriod;
	}
	
	public AcademicPeriod findByExternalId(String externaId) {
		TypedQuery<AcademicPeriod> query = this.em.createQuery("select b from " + AcademicPeriod.class.getSimpleName() + 
				" b where b.externalId='" + externaId + "'", AcademicPeriod.class);
		
		List<AcademicPeriod> academicPeriods = query.getResultList();
		AcademicPeriod aPeriod = null;
		if(academicPeriods != null && academicPeriods.size() > 0) {
			aPeriod = academicPeriods.get(0);
		}
		
		return aPeriod;
	}
	
	@Override
	public List<AcademicPeriod> findAll() {
		TypedQuery<AcademicPeriod> query = this.em.createQuery("from " + AcademicPeriod.class.getName(), AcademicPeriod.class);
	    query.setHint(QueryHints.HINT_CACHEABLE, true);
	    return query.getResultList();
	}   
	
	@Override
	public List<AcademicPeriod> findActive() {
		TypedQuery<AcademicPeriod> query = this.em.createQuery("select ap from " + AcademicPeriod.class.getName() + " ap where ap.active=True", 
				AcademicPeriod.class);
	    return query.getResultList();
	}

	@Override
	public void updateAllAsNotActive() {
		String query = "UPDATE academic_period SET active=False";
		em.createNativeQuery(query).executeUpdate();	
		em.flush();
	}   
}
