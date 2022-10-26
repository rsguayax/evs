package es.grammata.evaluation.evs.mvc.controller.util;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Iterator;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.ser.SerializerBase;
import org.json.JSONArray;
import org.json.JSONObject;

public class GenericInfoSerializer extends SerializerBase<GenericInfo> {
	
	public GenericInfoSerializer() {
	    super(GenericInfo.class, true);
	}

	@Override
	public void serialize(GenericInfo value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
		JSONObject genericInfo = value.getGenericInfo();
		Iterator<String> iter = genericInfo.keys();
		
		jgen.writeStartObject();
		while (iter.hasNext()) {
		    String key = iter.next();
		    writeObject(key, genericInfo.get(key), jgen);
		}
		jgen.writeEndObject();
	}
	
	public void writeObject(String key, Object object, JsonGenerator jgen) throws JsonGenerationException, IOException {
		if (object instanceof JSONArray) {
	    	JSONArray array = (JSONArray) object;
	    	jgen.writeArrayFieldStart(key);
	    	
	    	for (int i=0; i<array.length(); i++) {
	    		JSONObject jsonObject = array.getJSONObject(i);
	    		Iterator<String> iter = jsonObject.keys();
	    		
	    		jgen.writeStartObject();
	    		while (iter.hasNext()) {
	    		    String key2 = iter.next();
	    		    writeObject(key2, jsonObject.get(key2), jgen);
	    		}
	    		jgen.writeEndObject();
    		}
	    	
	    	jgen.writeEndArray();
	    } else if (object instanceof JSONObject) {
	    	JSONObject jsonObject = (JSONObject) object;
    		Iterator<String> iter = jsonObject.keys();
    		
    		jgen.writeObjectFieldStart(key);;
    		while (iter.hasNext()) {
    		    String key2 = iter.next();
    		    writeObject(key2, jsonObject.get(key2), jgen);
    		}
    		jgen.writeEndObject();
	    } else {
	    	jgen.writeObjectField(key, object);
	    }
	}

	@Override
	public JsonNode getSchema(SerializerProvider provider, Type typeHint) throws JsonMappingException {
		return null;
	}
}
