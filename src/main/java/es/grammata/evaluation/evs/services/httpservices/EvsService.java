package es.grammata.evaluation.evs.services.httpservices;

import java.util.List;

import es.grammata.evaluation.evs.services.httpservices.wrapper.BankWrap;

public interface EvsService {

	public List<BankWrap> getAllBanks();
	
	public void createBank(BankWrap bank);
	
	public void updateBank(BankWrap bank);
}
