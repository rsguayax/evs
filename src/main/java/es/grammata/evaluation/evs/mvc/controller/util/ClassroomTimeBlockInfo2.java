package es.grammata.evaluation.evs.mvc.controller.util;

import java.util.Date;

public class ClassroomTimeBlockInfo2 {
	
	private Long id;
	private Date startDate;
	private Date endDate;
	private Long capId;
	private String capSsid;
	private String evaluationCenterCode;
	private String classroomName;
	
	public ClassroomTimeBlockInfo2() {
		
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Object startDate) {
		this.startDate = parseDate(startDate);
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Object endDate) {
		this.endDate = parseDate(endDate);
	}

	public Long getCapId() {
		return capId;
	}

	public void setCapId(Long capId) {
		this.capId = capId;
	}

	public String getCapSsid() {
		return capSsid;
	}

	public void setCapSsid(String capSsid) {
		this.capSsid = capSsid;
	}

	public String getEvaluationCenterCode() {
		return evaluationCenterCode;
	}

	public void setEvaluationCenterCode(String evaluationCenterCode) {
		this.evaluationCenterCode = evaluationCenterCode;
	}

	public String getClassroomName() {
		return classroomName;
	}

	public void setClassroomName(String classroomName) {
		this.classroomName = classroomName;
	}

	private Date parseDate(Object date) {
		if (date instanceof Date) {
			return (Date) date;
		} else if (date instanceof Long) {
			return new Date((Long) date);
		} else {
			return null;
		}
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClassroomTimeBlockInfo2 other = (ClassroomTimeBlockInfo2) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
