package com.nirvasoft.rp.shared;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class merchantpaymentdatareq {
	
	private String token;
	private String merchantname;
	private String referencenumber;
	private paymenttransactiondata[] datalist;
	private String remark;
	private String syskey;
	private String transdescription;
	
	public merchantpaymentdatareq() {
		clearproperty();
	}

	void clearproperty() {
		token = "";
		merchantname = "";
		referencenumber = "";
		datalist = null;
		remark = "";
		syskey = "";
		transdescription = "";
	}

	public String getToken() {
		return token;
	}

	public String getMerchantname() {
		return merchantname;
	}

	public String getReferencenumber() {
		return referencenumber;
	}

	public paymenttransactiondata[] getDatalist() {
		return datalist;
	}

	public String getRemark() {
		return remark;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void setMerchantname(String merchantname) {
		this.merchantname = merchantname;
	}

	public void setReferencenumber(String referencenumber) {
		this.referencenumber = referencenumber;
	}

	public void setDatalist(paymenttransactiondata[] datalist) {
		this.datalist = datalist;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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
