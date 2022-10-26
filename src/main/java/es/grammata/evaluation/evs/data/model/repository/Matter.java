package es.grammata.evaluation.evs.data.model.repository;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

import es.grammata.evaluation.evs.data.model.base.BaseModelEntity;


@Entity
@Table(name = "MATTER")
public class Matter extends BaseModelEntity<Long> {

	public static final String TYPE_WS = "WS";
	public static final String TYPE_EVS = "EVS";

	@NotNull(message="El campo no puede estar vacío")
	@Size(max=1024, message="El tamaño no es correcto")
	private String name;

	@Size(min=1, max=256, message="El tamaño no es correcto")
	private String type;

	@ManyToOne
    @PrimaryKeyJoinColumn
	private AcademicLevel academicLevel;


	@Column(unique=true)
	@NotNull(message="El campo no puede estar vacío")
	@Size(min=2, max=256, message="El tamaño no es correcto")
	private String code;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "MATTER_BANK",
            joinColumns = { @JoinColumn(name = "matter_id") },
            inverseJoinColumns = { @JoinColumn(name = "bank_id") })
	private Set<Bank> banks;

	@ManyToMany(fetch=FetchType.EAGER)  
    @JoinTable(name="matter_day", joinColumns={@JoinColumn(name="matter_id")})
	private Set<WeekDay> daysAllowed;

	@OneToOne(mappedBy="matter", cascade=CascadeType.REMOVE, fetch=FetchType.EAGER)
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

	public AcademicLevel getAcademicLevel() {
		return academicLevel;
	}

	public void setAcademicLevel(AcademicLevel academicLevel) {
		this.academicLevel = academicLevel;
	}


	public Set<Bank> getBanks() {
		return banks;
	}


	public void setBanks(Set<Bank> banks) {
		this.banks = banks;
	}

	/*@JsonIgnore
	public Set<EvaluationEventMatter> getEvaluationEventMatters() {
		return evaluationEventMatters;
	}

	public void setEvaluationEventMatters(
			Set<EvaluationEventMatter> evaluationEventMatters) {
		this.evaluationEventMatters = evaluationEventMatters;
	}*/

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public Set<WeekDay> getDaysAllowed() {
		return daysAllowed;
	}

	public void setDaysAllowed(Set<WeekDay> daysAllowed) {
		this.daysAllowed = daysAllowed;
	}

	@JsonProperty
	public boolean getHasBanks() {
		if(banks != null && banks.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	@JsonIgnore
	public void setHasBanks(boolean hasBanks) {

	}

	@Override
	public String toString() {
	    StringBuilder builder = new StringBuilder();
	    builder.append("Matter [name=");
	    builder.append(name);
	    builder.append(", type=");
	    builder.append(type);
	    builder.append(", academicLevel=");
	    builder.append(academicLevel);
	    builder.append(", code=");
	    builder.append(code);
	    builder.append(", banks=");
	    builder.append(banks);
	    builder.append(", subjects=");
	    builder.append(", user=");
	    builder.append("]");
	    return builder.toString();
	}

	
	@JsonIgnore
	public String getIdAsString() {
		return new Long(this.getId()).toString();
	}
}
