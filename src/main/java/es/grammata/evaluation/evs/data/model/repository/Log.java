package es.grammata.evaluation.evs.data.model.repository;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

import es.grammata.evaluation.evs.data.model.base.audit.AuditableEntity;

@MappedSuperclass
public class Log extends AuditableEntity<Long> {
	
	public static String TYPE_ERROR = "ERROR";
	public static String TYPE_INFO = "INFO";
	public static String TYPE_WARNING = "WARNING";
	
	@NotNull(message="El campo no puede estar vacío")
	private String type;
	
	@NotNull(message="El campo no puede estar vacío")
	@Column(columnDefinition="TEXT")
	private String message;
	
	@Column(columnDefinition="TEXT")
	private String extra;
	
	private boolean read;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
	}

	public boolean isRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}
}
