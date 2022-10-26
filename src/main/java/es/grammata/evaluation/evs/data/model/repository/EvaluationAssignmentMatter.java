package es.grammata.evaluation.evs.data.model.repository;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import es.grammata.evaluation.evs.data.model.base.BaseModelEntity;


@Entity
@Table(name = "EVALUATION_ASSIGNMENT_MATTER")
public class EvaluationAssignmentMatter extends BaseModelEntity<Long> {
	
	@ManyToOne(fetch = FetchType.EAGER)
	private EvaluationAssignment evaluationAssignment;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private EvaluationEventMatter evaluationEventMatter;
	
	private String channel;
	
	private String career;
	
	private String careerCode;
	
	@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="center_id")
	private Center center;
	
	
	public EvaluationAssignment getEvaluationAssignment() {
		return evaluationAssignment;
	}

	public void setEvaluationAssignment(EvaluationAssignment evaluationAssignment) {
		this.evaluationAssignment = evaluationAssignment;
	}

	public EvaluationEventMatter getEvaluationEventMatter() {
		return evaluationEventMatter;
	}

	public void setEvaluationEventMatter(EvaluationEventMatter evaluationEventMatter) {
		this.evaluationEventMatter = evaluationEventMatter;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getCareer() {
		return career;
	}

	public void setCareer(String career) {
		this.career = career;
	}

	public String getCareerCode() {
		return careerCode;
	}

	public void setCareerCode(String careerCode) {
		this.careerCode = careerCode;
	}

	public Center getCenter() {
		return center;
	}

	public void setCenter(Center center) {
		this.center = center;
	}

}
