package com.nirvasoft.rp.shared.icbs;

import java.util.ArrayList;

import com.nirvasoft.rp.shared.AccountCustomerData;
import com.nirvasoft.rp.shared.AddressData;
import com.nirvasoft.rp.shared.CallDepositAccountData;
import com.nirvasoft.rp.shared.CallDepositInterestData;
import com.nirvasoft.rp.shared.CurrentInterestData;
import com.nirvasoft.rp.shared.SavingInterestData;
import com.nirvasoft.rp.shared.SignatureData;
import com.nirvasoft.rp.shared.SpecialDepositAccountData;
import com.nirvasoft.rp.shared.SpecialInterestData;
import com.nirvasoft.rp.shared.UniversalInterestData;
import com.nirvasoft.rp.shared.AccBarData;
import com.nirvasoft.rp.shared.FixedDepositAccountData;
import com.nirvasoft.rp.shared.MobileUserData;
import com.nirvasoft.rp.shared.PBFixedDepositAccountData;
import com.nirvasoft.rp.shared.AccountLinksData;
import com.nirvasoft.rp.shared.CheckIssueData;

public class AccountData {

	public static int Status_New = 0;

	public static int Status_Active = 0;
	public static int Status_Suspend = 1;
	public static int Status_ClosePending = 2;
	public static int Status_Closed = 3;
	public static int Status_Dormant = 4;
	public static int Status_Lost = 5;
	public static int Status_Damage = 6;
	public static int Status_StoppedPayment = 7;
	private String pAccountNumber = "";

	private String pProduct = "";
	private String pType = "";
	private String pBranchCode = "";
	private int pZoneCode;
	private String pCurrencyCode = "";
	private double pCurRate;
	private double pOpeningBalance;
	private double pMinimumBalance;
	private String pOpeningDate = "";
	private double pCurrentBalance;
	private String pClosingDate = "";
	private String pLastUpdate = "";
	private int pStatus;
	private String pLastTransDate = "";
	private String pDrawingType = "";
	private String pAccountName = "";
	private String pAccountDescription = "";
	private String pAccountID = "";
	private String pShortAccountCode = "";
	private String pPassbook = "";
	private String pCardNumber = "";
	private int pCardPriority;
	private double pCardLimit;
	private String pRemark = "";
	private String pCashInHandGL = "";
	private String pProductGL = "";
	// saving account
	private double IntToBePaid;

	// current accont
	private double OdLimit;

	private String OdExpDate = "";
	private double IntToBeCollected;
	private int BusinessType;
	private double Collateral;
	private String ApproveNo = "";
	private String ApproveId = "";
	private int SecurityType;
	private int IsOD;
	private String SanctionDate = "";
	private int ODStatus;
	private double TODLimit;
	private String TODExpDate = "";
	private String TODSanctionDate = "";
	private double SystemTOD;
	private int IsCommit;
	private double pAvailableBalance;
	// CLHeader
	private int LastTransNo;

	private int LastLineNo;
	private int LastPage;
	// PBHeader
	private int BeginTransNo;

	// PassBookHistory
	private String PBSerialNo = "";
	private String IssueDate = "";
	private int PBStatus;
	private int zone;
	private double pInterest;
	private double pInterestRate;
	private int pRef;
	private double pTenure;
	private ArrayList<AccBarData> pAccBarDatas;
	// public CreditCardlimitAmountData crditcardData = new
	// CreditCardlimitAmountData();
	// Card A/C
	private String pCardAccountNumber;

	// AccountRevaluate
	private double pBaseBalance = 0;

	private double pBaseBalanceWithCurrencyRate = 0;
	private double pDifferentBaseBalance = 0;
	ArrayList<PBFixedDepositAccountData> ppbFixedDeAcDataList;
	private PBFixedDepositAccountData pbFixedDeAcData;
	// Fixed Deposit ==> WHA
	private FixedDepositAccountData pFixedDepositAccountData;
	private ArrayList<AccountLinksData> pAccountLinkDatas;
	private boolean isExpire;
	private ArrayList<AccountCustomerData> pAccountCustomers;

