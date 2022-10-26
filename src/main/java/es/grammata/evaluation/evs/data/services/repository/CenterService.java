package es.grammata.evaluation.evs.data.services.repository;

import java.util.List;

import es.grammata.evaluation.evs.data.model.repository.Center;
import es.grammata.evaluation.evs.data.services.base.BaseService;

public interface CenterService extends BaseService<Center> {
	public List<Center> findUnassigned();
	public Center findByCode(String Code);
	public Center findByUniqueCode(String uniqueCode);
	public Center findByExternalId(String externaId);
	public List<Center> findActive();
	public void updateAllAsNotActive();
}

