package es.grammata.evaluation.evs.mvc.controller.util;

import java.io.Serializable;
import java.util.List;


public class CenterReport implements Serializable {

	public String name;
	
	public List<ClassroomReport> classrooms;
	
	public CenterReport() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ClassroomReport> getClassrooms() {
		return classrooms;
	}

	public void setClassrooms(List<ClassroomReport> classrooms) {
		this.classrooms = classrooms;
	}
}
