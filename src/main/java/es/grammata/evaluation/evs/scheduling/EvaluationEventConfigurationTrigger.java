package es.grammata.evaluation.evs.scheduling;

import java.util.Calendar;
import java.util.Date;

import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;

import es.grammata.evaluation.evs.data.model.repository.EvaluationEventConfiguration;

/**
 * @author ajmedialdea
 *
 */
public class EvaluationEventConfigurationTrigger implements Trigger {

    public static final String STUDENTS_AND_SCHEDULES_LOAD = "STUDENTS_AND_SCHEDULES_LOAD";

    public static final String EVALUATION_SCHEDULES_MAILING = "EVALUATION_SCHEDULES_MAILING";

    private final EvaluationEventConfiguration evaluationEventConfiguration;

    private final String purpose;

    public EvaluationEventConfigurationTrigger(EvaluationEventConfiguration evaluationEventConfiguration, String purpose) {
	this.evaluationEventConfiguration = evaluationEventConfiguration;
	this.purpose = purpose;
    }

    @Override
    public Date nextExecutionTime(TriggerContext triggerContext) {
	if (evaluationEventConfiguration != null) {
	    Date startDate = null;
	    Date endDate = null;
	    Date time = null;

	    switch (purpose) {
	    case STUDENTS_AND_SCHEDULES_LOAD:
		startDate = evaluationEventConfiguration.getDailyStudentsAndSchedulesLoadStartDate();
		endDate = evaluationEventConfiguration.getDailyStudentsAndSchedulesLoadEndDate();
		time = evaluationEventConfiguration.getDailyStudentsAndSchedulesLoadTime();
		break;

	    case EVALUATION_SCHEDULES_MAILING:
		startDate = evaluationEventConfiguration.getDailyEvaluationSchedulesMailingStartDate();
		endDate = evaluationEventConfiguration.getDailyEvaluationSchedulesMailingEndDate();
		time = evaluationEventConfiguration.getDailyEvaluationSchedulesMailingTime();
		break;

	    default:
		break;
	    }

	    if (startDate != null && endDate != null && time != null) {
		Calendar now = Calendar.getInstance();
		// startDate <= now
		if (compareTo(now.getTime(), startDate) >= 0) {
		    Calendar cal = Calendar.getInstance();
		    cal.set(Calendar.YEAR, now.get(Calendar.YEAR));
		    cal.set(Calendar.MONTH, now.get(Calendar.MONTH));
		    cal.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH));
		    cal.set(Calendar.SECOND, 0);
		    cal.set(Calendar.MILLISECOND, 0);
		    // time < now
		    if (getMinuteOfDay(now.getTime()) - getMinuteOfDay(time) >= 0) {
			cal.add(Calendar.DAY_OF_MONTH, 1);
		    }
		    if (compareTo(endDate, cal.getTime()) >= 0) {
			Calendar cal1 = Calendar.getInstance();
			cal1.setTime(time);
			cal.set(Calendar.HOUR_OF_DAY, cal1.get(Calendar.HOUR_OF_DAY));
			cal.set(Calendar.MINUTE, cal1.get(Calendar.MINUTE));
			Date nextExecutionTime = cal.getTime();
			return nextExecutionTime;
		    }
		}
	    }
	}
	return null;
    }

    /**
     * @param date1
     * @param date2
     * @return the value 0 if the date1 is equal to date2;
     * a value less than 0 if date1 is before the date2;
     * and a value greater than 0 if date1 is after the date2.
     */
    private int compareTo(Date date1, Date date2) {
	if (date1 == null) {
	    throw new NullPointerException("the first argument in null");
	}
	if (date2 == null) {
	    throw new NullPointerException("the second argument in null");
	}
	Calendar cal1 = Calendar.getInstance();
	cal1.setTime(date1);
	int year1 = cal1.get(Calendar.YEAR);
	int month1 = cal1.get(Calendar.MONTH);
	int day1 = cal1.get(Calendar.DAY_OF_MONTH);

	Calendar cal2 = Calendar.getInstance();
	cal2.setTime(date2);
	int year2 = cal2.get(Calendar.YEAR);
	int month2 = cal2.get(Calendar.MONTH);
	int day2 = cal2.get(Calendar.DAY_OF_MONTH);

	int yearDiff = year1 - year2;
	if (yearDiff != 0) {
	    return yearDiff;
	}
	int monthDiff = month1 - month2;
	if (monthDiff != 0) {
	    return monthDiff;
	}
	return day1 - day2;
    }

    private int getMinuteOfDay(Date date) {
	if (date == null) {
	    return 0;
	}
	Calendar calendar = Calendar.getInstance();
	calendar.setTime(date);
	int hour = calendar.get(Calendar.HOUR_OF_DAY);
	int minute = calendar.get(Calendar.MINUTE);
	return hour * 60 + minute;
    }

    @Override
    public int hashCode() {
	int result = 17;
	result = 31 * result + (evaluationEventConfiguration == null ? 0 : evaluationEventConfiguration.hashCode());
	result = 31 * result + purpose.hashCode();
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (obj == this) {
	    return true;
	}
	if (!(obj instanceof EvaluationEventConfigurationTrigger)) {
	    return false;
	}
	EvaluationEventConfigurationTrigger other = (EvaluationEventConfigurationTrigger) obj;
	return (evaluationEventConfiguration == other.evaluationEventConfiguration || (evaluationEventConfiguration != null && evaluationEventConfiguration.equals(other.evaluationEventConfiguration)))
		&& purpose == other.purpose;
    }

    @Override
    public String toString() {
	StringBuilder builder = new StringBuilder();
	builder.append("DynamicTrigger [evaluationEventConfiguration=");
	builder.append(evaluationEventConfiguration);
	builder.append(", purpose=");
	builder.append(purpose);
	builder.append("]");
	return builder.toString();
    }

}
