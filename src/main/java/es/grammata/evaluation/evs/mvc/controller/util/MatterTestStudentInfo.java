package es.grammata.evaluation.evs.mvc.controller.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;

import es.grammata.evaluation.evs.data.model.repository.MatterTestStudent;

public class MatterTestStudentInfo {
	
	private Long id;
	private Long userId;
	private Long studentEvaluationId;
	private Long evaluationEventMatterId;
	private Long evaluationAssignmentId;
	private Long evaluationAssignmentMatterId;
	private Long centerId;
	private Long classroomTimeBlockId;
	private String matterName;
	private String matterCode;
	private Long testId;
	private String testName;
	private String testEvaluationType;
	private Date evaluationDate;
	private String externalPassword;
	
	public MatterTestStudentInfo() {
		
	}
	
	public MatterTestStudentInfo(MatterTestStudent matterTestStudent) {
		id = matterTestStudent.getId();
		studentEvaluationId = matterTestStudent.getStudentEvaluation() != null ? matterTestStudent.getStudentEvaluation().getId() : null;
		matterName = matterTestStudent.getEvaluationEventMatterTest().getEvaluationEventMatter().getMatter().getName();
		testName = matterTestStudent.getEvaluationEventMatterTest().getTest().getName();
		testId = matterTestStudent.getEvaluationEventMatterTest().getTest().getId();
		centerId = matterTestStudent.getEvaluationAssignmentMatter().getCenter() != null ? matterTestStudent.getEvaluationAssignmentMatter().getCenter().getId() : null;
		evaluationEventMatterId = matterTestStudent.getEvaluationEventMatterTest().getEvaluationEventMatter().getId();
		evaluationAssignmentMatterId = matterTestStudent.getEvaluationAssignmentMatter().getId();
		evaluationAssignmentId = matterTestStudent.getEvaluationAssignmentMatter().getEvaluationAssignment().getId();
		classroomTimeBlockId = matterTestStudent.getStudentEvaluation() != null && matterTestStudent.getStudentEvaluation().getClassroomTimeBlock() != null ? matterTestStudent.getStudentEvaluation().getClassroomTimeBlock().getId() : null;
		testEvaluationType = matterTestStudent.getEvaluationEventMatterTest() != null && matterTestStudent.getEvaluationEventMatterTest().getTest().getEvaluationType() != null ? matterTestStudent.getEvaluationEventMatterTest().getTest().getEvaluationType().getCode() : null;
	}
	
	public MatterTestStudentInfo(JSONObject matterTestStudentInfoJson) {
		id = matterTestStudentInfoJson.has("id") ? matterTestStudentInfoJson.getLong("id") : null;
		//userId = matterTestStudentInfoJson.has("userId") ? matterTestStudentInfoJson.getLong("userId") : null;
		//código agregado el 3-oct-2022 18:00 por rsguayax
		System.out.print("antes de procesar userId");
		userId = matterTestStudentInfoJson.has("userId") && !matterTestStudentInfoJson.isNull("userId")? matterTestStudentInfoJson.getLong("userId") : null;
		matterCode = matterTestStudentInfoJson.has("matterCode") && !matterTestStudentInfoJson.isNull("matterCode")? matterTestStudentInfoJson.getString("matterCode") : null;
		externalPassword = matterTestStudentInfoJson.has("externalPassword") && !matterTestStudentInfoJson.isNull("externalPassword")? matterTestStudentInfoJson.getString("externalPassword") : null;
		evaluationDate = matterTestStudentInfoJson.has("evaluationDate") && !matterTestStudentInfoJson.isNull("evaluationDate")?  parseDate(matterTestStudentInfoJson.get("evaluationDate")) : null;
		System.out.print("después de procesar userId");
		//**** fin rsguayax
		studentEvaluationId = matterTestStudentInfoJson.has("studentEvaluationId") && !matterTestStudentInfoJson.isNull("studentEvaluationId") ? matterTestStudentInfoJson.getLong("studentEvaluationId") : null;
		matterName = matterTestStudentInfoJson.has("matterName") ? matterTestStudentInfoJson.getString("matterName") : null;
		//matterCode = matterTestStudentInfoJson.has("matterCode") ? matterTestStudentInfoJson.getString("matterCode") : null;
		testName = matterTestStudentInfoJson.has("testName") ? matterTestStudentInfoJson.getString("testName") : null;
		testId = matterTestStudentInfoJson.has("testId") ? matterTestStudentInfoJson.getLong("testId") : null;
		centerId = matterTestStudentInfoJson.has("centerId") ? matterTestStudentInfoJson.getLong("centerId") : null;
		evaluationEventMatterId = matterTestStudentInfoJson.has("evaluationEventMatterId") ? matterTestStudentInfoJson.getLong("evaluationEventMatterId") : null;
		evaluationAssignmentMatterId = matterTestStudentInfoJson.has("evaluationAssignmentMatterId") ? matterTestStudentInfoJson.getLong("evaluationAssignmentMatterId") : null;
		evaluationAssignmentId = matterTestStudentInfoJson.has("evaluationAssignmentId") && !matterTestStudentInfoJson.isNull("evaluationAssignmentId") ? matterTestStudentInfoJson.getLong("evaluationAssignmentId") : null;
		classroomTimeBlockId = matterTestStudentInfoJson.has("classroomTimeBlockId") && !matterTestStudentInfoJson.isNull("classroomTimeBlockId") ? matterTestStudentInfoJson.getLong("classroomTimeBlockId") : null;
		testEvaluationType = matterTestStudentInfoJson.has("testEvaluationType") && !matterTestStudentInfoJson.isNull("testEvaluationType") ? matterTestStudentInfoJson.getString("testEvaluationType") : null;
		//evaluationDate = matterTestStudentInfoJson.has("evaluationDate") ?  parseDate(matterTestStudentInfoJson.get("evaluationDate")) : null;
		//externalPassword = matterTestStudentInfoJson.has("externalPassword") ? matterTestStudentInfoJson.getString("externalPassword") : null;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getStudentEvaluationId() {
		return studentEvaluationId;
	}

