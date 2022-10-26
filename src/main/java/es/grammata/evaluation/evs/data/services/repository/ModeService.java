package es.grammata.evaluation.evs.data.services.repository;

import java.util.List;

import es.grammata.evaluation.evs.data.model.repository.Mode;
import es.grammata.evaluation.evs.data.services.base.BaseService;



public interface ModeService extends BaseService<Mode> {
	public Mode findByCode(String code);
	public List<Mode> findAll();
	public List<Mode> findNotDeleted();
	public void deleteAll();
}
