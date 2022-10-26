package es.grammata.evaluation.evs.data.services.repository;

import java.util.List;

import es.grammata.evaluation.evs.data.model.repository.EvaluationType;
import es.grammata.evaluation.evs.data.services.base.BaseService;

public interface EvaluationTypeService extends BaseService<EvaluationType> {
	public EvaluationType findByCode(String code);
	public EvaluationType findByCodeWs(String code);
	public List<EvaluationType> findAll();
}

