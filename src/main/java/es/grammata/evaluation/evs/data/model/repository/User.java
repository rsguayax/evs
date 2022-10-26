package es.grammata.evaluation.evs.data.model.repository;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

import es.grammata.evaluation.evs.data.model.base.BaseModelEntity;


@Entity
@Table(name = "EVS_USER")
public class User extends BaseModelEntity<Long> {

	@Column(unique=true)
	//@NotNull(message="El campo no puede estar vacío")
	private String username;

	private String password;

	//@NotNull(message="El campo no puede estar vacío")
	
	private Integer enabled;

	@NotNull(message="El campo no puede estar vacío")
	private String firstName;

	@NotNull(message="El campo no puede estar vacío")
	private String lastName;

	private String email;

	private String externalId;

	private String identification;
	
	@Transient
	private String fullName;
	
	@Transient
	private String fullNameForListings;

	@ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "USER_ROLE",
             joinColumns = { @JoinColumn(name = "user_id") },
             inverseJoinColumns = { @JoinColumn(name = "role_id") })
	private Set<Role> roles = new HashSet<Role>();


	@ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "USER_USER_PROFILE",
             joinColumns = { @JoinColumn(name = "user_id") },
             inverseJoinColumns = { @JoinColumn(name = "user_profile_id") })
	private Set<UserProfile> userProfiles = new HashSet<UserProfile>();


	@ManyToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
    @JoinTable(name="user_permission", joinColumns=@JoinColumn(name="user_id"), inverseJoinColumns=@JoinColumn(name="permission_id"))
    private Set<Permission> permissions;


	/*@OneToMany(mappedBy="enrollment", cascade=CascadeType.REMOVE, fetch=FetchType.EAGER)
    private Set<Enrollment> enrollments;
*/
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@JsonIgnore
	public String getPassword() {
		return password;
	}

	@JsonProperty
	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getEnabled() {
		return enabled;
	}

	public void setEnabled(Integer enabled) {
		this.enabled = enabled;
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

	@JsonProperty
	public String getFullName() {
	    StringBuilder sb = new StringBuilder();
	    if (firstName != null && !firstName.isEmpty()) {
	    	sb.append(firstName);
	    }
	    if (lastName != null && !lastName.isEmpty()) {
			if (sb.length() > 0) {
			    sb.append(" ");
			}
			sb.append(lastName);
	    }
	    return sb.toString();
	}
	
	@JsonIgnore
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	@JsonIgnore
	public void setFullNameForListings(String fullNameForListings) {
		this.fullNameForListings = fullNameForListings;
	}

	@JsonProperty
	public String getFullNameForListings() {
	    StringBuilder sb = new StringBuilder();
	    if (lastName != null && !lastName.isEmpty()) {
	    	sb.append(lastName);
	    }
	    if (firstName != null && !firstName.isEmpty()) {
			if (sb.length() > 0) {
			    sb.append(", ");
			}
			sb.append(firstName);
	    }
	    return sb.toString();
	}
	
	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	public String getIdentification() {
		return identification;
	}

	public void setIdentification(String identification) {
		this.identification = identification;
	}

	@JsonIgnore
	public Set<UserProfile> getUserProfiles() {
		return userProfiles;
	}

	public void setUserProfiles(Set<UserProfile> userProfiles) {
		this.userProfiles = userProfiles;
	}

	@JsonIgnore
	public Set<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<Permission> permissions) {
		this.permissions = permissions;
	}

	@Override
	public String toString() {
		return "User [username=" + username + ", password=" + password + ", enabled=" + enabled + ", firstName="
				+ firstName + ", lastName=" + lastName + ", email=" + email + ", externalId=" + externalId
				+ ", identification=" + identification + ", fullName=" + fullName + ", fullNameForListings="
				+ fullNameForListings + ", roles=" + roles + ", userProfiles=" + userProfiles + ", permissions="
				+ permissions + "]";
	}
	
	/*
	@JsonIgnore
	public Set<Enrollment> getEnrollments() {
		return enrollments;
	}

	public void setEnrollments(Set<Enrollment> enrollments) {
		this.enrollments = enrollments;
	}*/
	


}
