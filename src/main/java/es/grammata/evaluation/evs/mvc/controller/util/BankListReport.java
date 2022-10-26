package es.grammata.evaluation.evs.mvc.controller.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class BankListReport implements Serializable {
	
	private String evaluationEventName;
	private String departmentName;
	private Integer type;
	private String title;
	private List<BankListElementReport> elements;
	
	public BankListReport() {
		elements = new ArrayList<BankListElementReport>();
	}
	
	public String getEvaluationEventName() {
		return evaluationEventName;
	}
	public void setEvaluationEventName(String evaluationEventName) {
		this.evaluationEventName = evaluationEventName;
	}
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	public List<BankListElementReport> getElements() {
		return elements;
	}
	public void setElements(List<BankListElementReport> elements) {
		this.elements = elements;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	
}
