package es.grammata.evaluation.evs.mvc.controller.util;

import java.util.List;

import es.grammata.evaluation.evs.data.model.repository.Matter;

public class SubjectEvent {
	
	private List<Matter> subjects;
	private String weightAdd;

	//@JsonProperty
	public List<Matter> getSubjects() {
		return subjects;
	}
	//@JsonIgnore
	public void setSubjects(List<Matter> subjects) {
		this.subjects = subjects;
	}
	public String getWeightAdd() {
		return weightAdd;
	}
	public void setWeightAdd(String weightAdd) {
		this.weightAdd = weightAdd;
	}
	
	

}
