package es.grammata.evaluation.evs.data.model.repository;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import es.grammata.evaluation.evs.data.model.base.BaseModelEntity;
import es.grammata.evaluation.evs.serializers.DateTimeUtcSerializer;

@Entity
@Table(name = "SCHEDULE_MODIFICATION_DATE")
public class ScheduleModificationDate extends BaseModelEntity<Long> {
	
	@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="evaluation_event_id")
	@NotNull(message="El campo no puede estar vacío")
    private EvaluationEvent evaluationEvent;
	
	@NotNull(message="El campo no puede estar vacío")
	private Date startDate;

	@NotNull(message="El campo no puede estar vacío")
	private Date endDate;

	public ScheduleModificationDate() {
		
	}
	
	public ScheduleModificationDate(EvaluationEvent evaluationEvent, Date startDate, Date endDate) {
		this.evaluationEvent = evaluationEvent;
		this.startDate = startDate;
		this.endDate = endDate;
	}
	
	@JsonIgnore
	public EvaluationEvent getEvaluationEvent() {
		return evaluationEvent;
	}

	public void setEvaluationEvent(EvaluationEvent evaluationEvent) {
		this.evaluationEvent = evaluationEvent;
	}

	@JsonSerialize(using=DateTimeUtcSerializer.class)
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@JsonSerialize(using=DateTimeUtcSerializer.class)
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
}
