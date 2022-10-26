package es.grammata.evaluation.evs.data.model.repository;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import es.grammata.evaluation.evs.data.model.base.audit.AuditableEntity;
import es.grammata.evaluation.evs.serializers.DateTimeUtcSerializer;

@Entity
@Table(name = "CLASSROOM_TIME_BLOCK")
public class ClassroomTimeBlock extends AuditableEntity<Long> {

	@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="evaluation_event_classroom_id")
	@NotNull(message="El campo no puede estar vacío")
    private EvaluationEventClassroom evaluationEventClassroom;

	@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="available_state_id")
	@NotNull(message="El campo no puede estar vacío")
    private AvailableState availableState;

	@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="time_block_id")
    private TimeBlock timeBlock;

	@ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(name="classroom_time_block_student_type", joinColumns=@JoinColumn(name="classroom_time_block_id"), inverseJoinColumns=@JoinColumn(name="student_type_id"))
	private Set<StudentType> studentTypes;

	@ManyToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
    @JoinTable(name="evaluation_event_teacher_classroom_time_block", joinColumns=@JoinColumn(name="classroom_time_block_id"), inverseJoinColumns=@JoinColumn(name="evaluation_event_teacher_id"))
    private Set<EvaluationEventTeacher> evaluationEventTeachers;

	@OneToMany(mappedBy="classroomTimeBlock", fetch=FetchType.LAZY)
    private Set<StudentEvaluation> studentEvaluations = new HashSet<StudentEvaluation>();

	@NotNull(message="El campo no puede estar vacío")
	private Date startDate;

	@NotNull(message="El campo no puede estar vacío")
	private Date endDate;

	@NotNull(message="El campo no puede estar vacío")
	private Integer seats;

	public ClassroomTimeBlock() {

	}

	public ClassroomTimeBlock(EvaluationEventClassroom evaluationEventClassroom, TimeBlock timeBlock, AvailableState availableState) {
		this.evaluationEventClassroom = evaluationEventClassroom;
    	this.timeBlock = timeBlock;
    	this.availableState = availableState;
    	this.studentTypes = new HashSet<StudentType>(timeBlock.getStudentTypes());
    	this.startDate = timeBlock.getStartDate();
    	this.endDate = timeBlock.getEndDate();
    	this.seats = evaluationEventClassroom.getSeats();
    }

	@JsonIgnore
	public EvaluationEventClassroom getEvaluationEventClassroom() {
		return evaluationEventClassroom;
	}

	public void setEvaluationEventClassroom(
			EvaluationEventClassroom evaluationEventClassroom) {
		this.evaluationEventClassroom = evaluationEventClassroom;
	}

	public AvailableState getAvailableState() {
		return availableState;
	}

	public void setAvailableState(AvailableState availableState) {
		this.availableState = availableState;
	}

	public TimeBlock getTimeBlock() {
		return timeBlock;
	}

	public void setTimeBlock(TimeBlock timeBlock) {
		this.timeBlock = timeBlock;
	}

	@JsonIgnore
	public Set<EvaluationEventTeacher> getEvaluationEventTeachers() {
		return evaluationEventTeachers;
	}

	public void setEvaluationEventTeachers(
			Set<EvaluationEventTeacher> evaluationEventTeachers) {
		this.evaluationEventTeachers = evaluationEventTeachers;
	}

	@JsonIgnore
	public Set<StudentEvaluation> getStudentEvaluations() {
		return studentEvaluations;
	}

	@JsonIgnore
	public EvaluationCenter getEvaluationCenter() {
		return evaluationEventClassroom.getEvaluationCenter();
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

	public Integer getSeats() {
		return seats;
	}

	public void setSeats(Integer seats) {
		this.seats = seats;
	}

	public Integer getAvailableSeats() {
		return seats - getOccupiedSeats();
	}

	public Integer getOccupiedSeats() {
		return getStudentEvaluations().size();
	}

	public Set<StudentType> getStudentTypes() {
		return studentTypes;
	}

	public void setStudentTypes(Set<StudentType> studentTypes) {
		this.studentTypes = studentTypes;
	}

	public int getMinutes() {
		return (int) ((endDate.getTime() - startDate.getTime()) / 60000);
	}

	public void removeEvaluationEventTeacher(Long evaluationEventTeacherId) {
		for (EvaluationEventTeacher evaluationEventTeacher : evaluationEventTeachers) {
			if (evaluationEventTeacher.getId().equals(evaluationEventTeacherId)) {
				evaluationEventTeachers.remove(evaluationEventTeacher);
				break;
			}
		}
	}
}
