package es.grammata.evaluation.evs.data.services.repository.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import es.grammata.evaluation.evs.data.model.repository.EvaluationEventMatter;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEventMatterTest;
import es.grammata.evaluation.evs.data.model.repository.Session;
import es.grammata.evaluation.evs.data.model.repository.Test;
import es.grammata.evaluation.evs.data.services.base.impl.BaseServiceImpl;
import es.grammata.evaluation.evs.data.services.repository.EvaluationEventMatterTestService;
import es.grammata.evaluation.evs.mvc.controller.util.EvaluationReport;


@Repository
@Transactional(readOnly = true)
public class EvaluationEventMatterTestServiceImpl extends BaseServiceImpl<EvaluationEventMatterTest> implements EvaluationEventMatterTestService {
	
	public EvaluationEventMatterTest findByUnique(Test test, EvaluationEventMatter evaluationEventMatter) {	
		TypedQuery<EvaluationEventMatterTest> query = this.em.createQuery("select ea from " + EvaluationEventMatterTest.class.getSimpleName() + 
				" ea where ea.test.id = " + test.getId() + " and ea.evaluationEventMatter.id = " + evaluationEventMatter.getId(), 
				EvaluationEventMatterTest.class);
		
		List<EvaluationEventMatterTest> evaluationEventMatterTests = query.getResultList();
		EvaluationEventMatterTest evaluationEventMatterTest = null;
		if(evaluationEventMatterTests != null && evaluationEventMatterTests.size() > 0) {
			evaluationEventMatterTest = evaluationEventMatterTests.get(0);
		} 

		return evaluationEventMatterTest;
	}
	
	public boolean isAssignedTest(Test test) {
		TypedQuery<Long> query = this.em.createQuery("select count(ea) from " + EvaluationEventMatterTest.class.getSimpleName() + 
				" ea where ea.test.id = " + test.getId(), Long.class);
		
		 long countryCount = query.getSingleResult();
		 
		 return (countryCount>0)?true:false;
	}
	
	public List<EvaluationEventMatterTest> findByEvaluationEvent(Long id, boolean testComplete) {
		
		String sqlQuery = "select ea from " + EvaluationEventMatterTest.class.getSimpleName() + 
				" ea where ea.evaluationEventMatter.evaluationEvent.id = " + id;
		
		if(testComplete) {
			sqlQuery += " and ea.test.evaluationType is not null";
		}
		
		TypedQuery<EvaluationEventMatterTest> query = this.em.createQuery(sqlQuery, 
				EvaluationEventMatterTest.class);
		
		List<EvaluationEventMatterTest> evaluationEventMatterTests = query.getResultList();

		return evaluationEventMatterTests;
	}
	
	public List<EvaluationEventMatterTest> findByEvaluationEventCode(String code, boolean testComplete) {
		
		String sqlQuery = "select ea from " + EvaluationEventMatterTest.class.getSimpleName() + 
				" ea where ea.evaluationEventMatter.evaluationEvent.code = '" + code + "'";
		
		if(testComplete) {
			sqlQuery += " and ea.test.evaluationType is not null";
		}
		
		TypedQuery<EvaluationEventMatterTest> query = this.em.createQuery(sqlQuery, 
				EvaluationEventMatterTest.class);
		
		List<EvaluationEventMatterTest> evaluationEventMatterTests = query.getResultList();

		return evaluationEventMatterTests;
	}
	
	public List<EvaluationEventMatterTest> findByEvaluationEventMatter(Long eemId) {
		String sqlQuery = "select ea from " + EvaluationEventMatterTest.class.getSimpleName() + 
				" ea where ea.evaluationEventMatter.id = " + eemId + "";

		
		TypedQuery<EvaluationEventMatterTest> query = this.em.createQuery(sqlQuery, 
				EvaluationEventMatterTest.class);
		
		List<EvaluationEventMatterTest> evaluationEventMatterTests = query.getResultList();

		return evaluationEventMatterTests;
	}
	
