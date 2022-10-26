package es.grammata.evaluation.evs.mvc.controller.util;

import org.json.JSONObject;

import es.grammata.evaluation.evs.util.EnrollmentUtils;

public class EnrollmentRevisionInfo {

	private Long id;
	private Long enrollmentId;
	private String userFirstName;
	private String userLastName;
	private String userFullName;
	private String userIdentification;
	private String userEmail;
	private Long degreeId;
	private String degreeName;
	private String evaluationCenterName;
	private int priority;
	private int revision;
	private int status;
	private String statusString;
	private double grade;
	
	public EnrollmentRevisionInfo(JSONObject enrollmentRevisionInfoJson) {
		id = enrollmentRevisionInfoJson.has("id") ? enrollmentRevisionInfoJson.getLong("id") : null;
		enrollmentId = enrollmentRevisionInfoJson.has("enrollmentId") ? enrollmentRevisionInfoJson.getLong("enrollmentId") : null;
		userFirstName = enrollmentRevisionInfoJson.has("userFirstName") ? enrollmentRevisionInfoJson.getString("userFirstName") : null;
		userLastName = enrollmentRevisionInfoJson.has("userLastName") ? enrollmentRevisionInfoJson.getString("userLastName") : null;
		userIdentification = enrollmentRevisionInfoJson.has("userIdentification") ? enrollmentRevisionInfoJson.getString("userIdentification") : null;
		userEmail = enrollmentRevisionInfoJson.has("userEmail") ? enrollmentRevisionInfoJson.getString("userEmail") : null;
		degreeId = enrollmentRevisionInfoJson.has("degreeId") ? enrollmentRevisionInfoJson.getLong("degreeId") : null;
		degreeName = enrollmentRevisionInfoJson.has("degreeName") ? enrollmentRevisionInfoJson.getString("degreeName") : null;
		evaluationCenterName = enrollmentRevisionInfoJson.has("evaluationCenterName") ? enrollmentRevisionInfoJson.getString("evaluationCenterName") : null;
		priority = enrollmentRevisionInfoJson.has("priority") ? enrollmentRevisionInfoJson.getInt("priority") : 0;
		revision = enrollmentRevisionInfoJson.has("revision") ? enrollmentRevisionInfoJson.getInt("revision") : 0;
		status = enrollmentRevisionInfoJson.has("status") ? enrollmentRevisionInfoJson.getInt("status") : 0;
		grade = enrollmentRevisionInfoJson.has("grade") ? enrollmentRevisionInfoJson.getDouble("grade") : 0;
		
		grade = Math.round(grade * 100.0) / 100.0;
		statusString = EnrollmentUtils.getStatusString(status);
		
		userFullName = userFirstName;
		if (userLastName != null && userLastName.length() > 0) {
			userFullName = userFullName + " " + userLastName;
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getEnrollmentId() {
		return enrollmentId;
	}

	public void setEnrollmentId(Long enrollmentId) {
		this.enrollmentId = enrollmentId;
	}

	public String getUserFirstName() {
		return userFirstName;
	}

	public void setUserFirstName(String userFirstName) {
		this.userFirstName = userFirstName;
	}

	public String getUserLastName() {
		return userLastName;
	}

	public void setUserLastName(String userLastName) {
		this.userLastName = userLastName;
	}

	public String getUserFullName() {
		return userFullName;
	}

	public void setUserFullName(String userFullName) {
		this.userFullName = userFullName;
	}

	public String getUserIdentification() {
		return userIdentification;
	}

	public void setUserIdentification(String userIdentification) {
		this.userIdentification = userIdentification;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public Long getDegreeId() {
		return degreeId;
	}

	public void setDegreeId(Long degreeId) {
		this.degreeId = degreeId;
	}

	public String getDegreeName() {
		return degreeName;
	}

	public void setDegreeName(String degreeName) {
		this.degreeName = degreeName;
	}

	public String getEvaluationCenterName() {
		return evaluationCenterName;
	}

	public void setEvaluationCenterName(String evaluationCenterName) {
		this.evaluationCenterName = evaluationCenterName;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public int getRevision() {
		return revision;
	}

	public void setRevision(int revision) {
		this.revision = revision;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getStatusString() {
		return statusString;
	}

	public void setStatusString(String statusString) {
		this.statusString = statusString;
	}

	public double getGrade() {
		return grade;
	}

	public void setGrade(double grade) {
		this.grade = grade;
	}
}
