package es.grammata.evaluation.evs.data.model.repository;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import es.grammata.evaluation.evs.data.model.base.BaseModelEntity;

@Entity
@Table(name = "USER_PROFILE")
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
public class UserProfile extends BaseModelEntity<Long> {
	
	public static String DEFAULT = "default";

	@NotNull(message="El campo no puede estar vacío")
	@Column(unique=true)
	private String name;
	
	@NotNull(message="El campo no puede estar vacío")
	@Column(unique=true)
	private String code;
	
	@Column(columnDefinition="TEXT")
	private String description;
	
	@ManyToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)  
    @JoinTable(name="user_profile_permission", joinColumns=@JoinColumn(name="user_profile_id"), inverseJoinColumns=@JoinColumn(name="permission_id"))  
    private Set<Permission> permissions;
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<Permission> permissions) {
		this.permissions = permissions;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	
}
