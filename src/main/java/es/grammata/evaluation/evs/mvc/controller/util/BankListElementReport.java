package es.grammata.evaluation.evs.mvc.controller.util;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


public class BankListElementReport implements Serializable {
	
	public static final String COMPLETE = "LISTO";
	
	public static final String INCOMPLETE = "INCOMPLETO";
	
	public String bankName;

	public String matterName;
	
	public String matterCode;
	
	public String manager;
	
	public String departmentName;

	public Integer objQuestions;
	
	public Integer ensayQuestions;
	
	public Integer insertQuestions;
	
	public Integer activeQuestions;
	
	public Integer valActiveQuestions;
	
	public Integer noValActiveQuestions;
	
	public Integer inactiveQuestions;
	
	public Integer unsubscribeQuestions;
	
	public String state;
	
	public String testName;
	
	public String evaluationType;
	
	public Integer testQuestions;
	
	public String testState;
	
	public Float maxRate;
	
	public Integer testActiveQuestions;
	

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getMatterName() {
		return matterName;
	}

	public void setMatterName(String matterName) {
		this.matterName = matterName;
	}

	public String getManager() {
		return manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public Integer getObjQuestions() {
		return objQuestions;
	}

	public void setObjQuestions(Integer objQuestions) {
		this.objQuestions = objQuestions;
	}

	public Integer getEnsayQuestions() {
		return ensayQuestions;
	}

	public void setEnsayQuestions(Integer ensayQuestions) {
		this.ensayQuestions = ensayQuestions;
	}

	public Integer getInsertQuestions() {
		return insertQuestions;
	}

	public void setInsertQuestions(Integer insertQuestions) {
		this.insertQuestions = insertQuestions;
	}

	public Integer getActiveQuestions() {
		return activeQuestions;
	}

	public void setActiveQuestions(Integer activeQuestions) {
		this.activeQuestions = activeQuestions;
	}

	public Integer getValActiveQuestions() {
		return valActiveQuestions;
	}

	public void setValActiveQuestions(Integer valActiveQuestions) {
		this.valActiveQuestions = valActiveQuestions;
	}

	public Integer getNoValActiveQuestions() {
		return noValActiveQuestions;
	}

	public void setNoValActiveQuestions(Integer noValActiveQuestions) {
		this.noValActiveQuestions = noValActiveQuestions;
	}

	public Integer getInactiveQuestions() {
		return inactiveQuestions;
	}

	public void setInactiveQuestions(Integer inactiveQuestions) {
		this.inactiveQuestions = inactiveQuestions;
	}

	public Integer getUnsubscribeQuestions() {
		return unsubscribeQuestions;
	}

	public void setUnsubscribeQuestions(Integer unsubscribeQuestions) {
		this.unsubscribeQuestions = unsubscribeQuestions;
	}

	public String getState() {
		if(this.valActiveQuestions >= this.ensayQuestions + this.objQuestions) {
			return COMPLETE;
		} else {
			return INCOMPLETE;			
		}
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getTestName() {
		return testName;
	}

	public void setTestName(String testName) {
		this.testName = testName;
	}

	public String getEvaluationType() {
		return evaluationType;
	}

	public void setEvaluationType(String evaluationType) {
		this.evaluationType = evaluationType;
	}

	public Integer getTestQuestions() {
		return testQuestions;
	}

	public void setTestQuestions(Integer testQuestions) {
		this.testQuestions = testQuestions;
	}

	public String getTestState() {
		if(this.testActiveQuestions >= this.testQuestions) {
			return COMPLETE;
		} else {
			return INCOMPLETE;			
		}
	}

	public void setTestState(String testState) {
		this.testState = testState;
	}

	public Float getMaxRate() {
		return maxRate;
	}

	public void setMaxRate(Float maxRate) {
		this.maxRate = maxRate;
	}

	public Integer getTestActiveQuestions() {
		return testActiveQuestions;
	}

	public void setTestActiveQuestions(Integer testActiveQuestions) {
		this.testActiveQuestions = testActiveQuestions;
	}

	public String getMatterCode() {
		return matterCode;
	}

	public void setMatterCode(String matterCode) {
		this.matterCode = matterCode;
	}
}
