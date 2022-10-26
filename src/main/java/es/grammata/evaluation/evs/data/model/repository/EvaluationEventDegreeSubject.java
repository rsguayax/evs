package es.grammata.evaluation.evs.data.model.repository;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.codehaus.jackson.annotate.JsonIgnore;

import es.grammata.evaluation.evs.data.model.base.BaseModelEntity;

/**
 * Tabla de relaci�n �Eventos de evaluaci�n - Titulaciones - Tem�ticas�:
 * 
 * @author andres
 *
 */
@Entity
@Table(name = "EVALUATION_EVENT_DEGREE_SUBJECT_RELATIONS",
uniqueConstraints=@UniqueConstraint(columnNames = {"evaluation_event_id", "degree_id", "matter_id"}))

public class EvaluationEventDegreeSubject extends BaseModelEntity<Long>{
	@ManyToOne(fetch = FetchType.LAZY )
	@JoinColumn(name = "evaluation_event_id") 
	private EvaluationEvent evaluationEvent;// -- clave for�nea

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "degree_id")
	private Degree degree;// -- clave for�nea
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "matter_id")
	private Matter subject;// -- clave for�nea
	
	@Min(value=0, message="Debe ser mayor o igual a cero")
	@Max(value=1, message="Debe ser menor o igual a uno")  
	private Double weight; // -- peso de esta tem�tica, de 0 a 1

	
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
