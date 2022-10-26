package es.grammata.evaluation.evs.mvc.controller.util;

import java.util.Date;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

import es.grammata.evaluation.evs.data.model.repository.Degree;

public class DegreeFacahada2 {
	
	@JsonProperty(value = "active")
	private boolean Active;
	
	@JsonProperty(value = "created")
	private Date Created;
	@JsonProperty(value = "created_By")
	private Integer CreatedBy;
	@JsonProperty(value = "id")
	private Integer Id;
	@JsonProperty(value = "modified")
	private Date Modified;
	@JsonProperty(value = "modified_By")
	private Integer ModifiedBy;
	@JsonProperty(value = "name")
	private String Name;
	@JsonProperty(value = "code")
	private String Code;
	@JsonProperty(value = "mode")
	private String Mode;
	@JsonProperty(value = "academmicLevel")
	private String AcademicLevel;
	public boolean isActive() {
		return Active;
	}
	public void setActive(boolean active) {
		Active = active;
	}
	public Date getCreated() {
		return Created;
	}
	public void setCreated(Date created) {
		Created = created;
	}
	public Integer getCreatedBy() {
		return CreatedBy;
	}
	public void setCreatedBy(Integer createdBy) {
		CreatedBy = createdBy;
	}
	public Integer getId() {
		return Id;
	}
	public void setId(Integer id) {
		Id = id;
	}
	public Date getModified() {
		return Modified;
	}
	public void setModified(Date modified) {
		Modified = modified;
	}
	public Integer getModifiedBy() {
		return ModifiedBy;
	}
	public void setModifiedBy(Integer modifiedBy) {
		ModifiedBy = modifiedBy;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getCode() {
		return Code;
	}
	public void setCode(String code) {
		Code = code;
	}
	public String getMode() {
		return Mode;
	}
	public void setMode(String mode) {
		Mode = mode;
	}
	public String getAcademicLevel() {
		return AcademicLevel;
	}
	public void setAcademicLevel(String academicLevel) {
		AcademicLevel = academicLevel;
	}
}
