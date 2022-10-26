package es.grammata.evaluation.evs.data.services.repository;

import java.util.List;

import es.grammata.evaluation.evs.data.model.repository.GlobalConfig;
import es.grammata.evaluation.evs.data.services.base.BaseService;

public interface GlobalConfigService extends BaseService<GlobalConfig>{

	public List<GlobalConfig>  findAll();	
	public GlobalConfig findByNameConfig(String name) throws Exception;
	
}
