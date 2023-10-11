package com.nirvasoft.rp.shared.icbs;

import java.io.Serializable;
import java.util.ArrayList;

public class LoanAccountData implements Serializable {

	private static final long serialVersionUID = -3543202794198371011L;
	public static int HistoryType_AllowLimit = 0;
	public static int HistoryType_LoanType = 1;
	public static int HistoryType_BizType = 2;
	public static int HistoryType_LimitIncrease = 3;
	public static int HistoryType_ExpDate = 4;
	public static int HistoryType_BODInfo = 5;
	public static int HistoryType_Repayment = 6;
	public static final int Status_Active = 1;
	public static final int Status_Cancel = 3;
	private String AccNumber;
	private byte Term;
	private double Limit;
	private String ExpDate;
	private double IntToBeCollected;
	private byte BusinessType;
	private double Collateral;
	private byte LoanType;
	private String ApproveNo;
	private String ApproveId;

	private byte SecurityType;
	private String DrawnDownDate;
	private double RepaymentAmount;
	private int NoOfRepayment;
	private String RepaymentStartDate;
	private String RepaymentDueDate;
	private byte IsCommit;
	private String AccLinked;
	private double Rate;
	private double SanctionamountBr;
	private double SanctionamountHO;
	private String SanctionDateHO;
	private String SanctionDateBr;
	private String BackAccNumber;
	private String EntryDate;
	private double SanctionPrecent;
	private int status;
	private double SettleAmount;

	private int batchno;
	private String CancelDate;

	private int DrPriority;
	private int HistoryType;
	private int CollateralSerialNo;

	private byte BusinessTypeMain;
	private byte BusinessClass;
	private String ApprovalNoHO;

	private ArrayList<Integer> CollateralSerialNoList;
	private String CurrencyCode;

	private double CurrencyRate;

	private double N1;
	private double N2;
	private double N3;
	private String T1;
	private String T2;
	private double SCRate;
	private double TotalSanctionAmtBr;

	private ArrayList<TempData> TempDataList;
	private double withdrawAmountFrmLoanLimit;

	public LoanAccountData() {
		ClearProperty();
	}

	public void ClearProperty() {
		AccNumber = "";
		Term = (byte) 0;
		Limit = 0;
		ExpDate = "";
		IntToBeCollected = 0;
		BusinessType = (byte) 0;
		Collateral = 0;
		LoanType = (byte) 0;
		ApproveNo = "";
		ApproveId = "";
		SecurityType = (byte) 0;
		DrawnDownDate = "";
		RepaymentAmount = 0;
		NoOfRepayment = (byte) 0;
		RepaymentStartDate = "";
		RepaymentDueDate = "";
		IsCommit = (byte) 0;
		SanctionamountBr = 0;
		SanctionamountHO = 0;
		SanctionDateBr = "";
		SanctionDateHO = "";
		BackAccNumber = "";
		AccLinked = "";
		EntryDate = "";
		SanctionPrecent = 0;
		status = 0;
		SettleAmount = 0;
		batchno = 0;
		CancelDate = "";
		DrPriority = 0;
		HistoryType = 0;
		Rate = 0;
		CollateralSerialNo = 0;
		CollateralSerialNoList = new ArrayList<Integer>();
		setCurrencyCode("");
		setCurrencyRate(0);
		setApprovalNoHO("");
		setN1(0);
		setN2(0);
		setN3(0);
		setT1("");
		setT2("");
		SCRate = 0;
		setBusinessTypeMain((byte) 0);
		setBusinessClass((byte) 0);
		setTotalSanctionAmtBr(0);
		setTempDataList(new ArrayList<TempData>());
		setWithdrawAmountFrmLoanLimit(0);
	}

	public String getAccLinked() {
		return this.AccLinked;
	}

	public String getAccNumber() {
		return AccNumber;
	}

	public String getApprovalNoHO() {
		return ApprovalNoHO;
	}