	private boolean HasSystemOD;
	private Short pBusinessClass=((short)0);
	private boolean pLoanBackAccount = false;
	private String pStatusDesc;
	
	
	private boolean schedule ;
	private byte ODType;
	private byte loanType;
	private double pUniversalAmount;
	private CurrentInterestData CAData;
	private String pLastDepositDate;
	private CallDepositAccountData pCallDepositAccount;
	private String accToBePaid;
	private SavingInterestData SavAcData;
	private SpecialInterestData SpecialData;
	private CallDepositInterestData CallInterestData;
	private UniversalInterestData UniversalData;
	private boolean isBlacklist;
	private boolean isBlacklistAcc;
	private ArrayList<SignatureData> lstSignData;
	private AddressData pMailingAddress;
	private ArrayList<CheckIssueData> pCheckIssue;
	private SignatureData pSignatureData;
	private SpecialDepositAccountData pSpecialDepositAccount;
	MobileUserData mobileUserData = new MobileUserData();
	private boolean isSigSave;
	
	public AccountData() {
		ClearProperty();
	}

	private void ClearProperty() {
		pCurrencyCode = "MMK";
		pZoneCode = 0;
		pCurRate = 1;
		pStatus = 0;
		pAccountID = "";
		pAccountName = "";
		pDrawingType = "";
		pShortAccountCode = "";
		pRemark = "";
		IntToBePaid = 0;
		ApproveId = "";
		ApproveNo = "";
		LastTransNo = 0;
		LastLineNo = 0;
		LastPage = 0;
		PBSerialNo = "1";
		PBStatus = 1;
		pCashInHandGL = "";
		pProductGL = "";
		pCardAccountNumber = "";
		pAccountNumber = "";
		pBaseBalance = 0;
		pBaseBalanceWithCurrencyRate = 0;
		pDifferentBaseBalance = 0;

		isExpire = false;
		
		setHasSystemOD(false);
		pBusinessClass = ((short)0);//atds
		pLoanBackAccount = false;
		pStatusDesc = "";
		
		
		schedule = false;
		ODType = 0;
		loanType = 0;
		pUniversalAmount = 0.0;
		CAData = null;
		pLastDepositDate = "";
		pCallDepositAccount = null;
		accToBePaid = "";
		SavAcData = null;
		SpecialData = null;
		CallInterestData = null;
		UniversalData = null;
		isBlacklist = false;
		isBlacklistAcc = false;
		lstSignData = null;
		pMailingAddress = null;
		pCheckIssue = null;
		pSignatureData = new SignatureData();
		pSpecialDepositAccount = new SpecialDepositAccountData();
		isSigSave = false;
		pFixedDepositAccountData = new FixedDepositAccountData();
		pbFixedDeAcData=new PBFixedDepositAccountData();
		pCallDepositAccount = new CallDepositAccountData();
	}

	public ArrayList<AccBarData> getAccountBarDatas() {
		return pAccBarDatas;
	}

	public ArrayList<AccountCustomerData> getAccountCustomers() {
		return pAccountCustomers;
	}

	public String getAccountDescription() {
		return pAccountDescription;
	}

	public String getAccountID() {
		return pAccountID;
	}

	public ArrayList<AccountLinksData> getAccountLinkDatas() {
		return pAccountLinkDatas;
	}

	public String getAccountName() {
		return pAccountName;
	}

	public String getAccountNumber() {
		return pAccountNumber;
	}

	public String getApproveID() {
		return ApproveId;
	}

	public String getApproveNo() {
		return ApproveNo;
	}

	public double getAvailableBalance() {
		return pAvailableBalance;
	}

	public double getBaseBalancce() {
		return pBaseBalance;
	}

	public double getBaseBalanceWithCurrencyRate() {
		return pBaseBalanceWithCurrencyRate;
	}

	public int getBeginTransNo() {
		return BeginTransNo;
	}

	public String getBranchCode() {
		return pBranchCode;
	}

	public int getBusinessType() {
		return BusinessType;
	}

	public String getCardAccountNumber() {
		return pCardAccountNumber;
	}

	public double getCardLimit() {
		return pCardLimit;
	}

	public String getCardNumber() {
		return pCardNumber;
	}

	public int getCardPriority() {
		return pCardPriority;
	}

	public String getCashInHandGL() {
		return pCashInHandGL;
	}

	public String getClosingDate() {
		return pClosingDate;
	}

	public double getCollateral() {
		return Collateral;
	}

	public String getCurrencyCode() {
		return pCurrencyCode;
	}

	public double getCurrencyRate() {
		return pCurRate;
	}

	public double getCurrentBalance() {
		return pCurrentBalance;
	}

	public double getDifferentBaseBalance() {
		return pDifferentBaseBalance;
	}

	public String getDrawingType() {
		return pDrawingType;
	}

