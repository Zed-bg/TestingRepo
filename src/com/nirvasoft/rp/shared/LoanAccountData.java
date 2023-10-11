package com.nirvasoft.rp.shared;


import java.io.Serializable;
import java.util.ArrayList;

public class LoanAccountData implements Serializable{
	
	private static final long serialVersionUID = -3543202794198371011L;
	private String AccNumber;
	private byte Term;
	private double Limit;
	private String ExpDate;
	private double IntToBeCollected;
	//private int BusinessType;	
	private int BusinessType;	
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
	private int BusinessTypeMain;
	//private int BusinessClass;
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
	private String productID;
	private double SCRate;
	private int LoanTypeNew;
	private double TotalSanctionAmtBr;
	private double sanctionAmt;
	private ArrayList<TempData> TempDataList;
	
	private double withdrawAmountFrmLoanLimit;
	
	public static int HistoryType_AllowLimit = 0;
	public static int HistoryType_LoanType = 1;
	public static int HistoryType_BizType = 2;
	public static int HistoryType_LimitIncrease = 3;
	public static int HistoryType_ExpDate = 4;
	public static int HistoryType_BODInfo = 5;
	public static int HistoryType_Repayment = 6;
	public static int HistoryType_RepaymentNoInt = 7;
	
	public static final int Status_Active = 1;
	public static final int Status_Cancel = 3;
	
	private boolean prinRepay;
	private boolean isFull;
	
	//FPB
	private String sanctionNo;
	private String sanctionDate;
	private String limitedDate;
	private double currentBalance;
	
	public String getSanctionNo() {
		return sanctionNo;
	}
	public void setSanctionNo(String sanctionNo) {
		this.sanctionNo = sanctionNo;
	}
	public String getSanctionDate() {
		return sanctionDate;
	}
	public void setSanctionDate(String sanctionDate) {
		this.sanctionDate = sanctionDate;
	}
	public String getLimitedDate() {
		return limitedDate;
	}
	public void setLimitedDate(String limitedDate) {
		this.limitedDate = limitedDate;
	}
	
	public LoanAccountData() {
		ClearProperty();
	}
	public String getAccNumber() {
		return AccNumber;
	}
	public void setAccNumber(String accNumber) {
		AccNumber = accNumber;
	}
	public byte getTerm() {
		return Term;
	}
	public void setTerm(byte term) {
		Term = term;
	}
	public double getLimit() {
		return Limit;
	}
	public void setLimit(double limit) {
		Limit = limit;
	}
	public String getExpDate() {
		return ExpDate;
	}
	public void setExpDate(String expDate) {
		ExpDate = expDate;
	}
	public double getIntToBeCollected() {
		return IntToBeCollected;
	}
	public void setIntToBeCollected(double intToBeCollected) {
		IntToBeCollected = intToBeCollected;
	}		
	public double getCollateral() {
		return Collateral;
	}
	public void setCollateral(double collateral) {
		Collateral = collateral;
	}
	public byte getLoanType() {
		return LoanType;
	}
	public void setLoanType(byte loanType) {
		LoanType = loanType;
	}
	public String getApproveNo() {
		return ApproveNo;
	}
	public void setApproveNo(String approveNo) {
		ApproveNo = approveNo;
	}
	public String getApproveId() {
		return ApproveId;
	}
	public void setApproveId(String approveId) {
		ApproveId = approveId;
	}
	public byte getSecurityType() {
		return SecurityType;
	}
	public void setSecurityType(byte securityType) {
		SecurityType = securityType;
	}
	public String getDrawnDownDate() {
		return DrawnDownDate;
	}
	public void setDrawnDownDate(String drawnDownDate) {
		DrawnDownDate = drawnDownDate;
	}
	public double getRepaymentAmount() {
		return RepaymentAmount;
	}
	public void setRepaymentAmount(double repaymentAmount) {
		RepaymentAmount = repaymentAmount;
	}
	public int getNoOfRepayment() {
		return NoOfRepayment;
	}
	public void setNoOfRepayment(int noOfRepayment) {
		NoOfRepayment = noOfRepayment;
	}
	public String getRepaymentStartDate() {
		return RepaymentStartDate;
	}
	public void setRepaymentStartDate(String repaymentStartDate) {
		RepaymentStartDate = repaymentStartDate;
	}
	public String getRepaymentDueDate() {
		return RepaymentDueDate;
	}
	public void setRepaymentDueDate(String repaymentDueDate) {
		RepaymentDueDate = repaymentDueDate;
	}
	
