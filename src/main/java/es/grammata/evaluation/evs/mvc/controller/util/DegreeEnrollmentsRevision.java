package es.grammata.evaluation.evs.mvc.controller.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import es.grammata.evaluation.evs.data.model.custom.StatusEnrolled;
import es.grammata.evaluation.evs.data.model.repository.Degree;
import es.grammata.evaluation.evs.data.model.repository.Enrollment;
import es.grammata.evaluation.evs.data.model.repository.EnrollmentRevision;
import es.grammata.evaluation.evs.data.model.repository.EvaluationEventDegree;

public class DegreeEnrollmentsRevision {
	EvaluationEventDegree evaluationEventDegree;
	Map<Long, DegreeEnrollmentRevision> enrollments;

	public DegreeEnrollmentsRevision(EvaluationEventDegree evaluationEventDegree) {
		this.evaluationEventDegree = evaluationEventDegree;
	}

	public EvaluationEventDegree getEvaluationEventDegree() {
		return evaluationEventDegree;
	}

	public void setEvaluationEventDegree(EvaluationEventDegree evaluationEventDegree) {
		this.evaluationEventDegree = evaluationEventDegree;
	}

	public Degree getDegree() {
		return evaluationEventDegree.getDegree();
	}
	
	public DegreeEnrollmentRevision getDegreeEnrollmentRevision(Enrollment enrollment) {
		return enrollments.get(enrollment.getId());
	}

	public List<DegreeEnrollmentRevision> getEnrollments() {
		return new ArrayList<DegreeEnrollmentRevision>(enrollments.values());
	}

	public void setEnrollments(List<Enrollment> enrollments) {
		this.enrollments = new LinkedHashMap<Long, DegreeEnrollmentRevision>();
		for (Enrollment enrollment : enrollments) {
			this.enrollments.put(enrollment.getId(), new DegreeEnrollmentRevision(enrollment));
		}
		
		setEnrollmentsStatus();
	}
	
	public void setEnrollmentRevisions(List<EnrollmentRevision> enrollmentRevisions) {
		this.enrollments = new LinkedHashMap<Long, DegreeEnrollmentRevision>();
		for (EnrollmentRevision enrollmentRevision : enrollmentRevisions) {
			// Si la revisión de la inscripción tiene uno de los estados que se modifican manualmente, se los asignamos
			DegreeEnrollmentRevision degreeEnrollmentRevision = new DegreeEnrollmentRevision(enrollmentRevision.getEnrollment());
			if (enrollmentRevision.getStatus() == StatusEnrolled.MANUALLY_ADMITTED.status() || enrollmentRevision.getStatus() == StatusEnrolled.DENIED.status() || enrollmentRevision.getStatus() == StatusEnrolled.NOT_APPLICABLE.status()) {
				degreeEnrollmentRevision.setStatus(enrollmentRevision.getStatus());
				degreeEnrollmentRevision.setHasBeenModified(true);
			}
			
			this.enrollments.put(enrollmentRevision.getEnrollment().getId(), degreeEnrollmentRevision);
		}
		
		setEnrollmentsStatus();
	}
	
	public void setEnrollmentsStatus() {
		int quota = evaluationEventDegree.getQuota();
		Double cutOffGrade = evaluationEventDegree.getCut_off_grade();
		
		// Asignamos las plazas ADMITIDAS MANUALMENTE
		for (DegreeEnrollmentRevision degreeEnrollmentRevision : getEnrollments()) {
			if (degreeEnrollmentRevision.getStatus() == StatusEnrolled.MANUALLY_ADMITTED.status()) {
				quota -= 1;
			}
		}
		
		// Asignamos el resto de plazas por nota
		for (DegreeEnrollmentRevision degreeEnrollmentRevision : getEnrollments()) {
			if (degreeEnrollmentRevision.needToCheckStatus()) {
				if (quota > 0) {
					if (degreeEnrollmentRevision.getGrade() >= cutOffGrade) {
						degreeEnrollmentRevision.setStatus(StatusEnrolled.SUITABLE.status());
					} else {
						degreeEnrollmentRevision.setStatus(StatusEnrolled.ZERO_COURSE.status());
					}

					quota -= 1;
				} else {
					degreeEnrollmentRevision.setStatus(StatusEnrolled.UNSUITABLE.status());
				}
			}
		}
	}
	
	public boolean updateEnrollmentPriority2Status(DegreeEnrollmentRevision enrollmentPriority1, DegreeEnrollmentRevision enrollmentPriority2) {
		// Si Prioridad1 == APTO && Prioridad2 == APTO
		if (enrollmentPriority1.getStatus() == 1 && enrollmentPriority2.getStatus() == 1) {
			enrollmentPriority2.setStatus(StatusEnrolled.NOT_CONSUMED.status());
			enrollmentPriority2.setHasBeenModified(true);
			setEnrollmentsStatus();
			return true;
		}
		
		// Si Prioridad1 == APTO 0 && Prioridad2 == CURSO 0
		if (enrollmentPriority1.getStatus() == 1 && enrollmentPriority2.getStatus() == 3) {
			enrollmentPriority2.setStatus(StatusEnrolled.UNSUITABLE.status());
			enrollmentPriority2.setHasBeenModified(true);
			setEnrollmentsStatus();
			return true;
		}
		
		// Si Prioridad1 == CURSO 0 && Prioridad2 == CURSO 0
		if (enrollmentPriority1.getStatus() == 3 && enrollmentPriority2.getStatus() == 3) {
			enrollmentPriority2.setStatus(StatusEnrolled.UNSUITABLE.status());
			enrollmentPriority2.setHasBeenModified(true);
			setEnrollmentsStatus();
			return true;
		}
		
		return false;
	}
	
	public boolean updateEnrollmentPriority1Status(DegreeEnrollmentRevision enrollmentPriority1, DegreeEnrollmentRevision enrollmentPriority2) {
		// Si Prioridad1 == CURSO 0 && Prioridad2 == APTO
		if (enrollmentPriority1.getStatus() == 3 && enrollmentPriority2.getStatus() == 1) {
			enrollmentPriority1.setStatus(StatusEnrolled.UNSUITABLE.status());
			enrollmentPriority1.setHasBeenModified(true);
			setEnrollmentsStatus();
			return true;
		}
		
		return false;
	}
}
