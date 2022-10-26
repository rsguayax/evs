package es.grammata.evaluation.evs.data.model.repository;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonIgnore;

import es.grammata.evaluation.evs.data.model.base.audit.AuditableEntity2;

/**
 * 
 * @author andres Hereda campos de auditoria de la entidad AuditableEntoty2, est
 *         a su vez hereda la primary key de la entidad BaseModelEntity
 *
 */
@Entity
@Table(name = "ENROLLMENTS")
public class Enrollment extends AuditableEntity2<Long> {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "evaluation_event_id") 
	private EvaluationEvent evaluationEvent;// -- clave for�nea

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id") 
	private User user;// -- clave for�nea

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "degree_id") 
	private Degree degree;// -- clave for�nea

	@NotNull(message = "El campo no puede estar vacío")
	@Min(value = 1, message = "Debe ser mayor o igual a cero")
	@Max(value = 2, message = "Debe ser menor o igual a uno")
	private Integer priority; // -- no nula, 1 o 2

	@Min(value = 0, message = "Debe ser mayor o igual a cero")
	@Max(value = 1, message = "Debe ser menor o igual a uno")
	private Double grade; // anulable, de 0 a 1
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "evaluation_center_id") 
	private EvaluationCenter evaluationCenter;

	@ManyToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
    @JoinTable(name="enrollment_matter", joinColumns=@JoinColumn(name="enrollment_id"), inverseJoinColumns=@JoinColumn(name="evaluation_assignment_matter_id"))
    private Set<EvaluationAssignmentMatter> evaluationAssignmentMatters;
	
	@JsonIgnore
	public EvaluationEvent getEvaluationEvent() {
		return evaluationEvent;
	}

	public void setEvaluationEvent(EvaluationEvent evaluationEvent) {
		this.evaluationEvent = evaluationEvent;
	}


	public User getUser() {
		return user;
	}

	
	public void setUser(User user) {
		this.user = user;
	}
	
	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public Double getGrade() {
		return grade;
	}

	public void setGrade(Double grade) {
		this.grade = grade;
	}

	public Degree getDegree() {
		return degree;
	}

	public void setDegree(Degree degree) {
		this.degree = degree;
	}

	public EvaluationCenter getEvaluationCenter() {
		return evaluationCenter;
	}

	public void setEvaluationCenter(EvaluationCenter evaluationCenter) {
		this.evaluationCenter = evaluationCenter;
	}

	@JsonIgnore
	public Set<EvaluationAssignmentMatter> getEvaluationAssignmentMatters() {
		return evaluationAssignmentMatters;
	}

	public void setEvaluationAssignmentMatters(Set<EvaluationAssignmentMatter> evaluationAssignmentMatters) {
		this.evaluationAssignmentMatters = evaluationAssignmentMatters;
	}
}
