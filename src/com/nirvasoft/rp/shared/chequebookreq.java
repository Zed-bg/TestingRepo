package com.nirvasoft.rp.shared;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class chequebookreq {
	private String token;
	private String accnumber;
	private int chequesheetlength;
	private String syskey;
	
	public chequebookreq() {
		clearproperty();
	}

	void clearproperty() {
		token = "";
		accnumber = "";
		chequesheetlength = 0;
		syskey = "";
	}

	public String getToken() {
		return token;
	}

	public String getAccnumber() {
		return accnumber;
	}

	public int getChequesheetlength() {
		return chequesheetlength;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void setAccnumber(String accnumber) {
		this.accnumber = accnumber;
	}

	public void setChequesheetlength(int chequesheetlength) {
		this.chequesheetlength = chequesheetlength;
	}

	public String getSyskey() {
		return syskey;
	}

	public void setSyskey(String syskey) {
		this.syskey = syskey;
	}
}
