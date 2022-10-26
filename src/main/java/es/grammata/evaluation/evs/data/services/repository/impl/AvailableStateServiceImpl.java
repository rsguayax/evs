package es.grammata.evaluation.evs.data.services.repository.impl;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import es.grammata.evaluation.evs.data.model.repository.AvailableState;
import es.grammata.evaluation.evs.data.services.base.impl.BaseServiceImpl;
import es.grammata.evaluation.evs.data.services.repository.AvailableStateService;

@Repository
@Transactional(readOnly = true)
public class AvailableStateServiceImpl extends BaseServiceImpl<AvailableState> implements AvailableStateService {

	@Override
	public AvailableState findByState(String state) {
		TypedQuery<AvailableState> query = this.em.createQuery("select a from " + AvailableState.class.getSimpleName() + 
				" a where a.value='" + state + "'", AvailableState.class);
		
		if (!query.getResultList().isEmpty()) {
			return query.getResultList().get(0);
		} else {
			return null;
		}
	}
}
