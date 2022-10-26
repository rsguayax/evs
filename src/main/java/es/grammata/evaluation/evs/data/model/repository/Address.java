package es.grammata.evaluation.evs.data.model.repository;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import es.grammata.evaluation.evs.data.model.base.BaseModelEntity;


@Entity
@Table(name = "ADDRESS")
public class Address extends BaseModelEntity<Long> {
	
	@ManyToOne
    @PrimaryKeyJoinColumn
    private EvaluationCenter evaluationCenter;
	
	@NotNull(message="El campo no puede estar vacío")
	private String type;
	
	@NotNull(message="El campo no puede estar vacío")
	private String name;
	
	@NotNull(message="El campo no puede estar vacío")
	private String city;
	
	@NotNull(message="El campo no puede estar vacío")
	private String number;
	
	@NotNull(message="El campo no puede estar vacío")
	private String phone;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public EvaluationCenter getEvaluationCenter() {
		return evaluationCenter;
	}

	public void setEvaluationCenter(EvaluationCenter evaluationCenter) {
		this.evaluationCenter = evaluationCenter;
	}


	
}
