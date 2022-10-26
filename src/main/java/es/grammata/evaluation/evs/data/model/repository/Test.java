package es.grammata.evaluation.evs.data.model.repository;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonIgnore;

import es.grammata.evaluation.evs.data.model.base.BaseModelEntity;

@Entity
@Table(name = "TEST")
public class Test extends BaseModelEntity<Long> {

	@NotNull(message="El campo no puede estar vacío")
	private String name;
	
	private Integer minQuestionNum;
	
	private Integer maxQuestionNum;
	
	private Date createDate;
	
	private Integer time; //en segundos
	
	@Column(unique=true)
	private Integer externalId;
	
	private Integer active;
	
	private String locker;
	
	
	@ManyToOne
    @PrimaryKeyJoinColumn
    private Bank bank;
	
	@ManyToOne
    @PrimaryKeyJoinColumn
    private EvaluationType evaluationType;
	
	@ManyToOne
    @PrimaryKeyJoinColumn
    private TestType testType;
	
	@OneToOne(mappedBy="test", cascade=CascadeType.REMOVE, fetch=FetchType.EAGER)
	private CorrectionRule correctionRule; 

	public CorrectionRule getCorrectionRule() {
		return correctionRule;
	}

	public void setCorrectionRule(CorrectionRule correctionRule) {
		this.correctionRule = correctionRule;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getMinQuestionNum() {
		return minQuestionNum;
	}

	public void setMinQuestionNum(Integer minQuestionNum) {
		this.minQuestionNum = minQuestionNum;
	}

	public Integer getMaxQuestionNum() {
		return maxQuestionNum;
	}

	public void setMaxQuestionNum(Integer maxQuestionNum) {
		this.maxQuestionNum = maxQuestionNum;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Integer getTime() {
		return time;
	}

	public void setTime(Integer time) {
		this.time = time;
	}

	public Integer getExternalId() {
		return externalId;
	}

	public void setExternalId(Integer externalId) {
		this.externalId = externalId;
	}

	@JsonIgnore
	public Bank getBank() {
		return bank;
	}

	public void setBank(Bank bank) {
		this.bank = bank;
	}
	
	public String getBankName() {
		return bank != null ? bank.getName() : "";
	}

	public EvaluationType getEvaluationType() {
		return evaluationType;
	}

	public void setEvaluationType(EvaluationType evaluationType) {
		this.evaluationType = evaluationType;
	}

	public Integer getActive() {
		return active;
	}

	public void setActive(Integer active) {
		this.active = active;
	}

	public TestType getTestType() {
		return testType;
	}

	public void setTestType(TestType testType) {
		this.testType = testType;
	}

	public String getLocker() {
		return locker;
	}

	public void setLocker(String locker) {
		this.locker = locker;
	}

	@PrePersist
	public void init() {
	}

}
