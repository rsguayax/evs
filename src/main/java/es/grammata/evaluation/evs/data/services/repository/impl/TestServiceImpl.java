package es.grammata.evaluation.evs.data.services.repository.impl;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import es.grammata.evaluation.evs.data.model.repository.CorrectionRule;
import es.grammata.evaluation.evs.data.model.repository.Test;
import es.grammata.evaluation.evs.data.services.base.impl.BaseServiceImpl;
import es.grammata.evaluation.evs.data.services.repository.CorrectionRuleService;
import es.grammata.evaluation.evs.data.services.repository.TestService;

@Repository
@Transactional(readOnly = true)
public class TestServiceImpl extends BaseServiceImpl<Test> implements TestService {
	
	@Autowired
	private CorrectionRuleService correctionRuleService;
	
	@Transactional
	public void update(Test entity) {
		CorrectionRule correctionRule = entity.getCorrectionRule();
		if (correctionRule != null) {
			correctionRule.setTest(entity);
			
			if (correctionRule.getId() == null) {
				correctionRuleService.save(correctionRule);
			} else {
				correctionRuleService.update(correctionRule);
			}
		}
		
		super.update(entity);
	}
	
	public Test findByExternalId(Integer externalId) {
		TypedQuery<Test> query = this.em.createQuery("select b from " + Test.class.getSimpleName() + 
				" b where b.externalId=" + externalId, Test.class);
		
		List<Test> tests = query.getResultList();
		Test test = null;
		if(tests != null && tests.size() > 0) {
			test = tests.get(0);
		}
		
		return test;
	}

	@Override
	public int countWithoutEvaluationType() {
		TypedQuery<Long> query = this.em.createQuery("SELECT COUNT(*) from " + Test.class.getSimpleName() + 
				" t where t.evaluationType IS NULL AND t.active=1", Long.class);

		long count = query.getSingleResult();
		return (int) count;
	}
	
	@Override
	public List<Test> findByBank(Long bankId) {
		String queryJql = "select t from " + Test.class.getSimpleName() + 
				" t where t.bank.id=" + bankId; 

		TypedQuery<Test> query = this.em.createQuery(queryJql, Test.class);
			
		return query.getResultList();
	}
	
	@Override
	public List<Test> findByBankAndActive(Long bankId) {
		String queryJql = "select t from " + Test.class.getSimpleName() + 
				" t where t.bank.id=" + bankId + " and t.active=1"; 

		TypedQuery<Test> query = this.em.createQuery(queryJql, Test.class);
			
		return query.getResultList();
	}
	
	@Override
	public List<Test> findByBankOrderByActive(Long bankId) {
		String queryJql = "select t from " + Test.class.getSimpleName() + 
				" t where t.bank.id=" + bankId + " ORDER BY t.active DESC, t.id ASC"; 
		
		TypedQuery<Test> query = this.em.createQuery(queryJql, Test.class);
			
		return query.getResultList();
	}
}
