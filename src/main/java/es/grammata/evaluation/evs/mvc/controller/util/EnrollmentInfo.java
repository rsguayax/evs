package es.grammata.evaluation.evs.mvc.controller.util;

import org.json.JSONObject;

public class EnrollmentInfo {

	private Long id;
	private String userFirstName;
	private String userLastName;
	private String userFullName;
	private String userIdentification;
	private String userEmail;
	private String degreeName;
	private int priority;
	private String evaluationCenterName;
	
	public EnrollmentInfo(JSONObject enrollmentInfoJson) {
		id = enrollmentInfoJson.has("id") ? enrollmentInfoJson.getLong("id") : null;
		userFirstName = enrollmentInfoJson.has("userFirstName") ? enrollmentInfoJson.getString("userFirstName") : null;
		userLastName = enrollmentInfoJson.has("userLastName") ? enrollmentInfoJson.getString("userLastName") : null;
		userIdentification = enrollmentInfoJson.has("userIdentification") ? enrollmentInfoJson.getString("userIdentification") : null;
		userEmail = enrollmentInfoJson.has("userEmail") ? enrollmentInfoJson.getString("userEmail") : null;
		degreeName = enrollmentInfoJson.has("degreeName") ? enrollmentInfoJson.getString("degreeName") : null;
		evaluationCenterName = enrollmentInfoJson.has("evaluationCenterName") ? enrollmentInfoJson.getString("evaluationCenterName") : null;
		priority = enrollmentInfoJson.has("priority") ? enrollmentInfoJson.getInt("priority") : 0;
		
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

	public String getDegreeName() {
		return degreeName;
	}

	public void setDegreeName(String degreeName) {
		this.degreeName = degreeName;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String getEvaluationCenterName() {
		return evaluationCenterName;
	}

	public void setEvaluationCenterName(String evaluationCenterName) {
		this.evaluationCenterName = evaluationCenterName;
	}
}
