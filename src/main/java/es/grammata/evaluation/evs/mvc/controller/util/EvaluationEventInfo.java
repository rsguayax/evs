package es.grammata.evaluation.evs.mvc.controller.util;

import org.json.JSONObject;

public class EvaluationEventInfo {
	
	private Long id;
	private String name;
	private String code;
	
	public EvaluationEventInfo() {
		
	}
	
	public EvaluationEventInfo(JSONObject evaluationEventInfoJson) {
		id = evaluationEventInfoJson.has("id") ? evaluationEventInfoJson.getLong("id") : null;
		name = evaluationEventInfoJson.has("name") ? evaluationEventInfoJson.getString("name") : null;
		code = evaluationEventInfoJson.has("code") ? evaluationEventInfoJson.getString("code") : null;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
