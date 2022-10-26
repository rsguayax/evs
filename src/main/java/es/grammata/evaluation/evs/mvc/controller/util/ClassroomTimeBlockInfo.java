package es.grammata.evaluation.evs.mvc.controller.util;

import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.json.JSONObject;

import es.grammata.evaluation.evs.serializers.DateTimeUtcSerializer;

public class ClassroomTimeBlockInfo {
	
	private Long id;
	private Date startDate;
	private Date endDate;
	private int seats;
	private int occupiedSeats;
	private int availableSeats;
	private double percentageAvailableSeats;
	private double percentageOccupiedSeats;
	private String availableState;
	private Long evaluationEventEvaluationCenterId;
	private String evaluationCenterName;
	private Long classroomId;
	private String classroomName;
	
	public ClassroomTimeBlockInfo() {
		
	}
	
	public ClassroomTimeBlockInfo(JSONObject classroomTimeBlockInfoJson) {
		id = classroomTimeBlockInfoJson.has("id") ? classroomTimeBlockInfoJson.getLong("id") : null;
		startDate = classroomTimeBlockInfoJson.has("startDate") ? parseDate(classroomTimeBlockInfoJson.get("startDate")) : null;
		endDate = classroomTimeBlockInfoJson.has("endDate") ? parseDate(classroomTimeBlockInfoJson.get("endDate")) : null;
		seats = classroomTimeBlockInfoJson.has("seats") ? classroomTimeBlockInfoJson.getInt("seats") : 0;
		occupiedSeats = classroomTimeBlockInfoJson.has("occupiedSeats") ? classroomTimeBlockInfoJson.getInt("occupiedSeats") : 0;
		availableSeats = classroomTimeBlockInfoJson.has("availableSeats") ? classroomTimeBlockInfoJson.getInt("availableSeats") : 0;
		availableState = classroomTimeBlockInfoJson.has("availableState") ? classroomTimeBlockInfoJson.getString("availableState") : null;
		evaluationEventEvaluationCenterId = classroomTimeBlockInfoJson.has("evaluationEventEvaluationCenterId") ? classroomTimeBlockInfoJson.getLong("evaluationEventEvaluationCenterId") : null;
		evaluationCenterName = classroomTimeBlockInfoJson.has("evaluationCenterName") ? classroomTimeBlockInfoJson.getString("evaluationCenterName") : null;
		classroomId = classroomTimeBlockInfoJson.has("classroomId") ? classroomTimeBlockInfoJson.getLong("classroomId") : null;
		classroomName = classroomTimeBlockInfoJson.has("classroomName") ? classroomTimeBlockInfoJson.getString("classroomName") : null;
		percentageAvailableSeats = Math.round((availableSeats * 100.0 / seats) * 100.0) / 100.0;
		percentageOccupiedSeats = Math.round((occupiedSeats * 100.0 / seats) * 100.0) / 100.0;
	}

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

	public int getSeats() {
		return seats;
	}

	public void setSeats(int seats) {
		this.seats = seats;
	}

	public int getOccupiedSeats() {
		return occupiedSeats;
	}

	public void setOccupiedSeats(int occupiedSeats) {
		this.occupiedSeats = occupiedSeats;
	}

	public int getAvailableSeats() {
		return availableSeats;
	}

	public void setAvailableSeats(int availableSeats) {
		this.availableSeats = availableSeats;
	}

	public double getPercentageAvailableSeats() {
		return percentageAvailableSeats;
	}

	public void setPercentageAvailableSeats(long percentageAvailableSeats) {
		this.percentageAvailableSeats = percentageAvailableSeats;
	}

	public double getPercentageOccupiedSeats() {
		return percentageOccupiedSeats;
	}

	public void setPercentageOccupiedSeats(long percentageOccupiedSeats) {
		this.percentageOccupiedSeats = percentageOccupiedSeats;
	}

	public String getAvailableState() {
		return availableState;
	}

	public void setAvailableState(String availableState) {
		this.availableState = availableState;
	}

	public Long getEvaluationEventEvaluationCenterId() {
		return evaluationEventEvaluationCenterId;
	}

	public void setEvaluationEventEvaluationCenterId(
			Long evaluationEventEvaluationCenterId) {
		this.evaluationEventEvaluationCenterId = evaluationEventEvaluationCenterId;
	}

	public String getEvaluationCenterName() {
		return evaluationCenterName;
	}

	public void setEvaluationCenterName(String evaluationCenterName) {
		this.evaluationCenterName = evaluationCenterName;
	}

	public Long getClassroomId() {
		return classroomId;
	}

	public void setClassroomId(Long classroomId) {
		this.classroomId = classroomId;
	}

	public String getClassroomName() {
		return classroomName;
	}

	public void setClassroomName(String classroomName) {
		this.classroomName = classroomName;
	}
	
	private Date parseDate(Object date) {
		if (date instanceof Date) {
			return (Date) date;
		} else if (date instanceof Long) {
			return new Date((Long) date);
		} else {
			return null;
		}
	}
}
