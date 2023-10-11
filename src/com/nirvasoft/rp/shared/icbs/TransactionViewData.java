package com.nirvasoft.rp.shared.icbs;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TransactionViewData {

	private int serialNo;
	private int transRef;
	private String accNumber;
	private double amount;
	private String transDateTime;
	private String currencyCode;
	private String drcr;
	private String description;
	private String merchantID;

	public TransactionViewData() {
		transRef = 0;
		serialNo = 0;
		transDateTime = "";
		description = "";
		currencyCode = "";
		amount = 0.00;
		drcr = "";
		accNumber = "";
		merchantID = "";
	}

	public String getAccNumber() {
		return accNumber;
	}

	public double getAmount() {
		return amount;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public String getDescription() {
		return description;
	}

	public String getDrcr() {
		return drcr;
	}

	public String getMerchantID() {
		return merchantID;
	}

	public int getSerialNo() {
		return serialNo;
	}

	public String getTransDateTime() {
		return transDateTime;
	}

	public int getTransRef() {
		return transRef;
	}

	public void setAccNumber(String accNumber) {
		this.accNumber = accNumber;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setDrcr(String drcr) {
		this.drcr = drcr;
	}

	public void setMerchantID(String merchantID) {
		this.merchantID = merchantID;
	}

	public void setSerialNo(int serialNo) {
		this.serialNo = serialNo;
	}

	public void setTransDateTime(String transDateTime) {
		this.transDateTime = transDateTime;
	}

	public void setTransRef(int transRef) {
		this.transRef = transRef;
	}

}
