package es.grammata.evaluation.evs.data.model.repository;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonIgnore;

import es.grammata.evaluation.evs.data.model.base.BaseModelEntity;

/**
 * Tabla de relaci�n �Eventos de evaluaci�n - Titulaciones�:
 * 
 * @author andres
 *
 */
@Entity
@Table(name = "EVALUATION_EVENT_DEGREE_RELATIONS", uniqueConstraints = @UniqueConstraint(columnNames = {"evaluation_event_id", "degree_id" }))
public class EvaluationEventDegree extends BaseModelEntity<Long> {

	@ManyToOne(fetch = FetchType.LAZY )
	@JoinColumn(name = "evaluation_event_id") 
	private EvaluationEvent evaluationEvent;// -- clave for�nea

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "degree_id")
	private Degree degree;// -- clave for�nea

	@NotNull(message = "El campo no puede estar vacío")
	@Min(value = 0, message = "Debe ser mayor o igual a cero")
	@Max(value = 1, message = "Debe ser menor o igual a uno")
	private Double cut_off_grade;// -- no nula, de 0 a 1

	@NotNull(message = "El campo no puede estar vacío")
	@Min(value = 0, message = "Debe ser mayor o igual a cero")
	private Integer quota; // -- no nula, >= 0
	

	public EvaluationEventDegree() {
	}

	public EvaluationEventDegree(EvaluationEvent evaluationEvent, Degree degree) {

		this.setEvaluationEvent(evaluationEvent);
		this.setDegree(degree);
	}
	
	public EvaluationEventDegree(EvaluationEvent evaluationEvent, Degree degree, Double cut_off_grade, Integer quota) {
		
		this.setEvaluationEvent(evaluationEvent);
		this.setDegree(degree);
		this.setCut_off_grade(cut_off_grade);
		this.setQuota(quota);
	}


	@JsonIgnore
	public EvaluationEvent getEvaluationEvent() {
		return evaluationEvent;
	}

	public void setEvaluationEvent(EvaluationEvent evaluationEvent) {
		this.evaluationEvent = evaluationEvent;
	}

	public Degree getDegree() {
		return degree;
	}

	public void setDegree(Degree degree) {
		this.degree = degree;
	}

	public Double getCut_off_grade() {
		return cut_off_grade;
	}

	public void setCut_off_grade(Double cut_off_grade) {
		this.cut_off_grade = cut_off_grade;
	}

	public Integer getQuota() {
		return quota;
	}

	public void setQuota(Integer quota) {
		this.quota = quota;
	}

	@JsonIgnore
	public String getIdAsString() {
		return new Long(this.getId()).toString();
	}
}
