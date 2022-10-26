package es.grammata.evaluation.evs.mvc.controller.util;

import org.codehaus.jackson.annotate.JsonProperty;

public class DegreeF {
	
	
	@JsonProperty(value = "cut_off_grade")
	private Double CutOffGrade;
	
	@JsonProperty(value = "id")
	private Integer Id;
	
	@JsonProperty(value = "quota")
	private Double Quota;
	
	@JsonProperty(value = "degree")
	private DegreeFacahada2 degree;


	public Double getCutOffGrade() {
		return CutOffGrade;
	}
	public void setCutOffGrade(Double cutOffGrade) {
		CutOffGrade = cutOffGrade;
	}
	public Integer getId() {
		return Id;
	}
	public void setId(Integer id) {
		Id = id;
	}
	public Double getQuota() {
		return Quota;
	}
	public void setQuota(Double quota) {
		Quota = quota;
	}
	public DegreeFacahada2 getDegree() {
		return degree;
	}
	public void setDegree(DegreeFacahada2 degree) {
		this.degree = degree;
	}

}
