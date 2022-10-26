package es.grammata.evaluation.evs.mvc.controller.util;

import java.util.Date;
import java.util.List;

import es.grammata.evaluation.evs.data.model.repository.User;

public class TestGroup {
	private Integer testExternalId;
	private List<ExtendedUser> users;
	private Date initDate;
	private Date endDate;
	private Integer bankExternalId;
	private String capSsid;
	private String evaluationEventCode;
	private String matterCode;
	private String evaluationType;
	private String evalCenter;
	private String classroom;
	private String blockId;
	
	public Integer getExternalTestId() {
		return testExternalId;
	}
	public void setTestExternalId(Integer testExternalId) {
		this.testExternalId = testExternalId;
	}
	public List<ExtendedUser> getUsers() {
		return users;
	}
	public void setUsers(List<ExtendedUser> users) {
		this.users = users;
	}
	public Date getInitDate() {
		return initDate;
	}
	public void setInitDate(Date initDate) {
		this.initDate = initDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Integer getBankExternalId() {
		return bankExternalId;
	}
	public void setBankExternlId(Integer bankExternalId) {
		this.bankExternalId = bankExternalId;
	}
	public String getCapSsid() {
		return capSsid;
	}
	public void setCapSsid(String capSsid) {
		this.capSsid = capSsid;
	}
	public String getEvaluationEventCode() {
		return evaluationEventCode;
	}
	public void setEvaluationEventCode(String evaluationEventCode) {
		this.evaluationEventCode = evaluationEventCode;
	}
	public Integer getTestExternalId() {
		return testExternalId;
	}
	public void setBankExternalId(Integer bankExternalId) {
		this.bankExternalId = bankExternalId;
	}
	public String getMatterCode() {
		return matterCode;
	}
	public void setMatterCode(String matterCode) {
		this.matterCode = matterCode;
	}
	public String getEvaluationType() {
		return evaluationType;
	}
	public void setEvaluationType(String evaluationType) {
		this.evaluationType = evaluationType;
	}
	public String getEvalCenter() {
		return evalCenter;
	}
	public void setEvalCenter(String evalCenter) {
		this.evalCenter = evalCenter;
	}
	public String getClassroom() {
		return classroom;
	}
	public void setClassroom(String classroom) {
		this.classroom = classroom;
	}
	public String getBlockId() {
		return blockId;
	}
	public void setBlockId(String blockId) {
		this.blockId = blockId;
	}

}
