package com.nirvasoft.rp.shared;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class fixedaccountopenres {
	private String retcode;
	private String retmessage;
	private String toaccnumber;
	private String startdate;
	private String duedate;
	private String bankrefnumber;
	private String transdate;
	private String effectivedate;
	
	public fixedaccountopenres() {
		clearProperty();
	}

	private void clearProperty() {
		retcode = "";
		retmessage = "";
		toaccnumber = "";
		startdate = "";
		duedate = "";
		bankrefnumber = "";
		transdate = "";
		effectivedate = "";
	}

	public String getRetcode() {
		return retcode;
	}

	public String getRetmessage() {
		return retmessage;
	}

	public String getToaccnumber() {
		return toaccnumber;
	}

	public String getStartdate() {
		return startdate;
	}

	public String getDuedate() {
		return duedate;
	}

	public String getBankrefnumber() {
		return bankrefnumber;
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

	public void setToaccnumber(String toaccnumber) {
		this.toaccnumber = toaccnumber;
	}

	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}

	public void setDuedate(String duedate) {
		this.duedate = duedate;
	}

	public void setBankrefnumber(String bankrefnumber) {
		this.bankrefnumber = bankrefnumber;
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
