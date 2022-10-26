package es.grammata.evaluation.evs.data.model.repository;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import es.grammata.evaluation.evs.data.model.base.BaseModelEntity;

/**
 * Tabla para la entidad 'Calificación de admisión':
 * 
 * @author andres
 *
 */
@Entity
@Table(name ="ADMISSION_GRADES", uniqueConstraints=@UniqueConstraint(columnNames = {"evaluation_event_id", "user_id", "matter_id"}))
public class AdmissionGrade extends BaseModelEntity<Long> {
	
	@NotNull(message = "El campo no puede estar vacío")
	@DateTimeFormat(pattern="yyyy/MM-dd HH:mm")
	private Date created;// , -- no nula, fecha
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "evaluation_event_id") 
	private EvaluationEvent evaluation_event;// -- clave foránea

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id") 
	private User user;// -- clave foránea

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "matter_id") 
	private Matter matter;// , -- clave foránea
	
	@NotNull(message = "El campo no puede estar vacío")
	@Min(value=0, message="Debe ser mayor o igual a cero")
	@Max(value=1, message="Debe ser menor o igual a uno")  
	private Double grade;// -- no nula, de 0 a 1

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public EvaluationEvent getEvaluationEvent() {
		return evaluation_event;
	}

	public void setEvaluationEvent(EvaluationEvent evaluation_event) {
		this.evaluation_event = evaluation_event;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Matter getMatter() {
		return matter;
	}

	public void setMatter(Matter matter) {
		this.matter = matter;
	}

	public double getGrade() {
		return grade;
	}

	public void setGrade(double grade) {
		this.grade = grade;
	}	
}