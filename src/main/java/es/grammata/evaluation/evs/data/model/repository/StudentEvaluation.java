package es.grammata.evaluation.evs.data.model.repository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;

import es.grammata.evaluation.evs.data.model.base.audit.AuditableEntity;
import es.grammata.evaluation.evs.mvc.controller.util.MatterTestStudentInfo;

@Entity
@Table(name = "STUDENT_EVALUATION")
public class StudentEvaluation extends AuditableEntity<Long> {
	
	@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="evaluation_assignment_id")
    private EvaluationAssignment evaluationAssignment;
	
	@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="classroom_time_block_id")
    private ClassroomTimeBlock classroomTimeBlock;
	
	@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="net_id")
    private Net net;
	
	@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="server_id")
    private Server server;
	
	@OneToMany(mappedBy="studentEvaluation", fetch=FetchType.LAZY)
    private Set<MatterTestStudent> matterTests = new HashSet<MatterTestStudent>();

	public EvaluationAssignment getEvaluationAssignment() {
		return evaluationAssignment;
	}

	public void setEvaluationAssignment(EvaluationAssignment evaluationAssignment) {
		this.evaluationAssignment = evaluationAssignment;
	}

	public ClassroomTimeBlock getClassroomTimeBlock() {
		return classroomTimeBlock;
	}

	public void setClassroomTimeBlock(ClassroomTimeBlock classroomTimeBlock) {
		this.classroomTimeBlock = classroomTimeBlock;
	}
	
	@JsonIgnore
	public Set<MatterTestStudent> getMatterTests() {
		return matterTests;
	}
	
	public Classroom getClassroom() {
		return classroomTimeBlock.getEvaluationEventClassroom().getClassroom();
	}
	
	public EvaluationCenter getEvaluationCenter() {
		return classroomTimeBlock.getEvaluationEventClassroom().getEvaluationCenter();
	}
	
	public Long getEvaluationEventEvaluationCenterId() {
		return classroomTimeBlock.getEvaluationEventClassroom().getEvaluationEventEvaluationCenter().getId();
	}
	
	@JsonIgnore
	public Net getNet() {
		return net;
	}

	public void setNet(Net net) {
		this.net = net;
	}

	@JsonIgnore
	public Server getServer() {
		return server;
	}

	public void setServer(Server server) {
		this.server = server;
	}

	public List<MatterTestStudentInfo> getMatterTestInfos() {
		List<MatterTestStudentInfo> matterTestInfos = new ArrayList<MatterTestStudentInfo>();
		for (MatterTestStudent matterTest : matterTests) {
			matterTestInfos.add(new MatterTestStudentInfo(matterTest));
		}
		
		return matterTestInfos;
	}
	
	public boolean isScheduleModificationDate() {
		return getEvaluationAssignment().getEvaluationEvent().isScheduleModificationDate();
	}
}
