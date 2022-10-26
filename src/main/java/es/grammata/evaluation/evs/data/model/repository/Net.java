package es.grammata.evaluation.evs.data.model.repository;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.codehaus.jackson.annotate.JsonIgnore;

import es.grammata.evaluation.evs.data.model.base.BaseModelEntity;


@Entity
@Table(name = "NET")
public class Net extends BaseModelEntity<Long> {
	
	@ManyToOne
    @PrimaryKeyJoinColumn
    private EvaluationCenter evaluationCenter;
	
	@NotNull(message="El campo no puede estar vacío")
	@Size(min=2, max=512, message="El tamaño no es correcto")
	private String name;
	
	@NotNull(message="El campo no puede estar vacío")
	@Size(min=2, max=124, message="El tamaño no es correcto")
	@Column(unique=true)
	private String code;
	
	@NotNull(message="El campo no puede estar vacío")
	@Size(min=2, max=124, message="El tamaño no es correcto")
	private String password;
	
	@ManyToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
    @JoinTable(name="net_server", joinColumns=@JoinColumn(name="net_id_id"), inverseJoinColumns=@JoinColumn(name="server_id"))
    private Set<Server> servers;
	

	@JsonIgnore
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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<Server> getServers() {
		return servers;
	}

	public void setServers(Set<Server> servers) {
		this.servers = servers;
	}

	
}
