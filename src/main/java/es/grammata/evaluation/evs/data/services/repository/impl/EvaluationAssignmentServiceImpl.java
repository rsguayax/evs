package es.grammata.evaluation.evs.data.services.repository.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;

import org.json.JSONObject;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import es.grammata.evaluation.evs.data.model.repository.EvaluationAssignment;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEvent;
import es.grammata.evaluation.evs.data.model.repository.MatterTestStudent;
import es.grammata.evaluation.evs.data.model.repository.User;
import es.grammata.evaluation.evs.data.services.base.impl.BaseServiceImpl;
import es.grammata.evaluation.evs.data.services.repository.EvaluationAssignmentService;
import es.grammata.evaluation.evs.mvc.controller.util.EvaluationAssignmentInfo;
import es.grammata.evaluation.evs.mvc.controller.util.StudentSearchResult;


@Repository
@Transactional(readOnly = true)
public class EvaluationAssignmentServiceImpl extends BaseServiceImpl<EvaluationAssignment> implements EvaluationAssignmentService {

	public List<User> findUsersByEvaluationEvent(EvaluationEvent evaluationEvent) {
		return findUsersByEvaluationEvent(evaluationEvent.getId());
	}

	public List<User> findUsersByEvaluationEvent(Long evaluationEventId) {
		TypedQuery<User> query = this.em.createQuery("select ea.user from " + EvaluationAssignment.class.getSimpleName() +
				" ea where ea.evaluationEvent.id = " + evaluationEventId, User.class);

		List<User> users = query.getResultList();

		return users;
	}

	public List<User> findUnnotifiedUsersByEvaluationEvent(Long evaluationEventId) {
	    String query = "select u.* from evaluation_assignment ea "
		    + "left join evs_user u on ea.user_id=u.id "
		    + "left join email_notifications en on u.id=en.user_id "
		    + "where en.user_id is null and ea.evaluationevent_id= " + evaluationEventId + " "
		    + "order by u.id";

	    @SuppressWarnings("unchecked")
	    List<User> users = (List<User>) em.createNativeQuery(query, User.class).getResultList();
	    return users;
	}

	public List<User> findUsersByEvaluationEvent(EvaluationEvent evaluationEvent, int page, int pageSize) {
		TypedQuery<User> query = this.em.createQuery("select ea.user from " + EvaluationAssignment.class.getSimpleName() +
				" ea where ea.evaluationEvent.id = " + evaluationEvent.getId() + " order by lastName, firstName asc", User.class);

		query.setFirstResult(page * pageSize);
		query.setMaxResults(pageSize);

		List<User> users = query.getResultList();

		return users;
	}

	public List<User> findUsersByEvaluationEvent(EvaluationEvent evaluationEvent, int page, int pageSize, String q) {
		TypedQuery<User> query = null;
		List<User> users = new ArrayList<User>();
		if(q != null && !q.equals("")) {
			query = this.em.createQuery("select ea.user from " + EvaluationAssignment.class.getSimpleName() +
					" ea where ea.evaluationEvent.id = " + evaluationEvent.getId() + " and " +
							" (upper(ea.user.lastName) like '%" + q.toUpperCase() + "%' " +
							" or upper(ea.user.firstName) like '%" + q.toUpperCase() + "%'" +
							" or upper(ea.user.username) like '%" + q.toUpperCase() + "%')" +
							" order by ea.user.lastName, ea.user.firstName asc", User.class);
			query.setFirstResult(page * pageSize);
			query.setMaxResults(pageSize);
			users = query.getResultList();
		} else {
			users = this.findUsersByEvaluationEvent(evaluationEvent, page, pageSize);
		}

		return users;
	}


	public EvaluationAssignment findByUnique(EvaluationEvent evaluationEvent, User user) {
		TypedQuery<EvaluationAssignment> query = this.em.createQuery("select ea from " + EvaluationAssignment.class.getSimpleName() +
				" ea where ea.user.id = " + user.getId() + " and ea.evaluationEvent.id = " + evaluationEvent.getId(), EvaluationAssignment.class);

		List<EvaluationAssignment> evaluationAssignments = query.getResultList();
		EvaluationAssignment evaluationAssignment = null;
		if(evaluationAssignments != null && evaluationAssignments.size() > 0) {
			evaluationAssignment = evaluationAssignments.get(0);
		}

		return evaluationAssignment;
	}

	public List<EvaluationAssignment> findByUsername(String username) {
		TypedQuery<EvaluationAssignment> query = this.em.createQuery("select ea from " + EvaluationAssignment.class.getSimpleName() +
				" ea where ea.user.username=:username", EvaluationAssignment.class);

		query.setParameter("username", username);

		List<EvaluationAssignment> evaluationAssignments = query.getResultList();

		return evaluationAssignments;
	}

