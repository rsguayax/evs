package es.grammata.evaluation.evs.data.services.repository.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import es.grammata.evaluation.evs.data.model.repository.EvaluationAssignment;
import es.grammata.evaluation.evs.data.model.repository.EvaluationAssignmentMatter;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEventMatterTest;
import es.grammata.evaluation.evs.data.model.repository.MatterTestStudent;
import es.grammata.evaluation.evs.data.model.repository.Session;
import es.grammata.evaluation.evs.data.model.repository.StudentEvaluation;
import es.grammata.evaluation.evs.data.services.base.impl.BaseServiceImpl;
import es.grammata.evaluation.evs.data.services.repository.MatterTestStudentService;
import es.grammata.evaluation.evs.data.services.repository.ScheduleModificationService;
import es.grammata.evaluation.evs.mvc.controller.util.GenericInfo;
import es.grammata.evaluation.evs.mvc.controller.util.MatterTestStudentInfo;

@Repository
@Transactional(readOnly = true)
public class MatterTestStudentServiceImpl extends BaseServiceImpl<MatterTestStudent> implements MatterTestStudentService {

	@Autowired
	private ScheduleModificationService scheduleModificationService;

	public MatterTestStudent findByUnique(EvaluationEventMatterTest evaluationEventMatterTest, EvaluationAssignmentMatter evaluationAssignmentMatter) {
		TypedQuery<MatterTestStudent> query = this.em.createQuery("select ea from " + MatterTestStudent.class.getSimpleName() +
				" ea where ea.evaluationEventMatterTest.id = " + evaluationEventMatterTest.getId() +
				" and ea.evaluationAssignmentMatter.id = " + evaluationAssignmentMatter.getId(), MatterTestStudent.class);

		List<MatterTestStudent> matterTestStudents = query.getResultList();
		MatterTestStudent matterTestStudent = null;
		if(matterTestStudents != null && matterTestStudents.size() > 0) {
			matterTestStudent = matterTestStudents.get(0);
		}

		return matterTestStudent;
	}

	public List<MatterTestStudent> findByMatter(Long evaluationEventId, Long matterId) {
		String query = "select eam from " + MatterTestStudent.class.getSimpleName() +
				" eam where eam.evaluationEventMatterTest.evaluationEventMatter.evaluationEvent.id = " + evaluationEventId +
				" and eam.evaluationEventMatterTest.evaluationEventMatter.matter.id = " + matterId +
				" and eam.evaluationAssignmentMatter.evaluationEventMatter.evaluationEvent.id = " + evaluationEventId +
				" and eam.evaluationAssignmentMatter.evaluationEventMatter.matter.id = " + matterId +
				" and eam.evaluationEventMatterTest.evaluationEventMatter.evaluationEvent.id = eam.evaluationAssignmentMatter.evaluationEventMatter.evaluationEvent.id " +
				" and eam.evaluationEventMatterTest.evaluationEventMatter.matter.id = eam.evaluationAssignmentMatter.evaluationEventMatter.matter.id";

		TypedQuery<MatterTestStudent> typedQuery = this.em.createQuery(query, MatterTestStudent.class);

		List<MatterTestStudent> matters = typedQuery.getResultList();

		return matters;
	}

