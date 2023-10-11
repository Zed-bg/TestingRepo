package com.nirvasoft.rp.shared;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class accountcheckres {
	private String retcode;
	private String retmessage;
	private String accnumber;
	private String brcode;
	private String accname;
	private String status;
	private String curcode;
	private String currentbalance;
	private String productname;
	private String producttype;
	private String brname;
	private String acctype;
	private String openingdate;
	private String availablebalance;
	private Double currentbalancedec;
	
	
	public accountcheckres() {
		clearproperty();
	}

	private void clearproperty() {
		retcode = "";
		retmessage = "";
		accnumber = "";
		brcode = "";
		accname = "";
		status = "";
		curcode = "";
		currentbalance = "0.00";
		productname = "";
		producttype = "";
		brname = "";
		acctype = "";
		openingdate = "";
		availablebalance="0.00";
		currentbalancedec = 0.00;
	}

	public String getAvailablebalance() {
		return availablebalance;
	}

	public void setAvailablebalance(String availablebalance) {
		this.availablebalance = availablebalance;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCurrentbalance() {
		return currentbalance;
	}

	public void setCurrentbalance(String currentbalance) {
		this.currentbalance = currentbalance;
	}

	public String getProductname() {
		return productname;
	}

	public void setProductname(String productname) {
		this.productname = productname;
	}
	
	public String getOpeningdate() {
		return openingdate;
	}

	public void setOpeningdate(String openingdate) {
		this.openingdate = openingdate;
	}

	public String getRetcode() {
		return retcode;
	}

	public String getRetmessage() {
		return retmessage;
	}

	public String getAccnumber() {
		return accnumber;
	}

	public String getBrcode() {
		return brcode;
	}

	public String getAccname() {
		return accname;
	}

	public String getCurcode() {
		return curcode;
	}

	public String getBrname() {
		return brname;
	}

	public String getAcctype() {
		return acctype;
	}

	public void setRetcode(String retcode) {
		this.retcode = retcode;
	}

	public void setRetmessage(String retmessage) {
		this.retmessage = retmessage;
	}

	public void setAccnumber(String accnumber) {
		this.accnumber = accnumber;
	}

	public void setBrcode(String brcode) {
		this.brcode = brcode;
	}

	public void setAccname(String accname) {
		this.accname = accname;
	}

	public void setCurcode(String curcode) {
		this.curcode = curcode;
	}

	public void setBrname(String brname) {
		this.brname = brname;
	}

	public void setAcctype(String acctype) {
		this.acctype = acctype;
	}

	public String getProducttype() {
		return producttype;
	}

	public void setProducttype(String producttype) {
		this.producttype = producttype;
	}

	public Double getCurrentbalancedec() {
		return currentbalancedec;
	}

	public void setCurrentbalancedec(Double currentbalancedec) {
		this.currentbalancedec = currentbalancedec;
	}

	
}
