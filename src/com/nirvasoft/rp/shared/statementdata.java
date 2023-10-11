package com.nirvasoft.rp.shared;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class statementdata {
	private String transdate;
	private String transref;
	private String transdescription;
	private String transcode;
	private double transamount;
	private double balance;
	private String curcode;
	private String remark;
	private String transtypedescription;
	
	public statementdata() {
		clearProperty();
	}

	void clearProperty() {
		transdate = "";
		transref = "";
		transdescription = "";
		transcode = "";
		transamount = 0.00;
		balance = 0.00;
		curcode = "";
		remark = "";
		transtypedescription = "";
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getTransdate() {
		return transdate;
	}

	public String getTransref() {
		return transref;
	}

	public String getTransdescription() {
		return transdescription;
	}

	public String getTranscode() {
		return transcode;
	}

	public double getTransamount() {
		return transamount;
	}

	public String getCurcode() {
		return curcode;
	}

	public void setTransdate(String transdate) {
		this.transdate = transdate;
	}

	public void setTransref(String transref) {
		this.transref = transref;
	}

	public void setTransdescription(String transdescription) {
		this.transdescription = transdescription;
	}

	public void setTranscode(String transcode) {
		this.transcode = transcode;
	}

	public void setTransamount(double transamount) {
		this.transamount = transamount;
	}

	public void setCurcode(String curcode) {
		this.curcode = curcode;
	}

	public String getTranstypedescription() {
		return transtypedescription;
	}

	public void setTranstypedescription(String transtypedescription) {
		this.transtypedescription = transtypedescription;
	}
}
