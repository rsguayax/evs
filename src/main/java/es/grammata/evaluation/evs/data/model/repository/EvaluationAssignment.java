package es.grammata.evaluation.evs.data.model.repository;

import java.security.SecureRandom;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.codehaus.jackson.annotate.JsonIgnore;

import sun.misc.BASE64Decoder;

import es.grammata.evaluation.evs.data.model.base.BaseModelEntity;
import es.grammata.evaluation.evs.data.security.AESCrypto;


@Entity
@Table(name = "EVALUATION_ASSIGNMENT", uniqueConstraints=@UniqueConstraint(columnNames = {"evaluationevent_id", "user_id"}))
public class EvaluationAssignment extends BaseModelEntity<Long> {
	
	static final String PASS_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	
	static final int PASS_LENGTH = 6;

	
	
	@ManyToOne(fetch = FetchType.EAGER)
	private EvaluationEvent evaluationEvent;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private User user;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private StudentType studentType;
	
	private String externalPassword;

	@JsonIgnore
	public EvaluationEvent getEvaluationEvent() {
		return evaluationEvent;
	}

	public void setEvaluationEvent(EvaluationEvent evaluationEvent) {
		this.evaluationEvent = evaluationEvent;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public StudentType getStudentType() {
		return studentType;
	}

	public void setStudentType(StudentType studentType) {
		this.studentType = studentType;
	}

	@JsonIgnore
	public String getExternalPassword() {
		String aux = externalPassword;
		if(externalPassword != null) {
			try {
				aux = AESCrypto.decrypt(externalPassword);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return aux;
	}

	@JsonIgnore
	public void setExternalPassword(String externalPassword) {
		this.externalPassword = externalPassword;
	}
	
	@PrePersist
	private void onPrePersist() {
		SecureRandom rnd = new SecureRandom();

		StringBuilder sb = new StringBuilder(PASS_LENGTH);
		for (int i = 0; i < PASS_LENGTH; i++) {
			sb.append(PASS_CHARS.charAt(rnd.nextInt(PASS_CHARS.length())));
		}
		
		String cipherText = "";
		try {
			cipherText = AESCrypto.encrypt(sb.toString());
		} catch (Exception ex) {
			cipherText = "ERROR_AL_GENERAR_CONTRASENA";
			ex.printStackTrace();
		}
		
		
		this.setExternalPassword(cipherText);
	}

}
