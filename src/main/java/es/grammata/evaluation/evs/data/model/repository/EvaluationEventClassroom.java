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

import org.codehaus.jackson.annotate.JsonIgnore;

import es.grammata.evaluation.evs.data.model.base.BaseModelEntity;

@Entity
@Table(name = "EVALUATION_EVENT_CLASSROOM")
public class EvaluationEventClassroom extends BaseModelEntity<Long> {
	
	@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="evaluation_event_evaluation_center_id")
	@NotNull(message="El campo no puede estar vacío")
    private EvaluationEventEvaluationCenter evaluationEventEvaluationCenter;

	@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="classroom_id")
	@NotNull(message="El campo no puede estar vacío")
    private Classroom classroom;
	
	@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="schedule_id")
	@NotNull(message="El campo no puede estar vacío")
    private Schedule schedule;
	
	@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="cap_id")
    private Cap cap;
	
	@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="net_id")
    private Net net;
    
    @NotNull(message="El campo no puede estar vacío")
	private Integer seats;
    
    @OneToMany(mappedBy="evaluationEventClassroom", cascade=CascadeType.REMOVE, fetch=FetchType.EAGER)
    private Set<ClassroomTimeBlock> classroomTimeBlocks = new HashSet<ClassroomTimeBlock>();
    
    @JsonIgnore
    public EvaluationEventEvaluationCenter getEvaluationEventEvaluationCenter() {
		return evaluationEventEvaluationCenter;
	}

	public void setEvaluationEventEvaluationCenter(
			EvaluationEventEvaluationCenter evaluationEventEvaluationCenter) {
		this.evaluationEventEvaluationCenter = evaluationEventEvaluationCenter;
	}

	public Classroom getClassroom() {
		return classroom;
	}

	public void setClassroom(Classroom classroom) {
		this.classroom = classroom;
	}
	
	public Schedule getSchedule() {
		return schedule;
	}

	public void setSchedule(Schedule schedule) {
		this.schedule = schedule;
	}

	public Cap getCap() {
		return cap;
	}

	public void setCap(Cap cap) {
		this.cap = cap;
	}

	public Integer getSeats() {
		return seats;
	}

	public void setSeats(Integer seats) {
		this.seats = seats;
	}
	
	public Net getNet() {
		return net;
	}

	public void setNet(Net net) {
		this.net = net;
	}

	@JsonIgnore
	public Set<ClassroomTimeBlock> getClassroomTimeBlocks() {
		return classroomTimeBlocks;
	}

	public void setClassroomTimeBlocks(Set<ClassroomTimeBlock> classroomTimeBlocks) {
		this.classroomTimeBlocks = classroomTimeBlocks;
	}

	@JsonIgnore
	public EvaluationCenter getEvaluationCenter() {
		return evaluationEventEvaluationCenter.getEvaluationCenter();
	}

	// TODO: modificar para obtener el ID ya que es LAZY
	@JsonIgnore
	public EvaluationEvent getEvaluationEvent() {
		return evaluationEventEvaluationCenter.getEvaluationEvent();
	}
}
