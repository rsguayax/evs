package es.grammata.evaluation.evs.serializers;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

public class DateTimeUtcSerializer extends JsonSerializer<Date> {
	
    @Override
    public void serialize(Date value, JsonGenerator gen, SerializerProvider provider) throws IOException, JsonProcessingException {      
    	SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    	
    	String localDate = formatter.format(value);
    	formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
    	Date utcDate = null;
    	try {
			utcDate = formatter.parse(localDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	
    	gen.writeNumber(utcDate.getTime());
    }
}