	public String getApproveId() {
		return ApproveId;
	}

	public String getApproveNo() {
		return ApproveNo;
	}

	public String getBackAccNumber() {
		return this.BackAccNumber;
	}

	public int getBatchNo() {
		return this.batchno;
	}

	public byte getBusinessClass() {
		return BusinessClass;
	}

	public byte getBusinessType() {
		return BusinessType;
	}

	public byte getBusinessTypeMain() {
		return BusinessTypeMain;
	}

	public String getCancelDate() {
		return this.CancelDate;
	}

	public double getCollateral() {
		return Collateral;
	}

	public int getCollateralSerialNo() {
		return this.CollateralSerialNo;
	}

	public ArrayList<Integer> getCollateralSerialNoList() {
		return CollateralSerialNoList;
	}

	public String getCurrencyCode() {
		return CurrencyCode;
	}

	public double getCurrencyRate() {
		return CurrencyRate;
	}

	public String getDrawnDownDate() {
		return DrawnDownDate;
	}

	public int getDrPriority() {
		return this.DrPriority;
	}

	public String getEntryDate() {
		return this.EntryDate;
	}

	public String getExpDate() {
		return ExpDate;
	}

	public int getHistoryType() {
		return this.HistoryType;
	}

	public double getIntToBeCollected() {
		return IntToBeCollected;
	}

	public byte getIsCommit() {
		return IsCommit;
	}

	public double getLimit() {
		return Limit;
	}

	public byte getLoanType() {
		return LoanType;
	}

	public double getN1() {
		return N1;
	}

	public double getN2() {
		return N2;
	}

	public double getN3() {
		return N3;
	}

	public int getNoOfRepayment() {
		return NoOfRepayment;
	}

	public double getRate() {
		return this.Rate;
	}

	public double getRepaymentAmount() {
		return RepaymentAmount;
	}

	public String getRepaymentDueDate() {
		return RepaymentDueDate;
	}

	public String getRepaymentStartDate() {
		return RepaymentStartDate;
	}

	public double getSanctionAmountBr() {
		return this.SanctionamountBr;
	}

	public double getSanctionAmountHO() {
		return this.SanctionamountHO;
	}

	public String getSanctionDateBr() {
		return this.SanctionDateBr;
	}

	public String getSanctionDateHO() {
		return this.SanctionDateHO;
	}

	public double getSanctionPercent() {
		return this.SanctionPrecent;
	}

	public double getSCRate() {
		return this.SCRate;
	}

	public byte getSecurityType() {
		return SecurityType;
	}

	public double getSettleAmount() {
		return this.SettleAmount;
	}

	public int getStatus() {
		return this.status;
	}

	public String getT1() {
		return T1;
	}

	public String getT2() {
		return T2;
	}

	public ArrayList<TempData> getTempDataList() {
		return TempDataList;
	}

	public byte getTerm() {
		return Term;
	}

	public double getTotalSanctionAmtBr() {
		return TotalSanctionAmtBr;
	}

	public double getWithdrawAmountFrmLoanLimit() {
		return withdrawAmountFrmLoanLimit;
	}

	public void setAccLinked(String AccLinked) {
		this.AccLinked = AccLinked;
	}

	public void setAccNumber(String accNumber) {
		AccNumber = accNumber;
	}

	public void setApprovalNoHO(String approvalNoHO) {
		ApprovalNoHO = approvalNoHO;
	}

	public void setApproveId(String approveId) {
		ApproveId = approveId;
	}

	public void setApproveNo(String approveNo) {
		ApproveNo = approveNo;
	}

	public void setBackAccNumber(String BackAccNumber) {
		this.BackAccNumber = BackAccNumber;
	}

	public void setBatchNo(int batchno) {
		this.batchno = batchno;
	}

	public void setBusinessClass(byte businessClass) {
		BusinessClass = businessClass;
	}

	public void setBusinessType(byte businessType) {
		BusinessType = businessType;
	}

