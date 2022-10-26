package es.grammata.evaluation.evs.mvc.controller.util;

import java.io.Serializable;


public class StudentExtendedMatterDocReport implements Serializable {
	
	public String matterName;
	
	public String matterAcademicPeriod;
	
	public String test;
	
	public String matterMode;

	public String career;
		
	

	public String getMatterName() {
		return matterName;
	}

	public void setMatterName(String matterName) {
		this.matterName = matterName;
	}

	public String getCareer() {
		return career;
	}

	public void setCareer(String career) {
		this.career = career;
	}

	public String getMatterAcademicPeriod() {
		return matterAcademicPeriod;
	}

	public void setMatterAcademicPeriod(String matterAcademicPeriod) {
		this.matterAcademicPeriod = matterAcademicPeriod;
	}

	public String getMatterMode() {
		return matterMode;
	}

	public void setMatterMode(String matterMode) {
		this.matterMode = matterMode;
	}

	public String getTest() {
		return test;
	}

	public void setTest(String test) {
		this.test = test;
	}
	
	
}
