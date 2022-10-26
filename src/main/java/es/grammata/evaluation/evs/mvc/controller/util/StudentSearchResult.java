package es.grammata.evaluation.evs.mvc.controller.util;

import org.json.JSONObject;

public class StudentSearchResult {
	
	private Long evaluationAssignmentId;
	private Long evaluationEventId;
	private Long userId;
	private String username;
	private String firstName;
	private String lastName;
	private boolean includedInTimeBlock;
	
	public StudentSearchResult() {
		
	}
	
	public StudentSearchResult(JSONObject studentSearchResultJson) {
		evaluationAssignmentId = studentSearchResultJson.has("evaluationAssignmentId") ? studentSearchResultJson.getLong("evaluationAssignmentId") : null;
		evaluationEventId = studentSearchResultJson.has("evaluationEventId") ? studentSearchResultJson.getLong("evaluationEventId") : null;
		userId = studentSearchResultJson.has("userId") ? studentSearchResultJson.getLong("userId") : null;
		username = studentSearchResultJson.has("username") ? studentSearchResultJson.getString("username") : null;
		firstName = studentSearchResultJson.has("firstName") ? studentSearchResultJson.getString("firstName") : null;
		lastName = studentSearchResultJson.has("lastName") ? studentSearchResultJson.getString("lastName") : null;
		includedInTimeBlock = false;
	}

	public Long getEvaluationAssignmentId() {
		return evaluationAssignmentId;
	}

	public void setEvaluationAssignmentId(Long evaluationAssignmentId) {
		this.evaluationAssignmentId = evaluationAssignmentId;
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

	public boolean isIncludedInTimeBlock() {
		return includedInTimeBlock;
	}

	public void setIncludedInTimeBlock(boolean includedInTimeBlock) {
		this.includedInTimeBlock = includedInTimeBlock;
	}
	
	public String getFullName() {
		String name = firstName;
		
		if (!lastName.isEmpty()) {
			name += " " + lastName;
		}
		
		return name;
	}
}
