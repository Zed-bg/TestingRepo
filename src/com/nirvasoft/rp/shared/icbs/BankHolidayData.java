package com.nirvasoft.rp.shared.icbs;

import java.io.Serializable;

public class BankHolidayData implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5811407440635735537L;
	private String pHolidayDate;
	private String pDescription;
	private String pT1;
	private String pT2;
	private String pT3;
	private double pN1;
	private double pN2;
	private double pN3;

	public BankHolidayData() {
		clearProperty();
	}

	public void clearProperty() {
		pHolidayDate = "";
		pDescription = "";
		pT1 = "";
		pT2 = "";
		pT3 = "";
		pN1 = 0;
		pN2 = 0;
		pN3 = 0;
	}

	public String getDescription() {
		return pDescription;
	}

	public String getHolidayDate() {
		return pHolidayDate;
	}

	public double getN1() {
		return pN1;
	}

	public double getN2() {
		return pN2;
	}

	public double getN3() {
		return pN3;
	}

	public String getT1() {
		return pT1;
	}

	public String getT2() {
		return pT2;
	}

	public String getT3() {
		return pT3;
	}

	public void setDescription(String p) {
		pDescription = p;
	}

	public void setHolidayDate(String p) {
		pHolidayDate = p;
	}

	public void setN1(double p) {
		pN1 = p;
	}

	public void setN2(double p) {
		pN2 = p;
	}

	public void setN3(double p) {
		pN3 = p;
	}

	public void setT1(String p) {
		pT1 = p;
	}

	public void setT2(String p) {
		pT2 = p;
	}

	public void setT3(String p) {
		pT3 = p;
	}

}
