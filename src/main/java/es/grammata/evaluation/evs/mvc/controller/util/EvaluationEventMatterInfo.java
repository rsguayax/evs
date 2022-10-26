package es.grammata.evaluation.evs.mvc.controller.util;

import org.json.JSONObject;

public class EvaluationEventMatterInfo {

	private Long id;
	private Long matterId;
	private String matterName;
	private String matterCode;
	private String academicPeriodName;
	private String academicLevelName;
	private String modeName;
	private boolean hasBank;
	
	public EvaluationEventMatterInfo() {
		
	}
	
	public EvaluationEventMatterInfo(JSONObject evaluationEventMatterInfoJson) {
		id = evaluationEventMatterInfoJson.has("id") ? evaluationEventMatterInfoJson.getLong("id") : null;
		matterId = evaluationEventMatterInfoJson.has("matterId") ? evaluationEventMatterInfoJson.getLong("matterId") : null;
		matterName = evaluationEventMatterInfoJson.has("matterName") ? evaluationEventMatterInfoJson.getString("matterName") : null;
		matterCode = evaluationEventMatterInfoJson.has("matterCode") ? evaluationEventMatterInfoJson.getString("matterCode") : null;
		academicPeriodName = evaluationEventMatterInfoJson.has("academicPeriodName") ? evaluationEventMatterInfoJson.getString("academicPeriodName") : null;
		academicLevelName = evaluationEventMatterInfoJson.has("academicLevelName") ? evaluationEventMatterInfoJson.getString("academicLevelName") : null;
		modeName = evaluationEventMatterInfoJson.has("modeName") ? evaluationEventMatterInfoJson.getString("modeName") : null;
		hasBank = evaluationEventMatterInfoJson.has("hasBank") ? evaluationEventMatterInfoJson.getBoolean("hasBank") : false;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getMatterId() {
		return matterId;
	}

	public void setMatterId(Long matterId) {
		this.matterId = matterId;
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

	public String getAcademicPeriodName() {
		return academicPeriodName;
	}

	public void setAcademicPeriodName(String academicPeriodName) {
		this.academicPeriodName = academicPeriodName;
	}

	public String getAcademicLevelName() {
		return academicLevelName;
	}

	public void setAcademicLevelName(String academicLevelName) {
		this.academicLevelName = academicLevelName;
	}

	public String getModeName() {
		return modeName;
	}

	public void setModeName(String modeName) {
		this.modeName = modeName;
	}

	public boolean isHasBank() {
		return hasBank;
	}

	public void setHasBank(boolean hasBank) {
		this.hasBank = hasBank;
	}
}
