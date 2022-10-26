package es.grammata.evaluation.evs.mvc.controller.util;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import es.grammata.evaluation.evs.data.model.repository.User;
import siette.models.RangoProfesorAsignatura;

public class BankEditTeacherPeriod {

	public Integer id;
	public User teacher;
	@DateTimeFormat(pattern = "dd-MM-yyy HH:mm")
	public Date initDate;
	@DateTimeFormat(pattern = "dd-MM-yyy HH:mm")
	public Date endDate;

	public BankEditTeacherPeriod(){
		
	}
	public BankEditTeacherPeriod(BankEditTeacherPeriod period) {
		id = period.getId();
		teacher = new User();
		teacher.setUsername(period.getTeacher().getUsername());
		initDate = period.getInitDate();
		endDate = period.getEndDate();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public User getTeacher() {
		return teacher;
	}

	public void setTeacher(User teacher) {
		this.teacher = teacher;
	}

	public Date getInitDate() {
		return initDate;
	}

	public void setInitDate(Date initDate) {
		this.initDate = initDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
}
