package com.nirvasoft.rp.shared.icbs;

import java.io.Serializable;

public class AmountLimitData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2513459080766795463L;

	private int pTransType;
	private String pDescription;
	private double pMaxTransAmount;
	private int pTransCount;

	public AmountLimitData() {
		clearProperty();
	}

	private void clearProperty() {
		pTransType = 0;
		pDescription = "";
		pMaxTransAmount = 0;
		pTransCount = 0;
	}

	public String getDescription() {
		return pDescription;
	}

	public double getMaxTransAmount() {
		return pMaxTransAmount;
	}

	public int getTransCount() {
		return pTransCount;
	}

	public int getTransType() {
		return pTransType;
	}

	public void setDescription(String pDescription) {
		this.pDescription = pDescription;
	}

	public void setMaxTransAmount(double pMaxTransAmount) {
		this.pMaxTransAmount = pMaxTransAmount;
	}

	public void setTransCount(int pTransCount) {
		this.pTransCount = pTransCount;
	}

	public void setTransType(int pTransType) {
		this.pTransType = pTransType;
	}
}