	public FixedDepositAccountData getFixedDepositAccount() {
		return pFixedDepositAccountData;
	}

	public double getInterest() {
		return pInterest;
	}

	public double getInterestRate() {
		return pInterestRate;
	}

	public double getIntToBeCollected() {
		return IntToBeCollected;
	}

	public double getIntToBePaid() {
		return IntToBePaid;
	}

	public int getIsCommit() {
		return IsCommit;
	}

	public int getIsOD() {
		return IsOD;
	}

	public String getIssueDate() {
		return IssueDate;
	}

	public int getLastLineNo() {
		return LastLineNo;
	}

	public int getLastPage() {
		return LastPage;
	}

	public String getLastTransDate() {
		return pLastTransDate;
	}

	public int getLastTransNo() {
		return LastTransNo;
	}

	public String getLastUpdate() {
		return pLastUpdate;
	}

	public double getMinimumBalance() {
		return pMinimumBalance;
	}

	public String getOdExpDate() {
		return OdExpDate;
	}

	public double getOdLimit() {
		return OdLimit;
	}

	public int getODStatus() {
		return ODStatus;
	}

	public double getOpeningBalance() {
		return pOpeningBalance;
	}

	public String getOpeningDate() {
		return pOpeningDate;
	}

	public String getPassbook() {
		return pPassbook;
	}

	public PBFixedDepositAccountData getPBFixedDepAccountData() {
		return pbFixedDeAcData;
	}

	public String getPBSerialNo() {
		return PBSerialNo;
	}

	public int getPBStatus() {
		return PBStatus;
	}

	public String getProduct() {
		return pProduct;
	}

	public String getProductGL() {
		return pProductGL;
	}

	public int getRef() {
		return pRef;
	}

	public String getRemark() {
		return pRemark;
	}

	public String getSanctionDate() {
		return SanctionDate;
	}

	public int getSecurityType() {
		return SecurityType;
	}

	public String getShortAccountCode() {
		return pShortAccountCode;
	}

	public int getStatus() {
		return pStatus;
	}

	public double getSystemTOD() {
		return SystemTOD;
	}

	public double getTenure() {
		return pTenure;
	}

	public String getTODExpDate() {
		return TODExpDate;
	}

	public double getTODLimit() {
		return TODLimit;
	}

	public String getTODSanctionDate() {
		return TODSanctionDate;
	}

	public String getType() {
		return pType;
	}

	public int getZone() {
		return zone;
	}

	public int getZoneCode() {
		return pZoneCode;
	}

	/*
	 * public CreditCardlimitAmountData getCrditcardData() { return
	 * crditcardData; } public void setCrditcardData(CreditCardlimitAmountData
	 * crditcardData) { this.crditcardData = crditcardData; }
	 */
	public boolean isExpire() {
		return isExpire;
	}

	public void setAccountBarDatas(ArrayList<AccBarData> p) {
		pAccBarDatas = p;
	}

	public void setAccountCustomers(ArrayList<AccountCustomerData> p) {
		pAccountCustomers = p;
	}

	public void setAccountDescription(String p) {
		pAccountDescription = p;
	}

	public void setAccountID(String p) {
		pAccountID = p;
	}

	public void setAccountLinkDatas(ArrayList<AccountLinksData> pAccountLinkDatas) {
		this.pAccountLinkDatas = pAccountLinkDatas;
	}

	public void setAccountName(String p) {
		pAccountName = p;
	}

	public void setAccountNumber(String p) {
		pAccountNumber = p;
	}

	public void setApproveID(String p) {
		this.ApproveId = p;
	}

	public void setApproveNo(String p) {
		this.ApproveNo = p;
	}

	public void setAvailableBalance(double pAvailableBalance) {
		this.pAvailableBalance = pAvailableBalance;
	}

	public void setBaseBalance(double p) {
		pBaseBalance = p;
	}

	public void setBaseBalanceWithCurrencyRate(double p) {
		pBaseBalanceWithCurrencyRate = p;
	}

	public void setBeginTransNo(int p) {
		this.BeginTransNo = p;
	}

	public void setBranchCode(String p) {
		pBranchCode = p;
	}

	public void setBusinessType(int p) {
		this.BusinessType = p;
	}

	public void setCardAccountNumber(String pCardAccountNumber) {
		this.pCardAccountNumber = pCardAccountNumber;
	}

	public void setCardLimit(double p) {
		pCardLimit = p;
	}

	public void setCardNumber(String p) {
		pCardNumber = p;
	}

