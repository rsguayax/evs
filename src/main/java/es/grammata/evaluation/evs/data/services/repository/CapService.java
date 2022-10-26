package es.grammata.evaluation.evs.data.services.repository;

import java.util.List;

import es.grammata.evaluation.evs.data.model.repository.Cap;
import es.grammata.evaluation.evs.data.services.base.BaseService;

public interface CapService extends BaseService<Cap> {
	public List<Cap> findUnassigned(Long evaluationCenterId, Long capId);
}
