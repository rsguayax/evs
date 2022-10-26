package es.grammata.evaluation.evs.data.model.repository;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonIgnore;

import es.grammata.evaluation.evs.data.model.base.BaseModelEntity;

@Entity
@Table(name = "WEEK_DAY")
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
public class WeekDay extends BaseModelEntity<Long> {
	
	public static String LUNES = "LUN";
	public static String MARTES = "MAR";
	public static String MIERCOLES = "MIE";
	public static String JUEVES = "JUE";
	public static String VIERNES = "VIE";
	public static String SABADO = "SAB";
	public static String DOMINGO = "DOM";
	
	@NotNull(message="El campo no puede estar vacío")
	@Column(unique=true)
	private String code;
	
	@NotNull(message="El campo no puede estar vacío")
	private String name;
	

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@JsonIgnore
	public String getIdAsString() {
		return new Long(this.getId()).toString();
	}
}
