package es.grammata.evaluation.evs.data.model.repository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.validator.constraints.NotEmpty;

import es.grammata.evaluation.evs.data.model.base.audit.AuditableEntity;


@Entity
@Table(name = "EVALUATION_CENTER")
public class EvaluationCenter extends AuditableEntity<Long> {

	@NotNull(message="El campo no puede estar vacío")
	@NotEmpty(message="El campo no puede estar vacío")
	@Size(min=0, max=256, message="El tamaño no es correcto")
	@Column(unique=true)
	private String name;
	
	@NotNull(message="El campo no puede estar vacío")
	@NotEmpty(message="El campo no puede estar vacío")
	@Size(min=0, max=256, message="El tamaño no es correcto")
	@Column(unique=true)
	private String code;
	
	@OneToMany(mappedBy="evaluationCenter", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
	@OrderBy("name asc")  
	private Set<Center> registrationCenters;
	
	@Size(min=0, max=1024, message="El tamaño no es correcto")
	private String description;
	
	@OneToMany(mappedBy="evaluationCenter", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
	private Set<Address> addresses;
	
	@OneToMany(mappedBy="evaluationCenter", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
	@OrderBy("id")
	private Set<Classroom> classrooms;
	
	@OneToMany(mappedBy="evaluationCenter", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
	private Set<Cap> caps;
	
	@OneToMany(mappedBy="evaluationCenter", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
	private Set<Net> nets;
	
	
	@Column(columnDefinition="TEXT")
	@Size(min=0, max=100, message="El tamaño no es correcto")
	private String transport; 
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@JsonIgnore
	public Set<Address> getAddresses() {
		return addresses;
	}

	public void setAddresses(Set<Address> addresses) {
		this.addresses = addresses;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@JsonIgnore
	public Set<Classroom> getClassrooms() {
		return classrooms;
	}

	public void setClassrooms(Set<Classroom> classrooms) {
		this.classrooms = classrooms;
	}
	
	@JsonIgnore
	public Set<Cap> getCaps() {
		return caps;
	}

	public void setCaps(Set<Cap> caps) {
		this.caps = caps;
	}

	public String getTransport() {
		return transport;
	}

	public void setTransport(String transport) {
		this.transport = transport;
	}

	@JsonIgnore
	public Set<Center> getRegistrationCenters() {
		return registrationCenters;
	}
	
	public Set<Net> getNets() {
		return nets;
	}

	public void setNets(Set<Net> nets) {
		this.nets = nets;
	}

	public void setRegistrationCenters(Set<Center> registrationCenters) {
		this.registrationCenters = registrationCenters;
	}
	
	@JsonIgnore
	public List<Classroom> getAvailableClassrooms() {
		List<Classroom> availableClassrooms = new ArrayList<Classroom>();
		for (Classroom classroom : classrooms) {
			if (classroom.isAvailable()) {
				availableClassrooms.add(classroom);
			}
		}
		
		return availableClassrooms;
	}
}
