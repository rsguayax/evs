package es.grammata.evaluation.evs.mvc.controller.util;

import java.util.ArrayList;
import java.util.List;

public class Messages {
	
	private List<Message> messages = new ArrayList<Message>();
	private Object data;
	public static final String TYPE_SUCCESS = "success";
	
	public List<Message> getMessages() {
		return messages;
	}
	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
}
