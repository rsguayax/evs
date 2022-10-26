package es.grammata.evaluation.evs.data.model.repository;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.codehaus.jackson.annotate.JsonIgnore;

import es.grammata.evaluation.evs.data.model.base.BaseModelEntity;

@Entity
@Table(name = "SCHEDULE")
public class Schedule extends BaseModelEntity<Long> {

	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="evaluation_event_id")
	@NotNull(message="El campo no puede estar vacío")
	@JsonIgnore
    private EvaluationEvent evaluationEvent;
	
	@OneToMany(mappedBy="schedule", cascade=CascadeType.REMOVE, fetch=FetchType.EAGER)
    private Set<TimeBlock> timeBlocks = new HashSet<TimeBlock>();
	
	@OneToMany(mappedBy="schedule", fetch=FetchType.EAGER)
    private Set<EvaluationEventClassroom> evaluationEventClassrooms = new HashSet<EvaluationEventClassroom>();
	
	@NotNull(message="El campo no puede estar vacío")
	@Size(min=2, max=250, message="El tamaño no es correcto")
	private String name;
	
	@JsonIgnore
	public EvaluationEvent getEvaluationEvent() {
		return evaluationEvent;
	}

	public void setEvaluationEvent(EvaluationEvent evaluationEvent) {
		this.evaluationEvent = evaluationEvent;
	}
	
	@JsonIgnore
	public Set<TimeBlock> getTimeBlocks() {
		return timeBlocks;
	}

	public void setTimeBlocks(Set<TimeBlock> timeBlocks) {
		this.timeBlocks = timeBlocks;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@JsonIgnore
	public Set<EvaluationEventClassroom> getEvaluationEventClassrooms() {
		return evaluationEventClassrooms;
	}
}
