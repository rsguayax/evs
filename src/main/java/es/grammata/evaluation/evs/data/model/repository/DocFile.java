package es.grammata.evaluation.evs.data.model.repository;

import java.io.File;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonIgnore;

import es.grammata.evaluation.evs.data.model.base.audit.AuditableEntity;


@Entity
@Table(name = "DOC_FILE")
public class DocFile extends AuditableEntity<Long> {
	
	@NotNull(message="El campo no puede estar vacío")
	private String evaluationEventCode;
	
	private String evaluationCenterCode;
	
	private String classroomCode;
	
	private Date startDateTest;
	
	private Date endDateTest;
	
	@NotNull(message="El campo no puede estar vacío")
	private String fileName;
	
	private String filePath;
	
	private String emailToNotify;
	
	
	public DocFile() {
	}
	
	
	public DocFile(String evaluationEventCode, String evaluationCenterCode, String classroomCode, Date startDateTest, 
			Date endDateTest, String fileName, String filePath, String emailToNotify) {
		this.evaluationEventCode = evaluationEventCode;
		this.evaluationCenterCode = evaluationCenterCode;
		this.classroomCode = classroomCode; 
		this.startDateTest = startDateTest;
		this.endDateTest = endDateTest;
		this.fileName = fileName;
		this.filePath = filePath;
		this.emailToNotify = emailToNotify;
	}
	
	public String getEvaluationEventCode() {
		return evaluationEventCode;
	}

	public void setEvaluationEventcode(String evaluationEventcode) {
		this.evaluationEventCode = evaluationEventcode;
	}

	public String getEvaluationCenterCode() {
		return evaluationCenterCode;
	}

	public void setEvaluationCenterCode(String evaluationCenterCode) {
		this.evaluationCenterCode = evaluationCenterCode;
	}

	public String getClassroomCode() {
		return classroomCode;
	}

	public void setClassroomCode(String classroomCode) {
		this.classroomCode = classroomCode;
	}

	public Date getStartDateTest() {
		return startDateTest;
	}

	public void setStartDateTest(Date startDateTest) {
		this.startDateTest = startDateTest;
	}

	public Date getEndDateTest() {
		return endDateTest;
	}

	public void setEndDateTest(Date endDateTest) {
		this.endDateTest = endDateTest;
	}
	

	public String getFileName() {
		return fileName;
	}


	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	
	public void setEvaluationEventCode(String evaluationEventCode) {
		this.evaluationEventCode = evaluationEventCode;
	}
	
	
	public String getFilePath() {
		return filePath;
	}


	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	public String getFullPath() {	
		return File.separator +  this.getFilePath();	
	}
	
	public String getFullFileName() {	
		return File.separator +  this.getFilePath() + this.fileName;	
	}



	@JsonIgnore
	public static String generateFileName(String evaluationEventCode, String evaluationCenterCode, 
			String classroomCode, String startDate, String endDate, String ext) {
		
		String fileName =  evaluationEventCode;
		String time = "";
		if(evaluationCenterCode != null) {
			fileName += "_" + evaluationCenterCode;
		} else {
			fileName += "";
		}
		if(classroomCode != null) {
			fileName += "_" + classroomCode;
		} else {
			fileName += "";
		}
		if(startDate != null && endDate != null)  {
			time = "_" + startDate + "-" + endDate;
			fileName += time;
		}
		
		// el timestamp permite que el fichero siga existiendo mientras se genera el nuevo
		Date date = new Date();
		fileName += "_" + date.getTime() + "." + ext;
		
		return fileName;
	}

}