	public void setBusinessTypeMain(byte businessTypeMain) {
		BusinessTypeMain = businessTypeMain;
	}

	public void setCancelDate(String CancelDate) {
		this.CancelDate = CancelDate;
	}

	public void setCollateral(double collateral) {
		Collateral = collateral;
	}

	public void setCollateralSerialNo(int p_collateralserialno) {
		this.CollateralSerialNo = p_collateralserialno;
	}

	public void setCollateralSerialNoList(ArrayList<Integer> collateralSerialNoList) {
		CollateralSerialNoList = collateralSerialNoList;
	}

	public void setCurrencyCode(String currencyCode) {
		CurrencyCode = currencyCode;
	}

	public void setCurrencyRate(double currencyRate) {
		CurrencyRate = currencyRate;
	}

	public void setDrawnDownDate(String drawnDownDate) {
		DrawnDownDate = drawnDownDate;
	}

	public void setDrPriority(int DrPriority) {
		this.DrPriority = DrPriority;
	}

	public void setEntryDate(String EntryDate) {
		this.EntryDate = EntryDate;
	}

	public void setExpDate(String expDate) {
		ExpDate = expDate;
	}

	public void setHistoryType(int HistoryType) {
		this.HistoryType = HistoryType;
	}

	public void setIntToBeCollected(double intToBeCollected) {
		IntToBeCollected = intToBeCollected;
	}

	public void setIsCommit(byte isCommit) {
		IsCommit = isCommit;
	}

	public void setLimit(double limit) {
		Limit = limit;
	}

	public void setLoanType(byte loanType) {
		LoanType = loanType;
	}

	public void setN1(double n1) {
		N1 = n1;
	}

	public void setN2(double n2) {
		N2 = n2;
	}

	public void setN3(double n3) {
		N3 = n3;
	}

	public void setNoOfRepayment(int noOfRepayment) {
		NoOfRepayment = noOfRepayment;
	}

	public void setRate(double p_Rate) {
		this.Rate = p_Rate;
	}

	public void setRepaymentAmount(double repaymentAmount) {
		RepaymentAmount = repaymentAmount;
	}

	public void setRepaymentDueDate(String repaymentDueDate) {
		RepaymentDueDate = repaymentDueDate;
	}

	public void setRepaymentStartDate(String repaymentStartDate) {
		RepaymentStartDate = repaymentStartDate;
	}

	public void setSanctionAmountBr(double SanctionAmountBr) {
		this.SanctionamountBr = SanctionAmountBr;
	}

	public void setSanctionAmountHO(double SanctionAmountHO) {
		this.SanctionamountHO = SanctionAmountHO;
	}

	public void setSanctionDateBr(String SanctionDateBr) {
		this.SanctionDateBr = SanctionDateBr;
	}

	public void setSanctionDateHO(String SanctionDateHO) {
		this.SanctionDateHO = SanctionDateHO;
	}

	public void setSanctionPrecent(double SanctionPrecent) {
		this.SanctionPrecent = SanctionPrecent;
	}

	public void setSCRate(double SCRate) {
		this.SCRate = SCRate;
	}

	public void setSecurityType(byte securityType) {
		SecurityType = securityType;
	}

	public void setSettleAmount(double SettleAmount) {
		this.SettleAmount = SettleAmount;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public void setT1(String t1) {
		T1 = t1;
	}

	public void setT2(String t2) {
		T2 = t2;
	}

	public void setTempDataList(ArrayList<TempData> tempDataList) {
		TempDataList = tempDataList;
	}

	public void setTerm(byte term) {
		Term = term;
	}

	public void setTotalSanctionAmtBr(double totalSanctionAmtBr) {
		TotalSanctionAmtBr = totalSanctionAmtBr;
	}

	public void setWithdrawAmountFrmLoanLimit(double withdrawAmountFrmLoanLimit) {
		this.withdrawAmountFrmLoanLimit = withdrawAmountFrmLoanLimit;
	}
}
