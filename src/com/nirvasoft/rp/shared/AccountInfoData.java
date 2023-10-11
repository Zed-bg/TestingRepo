package com.nirvasoft.rp.shared;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class AccountInfoData {
	private java.lang.String cifid;
	private java.lang.String accdob;
	private java.lang.String accname;
	private java.lang.String accnrc;
	private java.lang.String accnumber;
	private java.lang.String acctype;
	private java.lang.String curcode;
	private java.lang.String producttype;
	private java.lang.String openingdate;
	private java.lang.String accstatus;
	private java.lang.String productcode;
	
	public AccountInfoData() {
		clearproperty();
	}

	void clearproperty() {
		cifid = "";
		accdob = "";
		accname = "";
		accnrc = "";
		accnumber = "";
		acctype = "";
		curcode = "";
		producttype = "";
		openingdate = "";
		accstatus = "";
		productcode= "";
	}

	public java.lang.String getCifid() {
		return cifid;
	}

	public java.lang.String getAccdob() {
		return accdob;
	}

	public java.lang.String getAccname() {
		return accname;
	}

	public java.lang.String getAccnrc() {
		return accnrc;
	}

	public java.lang.String getAccnumber() {
		return accnumber;
	}

	public java.lang.String getAcctype() {
		return acctype;
	}

	public java.lang.String getCurcode() {
		return curcode;
	}

	public java.lang.String getProducttype() {
		return producttype;
	}

	public java.lang.String getOpeningdate() {
		return openingdate;
	}

	public java.lang.String getAccstatus() {
		return accstatus;
	}

	public void setCifid(java.lang.String cifid) {
		this.cifid = cifid;
	}

	public void setAccdob(java.lang.String accdob) {
		this.accdob = accdob;
	}

	public void setAccname(java.lang.String accname) {
		this.accname = accname;
	}

	public void setAccnrc(java.lang.String accnrc) {
		this.accnrc = accnrc;
	}

	public void setAccnumber(java.lang.String accnumber) {
		this.accnumber = accnumber;
	}

	public void setAcctype(java.lang.String acctype) {
		this.acctype = acctype;
	}

	public void setCurcode(java.lang.String curcode) {
		this.curcode = curcode;
	}

	public void setProducttype(java.lang.String producttype) {
		this.producttype = producttype;
	}

	public void setOpeningdate(java.lang.String openingdate) {
		this.openingdate = openingdate;
	}

	public void setAccstatus(java.lang.String accstatus) {
		this.accstatus = accstatus;
	}

	public java.lang.String getProductcode() {
		return productcode;
	}

	public void setProductcode(java.lang.String productcode) {
		this.productcode = productcode;
	}
	    
}