	public List<MatterTestStudentInfo> findInfoWithoutStudentEvaluationByEvaluationAssignment(Long evaluationAssignmentId) {
		TypedQuery<Object[]> query = this.em.createQuery(
				"SELECT mts.id, mts.evaluationEventMatterTest.evaluationEventMatter.matter.name, mts.evaluationEventMatterTest.test.name," +
				" mts.evaluationAssignmentMatter.center.id, mts.evaluationEventMatterTest.evaluationEventMatter.id, mts.evaluationAssignmentMatter.id" +
				" FROM " + MatterTestStudent.class.getSimpleName() + " mts" +
				" WHERE mts.evaluationAssignmentMatter.evaluationAssignment.id=:evaluationAssignmentId AND mts.studentEvaluation is null", Object[].class);

		query.setParameter("evaluationAssignmentId", evaluationAssignmentId);
		List<Object[]> matterTestStudentDataList = query.getResultList();

		List<MatterTestStudentInfo> matterTestStudentInfos  = new ArrayList<MatterTestStudentInfo>();
		for (Object[] matterTestStudentData : matterTestStudentDataList) {
			JSONObject matterTestStudentInfoJson = new JSONObject();
			matterTestStudentInfoJson.put("id", matterTestStudentData[0]);
			matterTestStudentInfoJson.put("matterName", matterTestStudentData[1]);
			matterTestStudentInfoJson.put("testName", matterTestStudentData[2]);
			matterTestStudentInfoJson.put("centerId", matterTestStudentData[3]);
			matterTestStudentInfoJson.put("evaluationEventMatterId", matterTestStudentData[4]);
			matterTestStudentInfoJson.put("evaluationAssignmentMatterId", matterTestStudentData[5]);
			matterTestStudentInfos.add(new MatterTestStudentInfo(matterTestStudentInfoJson));
		}

		return matterTestStudentInfos;
	}

	public List<MatterTestStudent> findWithoutStudentEvaluationByEvaluationAssignment(Long evaluationAssignmentId) {
		TypedQuery<MatterTestStudent> query = this.em.createQuery(
				"SELECT mts FROM " + MatterTestStudent.class.getSimpleName() +
				" mts WHERE mts.evaluationAssignmentMatter.evaluationAssignment.id=:evaluationAssignmentId AND mts.studentEvaluation IS NULL", MatterTestStudent.class);

		query.setParameter("evaluationAssignmentId", evaluationAssignmentId);
		List<MatterTestStudent> matterTestStudents = query.getResultList();

		return matterTestStudents;
	}
	
	
	public List<MatterTestStudent> findByStudentEvaluation(Long studentEvaluationId) {
		TypedQuery<MatterTestStudent> query = this.em.createQuery(
				"SELECT mts FROM " + MatterTestStudent.class.getSimpleName() +
				" mts WHERE mts.studentEvaluation.id=:studentEvaluationId", MatterTestStudent.class);

		query.setParameter("studentEvaluationId", studentEvaluationId);
		List<MatterTestStudent> matterTestStudents = query.getResultList();

		return matterTestStudents;
	}

	public List<MatterTestStudentInfo> findInfoByEvaluationAssignment(Long evaluationAssignmentId) {
		TypedQuery<Object[]> query = this.em.createQuery(
				"SELECT mts.id, mts.evaluationEventMatterTest.evaluationEventMatter.matter.name, mts.evaluationEventMatterTest.test.name," +
				" mts.studentEvaluation.id, mts.evaluationAssignmentMatter.center.id, mts.evaluationEventMatterTest.evaluationEventMatter.id," +
				" mts.evaluationAssignmentMatter.id" +
				" FROM " + MatterTestStudent.class.getSimpleName() + " mts" +
				" WHERE mts.evaluationAssignmentMatter.evaluationAssignment.id=:evaluationAssignmentId", Object[].class);

		query.setParameter("evaluationAssignmentId", evaluationAssignmentId);
		List<Object[]> matterTestStudentDataList = query.getResultList();

		List<MatterTestStudentInfo> matterTestStudentInfos  = new ArrayList<MatterTestStudentInfo>();
		for (Object[] matterTestStudentData : matterTestStudentDataList) {
			JSONObject matterTestStudentInfoJson = new JSONObject();
			matterTestStudentInfoJson.put("id", matterTestStudentData[0]);
			matterTestStudentInfoJson.put("matterName", matterTestStudentData[1]);
			matterTestStudentInfoJson.put("testName", matterTestStudentData[2]);
			matterTestStudentInfoJson.put("studentEvaluationId", matterTestStudentData[3]);
			matterTestStudentInfoJson.put("centerId", matterTestStudentData[4]);
			matterTestStudentInfoJson.put("evaluationEventMatterId", matterTestStudentData[5]);
			matterTestStudentInfoJson.put("evaluationAssignmentMatterId", matterTestStudentData[6]);
			matterTestStudentInfos.add(new MatterTestStudentInfo(matterTestStudentInfoJson));
		}

		return matterTestStudentInfos;
	}

