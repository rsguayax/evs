package es.grammata.evaluation.evs.mvc.controller.util;

import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.springframework.format.annotation.DateTimeFormat;

public class BankEditPeriod {

	public Integer id;
	
	public Date initDate;

	public Date endDate;
	
	public BankEditPeriod() {
	}
	
	public BankEditPeriod(BankEditPeriod period) {
		id = period.getId();
		initDate = period.getInitDate();
		endDate = period.getEndDate();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getInitDate() {
		return initDate;
	}

	@JsonDeserialize(using = FormatDateDeserializer.class)
	public void setInitDate(Date initDate) {
		this.initDate = initDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	@JsonDeserialize(using = FormatDateDeserializer.class)
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
}