	public void setStudentEvaluationId(Long studentEvaluationId) {
		this.studentEvaluationId = studentEvaluationId;
	}

	public Long getEvaluationAssignmentMatterId() {
		return evaluationAssignmentMatterId;
	}

	public void setEvaluationAssignmentMatterId(Long evaluationAssignmentMatterId) {
		this.evaluationAssignmentMatterId = evaluationAssignmentMatterId;
	}

	public Long getEvaluationAssignmentId() {
		return evaluationAssignmentId;
	}

	public void setEvaluationAssignmentId(Long evaluationAssignmentId) {
		this.evaluationAssignmentId = evaluationAssignmentId;
	}

	public String getMatterName() {
		return matterName;
	}

	public void setMatterName(String matterName) {
		this.matterName = matterName;
	}

	public String getMatterCode() {
		return matterCode;
	}

	public void setMatterCode(String matterCode) {
		this.matterCode = matterCode;
	}

	public Long getTestId() {
		return testId;
	}

	public void setTestId(Long testId) {
		this.testId = testId;
	}

	public String getTestName() {
		return testName;
	}

	public void setTestName(String testName) {
		this.testName = testName;
	}
	
	public Long getCenterId() {
		return centerId;
	}

	public void setCenterId(Long centerId) {
		this.centerId = centerId;
	}
	
	public Long getEvaluationEventMatterId() {
		return evaluationEventMatterId;
	}

	public void setEvaluationEventMatterId(Long evaluationEventMatterId) {
		this.evaluationEventMatterId = evaluationEventMatterId;
	}

	public Long getClassroomTimeBlockId() {
		return classroomTimeBlockId;
	}

	public void setClassroomTimeBlockId(Long classroomTimeBlockId) {
		this.classroomTimeBlockId = classroomTimeBlockId;
	}

	public String getTestEvaluationType() {
		return testEvaluationType;
	}

	public void setTestEvaluationType(String testEvaluationType) {
		this.testEvaluationType = testEvaluationType;
	}

	public Date getEvaluationDate() {
		return evaluationDate;
	}

	public void setEvaluationDate(Date evaluationDate) {
		this.evaluationDate = evaluationDate;
	}
	
	public String getExternalPassword() {
		return externalPassword;
	}

	public void setExternalPassword(String externalPassword) {
		this.externalPassword = externalPassword;
	}

	private Date parseDate(Object date) {
		Date d = null;
		if (date instanceof Date) {
			d = (Date) date;
		} else if (date instanceof Long) {
			d = new Date((Long) date);
		}
		
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			return sdf.parse(sdf.format(d));
		} catch (Exception e) {
			return d;
		}
	}
}
