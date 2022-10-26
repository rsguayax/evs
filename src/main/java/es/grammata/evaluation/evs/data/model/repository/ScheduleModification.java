package es.grammata.evaluation.evs.data.model.repository;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonIgnore;

import es.grammata.evaluation.evs.data.model.base.audit.AuditableEntity;

@Entity
@Table(name = "SCHEDULE_MODIFICATION")
public class ScheduleModification extends AuditableEntity<Long> {
	
	@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="evaluation_assignment_id")
	@NotNull(message="El campo no puede estar vacío")
	private EvaluationAssignment evaluationAssignment;
	
	@OneToMany(mappedBy="scheduleModification", cascade=CascadeType.REMOVE, fetch=FetchType.EAGER)
    private Set<ScheduleModificationInfo> scheduleModificationInfos = new HashSet<ScheduleModificationInfo>();
	
	@NotNull(message="El campo no puede estar vacío")
	@Column(columnDefinition="TEXT")
	private String message;
	
	public ScheduleModification() {
		
	}
	
	public ScheduleModification(EvaluationAssignment evaluationAssignment, String message) {
		this.evaluationAssignment = evaluationAssignment;
		this.message = message;
	}

	@JsonIgnore
	public EvaluationAssignment getEvaluationAssignment() {
		return evaluationAssignment;
	}

	public void setEvaluationAssignment(EvaluationAssignment evaluationAssignment) {
		this.evaluationAssignment = evaluationAssignment;
	}

	public Set<ScheduleModificationInfo> getScheduleModificationInfos() {
		return scheduleModificationInfos;
	}

	public void setScheduleModificationInfos(Set<ScheduleModificationInfo> scheduleModificationInfos) {
		this.scheduleModificationInfos = scheduleModificationInfos;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
