package es.grammata.evaluation.evs.data.model.repository;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.codehaus.jackson.annotate.JsonIgnore;

import es.grammata.evaluation.evs.data.model.base.BaseModelEntity;


@Entity
@Table(name = "SERVER")
public class Server extends BaseModelEntity<Long> {
	
	@NotNull(message="El campo no puede estar vacío")
	@Size(min=2, max=512, message="El tamaño no es correcto")
	private String name;
	
	@NotNull(message="El campo no puede estar vacío")
	@Size(min=2, max=124, message="El tamaño no es correcto")
	@Column(unique=true)
	private String code;
	
	@NotNull(message="El campo no puede estar vacío")
	@Size(min=2, max=512, message="El tamaño no es correcto")
	private String url;
	


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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Server server = (Server) o;
		return Objects.equals(code, server.code);
	}

	@Override
	public int hashCode() {
		return Objects.hash(code);
	}
}
