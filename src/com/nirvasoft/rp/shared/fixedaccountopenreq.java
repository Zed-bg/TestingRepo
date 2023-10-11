package com.nirvasoft.rp.shared;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class fixedaccountopenreq {
	private String token;
	private String fromaccnumber;
	private String customerid;
	private String tenure;
	private String yearlyrate;
	private String amount;
	private String maturity;
	private String starttype;
	private String startdate;
	private String interestpaytype;
	private String acctype;
	private String rtype;
	private String syskey;
	private String tenuretype;
	private String datatenure;
	private String productcode;
	
	public fixedaccountopenreq() {
		clearProperty();
	}

	private void clearProperty() {
		token = "";
		fromaccnumber = "";
		customerid = "";
		tenure = "";
		yearlyrate = "";
		amount = "";
		maturity = "";
		starttype = "";
		startdate = "";
		interestpaytype = "";
		syskey = "";
		tenuretype="";
		datatenure="";
		productcode="";
	}

	public String getProductcode() {
		return productcode;
	}

	public void setProductcode(String productcode) {
		this.productcode = productcode;
	}

	public String getDatatenure() {
		return datatenure;
	}

	public void setDatatenure(String datatenure) {
		this.datatenure = datatenure;
	}

	public String getTenuretype() {
		return tenuretype;
	}

	public void setTenuretype(String tenuretype) {
		this.tenuretype = tenuretype;
	}

	public String getToken() {
		return token;
	}

	public String getFromaccnumber() {
		return fromaccnumber;
	}

	public String getCustomerid() {
		return customerid;
	}

	public String getTenure() {
		return tenure;
	}

	public String getYearlyrate() {
		return yearlyrate;
	}

	public String getAmount() {
		return amount;
	}

	public String getMaturity() {
		return maturity;
	}

	public String getStarttype() {
		return starttype;
	}

	public String getStartdate() {
		return startdate;
	}

	public String getInterestpaytype() {
		return interestpaytype;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void setFromaccnumber(String fromaccnumber) {
		this.fromaccnumber = fromaccnumber;
	}

	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}

	public void setTenure(String tenure) {
		this.tenure = tenure;
	}

	public void setYearlyrate(String yearlyrate) {
		this.yearlyrate = yearlyrate;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public void setMaturity(String maturity) {
		this.maturity = maturity;
	}

	public void setStarttype(String starttype) {
		this.starttype = starttype;
	}

	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}

	public void setInterestpaytype(String interestpaytype) {
		this.interestpaytype = interestpaytype;
	}

	public String getAcctype() {
		return acctype;
	}

	public String getRtype() {
		return rtype;
	}

	public void setAcctype(String acctype) {
		this.acctype = acctype;
	}

	public void setRtype(String rtype) {
		this.rtype = rtype;
	}

	public String getSyskey() {
		return syskey;
	}

	public void setSyskey(String syskey) {
		this.syskey = syskey;
	}
}
