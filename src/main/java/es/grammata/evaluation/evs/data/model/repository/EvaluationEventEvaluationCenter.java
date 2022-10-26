package es.grammata.evaluation.evs.data.model.repository;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;

import es.grammata.evaluation.evs.data.model.base.BaseModelEntity;

@Entity
@Table(name = "EVALUATION_EVENT_EVALUATION_CENTER")
public class EvaluationEventEvaluationCenter extends BaseModelEntity<Long> {
	
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="evaluation_event_id")
	@JsonIgnore
    private EvaluationEvent evaluationEvent;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="evaluation_center_id")
    private EvaluationCenter evaluationCenter;
    
    @OneToMany(mappedBy="evaluationEventEvaluationCenter", cascade=CascadeType.REMOVE, fetch=FetchType.LAZY)
    @OrderBy("id")
    private Set<EvaluationEventClassroom> evaluationEventClassrooms = new HashSet<EvaluationEventClassroom>();
    
    public EvaluationEventEvaluationCenter() {
    	
    }
    
    public EvaluationEventEvaluationCenter(EvaluationEvent evaluationEvent, EvaluationCenter evaluationCenter) {
    	this.evaluationEvent = evaluationEvent;
    	this.evaluationCenter = evaluationCenter;
    }

    @JsonIgnore
	public EvaluationEvent getEvaluationEvent() {
		return evaluationEvent;
	}

	public void setEvaluationEvent(EvaluationEvent evaluationEvent) {
		this.evaluationEvent = evaluationEvent;
	}

	public EvaluationCenter getEvaluationCenter() {
		return evaluationCenter;
	}

	public void setEvaluationCenter(EvaluationCenter evaluationCenter) {
		this.evaluationCenter = evaluationCenter;
	}
	
	public Set<EvaluationEventClassroom> getEvaluationEventClassrooms() {
		return evaluationEventClassrooms;
	}
	
	/**
	 * inicializar evealuationeventClassroom antes de usar
	 * @return
	 */
	@JsonIgnore
	public Set<Classroom> getClassrooms() {
		Set<Classroom> classrooms = new HashSet<Classroom>();
		for(EvaluationEventClassroom evaluationEventClassroom : evaluationEventClassrooms) {
			classrooms.add(evaluationEventClassroom.getClassroom());
		}
		
		return classrooms;
	}
	
	@JsonIgnore
	public Set<Cap> getCaps() {
		Set<Cap> caps = new HashSet<Cap>();
		for(EvaluationEventClassroom evaluationEventClassroom : evaluationEventClassrooms) {
			caps.add(evaluationEventClassroom.getCap());
		}
		
		return caps;
	}
}