	public List<EvaluationReport> resportEvaluationTests(Long evId, List<Long> matterIds) {
		
		String ids = "";
		if(matterIds.size() > 0) {
			ids = " and eem.id in (";
			for(Long matterId : matterIds) {
				ids += matterId.toString() + ",";
			}
			ids = ids.replaceAll("[,]$", "");
			ids += ")";
		}
		
		
		String sqlQuery = "select m.id as matter_id, m.code as matter_code, m.name as matter_name, " +
				" eemt.id as eemt_id, t.name as test_name, et.code as et_code, eam.channel, count(eam.channel), " +
				" eemt.enablePublish " +
				" from matter m " +
				" inner join evaluation_event_matter eem on m.id = eem.matter_id  " +
				" inner join evaluation_assignment_matter eam on eam.evaluationeventmatter_id = eem.id " +
				" inner join evaluation_event ee on ee.id = eem.evaluationevent_id " +
				" inner join evaluation_event_matter_test eemt on eemt.evaluationeventmatter_id = eem.id " +
				" inner join test t on t.id = eemt.test_id " +
				" inner join evaluation_type et on et.id = t.evaluationtype_id " +
				" where eem.evaluationevent_id = ? " + ids + 
				" group by m.id, m.code, m.name, et.code, eemt.id, t.name, eam.channel";
		
		Query query = em.createNativeQuery(sqlQuery);
	    query.setParameter(1, evId);
	    List<Object[]> sqlResults = (List<Object[]>) query.getResultList();
	
	    List<EvaluationReport> reportResults = new ArrayList<EvaluationReport>();
		for (Object[] row : sqlResults) {
			EvaluationReport reportResult = new EvaluationReport();
			reportResult.setCodigoMateria((String)row[1]);
			
			reportResult.setNombreMateria(stripAccents((String)row[2]));
			reportResult.setNombreTest(stripAccents((String)row[4]));
			reportResult.setTipoEvaluacion((String)row[5]);
			reportResult.setOrigenEvaluacion((String)row[6]);
			BigInteger nExams = getTestCount((BigInteger) row[3], (String) row[6]);
			String nExamsStr = "0";
			try {
				nExamsStr = nExams.toString();
				reportResult.setNExamenes(nExamsStr);
			} catch(Exception ex) {
				ex.printStackTrace();
			}
			
			BigInteger nPublishedExams = getPublishedTestCount((BigInteger) row[3], (String) row[6]);
			String nPublishedExamsStr = "0";
			try {
				nPublishedExamsStr = nPublishedExams.toString();
				reportResult.setExamenesPublicados(nPublishedExamsStr);
			} catch(Exception ex) {
				ex.printStackTrace();
			}
			
			BigInteger nQualifiedExams = getQualifiedTestCount((BigInteger) row[3], (String) row[6]);
			String nQualifiedExamsStr = "0";
			try {
				nQualifiedExamsStr = nQualifiedExams.toString();
				reportResult.setExamenesCalificados(nQualifiedExamsStr);
			} catch(Exception ex) {
				ex.printStackTrace();
			}
			
			Integer enablePublish = (Integer) row[8];
			reportResult.setHabilitadoPublicacion((enablePublish==1?"SI":"NO"));
			
			Float per =  nPublishedExams.floatValue()*100/nExams.floatValue();
			reportResult.setPorcentajePublicacion(per.toString());
			
			
			reportResults.add(reportResult);
	    }

	    return reportResults;
	}

	
	private BigInteger getTestCount(BigInteger eemtId, String channel) {
		String sqlQuery = "select count(mts.id) from matter_test_student mts " +
				" inner join evaluation_event_matter_test eemt on mts.evaluationeventmattertest_id = eemt.id " +
				" inner join evaluation_assignment_matter eam on eam.id = mts.evaluationassignmentmatter_id " +
				" where eemt.id=:eemtId and eam.channel = :channel";

	
	    Query query = em.createNativeQuery(sqlQuery);
		query.setParameter("eemtId", eemtId);
		query.setParameter("channel", channel);
	    BigInteger count = (BigInteger) query.getSingleResult();
	    return count;
	}
	
	private BigInteger getPublishedTestCount(BigInteger eemtId, String channel) {
		String sqlQuery = "select count(mts.id) from matter_test_student mts " +
				" inner join evaluation_event_matter_test eemt on mts.evaluationeventmattertest_id = eemt.id " +
				" inner join evaluation_assignment_matter eam on eam.id = mts.evaluationassignmentmatter_id " +
				" inner join session s on s.id = mts.session_id " +
				" where eemt.id=:eemtId and eam.channel = :channel and (s.state = :state1 OR s.state = :state2)";

	
	    Query query = em.createNativeQuery(sqlQuery);
		query.setParameter("eemtId", eemtId);
		query.setParameter("channel", channel);
		query.setParameter("state1", Session.STATE_PUBLISHED);
		query.setParameter("state2", Session.STATE_NOTIFIED);
	    BigInteger count = (BigInteger) query.getSingleResult();
	    return count;
	}
	
	private BigInteger getQualifiedTestCount(BigInteger eemtId, String channel) {
		String sqlQuery = "select count(mts.id) from matter_test_student mts " +
				" inner join evaluation_event_matter_test eemt on mts.evaluationeventmattertest_id = eemt.id " +
				" inner join evaluation_assignment_matter eam on eam.id = mts.evaluationassignmentmatter_id " +
				" inner join session s on s.id = mts.session_id " +
				" where eemt.id=:eemtId and eam.channel = :channel and s.state is not NULL";

	
	    Query query = em.createNativeQuery(sqlQuery);
		query.setParameter("eemtId", eemtId);
		query.setParameter("channel", channel);
	    BigInteger count = (BigInteger) query.getSingleResult();
	    return count;
	}
	
	public List<EvaluationEventMatterTest> getTestsByEvaluationEventAndBankDepartment(Long evaluationEventId, Long departmentId) {
		String queryJql = "select eemt from " + EvaluationEventMatterTest.class.getSimpleName() + " eemt " +
				"where eemt.evaluationEventMatter.bank is not null"; 
		
		if(evaluationEventId != null && evaluationEventId > 0) {
			queryJql += " and eemt.evaluationEventMatter.evaluationEvent.id=" + evaluationEventId;  
		}
		
		if(departmentId != null && departmentId > 0) {
			queryJql += " and eemt.evaluationEventMatter.bank.department=" + departmentId;  
		}
		
		TypedQuery<EvaluationEventMatterTest> query = this.em.createQuery(queryJql, EvaluationEventMatterTest.class);
			
		return query.getResultList();
	}
	
	
	
	
	
	private static final String ORIGINAL = "ÁáÉéÍíÓóÚúÑñÜü";
	private static final String REPLACEMENT = "AaEeIiOoUuNnUu";
	private static String stripAccents(String str) {
		if (str == null) {
		    return null;
		}
		char[] array = str.toCharArray();
		for (int index = 0; index < array.length; index++) {
		    int pos = ORIGINAL.indexOf(array[index]);
		    if (pos > -1) {
		        array[index] = REPLACEMENT.charAt(pos);
		    }
		}
		return new String(array);
	}
	
}
