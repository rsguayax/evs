package es.grammata.evaluation.evs.data.model.repository;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import es.grammata.evaluation.evs.data.model.base.BaseModelEntity;

@Entity
@Table(name = "AVAILABLE_STATE")
public class AvailableState extends BaseModelEntity<Long> {
	
	public static String AVAILABLE = "AVAILABLE";
	public static String FULL = "FULL";

	@NotNull(message="El campo no puede estar vacío")
	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
