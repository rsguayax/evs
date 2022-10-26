package es.grammata.evaluation.evs.mvc.controller.util;

import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import es.grammata.evaluation.evs.serializers.DateTimeUtcSerializer;

public class Journey {
	private Long id;
	private Date startDate;
	private Date endDate;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@JsonSerialize(using=DateTimeUtcSerializer.class)
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	@JsonSerialize(using=DateTimeUtcSerializer.class)
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
}