	public MatterTestStudentInfo findInfoById(Long id) {
		TypedQuery<Object[]> query = this.em.createQuery(
				"SELECT mts.id, mts.evaluationEventMatterTest.evaluationEventMatter.matter.name, mts.evaluationEventMatterTest.test.name," + 
				" mts.studentEvaluation.id, mts.evaluationAssignmentMatter.center.id, mts.evaluationEventMatterTest.evaluationEventMatter.id," +
				" mts.evaluationAssignmentMatter.id" +
				" FROM " + MatterTestStudent.class.getSimpleName() + " mts" +
				" WHERE mts.id=:id", Object[].class);
		
		query.setParameter("id", id);
		List<Object[]> matterTestStudentDataList = query.getResultList();
		
		List<MatterTestStudentInfo> matterTestStudentInfos  = new ArrayList<MatterTestStudentInfo>();
		for (Object[] matterTestStudentData : matterTestStudentDataList) {
			JSONObject matterTestStudentInfoJson = new JSONObject();
			matterTestStudentInfoJson.put("id", matterTestStudentData[0]);
			matterTestStudentInfoJson.put("matterName", matterTestStudentData[1]);
			matterTestStudentInfoJson.put("testName", matterTestStudentData[2]);
			matterTestStudentInfoJson.put("studentEvaluationId", matterTestStudentData[3]);
			matterTestStudentInfoJson.put("centerId", matterTestStudentData[4]);
			matterTestStudentInfoJson.put("evaluationEventMatterId", matterTestStudentData[5]);
			matterTestStudentInfoJson.put("evaluationAssignmentMatterId", matterTestStudentData[6]);
			matterTestStudentInfos.add(new MatterTestStudentInfo(matterTestStudentInfoJson));
		}
		
		if (!matterTestStudentInfos.isEmpty()) {
			return matterTestStudentInfos.get(0);
		}

		return null;
	}
	
	@Transactional
	public void updateStudentEvaluation(StudentEvaluation studentEvaluation, List<MatterTestStudentInfo> matterTestStudentInfos) {
		ArrayList<Long> matterTestStudentIds = new ArrayList<Long>();
		for (MatterTestStudentInfo matterTestStudentInfo : matterTestStudentInfos) {
			matterTestStudentIds.add(matterTestStudentInfo.getId());
		}

		Query query = em.createQuery("UPDATE " + MatterTestStudent.class.getSimpleName() +" mts SET mts.studentEvaluation=:studentEvaluation WHERE id IN (:matterTestStudentIds)");
        query.setParameter("studentEvaluation", studentEvaluation);
        query.setParameter("matterTestStudentIds", matterTestStudentIds);
        query.executeUpdate();
	}

	@Transactional
	public void deleteStudentEvaluation(Long matterTestStudentId) {
		Query query = em.createQuery("UPDATE " + MatterTestStudent.class.getSimpleName() +" mts SET mts.studentEvaluation=:studentEvaluation WHERE id=:matterTestStudentId)");
		query.setParameter("studentEvaluation", null);
		query.setParameter("matterTestStudentId", matterTestStudentId);
        query.executeUpdate();
	}
	
	@Transactional
	public void deleteAllByStudentEvaluation(Long studentEvaluationId) {
		Query query = em.createQuery("UPDATE " + MatterTestStudent.class.getSimpleName() +" mts SET mts.studentEvaluation=:studentEvaluation WHERE mts.studentEvaluation.id=:studentEvaluationId)");
		query.setParameter("studentEvaluation", null);
		query.setParameter("studentEvaluationId", studentEvaluationId);
        query.executeUpdate();
	}

