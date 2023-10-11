package com.nirvasoft.rp.shared.icbs;

import java.io.Serializable;

public class RateAmountRangeSetupData implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1201057991807712774L;

	private int pSerialNo;
	private int pComRangeType;
	private double pFromAmount;
	private double pToAmount;
	private double pCRate;
	private double pComMin;
	private double pComMax;
	private String pCommType;

	public RateAmountRangeSetupData() {
		clearProperty();

	}

	private void clearProperty() {
		pSerialNo = 0;
		pComRangeType = 0;
		pFromAmount = 0;
		pToAmount = 0;
		pCRate = 0;
		pComMin = 0;
		pComMax = 0;
		pCommType = "";
	}

	public double getComMax() {
		return pComMax;
	}

	public double getComMin() {
		return pComMin;
	}

	public String getCommType() {
		return pCommType;
	}

	public int getComRangeType() {
		return pComRangeType;
	}

	public double getCRate() {
		return pCRate;
	}

	public double getFromAmount() {
		return pFromAmount;
	}

	public int getSerialNo() {
		return pSerialNo;
	}

	public double getToAmount() {
		return pToAmount;
	}

	public void setComMax(double pComMax) {
		this.pComMax = pComMax;
	}

	public void setComMin(double pComMin) {
		this.pComMin = pComMin;
	}

	public void setCommType(String pCommType) {
		this.pCommType = pCommType;
	}

	public void setComRangeType(int pComRangeType) {
		this.pComRangeType = pComRangeType;
	}

	public void setCRate(double pCRate) {
		this.pCRate = pCRate;
	}

	public void setFromAmount(double pFromAmount) {
		this.pFromAmount = pFromAmount;
	}

	public void setSerialNo(int pSerialNo) {
		this.pSerialNo = pSerialNo;
	}

	public void setToAmount(double pToAmount) {
		this.pToAmount = pToAmount;
	}

}
