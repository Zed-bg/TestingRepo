package com.nirvasoft.rp.shared;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class LoanRepaymentReq {
	private String syskey;
	private String token;
	private String loanaccnumber;
	private String linkedaccnumber;
	private double amount;
	private double interestamt;
	private double servicechargesamt;
	private byte loantype;
	private LAODFScheduleData datalist ;
	
	
	public LoanRepaymentReq() {
		clearProperty();
	}

	private void clearProperty() {
		syskey = "";
		token = "";
		loanaccnumber = "";
		linkedaccnumber = "";
		amount = 0;
		interestamt = 0;
		servicechargesamt = 0;
		loantype = 0;
		datalist = null;
		
	}

	public String getSyskey() {
		return syskey;
	}

	public void setSyskey(String syskey) {
		this.syskey = syskey;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getLoanaccnumber() {
		return loanaccnumber;
	}

	public void setLoanaccnumber(String loanaccnumber) {
		this.loanaccnumber = loanaccnumber;
	}

	public String getLinkedaccnumber() {
		return linkedaccnumber;
	}

	public void setLinkedaccnumber(String linkedaccnumber) {
		this.linkedaccnumber = linkedaccnumber;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public double getInterestamt() {
		return interestamt;
	}

	public void setInterestamt(double interestamt) {
		this.interestamt = interestamt;
	}

	public double getServicechargesamt() {
		return servicechargesamt;
	}

	public void setServicechargesamt(double servicechargesamt) {
		this.servicechargesamt = servicechargesamt;
	}

	public byte getLoantype() {
		return loantype;
	}

	public void setLoantype(byte loantype) {
		this.loantype = loantype;
	}

	public LAODFScheduleData getDatalist() {
		return datalist;
	}

	public void setDatalist(LAODFScheduleData datalist) {
		this.datalist = datalist;
	}


}
