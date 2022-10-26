package es.grammata.evaluation.evs.mvc.controller.util;

import org.json.JSONObject;

public class EvaluationAssignmentInfo {
	
	private Long id;
	private Long evaluationEventId;
	private Long userId;
	private String evaluationEventName;
	private String username;
	private String firstName;
	private String lastName;
	private String externalPassword;
	
	public EvaluationAssignmentInfo() {
		
	}
	
	public EvaluationAssignmentInfo(JSONObject evaluationAssignmentInfoJson) {
		id = evaluationAssignmentInfoJson.has("id") ? evaluationAssignmentInfoJson.getLong("id") : null;
		evaluationEventId = evaluationAssignmentInfoJson.has("evaluationEventId") ? evaluationAssignmentInfoJson.getLong("evaluationEventId") : null;
		userId = evaluationAssignmentInfoJson.has("userId") ? evaluationAssignmentInfoJson.getLong("userId") : null;
		evaluationEventName = evaluationAssignmentInfoJson.has("evaluationEventName") ? evaluationAssignmentInfoJson.getString("evaluationEventName") : null;
		username = evaluationAssignmentInfoJson.has("username") ? evaluationAssignmentInfoJson.getString("username") : null;
		firstName = evaluationAssignmentInfoJson.has("firstName") ? evaluationAssignmentInfoJson.getString("firstName") : null;
		lastName = evaluationAssignmentInfoJson.has("lastName") ? evaluationAssignmentInfoJson.getString("lastName") : null;
		externalPassword = evaluationAssignmentInfoJson.has("externalPassword") ? evaluationAssignmentInfoJson.getString("externalPassword") : null;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getEvaluationEventId() {
		return evaluationEventId;
	}

	public void setEvaluationEventId(Long evaluationEventId) {
		this.evaluationEventId = evaluationEventId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getEvaluationEventName() {
		return evaluationEventName;
	}

	public void setEvaluationEventName(String evaluationEventName) {
		this.evaluationEventName = evaluationEventName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getExternalPassword() {
		return externalPassword;
	}

	public void setExternalPassword(String externalPassword) {
		this.externalPassword = externalPassword;
	}

	public String getFullName() {
		String name = firstName;
		
		if (!lastName.isEmpty()) {
			name += " " + lastName;
		}
		
		return name;
	}
}
