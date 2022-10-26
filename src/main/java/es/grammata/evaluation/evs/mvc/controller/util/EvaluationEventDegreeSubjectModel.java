package es.grammata.evaluation.evs.mvc.controller.util;

import org.codehaus.jackson.annotate.JsonProperty;

import es.grammata.evaluation.evs.data.model.repository.Matter;

public class EvaluationEventDegreeSubjectModel {

	@JsonProperty(value = "subject")
	private Matter subject;
	
	@JsonProperty(value = "weight")
	private Double weight;

	public Matter getSubject() {
		return subject;
	}

	public void setSubject(Matter subject) {
		this.subject = subject;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}
}
