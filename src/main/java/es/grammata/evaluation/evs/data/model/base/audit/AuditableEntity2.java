package es.grammata.evaluation.evs.data.model.base.audit;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


import es.grammata.evaluation.evs.data.model.base.BaseModelEntity;
import es.grammata.evaluation.evs.data.model.repository.User;

@EntityListeners({AuditingEntityListener.class})
@MappedSuperclass
public class AuditableEntity2<T extends Serializable> extends BaseModelEntity<T> {
	
	@CreatedDate
	@Temporal(TemporalType.TIMESTAMP)
	private Date created;
	
	//@CreatedBy
	private Integer created_By;

	@LastModifiedDate
	@Temporal(TemporalType.TIMESTAMP)
	private Date Modified;


	//@LastModifiedBy
	private Integer Modified_By;

	private boolean active;

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Integer getCreated_By() {
		return created_By;
	}

	public void setCreated_By(Integer created_By) {
		this.created_By = created_By;
	}

	public Date getModified() {
		return Modified;
	}

	public void setModified(Date modified) {
		Modified = modified;
	}

	public Integer getModified_By() {
		return Modified_By;
	}

	public void setModified_By(Integer modified_By) {
		Modified_By = modified_By;
	}

	public boolean getActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@Override
	public String toString() {
		return "AuditableEntity2 [created=" + created + ", created_By=" + created_By + ", Modified=" + Modified
				+ ", Modified_By=" + Modified_By + ", active=" + active + "]";
	}

	
	
}
