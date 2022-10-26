package es.grammata.evaluation.evs.data.model.custom;

public class Qualification {
	
	private Long subjectId;
	private double grade;
	private double weight;
	private double gradeTotal;

	public Qualification() {
		// TODO Auto-generated constructor stub
	}
				
	public Qualification(Long subjectId, double grade, double weight, double gradeTotal) {
		super();
		this.subjectId = subjectId;
		this.grade = grade;
		this.weight = weight;
		this.gradeTotal = gradeTotal;
	}

	public double getGrade() {
		return grade;
	}

	public void setGrade(double grade) {
		this.grade = grade;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public double getGradeTotal() {
		return gradeTotal;
	}

	public void setGradeTotal(double gradeTotal) {
		this.gradeTotal = gradeTotal;
	}

	public Long getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(Long subjectId) {
		this.subjectId = subjectId;
	}				
}
