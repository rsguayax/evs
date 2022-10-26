package es.grammata.evaluation.evs.data.model.repository;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonIgnore;

import es.grammata.evaluation.evs.data.model.base.BaseModelEntity;


@Entity
@Table(name = "ACADEMIC_PERIOD")
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
public class AcademicPeriod extends BaseModelEntity<Long> {
	
	@Column(unique=true)
	@NotNull(message="El campo no puede estar vacío")
	private String code;
	
	@NotNull(message="El campo no puede estar vacío")
	private String name;
	
	@Column(columnDefinition="TEXT")
	private String description;
	
	@NotNull(message="El campo no puede estar vacío")
	private Date startDate;
	
	@NotNull(message="El campo no puede estar vacío")
	private Date endDate;
	
	@Column(unique=true)
	@NotNull(message="El campo no puede estar vacío")
	private String externalId;

	private boolean active;
	
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
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	@JsonIgnore
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	@JsonIgnore
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	@JsonIgnore
	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}
	
	@JsonIgnore
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
}
