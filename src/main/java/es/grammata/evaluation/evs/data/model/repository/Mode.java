package es.grammata.evaluation.evs.data.model.repository;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonIgnore;

import es.grammata.evaluation.evs.data.model.base.BaseModelEntity;

@Entity
@Table(name = "MODE")
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
public class Mode extends BaseModelEntity<Long> {
	
	@NotNull(message="El campo no puede estar vacío")
	@Column(unique=true)
	private String code;
	
	@NotNull(message="El campo no puede estar vacío")
	private String name;
	
	@Column(columnDefinition="TEXT")
	private String description;
	
	private Date createDate;
	
	private boolean deleted;
	
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
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@JsonIgnore
	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
}
