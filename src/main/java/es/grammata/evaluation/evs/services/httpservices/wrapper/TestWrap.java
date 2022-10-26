package es.grammata.evaluation.evs.services.httpservices.wrapper;

import java.util.Date;

import es.grammata.evaluation.evs.data.model.repository.Test;
import es.grammata.evaluation.evs.data.model.repository.User;

public class TestWrap {
	private int nSuccess;
	private int nFails;
	private int nBlanks;
	private User user;
	private Test test;
	private String urlToken;
	private int externalId;
	private Date testDate;
	
	
	public TestWrap(User user, Test test, int nSuccess, int nFails, 
			int nBlanks, String urlToken, int externalId, Date testDate) {
		this.user = user;
		this.test = test;
		this.nSuccess = nSuccess;
		this.nFails = nFails;
		this.nBlanks = nBlanks;
		this.urlToken = urlToken;
		this.externalId = externalId;
		this.testDate = testDate;
	}
	
	public int getnSuccess() {
		return nSuccess;
	}
	public void setnSuccess(int nSuccess) {
		this.nSuccess = nSuccess;
	}
	public int getnFails() {
		return nFails;
	}
	public void setnFails(int nFails) {
		this.nFails = nFails;
	}
	public int getnBlanks() {
		return nBlanks;
	}
	public void setnBlanks(int nBlanks) {
		this.nBlanks = nBlanks;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Test getTest() {
		return test;
	}
	public void setTest(Test test) {
		this.test = test;
	}

	public String getUrlToken() {
		return urlToken;
	}

	public void setUrlToken(String urlToken) {
		urlToken = urlToken;
	}

	public int getExternalId() {
		return externalId;
	}

	public void setExternalId(int externalId) {
		this.externalId = externalId;
	}

	public Date getTestDate() {
		return testDate;
	}

	public void setTestDate(Date testDate) {
		this.testDate = testDate;
	}
	
	
}
