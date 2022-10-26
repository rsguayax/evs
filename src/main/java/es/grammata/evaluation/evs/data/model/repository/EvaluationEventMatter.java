package es.grammata.evaluation.evs.data.model.repository;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

import es.grammata.evaluation.evs.data.model.base.BaseModelEntity;


@Entity
@Table(name = "EVALUATION_EVENT_MATTER", uniqueConstraints=@UniqueConstraint(columnNames = {"evaluationevent_id", "matter_id", "academicperiod_id", "mode_id"}))
public class EvaluationEventMatter extends BaseModelEntity<Long> {
	
	@ManyToOne(fetch = FetchType.LAZY)
	private EvaluationEvent evaluationEvent;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private Matter matter;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private AcademicPeriod academicPeriod;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private Mode mode;
	
	@OneToOne(fetch = FetchType.EAGER)
	private Bank bank;
	
	private Float extraScore;
	
	@ManyToMany(fetch=FetchType.EAGER)  
    @JoinTable(name="evaluation_event_matter_evaluation_type", joinColumns={@JoinColumn(name="evaluationeventmatter_id")}, 
    						inverseJoinColumns=@JoinColumn(name="evaluation_type_id"))  
    private Set<EvaluationType> evaluationTypes;
	
	@ManyToMany(fetch=FetchType.EAGER)  
    @JoinTable(name="evaluation_event_matter_day", joinColumns={@JoinColumn(name="evaluationeventmatter_id")})
	private Set<WeekDay> daysAllowed;
	

	public EvaluationEventMatter() {}
	
	public EvaluationEventMatter(EvaluationEvent evaluationEvent, Matter matter, AcademicPeriod academicPeriod, Mode mode) {
		this.setEvaluationEvent(evaluationEvent);
		this.setMatter(matter);
		this.setAcademicPeriod(academicPeriod);
		this.setMode(mode);
	}

	@JsonIgnore
	public EvaluationEvent getEvaluationEvent() {
		return evaluationEvent;
	}

	public void setEvaluationEvent(EvaluationEvent evaluationEvent) {
		this.evaluationEvent = evaluationEvent;
	}

	public Matter getMatter() {
		return matter;
	}

	public void setMatter(Matter matter) {
		this.matter = matter;
	}

	@JsonIgnore
	public Bank getBank() {
		return bank;
	}

	public void setBank(Bank bank) {
		this.bank = bank;
	}
	
	public Float getExtraScore() {
		return extraScore;
	}

	public void setExtraScore(Float extraScore) {
		this.extraScore = extraScore;
	}
	
	@JsonIgnore
	public Set<EvaluationType> getEvaluationTypes() {
		return evaluationTypes;
	}

	public void setEvaluationTypes(Set<EvaluationType> evaluationTypes) {
		this.evaluationTypes = evaluationTypes;
	}
	
	public Set<WeekDay> getDaysAllowed() {
		return daysAllowed;
	}

	public void setDaysAllowed(Set<WeekDay> daysAllowed) {
		this.daysAllowed = daysAllowed;
	}
	
	public AcademicPeriod getAcademicPeriod() {
		return academicPeriod;
	}

	public void setAcademicPeriod(AcademicPeriod academicPeriod) {
		this.academicPeriod = academicPeriod;
	}

	public Mode getMode() {
		return mode;
	}

	public void setMode(Mode mode) {
		this.mode = mode;
	}
	
	@JsonProperty
	public boolean getHasBank() {
		if(bank != null) {
			return true;
		} else {
			return false;
		}
	}
	
	@JsonIgnore
	public void setHasBank(boolean hasBank) { 
	}

	@Override
	public int hashCode() {
		String hash = String.valueOf(this.getId().hashCode());
		int result = Integer.valueOf(hash);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EvaluationEventMatter other = (EvaluationEventMatter) obj;
		if (this.getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!this.getId().equals(other.getId()))
			return false;
		return true;
	}
	
	@PrePersist
	void preInsert() {
		this.extraScore = new Float(0);
	}
	
}
