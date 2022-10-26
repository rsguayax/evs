package es.grammata.evaluation.evs.data.model.repository;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import es.grammata.evaluation.evs.data.model.base.BaseModelEntity;

@Entity
@Table(name = "GLOBAL_CONFIGS")
public class GlobalConfig extends BaseModelEntity<Long> {
	
	@NotNull(message="El campo no puede estar vacío")
	private String name;
	
	@NotNull(message="El campo no puede estar vacío")
	private String type;
	
	@NotNull(message="El campo no puede estar vacío")
	private Date createDate;
	
	@NotNull(message="El campo no puede estar vacío")
	@Column(length = 5000)
	private String value;
	
	@NotNull(message="El campo no puede estar vacío")
	private String description;

	public GlobalConfig() {
		// TODO Auto-generated constructor stub
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
