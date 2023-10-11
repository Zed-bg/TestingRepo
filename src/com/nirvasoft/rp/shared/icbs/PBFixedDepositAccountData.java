package com.nirvasoft.rp.shared.icbs;

import java.io.Serializable;

public class PBFixedDepositAccountData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 210345969393098761L;
	private String AccNumber;
	private String RefNo;
	private String PreviousTransDate;
	private double InterestRate;
	private double Interest;
	private double Sundry;
	private double Accured;
	private Short Tenure;

	private String StartDate;
	private String DueDate;
	private double IntToBePaid;
	private short IntPayCode;
	private String AccToBePaid;
	private boolean AutoRenew;
	private String ReceiptNo;
	private byte OtherFacility;
	private double Amount;
	private String LastTransDate;
	private byte Status;
	private String OpeningDate;
	private String ClosingDate;
	private String ApprovalNo;
	private int TotalDays;
	private double YearRate;

	public PBFixedDepositAccountData() {
		ClearProperty();
	}

	public void ClearProperty() {
		PreviousTransDate = "";
		InterestRate = 0.0;
		Interest = 0.0;
		Sundry = 0.0;
		Accured = 0.0;
		AccNumber = "";
		RefNo = "";
		Tenure = (byte) 0;
		StartDate = "";
		DueDate = "";
		IntToBePaid = 0;
		IntPayCode = (byte) 0;
		AccToBePaid = "";
		AutoRenew = false;
		ReceiptNo = "";
		OtherFacility = (byte) 0;
		Amount = 0;
		LastTransDate = "";
		Status = (byte) 0;
		OpeningDate = "";
		ClosingDate = "";
		ApprovalNo = "";
		TotalDays = 0;
		YearRate = 0.0;
	}

	public String getAccNumber() {
		return AccNumber;
	}

	public String getAccToBePaid() {
		return AccToBePaid;
	}

	public double getAccured() {
		return Accured;
	}

	public double getAmount() {
		return Amount;
	}

	public String getApprovalNo() {
		return ApprovalNo;
	}

	public boolean getAutoRenew() {
		return AutoRenew;
	}

	public String getClosingDate() {
		return ClosingDate;
	}

	public String getDueDate() {
		return DueDate;
	}

	public double getInterest() {
		return Interest;
	}

	public double getInterestRate() {
		return InterestRate;
	}

	public short getIntPayCode() {
		return IntPayCode;
	}

	public double getIntToBePaid() {
		return IntToBePaid;
	}

	public String getLastTransDate() {
		return LastTransDate;
	}

	public String getOpeningDate() {
		return OpeningDate;
	}

	public byte getOtherFacility() {
		return OtherFacility;
	}

	public String getPreviousTransDate() {
		return PreviousTransDate;
	}

	public String getReceiptNo() {
		return ReceiptNo;
	}

	public String getRefNo() {
		return RefNo;
	}

	public String getStartDate() {
		return StartDate;
	}

	public byte getStatus() {
		return Status;
	}

	public Double getSundry() {
		return Sundry;
	}

	public Short getTenure() {
		return Tenure;
	}

	public int getTotalDays() {
		return TotalDays;
	}

	public double getYearRate() {
		return YearRate;
	}

	public void setAccNumber(String accNumber) {
		AccNumber = accNumber;
	}

	public void setAccToBePaid(String accToBePaid) {
		AccToBePaid = accToBePaid;
	}

	public void setAccured(double accured) {
		Accured = accured;
	}

	public void setAmount(double amount) {
		Amount = amount;
	}

	public void setApprovalNo(String approvalNo) {
		ApprovalNo = approvalNo;
	}

	public void setAutoRenew(boolean autoRenew) {
		AutoRenew = autoRenew;
	}

	public void setClosingDate(String closingDate) {
		ClosingDate = closingDate;
	}

	public void setDueDate(String dueDate) {
		DueDate = dueDate;
	}

	public void setInterest(double interest) {
		Interest = interest;
	}

	public void setInterestRate(double interestRate) {
		InterestRate = interestRate;
	}

	public void setIntPayCode(short intPayCode) {
		IntPayCode = intPayCode;
	}

	public void setIntToBePaid(double intToBePaid) {
		IntToBePaid = intToBePaid;
	}

	public void setLastTransDate(String lastTransDate) {
		LastTransDate = lastTransDate;
	}

	public void setOpeningDate(String openingDate) {
		OpeningDate = openingDate;
	}

	public void setOtherFacility(byte otherFacility) {
		OtherFacility = otherFacility;
	}

	public void setPreviousTransDate(String previousTransDate) {
		PreviousTransDate = previousTransDate;
	}

	public void setReceiptNo(String receiptNo) {
		ReceiptNo = receiptNo;
	}

	public void setRefNo(String refNo) {
		RefNo = refNo;
	}

	public void setStartDate(String startDate) {
		StartDate = startDate;
	}

	public void setStatus(byte status) {
		Status = status;
	}

	public void setSundry(Double sundry) {
		Sundry = sundry;
	}

	public void setTenure(Short tenure) {
		Tenure = tenure;
	}

	public void setTotalDays(int totalDays) {
		TotalDays = totalDays;
	}

	public void setYearRate(double yearRate) {
		YearRate = yearRate;
	}

}
