package es.grammata.evaluation.evs.data.model.repository;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonIgnore;

import es.grammata.evaluation.evs.data.model.base.BaseModelEntity;


@Entity
@Table(name = "CENTER")
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
public class Center extends BaseModelEntity<Long> {

	@ManyToOne
    @PrimaryKeyJoinColumn
    private EvaluationCenter evaluationCenter;

	private String type;
	
	@NotNull(message="El campo no puede estar vacío")
	private String name;
	
	private String sType;
	
	@NotNull(message="El campo no puede estar vacío")
	private String address;
	
	private String externalId;
	
	@NotNull(message="El campo no puede estar vacío")
	private String code;
	
	@NotNull(message="El campo no puede estar vacío")
	@Column(unique=true)
	private String uniqueCode;
	
	private String city;
	
	private boolean active;
	
	public EvaluationCenter getEvaluationCenter() {
		return evaluationCenter;
	}

	public void setEvaluationCenter(EvaluationCenter evaluationCenter) {
		this.evaluationCenter = evaluationCenter;
	}

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

	public String getSType() {
		return sType;
	}

	public void setSType(String sType) {
		this.sType = sType;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getUniqueCode() {
		return uniqueCode;
	}

	public void setUniqueCode(String uniqueCode) {
		this.uniqueCode = uniqueCode;
	}

	@JsonIgnore
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@JsonIgnore
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
}
