package es.grammata.evaluation.evs.data.model.repository;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "STUDENT_TEST_SCHEDULE_LOG")
public class StudentTestScheduleLog extends Log {
	
	@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="evaluation_assignment_id")
	@NotNull(message="El campo no puede estar vacío")
	private EvaluationAssignment evaluationAssignment;
	
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="matter_test_student_id")
	@NotNull(message="El campo no puede estar vacío")
	private MatterTestStudent matterTestStudent;
	
	@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="classroom_time_block_id")
	private ClassroomTimeBlock classroomTimeBlock;
	
	public EvaluationAssignment getEvaluationAssignment() {
		return evaluationAssignment;
	}

	public void setEvaluationAssignment(EvaluationAssignment evaluationAssignment) {
		this.evaluationAssignment = evaluationAssignment;
	}

	public MatterTestStudent getMatterTestStudent() {
		return matterTestStudent;
	}

	public void setMatterTestStudent(MatterTestStudent matterTestStudent) {
		this.matterTestStudent = matterTestStudent;
	}

	public ClassroomTimeBlock getClassroomTimeBlock() {
		return classroomTimeBlock;
	}

	public void setClassroomTimeBlock(ClassroomTimeBlock classroomTimeBlock) {
		this.classroomTimeBlock = classroomTimeBlock;
	}
}
