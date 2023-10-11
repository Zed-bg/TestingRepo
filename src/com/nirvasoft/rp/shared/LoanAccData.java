package com.nirvasoft.rp.shared;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement
public class LoanAccData {
	
	private String loanaccnumber;
	private String outstandingbalance;
	private String linkedaccnumber;
	private String interestamount;
	private String servicechargesamount;
	
	private int loantype;
	private String sanctiondate;
	private String expdate;
	
	private double interestrate;
	private double servicechargesrate;
	private String loantypedesc;
	private String loanlimit;
	
	public LoanAccData() {
		clearProperty();
	}

	private void clearProperty() {
		loanaccnumber = "";
		outstandingbalance = "";
		linkedaccnumber = "";
		interestamount = "";
		servicechargesamount = "";
		loantype = 0;
		sanctiondate = "";
		expdate = ""; 
		interestrate = 0;
		servicechargesrate = 0;
		loantypedesc = "";
		loanlimit = "";
	}

	public String getLoanaccnumber() {
		return loanaccnumber;
	}

	public void setLoanaccnumber(String loanaccnumber) {
		this.loanaccnumber = loanaccnumber;
	}

	public String getOutstandingbalance() {
		return outstandingbalance;
	}

	public void setOutstandingbalance(String outstandingbalance) {
		this.outstandingbalance = outstandingbalance;
	}

	public String getLinkedaccnumber() {
		return linkedaccnumber;
	}

	public void setLinkedaccnumber(String linkedaccnumber) {
		this.linkedaccnumber = linkedaccnumber;
	}

	public String getInterestamount() {
		return interestamount;
	}

	public void setInterestamount(String interestamount) {
		this.interestamount = interestamount;
	}

	public String getServicechargesamount() {
		return servicechargesamount;
	}

	public void setServicechargesamount(String servicechargesamount) {
		this.servicechargesamount = servicechargesamount;
	}

	public int getLoantype() {
		return loantype;
	}

	public void setLoantype(int loantype) {
		this.loantype = loantype;
	}

	public String getSanctiondate() {
		return sanctiondate;
	}

	public void setSanctiondate(String sanctiondate) {
		this.sanctiondate = sanctiondate;
	}

	public String getExpdate() {
		return expdate;
	}

	public void setExpdate(String expdate) {
		this.expdate = expdate;
	}

	public double getInterestrate() {
		return interestrate;
	}

	public void setInterestrate(double interestrate) {
		this.interestrate = interestrate;
	}

	public double getServicechargesrate() {
		return servicechargesrate;
	}

	public void setServicechargesrate(double servicechargesrate) {
		this.servicechargesrate = servicechargesrate;
	}

	public String getLoantypedesc() {
		return loantypedesc;
	}

	public void setLoantypedesc(String loantypedesc) {
		this.loantypedesc = loantypedesc;
	}

	public String getLoanlimit() {
		return loanlimit;
	}

	public void setLoanlimit(String loanlimit) {
		this.loanlimit = loanlimit;
	}
	
	
}
