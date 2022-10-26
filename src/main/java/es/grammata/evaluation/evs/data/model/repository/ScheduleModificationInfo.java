package es.grammata.evaluation.evs.data.model.repository;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonIgnore;

import es.grammata.evaluation.evs.data.model.base.BaseModelEntity;

@Entity
@Table(name = "SCHEDULE_MODIFICATION_INFO")
public class ScheduleModificationInfo extends BaseModelEntity<Long> {
	
	@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="schedule_modification_id")
	@NotNull(message="El campo no puede estar vacío")
	private ScheduleModification scheduleModification;
	
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="matter_test_student_id")
	@NotNull(message="El campo no puede estar vacío")
	private MatterTestStudent matterTestStudent;
	
	@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="old_classroom_time_block_id")
	@NotNull(message="El campo no puede estar vacío")
	private ClassroomTimeBlock oldClassroomTimeBlock;
	
	@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="new_classroom_time_block_id")
	@NotNull(message="El campo no puede estar vacío")
	private ClassroomTimeBlock newClassroomTimeBlock;
	
	public ScheduleModificationInfo() {
		
	}
	
	public ScheduleModificationInfo(ScheduleModification scheduleModification, MatterTestStudent matterTestStudent, ClassroomTimeBlock oldClassroomTimeBlock, ClassroomTimeBlock newClassroomTimeBlock) {
		this.scheduleModification = scheduleModification;
		this.matterTestStudent = matterTestStudent;
		this.oldClassroomTimeBlock = oldClassroomTimeBlock;
		this.newClassroomTimeBlock = newClassroomTimeBlock;
	}

	@JsonIgnore
	public ScheduleModification getScheduleModification() {
		return scheduleModification;
	}

	public void setScheduleModification(ScheduleModification scheduleModification) {
		this.scheduleModification = scheduleModification;
	}

	@JsonIgnore
	public MatterTestStudent getMatterTestStudent() {
		return matterTestStudent;
	}

	public void setMatterTestStudent(MatterTestStudent matterTestStudent) {
		this.matterTestStudent = matterTestStudent;
	}

	public ClassroomTimeBlock getOldClassroomTimeBlock() {
		return oldClassroomTimeBlock;
	}

	public void setOldClassroomTimeBlock(ClassroomTimeBlock oldClassroomTimeBlock) {
		this.oldClassroomTimeBlock = oldClassroomTimeBlock;
	}

	public ClassroomTimeBlock getNewClassroomTimeBlock() {
		return newClassroomTimeBlock;
	}

	public void setNewClassroomTimeBlock(ClassroomTimeBlock newClassroomTimeBlock) {
		this.newClassroomTimeBlock = newClassroomTimeBlock;
	}
}
