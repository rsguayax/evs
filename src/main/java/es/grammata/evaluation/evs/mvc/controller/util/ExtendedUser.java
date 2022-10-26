package es.grammata.evaluation.evs.mvc.controller.util;

import es.grammata.evaluation.evs.data.model.repository.User;

public class ExtendedUser {
	private User user;
	private String externalPassword;
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getExternalPassword() {
		return externalPassword;
	}
	public void setExternalPassword(String externalPassword) {
		this.externalPassword = externalPassword;
	}
}
