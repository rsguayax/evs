package es.grammata.evaluation.evs.data.services.repository.impl;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import es.grammata.evaluation.evs.data.model.repository.Bank;
import es.grammata.evaluation.evs.data.model.repository.Test;
import es.grammata.evaluation.evs.data.services.base.impl.BaseServiceImpl;
import es.grammata.evaluation.evs.data.services.repository.BankService;

@Repository
@Transactional(readOnly = true)
public class BankServiceImpl extends BaseServiceImpl<Bank> implements
		BankService {

	public Bank findByExternalId(Integer externalId) {
		TypedQuery<Bank> query = this.em.createQuery("select b from "
				+ Bank.class.getSimpleName() + " b where b.externalId="
				+ externalId, Bank.class);

		List<Bank> banks = query.getResultList();
		Bank bank = null;
		if (banks != null && banks.size() > 0) {
			bank = banks.get(0);
		}

		return bank;
	}

	/**
	 * Retorna true si todos los test activos del banco tienen un evalType asociado
	 * @param bank
	 * @return
	 */
	public boolean checkTests(Bank bank) {
		TypedQuery<Test> query = this.em.createQuery(
				"select t from " + Test.class.getSimpleName()
						+ " t where t.bank.id =" + bank.getId()
						+ "and t.active=1 and t.evaluationType is null" 
						+ "", Test.class);

		List<Test> tests = query.getResultList();
		if (tests != null && tests.size() > 0) {
			return false;
		}

		return true;
	}
	
	public List<Bank> findByName(String name) {
		// Special Chars
		name = name.replace("'", "''");
		
		TypedQuery<Bank> query = this.em.createQuery(
				"select b from " + Bank.class.getSimpleName()
				+ " b where UNACCENT(UPPER(b.name)) LIKE UNACCENT(UPPER('" + name + "'))"
				, Bank.class);

		List<Bank> banks = query.getResultList();
		return banks;
	}
	
	public List<Bank> findActive() {
		TypedQuery<Bank> query = this.em.createQuery(
				"select b from " + Bank.class.getSimpleName()
				+ " b where state!='INACTIVO'"
				, Bank.class);

		List<Bank> banks = query.getResultList();
		return banks;
	}
	
	public void disableAll() {
		String query = "UPDATE bank SET state='INACTIVO'";
		em.createNativeQuery(query).executeUpdate();	
		em.flush();
	}
}
