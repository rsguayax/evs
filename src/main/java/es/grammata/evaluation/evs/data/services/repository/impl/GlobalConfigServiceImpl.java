package es.grammata.evaluation.evs.data.services.repository.impl;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import es.grammata.evaluation.evs.data.model.repository.GlobalConfig;
import es.grammata.evaluation.evs.data.services.base.impl.BaseServiceImpl;
import es.grammata.evaluation.evs.data.services.repository.GlobalConfigService;

@Repository
@Transactional(readOnly = true)
public class GlobalConfigServiceImpl extends BaseServiceImpl<GlobalConfig> implements GlobalConfigService {

	@Override
	public GlobalConfig findByNameConfig(String name) {
		// TODO Auto-generated method stub
		TypedQuery<GlobalConfig> query = em.createQuery("select c from " + GlobalConfig.class.getSimpleName()
				+ " c where c.name = '"+name+"'", GlobalConfig.class);
		GlobalConfig config = query.getSingleResult();
		return config;
	}


}
