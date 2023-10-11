package com.nirvasoft.rp.shared;

import java.util.ArrayList;

public class LoanScheduleData {
	private String totalprinamount;
	private String totalinterestamount;
	private int totalcount;
	private ArrayList<LAODFScheduleData>  datalist;
	private String linkedaccnumber;
	private int loantype;
	private String loantypedesc;
	private int batchno;
	
	public LoanScheduleData() {
		clearProperty();
	}

	private void clearProperty() {
		totalprinamount = "";
		totalinterestamount = "";
		totalcount = 0;
		datalist = null;
		linkedaccnumber = "";
		loantype = 0;
		loantypedesc = "";
		batchno = 0;
	}
	

	public String getTotalprinamount() {
		return totalprinamount;
	}

	public void setTotalprinamount(String totalprinamount) {
		this.totalprinamount = totalprinamount;
	}

	public String getTotalinterestamount() {
		return totalinterestamount;
	}

	public void setTotalinterestamount(String totalinterestamount) {
		this.totalinterestamount = totalinterestamount;
	}

	public int getTotalcount() {
		return totalcount;
	}

	public void setTotalcount(int totalcount) {
		this.totalcount = totalcount;
	}

	public ArrayList<LAODFScheduleData> getDatalist() {
		return datalist;
	}

	public void setDatalist(ArrayList<LAODFScheduleData> datalist) {
		this.datalist = datalist;
	}

	public String getLinkedaccnumber() {
		return linkedaccnumber;
	}

	public void setLinkedaccnumber(String linkedaccnumber) {
		this.linkedaccnumber = linkedaccnumber;
	}

	public int getLoantype() {
		return loantype;
	}

	public void setLoantype(int loantype) {
		this.loantype = loantype;
	}

	public String getLoantypedesc() {
		return loantypedesc;
	}

	public void setLoantypedesc(String loantypedesc) {
		this.loantypedesc = loantypedesc;
	}

	public int getBatchno() {
		return batchno;
	}

	public void setBatchno(int batchno) {
		this.batchno = batchno;
	}
}
