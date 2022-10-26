package es.grammata.evaluation.evs.mvc.controller.util;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


public class StudentListElementReport implements Serializable {
	
	private String centerName;

	private String classRoomName;
	
	private String dateStr;
	
	private String initDateStr;

	private String endDateStr;
	
	private Date date;
	
	private String cityName;
	
	private List<Object[]> users;
	
	private List<StudentExtendedDocReport> extendedUsersInfo;
	

	public String getClassRoomName() {
		return classRoomName;
	}

	public void setClassRoomName(String classRoomName) {
		this.classRoomName = classRoomName;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public List<Object[]> getUsers() {
		return users;
	}

	public void setUsers(List<Object[]> users) {
		this.users = users;
	}

	public String getCenterName() {
		return centerName;
	}

	public void setCenterName(String centerName) {
		this.centerName = centerName;
	}

	public String getDateStr() {
		return dateStr;
	}

	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}

	public String getInitDateStr() {
		return initDateStr;
	}

	public void setInitDateStr(String initDateStr) {
		this.initDateStr = initDateStr;
	}

	public String getEndDateStr() {
		return endDateStr;
	}

	public void setEndDateStr(String endDateStr) {
		this.endDateStr = endDateStr;
	}

	public List<StudentExtendedDocReport> getExtendedUsersInfo() {
		return extendedUsersInfo;
	}
	
	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public void setExtendedUsersInfo(
			List<StudentExtendedDocReport> extendedUsersInfo) {
		this.extendedUsersInfo = extendedUsersInfo;
	}
}
