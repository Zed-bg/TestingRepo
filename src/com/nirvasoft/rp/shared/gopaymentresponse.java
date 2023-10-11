package com.nirvasoft.rp.shared;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class gopaymentresponse {

	private String retcode;
	private String retmessage;
	private String bankrefnumber;
	private String transdate;
	private String effectivedate;

	public gopaymentresponse() {
		clearproperty();
	}

	private void clearproperty() {
		retcode = "";
		retmessage = "";
		bankrefnumber = "";
		transdate = "";
		effectivedate = "";
	}

	public String getBankrefnumber() {
		return bankrefnumber;
	}

	public void setBankrefnumber(String bankrefnumber) {
		this.bankrefnumber = bankrefnumber;
	}

	public String getRetcode() {
		return retcode;
	}

	public String getRetmessage() {
		return retmessage;
	}

	public String getTransdate() {
		return transdate;
	}

	public void setRetcode(String retcode) {
		this.retcode = retcode;
	}

	public void setRetmessage(String retmessage) {
		this.retmessage = retmessage;
	}

	public void setTransdate(String transdate) {
		this.transdate = transdate;
	}

	public String getEffectivedate() {
		return effectivedate;
	}

	public void setEffectivedate(String effectivedate) {
		this.effectivedate = effectivedate;
	}
}