package com.nirvasoft.rp.shared;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class paymenttopupreq {
	
	private String token;
	private String topupnumber;
	private double topupamount;
	private String remark;
	private paymenttransactiondata[] datalist;
	private String syskey;
	private String transdescription;
	
	public paymenttopupreq() {
		clearproperty();
	}

	void clearproperty() {
		token = "";
		topupnumber = "";
		topupamount = 0.00;
		remark = "";
		datalist = null;
		syskey = "";
		transdescription = "";
	}

	public String getToken() {
		return token;
	}

	public String getTopupnumber() {
		return topupnumber;
	}

	public double getTopupamount() {
		return topupamount;
	}

	public String getRemark() {
		return remark;
	}

	public paymenttransactiondata[] getDatalist() {
		return datalist;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void setTopupnumber(String topupnumber) {
		this.topupnumber = topupnumber;
	}

	public void setTopupamount(double topupamount) {
		this.topupamount = topupamount;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public void setDatalist(paymenttransactiondata[] datalist) {
		this.datalist = datalist;
	}

	public String getSyskey() {
		return syskey;
	}

	public void setSyskey(String syskey) {
		this.syskey = syskey;
	}

	public String getTransdescription() {
		return transdescription;
	}

	public void setTransdescription(String transdescription) {
		this.transdescription = transdescription;
	}
}
