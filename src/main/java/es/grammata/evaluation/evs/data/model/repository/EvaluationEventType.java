package es.grammata.evaluation.evs.data.model.repository;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import es.grammata.evaluation.evs.data.model.base.BaseModelEntity;

@Entity
@Table(name = "EVALUATION_EVENT_TYPES")
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
public class EvaluationEventType extends BaseModelEntity<Long> {

	@CreatedDate
	@Temporal(TemporalType.TIMESTAMP)
	private Date created;

	@LastModifiedDate
	@Temporal(TemporalType.TIMESTAMP)
	private Date Modified;

	private boolean active;

	@Size(min = 1, max = 256, message = "El tamaño no es correcto")
	private String name;

	private String description;

	@OneToMany(mappedBy = "evaluationEventTypes", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
	private Set<EvaluationEvent> evaluationEvents;

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getModified() {
		return Modified;
	}

	public void setModified(Date modified) {
		Modified = modified;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@JsonIgnore
	public Set<EvaluationEvent> getEvaluationEvents() {
		return evaluationEvents;
	}

	public void setEvaluationEvents(Set<EvaluationEvent> evaluationEvents) {
		this.evaluationEvents = evaluationEvents;
	}
	

	@Override
	public String toString() {
		return "EvaluationEventType [created=" + created + ", Modified=" + Modified + ", active=" + active + ", name="
				+ name + ", description=" + description  + "]";
	}
	
	@JsonIgnore
	public String getIdAsString() {
		return new Long(this.getId()).toString();
	}
}
