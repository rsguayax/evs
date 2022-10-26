package es.grammata.evaluation.evs.services.restservices.server.wrapper;

public class ResultListResponse {
	private String matterCode;
	
	private String matterName;

	private String periodCode;

	private String periodName;

	private String studentFullName;

	private String studentIdentifier;
	
	private String locker;

	private Integer nSuccesses;
	
	private Integer nBlanks;
	
	private Integer nFails;
	
	private Float result;

	public String getMatterCode() {
		return matterCode;
	}

	public void setMatterCode(String matterCode) {
		this.matterCode = matterCode;
	}

	public String getMatterName() {
		return matterName;
	}

	public void setMatterName(String matterName) {
		this.matterName = matterName;
	}

	public String getPeriodCode() {
		return periodCode;
	}

	public void setPeriodCode(String periodCode) {
		this.periodCode = periodCode;
	}

	public String getPeriodName() {
		return periodName;
	}

	public void setPeriodName(String periodName) {
		this.periodName = periodName;
	}
	
	public String getStudentFullName() {
		return studentFullName;
	}

	public void setStudentFullName(String studentFullName) {
		this.studentFullName = studentFullName;
	}

	public String getStudentIdentifier() {
		return studentIdentifier;
	}

	public void setStudentIdentifier(String studentIdentifier) {
		this.studentIdentifier = studentIdentifier;
	}

	public String getLocker() {
		return locker;
	}

	public void setLocker(String locker) {
		this.locker = locker;
	}
	
	public Float getResult() {
		return result;
	}

	public void setResult(Float result) {
		this.result = result;
	}

	public Integer getnSuccesses() {
		return nSuccesses;
	}

	public void setnSuccesses(Integer nSuccesses) {
		this.nSuccesses = nSuccesses;
	}

	public Integer getnBlanks() {
		return nBlanks;
	}

	public void setnBlanks(Integer nBlanks) {
		this.nBlanks = nBlanks;
	}

	public Integer getnFails() {
		return nFails;
	}

	public void setnFails(Integer nFails) {
		this.nFails = nFails;
	}
}
