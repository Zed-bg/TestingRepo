package com.nirvasoft.rp.shared;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class accountnumberdataresponse {
	
	private String accnumber;
	private String brcode;
	private String brname;
	private String accname;
	private String bankname;
	
	private String retcode;
	private String retmessage;
	
	public accountnumberdataresponse() {
		clearproperty();
	}

	void clearproperty() {
		accnumber = "";
		brcode = "";
		brname = "";
		accname = "";
		bankname = "";
		
		retcode = "";
		retmessage = "";
	}

	public String getAccnumber() {
		return accnumber;
	}

	public String getBrcode() {
		return brcode;
	}

	public String getBrname() {
		return brname;
	}

	public String getAccname() {
		return accname;
	}

	public void setAccnumber(String accnumber) {
		this.accnumber = accnumber;
	}

	public void setBrcode(String brcode) {
		this.brcode = brcode;
	}

	public void setBrname(String brname) {
		this.brname = brname;
	}

	public void setAccname(String accname) {
		this.accname = accname;
	}

	public String getBankname() {
		return bankname;
	}

	public void setBankname(String bankname) {
		this.bankname = bankname;
	}

	public String getRetcode() {
		return retcode;
	}

	public String getRetmessage() {
		return retmessage;
	}

	public void setRetcode(String retcode) {
		this.retcode = retcode;
	}

	public void setRetmessage(String retmessage) {
		this.retmessage = retmessage;
	}
}
