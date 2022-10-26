package es.grammata.evaluation.evs.data.model.repository;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.format.annotation.DateTimeFormat;

import es.grammata.evaluation.evs.data.model.base.BaseModelEntity;

@Entity
@Table(name = "EVALUATION_EVENT_CONFIGURATION")
public class EvaluationEventConfiguration extends BaseModelEntity<Long> {

    private static final long serialVersionUID = 1L;
    
    public static final String MANUAL_TYPE = "MANUAL";
    public static final String AUTOMATIC_TYPE = "AUTOMATICO";

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluation_event_id")
    @NotNull(message = "El campo no puede estar vacío")
    private EvaluationEvent evaluationEvent;

    @NotNull(message = "El campo no puede estar vacío")
    private int maxTimeBlockTests;

    @NotNull(message = "El campo no puede estar vacío")
    private int maxDailyTests;

    private boolean testsInSeveralTimeBlocksSameDay;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date dailyStudentsAndSchedulesLoadStartDate;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date dailyStudentsAndSchedulesLoadEndDate;

    @DateTimeFormat(pattern = "HH:mm")
    private Date dailyStudentsAndSchedulesLoadTime;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date dailyEvaluationSchedulesMailingStartDate;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date dailyEvaluationSchedulesMailingEndDate;

    @DateTimeFormat(pattern = "HH:mm")
    private Date dailyEvaluationSchedulesMailingTime;
    
    private String assignmentType;
    
    @OneToOne(mappedBy="evaluationEventConfiguration", cascade=CascadeType.REMOVE, fetch=FetchType.EAGER)
	private CorrectionRule correctionRule; 

    public CorrectionRule getCorrectionRule() {
		return correctionRule;
	}

	public void setCorrectionRule(CorrectionRule correctionRule) {
		this.correctionRule = correctionRule;
	}

	public EvaluationEventConfiguration() {

    }

    public EvaluationEventConfiguration(EvaluationEvent evaluationEvent) {
		super();
		this.evaluationEvent = evaluationEvent;
		Calendar cyesterday = Calendar.getInstance();
		cyesterday.set(Calendar.HOUR_OF_DAY, 0);
		cyesterday.set(Calendar.MINUTE, 0);
		cyesterday.set(Calendar.SECOND, 0);
		cyesterday.set(Calendar.MILLISECOND, 0);
		cyesterday.add(Calendar.DAY_OF_MONTH, -1);
		Date yesterday = cyesterday.getTime();
	
		Calendar ctime = Calendar.getInstance();
		ctime.set(Calendar.HOUR_OF_DAY, 0);
		ctime.set(Calendar.MINUTE, 0);
		ctime.set(Calendar.SECOND, 0);
		ctime.set(Calendar.MILLISECOND, 0);
		ctime.add(Calendar.DAY_OF_MONTH, -1);
		Date time = ctime.getTime();
	
		this.maxTimeBlockTests = 0;
		this.maxDailyTests = 0;
		this.testsInSeveralTimeBlocksSameDay = true;
		this.assignmentType = MANUAL_TYPE;
		this.dailyStudentsAndSchedulesLoadStartDate = yesterday;
		this.dailyStudentsAndSchedulesLoadEndDate = yesterday;
		this.dailyStudentsAndSchedulesLoadTime = time;
		this.dailyEvaluationSchedulesMailingStartDate = yesterday;
		this.dailyEvaluationSchedulesMailingEndDate = yesterday;
		this.dailyEvaluationSchedulesMailingTime = time;
    }

    public EvaluationEventConfiguration( EvaluationEvent evaluationEvent, int maxTimeBlockTests, int maxDailyTests,
		    boolean testsInSeveralTimeBlocksSameDay, Date dailyStudentsAndSchedulesLoadStartDate,
		    Date dailyStudentsAndSchedulesLoadEndDate, Date dailyStudentsAndSchedulesLoadTime,
		    Date dailyEvaluationSchedulesMailingStartDate, Date dailyEvaluationSchedulesMailingEndDate,
		    Date dailyEvaluationSchedulesMailingTime) {
		super();
		this.evaluationEvent = evaluationEvent;
		this.maxTimeBlockTests = maxTimeBlockTests;
		this.maxDailyTests = maxDailyTests;
		this.testsInSeveralTimeBlocksSameDay = testsInSeveralTimeBlocksSameDay;
		this.dailyStudentsAndSchedulesLoadStartDate = dailyStudentsAndSchedulesLoadStartDate;
		this.dailyStudentsAndSchedulesLoadEndDate = dailyStudentsAndSchedulesLoadEndDate;
		this.dailyStudentsAndSchedulesLoadTime = dailyStudentsAndSchedulesLoadTime;
		this.dailyEvaluationSchedulesMailingStartDate = dailyEvaluationSchedulesMailingStartDate;
		this.dailyEvaluationSchedulesMailingEndDate = dailyEvaluationSchedulesMailingEndDate;
		this.dailyEvaluationSchedulesMailingTime = dailyEvaluationSchedulesMailingTime;
    }

