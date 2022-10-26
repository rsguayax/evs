package es.grammata.evaluation.evs.data.model.repository;

import java.security.SecureRandom;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import es.grammata.evaluation.evs.data.model.base.BaseModelEntity;


@Entity
@Table(name = "MATTER_TEST_STUDENT", uniqueConstraints=@UniqueConstraint(columnNames = {"evaluationeventmattertest_id", "evaluationassignmentmatter_id"}))
public class MatterTestStudent extends BaseModelEntity<Long> {
	
	static final String PASS_CHARS = "abcdefghijklmnopqrstuvwxyz";
	
	static final String PASS_DIGITS = "0123456789";
	
	static final int PASS_CHARS_LENGTH = 3;
	
	static final int PASS_DIGITS_LENGTH = 2;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private EvaluationEventMatterTest evaluationEventMatterTest;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private EvaluationAssignmentMatter evaluationAssignmentMatter;
	
	@ManyToOne(fetch=FetchType.EAGER)
    private StudentEvaluation studentEvaluation;

	@OneToOne
	private Session session;
	
	@OneToOne
	private AdmissionGrade admissionGrade;
	
	private String externalPassword;
	

	public EvaluationEventMatterTest getEvaluationEventMatterTest() {
		return evaluationEventMatterTest;
	}

	public void setEvaluationEventMatterTest(
			EvaluationEventMatterTest evaluationEventMatterTest) {
		this.evaluationEventMatterTest = evaluationEventMatterTest;
	}

	public EvaluationAssignmentMatter getEvaluationAssignmentMatter() {
		return evaluationAssignmentMatter;
	}

	public void setEvaluationAssignmentMatter(
			EvaluationAssignmentMatter evaluationAssignmentMatter) {
		this.evaluationAssignmentMatter = evaluationAssignmentMatter;
	}

	public StudentEvaluation getStudentEvaluation() {
		return studentEvaluation;
	}

	public void setStudentEvaluation(StudentEvaluation studentEvaluation) {
		this.studentEvaluation = studentEvaluation;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}
	
	public AdmissionGrade getAdmissionGrade() {
		return admissionGrade;
	}

	public void setAdmissionGrade(AdmissionGrade admissionGrade) {
		this.admissionGrade = admissionGrade;
	}

	public String getExternalPassword() {
		return externalPassword;
	}

	public void setExternalPassword(String externalPassword) {
		this.externalPassword = externalPassword;
	}

	@PrePersist
	private void onPrePersist() {
		SecureRandom rnd = new SecureRandom();

		StringBuilder sb = new StringBuilder(PASS_CHARS_LENGTH + PASS_DIGITS_LENGTH);
		for (int i = 0; i < PASS_CHARS_LENGTH; i++) {
			sb.append(PASS_CHARS.charAt(rnd.nextInt(PASS_CHARS.length())));
		}
		for (int i = 0; i < PASS_DIGITS_LENGTH; i++) {
			sb.append(PASS_DIGITS.charAt(rnd.nextInt(PASS_DIGITS.length())));
		}
		
		this.setExternalPassword(sb.toString());
	}
	
}
