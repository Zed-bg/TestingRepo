package com.nirvasoft.rp.shared;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class chequelistreq {
	private String token;
	private String accnumber;
	private String syskey;
	
	public chequelistreq() {
		clearProperty();
	}

	private void clearProperty() {
		token = "";
		accnumber = "";
		syskey = "";
	}

	public String getToken() {
		return token;
	}

	public String getAccnumber() {
		return accnumber;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void setAccnumber(String accnumber) {
		this.accnumber = accnumber;
	}

	public String getSyskey() {
		return syskey;
	}

	public void setSyskey(String syskey) {
		this.syskey = syskey;
	}
}
