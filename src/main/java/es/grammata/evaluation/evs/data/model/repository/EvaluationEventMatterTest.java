package es.grammata.evaluation.evs.data.model.repository;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import es.grammata.evaluation.evs.data.model.base.BaseModelEntity;


@Entity
@Table(name = "EVALUATION_EVENT_MATTER_TEST", uniqueConstraints=@UniqueConstraint(columnNames = {"test_id", "evaluationeventmatter_id"}))
public class EvaluationEventMatterTest extends BaseModelEntity<Long> {
	
	@ManyToOne(fetch = FetchType.EAGER)
	private Test test;
	
	@ManyToOne(fetch = FetchType.EAGER) 
	private EvaluationEventMatter evaluationEventMatter;
	
	private Integer extraScoreApplied;
	
	private String state;
	
	private Integer enablePublish;
	
	public static final String STATE_PUBLISHED = "PUBLICADO";
	
	public static final String STATE_EVALUABLE = "EVALUABLE";
	
	public static final String STATE_NOTIFIED = "NOTIFICADO";
	
	public static final String STATE_MODIFIED = "RECALIFICADO";
	
	// estado por defecto
	public static final String STATE_WAITING = "ESPERA";
	
	private Float extraScore;
	

	public Test getTest() {
		return test;
	}

	public void setTest(Test test) {
		this.test = test;
	}
	
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public EvaluationEventMatter getEvaluationEventMatter() {
		return evaluationEventMatter;
	}

	public void setEvaluationEventMatter(EvaluationEventMatter evaluationEventMatter) {
		this.evaluationEventMatter = evaluationEventMatter;
	}


	public Integer getExtraScoreApplied() {
		return extraScoreApplied;
	}

	public void setExtraScoreApplied(Integer extraScoreApplied) {
		this.extraScoreApplied = extraScoreApplied;
	}
	
	public Float getExtraScore() {
		return extraScore;
	}

	public void setExtraScore(Float extraScore) {
		this.extraScore = extraScore;
	}
	
	public Integer getEnablePublish() {
		return enablePublish;
	}

	public void setEnablePublish(Integer enablePublish) {
		this.enablePublish = enablePublish;
	}

	@PrePersist
	public void prePersist() {
		this.extraScoreApplied = 0;
		this.extraScore = 0F;
		this.state = STATE_WAITING;
		this.enablePublish = 0;
	}
	
}
