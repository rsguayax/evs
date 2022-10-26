package es.grammata.evaluation.evs.data.model.repository;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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

import es.grammata.evaluation.evs.data.model.base.BaseModelEntity;
import es.grammata.evaluation.evs.serializers.DateTimeUtcSerializer;

@Entity
@Table(name = "TIME_BLOCK")
public class TimeBlock extends BaseModelEntity<Long> {
	
	@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="schedule_id")
	@NotNull(message="El campo no puede estar vacío")
    private Schedule schedule;
	
	@ManyToMany(fetch=FetchType.EAGER)  
    @JoinTable(name="time_block_student_type", joinColumns=@JoinColumn(name="time_block_id"), inverseJoinColumns=@JoinColumn(name="student_type_id"))  
	private Set<StudentType> studentTypes;
	
	@OneToMany(mappedBy="timeBlock", fetch=FetchType.EAGER)
    private Set<ClassroomTimeBlock> classroomTimeBlocks = new HashSet<ClassroomTimeBlock>();

	@NotNull(message="El campo no puede estar vacío")
	private Date startDate;

	@NotNull(message="El campo no puede estar vacío")
	private Date endDate;
	
	public Schedule getSchedule() {
		return schedule;
	}

	public void setSchedule(Schedule schedule) {
		this.schedule = schedule;
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

	public Set<StudentType> getStudentTypes() {
		return studentTypes;
	}

	public void setStudentTypes(Set<StudentType> studentTypes) {
		this.studentTypes = studentTypes;
	}

	public int getMinutes() {
		return (int) ((endDate.getTime() - startDate.getTime()) / 60000);
	}
	
	@JsonIgnore
	public Set<ClassroomTimeBlock> getClassroomTimeBlocks() {
		return classroomTimeBlocks;
	}
}
