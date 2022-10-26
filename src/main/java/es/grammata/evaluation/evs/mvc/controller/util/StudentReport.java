package es.grammata.evaluation.evs.mvc.controller.util;

import java.io.Serializable;


public class StudentReport implements Serializable {
	
	private Integer cont;
	private String identification;
	private String fullName;
	private String password;
	public Integer getCont() {
		return cont;
	}
	public void setCont(Integer cont) {
		this.cont = cont;
	}
	public String getIdentification() {
		return identification;
	}
	public void setIdentification(String identification) {
		this.identification = identification;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

}