	public List<MatterTestStudent> findByEvaluationAssignmentMatter(EvaluationAssignmentMatter evaluationAssignmentMatter) {
		TypedQuery<MatterTestStudent> query = this.em.createQuery("select ea from " + MatterTestStudent.class.getSimpleName() +
				" ea where ea.evaluationAssignmentMatter.id = " + evaluationAssignmentMatter.getId(), MatterTestStudent.class);

		List<MatterTestStudent> matterTestStudents = query.getResultList();

		return matterTestStudents;
	}
	
	public List<MatterTestStudentInfo> findInfoByEvaluationAssignmentMatter(Long evaluationAssignmentMatterId) {
		TypedQuery<Object[]> query = this.em.createQuery(
				"SELECT mts.id, mts.evaluationEventMatterTest.evaluationEventMatter.matter.name, mts.evaluationEventMatterTest.test.name," +
				" mts.evaluationAssignmentMatter.center.id, mts.evaluationEventMatterTest.evaluationEventMatter.id," +
				" mts.evaluationAssignmentMatter.id" +
				" FROM " + MatterTestStudent.class.getSimpleName() + " mts" +
				" WHERE mts.evaluationAssignmentMatter.id=:evaluationAssignmentMatterId", Object[].class);

		query.setParameter("evaluationAssignmentMatterId", evaluationAssignmentMatterId);
		List<Object[]> matterTestStudentDataList = query.getResultList();

		List<MatterTestStudentInfo> matterTestStudentInfos  = new ArrayList<MatterTestStudentInfo>();
		for (Object[] matterTestStudentData : matterTestStudentDataList) {
			JSONObject matterTestStudentInfoJson = new JSONObject();
			matterTestStudentInfoJson.put("id", matterTestStudentData[0]);
			matterTestStudentInfoJson.put("matterName", matterTestStudentData[1]);
			matterTestStudentInfoJson.put("testName", matterTestStudentData[2]);
			matterTestStudentInfoJson.put("centerId", matterTestStudentData[3]);
			matterTestStudentInfoJson.put("evaluationEventMatterId", matterTestStudentData[4]);
			matterTestStudentInfoJson.put("evaluationAssignmentMatterId", matterTestStudentData[5]);
			matterTestStudentInfos.add(new MatterTestStudentInfo(matterTestStudentInfoJson));
		}

		return matterTestStudentInfos;
	}
	
	public List<MatterTestStudent> findByEvaluationAssignment(EvaluationAssignment evaluationAssignment) {
		TypedQuery<MatterTestStudent> query = this.em.createQuery("select ea from " + MatterTestStudent.class.getSimpleName() +
				" ea where ea.evaluationAssignmentMatter.evaluationAssignment.id = " + evaluationAssignment.getId(), MatterTestStudent.class);

		List<MatterTestStudent> matterTestStudents = query.getResultList();

		return matterTestStudents;
	}
	
	
	@Transactional
	public void deleteByEvaluationAssignmentMatter(EvaluationAssignmentMatter evaluationAssignmentMatter) {	
		Query query = this.em.createQuery("delete from " + MatterTestStudent.class.getSimpleName() + 
				" ea where ea.evaluationAssignmentMatter.id = " + evaluationAssignmentMatter.getId());

		query.executeUpdate();
	}

	public List<MatterTestStudent> findByEvaluationEventMatter(Long evaluationEventMatterId) {
		String query = "select eam from " + MatterTestStudent.class.getSimpleName() +
				" eam where eam.evaluationEventMatterTest.evaluationEventMatter.id = " + evaluationEventMatterId +
						" and eam.session is not null";

		TypedQuery<MatterTestStudent> typedQuery = this.em.createQuery(query, MatterTestStudent.class);

		List<MatterTestStudent> matters = typedQuery.getResultList();

		return matters;
	}
	