    @JsonIgnore
    public EvaluationEvent getEvaluationEvent() {
    	return evaluationEvent;
    }

    public void setEvaluationEvent(EvaluationEvent evaluationEvent) {
    	this.evaluationEvent = evaluationEvent;
    }

    public int getMaxTimeBlockTests() {
    	return maxTimeBlockTests;
    }

    public void setMaxTimeBlockTests(int maxTimeBlockTests) {
		this.maxTimeBlockTests = maxTimeBlockTests;
    }

    public int getMaxDailyTests() {
    	return maxDailyTests;
    }

    public void setMaxDailyTests(int maxDailyTests) {
    	this.maxDailyTests = maxDailyTests;
    }

    public boolean isTestsInSeveralTimeBlocksSameDay() {
    	return testsInSeveralTimeBlocksSameDay;
    }

    public void setTestsInSeveralTimeBlocksSameDay(boolean testsInSeveralTimeBlocksSameDay) {
    	this.testsInSeveralTimeBlocksSameDay = testsInSeveralTimeBlocksSameDay;
    }

    public Date getDailyStudentsAndSchedulesLoadStartDate() {
        return dailyStudentsAndSchedulesLoadStartDate;
    }

    public void setDailyStudentsAndSchedulesLoadStartDate(Date dailyStudentsAndSchedulesLoadStartDate) {
        this.dailyStudentsAndSchedulesLoadStartDate = dailyStudentsAndSchedulesLoadStartDate;
    }

    public Date getDailyStudentsAndSchedulesLoadEndDate() {
        return dailyStudentsAndSchedulesLoadEndDate;
    }

    public void setDailyStudentsAndSchedulesLoadEndDate(Date dailyStudentsAndSchedulesLoadEndDate) {
        this.dailyStudentsAndSchedulesLoadEndDate = dailyStudentsAndSchedulesLoadEndDate;
    }

    public Date getDailyStudentsAndSchedulesLoadTime() {
        return dailyStudentsAndSchedulesLoadTime;
    }

    public void setDailyStudentsAndSchedulesLoadTime(Date dailyStudentsAndSchedulesLoadTime) {
        this.dailyStudentsAndSchedulesLoadTime = dailyStudentsAndSchedulesLoadTime;
    }

    public Date getDailyEvaluationSchedulesMailingStartDate() {
        return dailyEvaluationSchedulesMailingStartDate;
    }

    public void setDailyEvaluationSchedulesMailingStartDate(Date dailyEvaluationSchedulesMailingStartDate) {
        this.dailyEvaluationSchedulesMailingStartDate = dailyEvaluationSchedulesMailingStartDate;
    }

    public Date getDailyEvaluationSchedulesMailingEndDate() {
        return dailyEvaluationSchedulesMailingEndDate;
    }

    public void setDailyEvaluationSchedulesMailingEndDate(Date dailyEvaluationSchedulesMailingEndDate) {
        this.dailyEvaluationSchedulesMailingEndDate = dailyEvaluationSchedulesMailingEndDate;
    }

    public Date getDailyEvaluationSchedulesMailingTime() {
        return dailyEvaluationSchedulesMailingTime;
    }

    public void setDailyEvaluationSchedulesMailingTime(Date dailyEvaluationSchedulesMailingTime) {
        this.dailyEvaluationSchedulesMailingTime = dailyEvaluationSchedulesMailingTime;
    }

    public String getAssignmentType() {
		return assignmentType;
	}

	public void setAssignmentType(String assignmentType) {
		this.assignmentType = assignmentType;
	}

	@Override
    public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("EvaluationEventConfiguration [evaluationEvent=");
		builder.append(evaluationEvent);
		builder.append(", maxTimeBlockTests=");
		builder.append(maxTimeBlockTests);
		builder.append(", maxDailyTests=");
		builder.append(maxDailyTests);
		builder.append(", testsInSeveralTimeBlocksSameDay=");
		builder.append(testsInSeveralTimeBlocksSameDay);
		builder.append(", dailyStudentsAndSchedulesLoadStartDate=");
		builder.append(dailyStudentsAndSchedulesLoadStartDate);
		builder.append(", dailyStudentsAndSchedulesLoadEndDate=");
		builder.append(dailyStudentsAndSchedulesLoadEndDate);
		builder.append(", dailyStudentsAndSchedulesLoadTime=");
		builder.append(dailyStudentsAndSchedulesLoadTime);
		builder.append(", dailyEvaluationSchedulesMailingStartDate=");
		builder.append(dailyEvaluationSchedulesMailingStartDate);
		builder.append(", dailyEvaluationSchedulesMailingEndDate=");
		builder.append(dailyEvaluationSchedulesMailingEndDate);
		builder.append(", dailyEvaluationSchedulesMailingTime=");
		builder.append(dailyEvaluationSchedulesMailingTime);
		builder.append("]");
		return builder.toString();
    }
}
