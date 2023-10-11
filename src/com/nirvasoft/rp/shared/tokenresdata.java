package com.nirvasoft.rp.shared;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class tokenresdata {
	private String token;
	private String retcode;
	private String retmessage;
	
	public tokenresdata() {
		clearProperty();
	}

	void clearProperty() {
		token = "";
		retcode = "";
		retmessage = "";
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getRetcode() {
		return retcode;
	}

	public void setRetcode(String retcode) {
		this.retcode = retcode;
	}

	public String getRetmessage() {
		return retmessage;
	}

	public void setRetmessage(String retmessage) {
		this.retmessage = retmessage;
	}

	
}
