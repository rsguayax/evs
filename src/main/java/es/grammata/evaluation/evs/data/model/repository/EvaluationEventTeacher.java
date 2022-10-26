package es.grammata.evaluation.evs.data.model.repository;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import es.grammata.evaluation.evs.data.model.base.BaseModelEntity;

@Entity
@Table(name = "EVALUATION_EVENT_TEACHER")
public class EvaluationEventTeacher extends BaseModelEntity<Long> {

	@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="evaluation_event_id")
    private EvaluationEvent evaluationEvent;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="user_id")
    private User teacher;
    
    public EvaluationEventTeacher() {
    	
    }
    
    public EvaluationEventTeacher(EvaluationEvent evaluationEvent, User teacher) {
    	this.evaluationEvent = evaluationEvent;
    	this.teacher = teacher;
    }

	public EvaluationEvent getEvaluationEvent() {
		return evaluationEvent;
	}

	public void setEvaluationEvent(EvaluationEvent evaluationEvent) {
		this.evaluationEvent = evaluationEvent;
	}

	public User getTeacher() {
		return teacher;
	}

	public void setTeacher(User teacher) {
		this.teacher = teacher;
	}
}
