package es.grammata.evaluation.evs.data.model.repository;

import java.io.Serializable;

import javax.persistence.Embeddable;



@Embeddable
public class EvaluationEventMatterId implements Serializable {

	private Long evaluationEventId;

	private Long matterId;
	
	private Long academicPeriodId;
	
	private Long modeId;
	 

	public Long getEvaluationEventId() {
		return evaluationEventId;
	}

	public void setEvaluationEventId(Long evaluationEventId) {
		this.evaluationEventId = evaluationEventId;
	}

	public Long getMatterId() {
		return matterId;
	}

	public void setMatterId(Long matterId) {
		this.matterId = matterId;
	}
	
	public Long getAcademicPeriodId() {
		return academicPeriodId;
	}

	public void setAcademicPeriodId(Long academicPeriodId) {
		this.academicPeriodId = academicPeriodId;
	}

	public Long getModeId() {
		return modeId;
	}

	public void setModeId(Long modeId) {
		this.modeId = modeId;
	}

	@Override
	public int hashCode() {
		String hash = this.getEvaluationEventId().toString() + this.getMatterId().toString() +
				this.getAcademicPeriodId().toString() + this.getModeId().toString();
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
		EvaluationEventMatterId other = (EvaluationEventMatterId) obj;
		if (!evaluationEventId.equals(other.evaluationEventId) || !matterId.equals(other.matterId)
				 || !academicPeriodId.equals(other.academicPeriodId) || !modeId.equals(other.modeId))
			return false;
		return true;
	}
}
