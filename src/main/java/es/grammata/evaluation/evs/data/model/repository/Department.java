package es.grammata.evaluation.evs.data.model.repository;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import es.grammata.evaluation.evs.data.model.base.BaseModelEntity;


@Entity
@Table(name = "DEPARTMENT")
public class Department extends BaseModelEntity<Long> {
	
	@NotNull(message="El campo no puede estar vacío")
	@Size(min=2, max=512, message="El tamaño no es correcto")
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