	public byte getIsCommit() {
		return IsCommit;
	}
	public void setIsCommit(byte isCommit) {
		IsCommit = isCommit;
	}
	
	public int getLoanTypeNew() {
		return LoanTypeNew;
	}
	public void setLoanTypeNew(int loanTypeNew) {
		LoanTypeNew = loanTypeNew;
	}
	public void ClearProperty() {
		AccNumber="";
		Term=(byte)0;
		Limit=0;
		ExpDate="";
		IntToBeCollected=0;
		BusinessType=(byte)0;
		Collateral=0;
		LoanType=(byte)0;
		ApproveNo="";
		ApproveId="";
		SecurityType=(byte)0;
		DrawnDownDate="";
		RepaymentAmount=0;
		NoOfRepayment=(byte)0;
		RepaymentStartDate="";
		RepaymentDueDate="";
		IsCommit=(byte)0;
		SanctionamountBr = (double)0;
		SanctionamountHO = (double)0;
		SanctionDateBr = "";
		SanctionDateHO = "";
		BackAccNumber = "";
		AccLinked = "";
		EntryDate = "";
		SanctionPrecent = (double)0;
		status = 0;
		SettleAmount = (double)0;
		batchno=0;
		CancelDate = "";
		DrPriority = 0;
		HistoryType = 0;
		Rate = (double)0;
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
		SCRate = (double)0;
		setBusinessTypeMain((byte)0);
		BusinessType=(byte)0;
		setTotalSanctionAmtBr(0);
		setTempDataList(new ArrayList<TempData>());
		setWithdrawAmountFrmLoanLimit(0);
		prinRepay = false;
		isFull = false;
		LoanTypeNew=0;
		productID = "";
		sanctionNo ="";
		sanctionDate ="";
		limitedDate ="";	
		sanctionAmt =0;
	}
	
	public double getSanctionAmountBr(){return this.SanctionamountBr;}
	public void setSanctionAmountBr(double SanctionAmountBr){this.SanctionamountBr = SanctionAmountBr;}
	
	public double getSanctionAmountHO(){return this.SanctionamountHO;}
	public void setSanctionAmountHO(double SanctionAmountHO){this.SanctionamountHO = SanctionAmountHO;}
	
	public String getSanctionDateBr(){return this.SanctionDateBr;}
	public void setSanctionDateBr(String SanctionDateBr){this.SanctionDateBr = SanctionDateBr;}
	
	public String getSanctionDateHO(){return this.SanctionDateHO;}
	public void setSanctionDateHO(String SanctionDateHO){this.SanctionDateHO = SanctionDateHO;}
	
	public String getBackAccNumber(){return this.BackAccNumber;}
	public void setBackAccNumber(String BackAccNumber) {this.BackAccNumber = BackAccNumber;}
	
	public String getAccLinked(){return this.AccLinked;}
	public void setAccLinked(String AccLinked){this.AccLinked = AccLinked;}
	
	public String getEntryDate(){return this.EntryDate;}
	public void setEntryDate(String EntryDate){this.EntryDate = EntryDate;}
	
	public double getSanctionPrecent(){return this.SanctionPrecent;}
	public void setSanctionPrecent(double SanctionPrecent){this.SanctionPrecent = SanctionPrecent;}
	
	public int getStatus(){return this.status;}
	public void setStatus(int status){this.status = status;}
	
	public double getSettleAmount(){return this.SettleAmount;}
	public void setSettleAmount(double SettleAmount){this.SettleAmount = SettleAmount;}
	
	public int getBatchNo(){return this.batchno;}
	public void setBatchNo(int batchno){this.batchno = batchno;}
	
	public String getCancelDate(){return this.CancelDate;}
	public void setCancelDate(String CancelDate){this.CancelDate = CancelDate;}
	
