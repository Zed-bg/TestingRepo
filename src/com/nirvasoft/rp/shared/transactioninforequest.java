package com.nirvasoft.rp.shared;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class transactioninforequest {
	
	private String token;
	private String accnumber;
	private String fromdate;
	private String todate;
	private String syskey;
	
	public transactioninforequest() {
		clearproperty();
	}

	void clearproperty() {
		token = "";
		accnumber = "";
		fromdate = "";
		todate = "";
		syskey = "";
	}

	public String getToken() {
		return token;
	}

	public String getAccnumber() {
		return accnumber;
	}

	public String getFromdate() {
		return fromdate;
	}

	public String getTodate() {
		return todate;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void setAccnumber(String accnumber) {
		this.accnumber = accnumber;
	}

	public void setFromdate(String fromdate) {
		this.fromdate = fromdate;
	}

	public void setTodate(String todate) {
		this.todate = todate;
	}

	public String getSyskey() {
		return syskey;
	}

	public void setSyskey(String syskey) {
		this.syskey = syskey;
	}
}
