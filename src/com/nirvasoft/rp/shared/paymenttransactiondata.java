package com.nirvasoft.rp.shared;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class paymenttransactiondata {
	
	private String fromaccnumber;
	private String toaccnumber;
	private String fromcurcode;
	private double amount;
	private String description;
	
	public paymenttransactiondata() {
		clearproperty();
	}

	void clearproperty() {
		fromaccnumber = "";
		toaccnumber = "";
		fromcurcode  = "";
		amount = 0.00;
		description = "";
	}

	public String getFromaccnumber() {
		return fromaccnumber;
	}

	public String getToaccnumber() {
		return toaccnumber;
	}

	public String getFromcurcode() {
		return fromcurcode;
	}

	public double getAmount() {
		return amount;
	}

	public void setFromaccnumber(String fromaccnumber) {
		this.fromaccnumber = fromaccnumber;
	}

	public void setToaccnumber(String toaccnumber) {
		this.toaccnumber = toaccnumber;
	}

	public void setFromcurcode(String fromcurcode) {
		this.fromcurcode = fromcurcode;
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
