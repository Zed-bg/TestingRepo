package com.nirvasoft.rp.shared;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class bulkpaymentdatareq {
	private String rowno;
	private String toaccnumber;
	private String toaccname;
	private double amount;
	private String description;
	
	public bulkpaymentdatareq() {
		clearproperty();
	}

	void clearproperty() {
		rowno = "";
		toaccnumber = "";
		toaccname = "";
		amount = 0.00;
		description = "";
	}

	public String getRowno() {
		return rowno;
	}

	public String getToaccnumber() {
		return toaccnumber;
	}

	public String getToaccname() {
		return toaccname;
	}

	public double getAmount() {
		return amount;
	}

	public void setRowno(String rowno) {
		this.rowno = rowno;
	}

	public void setToaccnumber(String toaccnumber) {
		this.toaccnumber = toaccnumber;
	}

	public void setToaccname(String toaccname) {
		this.toaccname = toaccname;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
