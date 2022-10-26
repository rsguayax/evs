package es.grammata.evaluation.evs.data.services.repository.impl;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import es.grammata.evaluation.evs.data.model.repository.Degree;
import es.grammata.evaluation.evs.data.services.base.impl.BaseServiceImpl;
import es.grammata.evaluation.evs.data.services.repository.DegreeService;

/**
 * 
 * Clase DegreeServiceImpl Hereda los servicios
 * 
 * @author andres
 *
 */
@Repository
@Transactional(readOnly = true)
public class DegreeServiceImpl extends BaseServiceImpl<Degree> implements DegreeService {

	@Override
	public void deletLogic(Degree entity) {
		// TODO Auto-generated method stub
		if (entity.getId() != null) {
			em.merge(entity);
			em.flush();
		}

	}

	@Override
	public List<Degree> findAllAtive() {
		TypedQuery<Degree> query = this.em.createQuery(
				"select r from " + Degree.class.getSimpleName() + " r where r.active='" + true + "'", Degree.class);

		List<Degree> degrees = query.getResultList();
		return degrees;
	}

	@Override
	public Degree findByCode(String code) {
		TypedQuery<Degree> query = this.em.createQuery("select d from " + Degree.class.getSimpleName() + 
				" d where d.code='" + code + "'", Degree.class);
		
		List<Degree> degrees = query.getResultList();
		Degree degree = null;
		if(degrees != null && degrees.size() > 0) {
			degree = degrees.get(0);
		}
		
		return degree;
	}
}
