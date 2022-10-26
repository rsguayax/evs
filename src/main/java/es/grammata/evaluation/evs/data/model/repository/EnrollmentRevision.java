package es.grammata.evaluation.evs.data.model.repository;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.format.annotation.DateTimeFormat;

import es.grammata.evaluation.evs.data.model.base.BaseModelEntity;
/**
 * Tabla para la entidad 'Revisión de las inscripciones'
 * @author andres
 *
 */
@Entity
@Table(name = "ENROLLMENT_REVISIONS")
public class EnrollmentRevision extends BaseModelEntity<Long> {

	
	@NotNull(message="El campo no puede estar vacío")
	@DateTimeFormat(pattern="yyyy/MM-dd HH:mm")
	private Date created;// , -- no nula, fecha
	
	@NotNull(message="El campo no puede estar vacío")
	//@DateTimeFormat(pattern="yyyy/MM-dd HH:mm")
	private int  created_by;// , -- no nula, fecha
	
	@ManyToOne(fetch=FetchType.EAGER)
	private Enrollment enrollment;// -- clave foránea
	
	@NotNull(message="El campo no puede estar vacío")
	private int status;// , -- no nula, de un enumerado de estados ojo revisar
	
	@NotNull(message="El campo no puede estar vacío")	
	private int revision;// -- no nula, secuencia autoincremental

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public int getCreated_by() {
		return created_by;
	}

	public void setCreated_by(int created_by) {
		this.created_by = created_by;
	}

	@JsonIgnore
	public Enrollment getEnrollment() {
		return enrollment;
	}

	public void setEnrollment(Enrollment enrollment) {
		this.enrollment = enrollment;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getRevision() {
		return revision;
	}

	public void setRevision(int revision) {
		this.revision = revision;
	}
}
