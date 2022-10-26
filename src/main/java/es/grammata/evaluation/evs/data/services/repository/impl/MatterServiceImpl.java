package es.grammata.evaluation.evs.data.services.repository.impl;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Strings;

import es.grammata.evaluation.evs.data.model.repository.CorrectionRule;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEventMatter;
import es.grammata.evaluation.evs.data.model.repository.Matter;
import es.grammata.evaluation.evs.data.services.base.impl.BaseServiceImpl;
import es.grammata.evaluation.evs.data.services.repository.CorrectionRuleService;
import es.grammata.evaluation.evs.data.services.repository.MatterService;

@Repository
@Transactional(readOnly = true)
public class MatterServiceImpl extends BaseServiceImpl<Matter> implements MatterService {
	
	@Autowired
	private CorrectionRuleService correctionRuleService;
	
	public List<Matter> getMattersByEvaluationEvent(Long id, boolean hasBank) {
		String queryJql = "select m from " + Matter.class.getSimpleName() + 
				" m, " + EvaluationEventMatter.class.getSimpleName() + " eem where eem.evaluationEvent.id=" + id + " and eem.matter.id=m.id"; 
		
		if(hasBank) {
			queryJql += " and eem.bank is not null";
		}
		
		TypedQuery<Matter> query = this.em.createQuery(queryJql, Matter.class);
			
		return query.getResultList();
	}
	
	public void updateData(Matter newMatter) {
		Matter matter = this.findById(newMatter.getId());
		CorrectionRule correctionRule = newMatter.getCorrectionRule();
		newMatter.setBanks(matter.getBanks());
		
		if (correctionRule != null && correctionRule.getMinGrade() != null && correctionRule.getMaxGrade() != null && !Strings.isNullOrEmpty(correctionRule.getType())) {	
			correctionRule.setMatter(newMatter);
			
			if (newMatter.getCorrectionRule().getId() == null) {
				correctionRuleService.save(correctionRule);
			} else {
				correctionRuleService.update(correctionRule);
			}
		} else {
			newMatter.setCorrectionRule(null);
		}
		
		super.update(newMatter);
	}

	public Matter findByCode(String code) {
		TypedQuery<Matter> query = this.em.createQuery("select r from " + Matter.class.getSimpleName() + 
				" r where r.code='" + code + "'", Matter.class);
		
		List<Matter> matters = query.getResultList();
		Matter matter = null;
		if(matters != null && matters.size() > 0) {
			matter = matters.get(0);
		}
		
		return matter;
	}
	
	public Matter saveOrLoadByCode(Matter matter) {
		Matter matterAux = this.findByCode(matter.getCode());
		if(matterAux == null) {
			this.save(matter);
		} else {
			matter = matterAux;
		}
		
		return matter;
	}
	
	public List<Matter> getMattersByEvaluationEventType(Long evaluationEventTypeId) {
		String queryJql = "select m from " + Matter.class.getSimpleName() + 
				" m join m.banks b where b.evaluationEventType.id=" + evaluationEventTypeId; 
		
		TypedQuery<Matter> query = this.em.createQuery(queryJql, Matter.class);
			
		return query.getResultList();
	}
	
	public List<Matter> getMattersWithoutBank() {
		String queryJql = "select m from " + Matter.class.getSimpleName() + 
				" m WHERE size(m.banks) = 0"; 
		
		TypedQuery<Matter> query = this.em.createQuery(queryJql, Matter.class);
			
		return query.getResultList();
	}
	
	public int countWithoutBank() {
		TypedQuery<Long> query = this.em.createQuery("SELECT COUNT(*) from " + Matter.class.getSimpleName() + 
				" m WHERE size(m.banks) = 0", Long.class);

		long count = query.getSingleResult();
		return (int) count;
	}
}
