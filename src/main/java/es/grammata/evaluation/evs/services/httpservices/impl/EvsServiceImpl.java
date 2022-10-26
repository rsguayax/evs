package es.grammata.evaluation.evs.services.httpservices.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import es.grammata.evaluation.evs.data.model.repository.Bank;
import es.grammata.evaluation.evs.data.services.repository.BankService;
import es.grammata.evaluation.evs.services.httpservices.EvsService;
import es.grammata.evaluation.evs.services.httpservices.wrapper.BankWrap;

public class EvsServiceImpl implements EvsService {
	
	@Autowired
	public BankService bankService;
	
	@Override
	public List<BankWrap> getAllBanks() {		
		List<Bank> banks = bankService.findAll();
		List<BankWrap> bankWrappers = new ArrayList<BankWrap>();
		for(Bank bank : banks) {
			BankWrap bankWrap = this.populateBankWrapper(bank);
			bankWrappers.add(bankWrap);
		}
		return bankWrappers;
	}
	
	@Override
	public void createBank(BankWrap bankWrapper) {
		Bank bank = this.populateBank(bankWrapper);
		bankService.save(bank);
	}
	
	@Override
	public void updateBank(BankWrap bankWrapper) {
		Bank bank = this.populateBank(bankWrapper);
		Bank bankOld = bankService.findByExternalId(bank.getExternalId());
		bankOld.setName(bankWrapper.getName());
		bankService.update(bankOld);
	}
	
	public Bank populateBank(BankWrap bankWrap) {
	    Bank bank = new Bank();
	    /*bank.setCreateDate(bankWrap.getCreateDate());
	    bank.setCurrentNumber(bankWrap.getCurrentNumber());
	    bank.setQuestionNumber(bankWrap.getQuestionNumber());
	    bank.setExternalId(bankWrap.getExternalId());
	    bank.setIsComplete(bankWrap.getIsComplete());*/
	    bank.setName(bankWrap.getName());
	    bank.setExternalId(bankWrap.getExternalId());
	
		return bank;
	}
	

	public BankWrap populateBankWrapper(Bank bank) {
	    BankWrap bankWrap = new BankWrap();
	    /*bankWrap.setCreateDate(bank.getCreateDate());
	    bankWrap.setCurrentNumber(bank.getCurrentNumber());
	    bankWrap.setQuestionNumber(bank.getQuestionNumber());
	    bankWrap.setExternalId(bank.getExternalId());
	    bankWrap.setIsComplete(bank.getIsComplete());*/
	    bankWrap.setName(bank.getName());
	    bankWrap.setExternalId(bank.getExternalId());
	
		return bankWrap;
	}	
}
