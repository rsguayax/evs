package es.grammata.evaluation.evs.data.model.repository;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import es.grammata.evaluation.evs.data.model.base.BaseModelEntity;


@Entity
@Table(name = "ROLE")
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
public class Role extends BaseModelEntity<Long> {
	
	public static String ADMIN = "ADMIN";
	public static String EVENT_ADMIN = "EVENT_ADMIN";
	public static String TEACHER = "TEACHER";
	public static String STUDENT = "STUDENT";
	public static String STAFF = "STAFF";
	
	@NotNull(message="El campo no puede estar vacío")
	private String name;
	
	@NotNull(message="El campo no puede estar vacío")
	private String code;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
