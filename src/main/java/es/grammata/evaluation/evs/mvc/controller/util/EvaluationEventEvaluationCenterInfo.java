package es.grammata.evaluation.evs.mvc.controller.util;

import org.json.JSONObject;

public class EvaluationEventEvaluationCenterInfo {
	
	private Long id;
	private Long evaluationEventId;
	private Long evaluationCenterId;
	private String evaluationEventName;
	private String evaluationCenterName;
	private String evaluationCenterCode;
	
	public EvaluationEventEvaluationCenterInfo() {
		
	}
	
	public EvaluationEventEvaluationCenterInfo(JSONObject eventCenterInfoJson) {
		id = eventCenterInfoJson.has("id") ? eventCenterInfoJson.getLong("id") : null;
		evaluationEventId = eventCenterInfoJson.has("evaluationEventId") ? eventCenterInfoJson.getLong("evaluationEventId") : null;
		evaluationCenterId = eventCenterInfoJson.has("evaluationCenterId") ? eventCenterInfoJson.getLong("evaluationCenterId") : null;
		evaluationEventName = eventCenterInfoJson.has("evaluationEventName") ? eventCenterInfoJson.getString("evaluationEventName") : null;
		evaluationCenterName = eventCenterInfoJson.has("evaluationCenterName") ? eventCenterInfoJson.getString("evaluationCenterName") : null;
		evaluationCenterCode = eventCenterInfoJson.has("evaluationCenterCode") ? eventCenterInfoJson.getString("evaluationCenterCode") : null;
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

	public Long getEvaluationCenterId() {
		return evaluationCenterId;
	}

	public void setEvaluationCenterId(Long evaluationCenterId) {
		this.evaluationCenterId = evaluationCenterId;
	}

	public String getEvaluationEventName() {
		return evaluationEventName;
	}

	public void setEvaluationEventName(String evaluationEventName) {
		this.evaluationEventName = evaluationEventName;
	}

	public String getEvaluationCenterName() {
		return evaluationCenterName;
	}

	public void setEvaluationCenterName(String evaluationCenterName) {
		this.evaluationCenterName = evaluationCenterName;
	}

	public String getEvaluationCenterCode() {
		return evaluationCenterCode;
	}

	public void setEvaluationCenterCode(String evaluationCenterCode) {
		this.evaluationCenterCode = evaluationCenterCode;
	}
}
