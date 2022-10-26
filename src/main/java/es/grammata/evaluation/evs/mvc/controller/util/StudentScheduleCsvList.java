package es.grammata.evaluation.evs.mvc.controller.util;

import java.util.List;


public class StudentScheduleCsvList {

	private List<StudentScheduleCsvBean> studentsCsv;
	private int mattersNum;
	
	public StudentScheduleCsvList() {
	}

	public List<StudentScheduleCsvBean> getStudentsCsv() {
		return studentsCsv;
	}

	public void setStudentsCsv(List<StudentScheduleCsvBean> studentsCsv) {
		this.studentsCsv = studentsCsv;
	}

	public int getMattersNum() {
		return mattersNum;
	}

	public void setMattersNum(int mattersNum) {
		this.mattersNum = mattersNum;
	}
	
}