	public List<MatterTestStudent> findByEvaluationEventMatterTestWithSession(Long evaluationEventMatterTestId) {
		String query = "select mts from " + MatterTestStudent.class.getSimpleName() +
				" mts where mts.evaluationEventMatterTest.id = " + evaluationEventMatterTestId +
						" and mts.session is not null";

		TypedQuery<MatterTestStudent> typedQuery = this.em.createQuery(query, MatterTestStudent.class);

		List<MatterTestStudent> matters = typedQuery.getResultList();

		return matters;
	}

	public GenericInfo getTestsSchedulesInfo(Long evaluationEventId) {
		JSONObject testsSchedulesInfoJson = new JSONObject();

		TypedQuery<Object[]> query = this.em.createQuery(
				"SELECT COUNT(*), COUNT(DISTINCT mts.evaluationAssignmentMatter.evaluationAssignment.id) FROM " + MatterTestStudent.class.getSimpleName() +
				" mts WHERE mts.evaluationEventMatterTest.evaluationEventMatter.evaluationEvent.id=:evaluationEventId AND mts.studentEvaluation IS NULL", Object[].class);

		query.setParameter("evaluationEventId", evaluationEventId);
		List<Object[]> matterTestStudentDataList = query.getResultList();
		testsSchedulesInfoJson.put("testsWithoutSchedule", matterTestStudentDataList.get(0)[0]);
		testsSchedulesInfoJson.put("studentsWithTestsWithoutSchedule", matterTestStudentDataList.get(0)[1]);

		query = this.em.createQuery(
				"SELECT COUNT(*) FROM " + MatterTestStudent.class.getSimpleName() +
				" mts WHERE mts.evaluationEventMatterTest.evaluationEventMatter.evaluationEvent.id=:evaluationEventId AND mts.studentEvaluation IS NOT NULL", Object[].class);

		query.setParameter("evaluationEventId", evaluationEventId);
		matterTestStudentDataList = query.getResultList();
		testsSchedulesInfoJson.put("testsWithSchedule", matterTestStudentDataList.get(0));

		return new GenericInfo(testsSchedulesInfoJson);
	}

	public List<MatterTestStudent> findWithoutStudentEvaluationByEvaluationEvent(Long evaluationEventId) {
		TypedQuery<MatterTestStudent> query = this.em.createQuery(
				"SELECT mts FROM " + MatterTestStudent.class.getSimpleName() +
				" mts WHERE mts.evaluationEventMatterTest.evaluationEventMatter.evaluationEvent.id=:evaluationEventId AND mts.studentEvaluation IS NULL", MatterTestStudent.class);

		query.setParameter("evaluationEventId", evaluationEventId);
		List<MatterTestStudent> matterTestStudents = query.getResultList();

		return matterTestStudents;
	}

	public List<MatterTestStudent> findByEvaluationEventMatterTest(EvaluationEventMatterTest evaluationEventMatterTest, boolean published) {
		String query = "select eam from " + MatterTestStudent.class.getSimpleName() +
				" eam where eam.evaluationEventMatterTest.id = " + evaluationEventMatterTest.getId() +
						" and eam.session is not null";

		if(!published) {
			query += " and eam.session.state <> '" + Session.STATE_PUBLISHED + "'";
		}

		TypedQuery<MatterTestStudent> typedQuery = this.em.createQuery(query, MatterTestStudent.class);

		List<MatterTestStudent> matters = typedQuery.getResultList();

		return matters;
	}
	
	public List<MatterTestStudent> findByEvaluationEventMatterTest(EvaluationEventMatterTest evaluationEventMatterTest) {
		String query = "select mts from " + MatterTestStudent.class.getSimpleName() +
				" mts where mts.session is null " +
				"and mts.evaluationEventMatterTest.id = " + evaluationEventMatterTest.getId() + 
				" and mts.studentEvaluation is not null";

		TypedQuery<MatterTestStudent> typedQuery = this.em.createQuery(query, MatterTestStudent.class);

		List<MatterTestStudent> matters = typedQuery.getResultList();

		return matters;
	}
	
