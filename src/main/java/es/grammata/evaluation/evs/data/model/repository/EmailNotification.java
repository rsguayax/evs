package es.grammata.evaluation.evs.data.model.repository;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import es.grammata.evaluation.evs.data.model.base.BaseModelEntity;

@Entity
@Table(name = "EMAIL_NOTIFICATIONS")
public class EmailNotification extends BaseModelEntity<Long> {

    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    private EvaluationEvent evaluationEvent;

    @NotNull(message = "El campo no puede estar vacío")
    private String token;

    @NotNull(message = "El campo no puede estar vacío")
    @DateTimeFormat(pattern="dd-MM-yyyy HH:mm")
    private Date sent;

    @DateTimeFormat(pattern="dd-MM-yyyy HH:mm")
    private Date read;

    public User getUser() {
	return user;
    }

    public void setUser(User test) {
	this.user = test;
    }

    public EvaluationEvent getEvaluationEvent() {
	return evaluationEvent;
    }

    public void setEvaluationEvent(EvaluationEvent evaluationEvent) {
	this.evaluationEvent = evaluationEvent;
    }

    public String getToken() {
	return token;
    }

    public void setToken(String token) {
	this.token = token;
    }

    public Date getSent() {
	return sent;
    }

    public void setSent(Date sent) {
	this.sent = sent;
    }

    public Date getRead() {
	return read;
    }

    public void setRead(Date read) {
	this.read = read;
    }

    @PrePersist
    public void prePersist() {
	this.read = null;
    }

}
