package es.grammata.evaluation.evs.mvc.controller.util;

import java.util.Date;


/*
1.	Identificador de alumno.
2.	Código de centro de evaluación.
3.	Período académico.
4.	Modalidad.
5.	Tipo de evaluación.
6.	Número de asignaturas
7.	A continuación hay una cantidad de columnas variable indicado por el número de asignaturas que contendrán los nombres de asignatura de las que ha de examinarse el alumno.
8.	Código de aula: vacía
9.	Sistema de conexión: vacío.
10.	Fecha de tests: vacía.
11.	Hora de inicio: vacía.
12.	Hora de fin: vacía.
13.	Código del servidor: vacía. 
 
 */
public class StudentScheduleCsvBean {

	private String identifier;
	private String evaluationCenterCode;
	private String academicPeriod;
	private String mode;
	private String evaluationType;
	private int mattersNumber;
	private String[] matters;
	private String classroomCode;
	private String connectionType;
	private Date testsDate;
	private String startTime;
	private String endTime;
	private String serverCode;
	
	
	
	
	public StudentScheduleCsvBean() {
	}

	public StudentScheduleCsvBean(String identifier, String evaluationCenterCode, 
			String academicPeriod, String mode, String evaluationType, int mattersNumber, String[] matters, 
			String classroomCode, String connectionType, Date testsDate, String startTime, String endTime, String serverCode) {
		this.identifier = identifier;
		this.evaluationCenterCode = evaluationCenterCode;
		this.academicPeriod = academicPeriod;
		this.mode = mode;
		this.evaluationType = evaluationType;
		this.mattersNumber = mattersNumber;
		this.matters = matters;
		this.classroomCode = classroomCode;
		this.connectionType = connectionType;
		this.testsDate = testsDate;
		this.startTime = startTime;
		this.endTime = endTime;
		this.serverCode = serverCode;
		
	}
	

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getEvaluationCenterCode() {
		return evaluationCenterCode;
	}

	public void setEvaluationCenterCode(String evaluationCenterCode) {
		this.evaluationCenterCode = evaluationCenterCode;
	}

	public String getAcademicPeriod() {
		return academicPeriod;
	}

	public void setAcademicPeriod(String academicPeriod) {
		this.academicPeriod = academicPeriod;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getEvaluationType() {
		return evaluationType;
	}

	public void setEvaluationType(String evaluationType) {
		this.evaluationType = evaluationType;
	}

	public int getMattersNumber() {
		return mattersNumber;
	}

	public void setMattersNumber(int mattersNumber) {
		this.mattersNumber = mattersNumber;
	}

	public String[] getMatters() {
		return matters;
	}

	public void setMatters(String[] matters) {
		this.matters = matters;
	}

	public String getClassroomCode() {
		return classroomCode;
	}

	public void setClassroomCode(String classroomCode) {
		this.classroomCode = classroomCode;
	}

	public String getConnectionType() {
		return connectionType;
	}

	public void setConnectionType(String connectionType) {
		this.connectionType = connectionType;
	}

	public Date getTestsDate() {
		return testsDate;
	}

	public void setTestsDate(Date testsDate) {
		this.testsDate = testsDate;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getServerCode() {
		return serverCode;
	}

	public void setServerCode(String serverCode) {
		this.serverCode = serverCode;
	}

	@Override
	public String toString() {
		return "StudentCsv{" + "id=" + this.identifier + "'}'";
	}
}