	public void setCardPriority(int p) {
		pCardPriority = p;
	}

	public void setCashInHandGL(String p) {
		pCashInHandGL = p;
	}
	
	public void setClosingDate(String p) {
		pClosingDate = p;
	}

	public void setCollateral(double p) {
		this.Collateral = p;
	}

	public void setCurrencyCode(String p) {
		pCurrencyCode = p;
	}

	public void setCurrencyRate(double p) {
		pCurRate = p;
	}

	public void setCurrentBalance(double p) {
		pCurrentBalance = p;
	}

	public void setDifferentBaseBalance(double p) {
		pDifferentBaseBalance = p;
	}

	public void setDrawingType(String p) {
		pDrawingType = p;
	}

	public void setExpire(boolean isExpire) {
		this.isExpire = isExpire;
	}

	public void setFixedDepositAccount(FixedDepositAccountData p) {
		pFixedDepositAccountData = p;
	}

	public void setInterest(double pInterest) {
		this.pInterest = pInterest;
	}

	public void setInterestRate(double pInterestRate) {
		this.pInterestRate = pInterestRate;
	}

	public void setIntToBeCollected(double p) {
		this.IntToBeCollected = p;
	}

	public void setIntToBePaid(double p) {
		this.IntToBePaid = p;
	}

	public void setIsCommit(int p) {
		this.IsCommit = p;
	}

	public void setIsOD(int p) {
		this.IsOD = p;
	}

	public void setIssueDate(String p) {
		this.IssueDate = p;
	}

	public void setLastLineNo(int p) {
		this.LastLineNo = p;
	}

	public void setLastPage(int p) {
		this.LastPage = p;
	}

	public void setLastTransDate(String p) {
		pLastTransDate = p;
	}

	public void setLastTransNo(int p) {
		this.LastTransNo = p;
	}

	public void setLastUpdate(String p) {
		pLastUpdate = p;
	}

	public void setMinimumBalance(double p) {
		pMinimumBalance = p;
	}

	public void setOdExpDate(String p) {
		this.OdExpDate = p;
	}

	public void setOdLimit(double p) {
		this.OdLimit = p;
	}

	public void setODStatus(int p) {
		this.ODStatus = p;
	}

	public void setOpeningBalance(double p) {
		pOpeningBalance = p;
	}

	public void setOpeningDate(String p) {
		pOpeningDate = p;
	}

	public void setPassbook(String p) {
		pPassbook = p;
	}

	public void setPBFixedDepAccountData(PBFixedDepositAccountData pbFixedDeAcData) {
		this.pbFixedDeAcData = pbFixedDeAcData;
	}

	public void setPBSerialNo(String p) {
		this.PBSerialNo = p;
	}

	public void setPBStatus(int p) {
		this.PBStatus = p;
	}

	public void setProduct(String p) {
		pProduct = p;
	}

	public void setProductGL(String p) {
		pProductGL = p;
	}

	public void setRef(int pRef) {
		this.pRef = pRef;
	}

	public void setRemark(String p) {
		pRemark = p;
	}

	public void setSanctionDate(String p) {
		this.SanctionDate = p;
	}

	public void setSecurityType(int p) {
		this.SecurityType = p;
	}

	public void setShortAccountCode(String p) {
		pShortAccountCode = p;
	}

	public void setStatus(int i) {
		pStatus = i;
	}

	public void setSystemTOD(double p) {
		this.SystemTOD = p;
	}

	public void setTenure(double pTenure) {
		this.pTenure = pTenure;
	}

	public void setTODExpDate(String p) {
		this.TODExpDate = p;
	}

	public void setTODLimit(double p) {
		this.TODLimit = p;
	}

	public void setTODSanctionDate(String p) {
		this.TODSanctionDate = p;
	}

	public void setType(String p) {
		pType = p;
	}	

	public void setZone(int zone) {
		this.zone = zone;
	}

	public void setZoneCode(int p) {
		this.pZoneCode = p;
	}
	
	public boolean isHasSystemOD() {
		return HasSystemOD;
	}
	public void setHasSystemOD(boolean hasSystemOD) {
		HasSystemOD = hasSystemOD;
	}
	
	public Short getBusinessClass() {
		return pBusinessClass;
	}
	
	public void setBusinessClass(Short pBusinessClass) {
		this.pBusinessClass = pBusinessClass;
	}
	
	public boolean isLoanBackAccount() {
		return pLoanBackAccount;
	}
	
