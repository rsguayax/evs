package es.grammata.evaluation.evs.services.restservices.server.wrapper;

public class TestListResponse {
	private Long testId;
	
	private String locker;
	
	private String name;
	
	private String matterCode;
	
	private String evaluationTypeCode;

	private Integer externalId;
	

	public Long getTestId() {
		return testId;
	}

	public void setTestId(Long testId) {
		this.testId = testId;
	}

	public String getLocker() {
		return locker;
	}

	public void setLocker(String locker) {
		this.locker = locker;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMatterCode() {
		return matterCode;
	}

	public void setMatterCode(String matterCode) {
		this.matterCode = matterCode;
	}

	public String getEvaluationTypeCode() {
		return evaluationTypeCode;
	}

	public void setEvaluationTypeCode(String evaluationTypeCode) {
		this.evaluationTypeCode = evaluationTypeCode;
	}

	public Integer getExternalId() {
		return externalId;
	}

	public void setExternalId(Integer externalId) {
		this.externalId = externalId;
	}	
	
}
