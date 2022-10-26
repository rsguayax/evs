package es.grammata.evaluation.evs.data.model.repository;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonIgnore;

import es.grammata.evaluation.evs.data.model.base.BaseModelEntity;

@Entity
@Table(name = "EVALUATION_TYPE")
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
public class EvaluationType extends BaseModelEntity<Long> {
	
	@NotNull(message="El campo no puede estar vacío")
	@Column(unique=true)
	private String code;
	
	@NotNull(message="El campo no puede estar vacío")
	private String codeWS;
	
	@NotNull(message="El campo no puede estar vacío")
	private String name;
	
	@Column(columnDefinition="TEXT")
	private String description;
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCodeWS() {
		return codeWS;
	}

	public void setCodeWS(String codeWS) {
		this.codeWS = codeWS;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@JsonIgnore
	public String getIdAsString() {
		return new Long(this.getId()).toString();
	}
	
}