	public void setLoanBackAccount(boolean pLoanBackAccount) {
		this.pLoanBackAccount = pLoanBackAccount;
	}

	public String getpStatusDesc() {
		return pStatusDesc;
	}

	public void setpStatusDesc(String pStatusDesc) {
		this.pStatusDesc = pStatusDesc;
	}

	public boolean isSchedule() {
		return schedule;
	}

	public void setSchedule(boolean schedule) {
		this.schedule = schedule;
	}
	
	public byte getODType() {
		return ODType;
	}
	
	public void setODType(byte oDType) {
		this.ODType = oDType;
	}
	
	public byte getLoanType() {
		return loanType;
	}
	
	public void setLoanType(byte loanType) {
		this.loanType = loanType;
	}
	
	public double getpUniversalAmount() {
		return pUniversalAmount;
	}
	
	public void setpUniversalAmount(double pUniversalAmount) {
		this.pUniversalAmount = pUniversalAmount;
	}
	
	public CurrentInterestData getCurrentAccountData() {
		return CAData;	
	}
	
	public void setCurrentAccountData(CurrentInterestData caData) {
		CAData = caData;	
	}
	
	public String getLastDepositDate(){
		return pLastDepositDate;
	}
	
	public void setLastDepositDate(String p){
		this.pLastDepositDate=p;
	}
	
	public CallDepositAccountData getCallDepositAccount() {
		return pCallDepositAccount;
	}
	
	public void setCallDepositAccount( CallDepositAccountData pCallDepositAccount ) {
		this.pCallDepositAccount = pCallDepositAccount;
	}
	
	public String getAccToBePaid() {
		return accToBePaid;
	}
	
	public void setAccToBePaid(String accToBePaid) {
		this.accToBePaid = accToBePaid;
	}
	
	public SavingInterestData getSavingAccountData() {
		return SavAcData;	
	}
	public void setSavingAccountData(SavingInterestData savAcData) {
		this.SavAcData = savAcData;	
	}
	
	public SpecialInterestData getSpecialAccountData() {
		return SpecialData; 
	}
	
	public void setSpecialAccountData(SpecialInterestData speAcData) {
		SpecialData = speAcData;	
	}
	
	public CallDepositInterestData getCallAccountData() {	
		return CallInterestData; 
	}
	
	public void setCallAccountData(CallDepositInterestData callInterestData) {	
		CallInterestData = callInterestData; 
	}
	
	public UniversalInterestData getUniversalData() {
		return UniversalData;
	}
	
	public void setUniversalData(UniversalInterestData universalData) {
		UniversalData = universalData;
	}
	
	public boolean isBlacklist() {
		return isBlacklist;
	}
	
	public void setBlacklist(boolean isBlacklist) {
		this.isBlacklist = isBlacklist;
	}
	
	public boolean isBlacklistAcc() {
		return isBlacklistAcc;
	}
	
	public void setBlacklistAcc(boolean isBlacklistAcc) {
		this.isBlacklistAcc = isBlacklistAcc;
	}	
	
	public ArrayList<SignatureData> getLstSignData() {
		return lstSignData;
	}
	
	public void setLstSignData(ArrayList<SignatureData> lstSignData) {
		this.lstSignData = lstSignData;
	}
	
	public void setMailingAddress(AddressData p) { 
		pMailingAddress = p; 
	}
	
	public AddressData getMailingAddress() {
		return pMailingAddress; 
	}
	
	public void setCheckIssue(ArrayList<CheckIssueData> pCheckIssue) {
		this.pCheckIssue = pCheckIssue;
	}
	
	public ArrayList<CheckIssueData> getCheckIssue() {
		return pCheckIssue;
	}
	
	public SignatureData getSignatureData() {return pSignatureData; }
	
	public void setSignatureData(SignatureData p) { pSignatureData = p;}
	
	public SpecialDepositAccountData getSpecialDepositAccount() {
		return pSpecialDepositAccount;
	}
	public void setSpecialDepositAccount(
			SpecialDepositAccountData pSpecialDepositAccount) {
		this.pSpecialDepositAccount = pSpecialDepositAccount;
	}
	
	public MobileUserData getMobileUserData() {
		return mobileUserData;
	}
	public void setMobileUserData(MobileUserData mobileUserData) {
		this.mobileUserData = mobileUserData;
	}
	
	public boolean isSigSave() { return isSigSave; }
	public void setSigSave(boolean isSigSave) { this.isSigSave = isSigSave; }
}
