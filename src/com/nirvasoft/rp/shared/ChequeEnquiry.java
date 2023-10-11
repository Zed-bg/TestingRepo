package com.nirvasoft.rp.shared;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ChequeEnquiry {

	private String token;
	private String accnumber;
	private String chequenumber;
	private String syskey;

	public ChequeEnquiry() {
		clearProperty();
	}

	private void clearProperty() {
		token = "";
		accnumber = "";
		chequenumber = "";
		syskey = "";
	}

	public String getToken() {
		return token;
	}

	public String getAccnumber() {
		return accnumber;
	}

	public String getChequenumber() {
		return chequenumber;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void setAccnumber(String accnumber) {
		this.accnumber = accnumber;
	}

	public void setChequenumber(String chequenumber) {
		this.chequenumber = chequenumber;
	}

	public String getSyskey() {
		return syskey;
	}

	public void setSyskey(String syskey) {
		this.syskey = syskey;
	}

}
