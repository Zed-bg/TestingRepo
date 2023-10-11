package com.nirvasoft.rp.shared;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class coderequestcct {
	String token;
	String priority;
	String byear;
	String frombranchcode;
	private String syskey;
	
	public coderequestcct() {
		clearproperty();
	}

	void clearproperty() {
		token ="";
		priority ="";
		byear ="";
		frombranchcode = "";
		syskey = "";
	}

	public String getToken() {
		return token;
	}

	public String getPriority() {
		return priority;
	}


	public void setToken(String token) {
		this.token = token;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getByear() {
		return byear;
	}

	public void setByear(String byear) {
		this.byear = byear;
	}

	public String getFrombranchcode() {
		return frombranchcode;
	}

	public void setFrombranchcode(String frombranchcode) {
		this.frombranchcode = frombranchcode;
	}

	public String getSyskey() {
		return syskey;
	}

	public void setSyskey(String syskey) {
		this.syskey = syskey;
	}
}
