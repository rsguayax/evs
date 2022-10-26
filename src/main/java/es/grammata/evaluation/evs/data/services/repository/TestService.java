package es.grammata.evaluation.evs.data.services.repository;

import java.util.List;

import es.grammata.evaluation.evs.data.model.repository.Test;
import es.grammata.evaluation.evs.data.services.base.BaseService;

public interface TestService extends BaseService<Test> {
	public Test findByExternalId(Integer externalId);
	public int countWithoutEvaluationType();
	public List<Test> findByBank(Long bankId);
	public List<Test> findByBankAndActive(Long bankId);
	public List<Test> findByBankOrderByActive(Long bankId);
}

