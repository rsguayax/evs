package es.grammata.evaluation.evs.mvc.controller.util;

public class Message {
	
	private String type;
	private String message;
	private int error;
	private Object data;
	
	public static final String TYPE_ERROR = "danger";
	public static final String TYPE_WARNING = "warning";
	public static final String TYPE_SUCCESS = "success";
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getError() {
		return error;
	}
	public void setError(int error) {
		this.error = error;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
}
