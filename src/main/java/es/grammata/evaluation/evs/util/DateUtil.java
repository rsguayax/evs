package es.grammata.evaluation.evs.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

public class DateUtil {
	
	public static Date dateToUtcDate(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    	String localDate = formatter.format(date);
    	formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
    	Date utcDate = null;
    	try {
			utcDate = formatter.parse(localDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	
    	return utcDate;
	}
	
	public static Long getUtcTime(Date date) {
		return dateToUtcDate(date).getTime();
	}
	
	public static List<Date> getDaysBetweenDates(Date startdate, Date enddate)
	{
	    List<Date> dates = new ArrayList<>();
	    Calendar calendar = new GregorianCalendar();
	    calendar.setTime(startdate);

	    while (calendar.getTime().before(enddate))
	    {
	        Date result = calendar.getTime();
	        dates.add(result);
	        calendar.add(Calendar.DATE, 1);
	    }
	    return dates;
	}
	
	public static Date setTime(Date date, String time)
	{
		try {
			SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat sdfDatetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return sdfDatetime.parse(sdfDate.format(date) + " " + time);
		} catch (Exception e) {
			return null;
		}
	}
}
