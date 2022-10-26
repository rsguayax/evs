package es.grammata.evaluation.evs.data.model.repository;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.springframework.format.annotation.DateTimeFormat;

import es.grammata.evaluation.evs.data.model.base.BaseModelEntity;
import es.grammata.evaluation.evs.serializers.DateTimeUtcSerializer;


@Entity
@Table(name = "EVALUATION_EVENT")
public class EvaluationEvent extends BaseModelEntity<Long> {

	public static final String STATE_EDIT = "EDIT";
	public static final String STATE_END = "END";
	
	// Estados de eventos de tipo admisión o complexivo
	public static final String STATE_PENDING = "PENDING";
	public static final String STATE_PENDING_QUALIFICATION = "PENDING_QUALIFICATION";	// Se han notificado los usuarios, test y horarios a siette y se esta pendiente de la finalización de los test para obtener las notas
	public static final String STATE_QUALIFIED = "QUALIFIED";	// Se han obtenido las notas de Siette
	public static final String STATE_ADMISSION_QUALIFIED = "ADMISSION_QUALIFIED";	// Se han calculado las notas de Admisión
	public static final String STATE_REPORTED = "REPORTED";
	public static final String STATE_CLOSED = "CLOSED";

	@NotNull(message="El campo no puede estar vacío")
	@Size(min=2, max=100, message="El código debe tener al menos 2 caracteres")
	@Column(unique=true)
	private String code;

	@NotNull(message="El campo no puede estar vacío")
	@Size(min=2, max=250, message="El nombre debe tener al menos 2 caracteres")
	private String name;

	@NotNull(message="El campo no puede estar vacío")
	@DateTimeFormat(pattern="dd-MM-yyyy HH:mm")
	private Date startDate;

	@NotNull(message="El campo no puede estar vacío")
	@DateTimeFormat(pattern="dd-MM-yyyy HH:mm")
	private Date endDate;

	@NotNull(message="El campo no puede estar vacío")
	private int testDuration;

	@Column(columnDefinition="TEXT")
	private String comment;

	/**
	 * Para los eventos de evaluaci�n de los tipos admisi�n y complexivo en la tabla
evaluation_event el campo state almacenar� el estado asignaci�n de plazas, que podr�
tomar los siguientes valores:
1. PENDING.- Pendiente de los resultados de los tests (creado)
2. REPORTED.- Reporte de admisión generado
3. CLOSED.- Estado tras cerrar la asignaci�n de plazas
	 */
	@Size(min=1, max=64, message="El tamaño no es correcto")
	private String state;

	@OneToOne(mappedBy="evaluationEvent", cascade=CascadeType.REMOVE, fetch=FetchType.EAGER)
	private EvaluationEventConfiguration configuration; 

	@OneToMany(mappedBy="evaluationEvent", cascade=CascadeType.REMOVE, fetch=FetchType.LAZY)
    private Set<EvaluationEventEvaluationCenter> evaluationEventEvaluationCenters = new HashSet<EvaluationEventEvaluationCenter>();

	@ManyToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
    @JoinTable(name="evaluation_event_admin", joinColumns=@JoinColumn(name="evaluation_event_id"), inverseJoinColumns=@JoinColumn(name="user_id"))
    private Set<User> admins;

	@OneToMany(mappedBy="evaluationEvent", cascade=CascadeType.REMOVE, fetch=FetchType.EAGER)
    private Set<EvaluationEventTeacher> evaluationEventTeachers;

	@OneToMany(mappedBy="evaluationEvent", cascade=CascadeType.REMOVE, fetch=FetchType.LAZY)
    private Set<Schedule> schedules;

	@OneToMany(mappedBy="evaluationEvent", cascade=CascadeType.REMOVE, fetch=FetchType.EAGER)
    private Set<ScheduleModificationDate> scheduleModificationDates;

