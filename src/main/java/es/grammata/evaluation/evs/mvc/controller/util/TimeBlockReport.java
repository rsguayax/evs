package es.grammata.evaluation.evs.mvc.controller.util;

import java.io.Serializable;
import java.util.List;


public class TimeBlockReport implements Serializable {

	public String date;
	
	public String initDate;

	public String endDate;
	
	public List<Object[]> users;
	
	public TimeBlockReport() {
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getInitDate() {
		return initDate;
	}

	public void setInitDate(String initDate) {
		this.initDate = initDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public List<Object[]> getUsers() {
		return users;
	}

	public void setUsers(List<Object[]> users) {
		this.users = users;
	}
	
	
}
