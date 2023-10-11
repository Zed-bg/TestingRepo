package com.nirvasoft.rp.framework;

import java.io.Serializable;

import com.nirvasoft.rp.users.data.UserData;

public class MrBean implements Serializable {

	private static final long serialVersionUID = -5796624435566145102L;
	private UserData user;

	private String message = "";
	private String logoText = "";
	private boolean status = false;

	public MrBean() {
		this.user = new UserData();

		this.message = "";
		this.status = false;
	}

	public String getLogoText() {
		return logoText;
	}

	public String getMessage() {
		return message;
	}

	public boolean getStatus() {
		return status;
	}

	public UserData getUser() {
		return this.user;
	}

	public void setLogoText(String logoText) {
		this.logoText = logoText;
	}

	public void setMessage(String command) {
		this.message = command;
	}

	public void setStatus(boolean p) {
		status = p;
	}

	public void setUser(UserData p) {
		user = p;
	}
}
