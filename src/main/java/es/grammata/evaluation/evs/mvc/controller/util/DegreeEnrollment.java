package es.grammata.evaluation.evs.mvc.controller.util;

import java.util.List;

import es.grammata.evaluation.evs.data.model.repository.Degree;
import es.grammata.evaluation.evs.data.model.repository.Matter;

public class DegreeEnrollment {
	private List<Degree> degrees;
	private String academicPeriodAdd;
	private String modeAdd;
	public List<Degree> getDegrees() {
		return degrees;
	}
	public void setMatters(List<Degree> degrees) {
		this.degrees = degrees;
	}
	public String getAcademicPeriodAdd() {
		return academicPeriodAdd;
	}
	public void setAcademicPeriodAdd(String academicPeriodAdd) {
		this.academicPeriodAdd = academicPeriodAdd;
	}
	public String getModeAdd() {
		return modeAdd;
	}
	public void setModeAdd(String modeAdd) {
		this.modeAdd = modeAdd;
	}
	

}
