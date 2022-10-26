package es.grammata.evaluation.evs.mvc.controller.util;

import es.grammata.evaluation.evs.data.model.custom.StatusEnrolled;
import es.grammata.evaluation.evs.data.model.repository.Enrollment;
import es.grammata.evaluation.evs.data.model.repository.User;
import es.grammata.evaluation.evs.util.EnrollmentUtils;

public class DegreeEnrollmentRevision {
	private Long enrollmentId;
	private User user;
	private Integer priority;
	private Double grade;
	private int status;
	private boolean hasBeenModified;
	
	public DegreeEnrollmentRevision(Enrollment enrollment) {
		this.enrollmentId = enrollment.getId();
		this.user = enrollment.getUser();
		this.priority = enrollment.getPriority();
		this.grade = enrollment.getGrade();
		this.hasBeenModified = false;
	}

	public Long getEnrollmentId() {
		return enrollmentId;
	}

	public void setEnrollmentId(Long enrollmentId) {
		this.enrollmentId = enrollmentId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public Double getGrade() {
		return grade;
	}

	public void setGrade(Double grade) {
		this.grade = grade;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	public boolean isHasBeenModified() {
		return hasBeenModified;
	}

	public void setHasBeenModified(boolean hasBeenModified) {
		this.hasBeenModified = hasBeenModified;
	}
	
	public boolean needToCheckStatus() {
		if (hasBeenModified) {
			return false;
		}
		
		return true;
	}

	public String getStatusString() {
		return EnrollmentUtils.getStatusString(status);
	}
}