	public List<EvaluationAssignmentInfo> findInfoByUsername(String username) {
		TypedQuery<Object[]> query = this.em.createQuery(
				"SELECT ea.id, ea.evaluationEvent.id, ea.user.id, ea.user.username, ea.user.firstName, ea.user.lastName, ea.evaluationEvent.name FROM " + EvaluationAssignment.class.getSimpleName() +
				" ea WHERE ea.user.username=:username", Object[].class);

		query.setParameter("username", username);
		List<Object[]> evaluationAssignmentDataList = query.getResultList();

		List<EvaluationAssignmentInfo> evaluationAssignmentInfos = new ArrayList<EvaluationAssignmentInfo>();
		for (Object[] evaluationAssignmentData : evaluationAssignmentDataList) {
			JSONObject evaluationAssignmentInfoJson = new JSONObject();
			evaluationAssignmentInfoJson.put("id", evaluationAssignmentData[0]);
			evaluationAssignmentInfoJson.put("evaluationEventId", evaluationAssignmentData[1]);
			evaluationAssignmentInfoJson.put("userId", evaluationAssignmentData[2]);
			evaluationAssignmentInfoJson.put("username", evaluationAssignmentData[3]);
			evaluationAssignmentInfoJson.put("firstName", evaluationAssignmentData[4]);
			evaluationAssignmentInfoJson.put("lastName", evaluationAssignmentData[5]);
			evaluationAssignmentInfoJson.put("evaluationEventName", evaluationAssignmentData[6]);

			evaluationAssignmentInfos.add(new EvaluationAssignmentInfo(evaluationAssignmentInfoJson));
		}

		return evaluationAssignmentInfos;
	}

	public EvaluationAssignment findByUsernameAndEvaluationEvent(String username, Long evaluationEventId) {
		TypedQuery<EvaluationAssignment> query = this.em.createQuery(
				"SELECT ea FROM " + EvaluationAssignment.class.getSimpleName() +
				" ea WHERE ea.user.username=:username AND ea.evaluationEvent.id=:evaluationEventId", EvaluationAssignment.class);

		query.setParameter("username", username);
		query.setParameter("evaluationEventId", evaluationEventId);
		List<EvaluationAssignment> evaluationAssignments = query.getResultList();

		if (!evaluationAssignments.isEmpty()) {
			return evaluationAssignments.get(0);
		}

		return null;
	}

	public EvaluationAssignmentInfo findInfoByUsernameAndEvaluationEvent(String username, Long evaluationEventId) {
		TypedQuery<Object[]> query = this.em.createQuery(
				"SELECT ea.id, ea.evaluationEvent.id, ea.user.id, ea.user.username, ea.user.firstName, ea.user.lastName, ea.evaluationEvent.name FROM " + EvaluationAssignment.class.getSimpleName() +
				" ea WHERE ea.user.username=:username AND ea.evaluationEvent.id=:evaluationEventId", Object[].class);

		query.setParameter("username", username);
		query.setParameter("evaluationEventId", evaluationEventId);
		List<Object[]> evaluationAssignmentDataList = query.getResultList();

		if (evaluationAssignmentDataList.size() > 0) {
			Object[] evaluationAssignmentData = evaluationAssignmentDataList.get(0);
			JSONObject evaluationAssignmentInfoJson = new JSONObject();
			evaluationAssignmentInfoJson.put("id", evaluationAssignmentData[0]);
			evaluationAssignmentInfoJson.put("evaluationEventId", evaluationAssignmentData[1]);
			evaluationAssignmentInfoJson.put("userId", evaluationAssignmentData[2]);
			evaluationAssignmentInfoJson.put("username", evaluationAssignmentData[3]);
			evaluationAssignmentInfoJson.put("firstName", evaluationAssignmentData[4]);
			evaluationAssignmentInfoJson.put("lastName", evaluationAssignmentData[5]);
			evaluationAssignmentInfoJson.put("evaluationEventName", evaluationAssignmentData[6]);

			return new EvaluationAssignmentInfo(evaluationAssignmentInfoJson);
		} else {
			return null;
		}
	}

	public List<StudentSearchResult> findStudents(EvaluationEvent evaluationEvent, String searchFor) {
		TypedQuery<Object[]> query = this.em.createQuery(
				"SELECT ea.id, ea.evaluationEvent.id, ea.user.id, ea.user.username, ea.user.firstName, ea.user.lastName FROM " + EvaluationAssignment.class.getSimpleName() +
				" ea, " + User.class.getSimpleName() + " u " +
				"WHERE ea.evaluationEvent.id= :evaluationEventId " +
				"AND ea.user.id=u.id " +
				"AND (lower(u.firstName) LIKE lower(:searchFor) OR lower(u.lastName) LIKE lower(:searchFor) OR lower(u.username) LIKE lower(:searchFor) OR lower(u.identification) LIKE lower(:searchFor))", Object[].class);

		query.setParameter("evaluationEventId", evaluationEvent.getId());
		query.setParameter("searchFor", "%" + searchFor + "%");
		List<Object[]> studentDataList = query.getResultList();

		List<StudentSearchResult> studentsSearchResults = new ArrayList<StudentSearchResult>();
		for (Object[] studentData : studentDataList) {
			JSONObject studentSearchResultJson = new JSONObject();
			studentSearchResultJson.put("evaluationAssignmentId", studentData[0]);
			studentSearchResultJson.put("evaluationEventId", studentData[1]);
			studentSearchResultJson.put("userId", studentData[2]);
			studentSearchResultJson.put("username", studentData[3]);
			studentSearchResultJson.put("firstName", studentData[4]);
			studentSearchResultJson.put("lastName", studentData[5]);

			studentsSearchResults.add(new StudentSearchResult(studentSearchResultJson));
		}

		return studentsSearchResults;
	}

