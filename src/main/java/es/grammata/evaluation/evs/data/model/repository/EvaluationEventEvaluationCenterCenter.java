package es.grammata.evaluation.evs.data.model.repository;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import es.grammata.evaluation.evs.data.model.base.BaseModelEntity;

@Entity
@Table(name = "EVALUATION_EVENT_EVALUATION_CENTER_CENTER", 
	uniqueConstraints=@UniqueConstraint(columnNames = {"center_id", "evaluation_event_evaluation_center_id"}))
public class EvaluationEventEvaluationCenterCenter extends BaseModelEntity<Long> {
	
	@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="center_id")
    private Center center;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="evaluation_event_evaluation_center_id")
    //@OnDelete(action = OnDeleteAction.CASCADE)
    private EvaluationEventEvaluationCenter evaluationEventEvaluationCenter;
    

	public EvaluationEventEvaluationCenterCenter() {
    }
    
    public EvaluationEventEvaluationCenterCenter(Center center, EvaluationEventEvaluationCenter evaluationEventEvaluationCenter) {
    	this.center = center;
    	this.evaluationEventEvaluationCenter = evaluationEventEvaluationCenter;
    }

	public EvaluationEventEvaluationCenter getEvaluationEventEvaluationCenter() {
		return evaluationEventEvaluationCenter;
	}

	public void setEvaluationEventEvaluationCenter(
			EvaluationEventEvaluationCenter evaluationEventEvaluationCenter) {
		this.evaluationEventEvaluationCenter = evaluationEventEvaluationCenter;
	}

	public Center getCenter() {
		return center;
	}

	public void setCenter(Center center) {
		this.center = center;
	}
	
	

    

}
