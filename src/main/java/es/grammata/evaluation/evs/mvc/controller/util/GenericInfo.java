package es.grammata.evaluation.evs.mvc.controller.util;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.json.JSONObject;

@JsonSerialize(using=GenericInfoSerializer.class)
public class GenericInfo {
	
	private JSONObject genericInfo;
	
	public GenericInfo() {
		genericInfo = new JSONObject();
	}
	
	public GenericInfo(JSONObject genericInfoJson) {
		genericInfo = genericInfoJson;
	}

	public JSONObject getGenericInfo() {
		return genericInfo;
	}

	public void setGenericInfo(JSONObject genericInfo) {
		this.genericInfo = genericInfo;
	}
	
	public Object get(String key) {
		return genericInfo.get(key);
	}
	
	public Object put(String key, Object value) {
		return genericInfo.put(key, value);
	}
}
