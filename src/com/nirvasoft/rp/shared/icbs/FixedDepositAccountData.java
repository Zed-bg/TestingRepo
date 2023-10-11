package com.nirvasoft.rp.shared.icbs;

import java.io.Serializable;

public class FixedDepositAccountData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3804285295873802139L;

	private String pAccNumber;
	private Short pTenure;
	private String pStartDate;
	private String pDueDate;
	private double pIntToBePaid;
	private Short pIntPayCode;
	private String pAccToBePaid;
	private boolean pAutoRenew;
	private String pReceiptNo;

	private String pRefNo;
	private byte pOtherFacility;
	private double pAmount;
	private String pLastTransDate;
	private byte pStatus;
	private String pOpeningDate;
	private String pClosingDate;
	private String pApprovalNo;
	private int pTotalDays;
	private double pYearlyRate;
	private double pInterestAmt;
	private double pAccruedAmt;
	private String pCurrencyCode;
	private double pCurrencyRate;
	private String pBranchCode;
	private String pCounterID;

	private String pPrincipalAccount;
	private String pInterestAccount;
	private String pAccruedEndDate;
	private String pProductType;

	public FixedDepositAccountData() {
		clearProperty();
	}

	public void clearProperty() {
		pAccNumber = "";
		pTenure = 0;
		pStartDate = "";
		pDueDate = "";
		pIntToBePaid = 0;
		pIntPayCode = 0;
		pAccToBePaid = "";
		pAutoRenew = false;
		pReceiptNo = "0";

		pRefNo = "";
		pOtherFacility = (byte) 0;
		pAmount = 0;
		pLastTransDate = "";
		pStatus = (byte) 0;
		pOpeningDate = "";
		pClosingDate = "";
		pApprovalNo = "";
		pTotalDays = 0;
		pYearlyRate = 0;
		pInterestAmt = 0;
		pAccruedAmt = 0;
		pCurrencyCode = "";
		pCurrencyRate = 0;
		pBranchCode = "";
		pCounterID = "";
		pPrincipalAccount = "";
		pInterestAccount = "";
		pAccruedEndDate = "";
		pProductType = "";
	}

	public String getAccountNumber() {
		return pAccNumber;
	}

	public double getAccruedAmt() {
		return pAccruedAmt;
	}

	public String getAccruedEndDate() {
		return pAccruedEndDate;
	}

	public String getAccToBePaid() {
		return pAccToBePaid;
	}

	public double getAmount() {
		return pAmount;
	}

	public String getApprovalNo() {
		return pApprovalNo;
	}

	public boolean getAutoRenew() {
		return pAutoRenew;
	}

	public String getBranchCode() {
		return pBranchCode;
	}

	public String getClosingDate() {
		return pClosingDate;
	}

	public String getCounterID() {
		return pCounterID;
	}

	public String getCurrencyCode() {
		return pCurrencyCode;
	}

	public double getCurrencyRate() {
		return pCurrencyRate;
	}

	public String getDueDate() {
		return pDueDate;
	}

	public String getInterestAccount() {
		return pInterestAccount;
	}

	public double getInterestAmt() {
		return pInterestAmt;
	}

	public Short getIntPayCode() {
		return pIntPayCode;
	}

	public double getIntToBePaid() {
		return pIntToBePaid;
	}

	public String getLastTransDate() {
		return pLastTransDate;
	}

	public String getOpeningDate() {
		return pOpeningDate;
	}

	public byte getOtherFacility() {
		return pOtherFacility;
	}

	public String getPrincipalAccount() {
		return pPrincipalAccount;
	}

	public String getProductType() {
		return pProductType;
	}

	public String getReceiptNo() {
		return pReceiptNo;
	}

	public String getRefNo() {
		return pRefNo;
	}

	public String getStartDate() {
		return pStartDate;
	}

	public byte getStatus() {
		return pStatus;
	}

	public Short getTenure() {
		return pTenure;
	}

	public int getTotalDays() {
		return pTotalDays;
	}

	public double getYearlyRate() {
		return pYearlyRate;
	}

	public void setAccountNumber(String pAccNumber) {
		this.pAccNumber = pAccNumber;
	}

	public void setAccruedAmt(double pAccruedAmt) {
		this.pAccruedAmt = pAccruedAmt;
	}

	public void setAccruedEndDate(String accruedEndDate) {
		pAccruedEndDate = accruedEndDate;
	}

	public void setAccToBePaid(String pAccToBePaid) {
		this.pAccToBePaid = pAccToBePaid;
	}

	public void setAmount(double pAmount) {
		this.pAmount = pAmount;
	}

	public void setApprovalNo(String pApprovalNo) {
		this.pApprovalNo = pApprovalNo;
	}

	public void setAutoRenew(boolean pAutoRenew) {
		this.pAutoRenew = pAutoRenew;
	}

	public void setBranchCode(String pBranchCode) {
		this.pBranchCode = pBranchCode;
	}

	public void setClosingDate(String pClosingDate) {
		this.pClosingDate = pClosingDate;
	}

	public void setCounterID(String pCounterID) {
		this.pCounterID = pCounterID;
	}

	public void setCurrencyCode(String pCurrencyCode) {
		this.pCurrencyCode = pCurrencyCode;
	}

	public void setCurrencyRate(double pCurrencyRate) {
		this.pCurrencyRate = pCurrencyRate;
	}

	public void setDueDate(String pDueDate) {
		this.pDueDate = pDueDate;
	}

	public void setInterestAccount(String pInterestAccount) {
		this.pInterestAccount = pInterestAccount;
	}

	public void setInterestAmt(double pInterestAmt) {
		this.pInterestAmt = pInterestAmt;
	}

	public void setIntPayCode(Short pIntPayCode) {
		this.pIntPayCode = pIntPayCode;
	}

	public void setIntToBePaid(double pIntToBePaid) {
		this.pIntToBePaid = pIntToBePaid;
	}

	public void setLastTransDate(String pLastTransDate) {
		this.pLastTransDate = pLastTransDate;
	}

	public void setOpeningDate(String pOpeningDate) {
		this.pOpeningDate = pOpeningDate;
	}

	public void setOtherFacility(byte pOtherFacility) {
		this.pOtherFacility = pOtherFacility;
	}

	public void setPrincipalAccount(String pPrincipalAccount) {
		this.pPrincipalAccount = pPrincipalAccount;
	}

	public void setProductType(String pProductType) {
		this.pProductType = pProductType;
	}

	public void setReceiptNo(String pReceiptNo) {
		this.pReceiptNo = pReceiptNo;
	}

	public void setRefNo(String pRefNo) {
		this.pRefNo = pRefNo;
	}

	public void setStartDate(String pStartDate) {
		this.pStartDate = pStartDate;
	}

	public void setStatus(byte pStatus) {
		this.pStatus = pStatus;
	}

	public void setTenure(Short pTenure) {
		this.pTenure = pTenure;
	}

	public void setTotalDays(int pTotalDays) {
		this.pTotalDays = pTotalDays;
	}

	public void setYearlyRate(double pYearlyRate) {
		this.pYearlyRate = pYearlyRate;
	}
}
