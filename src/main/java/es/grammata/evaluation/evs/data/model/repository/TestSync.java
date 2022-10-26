package es.grammata.evaluation.evs.data.model.repository;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import es.grammata.evaluation.evs.data.model.base.audit.AuditableEntity;

@Entity
@Table(name = "TEST_SYNC")
public class TestSync extends AuditableEntity<Long> {
	
	public final static String STATE_ERROR = "ERROR";
	
	public final static String STATE_START = "INICIADO";
	
	public final static String STATE_END = "TERMINADO";
	
	@ManyToOne(fetch = FetchType.LAZY)
	private EvaluationEvent evaluationEvent;
	
	private String state;
	
	@Column(columnDefinition="TEXT")
	private String description;

	public EvaluationEvent getEvaluationEvent() {
		return evaluationEvent;
	}

	public void setEvaluationEvent(EvaluationEvent evaluationEvent) {
		this.evaluationEvent = evaluationEvent;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
