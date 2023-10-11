package com.nirvasoft.rp.shared;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class accstatusdata {
	private String accnumber;
	private String accountstatus;
	
	public accstatusdata() {
		clearproperty();
	}
	void clearproperty() {
		accnumber = "";
		accountstatus = "";
	}
	public String getAccnumber() {
		return accnumber;
	}
	public String getAccountstatus() {
		return accountstatus;
	}
	public void setAccnumber(String accnumber) {
		this.accnumber = accnumber;
	}
	public void setAccountstatus(String accountstatus) {
		this.accountstatus = accountstatus;
	}
}
