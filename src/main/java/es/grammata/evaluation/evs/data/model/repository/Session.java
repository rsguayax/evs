package es.grammata.evaluation.evs.data.model.repository;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import es.grammata.evaluation.evs.data.model.base.BaseModelEntity;

@Entity
@Table(name = "SESSION")
public class Session extends BaseModelEntity<Long> {
	@NotNull
	private Integer nSuccesses;
	
	@NotNull
	private Integer nFails;
	
	@NotNull
	private Integer nBlanks;
	
	@NotNull
	private Float rate;
	
	private String state;
	
	private Long externalId;
	
	private String urlToken;
	
	private Date testDate;
	
	
	public static final String STATE_PUBLISHED = "PUBLICADO";
	
	public static final String STATE_EVALUABLE = "EVALUABLE";
	
	public static final String STATE_NOTIFIED = "NOTIFICADO";
	
	public static final String STATE_MODIFIED = "RECALIFICADO";
		
	
	public Integer getnSuccesses() {
		return nSuccesses;
	}

	public void setnSuccesses(Integer nSuccesses) {
		this.nSuccesses = nSuccesses;
	}

	public Integer getnFails() {
		return nFails;
	}

	public void setnFails(Integer nFails) {
		this.nFails = nFails;
	}

	public Integer getnBlanks() {
		return nBlanks;
	}

	public void setnBlanks(Integer nBlanks) {
		this.nBlanks = nBlanks;
	}

	public Float getRate() {
		return rate;
	}

	public void setRate(Float rate) {
		this.rate = rate;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Long getExternalId() {
		return externalId;
	}

	public void setExternalId(Long externalId) {
		this.externalId = externalId;
	}

	public String getUrlToken() {
		return urlToken;
	}

	public void setUrlToken(String urlToken) {
		this.urlToken = urlToken;
	}

	public Date getTestDate() {
		return testDate;
	}

	public void setTestDate(Date testDate) {
		this.testDate = testDate;
	}
	
}
