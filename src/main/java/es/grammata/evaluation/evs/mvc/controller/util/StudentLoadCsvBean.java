package es.grammata.evaluation.evs.mvc.controller.util;

import java.util.Date;

import es.grammata.evaluation.evs.util.StringUtil;

public class StudentLoadCsvBean {
	
	/*1.	Identificador de alumno.
	2.	Código de centro de evaluación.
	3.	Período académico.
	4.	Modalidad.
	5.	Código de asignatura.
	6.	Nombre asignatura.
	7.	Código de aula: vacía
	8.	Sistema de conexión: vacío.
	9.	Fecha de tests: vacía.
	10.	Hora de inicio: vacía.
	11.	Hora de fin: vacía.
	12.	Código del servidor: vacía.*/
	
	public static final String NET_CON_TYPE = "RED";
	public static final String CAP_CON_TYPE = "CAP";

	private String identification;
	private String evaluationCenterCode;
	private String academicPeriodCode;
	private String mode;
	private String matterCode;
	private String matterName;
	private String classroomCode;
	private String connection;
	private Date testDate;
	private String startTime;
	private String endTime;
	private String serverCode;

	public StudentLoadCsvBean() {
	}

	public StudentLoadCsvBean(String identification,
			String evaluationCenterCode, String academicPeriodCode,
			String mode, String matterCode, String matterName, String classroomCode,
			String connection, Date testDate, String startTime, String endTime,
			String serverCode) {
		this.identification = identification;
		this.evaluationCenterCode = evaluationCenterCode;
		this.academicPeriodCode = academicPeriodCode;
		this.mode = mode;
		this.matterCode = matterCode;
		this.matterName = matterName;
		this.classroomCode = classroomCode;
		this.connection = connection;
		this.testDate = testDate;
		this.startTime = startTime;
		this.endTime = endTime;
		this.serverCode = serverCode;
	}

	public String getIdentification() {
		return identification;
	}

	public void setIdentification(String identification) {
		this.identification = identification;
	}

	public String getEvaluationCenterCode() {
		return evaluationCenterCode;
	}

	public void setEvaluationCenterCode(String evaluationCenterCode) {
		this.evaluationCenterCode = evaluationCenterCode;
	}

	public String getAcademicPeriodCode() {
		return academicPeriodCode;
	}

	public void setAcademicPeriodCode(String academicPeriodCode) {
		this.academicPeriodCode = academicPeriodCode;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getMatterName() {
		return StringUtil.dropAccentMark(matterName);
	}

	public void setMatterName(String matterName) {
		this.matterName = matterName;
	}

	public String getClassroomCode() {
		return classroomCode;
	}

	public void setClassroomCode(String classroomCode) {
		this.classroomCode = classroomCode;
	}

	public String getConnection() {
		return connection;
	}

	public void setConnection(String connection) {
		this.connection = connection;
	}

	public Date getTestDate() {
		return testDate;
	}

	public void setTestDate(Date testDate) {
		this.testDate = testDate;
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

	public String getMatterCode() {
		return matterCode;
	}

	public void setMatterCode(String matterCode) {
		this.matterCode = matterCode;
	}


	
}
