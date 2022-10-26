package es.grammata.evaluation.evs.mvc.controller.util;

import java.io.Serializable;
import java.util.List;


public class ClassroomReport implements Serializable {

	public String name;
	
	public List<TimeBlockReport> timeblocks;

	
	public ClassroomReport() {
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public List<TimeBlockReport> getTimeblocks() {
		return timeblocks;
	}


	public void setTimeblocks(List<TimeBlockReport> timeblocks) {
		this.timeblocks = timeblocks;
	}

	
}
