package com.nirvasoft.rp.shared.icbs;

import java.io.Serializable;

public class ATMParameterData implements Serializable {

	private static final long serialVersionUID = 3434453023368218994L;

	private String pCurrencyCode;

	private String pTerminalId;
	private double pAmount;
	private double pTransactionFee;
	private String pTraceNumber;
	private String pInstitutionCode;
	private String fromCCY;
	private String toCCY;

	public ATMParameterData() {
		clearProperty();
	}

	public void clearProperty() {
		pCurrencyCode = "104";
		pTerminalId = "";
		pAmount = 0;
		pTransactionFee = 0;
		pTraceNumber = "";
		pInstitutionCode = "";
		fromCCY = "";
		toCCY = "";
	}

	public double getAmount() {
		return pAmount;
	}

	public String getCurrencyCode() {
		return pCurrencyCode;
	}

	public String getFromCCY() {
		return fromCCY;
	}

	public String getInstitutionCode() {
		return pInstitutionCode;
	}

	public String getTerminalId() {
		return pTerminalId;
	}

	public String getToCCY() {
		return toCCY;
	}

	public String getTraceNumber() {
		return pTraceNumber;
	}

	public double getTransactionFee() {
		return pTransactionFee;
	}

	public void setAmount(double pAmount) {
		this.pAmount = pAmount;
	}

	public void setCurrencyCode(String pCurrencyCode) {
		this.pCurrencyCode = pCurrencyCode;
	}

	public void setFromCCY(String fromCCY) {
		this.fromCCY = fromCCY;
	}

	public void setInstitutionCode(String pInstitutionCode) {
		this.pInstitutionCode = pInstitutionCode;
	}

	public void setTerminalId(String pTerminalId) {
		this.pTerminalId = pTerminalId;
	}

	public void setToCCY(String toCCY) {
		this.toCCY = toCCY;
	}

	public void setTraceNumber(String pTraceNumber) {
		this.pTraceNumber = pTraceNumber;
	}

	public void setTransactionFee(double pTransactionFee) {
		this.pTransactionFee = pTransactionFee;
	}
}