	public int getDrPriority(){return this.DrPriority;}
	public void setDrPriority(int DrPriority){this.DrPriority = DrPriority;}
	
	public int getHistoryType(){return this.HistoryType;}
	public void setHistoryType(int HistoryType){this.HistoryType = HistoryType;}
	
	public double getRate(){return this.Rate;}
	public void setRate(double p_Rate){this.Rate = p_Rate;}
	
	public int getCollateralSerialNo(){return this.CollateralSerialNo;}
	public void setCollateralSerialNo(int p_collateralserialno){this.CollateralSerialNo = p_collateralserialno;}
	
	public String getCurrencyCode() {
		return CurrencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		CurrencyCode = currencyCode;
	}
	public double getCurrencyRate() {
		return CurrencyRate;
	}
	public void setCurrencyRate(double currencyRate) {
		CurrencyRate = currencyRate;
	}
	public String getApprovalNoHO() {
		return ApprovalNoHO;
	}
	public void setApprovalNoHO(String approvalNoHO) {
		ApprovalNoHO = approvalNoHO;
	}
	public ArrayList<Integer> getCollateralSerialNoList() {
		return CollateralSerialNoList;
	}
	public void setCollateralSerialNoList(ArrayList<Integer> collateralSerialNoList) {
		CollateralSerialNoList = collateralSerialNoList;
	}
	public double getN1() {
		return N1;
	}
	public void setN1(double n1) {
		N1 = n1;
	}
	public double getN2() {
		return N2;
	}
	public void setN2(double n2) {
		N2 = n2;
	}
	public double getN3() {
		return N3;
	}
	public void setN3(double n3) {
		N3 = n3;
	}
	public String getT1() {
		return T1;
	}
	public void setT1(String t1) {
		T1 = t1;
	}
	public double getSCRate(){return this.SCRate;}
	public void setSCRate(double SCRate){this.SCRate = SCRate;}
	
	public Integer getBusinessTypeMain() {
		return BusinessTypeMain;
	}
	public void setBusinessTypeMain(int businessTypeMain) {
		BusinessTypeMain = businessTypeMain;
	}
	public double getTotalSanctionAmtBr() {
		return TotalSanctionAmtBr;
	}
	public void setTotalSanctionAmtBr(double totalSanctionAmtBr) {
		TotalSanctionAmtBr = totalSanctionAmtBr;
	}
	public String getT2() {
		return T2;
	}
	public void setT2(String t2) {
		T2 = t2;
	}
	public ArrayList<TempData> getTempDataList() {
		return TempDataList;
	}
	public void setTempDataList(ArrayList<TempData> tempDataList) {
		TempDataList = tempDataList;
	}
	public double getWithdrawAmountFrmLoanLimit() {
		return withdrawAmountFrmLoanLimit;
	}
	public void setWithdrawAmountFrmLoanLimit(double withdrawAmountFrmLoanLimit) {
		this.withdrawAmountFrmLoanLimit = withdrawAmountFrmLoanLimit;
	}
	public boolean isPrinRepay() {
		return prinRepay;
	}
	public void setPrinRepay(boolean prinRepay) {
		this.prinRepay = prinRepay;
	}
	public boolean isFull() {
		return isFull;
	}
	public void setFull(boolean isFull) {
		this.isFull = isFull;
	}
	public byte getBusinessClass() {
		return BusinessClass;
	}
	public void setBusinessClass(byte businessClass) {
		BusinessClass = businessClass;
	}
	public int getBusinessType() {
		return BusinessType;
	}
	public void setBusinessType(int businessType) {
		BusinessType = businessType;
	}
	public String getProductID() {
		return productID;
	}
	public void setProductID(String productID) {
		this.productID = productID;
	}
	public double getSanctionAmt() {
		return sanctionAmt;
	}
	public void setSanctionAmt(double sanctionAmt) {
		this.sanctionAmt = sanctionAmt;
	}
	public double getCurrentBalance() {
		return currentBalance;
	}
	public void setCurrentBalance(double currentBalance) {
		this.currentBalance = currentBalance;
	}			
}

