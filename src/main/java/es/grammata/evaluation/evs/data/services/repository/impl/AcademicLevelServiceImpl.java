package es.grammata.evaluation.evs.data.services.repository.impl;

import java.util.List;

import javax.persistence.TypedQuery;

import org.hibernate.ejb.QueryHints;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import es.grammata.evaluation.evs.data.model.repository.AcademicLevel;
import es.grammata.evaluation.evs.data.services.base.impl.BaseServiceImpl;
import es.grammata.evaluation.evs.data.services.repository.AcademicLevelService;


@Repository
@Transactional(readOnly = true)
public class AcademicLevelServiceImpl extends BaseServiceImpl<AcademicLevel> implements AcademicLevelService {
	public AcademicLevel findByCode(String code) {
		TypedQuery<AcademicLevel> query = this.em.createQuery("select r from " + AcademicLevel.class.getSimpleName() + 
				" r where r.code='" + code + "'", AcademicLevel.class);
		
		List<AcademicLevel> academicLevels = query.getResultList();
		AcademicLevel academicLevel = null;
		if(academicLevels != null && academicLevels.size() > 0) {
			academicLevel = academicLevels.get(0);
		}
		
		return academicLevel;
	}
	
	
	@Override
	public List<AcademicLevel> findAll() {
		TypedQuery<AcademicLevel> query = this.em.createQuery("from " + AcademicLevel.class.getName(), AcademicLevel.class);
	    query.setHint(QueryHints.HINT_CACHEABLE, true);
	    return query.getResultList();
	}
	
	@Override
	public List<AcademicLevel> findNotDeleted() {
		TypedQuery<AcademicLevel> query = this.em.createQuery("select al from " + AcademicLevel.class.getName() + " al where al.deleted=False", 
				AcademicLevel.class);
	    return query.getResultList();
	}
	
	@Override
	public void deleteAll() {
		String query = "UPDATE academic_level SET deleted=True";
		em.createNativeQuery(query).executeUpdate();	
		em.flush();
	}
}
