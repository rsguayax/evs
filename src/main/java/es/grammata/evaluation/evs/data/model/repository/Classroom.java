package es.grammata.evaluation.evs.data.model.repository;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import es.grammata.evaluation.evs.data.model.base.BaseModelEntity;
import es.grammata.evaluation.evs.data.model.base.audit.AuditableEntity;


@Entity
@Table(name = "CLASSROOM")
public class Classroom extends AuditableEntity<Long> {
	
	@ManyToOne
    @PrimaryKeyJoinColumn
    private EvaluationCenter evaluationCenter;
	
	@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="cap_id")
    private Cap cap;
	
	@NotNull(message="El campo no puede estar vacío")
	@Size(min=1, max=256, message="El tamaño no es correcto")
	private String name;

	@NotNull(message="El campo no puede estar vacío")
	@Size(min=1, max=1024, message="El tamaño no es correcto")
	private String location;
	
	@NotNull(message="El campo no puede estar vacío")
	private Integer seats;
	
	private Boolean available;
	
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="net_id")
    private Net net;
	

	public EvaluationCenter getEvaluationCenter() {
		return evaluationCenter;
	}

	public void setEvaluationCenter(EvaluationCenter evaluationCenter) {
		this.evaluationCenter = evaluationCenter;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Integer getSeats() {
		return seats;
	}

	public void setSeats(Integer seats) {
		this.seats = seats;
	}
	
	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	public Cap getCap() {
		return cap;
	}

	public void setCap(Cap cap) {
		this.cap = cap;
	}

	public Net getNet() {
		return net;
	}

	public void setNet(Net net) {
		this.net = net;
	}
	
	
}