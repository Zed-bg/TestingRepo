package com.nirvasoft.rp.shared;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class bulkpaymentrequest {
	private String token;
	private String paymenttype;
	private String fromaccnumber;
	private double totalamount;
	private String refcode;
	private bulkpaymentdatareq[] datalist;
	private String syskey;
	private String transdescription;
	
	public bulkpaymentrequest() {
		clearproperty();
	}

	void clearproperty() {
		token = "";
		paymenttype = "";
		fromaccnumber = "";
		totalamount = 0.00;
		refcode = "";
		datalist = null;
		syskey = "";
		transdescription = "";
	}

	public String getToken() {
		return token;
	}

	public String getPaymenttype() {
		return paymenttype;
	}

	public String getFromaccnumber() {
		return fromaccnumber;
	}

	public double getTotalamount() {
		return totalamount;
	}

	public bulkpaymentdatareq[] getDatalist() {
		return datalist;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void setPaymenttype(String paymenttype) {
		this.paymenttype = paymenttype;
	}

	public void setFromaccnumber(String fromaccnumber) {
		this.fromaccnumber = fromaccnumber;
	}

	public void setTotalamount(double totalamount) {
		this.totalamount = totalamount;
	}

	public String getRefcode() {
		return refcode;
	}

	public void setRefcode(String refcode) {
		this.refcode = refcode;
	}

	public void setDatalist(bulkpaymentdatareq[] datalist) {
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
