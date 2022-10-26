package es.grammata.evaluation.evs.data.model.repository;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import es.grammata.evaluation.evs.data.model.base.BaseModelEntity;

@Entity
@Table(name = "STUDENT_TYPE")
public class StudentType extends BaseModelEntity<Long> {
	
	@Column(unique=true)
	@NotNull(message="El campo no puede estar vacío")
	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
