package es.grammata.evaluation.evs.data.model.repository;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
@Table(name = "GENERIC_SCHEDULE_LOG")
public class GenericScheduleLog extends Log {
	
	@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="evaluation_event_id")
	@NotNull(message="El campo no puede estar vacío")
	private EvaluationEvent evaluationEvent;

	@JsonIgnore
	public EvaluationEvent getEvaluationEvent() {
		return evaluationEvent;
	}

	public void setEvaluationEvent(EvaluationEvent evaluationEvent) {
		this.evaluationEvent = evaluationEvent;
	}
	
	
}
