package es.grammata.evaluation.evs.data.model.repository;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.LazyInitializationException;
import org.springframework.format.annotation.DateTimeFormat;

import es.grammata.evaluation.evs.data.model.base.BaseModelEntity;


@Entity
@Table(name = "BANK")
public class Bank extends BaseModelEntity<Long> {
	
	public static final String STATE_EDIT = "EDICIÓN";
	public static final String STATE_END = "FINALIZADO";
	
	public static final String STATE_ACTIVO = "ACTIVO";
	public static final String STATE_INACTIVO = "INACTIVO";
	
	private Integer isComplete; // obsoleto
	
	@NotNull(message="El campo no puede estar vacío")
	@Size(min=2, max=512, message="El tamaño no es correcto")
	private String name;
	
	@NotNull(message="El campo no puede estar vacío")
	private Integer questionNumber;
	
	private Integer currentNumber; // obsoleto
	
	@ManyToOne(fetch = FetchType.EAGER)
	private Department department;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private EvaluationEventType evaluationEventType;
	
	@NotNull(message="El campo no puede estar vacío")
	@DateTimeFormat(pattern="yyyy/MM-dd HH:mm")
	private Date createDate;
	
	@DateTimeFormat(pattern="yyyy/MM-dd HH:mm")
	private Date modifiedDate;
	
	@NotNull(message="El campo no puede estar vacío")
	@Size(min=2, max=45, message="El tamaño no es correcto")
	private String state;
	
	@Column(unique=true)
	@NotNull(message="El campo no puede estar vacío")
	private Integer externalId;
	
	@OneToMany(mappedBy="bank", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
	private Set<Test> tests;
	
	@PrePersist
	void preInsert() {
		isComplete = 0;
		if(questionNumber == null) {
			questionNumber = 0;
		}
		if(currentNumber == null) {
			currentNumber = 0;
		}
		createDate = new Date();
		state = STATE_EDIT;
	}

	public Integer getIsComplete() {
		return isComplete;
	}

	public void setIsComplete(Integer isComplete) {
		this.isComplete = isComplete;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getQuestionNumber() {
		return questionNumber;
	}

	public void setQuestionNumber(Integer questionNumber) {
		this.questionNumber = questionNumber;
	}

	public Integer getCurrentNumber() {
		return currentNumber;
	}

	public void setCurrentNumber(Integer currentNumber) {
		this.currentNumber = currentNumber;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Integer getExternalId() {
		return externalId;
	}

	public void setExternalId(Integer externalId) {
		this.externalId = externalId;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	@JsonIgnore
	public Set<Test> getTests() {
		return tests;
	}

	public void setTests(Set<Test> tests) {
		this.tests = tests;
	}

	@JsonIgnore
	public String getIsCompleteToString() {
		if(this.isComplete==1) {
			return "Sí";
		} else {
			return "No";
		}
	}

	public EvaluationEventType getEvaluationEventType() {
		return evaluationEventType;
	}

	public void setEvaluationEventType(EvaluationEventType evaluationEventType) {
		this.evaluationEventType = evaluationEventType;
	}

	@JsonIgnore
	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	
	@JsonProperty
	public boolean getHasActiveTestWithoutEvaluationType() {
		try {
			for (Test test : getTests()) {
				if (test.getActive() == 1 && test.getEvaluationType() == null) {
					return true;
				}
			}
			
			return false;
		} catch (LazyInitializationException e) {
			return false;
		}
	}
	
	@JsonIgnore
	public void setHasActiveTestWithoutEvaluationType(boolean HasActiveTestWithoutEvaluationType) {

	}
}
