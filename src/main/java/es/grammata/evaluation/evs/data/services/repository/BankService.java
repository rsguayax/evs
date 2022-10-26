package es.grammata.evaluation.evs.data.services.repository;

import java.util.List;

import es.grammata.evaluation.evs.data.model.repository.Bank;
import es.grammata.evaluation.evs.data.services.base.BaseService;

public interface BankService extends BaseService<Bank> {
	public Bank findByExternalId(Integer externalId);
	public boolean checkTests(Bank bank);
	public List<Bank> findActive();
	public List<Bank> findByName(String name);
	public void disableAll();
}