	public List<MatterTestStudentInfo> findInfoByEvaluationEventMatterTest(Long evaluationEventMatterTestId) {
		TypedQuery<Object[]> query = this.em.createQuery(
				"SELECT mts.id, mts.evaluationEventMatterTest.evaluationEventMatter.matter.name, mts.evaluationEventMatterTest.test.name," +
				" mts.evaluationAssignmentMatter.center.id, mts.evaluationEventMatterTest.evaluationEventMatter.id, mts.evaluationAssignmentMatter.id," +
				" mts.evaluationAssignmentMatter.evaluationAssignment.id, mts.studentEvaluation.id FROM " + MatterTestStudent.class.getSimpleName() + " mts" +
				" WHERE mts.session is null AND mts.evaluationEventMatterTest.id=:evaluationEventMatterTestId AND mts.studentEvaluation is not null", Object[].class);

		query.setParameter("evaluationEventMatterTestId", evaluationEventMatterTestId);
		List<Object[]> matterTestStudentDataList = query.getResultList();

		List<MatterTestStudentInfo> matterTestStudentInfos  = new ArrayList<MatterTestStudentInfo>();
		for (Object[] matterTestStudentData : matterTestStudentDataList) {
			JSONObject matterTestStudentInfoJson = new JSONObject();
			matterTestStudentInfoJson.put("id", matterTestStudentData[0]);
			matterTestStudentInfoJson.put("matterName", matterTestStudentData[1]);
			matterTestStudentInfoJson.put("testName", matterTestStudentData[2]);
			matterTestStudentInfoJson.put("centerId", matterTestStudentData[3]);
			matterTestStudentInfoJson.put("evaluationEventMatterId", matterTestStudentData[4]);
			matterTestStudentInfoJson.put("evaluationAssignmentMatterId", matterTestStudentData[5]);
			matterTestStudentInfoJson.put("evaluationAssignmentId", matterTestStudentData[6]);
			matterTestStudentInfoJson.put("studentEvaluationId", matterTestStudentData[7]);
			
			if (matterTestStudentData[7] != null) {
				TypedQuery<Object> query2 = this.em.createQuery("SELECT se.classroomTimeBlock.id FROM " + StudentEvaluation.class.getSimpleName() + " se WHERE se.id=:studentEvaluationId", Object.class);
				query2.setParameter("studentEvaluationId", matterTestStudentData[7]);
				List<Object> dataList = query2.getResultList();
				matterTestStudentInfoJson.put("classroomTimeBlockId", dataList.get(0));
			}
			
			matterTestStudentInfos.add(new MatterTestStudentInfo(matterTestStudentInfoJson));
		}

		return matterTestStudentInfos;
	}
	
	public List<MatterTestStudent> findByEvaluationEventMatterTestPublishedOrNotified(EvaluationEventMatterTest evaluationEventMatterTest) {
		String query = "select eam from " + MatterTestStudent.class.getSimpleName() +
				" eam where eam.evaluationEventMatterTest.id = " + evaluationEventMatterTest.getId() +
						" and eam.session is not null";

		
		query += " and (eam.session.state = '" + Session.STATE_PUBLISHED + "' OR eam.session.state = '" + Session.STATE_NOTIFIED + "')";
		

		TypedQuery<MatterTestStudent> typedQuery = this.em.createQuery(query, MatterTestStudent.class);

		List<MatterTestStudent> matters = typedQuery.getResultList();

		return matters;
	}

	@Override
	public List<MatterTestStudent> findWithStudentEvaluationByEvaluationAssignment(Long evaluationAssignmentId) {
	    // TODO Auto-generated method stub
	    return null;
	}