	@NotNull(message="El campo no puede estar vacío")
	@ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(name="evaluation_event_evaluation_type", joinColumns=@JoinColumn(name="evaluation_event_id"), inverseJoinColumns=@JoinColumn(name="evaluation_type_id"))
    private Set<EvaluationType> evaluationTypes;
	 
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "evaluation_event_type_id")
	private EvaluationEventType evaluationEventTypes;
	
	/*@ManyToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
    @JoinTable(name="evaluation_event_degree_relations", joinColumns=@JoinColumn(name="evaluation_event_id"), inverseJoinColumns=@JoinColumn(name="degree_id"))
   */

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@JsonSerialize(using=DateTimeUtcSerializer.class)
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@JsonSerialize(using=DateTimeUtcSerializer.class)
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public int getTestDuration() {
		return testDuration;
	}

	public void setTestDuration(int testDuration) {
		this.testDuration = testDuration;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@JsonIgnore
	public EvaluationEventConfiguration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(EvaluationEventConfiguration configuration) {
	    this.configuration = configuration;
	}

	@JsonIgnore
	public Set<User> getAdmins() {
		return admins;
	}

	@JsonIgnore
	public Set<User> getTeachers() {
		Set<User> teachers = new HashSet<User>();
		for(EvaluationEventTeacher evaluationEventTeacher : evaluationEventTeachers) {
			teachers.add(evaluationEventTeacher.getTeacher());
		}
		return teachers;
	}

	@JsonIgnore
	public Set<EvaluationEventTeacher> getEvaluationEventTeachers() {
		return evaluationEventTeachers;
	}

	@JsonIgnore
	public Set<EvaluationEventEvaluationCenter> getEvaluationEventEvaluationCenters() {
		return evaluationEventEvaluationCenters;
	}

	@JsonIgnore
	public Set<EvaluationCenter> getEvaluationCenters() {
		Set<EvaluationCenter> evaluationCenters = new HashSet<EvaluationCenter>();
		for(EvaluationEventEvaluationCenter evaluationEventEvaluationCenter : evaluationEventEvaluationCenters) {
			evaluationCenters.add(evaluationEventEvaluationCenter.getEvaluationCenter());
		}

		return evaluationCenters;
	}

	@JsonIgnore
	public Set<Schedule> getSchedules() {
		return schedules;
	}

	@JsonIgnore
	public Set<ScheduleModificationDate> getScheduleModificationDates() {
		return scheduleModificationDates;
	}

	@JsonIgnore
	public Set<EvaluationType> getEvaluationTypes() {
		return evaluationTypes;
	}

	public void setEvaluationTypes(Set<EvaluationType> evaluationTypes) {
		this.evaluationTypes = evaluationTypes;
	}
	
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
	@PrePersist
	public void onPrePersist() {
		this.state = isAdmissionOrComplexiveType() ? STATE_PENDING : STATE_EDIT;
	}
	
	@JsonIgnore
	public boolean isScheduleModificationDate() {
		Date now = new Date();

		for (ScheduleModificationDate scheduleModificationDate : scheduleModificationDates) {
			if (!(now.before(scheduleModificationDate.getStartDate()) || now.after(scheduleModificationDate.getEndDate()))) {
				return true;
			}
		}

		return false;
	}

	public EvaluationEventType getEvaluationEventTypes() {
		return evaluationEventTypes;
	}

	public void setEvaluationEventTypes(EvaluationEventType evaluationEventTypes) {
		this.evaluationEventTypes = evaluationEventTypes;
	}

	public boolean isAdmissionOrComplexiveType() {
		if (evaluationEventTypes.getId() == 1 || evaluationEventTypes.getId() == 2 || evaluationEventTypes.getId() == 3) {
			return true;
		}
		
		return false;
	}
	
	public boolean isState1() {
		boolean retorno;
		if (state.equals("PENDING")|| state.equals("REPORTED")) {
			retorno= true;
		}else {
			retorno= false;
		}
		
		return retorno;
	}
}