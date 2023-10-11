package com.nirvasoft.rp.shared;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class userdata {
	private String userid;
	private String customerid;
	private String token;
	private String accnumber;
	private String syskey;
	private String bankcode;
	private String searchtype;
	private String[] excludedaccounts;
	private String customertype;
	
	public userdata() {
		clearproperty();
	}

	void clearproperty() {
		customerid = "";
		token = "";
		accnumber = "";
		userid = "";
		syskey = "";
		bankcode = "";
		searchtype="";
		customertype = "";
	}

	public String getSearchtype() {
		return searchtype;
	}

	public void setSearchtype(String searchtype) {
		this.searchtype = searchtype;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getCustomerid() {
		return customerid;
	}

	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getAccnumber() {
		return accnumber;
	}

	public void setAccnumber(String accnumber) {
		this.accnumber = accnumber;
	}

	public String getSyskey() {
		return syskey;
	}

	public void setSyskey(String syskey) {
		this.syskey = syskey;
	}

	public String getBankcode() {
		return bankcode;
	}

	public void setBankcode(String bankcode) {
		this.bankcode = bankcode;
	}

	public String[] getExcludedaccounts() {
		return excludedaccounts;
	}

	public void setExcludedaccounts(String[] excludedaccounts) {
		this.excludedaccounts = excludedaccounts;
	}

	public String getCustomertype() {
		return customertype;
	}

	public void setCustomertype(String customertype) {
		this.customertype = customertype;
	}

	
}