	@Override
	public List<Object[]> findEvaluationSchedule(Long evaluationAssignmentId) {
	    String strQuery = "select "
		    + "matter51_.name as matter_name, "
		    + "classroomt18_.startdate, "
		    + "classroom17_.name as classroom_name, classroom17_.location, "
		    + "evaluation2_.name as evaluation_center_name, "
		    + "addresses3_.type, addresses3_.name as street, addresses3_.number, addresses3_.city "
		    + "from MATTER_TEST_STUDENT mattertest0_ "
		    + "left join EVALUATION_ASSIGNMENT_MATTER evaluation1_ on mattertest0_.evaluationAssignmentMatter_id=evaluation1_.id "
		    + "left join EVALUATION_EVENT_MATTER evaluation40_ on evaluation1_.evaluationEventMatter_id=evaluation40_.id "
		    + "left join MATTER matter51_ on evaluation40_.matter_id=matter51_.id "
		    + "left join STUDENT_EVALUATION studenteva30_ on studenteva30_.evaluation_assignment_id=evaluation1_.evaluationassignment_id "
		    + "left join CLASSROOM_TIME_BLOCK classroomt18_ on classroomt18_.id=studenteva30_.classroom_time_block_id "
		    + "left join EVALUATION_EVENT_CLASSROOM evaluation27_ on evaluation27_.id=classroomt18_.evaluation_event_classroom_id "
		    + "left join CLASSROOM classroom17_ on evaluation27_.classroom_id=classroom17_.id "
		    + "left join EVALUATION_CENTER evaluation2_ on evaluation2_.id=classroom17_.evaluationcenter_id "
		    + "left join ADDRESS addresses3_ on evaluation2_.id=addresses3_.evaluationcenter_id "
		    + "where evaluation1_.evaluationAssignment_id = ?";
	    Query query = em.createNativeQuery(strQuery);
	    query.setParameter(1, evaluationAssignmentId);
	    return (List<Object[]>) query.getResultList();
	}
	
	
	public Long countByEvaluationEventMatterTestEvaluationType(Long eemId, Long etId) {
		TypedQuery<Long> query = this.em.createQuery( "select count(mts) from " + MatterTestStudent.class.getSimpleName() +
				" mts where mts.evaluationEventMatterTest.evaluationEventMatter.id = :eemId " +
						" and mts.evaluationEventMatterTest.test.evaluationType.id = :etId", Long.class);
	
		query.setParameter("eemId", eemId);
		query.setParameter("etId", etId);
		List<Long> dataList = query.getResultList();
		
		return dataList.get(0);
	}
	
	
	@Override
	public boolean checkEvaluationEventByParameters(Long evaluationEventId, Long userId, Long testId, Date testDate) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String testDateStr = sdf.format(testDate);
		
		//" AND '" + testDateStr + "' >= mts.studentEvaluation.classroomTimeBlock.startDate " +
		//" AND '" + testDateStr + "' < mts.studentEvaluation.classroomTimeBlock.endDate " +
		
		TypedQuery<MatterTestStudent> query = this.em.createQuery(
				"SELECT mts FROM " + MatterTestStudent.class.getSimpleName() + 
				" mts WHERE mts.studentEvaluation.evaluationAssignment.user.id=:userId " +
				" AND mts.evaluationEventMatterTest.test.id=:testId " +
				" AND '" + testDateStr + "' >= mts.evaluationEventMatterTest.evaluationEventMatter.evaluationEvent.startDate " +
				" AND '" + testDateStr + "' <mts.evaluationEventMatterTest.evaluationEventMatter.evaluationEvent.endDate " +
				" AND mts.evaluationEventMatterTest.evaluationEventMatter.evaluationEvent.id=:evaluationEventId" +
				"", MatterTestStudent.class);
		
		query.setParameter("userId", userId);
		query.setParameter("testId", testId);
		query.setParameter("evaluationEventId", evaluationEventId);
		
		List<MatterTestStudent> studentEvaluations = query.getResultList();
		if (studentEvaluations.size() > 0) {
			return true;
		}
		