	public long totalEvaluationEventStudents(Long evaluationEventId) {
		String query = "select count(*) from evaluation_assignment where evaluationevent_id = " + evaluationEventId;

		long total = ((Number)em.createNativeQuery(query).getSingleResult()).longValue();

		return total;
	}

	public long totalEvaluationEventStudents(Long evaluationEventId, String q) {
		/*String query = "select count(*) from evaluation_assignment ea inner join evs_user u on ea.user_id = u.id " +
				" where ea.evaluationevent_id = " + evaluationEventId +
				" and (u.firstname like '%" + q + "%' or u.lastname like '%" + q + "%')";
		
		long total = ((Number)em.createNativeQuery(query).getSingleResult()).longValue();
		*/
		
		TypedQuery<Long> query = this.em.createQuery(
				"SELECT COUNT(*) FROM  " + EvaluationAssignment.class.getSimpleName() +
				" ea where ea.evaluationEvent.id=:evaluationEventId and " +
				" (upper(ea.user.lastName) like '%" + q.toUpperCase() + "%' " +
				" or upper(ea.user.firstName) like '%" + q.toUpperCase() + "%'" +
				" or upper(ea.user.username) like '%" + q.toUpperCase() + "%')", Long.class);
		
		query.setParameter("evaluationEventId", evaluationEventId);
		List<Long> dataList = query.getResultList();

		return dataList.get(0);
	}


	public List<EvaluationAssignment> findWithTestsWithoutScheduleByEvaluationEvent(Long evaluationEventId) {
		TypedQuery<EvaluationAssignment> query = this.em.createQuery(
				"SELECT DISTINCT(ea) FROM " + EvaluationAssignment.class.getSimpleName() +  " ea, " + MatterTestStudent.class.getSimpleName() +
				" mts WHERE ea.evaluationEvent.id=:evaluationEventId AND mts.evaluationAssignmentMatter.evaluationAssignment.id=ea.id AND mts.studentEvaluation IS NULL ORDER BY ea.id ASC", EvaluationAssignment.class);

		query.setParameter("evaluationEventId", evaluationEventId);
		List<EvaluationAssignment> evaluationAssignments = query.getResultList();

		return evaluationAssignments;
	}


	public List<EvaluationAssignment> findByEvaluationEvent(Long evaluationEventId) {
		TypedQuery<EvaluationAssignment> query = this.em.createQuery(
				"SELECT ea FROM " + EvaluationAssignment.class.getSimpleName() +  " ea WHERE ea.evaluationEvent.id=:evaluationEventId", EvaluationAssignment.class);

		query.setParameter("evaluationEventId", evaluationEventId);
		List<EvaluationAssignment> evaluationAssignments = query.getResultList();

		return evaluationAssignments;
	}
	
	public EvaluationAssignmentInfo findInfoById(Long id) {
		TypedQuery<Object[]> query = this.em.createQuery(
				"SELECT ea.id, ea.evaluationEvent.id, ea.user.id, ea.user.username, ea.user.firstName, ea.user.lastName, ea.evaluationEvent.name, ea.externalPassword FROM " + EvaluationAssignment.class.getSimpleName() +
				" ea WHERE ea.id=:id", Object[].class);

		query.setParameter("id", id);
		List<Object[]> evaluationAssignmentDataList = query.getResultList();

		EvaluationAssignmentInfo evaluationAssignmentInfo = null;
		if (evaluationAssignmentDataList.size() > 0) {
			Object[] evaluationAssignmentData = evaluationAssignmentDataList.get(0);
			JSONObject evaluationAssignmentInfoJson = new JSONObject();
			
			evaluationAssignmentInfoJson.put("id", evaluationAssignmentData[0]);
			evaluationAssignmentInfoJson.put("evaluationEventId", evaluationAssignmentData[1]);
			evaluationAssignmentInfoJson.put("userId", evaluationAssignmentData[2]);
			evaluationAssignmentInfoJson.put("username", evaluationAssignmentData[3]);
			evaluationAssignmentInfoJson.put("firstName", evaluationAssignmentData[4]);
			evaluationAssignmentInfoJson.put("lastName", evaluationAssignmentData[5]);
			evaluationAssignmentInfoJson.put("evaluationEventName", evaluationAssignmentData[6]);
			evaluationAssignmentInfoJson.put("externalPassword", evaluationAssignmentData[7]);
			
			evaluationAssignmentInfo = new EvaluationAssignmentInfo(evaluationAssignmentInfoJson);
		}

		return evaluationAssignmentInfo;
	}
}
