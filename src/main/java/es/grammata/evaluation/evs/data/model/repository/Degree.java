package es.grammata.evaluation.evs.data.model.repository;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.codehaus.jackson.annotate.JsonIgnore;

import es.grammata.evaluation.evs.data.model.base.audit.AuditableEntity2;

/**
 * Tabla para entidad Titulación. Ejemplo:Arquitectura o Artes
 * Visuales:
 * 
 * @author andres Hereda campos de auditoria de la entidad AuditableEntoty2, est
 *         a su vez hereda la primary key de la entidad BaseModelEntity
 *
 */

@Entity
@Table(name = "DEGREES")
public class Degree extends AuditableEntity2<Long>{
	
	@Size(min=1, max=256, message="El tamaño no es correcto")
	private String name;
	
	@Column(unique = true, nullable = false)
	private String utplId;
	
    private String code;
	
	private String mode;
    
    private String academicLevel;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@JsonIgnore
	public String getUtplId() {
		return utplId;
	}
	
	public void setUtplId(String utplId) {
		this.utplId = utplId;
	}
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getMode() {
		return mode;
	}
	
	public void setMode(String mode) {
		this.mode = mode;
	}
	
	public String getAcademicLevel() {
		return academicLevel;
	}
	
	public void setAcademicLevel(String academicLevel) {
		this.academicLevel = academicLevel;
	}
	
	@JsonIgnore
	public String getIdAsString() {
		return new Long(this.getId()).toString();
	}
}
