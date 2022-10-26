package es.grammata.evaluation.evs.mvc.controller.util;

import java.util.List;

import es.grammata.evaluation.evs.data.model.repository.Matter;

public class MatterEvent {
	private List<Matter> matters;
	private String academicPeriodAdd;
	private String modeAdd;
	
	public List<Matter> getMatters() {
		return matters;
	}
	public void setMatters(List<Matter> matters) {
		this.matters = matters;
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
