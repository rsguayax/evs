package es.grammata.evaluation.evs.data.model.repository;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import es.grammata.evaluation.evs.data.model.base.BaseModelEntity;

@Entity
@Table(name = "PERMISSION")
public class Permission extends BaseModelEntity<Long> {

	@NotNull(message="El campo no puede estar vacío")
	private String permission;

	private String domainObject;
	
	private Long objectId;

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public String getDomainObject() {
		return domainObject;
	}

	public void setDomainObject(String domainObject) {
		this.domainObject = domainObject;
	}

	public Long getObjectId() {
		return objectId;
	}

	public void setId(Long objectId) {
		this.objectId = objectId;
	}
}
