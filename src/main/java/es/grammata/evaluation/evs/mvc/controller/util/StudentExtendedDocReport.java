package es.grammata.evaluation.evs.mvc.controller.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class StudentExtendedDocReport implements Serializable {
	
	private String fullName;

	private String centerName;

	private String strDate;
	
	private String evaluationEventName;
	
	private String time;
	
	private String identification;
	
	private String userName;
	
	private String password;
	
	private String netName;
	
	private String netPassword;
	
	private String careers;
	
	private List<StudentExtendedMatterDocReport> extendedMatters = new ArrayList<StudentExtendedMatterDocReport>();

	
	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getCenterName() {
		return centerName;
	}

	public void setCenterName(String centerName) {
		this.centerName = centerName;
	}

	public String getEvaluationEventName() {
		return evaluationEventName;
	}

	public void setEvaluationEventName(String evaluationEventName) {
		this.evaluationEventName = evaluationEventName;
	}

	public String getStrDate() {
		return strDate;
	}

	public void setStrDate(String strDate) {
		this.strDate = strDate;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getIdentification() {
		return identification;
	}

	public void setIdentification(String identification) {
		this.identification = identification;
	}

	public List<StudentExtendedMatterDocReport> getExtendedMatters() {
		return extendedMatters;
	}

	public void setExtendedMatters(
			List<StudentExtendedMatterDocReport> extendedMatters) {
		this.extendedMatters = extendedMatters;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNetName() {
		return netName;
	}

	public void setNetName(String netName) {
		this.netName = netName;
	}

	public String getNetPassword() {
		return netPassword;
	}

	public void setNetPassword(String netPassword) {
		this.netPassword = netPassword;
	}

	public String getCareers() {
		return careers;
	}

	public void setCareers(String careers) {
		this.careers = careers;
	}
	
	

}
