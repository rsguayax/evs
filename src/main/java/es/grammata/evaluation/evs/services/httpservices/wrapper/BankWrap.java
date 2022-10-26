package es.grammata.evaluation.evs.services.httpservices.wrapper;

import java.io.Serializable;
import java.util.Date;


public class BankWrap implements Serializable {
	
	//public static final String STATE_EDIT = "EDICIÓN";
	//public static final String STATE_END = "FINALIZADO";
	
	//private Integer isComplete;
	
	private String name;
	
	private Integer externalId;

	/*private Integer questionNumber;
	
	private Integer currentNumber;
	
	private Date createDate;
	
	private String state;*/

	

	/*public Integer getIsComplete() {
		return isComplete;
	}

	public void setIsComplete(Integer isComplete) {
		this.isComplete = isComplete;
	}*/

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Integer getExternalId() {
		return externalId;
	}

	public void setExternalId(Integer externalId) {
		this.externalId = externalId;
	}

	/*public Integer getQuestionNumber() {
		return questionNumber;
	}

	public void setQuestionNumber(Integer questionNumber) {
		this.questionNumber = questionNumber;
	}

	public Integer getCurrentNumber() {
		return currentNumber;
	}

	public void setCurrentNumber(Integer currentNumber) {
		this.currentNumber = currentNumber;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public static String getStateEdit() {
		return STATE_EDIT;
	}

	public static String getStateEnd() {
		return STATE_END;
	}*/
}
	