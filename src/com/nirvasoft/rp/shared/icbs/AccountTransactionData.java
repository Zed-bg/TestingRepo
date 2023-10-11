package com.nirvasoft.rp.shared.icbs;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class AccountTransactionData {
	private int TransNo;
	private String branchCode;
	private String workStation;
	private int transRef;
	private int serialNo;
	private String tellerId;
	private String supervisorId;
	private String transTime;
	private String transDate;
	private String description;
	private String chequeNo;
	private String currencyCode;
	private int currencyRate;
	private double amount;
	private int transType;
	private String accNumber;
	private double prevBalance;
	private String prevUpdate;
	private String effectiveDate;
	private String contraDate;
	private int status;
	private String accRef;
	private String remark;
	private int systemCode;
	private String subRef;

	public AccountTransactionData() {
		clearProperty();
	}

	private void clearProperty() {
		TransNo = 0;
		branchCode = "";
		workStation = "";
		transRef = 0;
		serialNo = 0;
		tellerId = "";
		supervisorId = "";
		transTime = "";
		transDate = "";
		description = "";
		chequeNo = "";
		currencyCode = "";
		currencyRate = 0;
		amount = 0;
		transType = 0;
		accNumber = "";
		prevBalance = 0;
		prevUpdate = "";
		effectiveDate = "";
		contraDate = "";
		status = 0;
		accRef = "";
		remark = "";
		systemCode = 0;
		subRef = "";
	}

	public String getAccNumber() {
		return accNumber;
	}

	public String getAccRef() {
		return accRef;
	}

	public double getAmount() {
		return amount;
	}

	public String getBranchCode() {
		return branchCode;
	}

	public String getChequeNo() {
		return chequeNo;
	}

	public String getContraDate() {
		return contraDate;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public int getCurrencyRate() {
		return currencyRate;
	}

	public String getDescription() {
		return description;
	}

	public String getEffectiveDate() {
		return effectiveDate;
	}

	public double getPrevBalance() {
		return prevBalance;
	}

	public String getPrevUpdate() {
		return prevUpdate;
	}

	public String getRemark() {
		return remark;
	}

	public int getSerialNo() {
		return serialNo;
	}

	public int getStatus() {
		return status;
	}

	public String getSubRef() {
		return subRef;
	}

	public String getSupervisorId() {
		return supervisorId;
	}

	public int getSystemCode() {
		return systemCode;
	}

	public String getTellerId() {
		return tellerId;
	}

	public String getTransDate() {
		return transDate;
	}

	public int getTransNo() {
		return TransNo;
	}

	public int getTransRef() {
		return transRef;
	}

	public String getTransTime() {
		return transTime;
	}

	public int getTransType() {
		return transType;
	}

	public String getWorkStation() {
		return workStation;
	}

	public void setAccNumber(String accNumber) {
		this.accNumber = accNumber;
	}

	public void setAccRef(String accRef) {
		this.accRef = accRef;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

	public void setChequeNo(String chequeNo) {
		this.chequeNo = chequeNo;
	}

	public void setContraDate(String contraDate) {
		this.contraDate = contraDate;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public void setCurrencyRate(int currencyRate) {
		this.currencyRate = currencyRate;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public void setPrevBalance(double prevBalance) {
		this.prevBalance = prevBalance;
	}

	public void setPrevUpdate(String prevUpdate) {
		this.prevUpdate = prevUpdate;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public void setSerialNo(int serialNo) {
		this.serialNo = serialNo;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public void setSubRef(String subRef) {
		this.subRef = subRef;
	}

	public void setSupervisorId(String supervisorId) {
		this.supervisorId = supervisorId;
	}

	public void setSystemCode(int systemCode) {
		this.systemCode = systemCode;
	}

	public void setTellerId(String tellerId) {
		this.tellerId = tellerId;
	}

	public void setTransDate(String transDate) {
		this.transDate = transDate;
	}

	public void setTransNo(int transNo) {
		TransNo = transNo;
	}

	public void setTransRef(int transRef) {
		this.transRef = transRef;
	}

	public void setTransTime(String transTime) {
		this.transTime = transTime;
	}

	public void setTransType(int transType) {
		this.transType = transType;
	}

	public void setWorkStation(String workStation) {
		this.workStation = workStation;
	}

}
