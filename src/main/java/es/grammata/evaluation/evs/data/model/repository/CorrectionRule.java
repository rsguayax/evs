package es.grammata.evaluation.evs.data.model.repository;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonIgnore;

import es.grammata.evaluation.evs.data.model.base.BaseModelEntity;

@Entity
@Table(name = "CORRECTION_RULE")
public class CorrectionRule extends BaseModelEntity<Long> {
	
	@NotNull(message="El campo no puede estar vacío")
	private String type;

	@NotNull(message="El campo no puede estar vacío")
	private Float minGrade;
	
	@NotNull(message="El campo no puede estar vacío")
	private Float maxGrade;
	
	@OneToOne(fetch = FetchType.LAZY)
    private EvaluationEventConfiguration evaluationEventConfiguration;
	
	@OneToOne(fetch = FetchType.LAZY)
    private Matter matter;
	
	@OneToOne(fetch = FetchType.LAZY)
    private Test test;
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Float getMinGrade() {
		return minGrade;
	}

	public void setMinGrade(Float minGrade) {
		this.minGrade = minGrade;
	}

	public Float getMaxGrade() {
		return maxGrade;
	}

	public void setMaxGrade(Float maxGrade) {
		this.maxGrade = maxGrade;
	}

	@JsonIgnore
	public EvaluationEventConfiguration getEvaluationEventConfiguration() {
		return evaluationEventConfiguration;
	}

	public void setEvaluationEventConfiguration(EvaluationEventConfiguration evaluationEventConfiguration) {
		this.evaluationEventConfiguration = evaluationEventConfiguration;
	}

	@JsonIgnore
	public Matter getMatter() {
		return matter;
	}

	public void setMatter(Matter matter) {
		this.matter = matter;
	}

	@JsonIgnore
	public Test getTest() {
		return test;
	}

	public void setTest(Test test) {
		this.test = test;
	}
}
