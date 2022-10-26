package es.grammata.evaluation.evs.mvc.controller.util;

import java.util.Date;

public class DayTest {
	private Date day;
	private Long testId;
	
	public DayTest(MatterTestStudentInfo mts) {
		this.day = mts.getEvaluationDate();
		this.testId = mts.getTestId();
	}

	public Date getDay() {
		return day;
	}
	
	public void setDay(Date day) {
		this.day = day;
	}

	public Long getTestId() {
		return testId;
	}

	public void setTestId(Long testId) {
		this.testId = testId;
	}
	
	@Override
	public int hashCode() {
		return day.hashCode() * testId.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DayTest other = (DayTest) obj;
		if (this.testId.equals(other.getTestId()) && this.getDay().equals(other.getDay()))
			return true;
		return false;
	}
}
