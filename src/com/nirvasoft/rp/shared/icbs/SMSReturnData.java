package com.nirvasoft.rp.shared.icbs;

import java.io.Serializable;

import com.nirvasoft.rp.shared.DataResult;

public class SMSReturnData extends DataResult implements Serializable {

	private static final long serialVersionUID = -348255555213538684L;
	private double availableBalance;
	private double currentBalance;
	private int errorFlag;
	private String errorCode;
	private String errorDescription;
	private String ToPhoneNo;
	private String FromPhoneNo;
	private int transferKey;
	private String debitIBTKey;
	private String creditIBTKey;
	private String effectiveDate;
	private double comm1;
	private double comm2;
	private int onlineSerialNo;
	private String ccy;

	public SMSReturnData() {
		clearProperty();
	}

	public void clearProperty() {
		availableBalance = 0;
		currentBalance = 0;
		errorFlag = 0;
		errorCode = "";
		errorDescription = "";
		ToPhoneNo = "";
		FromPhoneNo = "";

		transferKey = 0;
		debitIBTKey = "";
		creditIBTKey = "";
		effectiveDate = "01/01/1900";
		comm1 = 0;
		comm2 = 0;
		onlineSerialNo = 0;
		ccy = "MMK";
	}

	public double getAvailableBalnace() {
		return availableBalance;
	}

	public String getCcy() {
		return ccy;
	}

	public double getComm1() {
		return comm1;
	}

	public double getComm2() {
		return comm2;
	}

	public String getCreditIBTKey() {
		return creditIBTKey;
	}

	public double getCurrentBalance() {
		return currentBalance;
	}

	public String getDebitIBTKey() {
		return debitIBTKey;
	}

	public String getEffectiveDate() {
		return effectiveDate;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public String getErrorDescription() {
		return errorDescription;
	}

	@Override
	public int getErrorFlag() {
		return errorFlag;
	}

	public String getFromPhoneNo() {
		return FromPhoneNo;
	}

	public int getOnlineSerialNo() {
		return onlineSerialNo;
	}

	public String getToPhoneNo() {
		return ToPhoneNo;
	}

	public int getTransferKey() {
		return transferKey;
	}

	public void setAvailableBalnace(double availableBalance) {
		this.availableBalance = availableBalance;
	}

	public void setCcy(String ccy) {
		this.ccy = ccy;
	}

	public void setComm1(double comm1) {
		this.comm1 = comm1;
	}

	public void setComm2(double comm2) {
		this.comm2 = comm2;
	}

	public void setCreditIBTKey(String creditIBTKey) {
		this.creditIBTKey = creditIBTKey;
	}

	public void setCurrentBalance(double currentBalance) {
		this.currentBalance = currentBalance;
	}

	public void setDebitIBTKey(String debitIBTKey) {
		this.debitIBTKey = debitIBTKey;
	}

	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}

	@Override
	public void setErrorFlag(int errorFlag) {
		this.errorFlag = errorFlag;
	}

	public void setFromPhoneNo(String fromPhoneNo) {
		FromPhoneNo = fromPhoneNo;
	}

	public void setOnlineSerialNo(int onlineSerialNo) {
		this.onlineSerialNo = onlineSerialNo;
	}

	public void setToPhoneNo(String pPhoneNo) {
		this.ToPhoneNo = pPhoneNo;
	}

	public void setTransferKey(int transferKey) {
		this.transferKey = transferKey;
	}
}
