package es.grammata.evaluation.evs.mvc.controller.util;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class EnrollmentFachada {
	
	@JsonProperty(value = "degrees")
	private List<DegreeF> degrees;
	@JsonProperty(value = "firstname")
	private String firstName;
	@JsonProperty(value = "lastname")
	private String lastName;
	@JsonProperty(value = "email")
	private String email;
	@JsonProperty(value = "identification")
	private String identification;
	@JsonProperty(value = "evaluationCenterId")
	private Long evaluationCenterId;
	
	public List<DegreeF> getDegrees() {
		return degrees;
	}
	public void setDegrees(List<DegreeF> degrees) {
		this.degrees = degrees;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getIdentification() {
		return identification;
	}
	public void setIdentification(String identification) {
		this.identification = identification;
	}
	public Long getEvaluationCenterId() {
		return evaluationCenterId;
	}
	public void setEvaluationCenterId(Long evaluationCenterId) {
		this.evaluationCenterId = evaluationCenterId;
	}
	
	public boolean hasValidData() {
		return getErrorMessage().length() == 0 ? true : false;
	}
	
	public String getErrorMessage() {
		String errorMessage = "";
		if (identification == null || identification.trim().length() == 0) {
			errorMessage += "<li>Introduzca la identificación</li>";
		}
		
		if (firstName == null || firstName.trim().length() == 0) {
			errorMessage += "<li>Introduzca el nombre</li>";
		}
		
		if (lastName == null || lastName.trim().length() == 0) {
			errorMessage += "<li>Introduzca los apellidos</li>";
		}

		if (evaluationCenterId == null || evaluationCenterId == 0) {
			errorMessage += "<li>Seleccione el centro de evaluación</li>";
		}
		
		if (degrees == null || degrees.size() == 0) {
			errorMessage += "<li>Seleccione al menos una titulación</li>";
		}
		
		if (errorMessage.length() > 0) {
			errorMessage = "<ul>" + errorMessage + "</ul>";
		}
		
		return errorMessage;
	}
}
