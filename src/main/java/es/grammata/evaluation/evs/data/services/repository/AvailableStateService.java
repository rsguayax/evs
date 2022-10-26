package es.grammata.evaluation.evs.data.services.repository;

import es.grammata.evaluation.evs.data.model.repository.AvailableState;
import es.grammata.evaluation.evs.data.services.base.BaseService;

public interface AvailableStateService extends BaseService<AvailableState> {
	
	public AvailableState findByState(String state);
}