		return false;
	}
	
	public List<MatterTestStudentInfo> findInfoWithoutStudentEvaluationByEvaluationEvent(Long evaluationEventId) {
		TypedQuery<Object[]> query = this.em.createQuery(
				"SELECT mts.id, mts.evaluationEventMatterTest.evaluationEventMatter.matter.name, mts.evaluationEventMatterTest.test.name," +
				" mts.evaluationAssignmentMatter.center.id, mts.evaluationEventMatterTest.evaluationEventMatter.id, mts.evaluationAssignmentMatter.id," +
				" mts.evaluationAssignmentMatter.evaluationAssignment.id FROM " + MatterTestStudent.class.getSimpleName() + " mts" +
				" WHERE mts.evaluationAssignmentMatter.evaluationAssignment.evaluationEvent.id=:evaluationEventId AND mts.studentEvaluation is null", Object[].class);

		query.setParameter("evaluationEventId", evaluationEventId);
		List<Object[]> matterTestStudentDataList = query.getResultList();

		List<MatterTestStudentInfo> matterTestStudentInfos  = new ArrayList<MatterTestStudentInfo>();
		for (Object[] matterTestStudentData : matterTestStudentDataList) {
			JSONObject matterTestStudentInfoJson = new JSONObject();
			matterTestStudentInfoJson.put("id", matterTestStudentData[0]);
			matterTestStudentInfoJson.put("matterName", matterTestStudentData[1]);
			matterTestStudentInfoJson.put("testName", matterTestStudentData[2]);
			matterTestStudentInfoJson.put("centerId", matterTestStudentData[3]);
			matterTestStudentInfoJson.put("evaluationEventMatterId", matterTestStudentData[4]);
			matterTestStudentInfoJson.put("evaluationAssignmentMatterId", matterTestStudentData[5]);
			matterTestStudentInfoJson.put("evaluationAssignmentId", matterTestStudentData[6]);
			matterTestStudentInfos.add(new MatterTestStudentInfo(matterTestStudentInfoJson));
		}

		return matterTestStudentInfos;
	}
	
	public List<MatterTestStudentInfo> findInfoWithStudentEvaluationByEvaluationEvent(Long evaluationEventId) {
		TypedQuery<Object[]> query = this.em.createQuery(
				"SELECT mts.id," +
				" mts.evaluationEventMatterTest.test.id, " +
				" mts.evaluationEventMatterTest.evaluationEventMatter.matter.code, " +
				" mts.evaluationAssignmentMatter.evaluationAssignment.user.id, " +
				" mts.studentEvaluation.classroomTimeBlock.timeBlock.startDate, " + 
				" mts.evaluationAssignmentMatter.evaluationAssignment.externalPassword " + 
				" FROM " + MatterTestStudent.class.getSimpleName() + " mts" +
				" WHERE mts.evaluationAssignmentMatter.evaluationAssignment.evaluationEvent.id=:evaluationEventId AND mts.studentEvaluation is not null", Object[].class);

		query.setParameter("evaluationEventId", evaluationEventId);
		List<Object[]> matterTestStudentDataList = query.getResultList();

		List<MatterTestStudentInfo> matterTestStudentInfos  = new ArrayList<MatterTestStudentInfo>();
		for (Object[] matterTestStudentData : matterTestStudentDataList) {
			JSONObject matterTestStudentInfoJson = new JSONObject();
			matterTestStudentInfoJson.put("id", matterTestStudentData[0]);
			matterTestStudentInfoJson.put("testId", matterTestStudentData[1]);
			matterTestStudentInfoJson.put("matterCode", matterTestStudentData[2]);
			matterTestStudentInfoJson.put("userId", matterTestStudentData[3]);
			matterTestStudentInfoJson.put("evaluationDate", matterTestStudentData[4]);
			matterTestStudentInfoJson.put("externalPassword", matterTestStudentData[5]);
			matterTestStudentInfos.add(new MatterTestStudentInfo(matterTestStudentInfoJson));
		}

		return matterTestStudentInfos;
	}
}
