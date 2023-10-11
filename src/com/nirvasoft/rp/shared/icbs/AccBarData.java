package com.nirvasoft.rp.shared.icbs;

import java.io.Serializable;

public class AccBarData implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String pAccountNumber;
	private double pBarAmount;
	private String pDate;
	private String pRemark;
	private int pBarType;
	private String pDescription;
	private String pOldAccNumber = "";

	public String getAccountNumber() {
		return pAccountNumber;
	}

	public double getBarAmount() {
		return pBarAmount;
	}

	public int getBarType() {
		return pBarType;
	}

	public String getDate() {
		return pDate;
	}

	public String getDescription() {
		return pDescription;
	}

	public String getOldAccNumber() {
		return pOldAccNumber;
	}

	public String getRemark() {
		return pRemark;
	}

	public void setAccountNumber(String p) {
		pAccountNumber = p;
	}

	public void setBarAmount(double p) {
		pBarAmount = p;
	}

	public void setBarType(int p) {
		pBarType = p;
	}

	public void setDate(String p) {
		pDate = p;
	}

	public void setDescription(String pDescription) {
		this.pDescription = pDescription;
	}

	public void setOldAccNumber(String pOldAccNumber) {
		this.pOldAccNumber = pOldAccNumber;
	}

	public void setRemark(String p) {
		pRemark = p;
	}
}
